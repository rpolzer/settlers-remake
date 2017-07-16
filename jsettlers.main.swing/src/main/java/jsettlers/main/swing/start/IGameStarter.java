package jsettlers.main.swing.start;

import javax.swing.JFrame;

import go.graphics.area.Area;

/**
 * Starts the game
 * @author Michael Zangl
 */
public interface IGameStarter {
	void startGame(Area contentArea, JFrame mainFrame, boolean fullScreen);
	
	void abortGame();

	void setFullScreenMode(boolean fullScreen);
}
