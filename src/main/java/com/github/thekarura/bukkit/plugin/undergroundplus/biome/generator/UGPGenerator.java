package com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator;

import java.util.Random;

import org.bukkit.block.Block;

import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;

public abstract class UGPGenerator {
	
	public abstract void generate(BiomeDate bd, Block block, Random random);
	
}
