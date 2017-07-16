package jsettlers.main.swing.start;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import go.graphics.area.Area;
import go.graphics.lwjgl.LwjglWindow;
import jsettlers.main.swing.JSettlersFrame;

/**
 * Start the game using a lwjgl window. Hide the swing menu
 * @author Michael Zangl
 */
public class LwjglGameStarter implements IGameStarter {

	private JFrame mainFrame;
	private LwjglWindow lwjglWindow;

	public LwjglGameStarter() {
	}

	@Override
	public synchronized void startGame(Area contentArea, JFrame mainFrame, boolean fullScreen) {
		this.mainFrame = mainFrame;
		mainFrame.setVisible(false);

		// TODO: make this nice
		lwjglWindow = new LwjglWindow(contentArea, mainFrame.getBounds(), mainFrame.getTitle()) {
			@Override
			protected void onClose() {
				super.onClose();
				if (LwjglGameStarter.this.mainFrame != null) {
					// exit on close.
					System.exit(0);
				}
			}
		};
		throw new RuntimeException();
	}

	@Override
	public synchronized void abortGame() {
		if (mainFrame != null) {
			mainFrame.setVisible(true);
			mainFrame = null;
			lwjglWindow.close();
		}
	}

	@Override
	public void setFullScreenMode(boolean fullScreen) {
		// TODO Auto-generated method stub
		
	}

}
