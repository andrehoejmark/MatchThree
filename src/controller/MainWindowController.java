package controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import multiplayer.Server;
import view.ErrorDialog;
import view.MainMenuBar;
import view.Window;

/**
 * Main window controller.
 */
public class MainWindowController
{
	/** Default window title. */
	private static final String WINDOW_TITLE = "MatchThree";
	
	/** Reference to MatchThree controller. */
	private MatchThreeController matchThreeController = null;
	
	/** Window view. */
	private Window window = null;
	
	/**
	 * Listens for window events.
	 */
	class WindowListener
		extends WindowAdapter
	{
		@Override
		public void windowActivated(final WindowEvent event) { }
		
		@Override
		public void windowClosed(final WindowEvent event) {
			// TODO: Avoid directly closing the application.
			// TODO: Use constant for exit code.
			System.exit(0);
		}
		
		@Override
		public void windowClosing(final WindowEvent event) {
			// Close windows and free its resources //
			// TODO: Is it necessary to use `view.setVisible(false)` as well?
			window.dispose();
		}
		
		@Override
		public void windowDeactivated(final WindowEvent event) { }
		
		@Override
		public void windowDeiconified(final WindowEvent event) { }
		
		@Override
		public void windowGainedFocus(final WindowEvent event) { }
		
		@Override
		public void windowIconified(final WindowEvent event) { }
		
		@Override
		public void windowLostFocus(final WindowEvent event) { }
		
		@Override
		public void windowOpened(final WindowEvent event) { }
		
		@Override
		public void windowStateChanged(final WindowEvent event) { }
	}
	
	/**
	 * Create `MainWindowController`.
	 */
	public MainWindowController() {
		// TODO: Fix this.
		this.matchThreeController = null;
		
		// Create main window //
		// TODO: Consider removing this panel.
		JPanel panel = new JPanel();
		MainMenuBar menuBar = new MainMenuBar();
		window = new Window(WINDOW_TITLE, menuBar, panel);
		
		// Create UI //
		UIController uiController = new UIController(panel, this);
		
		// Resize window //
		pack();
		
		// Register event listeners //
		menuBar.addNewListener(event -> {
			// Restart the game //
			matchThreeController.restartGame();
		});
		menuBar.addOpenListener(event -> {
			// TODO: Not implemented.
			new ErrorDialog("Function not implemented", "Open not implemented");
		});
		menuBar.addQuitListener(event -> {
			// Close main window //
			closeWindow();
		});
		menuBar.addSaveListener(event -> {
			// Save the game //
			matchThreeController.saveGame();
		});
		window.addWindowListener(new WindowListener());
		
		// Start server //
		new Server(window, uiController, 3333).start();
	}
	
	/**
	 * Close main window.
	 */
	private void closeWindow() {
		WindowEvent e = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
		window.dispatchEvent(e);
	}
	
	/**
	 * Update window with content.
	 */
	public void pack() {
		window.pack();
	}
	
	public void centerWindow() {
		window.centerWindow();
	}
}