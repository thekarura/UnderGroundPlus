package com.github.thekarura.bukkit.plugin.undergroundplus.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.github.thekarura.bukkit.plugin.undergroundplus.UnderGroundPlus;

public class FileHandler {
	
	private static String Encode = "UTF-8";

	/**
	 * ディレクトリーを生成します。
	 * @param dir
	 * @return 生成された場合trueをします
	 */
	public static boolean createDirectory(File dir){
		if (!dir.exists()){
		if (dir.mkdirs()){
			return false;
		} } return true;
	}

	/**
	 * ファイルを生成します。
	 * @param file
	 * @return 生成された場合trueを返します
	 */
	public static boolean createFile(File file) {
		try {
			if (file.exists()){ return false; }
			if (file.createNewFile()){
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Jarファイル内のファイルをコピーします。
	 * @param paste	コピー先ファイル
	 * @param target	目的のファイル名
	 */
	public static void inJarFileCopy(File paste, String target) {

		File file = new File(paste, target);
		
		// 既に作られていれば処理を止める。
		if ( file.exists() ){ return; }
		
		createDirectory(paste);
		
		File inJarFile = UnderGroundPlus.getPluginJarFile();
		
		try ( JarFile jarfile = new JarFile(inJarFile); ) {
			
			ZipEntry zipentry = jarfile.getEntry(target);
			
			try ( InputStream is = jarfile.getInputStream(zipentry);
					FileOutputStream fos = new FileOutputStream(file);
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, Encode));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));) {
				
				String line;
				while ((line = reader.readLine()) != null){
					writer.write(line);
					writer.newLine();
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Log.info(e.getMessage());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		
	}
	
}
