package me.mmmjjkx.pebblegetter;

import blue.endless.jankson.Comment;
import mod.cn.mmmjjkx.lins.linsapi.config.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name = "PebbleGetter")
public class ConfigHandler {
    @Comment("Right-click the allowed blocks to get specified items.")
    public List<String> AllowBlocks = new ArrayList<>();
    @Comment("""
            Set the items dropped after the allowed blocks by right-clicking.
            Format: 'Namespace:ID|Amount|Probability' or 'Namespace:ID|Amount'(The default probability of items without probability is 100%)
            Example: 'pebblegetter:pebble|1|0.21' or 'pebblegetter:pebble|1'
            """)
    public List<String> DropThings = new ArrayList<>();
    @Comment("You need shift+right-click to get the items when this is true.")
    public boolean ShiftRight = false;

    @Comment("When you set it to true, there will be a probability lottery for items in 'DropThings'.")
    public boolean EnableLottery = false;

    @Comment("""
                Play sound after right click allowed blocks or every block or not.
                0 is don't play any sound.
                1 is play sound after right click allowed blocks.
                2 is play sound after right click every block.
                """)
    public int SoundMode = 0;
    @Comment("""
                What sound do you want to play?
                If you want to play it out, please change 'soundMode' to 1 or 2.
                """)
    public String SoundPath = "";
    @Comment("Sound volume")
    public int SoundVolume = 3;
    @Comment("Sound pitch")
    public int SoundPitch = 3;
}
