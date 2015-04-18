package com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.thekarura.bukkit.plugin.undergroundplus.UnderGroundPlus;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate.BiomeMaterial;

public class TopGenerater extends UGPGenerator {
	
	private final static String key = "tops";
	private final static String plants = "top_plants";
	
	@SuppressWarnings("deprecation")
	@Override
	public void generate(BiomeDate bd, Block block, Random random) {
		
		// 現在ブロックが天井か確認をします。
		if (!isTop(block)) return;
		
		// 生成に使用する保管クラスを呼び出します。
		List<BiomeMaterial> list = bd.getBiomeMaterial().get(key);
		
		// 存在しない要素か確認します。
		if (list == null || list.isEmpty()) return;
		
		// リストの最大数値でgetを使用すると例外が出るので-1で対策
		BiomeMaterial bm = list.get(random.nextInt(list.size()));
		
		// 100%の場合もしくは確立で条件を指定します。
		if (bm.percent == 100 || bm.percent > random.nextInt(100)){
			
			// ブロックを配置します。第二引数は更新防止
			block.setTypeIdAndData(bm.material.getId(), bm.damage, false);
			
			// 天井の植物を取得します。
			List<BiomeMaterial> p_list = bd.getBiomeMaterial().get(plants);
			
			// 天井の植物が存在するか確認します。
			if (p_list == null) return;
			
			// リストの中からランダムに取得します。
			BiomeMaterial p_bm = p_list.get(random.nextInt(p_list.size()));
			
			// パッチを指定します。
			String biome_path = "biomes." + bd.getBiome().name().toLowerCase() + ".set.";
			
			// コンフィグファイルを呼び出します。
			FileConfiguration config = UnderGroundPlus.getInstance().getConfigs().getConfig();
			
			// 最大サイズと最小サイズを指定します。
			int min = config.getInt(biome_path + "top_plants.min", 0);
			int max = config.getInt(biome_path + "top_plants.max", 1);
			
			// ランダムな値から生成する範囲を指定します。
			int g_max = random.nextInt(max) + min + 1;
			
			// 収得した情報からループ処理を実行します。
			for ( int l = 1 ; l < g_max ; l++ ){
				
				// 0座標以上もしくは最大座標以下の場合のみ処理します。
				if (block.getY() - l >= 0 && block.getY() - l <= block.getWorld().getMaxHeight()){
					
					// 指定した床の上を取得します。
					Block p_block = block.getRelative(BlockFace.DOWN, l);
					
					// 空気ブロックでない場合は処理をしない
					if (!p_block.getType().equals(Material.AIR)) return;
					
					// 100%の場合もしくは確立で条件を指定します。
					if (p_bm.percent == 100 || p_bm.percent > random.nextInt(100)){
						
						// 生成します。
						p_block.setTypeIdAndData(p_bm.material.getId(), p_bm.damage, false);
						
					}
					
				}
				
			}
			
			
		}
		
	}
	
	private boolean isTop(Block block){
		
		// 石ブロックか確認します
		if (!block.getType().equals(Material.STONE)) return false;
		
		// 上のブロック情報を習得
		Block down = block.getRelative(BlockFace.DOWN);
		
		// 透過性のブロックかを確認します。(空気、蜘蛛の巣、線路など)
		if (down.getType().isSolid()) return false;
		
		return true;
		
	}
	
}
