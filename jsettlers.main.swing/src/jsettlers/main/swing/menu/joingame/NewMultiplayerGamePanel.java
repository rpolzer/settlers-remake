package jsettlers.main.swing.menu.joingame;

import javax.swing.SwingUtilities;

import jsettlers.common.menu.EProgressState;
import jsettlers.common.menu.IJoinPhaseMultiplayerGameConnector;
import jsettlers.common.menu.IJoiningGame;
import jsettlers.common.menu.IJoiningGameListener;
import jsettlers.common.menu.IMultiplayerConnector;
import jsettlers.common.menu.IMultiplayerListener;
import jsettlers.common.menu.IStartingGame;
import jsettlers.graphics.localization.Labels;
import jsettlers.logic.map.MapLoader;
import jsettlers.main.swing.JSettlersFrame;
import jsettlers.main.swing.menu.joingame.slots.HostOfMultiplayerPlayerSlotFactory;

public class NewMultiplayerGamePanel extends JoinGamePanel {
	private IJoinPhaseMultiplayerGameConnector joinedGame;
	private IJoiningGame joiningGame;

	public NewMultiplayerGamePanel(JSettlersFrame settlersFrame, MapLoader mapLoader, IMultiplayerConnector connector) {
		super(settlersFrame, mapLoader, new HostOfMultiplayerPlayerSlotFactory());
		titleLabel.setText(Labels.getString("join-game-panel-new-multi-player-game-title"));
		numberOfPlayersComboBox.setEnabled(false);
		peaceTimeComboBox.setEnabled(false);
		startResourcesComboBox.setEnabled(false);
		startGameButton.setVisible(true);
		setChatVisible(true);
		joiningGame = connector.openNewMultiplayerGame(new OpenMultiPlayerGameInfo(mapLoader));
		joiningGame.setListener(new IJoiningGameListener() {

			@Override
			public void joinProgressChanged(EProgressState state, float progress) {

			}

			@Override
			public void gameJoined(IJoinPhaseMultiplayerGameConnector connector) {
				SwingUtilities.invokeLater(() -> {
					initializeChatFor(connector);
					joinedGame = connector;
					// connector.getPlayers().setListener(changingPlayers -> onPlayersChanges(changingPlayers, connector));XXX
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
					});
			}
		});
	}

	@Override
	protected void onGameStart() {
		if (joinedGame != null) {
			joinedGame.startGame();
		}
	}

	@Override
	protected void onCancel() {
		joiningGame.abort();
		super.onCancel();
	}

}
