package org.apollo.game.release.r83;

import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.*;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.sync.block.*;
import org.apollo.game.sync.seg.*;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

public class PlayerSynchronizationMessageEncoder extends MessageEncoder<PlayerSynchronizationMessage> {

	@Override
	public GamePacket encode(PlayerSynchronizationMessage event) {
		try {
			GamePacketBuilder packetBuilder = new GamePacketBuilder(129, PacketType.VARIABLE_SHORT);
			packetBuilder.switchToBitAccess();
			GamePacketBuilder blockBuilder = new GamePacketBuilder();
			putMovementUpdate(event.getSegment(), event, packetBuilder);
			putBlocks(event.getSegment(), blockBuilder);
			packetBuilder.putBits(8, event.getLocalPlayers());

			for (SynchronizationSegment segment : event.getSegments()) {
				SegmentType segmentType = segment.getType();

				if (segmentType != SegmentType.ADD_MOB && segmentType != SegmentType.REMOVE_MOB) {
					putMovementUpdate(segment, event, packetBuilder);
					putBlocks(segment, blockBuilder);
				}
			}

			for (SynchronizationSegment segment : event.getSegments()) {
				SegmentType type = segment.getType();
				if (type == SegmentType.REMOVE_MOB) {
					putRemoveCharacterUpdate(packetBuilder);
				} else if (type == SegmentType.ADD_MOB) {
					putAddPlayerUpdate((AddPlayerSegment) segment, event, packetBuilder);
					putBlocks(segment, blockBuilder);
				}
			}

			if (blockBuilder.getLength() > 0) {
				packetBuilder.putBits(11, 2047);
				packetBuilder.switchToByteAccess();
				packetBuilder.putRawBuilder(blockBuilder);
			} else {
				packetBuilder.switchToByteAccess();
			}
			return packetBuilder.toGamePacket();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void putRemoveCharacterUpdate(GamePacketBuilder packetBuilder) {
		packetBuilder.putBits(1, 1);
		packetBuilder.putBits(2, 3);
	}

	private void putAddPlayerUpdate(AddPlayerSegment segment, PlayerSynchronizationMessage event, GamePacketBuilder packetBuilder) {
		Position player = event.getPosition();
		Position other = segment.getPosition();

		int yPos = other.getY() - player.getY();
		int xPos = other.getX() - player.getX();

		packetBuilder.putBits(11, segment.getIndex());
		packetBuilder.putBits(1, 1);
		packetBuilder.putBits(5, xPos);
		packetBuilder.putBits(3, segment.getDirection().toInteger());
		packetBuilder.putBits(1, 1);
		packetBuilder.putBits(5, yPos);
	}

	private void putMovementUpdate(SynchronizationSegment segment, PlayerSynchronizationMessage event, GamePacketBuilder packetBuilder) {
		boolean updateRequired = segment.getBlockSet().size() > 0;
		if (segment.getType() == SegmentType.TELEPORT) {
			Position pos = ((TeleportSegment) segment).getDestination();
			packetBuilder.putBits(1, 1);
			packetBuilder.putBits(2, 3);
			packetBuilder.putBits(1, event.hasRegionChanged() ? 0 : 1);
			packetBuilder.putBits(7, pos.getLocalX(event.getLastKnownRegion()));
			packetBuilder.putBits(2, pos.getHeight());
			packetBuilder.putBits(1, updateRequired ? 1 : 0);
			packetBuilder.putBits(7, pos.getLocalY(event.getLastKnownRegion()));
		} else if (segment.getType() == SegmentType.RUN) {
			Direction[] directions = ((MovementSegment) segment).getDirections();
			packetBuilder.putBits(1, 1);
			packetBuilder.putBits(2, 2);
			packetBuilder.putBits(3, directions[0].toInteger());
			packetBuilder.putBits(3, directions[1].toInteger());
			packetBuilder.putBits(1, updateRequired ? 1 : 0);
		} else if (segment.getType() == SegmentType.WALK) {
			Direction[] directions = ((MovementSegment) segment).getDirections();
			packetBuilder.putBits(1, 1);
			packetBuilder.putBits(2, 1);
			packetBuilder.putBits(3, directions[0].toInteger());
			packetBuilder.putBits(1, updateRequired ? 1 : 0);
		} else {
			if (updateRequired) {
				packetBuilder.putBits(1, 1);
				packetBuilder.putBits(2, 0);
			} else {
				packetBuilder.putBits(1, 0);
			}
		}
	}

	private void putBlocks(SynchronizationSegment segment, GamePacketBuilder blockBuilder) {
		SynchronizationBlockSet blockSet = segment.getBlockSet();
		if (blockSet.size() > 0) {
			int mask = 0;

			if (blockSet.contains(ChatBlock.class)) {
				mask |= 0x10;
			}

            if (blockSet.contains(AnimationBlock.class)) {
                mask |= 0x4;
            }

			if (blockSet.contains(AppearanceBlock.class)) {
				mask |= 0x1;
			}

//            if (blockSet.contains(GraphicBlock.class)) {
//                mask |= 0x100;
//            }

//            if (blockSet.contains(ForcedChatBlock.class)) {
//                mask |= 0x40;
//            }
//
            if (blockSet.contains(InteractingMobBlock.class)) {
                mask |= 0x2;
            }
//
//            if (blockSet.contains(HitUpdateBlock.class)) {
//                mask |= 4;
//            }
//
//            if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
//                mask |= 0x200;
//            }

			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 0x20;
			}

			/*
			 * if (blockSet.contains(ForcedMovementBlock.class)) { mask |=
			 * 0x400; }
			 */
			if (mask >= 0x100) {
				mask |= 0x80;
				blockBuilder.put(DataType.BYTE, (mask & 0xFF));
				blockBuilder.put(DataType.BYTE, (mask >> 8));

			} else {
				blockBuilder.put(DataType.BYTE, mask);
			}

			if (blockSet.contains(ChatBlock.class)) {
				putChatBlock(blockSet.get(ChatBlock.class), blockBuilder);
			}

            if (blockSet.contains(AnimationBlock.class)) {
                putAnimationBlock(blockSet.get(AnimationBlock.class), blockBuilder);
            }

			if (blockSet.contains(AppearanceBlock.class)) {
				putAppearanceBlock(blockSet.get(AppearanceBlock.class), blockBuilder);
			}
//
//            if (blockSet.contains(GraphicBlock.class)) {
//                putGraphicBlock(blockSet.get(GraphicBlock.class), blockBuilder);
//            }

//            if (blockSet.contains(ForcedChatBlock.class)) {
//                putForcedChatBlock(blockSet.get(ForcedChatBlock.class), blockBuilder);
//            }


            if (blockSet.contains(InteractingMobBlock.class)) {
                putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), blockBuilder);
            }

            if (blockSet.contains(TurnToPositionBlock.class)) {
                putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), blockBuilder);
            }
