package me.mmmjjkx.pebblegetter;

import com.mojang.logging.LogUtils;
import me.mmmjjkx.pebblegetter.lottery.Award;
import me.mmmjjkx.pebblegetter.lottery.LotterySystem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod(PebbleGetter.MODID)
public class PebbleGetter {
    public static final String MODID = "pebblegetter";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> PEBBLE = ITEMS.register("pebble",() -> new Item(new Item.Properties()));

    public PebbleGetter() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::buildContents);
        modEventBus.addListener(this::register);
        ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,PBConfig.cfg,"PebbleGetter.toml");
        LOGGER.info("PebbleGetter is loaded!");
    }

    @SubscribeEvent
    public void buildContents(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID,"group"), builder ->
                builder.title(Component.translatable("item_group." + MODID + ".group"))
                .icon(() -> new ItemStack(PEBBLE.get()))
                .displayItems((enabledFlags, pop, hasPermissions) -> pop.accept(PEBBLE.get())));
    }

    @SubscribeEvent
    public void register(final FMLClientSetupEvent e){
        e.enqueueWork(() -> MinecraftForge.EVENT_BUS.addListener(this::useBlock));
    }

    @SubscribeEvent
    public void useBlock(PlayerInteractEvent.RightClickBlock e){
        int soundMode = PBConfig.SoundMode.get();
        String sound = PBConfig.SoundPath.get();
        Player p = e.getEntity();
        //play sound
        if (soundMode == 2&&!sound.isBlank()){
            playSound(p,sound);
        }
        List<String> blocks = PBConfig.AllowBlocks.get();
        Block b = p.getLevel().getBlockState(e.getHitVec().getBlockPos()).getBlock();
        //give pebbles
        if (blocks.contains(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(b)).toString())) {
            if (soundMode == 1&&!sound.isBlank()){
                playSound(p,sound);
            }
            if (p.getMainHandItem().isEmpty()) {
                if (PBConfig.ShiftRight.get()){
                    if (p.isShiftKeyDown()) {
                        giveThings(p);
                    }
                }else {
                    giveThings(p);
                }
            }
        }
    }

    private void playSound(Player p,String sound){
       p.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation(sound))
                ,PBConfig.SoundVolume.get(),PBConfig.SoundPitch.get());
    }

    private void giveThings(Player p){
        if (PBConfig.EnableLottery.get()){
            Award a = LotterySystem.lottery(getAwards());
            if (a != null) {
                p.addItem(a.getAwardStack());
            }
        }else {
            List<String> things = PBConfig.DropThings.get();
            for (String thing : things) {
                String[] s = thing.split("\\|");
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s[0]));
                int amount = s.length == 1 || s[1].isBlank() ? 1 : Integer.parseInt(s[1]);
                p.addItem(new ItemStack(item, amount));
            }
        }
    }

    private List<Award> getAwards(){
        List<Award> list = new ArrayList<>();
        for (String str : PBConfig.DropThings.get()) {
            String[] s = str.split("\\|");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s[0]));
            int amount = s[1].isBlank() ? 1 : Integer.parseInt(s[1]);
            double probability = s[2].isBlank() ? 1 : Double.parseDouble(s[2]);
            list.add(new Award(new ItemStack(item, amount), probability));
        }
        return list;
    }
}
