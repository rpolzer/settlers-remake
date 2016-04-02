package jsettlers.main.swing.menu.joingame;

import jsettlers.common.menu.IJoinPhaseMultiplayerGameConnector;
import jsettlers.common.menu.IMultiplayerListener;
import jsettlers.common.menu.IStartingGame;
import jsettlers.graphics.localization.Labels;
import jsettlers.logic.map.MapLoader;
import jsettlers.main.swing.JSettlersFrame;
import jsettlers.main.swing.menu.joingame.slots.ClientOfMultiplayerPlayerSlotFactory;

public class JoinMultiplayerGamePanel extends JoinGamePanel {

	public JoinMultiplayerGamePanel(JSettlersFrame settlersFrame, MapLoader mapLoader, IJoinPhaseMultiplayerGameConnector connector) {
		super(settlersFrame, mapLoader, new ClientOfMultiplayerPlayerSlotFactory());
		titleLabel.setText(Labels.getString("join-game-panel-join-multi-player-game-title"));
		numberOfPlayersComboBox.setEnabled(false);
		peaceTimeComboBox.setEnabled(false);
		startResourcesComboBox.setEnabled(false);
		setChatVisible(true);
		cancelButton.addActionListener(e -> {
			settlersFrame.showMainMenu();
		});
		startGameButton.setVisible(false);

		// joinMultiPlayerMap.getPlayers().setListener(changingPlayers -> { XXX
		// onPlayersChanges(changingPlayers, joinMultiPlayerMap);
		// });
		connector.setMultiplayerListener(new IMultiplayerListener() {
			@Override
			public void gameIsStarting(IStartingGame game) {
				settlersFrame.showStartingGamePanel(game);
			}

			@Override
			public void gameAborted() {
				settlersFrame.showMainMenu();
			}
		});
		initializeChatFor(connector);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