//
//
//            if (blockSet.contains(HitUpdateBlock.class)) {
//                putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), blockBuilder);
//            }
//
//
//            if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
//                putSecondHitUpdateBlock(blockSet.get(SecondaryHitUpdateBlock.class), blockBuilder);
//            }
			/*
			 * if (blockSet.contains(ForcedMovementBlock.class)) {
			 * putForcedMovementBlock(blockSet.get(ForcedMovementBlock.class),
			 * blockBuilder); }
			 */
			// ill fix in time for agility
		}
	}

//    private void putForcedChatBlock(ForcedChatBlock forcedChatBlock, GamePacketBuilder blockBuilder) {
//        blockBuilder.putString(forcedChatBlock.getMessage());
//    }


	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getType());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getCurrentHealth());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getMaximumHealth());
	}

	/**
	 * Puts a Second Hit Update block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getType());
		builder.put(DataType.BYTE, block.getCurrentHealth());
		builder.put(DataType.BYTE, block.getMaximumHealth());
	}

	/*
	 * private void putForcedMovementBlock( ForcedMovementBlock
	 * forcedMovementBlock, GamePacketBuilder blockBuilder) { // TODO
	 * Auto-generated method stub
	 *
	 * }
	 */
	private void putInteractingMobBlock(InteractingMobBlock interactingMobBlock, GamePacketBuilder blockBuilder) {
		blockBuilder.put(DataType.SHORT, DataTransformation.ADD, interactingMobBlock.getIndex());
	}

	private void putAppearanceBlock(AppearanceBlock appearanceBlock, GamePacketBuilder blockBuilder) {
		Appearance appearance = appearanceBlock.getAppearance();
		GamePacketBuilder playerProperties = new GamePacketBuilder();

		playerProperties.put(DataType.BYTE, appearance.getGender().toInteger());
		playerProperties.put(DataType.BYTE, -1); //appearance.getSkullIcon().getId()); // skull icon
		playerProperties.put(DataType.BYTE, -1); //appearance.getPrayerIcon().getId()); // prayer icon
		Inventory equipment = appearanceBlock.getEquipment();
		int[] style = appearance.getStyle();
		Item item, chest, helm;

		for (int slot = 0; slot < 4; slot++) {
			if ((item = equipment.get(slot)) != null) {
				playerProperties.put(DataType.SHORT, 0x200 + item.getId());
			} else {
				playerProperties.put(DataType.BYTE, 0);
			}
		}
		if ((chest = equipment.get(EquipmentConstants.CHEST)) != null) {
			EquipmentDefinition def = EquipmentDefinition.lookup(chest.getId());
			if (def != null && !def.isFullBody()) {
				playerProperties.put(DataType.SHORT, 0x100 + style[3]);
			} else {
				playerProperties.put(DataType.SHORT, 0x200 + chest.getId());
			}
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[3]);
		}

		if ((item = equipment.get(EquipmentConstants.SHIELD)) != null) {
			playerProperties.put(DataType.SHORT, 0x200 + item.getId());
		} else {
			playerProperties.put(DataType.BYTE, 0);
		}
		if (chest != null) {
			playerProperties.put(DataType.SHORT, 0x200 + chest.getId());
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[2]);
		}

		if ((item = equipment.get(EquipmentConstants.LEGS)) != null) {
			playerProperties.put(DataType.SHORT, 0x200 + item.getId());
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[5]);
		}

		if ((helm = equipment.get(EquipmentConstants.HAT)) != null) {
			EquipmentDefinition def = null;
			if (helm != null) {
				def = EquipmentDefinition.lookup(helm.getId());
			}
			if (def != null && !def.isFullHat() && !def.isFullMask()) {
				playerProperties.put(DataType.SHORT, 0x100 + style[0]);
			} else {
				playerProperties.put(DataType.BYTE, 0);
			}
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[0]);
		}

		if ((item = equipment.get(EquipmentConstants.HANDS)) != null) {
			playerProperties.put(DataType.SHORT, 0x200 + item.getId());
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[4]);
		}

		if ((item = equipment.get(EquipmentConstants.FEET)) != null) {
			playerProperties.put(DataType.SHORT, 0x200 + item.getId());
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[6]);
		}

		EquipmentDefinition def = null;
		if (helm != null) {
			def = EquipmentDefinition.lookup(helm.getId());
		}
		if ((def != null && (def.isFullHat() || def.isFullMask())) || appearance.getGender() == Gender.FEMALE) {
			playerProperties.put(DataType.BYTE, 0);
		} else {
			playerProperties.put(DataType.SHORT, 0x100 + style[1]);
		}

		int[] colors = appearance.getColors();
		for (int color : colors) {
			playerProperties.put(DataType.BYTE, color);
		}

		playerProperties.put(DataType.SHORT, 0x328); // stand
		playerProperties.put(DataType.SHORT, 0x337); // stand turn
		playerProperties.put(DataType.SHORT, 0x333); // walk
		playerProperties.put(DataType.SHORT, 0x334); // turn 180
		playerProperties.put(DataType.SHORT, 0x335); // turn 90 cw
		playerProperties.put(DataType.SHORT, 0x336); // turn 90 ccw
		playerProperties.put(DataType.SHORT, 0x338); // run

