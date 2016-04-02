package jsettlers.main.swing.menu.joingame;

import jsettlers.common.menu.IStartingGame;
import jsettlers.graphics.localization.Labels;
import jsettlers.logic.map.MapLoader;
import jsettlers.logic.player.PlayerSetting;
import jsettlers.main.JSettlersGame;
import jsettlers.main.swing.JSettlersFrame;
import jsettlers.main.swing.menu.joingame.slots.SinglePlayerSlotFactory;

public class SinglePlayerJoinPanel extends JoinGamePanel {

	public SinglePlayerJoinPanel(JSettlersFrame settlersFrame, MapLoader mapLoader) {
		super(settlersFrame, mapLoader, new SinglePlayerSlotFactory());
		titleLabel.setText(Labels.getString("join-game-panel-new-single-player-game-title"));
		numberOfPlayersComboBox.setEnabled(true);
		peaceTimeComboBox.setEnabled(true);
		startResourcesComboBox.setEnabled(true);
		startGameButton.setVisible(true);
		setChatVisible(false);
		cancelButton.addActionListener(e -> settlersFrame.showMainMenu());
	}

	@Override
	protected void onGameStart() {
		long randomSeed = System.currentTimeMillis();
		PlayerSetting[] playerSettings = playerSlotsPanel.getPlayerSettings();
		JSettlersGame game = new JSettlersGame(mapLoader, randomSeed, playerSlotsPanel.getActivePlayerIndex(), playerSettings);
		IStartingGame startingGame = game.start();
		settlersFrame.showStartingGamePanel(startingGame);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
