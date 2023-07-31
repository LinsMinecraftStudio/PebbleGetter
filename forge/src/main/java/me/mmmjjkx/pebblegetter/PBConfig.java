package me.mmmjjkx.pebblegetter;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class PBConfig {
    public static final ForgeConfigSpec cfg;
    public static ForgeConfigSpec.ConfigValue<List<String>> AllowBlocks;
    public static ForgeConfigSpec.ConfigValue<List<String>> DropThings;
    public static ForgeConfigSpec.BooleanValue ShiftRight;
    public static ForgeConfigSpec.BooleanValue EnableLottery;
    public static ForgeConfigSpec.IntValue SoundMode;
    public static ForgeConfigSpec.ConfigValue<String> SoundPath;
    public static ForgeConfigSpec.IntValue SoundVolume;
    public static ForgeConfigSpec.IntValue SoundPitch;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("block");
        AllowBlocks = builder.comment("Right-click the allowed blocks to get specified items.").
                define("AllowBlocks",new ArrayList<>());
        DropThings = builder.comment("""
                Set the items dropped after the allowed blocks by right-clicking.
                Format: 'Namespace:ID|Amount|Probability' or 'Namespace:ID|Amount'(The default probability of items without probability is 100%)
                Example: 'pebblegetter:pebble|1|0.21' or 'pebblegetter:pebble|1'""")
                .define("DropThings",new ArrayList<>());
        ShiftRight = builder.comment("You need shift+right-click to get the items when this is true.")
                .define("ShiftRight",false);
        EnableLottery = builder.comment("When you set it to true, there will be a probability lottery for items in 'DropThings'.")
                .define("EnableLottery", false);
        builder.pop();
        builder.push("sound");
        SoundMode = builder.comment("""
                Play sound after right click allowed blocks or every block or not.
                0 is don't play any sound.
                1 is play sound after right click allowed blocks.
                2 is play sound after right click every blocks.""")
                .defineInRange("SoundMode",0,0,2);
        SoundPath = builder.comment("""
                What sound do you want to play?
                If you want to play it out, please change 'soundMode' to 1 or 2.""")
                .define("SoundPath","");
        SoundVolume = builder.comment("Sound volume").
                defineInRange("SoundVolume",3,1,100);
        SoundPitch = builder.comment("Sound pitch").
                defineInRange("SoundPitch",3,1,100);
        builder.pop();
        cfg = builder.build();
    }
}
