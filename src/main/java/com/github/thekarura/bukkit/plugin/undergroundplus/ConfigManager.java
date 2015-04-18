package com.github.thekarura.bukkit.plugin.undergroundplus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;
import com.github.thekarura.bukkit.plugin.undergroundplus.util.FileHandler;
import com.github.thekarura.bukkit.plugin.undergroundplus.util.Log;

public class ConfigManager {
	
	private FileConfiguration config;
	private final File plugin_folder = new File("plugins");
	private final File config_folder = new File(plugin_folder, "UnderGroundPlus");
	private final File config_file = new File(config_folder, "config.yml");
	
	private boolean enable;
	
	// ## BiomeDate 情報 ## //
	private int max_heigth;
	private int min_heigth;
	private int gradation_heigth;
	private final String[] dates = {"layers", "floots", "floot_plants", "tops", "top_plants"};
	private Map<Biome, BiomeDate> biomes;
	
	// ## BottomDate 情報 ## //
	private boolean bottom;
	private int bottom_heigth;
	
	public void load(){
		
		// ファイル生成
		FileHandler.createDirectory(config_folder);
		FileHandler.inJarFileCopy(config_folder, "config.yml");
		config = YamlConfiguration.loadConfiguration(config_file);
		
		// コンフィグ読み込み
		this.enable = config.getBoolean("enable", true); // プラグインを有効化させます。
		
		this.max_heigth = config.getInt("heigth.max", 256);           // 地層生成の最大範囲を指定します。
		this.min_heigth = config.getInt("heigth.min", 20);            // 地層生成の最小範囲を指定します。
		this.gradation_heigth = config.getInt("heigth.gradation", 5); // 地層のグラデーションを指定します。
		this.biomes = getBiomeDates(); // バイオームデーターを読み込みます。
		
		// N/A this.bottom = config.getBoolean("bottm.enable", false);  // 最下層生成を有効化させます。
		// N/A this.bottom_heigth = config.getInt("bottom.heigth", 10); // 最下層を生成する高さ
		
	}
	
	private Map<Biome, BiomeDate> getBiomeDates(){
		
		// 保管するを生成
		Map<Biome, BiomeDate> biomedates = new HashMap<Biome, BiomeDate>();
		
		// ロードするバイオームをループします。
		for (Biome biome : Biome.values()){
			
			// キーを省略します。
			String path = "biomes." + biome.name().toLowerCase();
			
			// 存在しないキーであれば処理をしない
			if (config.get(path) == null) continue;
			
			// BiomeDateクラスを生成します。
			BiomeDate bd = new BiomeDate(biome);
			
			// ロードする内容分をループさせます。
			for (String date : dates){
				
				// キーを省略します。
				String date_path = path + "." + date;
				
				// 存在しなければ処理をしない
				if (config.get(path + "." + date) == null) continue;
				
				// リスト内にある文字列分をループさせます。
				for (String blocks : config.getStringList(date_path)){
					
					String[] par = blocks.split("[\\s]+");
					
					Material material = Material.getMaterial(par[0].toUpperCase());
					byte damage = 0;
					byte percent = 100;
					
					if (material == null){
						Log.info("該当するMaterialがありません。 : " + blocks);
						continue;
					}
					
					if (par.length >= 2){
						
						try {
							damage = new Byte(par[1]);
						} catch (NumberFormatException e) {
							Log.info("不正な引数です。数値を指定してください。 : " + blocks);
							continue;
						}
						
						if (damage < 0 && damage < 16){
							Log.info("damage値を0～16以内に指定してください。 : " + blocks);
							continue;
						}
						
						if (par.length >= 3){
							
							try {
								percent = new Byte(par[2]);
							} catch (NumberFormatException e) {
								Log.info("不正な引数です。数値を指定してください。 : " + blocks);
								continue;
							}
							
							if (percent < 0 && percent < 100){
								Log.info("percent値を0～100以内に指定してください。 : " + blocks);
								continue;
							}
							
						}
						
					}
					
					// 追加します。
					bd.addBiomeMaterial(date, material, damage, percent);
					
				}
				
			}
			
			biomedates.put(biome, bd);
			
		}
		
		return biomedates;
		
	}
	
	public boolean isEnable(){
		return this.enable;
	}
	
	public int getMaxHeigth(){
		return this.max_heigth;
	}
	
	public int getMinHeigth(){
		return this.min_heigth;
	}
	
	public int getGradationHeigth(){
		return this.gradation_heigth;
	}
	
	public boolean isBottom(){
		return this.bottom;
	}
	
	public int getBottomHeigth(){
		return this.bottom_heigth;
	}
	
	public Map<Biome, BiomeDate> getBiomeDate(){
		return this.biomes;
	}
	
	public BiomeDate getBiomeDates(Biome biome){
		return this.biomes.get(biome);
	}
	
	public FileConfiguration getConfig(){
		return config;
	}
	
}
