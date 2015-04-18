package com.github.thekarura.bukkit.plugin.undergroundplus.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

/**
 * リフレクション操作関連のクラスです。
 * @author karura
 */
public class MinecraftReflection {
	
	private final Class<?> class_;
	
	/**
	 * net.minecraft.server内のパッケージのクラスを習得します。
	 * @param class_name - パッケージ内にクラス名
	 * @return クラス名から指定したクラス
	 * @throws Exception - ClassクラスのforNameが出す例外
	 */
	public MinecraftReflection(String class_name) throws Exception{
		String cb_package = Bukkit.getServer().getClass().getPackage().getName();
		String cb_version = cb_package.split("\\.")[3];
		class_ = Class.forName("net.minecraft.server." + cb_version + "." + class_name);
	}
	
	/**
	 * このクラスが持つClassメソッドを習得します。
	 * @param method_name - メソッド名
	 * @param args - メソッドの引数
	 * @return メソッド名から指定したメソッド
	 * @throws Exception - ClassクラスのgetMethodが出す例外
	 */
	public Method getMinecraftMethod(String method_name, Class<?>... args) throws Exception {
		return class_.getMethod(method_name, args);
	}
	
	/**
	 * このクラスが持つClassメソッドのフィールドを習得します。
	 * @param field_name - フィールド名
	 * @return フィールド名から指定したフィールド
	 * @throws Exception - ClassクラスのgetFieldが出す例外
	 */
	public Field getMinecraftField(String field_name) throws Exception {
		return class_.getField(field_name);
	}
	
	/**
	 * このクラスが持つnet.minecraft.server内パッケージのクラスを返します。
	 * @return
	 */
	public Class<?> getMinecraftClass(){
		return this.class_;
	}
	
}
