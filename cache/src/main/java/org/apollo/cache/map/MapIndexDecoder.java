package org.apollo.cache.map;

import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.region.Region;
import org.apollo.cache.IndexedFileSystem;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes {@link MapIndex}s from the {@link IndexedFileSystem}.
 *
 * @author Ryley
 * @author Major
 */
public final class MapIndexDecoder implements Runnable {

	/**
	 * The maximum number of possible regions.
	 */
	private static final int MAX_REGIONS = Short.MAX_VALUE;

	/**
	 * The file id of the versions archive.
	 */
	private static final int VERSIONS_ARCHIVE_FILE_ID = 5;

	/**
	 * The cache.
	 */
	private final Cache cache;

	/**
	 * Creates the MapIndexDecoder.
	 *
	 * @param cache The {@link Cache}.
	 */
	public MapIndexDecoder(Cache cache) {
		this.cache = cache;
	}

	/**
	 * Decodes {@link MapIndex}s from the specified {@link IndexedFileSystem}.
	 *
	 * @return A {@link Map} of packed coordinates to their MapDefinitions.
	 * @throws IOException If there is an error reading or decoding the Archive.
	 */
	public Map<Integer, MapIndex> decode() throws IOException {
		Map<Integer, MapIndex> definitions = new HashMap<>();

		for (int id = 0; id < MAX_REGIONS; id++) {
			Region region = new Region(id);
			int terrain = cache.getFileId(VERSIONS_ARCHIVE_FILE_ID, region.getTerrainIdentifier());
			int objects = cache.getFileId(VERSIONS_ARCHIVE_FILE_ID, region.getLocationsIdentifier());

			if (terrain == -1 && objects == -1) {
				continue;
			}

			definitions.put(id, new MapIndex(id, terrain, objects, true));
		}

		return definitions;
	}

	@Override
	public void run() {
		try {
			MapIndex.init(decode());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}