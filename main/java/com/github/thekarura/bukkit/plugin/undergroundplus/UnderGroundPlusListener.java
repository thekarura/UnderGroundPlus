package com.github.thekarura.bukkit.plugin.undergroundplus;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.world.ChunkPopulateEvent;

/**
 * 
 * コンフィグ情報と宣言関連を扱うためクラス化
 * 
 * @author karura
 *
 */
public class UnderGroundPlusListener {
	
	// メイン継承
	private UnderGroundPlus instance;
	private FileConfiguration config;
	
	// ブロック情報
	private Map<Biome, BiomeBlocks> id;
	
	// イベント情報
	private World world;
	private Chunk chunk;
	
	// ランダマイズ情報
	private Random random;
	
	// コンストラクタ
	public UnderGroundPlusListener(UnderGroundPlus plugin, ChunkPopulateEvent event) {
		this.instance = plugin;
		this.config = instance.getConfig();
		this.id = instance.getBiomeBlocks();
		world = event.getWorld();
		chunk = event.getChunk();
		random = new Random(world.getSeed()); // ワールドSeedに合わせます。
		main();
	}
	
	/**
	 * 処理部分
	 */
	private void main() {
		
		// 該当するワールドかを調べます。
		if (!checkWorlds()) { return; }
		
		// チャンク内のブロック全体分ループさせます。
		for (int cx = 0; cx < 16; cx++) {
			for (int cz = 0; cz < 16; cz++) {
				for (int y = world.getMaxHeight(); y != 0; y--) {
					
					setFloot(cx ,y ,cz);
					setTop(cx ,y ,cz);
					setLayer(cx ,y ,cz);
					
				}
			}
		}
		
	}
	
	/**
	 * 洞窟の地層を変更します。
	 * @param x
	 * @param y
	 * @param z
	 */
	@SuppressWarnings("deprecation")
	private void setLayer(int x ,int y ,int z) {
		
		// 簡略化用変数
		Block block = chunk.getBlock(x, y, z);
		Biome biome = block.getBiome();
		
		// ブロック情報を登録
		List<Material> blocks = id.get(biome).getBlocks();
		List<Byte> blocks_date= id.get(biome).getBlocksDamage();
		//List<Byte> blocks_per = id.get(biome).getBlocksPercent();
		
		// 登録した情報がnullであれば無視する。
		if ( blocks.size() != 0) {
			
			int max = random.nextInt(blocks.size());
			
			// 石ブロックであれば変更する
			if (block.getType().equals(Material.STONE)) {
				
				block.setTypeIdAndData(blocks.get(max).getId(), blocks_date.get(max), false);
				
			}
			
		}
		
	}
	
	/**
	 * 洞窟の床を変更します。
	 * @param x
	 * @param y
	 * @param z
	 */
	@SuppressWarnings("deprecation")
	private void setFloot(int x ,int y, int z){
		
		// 簡略化用変数
		Block block = chunk.getBlock(x, y, z);
		Biome biome = block.getBiome();
		
		if (getFloot(block)){
			
			// ## 地面生成 ## //
			
			// ブロック情報を登録
			List<Material> blocks = id.get(biome).getBlocks_floot();
			List<Byte> blocks_date= id.get(biome).getBlocks_flootDamage();
			//List<Byte> blocks_per = id.get(biome).getBlocks_flootPercent();
			
			if ( blocks.size() != 0 ){
				
				int max = random.nextInt(blocks.size());
				
				Material material = blocks.get(max);
				byte date = blocks_date.get(max);
				//byte par = blocks_per.get(max);
				
				block.setTypeIdAndData(material.getId(), date, false);
				
			}
			
			// ## 植物生成 ## //
			
			List<Material> blocks_ = id.get(biome).getBlocks_floot_plants();
			List<Byte> blocks_date_= id.get(biome).getBlocks_floot_plantsDamage();
			//List<Byte> blocks_par_ = id.get(biome).getBlocks_floot_plantsPercent();
			
			if ( blocks_.size() != 0 ){
				
				int max = random.nextInt(blocks_.size());
				
				Material material = blocks_.get(max);
				byte damage = blocks_date_.get(max);
				//byte parcent_ = blocks_par_.get(max_);
				
				if (block.getRelative(BlockFace.UP).getType().equals(Material.AIR)){
					
					block.getRelative(BlockFace.UP).setTypeIdAndData(material.getId(), damage, false);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * 洞窟内の地面を検出します。
	 * @param block
	 * @return
	 */
	private boolean getFloot(Block block){
		
		if (block.getType().equals(Material.STONE)){
			if (block.getRelative(BlockFace.UP).getType().equals(Material.AIR)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 洞窟の天井を変更します。
	 * @param x
	 * @param y
	 * @param z
	 */
	@SuppressWarnings("deprecation")
	private void setTop(int x ,int y ,int z){
		
		Block block = chunk.getBlock(x, y, z);
		Biome biome = block.getBiome();
		
		if (getTop(block)){
			
			List<Material> blocks = id.get(biome).getBlocks_top();
			List<Byte> blocks_date= id.get(biome).getBlocks_topDamage();
			//List<Byte> blocks_par = id.get(biome).getBlocks_topPercent();
			
			if ( blocks.size() != 0 ){
				
				int max = random.nextInt(blocks.size());
				
				// ## 天井関連 ## //
				
				Material material = blocks.get(max);
				byte damage = blocks_date.get(max);
				//byte par = blocks_par.get(max);
				
				block.setTypeIdAndData(material.getId(), damage, false);
				
			}
			
			// ## 植物関連 ## //
			
			List<Material> blocks_ = id.get(biome).getBlocks_top_plants();
			List<Byte> blocks_date_= id.get(biome).getBlocks_top_plantsDamage();
			//List<Byte> blocks_par_ = id.get(biome).getBlocks_top_plantsPercent();
			
			// 
			if ( blocks_.size() != 0 ){
				
				// ランダム設定
				String path = "biome." + biome.name()  +".set.top_plants";
				int height = random.nextInt(config.getInt(path+".max", 0))+config.getInt(path+".min", 1);
				
				int max = random.nextInt(blocks_.size());
				
				Material material = blocks_.get(max);
				byte damage = blocks_date_.get(max);
				//byte parcent_ = blocks_par_.get(max_);
				
				// configから設定した分の高さを下へ垂らします。
				for ( int l = height + 1 ; l != -1 ; l-- ){
					
					if (!block.getRelative(BlockFace.DOWN, l).getType().equals(Material.AIR)){ break; }
					
					block.getRelative(BlockFace.DOWN, l).setTypeIdAndData(material.getId(), damage, false);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * 洞窟内の天井を検出します。
	 * @param block
	 * @return
	 */
	private boolean getTop(Block block){
		
		if (block.getType().equals(Material.STONE)){
			if (block.getType().isSolid()){
				if (!block.getRelative(BlockFace.DOWN).getType().isSolid()){
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	
	
	/**
	 * 該当するワールドを検索します。
	 * @param world 検索するワールド
	 * @return 該当する場合はtrueを返します。
	 */
	private boolean checkWorlds() {
		for (String world_ : instance.getWorlds()) {
			if (world_.equals(world.getName())) {
				return true;
			}
		}
		return false;
	}
	
}
