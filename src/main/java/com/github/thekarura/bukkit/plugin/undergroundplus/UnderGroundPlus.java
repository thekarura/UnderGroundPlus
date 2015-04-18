package com.github.thekarura.bukkit.plugin.undergroundplus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.thekarura.bukkit.plugin.undergroundplus.biome.BiomeDate;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator.FlootGenerater;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator.LayerGenerater;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator.TopGenerater;
import com.github.thekarura.bukkit.plugin.undergroundplus.biome.generator.UGPGenerator;
import com.github.thekarura.bukkit.plugin.undergroundplus.populator.UGPopulator;

public class UnderGroundPlus extends JavaPlugin implements Listener {
	
	public final static String prefix = "[UGP] ";
	
	private static UnderGroundPlus instance; // getInstanceメソッド用の変数
	private ConfigManager config;            // getConfigsメソッド用変数
	
	private static List<World> trigger_world = new ArrayList<World>(); // 軽減用に使用するリスト
	private static List<UGPGenerator> ugpgs = getUGPGenerate();        // 追加する処理リスト
	
	/**
	 * Pluginが呼び出される時の処理
	 * ここで起動後に必要な処理を登録する。
	 */
	@Override
	public void onEnable(){
		
		// 静的な変数へ代入
		instance = this;
		
		// クラス登録処理
		config = new ConfigManager();
		
		// コンフィグロード用の処理
		config.load();
		
		// イベント登録処理 onWorldLoadEventメソッド用
		getServer().getPluginManager().registerEvents(this, this);
		
		// 正常に起動出来た時の通知
		getLogger().info("plugin has enabled!");;
		
	}
	
	/**
	 * Pluginが停止される時の処理
	 */
	@Override
	public void onDisable(){
		
		// 停止した通知
		getLogger().info("plugin has disabed!");
		
	}
	
	/**
	 * コマンドが実行される時に呼び出されるメソッド
	 * @return falseを返すとplugin.ymlのcommands.UnderGroundPlus.usageが表示されます。
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		// コマンドが"UnderGroundPlusになっているか確認します。
		if (cmd.getName().equalsIgnoreCase("UnderGroundPlus")){
			
			// 引数がreloadを入れられている場合
			if (args.length >= 1 && args[0].equalsIgnoreCase("reload")){
				
				// リロード処理
				config.load();
				
				// コンフィグから文字列を読み取り
				String message = prefix + config.getConfig().getString("command.reload.message");
				
				// カラーフォマットをします。
				message = ChatColor.translateAlternateColorCodes('&', message);
				
				// メッセージを送信
				sender.sendMessage(message);
				
				return true;
				
			}
			
			if (args.length >= 1 && args[0].equalsIgnoreCase("pop")){
				
				for (World world : Bukkit.getWorlds()){
					
					for (BlockPopulator bp : world.getPopulators()){
						
						sender.sendMessage(bp.getClass().getSimpleName());
						
					}
					
				}
				
				return true;
				
			}
			
			if (args.length >= 1 && args[0].equalsIgnoreCase("date")){
				
				for (Entry<Biome, BiomeDate> s : config.getBiomeDate().entrySet()){
					
					sender.sendMessage("biome: " + s.getKey().name());
					sender.sendMessage("size: " + s.getValue().getBiomeMaterial().get("layers").size());
					
				}
				
				return true;
				
			}
			
			// 該当が内場合はヘルプを表示します。
			else {
				
				// コンフィグから文字列を読み取りループさせます。
				for (String message : config.getConfig().getStringList("command.help.message")){
					
					String mes = prefix + message;
					
					// カラーフォマットをします。
					mes = ChatColor.translateAlternateColorCodes('&', mes);
					
					// メッセージを送信
					sender.sendMessage(mes);
					
				}
				
				return true;
				
			}
			
		}
		
		return false;
	}
	
	/**
	 * ワールドがロードされる時に呼び出されるイベント
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChunkPopulateEvent(ChunkPopulateEvent event){
		
		// イベントから呼び出されたワールドクラスを代入
		World world = event.getWorld();
		
		// Populator追加処理(詳しい解説はメソッドに書いてます)
		addPopulator(world);
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		
		// プレイヤーを取得
		Player player = event.getPlayer();
		
		// UUIDからユーザーを判定(製作者のUUIDを判定)
		if (player.getUniqueId().toString().equals("db113963-10b7-41ec-b8a9-7d9fffd0cfc7")){
			
			player.sendMessage("§a[UnderGroundPlus] このサーバーはUnderGroundPlusを使用しています。");
			
		}
		
	}
	
	private void addPopulator(World world){
		
		// Configで無効化しれいてば処理を止める
		if (!config.getConfig().getBoolean("enable")) return;
		
		// Configで指定したworlds一覧から呼び出されたワールド名を比較させます。
		if (config.getConfig().getStringList("worlds").contains(world.getName())){
			
			// もし既に追加されていれば処理をしない
			if (trigger_world.contains(world)) return;
			
			// 生成変更を行います。
			List<BlockPopulator> populators = world.getPopulators();
			
			populators.add(new UGPopulator());
			
			// PopulatorはBukkitが提供しているワールド生成をカスタマイズしやすくする機能の一つです。
			// ワールドに配置されているダンジョン、村、遺跡、廃坑など
			// 形がある程度決まった地形を生成させる場合などにPopulatorクラスが利用されます。
			
			// 処理トリガー
			trigger_world.add(world);
			
		}
		
	}
	
	private static List<UGPGenerator> getUGPGenerate(){
		List<UGPGenerator> ugpgs = new ArrayList<UGPGenerator>();
		// 優先度の高い順から並べてください
		ugpgs.add(new FlootGenerater()); // 地面生成クラス
		ugpgs.add(new TopGenerater());   // 天井クラス
		ugpgs.add(new LayerGenerater()); // 地層生成クラス
		return ugpgs;
	}
	
	/**
	 * このクラスを返します。
	 * @return staticなUnderGroundPlusクラス
	 */
	public static UnderGroundPlus getInstance(){
		return instance;
	}
	
	/**
	 * PluginのJarファイルを返します。
	 * @return jarファイルが置いてあるファイル階層
	 */
	public static File getPluginJarFile(){
		return instance.getFile();
	}
	
	/**
	 * ConfigManagerクラスを返します。
	 * @return ConfigManager
	 */
	public ConfigManager getConfigs(){
		return config;
	}
	
	public static List<UGPGenerator> getGenerators(){
		return ugpgs;
	}
	
}
