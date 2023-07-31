package me.mmmjjkx.pebblegetter;

import net.minecraft.world.item.ItemStack;

public class Award{
    public Award(ItemStack is, double probability){
        this.awardStack = is;
        this.probability = probability;
    }
    private final ItemStack awardStack;
    private final double probability;
    public ItemStack getAwardStack() {
        return awardStack;
    }
    public double getProbability() {
        return probability;
    }
}