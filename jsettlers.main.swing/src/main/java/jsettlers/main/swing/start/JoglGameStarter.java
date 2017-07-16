package jsettlers.main.swing.start;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import go.graphics.area.Area;
import go.graphics.jogl.AreaContainer;

public class JoglGameStarter implements IGameStarter {

	private Timer redrawTimer;

	@Override
	public synchronized void startGame(Area contentArea, JFrame mainFrame, boolean fullScreen) {
		redrawTimer = new Timer("opengl-redraw");
		redrawTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				contentArea.requestRedraw();
			}
		}, 100, 25);

		SwingUtilities.invokeLater(() -> {
			mainFrame.setContentPane(new AreaContainer(contentArea));
			mainFrame.revalidate();
			mainFrame.repaint();
		});
	}

	@Override
	public synchronized void abortGame() {
		if (redrawTimer != null) {
			redrawTimer.cancel();
			redrawTimer = null;
		}
	}

	@Override
	public void setFullScreenMode(boolean fullScreen) {
		//  Ignored. Main window is reused and main window logic will do this for us.
	}

}
