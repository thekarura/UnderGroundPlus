package com.github.thekarura.bukkit.plugin.undergroundplus.biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Biome;

/**
 * 生成時に使用する保管クラスです。
 * @author the_karura
 */
public class BiomeDate {
	
	private final Biome biome;
	private Map<String, List<BiomeMaterial>> date = new HashMap<String, List<BiomeMaterial>>();
	
	// Options
	private int floot_plant_min = 0; // 地面の植物が生える最小範囲
	private int floot_plant_max = 0; // 地面の植物が生える最大範囲
	private int top_plant_min = 0; // 天井の植物が生える最小範囲
	private int top_plant_max = 0; // 天井の植物が生える最大範囲
	private boolean stripe_layer = false; // ストライプス状の地層生成を許可するか
	private int stripe_layer_min = 0; // 模様の最小範囲
	private int stripe_layer_max = 0; // 模様の最大範囲
	
	/**
	 * 生成用に使用される保管クラスです。
	 * @param biome - 生成時に使用するバイオーム情報
	 */
	public BiomeDate(Biome biome){
		this.biome = biome;
	}
	
	/* =============== *
	 * Setter
	 * =============== */
	
	/**
	 * 生成させるブロック情報を追加します。
	 * @param material - ブロック素材
	 * @param damage - ブロックダメージ値
	 * @param percent - 生成する確立
	 */
	public void addBiomeMaterial(String key, Material material, byte damage, byte percent){
		if (date.get(key) == null){
			date.put(key, new ArrayList<BiomeMaterial>());
		}
		date.get(key).add(new BiomeMaterial(material, damage, percent));
	}
	
	/**
	 * 地面の植物が生える最小範囲を設定します。
	 * @param min 最小範囲
	 */
	public void setFlootPlantMin(int min){
		this.floot_plant_min = min;
	}
	
	/**
	 * 地面の植物が生える最大範囲を設定します。
	 * @param max 最大範囲
	 */
	public void setFlootPlantMax(int max){
		this.floot_plant_max = max;
	}
	
	/**
	 * 天井の植物が生える最小範囲を設定します。
	 * @param min 最小範囲
	 */
	public void setTopPlantMin(int min){
		this.top_plant_min = min;
	}
	
	/**
	 * 天井の植物が生える最大範囲を設定します。
	 * @param max 最大範囲
	 */
	public void setTopPlantMax(int max){
		this.top_plant_max = max;
	}
	
	/**
	 * ストラプス状の地層生成を許可するか設定します。
	 * @param enable trueで地層生成を許可します。
	 */
	public void setStripe_Laier(boolean enable){
		this.stripe_layer = enable;
	}
	
	/**
	 * 模様の最小範囲を指定します。
	 * @param min 最小範囲
	 */
	public void setStripe_Layer_Min(int min){
		this.stripe_layer_min = min;
	}
	
	/**
	 * 模様の最大範囲を指定します。
	 * @param max 最大範囲
	 */
	public void setStripe_Layer_Max(int max){
		this.stripe_layer_max = max;
	}
	
	/* =============== *
	 * Getter
	 * =============== */
	
	/**
	 * 保管しているバイオーム情報を返します。
	 * @return バイオーム情報
	 */
	public Biome getBiome(){
		return this.biome;
	}
	
	/**
	 * 生成させるブロック情報一覧を返します。
	 * @return 保管されたリスト
	 */
	public Map<String, List<BiomeMaterial>> getBiomeMaterial(){
		return this.date;
	}
	
	/**
	 * 地面に生成される植物の最小範囲を返します。
	 * @return 最小範囲
	 */
	public int getFlootPlantMin(){
		return this.floot_plant_min;
	}
	
	/**
	 * 地面に生成される植物の最大範囲を返します。
	 * @return 最大範囲
	 */
	public int getFlootPlantMax(){
		return this.floot_plant_max;
	}
	
	/**
	 * 天井に生成される植物の最小範囲を返します。
	 * @return 最小範囲
	 */
	public int getTopPlantMin(){
		return this.top_plant_min;
	}
	
	/**
	 * 天井に生成される植物の最大範囲を返します。
	 * @return 最大範囲
	 */
	public int getTopPlantMax(){
		return this.top_plant_max;
	}
	
	/**
	 * 縞模様状の地層生成を許可するかを確認します。
	 * @return 許可されている場合はtrue
	 */
	public boolean isStripeLayer(){
		return this.stripe_layer;
	}
	
	/**
	 * 模様の最小範囲を返します。
	 * @return 最小範囲
	 */
	public int getStripeMin(){
		return this.stripe_layer_min;
	}
	
	/**
	 * 模様の最大範囲を返します。
	 * @return 最大範囲
	 */
	public int getStripeMax(){
		return this.stripe_layer_max;
	}
	
	/**
	 * 配置するブロック情報の保管クラスです。
	 * @author the_karura
	 */
	public class BiomeMaterial{
		
		public final Material material;
		public final byte damage;
		public final byte percent;
		public final Material replace;
		
		BiomeMaterial(Material material, byte damage, byte percent){
			this.material = material;
			this.damage = damage;
			this.percent = percent;
			this.replace = null;
		}
		
		BiomeMaterial(Material material, byte damage, byte percent, Material replace){
			this.material = material;
			this.damage = damage;
			this.percent = percent;
			this.replace = replace;
		}
		
	}
	
	public boolean isDefLayer(){
		boolean result = true;
		if (stripe_layer) result = false;
		return result;
	}
	
}
