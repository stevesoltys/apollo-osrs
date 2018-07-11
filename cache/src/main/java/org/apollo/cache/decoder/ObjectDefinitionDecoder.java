package org.apollo.cache.decoder;

import com.oldscape.tool.cache.Archive;
import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.ReferenceTable;
import com.oldscape.tool.cache.type.CacheIndex;
import com.oldscape.tool.cache.type.ConfigArchive;
import com.oldscape.tool.util.BitUtils;
import com.oldscape.tool.util.ByteBufferUtils;
import org.apollo.cache.def.ObjectDefinition;
import org.apollo.util.BufferUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes object data from the {@code loc.dat} file into {@link ObjectDefinition}s.
 *
 * @author Major
 */
public final class ObjectDefinitionDecoder implements Runnable {

	/**
	 * The cache.
	 */
	private final Cache cache;

	/**
	 * Creates the ObjectDefinitionDecoder.
	 *
	 * @param cache The {@link Cache}.
	 */
	public ObjectDefinitionDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		try {
			ReferenceTable table = cache.getReferenceTable(CacheIndex.CONFIGS);
			ReferenceTable.Entry entry = table.getEntry(ConfigArchive.OBJECT);
			Archive archive = Archive.decode(cache.read(CacheIndex.CONFIGS, ConfigArchive.OBJECT).getData(),
				entry.size());


			ObjectDefinition[] definitions = new ObjectDefinition[entry.capacity()];

			for (int id = 0; id < definitions.length; id++) {
				ReferenceTable.ChildEntry child = entry.getEntry(id);

				if (child != null) {
					definitions[id] = decode(id, archive.getEntry(child.index()));
				} else {
					definitions[id] = new ObjectDefinition(id);
				}
			}

			ObjectDefinition.init(definitions);

		} catch (IOException e) {
			throw new UncheckedIOException("Error decoding NpcDefinitions.", e);
		}
	}

	/**
	 * Decodes data from the cache into an {@link ObjectDefinition}.
	 *
	 * @param id   The id of the object.
	 * @param data The {@link ByteBuffer} containing the data.
	 * @return The object definition.
	 */
	private ObjectDefinition decode(int id, ByteBuffer data) {
		ObjectDefinition definition = new ObjectDefinition(id);

		while (true) {
			int opcode = data.get() & 0xFF;

			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				int length = data.get() & 0xFF;
				if (length > 0) {
					int[] objectTypes = new int[length];
					int[] objectModels = new int[length];

					for (int index = 0; index < length; ++index) {
						objectModels[index] = data.getShort() & 0xFFFF;
						objectTypes[index] = data.get() & 0xFF;
					}
				}
			} else if (opcode == 2) {
				definition.setName(BufferUtil.readString(data));
			} else if (opcode == 3) {
				definition.setDescription(BufferUtil.readString(data));
			} else if (opcode == 5) {
				int length = data.get() & 0xFF;
				if (length > 0) {
					int[] objectTypes = null;
					int[] objectModels = new int[length];

					for (int index = 0; index < length; ++index) {
						objectModels[index] = data.getShort() & 0xFFFF;
					}
				}
			} else if (opcode == 14) {
				definition.setWidth(data.get() & 0xFF);
			} else if (opcode == 15) {
				definition.setLength(data.get() & 0xFF);
			} else if (opcode == 17) {
				definition.setSolid(false);
			} else if (opcode == 18) {
				definition.setImpenetrable(false);
			} else if (opcode == 19) {
				int anInt2088 = data.get() & 0xFF;
			} else if (opcode == 21) {
				int anInt2105 = 0;
			} else if (opcode == 22) {
				boolean nonFlatShading = true;
			} else if (opcode == 23) {
				boolean aBool2111 = true;
			} else if (opcode == 24) {
				int animationID = data.getShort() & 0xFFFF;
				if (animationID == 0xFFFF) {
					animationID = -1;
				}
			} else if (opcode == 27) {
				int anInt2094 = 1;
			} else if (opcode == 28) {
				int anInt2069 = data.get() & 0xFF;
			} else if (opcode == 29) {
				int ambient = data.get();
			} else if (opcode == 39) {
				int contrast = data.get();
			} else if (opcode >= 30 && opcode < 35) {
				String[] actions = definition.getMenuActions();

				if (actions == null) {
					actions = new String[10];
				}

				String action = BufferUtil.readString(data);

				if (action.equalsIgnoreCase("Hidden")) {
					action = null;
				}

				actions[opcode - 30] = action;
				definition.setMenuActions(actions);

			} else if (opcode == 40) {
				int length = data.get() & 0xFF;
				short[] recolorToFind = new short[length];
				short[] recolorToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					recolorToFind[index] = (short) (data.getShort() & 0xFFFF);
					recolorToReplace[index] = (short) (data.getShort() & 0xFFFF);
				}

			} else if (opcode == 41) {
				int length = data.get() & 0xFF;
				short[] retextureToFind = new short[length];
				short[] textureToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					retextureToFind[index] = (short) (data.getShort() & 0xFFFF);
					textureToReplace[index] = (short) (data.getShort() & 0xFFFF);
				}
			} else if (opcode == 62) {
				boolean aBool2108 = true;
			} else if (opcode == 64) {
				boolean aBool2097 = false;
			} else if (opcode == 65) {
				int modelSizeX = data.getShort() & 0xFFFF;
			} else if (opcode == 66) {
				int modelSizeHeight = data.getShort() & 0xFFFF;
			} else if (opcode == 67) {
				int modelSizeY = data.getShort() & 0xFFFF;
			} else if (opcode == 68) {
				int mapSceneID = data.getShort() & 0xFFFF;
			} else if (opcode == 69) {
				data.get();
			} else if (opcode == 70) {
				int offsetX = data.getShort() & 0xFFFF;
			} else if (opcode == 71) {
				int offsetHeight = data.getShort() & 0xFFFF;
			} else if (opcode == 72) {
				int offsetY = data.getShort() & 0xFFFF;
			} else if (opcode == 73) {
				definition.setObstructive(true);
			} else if (opcode == 74) {
				definition.setSolid(true);
			} else if (opcode == 75) {
				int anInt2106 = data.get() & 0xFF;
			} else if (opcode == 77) {
				int varpID = data.getShort() & 0xFFFF;
				if (varpID == 0xFFFF) {
					varpID = -1;
				}

				int configId = data.getShort() & 0xFFFF;
				if (configId == 0xFFFF) {
					configId = -1;
				}

				int length = data.get() & 0xFF;
				int[] configChangeDest = new int[length + 2];

				for (int index = 0; index <= length; ++index) {
					configChangeDest[index] = data.getShort() & 0xFFFF;
					if (0xFFFF == configChangeDest[index]) {
						configChangeDest[index] = -1;
					}
				}

				configChangeDest[length + 1] = -1;
			} else if (opcode == 78) {
				int anInt2110 = data.getShort() & 0xFFFF;
				int anInt2083 = data.get() & 0xFF;
			} else if (opcode == 79) {
				int anInt2112 = data.getShort() & 0xFFFF;
				int anInt2113 = data.getShort() & 0xFFFF;
				int anInt2083 = data.get() & 0xFF;
				int length = data.get() & 0xFF;
				int[] anIntArray2084 = new int[length];

				for (int index = 0; index < length; ++index) {
					anIntArray2084[index] = data.getShort() & 0xFFFF;
				}
			} else if (opcode == 81) {
				int anInt2105 = data.get() & 0xFF;
			} else if (opcode == 82) {
				int mapAreaId = data.getShort() & 0xFFFF;
			} else if (opcode == 92) {
				int varpID = data.getShort() & 0xFFFF;
				if (varpID == 0xFFFF) {
					varpID = -1;
				}

				int configId = data.getShort() & 0xFFFF;
				if (configId == 0xFFFF) {
					configId = -1;
				}

				int var = data.getShort() & 0xFFFF;
				if (var == 0xFFFF) {
					var = -1;
				}

				int length = data.get() & 0xFF;
				int[] configChangeDest = new int[length + 2];

				for (int index = 0; index <= length; ++index) {
					configChangeDest[index] = data.getShort() & 0xFFFF;
					if (0xFFFF == configChangeDest[index]) {
						configChangeDest[index] = -1;
					}
				}

				configChangeDest[length + 1] = var;
			} else if (opcode == 249) {
				int length = data.get() & 0xFF;

				Map<Integer, Object> params = new HashMap<>(BitUtils.nextPowerOfTwo(length));
				for (int i = 0; i < length; i++) {
					boolean isString = (data.get() & 0xFF) == 1;
					int key = ByteBufferUtils.getMedium(data);
					Object value;

					if (isString) {
						value = ByteBufferUtils.getString(data);
					} else {
						value = data.getInt();
					}

					params.put(key, value);
				}
			}
		}
	}

}