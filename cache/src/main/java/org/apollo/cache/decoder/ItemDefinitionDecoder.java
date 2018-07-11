package org.apollo.cache.decoder;

import com.oldscape.tool.cache.Archive;
import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.ReferenceTable;
import com.oldscape.tool.cache.type.CacheIndex;
import com.oldscape.tool.cache.type.ConfigArchive;
import com.oldscape.tool.util.BitUtils;
import com.oldscape.tool.util.ByteBufferUtils;
import org.apollo.cache.def.ItemDefinition;
import org.apollo.util.BufferUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes item data from the {@code obj.dat} file into {@link ItemDefinition}s.
 *
 * @author Graham
 */
public final class ItemDefinitionDecoder implements Runnable {

	/**
	 * The cache.
	 */
	private final Cache cache;

	/**
	 * Creates the ItemDefinitionEncoder.
	 *
	 * @param cache The {@link Cache}.
	 */
	public ItemDefinitionDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		try {
			ReferenceTable table = cache.getReferenceTable(CacheIndex.CONFIGS);
			ReferenceTable.Entry entry = table.getEntry(ConfigArchive.ITEM);
			Archive archive = Archive.decode(cache.read(CacheIndex.CONFIGS, ConfigArchive.ITEM).getData(),
				entry.size());

			ItemDefinition[] definitions = new ItemDefinition[entry.capacity()];
			for (int id = 0; id < definitions.length; id++) {
				ReferenceTable.ChildEntry child = entry.getEntry(id);

				if (child != null) {
					definitions[id] = decode(id, archive.getEntry(child.index()));
				} else {
					definitions[id] = new ItemDefinition(id);
				}
			}

			ItemDefinition.init(definitions);

		} catch (IOException e) {
			throw new UncheckedIOException("Error decoding ItemDefinitions.", e);
		}
	}

	/**
	 * Decodes a single definition.
	 *
	 * @param id     The item's id.
	 * @param buffer The buffer.
	 * @return The {@link ItemDefinition}.
	 */
	private ItemDefinition decode(int id, ByteBuffer buffer) {
		ItemDefinition definition = new ItemDefinition(id);

		while (true) {
			int opcode = buffer.get() & 0xFF;

			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				int inventoryModel = buffer.getShort() & 0xFFFF;
			} else if (opcode == 2) {
				definition.setName(BufferUtil.readString(buffer));
			} else if (opcode == 3) {
				definition.setDescription(BufferUtil.readString(buffer));
			} else if (opcode == 4) {
				int zoom2d = buffer.getShort() & 0xFFFF;
			} else if (opcode == 5) {
				int xan2d = buffer.getShort() & 0xFFFF;
			} else if (opcode == 6) {
				int yan2d = buffer.getShort() & 0xFFFF;
			} else if (opcode == 7) {
				int xOffset2d = buffer.getShort() & 0xFFFF;
				if (xOffset2d > 32767) {
					xOffset2d -= 65536;
				}
			} else if (opcode == 8) {
				int yOffset2d = buffer.getShort() & 0xFFFF;
				if (yOffset2d > 32767) {
					yOffset2d -= 65536;
				}
			} else if (opcode == 11) {
				definition.setStackable(true);
			} else if (opcode == 12) {
				definition.setValue(buffer.getInt());
			} else if (opcode == 16) {
				definition.setMembersOnly(true);
			} else if (opcode == 23) {
				int maleModel0 = buffer.getShort() & 0xFFFF;
				int maleOffset = buffer.get() & 0xFF;
			} else if (opcode == 24) {
				int maleModel1 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 25) {
				int femaleModel0 = buffer.getShort() & 0xFFFF;
				int femaleOffset = buffer.get() & 0xFF;
			} else if (opcode == 26) {
				int femaleModel1 = buffer.getShort() & 0xFFFF;
			} else if (opcode >= 30 && opcode < 35) {
				String str = BufferUtil.readString(buffer);
				if (str.equalsIgnoreCase("hidden")) {
					str = null;
				}
				definition.setGroundAction(opcode - 30, str);
			} else if (opcode >= 35 && opcode < 40) {
				definition.setInventoryAction(opcode - 35, BufferUtil.readString(buffer));
			} else if (opcode == 40) {
				int length = buffer.get() & 0xFF;
				short[] colorFind = new short[length];
				short[] colorReplace = new short[length];

				for (int idx = 0; idx < length; ++idx) {
					colorFind[idx] = (short) (buffer.getShort() & 0xFFFF);
					colorReplace[idx] = (short) (buffer.getShort() & 0xFFFF);
				}

			} else if (opcode == 41) {
				int length = buffer.get() & 0xFF;
				short[] textureFind = new short[length];
				short[] textureReplace = new short[length];

				for (int idx = 0; idx < length; ++idx) {
					textureFind[idx] = (short) (buffer.getShort() & 0xFFFF);
					textureReplace[idx] = (short) (buffer.getShort() & 0xFFFF);
				}
			} else if (opcode == 42) {
				int anInt2173 = buffer.get() & 0xFF;
			} else if (opcode == 65) {
				boolean stockMarket = true;
			} else if (opcode == 78) {
				int maleModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 79) {
				int femaleModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 90) {
				int maleHeadModel = buffer.getShort() & 0xFFFF;
			} else if (opcode == 91) {
				int femaleHeadModel = buffer.getShort() & 0xFFFF;
			} else if (opcode == 92) {
				int maleHeadModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 93) {
				int femaleHeadModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 95) {
				int zan2d = buffer.getShort() & 0xFFFF;
			} else if (opcode == 97) {
				definition.setNoteInfoId(buffer.getShort() & 0xFFFF);
			} else if (opcode == 98) {
				definition.setNoteGraphicId(buffer.getShort() & 0xFFFF);
			} else if (opcode >= 100 && opcode < 110) {
				int[] countObj = null, countCo = null;

				if (countObj == null) {
					countObj = new int[10];
					countCo = new int[10];
				}

				countObj[opcode - 100] = buffer.getShort() & 0xFFFF;
				countCo[opcode - 100] = buffer.getShort() & 0xFFFF;
			} else if (opcode == 110) {
				int resizeX = buffer.getShort() & 0xFFFF;
			} else if (opcode == 111) {
				int resizeY = buffer.getShort() & 0xFFFF;
			} else if (opcode == 112) {
				int resizeZ = buffer.getShort() & 0xFFFF;
			} else if (opcode == 113) {
				int ambient = buffer.get();
			} else if (opcode == 114) {
				int contrast = buffer.get();
			} else if (opcode == 115) {
				definition.setTeam(buffer.get() & 0xFF);
			} else if (opcode == 139) {
				int boughtLink = buffer.getShort() & 0xFFFF;
			} else if (opcode == 140) {
				int boughtTemplate = buffer.getShort() & 0xFFFF;
			} else if (opcode == 148) {
				int anInt1879 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 149) {
				int anInt1833 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 249) {
				int length = buffer.get() & 0xFF;

				Map<Integer, Object> params = new HashMap<>(BitUtils.nextPowerOfTwo(length));
				for (int i = 0; i < length; i++) {
					boolean isString = (buffer.get() & 0xFF) == 1;
					int key = ByteBufferUtils.getMedium(buffer);
					Object value;

					if (isString) {
						value = ByteBufferUtils.getString(buffer);
					} else {
						value = buffer.getInt();
					}

					params.put(key, value);
				}
			}
		}
	}

}