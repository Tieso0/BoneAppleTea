package com.tieso2001.boneappletea.world;

import com.tieso2001.boneappletea.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GrassSeeds {

    public static void register() {
        addGrassSeed(ModItems.BARLEY_SEEDS, 2);
        addGrassSeed(ModItems.CORN_KERNELS, 2);
    }

    private static void addGrassSeed(Item seed, int weight) {
        MinecraftForge.addGrassSeed(new ItemStack(seed), weight);
    }

}