package com.oldscape.tool.util;

import com.oldscape.tool.cache.Cache;
import org.apollo.cache.FileDescriptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MapXTEA {

	private static Map<Integer, int[]> mapKeys = new HashMap<Integer, int[]>();

	static {
		init();
	}

	public static void init() {
		try {
			loadUnpacked();
			Logger.getAnonymousLogger().info("Loaded " + mapKeys.size() + " map XTEA key(s)");
		} catch (Exception e) {
			System.err.println("Failed to load map xtea(s)!");
			e.printStackTrace();
		}
	}

	public static void loadUnpacked() throws IOException {//could've swore their was a packer lol could maybe edit it to load unpacked lol
		File directory = new File("data/xtea/");
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					BufferedReader input = new BufferedReader(new FileReader(file));
					int id = Integer.parseInt(file.getName().substring(0, file.getName().indexOf(".")));
					int[] keys = new int[4];
					for (int i = 0; i < 4; i++) {
						String line = input.readLine();
						if (line != null) {
							keys[i] = Integer.parseInt(line);
						} else {
							System.err.println("Corrupted XTEA file : " + id + "; line: " + line);
							keys[i] = 0;
						}
					}
					input.close();
					mapKeys.put(id, keys);
				}
			}
		}
	}

	public static int[] getKeysForFile(Cache cache, FileDescriptor descriptor) throws IOException {

		if(descriptor.getType() != 5) {
			return new int[4];
		}

		for (int regionId : mapKeys.keySet()) {
			int map = cache.getFileId(5, "m" + (regionId >> 8) + "_" + (regionId & 0xFF));
			int land = cache.getFileId(5, "l" + (regionId >> 8) + "_" + (regionId & 0xFF));

			if (map == descriptor.getFile() || land == descriptor.getFile()) {
				return getKey(regionId);
			}
		}

		return new int[4];
	}

	public static int[] getKey(int region) {
		int[] keys = mapKeys.get(region);
		if (keys == null) {
			return new int[4];
		}
		return keys;
	}

	public static Map<Integer, int[]> getMapKeys() {
		return mapKeys;
	}
}
