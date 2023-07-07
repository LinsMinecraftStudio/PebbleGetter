package me.mmmjjkx.pebblegetter;

import com.mojang.logging.LogUtils;
import me.mmmjjkx.pebblegetter.lottery.Award;
import me.mmmjjkx.pebblegetter.lottery.LotterySystem;
import mod.cn.mmmjjkx.lins.linsapi.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PebbleGetter implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Item PEBBLE = Registry.register(Registries.ITEM,
            new Identifier("pebblegetter","pebble"), new Item(new FabricItemSettings()));

    private static ConfigHandler config;

    @Override
    public void onInitialize() {
        config = ConfigManager.loadConfig(ConfigHandler.class);
        LOGGER.info("PebbleGetter is loaded!");
        Registry.register(Registries.ITEM_GROUP, new Identifier("pebblegetter", "item_group"),
                FabricItemGroup.builder().entries((displayContext, entries) -> entries.add(PEBBLE))
                        .icon(() -> new ItemStack(PEBBLE))
                        .build()
        );
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            BlockState b = world.getBlockState(hitResult.getBlockPos());
            Identifier id = Registries.BLOCK.getId(b.getBlock());
            if (config.SoundMode == 2){
                if (!config.SoundPath.isBlank()) {
                    player.playSound(SoundEvent.of(new Identifier(config.SoundPath)),
                            config.SoundVolume, config.SoundPitch);
                }
            }
            if (config.AllowBlocks.contains(id.toString())) {
                //play sound
                if (config.SoundMode == 1) {
                    if (!config.SoundPath.isBlank()){
                        player.playSound(SoundEvent.of(new Identifier(config.SoundPath)),
                                config.SoundVolume, config.SoundPitch);
                    }
                }
                //give pebble to player
                if (player.getMainHandStack().isEmpty()) {
                    if (!config.DropThings.isEmpty()){
                        if (config.ShiftRight) {
                            if (player.isSneaking()){return giveItem(player);}
                        }else {return giveItem(player);}
                    }
                    return ActionResult.PASS;
                }
            }
            return ActionResult.PASS;
        }));
    }

    @NotNull
    private ActionResult giveItem(PlayerEntity player) {
        if (config.EnableLottery) {
            Award a = LotterySystem.lottery(getAwards());
            if (a != null) {
                player.dropItem(a.getAwardStack(), false);
            }
        }else {
            giveItems(player);
        }
        return ActionResult.PASS;
    }
    private List<Award> getAwards(){
        List<Award> list = new ArrayList<>();
        for (String str : config.DropThings) {
            String[] s = str.split("\\|");
            Item item = Registries.ITEM.get(new Identifier(s[0]));
            int amount = s[1].isBlank() ? 1 : Integer.parseInt(s[1]);
            double probability = s[2].isBlank() ? 1 : Double.parseDouble(s[2]);
            list.add(new Award(new ItemStack(item, amount), probability));
        }
        return list;
    }

    private void giveItems(PlayerEntity player){
        for (String str : config.DropThings) {
            String[] s = str.split("\\|");
            Item item = Registries.ITEM.get(new Identifier(s[0]));
            int amount = s[1].isBlank() ? 1 : Integer.parseInt(s[1]);
            if (amount <= 0) amount = 1;
            player.dropItem(new ItemStack(item, amount), false);
        }
    }
}
