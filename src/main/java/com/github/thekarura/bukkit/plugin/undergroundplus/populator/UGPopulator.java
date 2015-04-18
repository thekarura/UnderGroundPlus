package com.github.thekarura.bukkit.plugin.undergroundplus.populator;

import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import com.github.thekarura.bukkit.plugin.undergroundplus.ConfigManager;
import com.github.thekarura.bukkit.plugin.undergroundplus.UnderGroundPlus;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator.UGPGenerator;

/**
 * ワールド生成用に提供されているBlockPopulatorを使ったクラスです。
 * @author karura
 */
public class UGPopulator extends BlockPopulator {
	
	private final ConfigManager config = UnderGroundPlus.getInstance().getConfigs();
	private final List<UGPGenerator> ugpgs = UnderGroundPlus.getGenerators();
	
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		
		// Chunkのx軸最大までループさせます。
		for ( int x = 0 ; x <= 16  ; x++ ){
			
			// Chunkのz軸最大までループさせます。
			for ( int z = 0; z <= 16 ; z++ ){
				
				int max_heigth = config.getMaxHeigth();
				int min_heigth = config.getMinHeigth();
				int gradation = random.nextInt(config.getGradationHeigth());
				
				// Chunkのy軸最大までループさせます。
				for ( int y = max_heigth ; y > min_heigth + gradation ; y-- ){
					
					// 処理をするブロックを代入します。
					Block block = chunk.getBlock(x, y, z);
					
					// ブロックがあるバイオーム情報を代入します。
					Biome biome = block.getBiome();
					
					// バイオーム情報から保管していた情報を代入します。
					BiomeDate bd = UnderGroundPlus.getInstance().getConfigs().getBiomeDates(biome);
					
					// 読み込んだBiomeDateの情報がnullの確認
					if (bd == null) continue;
					
					// 追加要素を反映しやすく拡張forを利用します。
					for (UGPGenerator generator : ugpgs){
						
						// 拡張forに登録されたUGPGeneratorを継承した
						// クラスのメソッドを実行させます。
						generator.generate(bd, block, random);
						
					}
					
				}
			}
		}
	}
	
}
