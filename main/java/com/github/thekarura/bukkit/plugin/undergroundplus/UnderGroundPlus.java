package com.github.thekarura.bukkit.plugin.undergroundplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.thekarura.bukkit.plugin.undergroundplus.util.FileHandler;

/**
 * UnderGroundPlus
 * @author karura
 */
public class UnderGroundPlus extends JavaPlugin implements Listener {
	
	// インスタンスを準備
	private static UnderGroundPlus instance;
	private BiomeBlocks biomeblocks;
	
	// Loggerの準備
	public static Logger log = Logger.getLogger("UnderGroundPlus");
	public static String logPrefix = "[UnderGroundPlus] ";
	
	// バイオームごとに保存する変数を準備
	private Map<Biome, BiomeBlocks> id;
	
	// 許可ワールド一覧
	private List<String> worlds;
	
	// ファイル関連
	private File conf_dir = new File("plugins/UnderGroundPlus");
	private File conf_fil = new File(conf_dir.toString() + "/config.yml");
	
	// 起動時に呼び出されるメソッド
	@Override
	public void onEnable() {
		
		// インスタンスを登録
		instance = this;
		
		// コンフィグ読み込み
		try {
			configLoad();
		} catch (IOException e) {
			e.printStackTrace();
			log.warning(logPrefix + e.getMessage());
		}
		
		// イベント登録
		getServer().getPluginManager().registerEvents(this, this);
		
		log.info(logPrefix + "plugin has Enabled!");
	}
	
	// 停止時に呼び出されるメソッド
	public void onDisable() {
		log.info(logPrefix + "plugin has Disabled!");
	}
	
	// コンフィグの読み込み
	private void configLoad() throws IOException {
		
		// ============================== //
		// ## コンフィグのセットアップ ## //
		// ============================== //
		
		// コンフィグの変更を確認
		reloadConfig();
		
		// コンフィグの変数を登録
		FileConfiguration config = getFileConfigration();
		
		// ディレクトリーがなければ作成する
		if (!conf_dir.exists()) {
			conf_dir.mkdirs();
		}
		
		// コンフィグファイルがなければjarから読み取る
		if (!conf_fil.exists()) {
			FileHandler.inJarFileCopy(conf_dir, "config.yml", false);
		}
		
		// ============================ //
		// ## ブロック登録関連の処理 ## //
		// ============================ //
		
		// ハッシュマップを初期化
		id = new HashMap<Biome, BiomeBlocks>();
		
		// バイオームごとに習得します。
		for (Biome biome : Biome.values()) {
			
			//クラスを使ったブロック登録
			biomeblocks = new BiomeBlocks();
			
			// バイオーム名を習得
			String biome_name = biome.name();
			
			String[] types = {
					"layer",
					"floot",
					"floot_plants",
					"top",
					"top_plants"
			};
			
			for (String type : types) {
				
				List<String> blocks = config.getStringList("biome." + biome_name + "." + type);
				
				// 各種設定を確認します。
				if (blocks != null) {
					
					for (String blocks_ : blocks) {
						
						String[] args = blocks_.split("\\:");
						
						Material material = Material.getMaterial(args[0].toUpperCase());
						
						// 用意
						Byte damage = 0;   // ダメージ値
						Byte percent= 100; // パーセントゲージ
						
						// 数字ではない値が代入される可能性を配慮
						try {
							
							if (args.length > 1){
								
								damage = new Byte(args[1]);
								
								/* TODO: 未実装
								if (args.length > 2){
									
									percent = new Byte(args[2]);
									
								}
								*/
								
							}
							
						} catch (NumberFormatException ex) {
							
							log.info(logPrefix+"ConvertErroe: config[" + blocks.toString() + "] data is not number!");
							
						}
						
						if (material != null && material.isBlock()) {
							
							if (type.equals("layer")) {
								biomeblocks.setBlocks(material , percent ,damage);
							}
							
							if (type.equals("floot")) {
								biomeblocks.setBlock_floot(material , percent ,damage);
							}
							
							if (type.equals("floot_plants")){
								biomeblocks.setBlocks_floot_plants(material , percent ,damage);
							}
							
							if (type.equals("top")) {
								biomeblocks.setBlocks_top(material , percent ,damage);
							}
							
							if (type.equals("top_plants")){
								biomeblocks.setBlocks_top_plants(material , percent ,damage);
							}
							
						}
						
					}
					
				}
				
			}
			
			id.put(biome, biomeblocks);
			
		}
		
		// ============================ //
		// ## 許可ワールド関連の処理 ## //
		// ============================ //
		
		// 許可ワールドのリストを初期化
		worlds = new ArrayList<String>();
		
		// コンフィグから文字列リストを習得
		List<String> world = config.getStringList("worlds");
		
		// コンフィグに該当があるかどうか
		if (!(world.size() <= 0)) {
			
			// リスト分ループさせます。
			for (String world_ : world) {
				
				// 代入したワールドをリスト化
				worlds.add(world_);
				
			}
			
		} else {
			
			// ない場合はインフォを出して終了
			log.info(logPrefix + "Allow Worlds Not Found.");
			
		}
		
		// コンフィグ情報をセーブさせます。
		saveConfig();
		
		log.info(logPrefix + "config is loaded.");
		
	}
	
	/**
	 * チャンク読み込み時に発生するイベント
	 * チャンク生成時に呼び出されます。
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChunkPopulateEvent(ChunkPopulateEvent event) {
		new UnderGroundPlusListener(this, event);
	}
	
	// リロード用コマンド
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("UnderGroundPlus")) {
			
			if (args.length == 0) {
				
				sender.sendMessage(logPrefix + "config reloading is \"/UnderGroundPlus reload\"");
				return true;
				
			}
			
			if (args[0].equalsIgnoreCase("reload")) {
				
				// コンフィグ読み込み
				try {
					configLoad();
				} catch (IOException e) {
					e.printStackTrace();
					log.warning(logPrefix + e.getMessage());
				}
				
				return true;
				
			}
			
		}
		
		return true;
	}
	
	/**
	 * BukkitAPIのコンフィグを返します。
	 * @return
	 */
	public FileConfiguration getFileConfigration() {
		return this.getConfig();
	}
	
	/**
	 * 登録したバイオームリストを返します。
	 * @return
	 */
	public Map<Biome, BiomeBlocks> getBiomeBlocks() {
		return this.id;
	}
	
	/**
	 * 許可するワールドリストを習得します。
	 * @return
	 */
	public List<String> getWorlds() {
		return this.worlds;
	}
	
	/**
	 * pluginのjarを返します。
	 * @return
	 */
	public static File getPluginJarFile() {
		return instance.getFile();
	}
	
}
