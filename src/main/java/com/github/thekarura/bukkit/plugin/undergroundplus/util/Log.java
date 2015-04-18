package com.github.thekarura.bukkit.plugin.undergroundplus.util;

import java.util.logging.Logger;

import com.github.thekarura.bukkit.plugin.undergroundplus.UnderGroundPlus;

public class Log {
	
	private static final String prefix = UnderGroundPlus.prefix;
	private static final Logger log = UnderGroundPlus.getInstance().getLogger();
	
	public static void info(String mes){
		log.info(prefix+mes);
	}
	
	public static void warning(String mes){
		log.warning(prefix+mes);
	}
	
}