//		AnimationSet animationSet = appearanceBlock.getAnimationSet();
//
//		playerProperties.put(DataType.SHORT, animationSet.getStand()); // stand
//		playerProperties.put(DataType.SHORT, animationSet.getIdleTurn()); // idle turn
//		playerProperties.put(DataType.SHORT, animationSet.getWalking()); // walk
//		playerProperties.put(DataType.SHORT, animationSet.getTurnAround()); // turn 180
//		playerProperties.put(DataType.SHORT, animationSet.getTurnRight()); // turn 90 cw
//		playerProperties.put(DataType.SHORT, animationSet.getTurnLeft()); // turn 90 ccw
//		playerProperties.put(DataType.SHORT, animationSet.getRunning()); // run

		playerProperties.putString(appearanceBlock.getName());
		playerProperties.put(DataType.BYTE, appearanceBlock.getCombatLevel());
		playerProperties.put(DataType.SHORT, appearanceBlock.getSkillLevel());
		playerProperties.put(DataType.BYTE, 0); // spot animation flag

		blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, playerProperties.getLength());
		blockBuilder.putRawBuilderReverse(playerProperties);
	}

	private void putGraphicBlock(GraphicBlock graphicBlock, GamePacketBuilder blockBuilder) {
		Graphic graphic = graphicBlock.getGraphic();
		blockBuilder.put(DataType.SHORT, DataTransformation.ADD, graphic.getId());
		blockBuilder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, graphic.getDelay() + (graphic.getHeight() * 65536));
	}

	private void putChatBlock(ChatBlock chatBlock, GamePacketBuilder blockBuilder) {

		blockBuilder.put(DataType.SHORT, DataOrder.LITTLE,
			(chatBlock.getTextColor() & 0xFF) << 8 | chatBlock.getTextEffects() & 0xFF);
		blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, chatBlock.getPrivilegeLevel().toInteger());
		byte[] compressedMessage = chatBlock.getCompressedMessage();
		blockBuilder.put(DataType.BYTE, DataTransformation.ADD, 0);
		blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, compressedMessage.length);
		blockBuilder.putBytesReverse(DataTransformation.ADD, compressedMessage);
	}

	private void putAnimationBlock(AnimationBlock animationBlock, GamePacketBuilder blockBuilder) {
		Animation anim = animationBlock.getAnimation();
		blockBuilder.put(DataType.SHORT, anim.getId());
		blockBuilder.put(DataType.BYTE, DataTransformation.SUBTRACT, anim.getDelay());
	}

	private void putTurnToPositionBlock(TurnToPositionBlock turnToPositionBlock, GamePacketBuilder blockBuilder) {
		Position loc = turnToPositionBlock.getPosition();
		if (loc == null) {
			blockBuilder.put(DataType.SHORT, DataTransformation.ADD, 0);
			blockBuilder.put(DataType.SHORT, DataTransformation.ADD, 0);
		} else {
			blockBuilder.put(DataType.SHORT, DataTransformation.ADD, loc.getX() * 2 + 1);
			blockBuilder.put(DataType.SHORT, DataTransformation.ADD, loc.getY() * 2 + 1);
		}
	}
}
