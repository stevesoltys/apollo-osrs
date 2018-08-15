package com.oldscape.tool.util;

import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.Container;
import com.oldscape.tool.cache.FileStore;
import org.apollo.cache.map.MapConstants;
import org.apollo.cache.map.MapIndex;
import org.apollo.cache.map.MapIndexDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Steve Soltys
 */
public class DecryptMaps {


	public static void main(String[] args) throws IOException {
		Cache cache = new Cache(FileStore.open(Paths.get("data/fs/83").toFile()));

		MapIndexDecoder mapIndexDecoder = new MapIndexDecoder(cache);
		Map<Integer, MapIndex> indices = mapIndexDecoder.decode();

		for (MapIndex mapIndex : indices.values()) {
			ByteBuffer data;

			try {
				int[] decryptionKeys = MapXTEA.getKey(mapIndex.getPackedCoordinates());
				data = cache.read(MapConstants.MAP_INDEX, mapIndex.getObjectFile(), decryptionKeys).getData();

			} catch (Exception ex) {
				data = ByteBuffer.allocate(0);
			}

			cache.write(MapConstants.MAP_INDEX, mapIndex.getObjectFile(),
				new Container(Container.COMPRESSION_GZIP, data, 1));
		}
	}
}
