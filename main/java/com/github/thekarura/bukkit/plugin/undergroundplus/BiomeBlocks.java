package com.github.thekarura.bukkit.plugin.undergroundplus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

/**
 * バイオーム毎に情報を登録するためクラス化
 * @author karura
 */
public class BiomeBlocks {
	
	// ブロック部分を準備
	private List<Material>
	blocks,                  // 地層
	blocks_floot,            // 床
	blocks_floot_plants,     // 床の植物
	blocks_top,              // 天井
	blocks_top_plants;       // 天井の植物
	
	// パーセントデーター
	private List<Byte>
	par_blocks,              // 地層
	par_blocks_floot,        // 床
	par_blocks_floot_plants, // 床の植物
	par_blocks_top,          // 天井
	par_blocks_top_plants;   // 天井の植物
	
	// ダメージデーター
	private List<Byte>
	date_blocks,             // 地層
	date_blocks_floot,       // 床
	date_blocks_floot_plants,// 床の植物
	date_blocks_top ,        // 天井
	date_blocks_top_plants;  // 天井の植物

	public BiomeBlocks() {
		// Block
		blocks =					new ArrayList<Material>();
		blocks_floot =				new ArrayList<Material>();
		blocks_floot_plants =		new ArrayList<Material>();
		blocks_top =				new ArrayList<Material>();
		blocks_top_plants =			new ArrayList<Material>();
		// Damage
		date_blocks =				new ArrayList<Byte>();
		date_blocks_floot =			new ArrayList<Byte>();
		date_blocks_floot_plants =	new ArrayList<Byte>();
		date_blocks_top =			new ArrayList<Byte>();
		date_blocks_top_plants =	new ArrayList<Byte>();
		// Percent
		par_blocks =				new ArrayList<Byte>();
		par_blocks_floot =			new ArrayList<Byte>();
		par_blocks_floot_plants =	new ArrayList<Byte>();
		par_blocks_top =			new ArrayList<Byte>();
		par_blocks_top_plants =		new ArrayList<Byte>();
	}
	
	/* ======================== *
	 * Setter                   *
	 * ======================== */
	
	/**
	 * 地層部分のブロックを追加します。
	 * @param material 追加するブロック
	 * @param percent 生成確立
	 * @param damage ダメージ値
	 */
	public void setBlocks(Material material, Byte percent, Byte damage) {
		set(blocks, date_blocks, par_blocks, material, damage, percent);
	}
	
	/**
	 * 床部分のブロックを追加します。
	 * @param material 追加するブロック
	 * @param percent 生成確立
	 * @param damage ダメージ値
	 */
	public void setBlock_floot(Material material ,Byte percent, Byte damage) {
		set(blocks_floot, date_blocks_floot, par_blocks_floot, material, damage, percent);
	}
	
	/**
	 * 床の装飾ブロックを追加します。
	 * @param material 追加するブロック
	 * @param percent 生成確立
	 * @param damage ダメージ値
	 */
	public void setBlocks_floot_plants(Material material ,Byte percent, Byte damage) {
		set(blocks_floot_plants, date_blocks_floot_plants, par_blocks_floot_plants, material, damage, percent);
	}
	
	/**
	 * 天井部分のブロックを追加します。
	 * @param material 追加するブロック
	 * @param percent 生成確立
	 * @param damage ダメージ値
	 */
	public void setBlocks_top(Material material ,Byte percent, Byte damage) {
		set(blocks_top, date_blocks_top, par_blocks_top, material, damage, percent);
	}
	
	/**
	 * 天井の装飾ブロックを追加します。
	 * @param material 追加するブロック
	 * @param percent 生成確立
	 * @param damage ダメージ値
	 */
	public void setBlocks_top_plants(Material material ,Byte percent, Byte damage) {
		set(blocks_top_plants, date_blocks_top_plants, par_blocks_top_plants, material, damage, percent);
	}
	
	/**
	 * 値代入メソッドです。
	 * @param m  素材リスト
	 * @param d  ダメージリスト
	 * @param p  パーセントリスト
	 * @param m_ 素材
	 * @param d_ ダメージ
	 * @param p_ パーセント
	 */
	private void set(List<Material> m ,List<Byte> d ,List<Byte> p ,Material m_ ,Byte d_ ,Byte p_){
		if ( checkDamage(d_) ){
			d.add(d_);
		} else {
			d.add((byte) 0);
		}
		m.add(m_);
		if ( checkPercent(p_) ){
			p.add(p_);
		} else {
			p.add((byte) 0);
		}
	}
	
	/* ======================== *
	 * Getter                   *
	 * ======================== */
	
	/**
	 * 地層部分のリストを返します。
	 * @return
	 */
	public List<Material> getBlocks() {
		return this.blocks;
	}
	public List<Byte> getBlocksDamage() {
		return this.date_blocks;
	}
	public List<Byte> getBlocksPercent(){
		return this.par_blocks;
	}
	
	/**
	 * 床部分のリストを返します。
	 * @return
	 */
	public List<Material> getBlocks_floot() {
		return this.blocks_floot;
	}
	public List<Byte> getBlocks_flootDamage(){
		return this.date_blocks_floot;
	}
	public List<Byte> getBlocks_flootPercent(){
		return this.par_blocks_floot;
	}
	
	/**
	 * 床の装飾部分のリストを返します。
	 * @return
	 */
	public List<Material> getBlocks_floot_plants() {
		return this.blocks_floot_plants;
	}
	public List<Byte> getBlocks_floot_plantsDamage(){
		return this.date_blocks_floot_plants;
	}
	public List<Byte> getBlocks_floot_plantsPercent(){
		return this.par_blocks_floot_plants;
	}
	
	/**
	 * 天井部分のリストを返します。
	 * @return
	 */
	public List<Material> getBlocks_top() {
		return this.blocks_top;
	}
	public List<Byte> getBlocks_topDamage(){
		return this.date_blocks_top;
	}
	public List<Byte> getBlocks_topPercent(){
		return this.par_blocks_top;
	}
	
	/**
	 * 床の装飾部分のリストを返します。
	 * @return
	 */
	public List<Material> getBlocks_top_plants() {
		return this.blocks_top_plants;
	}
	public List<Byte> getBlocks_top_plantsDamage(){
		return this.date_blocks_top_plants;
	}
	public List<Byte> getBlock_top_plantsPercent(){
		return this.par_blocks_top_plants;
	}
	
	
	/* ======================== *
	 * Checker                  *
	 * ======================== */
	
	/**
	 * ブロックの生成パーセントゲージを調べます。
	 * @param percent
	 * @return
	 */
	private boolean checkPercent(Byte percent){
		if (percent != null){
			if (percent <= 100){ // パーセントゲージが100を超えていないかどうか
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ブロックのダメージ値を調べます。
	 * @param damage
	 * @return
	 */
	private boolean checkDamage(Byte damage){
		if (damage != null){
			if (damage <= 16){ // ダメージ値16をオーバーをしているかどうか
				return true;
			}
		}
		return false;
	}
	
}
