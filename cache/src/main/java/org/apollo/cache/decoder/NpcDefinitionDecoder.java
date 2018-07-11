package org.apollo.cache.decoder;

import com.oldscape.tool.cache.Archive;
import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.ReferenceTable;
import com.oldscape.tool.cache.type.CacheIndex;
import com.oldscape.tool.cache.type.ConfigArchive;
import com.oldscape.tool.util.BitUtils;
import com.oldscape.tool.util.ByteBufferUtils;
import org.apollo.cache.def.NpcDefinition;
import org.apollo.util.BufferUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes npc data from the {@code npc.dat} file into {@link NpcDefinition}s.
 *
 * @author Major
 */
public final class NpcDefinitionDecoder implements Runnable {

	/**
	 * The cache.
	 */
	private final Cache cache;

	/**
	 * Creates the NpcDefinitionDecoder.
	 *
	 * @param cache The {@link Cache}.
	 */
	public NpcDefinitionDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		try {
			ReferenceTable table = cache.getReferenceTable(CacheIndex.CONFIGS);
			ReferenceTable.Entry entry = table.getEntry(ConfigArchive.NPC);
			Archive archive = Archive.decode(cache.read(CacheIndex.CONFIGS, ConfigArchive.NPC).getData(), entry.size());

			NpcDefinition[] definitions = new NpcDefinition[entry.capacity()];

			for (int id = 0; id < definitions.length; id++) {
				ReferenceTable.ChildEntry child = entry.getEntry(id);

				if (child != null) {
					definitions[id] = decode(id, archive.getEntry(child.index()));
				} else {
					definitions[id] = new NpcDefinition(id);
				}
			}

			NpcDefinition.init(definitions);

		} catch (IOException e) {
			throw new UncheckedIOException("Error decoding NpcDefinitions.", e);
		}
	}

	/**
	 * Decodes a single definition.
	 *
	 * @param id     The npc's id.
	 * @param buffer The buffer.
	 * @return The {@link NpcDefinition}.
	 */
	private NpcDefinition decode(int id, ByteBuffer buffer) {
		NpcDefinition definition = new NpcDefinition(id);

		while (true) {
			int opcode = buffer.get() & 0xFF;

			if (opcode == 0) {
				return definition;
			} else if (opcode == 1) {
				int length = buffer.get() & 0xFF;
				int[] models = new int[length];

				for (int idx = 0; idx < length; ++idx) {
					models[idx] = buffer.getShort() & 0xFFFF;
				}

			} else if (opcode == 2) {
				definition.setName(BufferUtil.readString(buffer));
			} else if (opcode == 3) {
				definition.setDescription(BufferUtil.readString(buffer));
			} else if (opcode == 12) {
				definition.setSize(buffer.get() & 0xFF);
			} else if (opcode == 13) {
				definition.setStandAnimation(buffer.getShort() & 0xFFFF);
			} else if (opcode == 14) {
				definition.setWalkAnimation(buffer.getShort() & 0xFFFF);
			} else if (opcode == 15) {
				int anInt2165 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 16) {
				int anInt2189 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 17) {
				definition.setWalkAnimations(buffer.getShort() & 0xFFFF, buffer.getShort() & 0xFFFF,
					buffer.getShort() & 0xFFFF, buffer.getShort() & 0xFFFF);
			} else if (opcode >= 30 && opcode < 35) {
				String action = BufferUtil.readString(buffer);

				if (action.equals("hidden")) {
					action = null;
				}

				definition.setInteraction(opcode - 30, action);
			} else if (opcode == 40) {
				int length = buffer.get() & 0xFF;
				short[] recolorToFind = new short[length];
				short[] recolorToReplace = new short[length];

				for (int idx = 0; idx < length; ++idx) {
					recolorToFind[idx] = (short) (buffer.getShort() & 0xFFFF);
					recolorToReplace[idx] = (short) (buffer.getShort() & 0xFFFF);
				}

			} else if (opcode == 41) {
				int length = buffer.get() & 0xFF;
				short[] retextureToFind = new short[length];
				short[] retextureToReplace = new short[length];

				for (int idx = 0; idx < length; ++idx) {
					retextureToFind[idx] = (short) (buffer.getShort() & 0xFFFF);
					retextureToReplace[idx] = (short) (buffer.getShort() & 0xFFFF);
				}

			} else if (opcode == 60) {
				int length = buffer.get() & 0xFF;
				int[] models_2 = new int[length];

				for (int idx = 0; idx < length; ++idx) {
					models_2[idx] = buffer.getShort() & 0xFFFF;
				}

			} else if (opcode == 93) {
				boolean renderOnMinimap = false;
			} else if (opcode == 95) {
				definition.setCombatLevel(buffer.getShort() & 0xFFFF);
			} else if (opcode == 97) {
				int resizeX = buffer.getShort() & 0xFFFF;
			} else if (opcode == 98) {
				int resizeY = buffer.getShort() & 0xFFFF;
			} else if (opcode == 99) {
				boolean hasRenderPriority = true;
			} else if (opcode == 100) {
				int ambient = buffer.get();
			} else if (opcode == 101) {
				int contrast = buffer.get();
			} else if (opcode == 102) {
				int headIcon = buffer.getShort() & 0xFFFF;
			} else if (opcode == 103) {
				int anInt2156 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 106) {
				int anInt2174 = buffer.getShort() & 0xFFFF;
				if (0xFFFF == anInt2174) {
					anInt2174 = -1;
				}

				int anInt2187 = buffer.getShort() & 0xFFFF;
				if (0xFFFF == anInt2187) {
					anInt2187 = -1;
				}

				int length = buffer.get() & 0xFF;
				int[] anIntArray2185 = new int[length + 2];

				for (int idx = 0; idx <= length; ++idx) {
					anIntArray2185[idx] = buffer.getShort() & 0xFFFF;
					if (anIntArray2185[idx] == 0xFFFF) {
						anIntArray2185[idx] = -1;
					}
				}

				anIntArray2185[length + 1] = -1;
			} else if (opcode == 107) {
				boolean isClickable = false;
			} else if (opcode == 109) {
				boolean aBool2170 = false;
			} else if (opcode == 111) {
				boolean aBool2190 = true;
			} else if (opcode == 118) {
				int anInt2174 = buffer.getShort() & 0xFFFF;
				if (0xFFFF == anInt2174) {
					anInt2174 = -1;
				}

				int anInt2187 = buffer.getShort() & 0xFFFF;
				if (0xFFFF == anInt2187) {
					anInt2187 = -1;
				}

				int var = buffer.getShort() & 0xFFFF;
				if (var == 0xFFFF) {
					var = -1;
				}

				int length = buffer.get() & 0xFF;
				int[] anIntArray2185 = new int[length + 2];

				for (int idx = 0; idx <= length; ++idx) {
					anIntArray2185[idx] = buffer.getShort() & 0xFFFF;
					if (anIntArray2185[idx] == 0xFFFF) {
						anIntArray2185[idx] = -1;
					}
				}

				anIntArray2185[length + 1] = var;
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

	/**
	 * Wraps a morphism value around, returning -1 if the specified value is 65,535.
	 *
	 * @param value The value.
	 * @return -1 if {@code value} is 65,535, otherwise {@code value}.
	 */
	private int wrap(int value) {
		return value == 65_535 ? -1 : value;
	}

}