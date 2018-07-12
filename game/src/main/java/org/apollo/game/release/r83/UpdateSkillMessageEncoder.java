package org.apollo.game.release.r83;

import org.apollo.game.message.impl.UpdateSkillMessage;
import org.apollo.game.model.entity.Skill;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

public final class UpdateSkillMessageEncoder extends MessageEncoder<UpdateSkillMessage> {

	@Override
	public GamePacket encode(UpdateSkillMessage event) {
		GamePacketBuilder builder = new GamePacketBuilder(14);
		Skill skill = event.getSkill();
		builder.put(DataType.BYTE, skill.getCurrentLevel());
		builder.put(DataType.BYTE, DataTransformation.ADD, event.getId());
		builder.put(DataType.INT, DataOrder.LITTLE, (int) skill.getExperience());
		return builder.toGamePacket();
	}
}
