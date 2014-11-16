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
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import com.github.thekarura.bukkit.plugin.undergroundplus.UnderGroundPlus;

/**
 * ファイル関連の操作を簡易的に出来るようにするclassです。
 * @author karura
 */
public class FileHandler {
	
	private static Logger log = UnderGroundPlus.log;
	private static String logPrefix = UnderGroundPlus.logPrefix;
	
	private static String Encode = "UTF-8";

	/**
	 * ディレクトリーを生成します。
	 * @param dir
	 * @return 生成された場合trueをします
	 */
	public static boolean createDirectory(File dir){
		if (!dir.exists()){
		if (dir.mkdirs()){
			log.info(logPrefix + "can't create directory. : " + dir.getName());
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
			log.info(logPrefix+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Jarファイル内のファイルをコピーします。
	 * @param Pasete	コピー先ファイル
	 * @param filename	目的のファイル名
	 * @param Overwrite	上書きするか
	 */
	public static void inJarFileCopy(File Pasete, String Filename, boolean Overwrite){
		
		File pasete = new File(Pasete + "/" + Filename);
		
		// 既に作られていれば処理を止める。(上書き設定がtrueであればその限りではない)
		if ( pasete.exists() && !Overwrite ){ return; }
		
		JarFile jar = null;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try {
			
			jar = new JarFile(UnderGroundPlus.getPluginJarFile());
			ZipEntry zipentry = jar.getEntry(Filename);
			is = jar.getInputStream(zipentry);
			
			fos = new FileOutputStream(pasete);
			reader = new BufferedReader(new InputStreamReader(is, Encode));
			writer = new BufferedWriter(new OutputStreamWriter(fos));
			
			String line;
			while ((line = reader.readLine()) != null){
				writer.write(line);
				writer.newLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.info(logPrefix+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.info(logPrefix+e.getMessage());
		} finally {
			try {
				if ( writer != null ){
					writer.flush();
					writer.close();
				}
				if ( reader != null ){
					reader.close();
				}
				if ( fos != null ){
					fos.flush();
					fos.close();
				}
				if ( is != null ){
					is.close();
				}
			} catch(IOException e) {
			}
		}
		
	}
	
}
