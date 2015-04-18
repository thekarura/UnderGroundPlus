package com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate.BiomeMaterial;

public class FlootGenerater extends UGPGenerator {
	
	private final static String key = "floots";
	private final static String plants = "floot_plants";
	
	@SuppressWarnings("deprecation")
	@Override
	public void generate(BiomeDate bd, Block block, Random random) {
		
		// 現在ブロックが地面か確認をします。
		if (!isFloot(block)) return;
		
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
			
			// 床の植物を取得します。
			List<BiomeMaterial> p_list = bd.getBiomeMaterial().get(plants);
			
			// ない場合は処理しません。
			if (p_list == null) return;
			
			// リストの中からランダムに取得します。
			BiomeMaterial p_bm = p_list.get(random.nextInt(p_list.size()));
			
			// 指定した床の上を取得します。
			Block p_block = block.getRelative(BlockFace.UP);
			
			// 空気でない場合は処理しません。
			if (!p_block.getType().equals(Material.AIR)) return;
			
			// 100%の場合もしくは確立で条件を指定します。
			if (p_bm.percent == 100 || p_bm.percent > random.nextInt(100)){
				
				// 生成します。
				p_block.setTypeIdAndData(p_bm.material.getId(), p_bm.damage, false);
				
			}
			
		}
		
	}
	
	private boolean isFloot(Block block){
		
		// 石ブロックか確認します
		if (!block.getType().equals(Material.STONE)) return false;
		
		// 上のブロック情報を習得
		Block up = block.getRelative(BlockFace.UP);
		
		// 透過性のブロックかを確認します。(空気、蜘蛛の巣、線路など)
		if (up.getType().isSolid() && 
				!up.getType().equals(Material.RAILS) && 
				!up.getType().equals(Material.FENCE)) return false;
		
		// 上が水ブロックか確認します。
		if (up.getType().equals(Material.STATIONARY_WATER)) return false;
		
		// 上が溶岩ブロックか確認します。
		if (up.getType().equals(Material.STATIONARY_LAVA)) return false;
		
		return true;
	}
	
}
