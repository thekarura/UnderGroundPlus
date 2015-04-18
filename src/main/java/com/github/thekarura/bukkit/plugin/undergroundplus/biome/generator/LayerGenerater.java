package com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate.BiomeMaterial;

public class LayerGenerater extends UGPGenerator {
	
	private final static String key = "layers";
	
	@SuppressWarnings("deprecation")
	@Override
	public void generate(BiomeDate bd, Block block, Random random) {
		
		if (bd.isDefLayer()){
			
			// 現在ブロックが石ブロックか確認
			if (!block.getType().equals(Material.STONE)) return;
			
			// 生成に使用する保管クラスを呼び出します。
			List<BiomeMaterial> list = bd.getBiomeMaterial().get(key);
			
			// 存在しない要素か確認します。
			if (list == null || list.isEmpty()) return;
			
			// リストからランダムに収得
			BiomeMaterial bm = list.get(random.nextInt(list.size()));
			
			// 100%の場合もしくは確立で条件を指定します。
			if (bm.percent == 100 || bm.percent > random.nextInt(100)){
				
				// ブロックを配置します。第二引数は更新防止
				block.setTypeIdAndData(bm.material.getId(), bm.damage, false);
				
			}
			
			return;
			
		}
		
		if (bd.isStripeLayer()){
			
			// 現在ブロックが石ブロックか確認
			if (!block.getType().equals(Material.STONE)) return;
			
			// 生成に使用する保管クラスを呼び出します。
			List<BiomeMaterial> list = bd.getBiomeMaterial().get(key);
			
			// 存在しない要素か確認します。
			if (list == null || list.isEmpty()) return;
			
			// リストからランダムに収得
			BiomeMaterial bm = list.get(random.nextInt(list.size()));
			
			// 100%の場合もしくは確立で条件を指定します。
			if (bm.percent == 100 || bm.percent > random.nextInt(100)){
				
				// ブロックを配置します。第二引数は更新防止
				block.setTypeIdAndData(bm.material.getId(), bm.damage, false);
				
			}
			
			return;
			
		}
		
	}
	
}
