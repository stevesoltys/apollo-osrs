package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.EnterAmountListener;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inter.bank.BankDepositEnterAmountListener;
import org.apollo.game.model.inter.bank.BankUtils;
import org.apollo.game.model.inter.bank.BankWithdrawEnterAmountListener;

/**
 * A {@link MessageHandler} that handles withdrawing and depositing items from/to a player's bank.
 *
 * @author Graham
 */
public final class BankMessageHandler extends MessageHandler<ButtonMessage> {

	/**
	 * Creates the BankMessageHandler.
	 *
	 * @param world The {@link World} the {@link ItemActionMessage} occurred in.
	 */
	public BankMessageHandler(World world) {
		super(world);
	}

	/**
	 * Converts an inventory option to an amount.
	 *
	 * @param option        The option.
	 * @param currentAmount The current amount.
	 * @return The amount.
	 * @throws IllegalArgumentException If the option is invalid.
	 */
	private static int inventoryOptionToAmount(int option, int currentAmount) {
		switch (option) {
			case 2:
				return 1;
			case 3:
				return 5;
			case 4:
				return 10;
			case 6:
				return -1;
			case 7:
				return currentAmount;
		}

		throw new IllegalArgumentException("Invalid option supplied.");
	}

	/**
	 * Converts an inventory option to an amount.
	 *
	 * @param option        The option.
	 * @param currentAmount The current amount.
	 * @return The amount.
	 * @throws IllegalArgumentException If the option is invalid.
	 */
	private static int bankOptionToAmount(int option, int currentAmount) {
		switch (option) {
			case 1:
				return 1;
			case 2:
				return 5;
			case 3:
				return 10;
			case 5:
				return -1;
			case 6:
				return currentAmount;
			case 7:
				return currentAmount - 1;
		}

		throw new IllegalArgumentException("Invalid option supplied.");
	}

	@Override
	public void handle(Player player, ButtonMessage message) {
		if (player.getInterfaceSet().contains(BankConstants.WINDOW_ID)) {
			if (message.getInterfaceId() == BankConstants.SIDEBAR_ID) {
				deposit(player, message);
			} else if (message.getInterfaceId() == BankConstants.WINDOW_ID) {
				withdraw(player, message);
			}
		}
	}

	/**
	 * Handles a deposit action.
	 *
	 * @param player  The player.
	 * @param message The message.
	 */
	private void deposit(Player player, ButtonMessage message) {
		int currentAmount = player.getInventory().get(message.getChildButton()).getAmount();
		int amount = inventoryOptionToAmount(message.getIndex(), currentAmount);

		if (amount == -1) {
			EnterAmountListener listener = new BankDepositEnterAmountListener(player, message.getChildButton(),
				message.getItemId());

			player.getInterfaceSet().openEnterAmountDialogue(listener);
		} else if (!BankUtils.deposit(player, message.getChildButton(), message.getItemId(), amount)) {
			message.terminate();
		}
	}

	/**
	 * Handles a withdraw action.
	 *
	 * @param player  The player.
	 * @param message The message.
	 */
	private void withdraw(Player player, ButtonMessage message) {
		int currentAmount = player.getBank().get(message.getChildButton()).getAmount();
		int amount = bankOptionToAmount(message.getIndex(), currentAmount);

		if (amount == -1) {
			EnterAmountListener listener = new BankWithdrawEnterAmountListener(player, message.getChildButton(),
				message.getItemId());

			player.getInterfaceSet().openEnterAmountDialogue(listener);
		} else if (!BankUtils.withdraw(player, message.getChildButton(), message.getItemId(), amount)) {
			message.terminate();
		}
	}

}