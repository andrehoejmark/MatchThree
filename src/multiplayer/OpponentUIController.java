package multiplayer;

import controller.MatchThreeHeaderController;
import controller.UIController;
import java.awt.Container;
import model.Settings;
import view.GridView;
import view.MatchThreeUI;

/**
 * Opponent UI controller. Listens for network messages and updates model
 * accordingly.
 *
 * @author Linus
 */
public class OpponentUIController
{
	/** Default game size. */
	private static final int GAME_SIZE = Settings.getGameSize();
	
	/** Opponent controller (for receiving data from opponent). */
	private OpponentController opponentController = null;
	
	/**
	 * Constructor.
	 *
	 * @param parent        Parent view to use.
	 * @param uiController  UI controller to use.
	 * @param settings      Application settings.
	 * @param opponentModel Model to use.
	 * @param port          Port number to listen on.
	 */
	public OpponentUIController(
		final Container       parent,
		final UIController    uiController,
		final Settings        settings,
		final OpponentModel opponentModel,
		final int port
	) {
		// Validate arguments //
		if (parent == null) {
			throw new IllegalArgumentException("`parent` must not be null");
		}
		if (uiController == null) {
			throw new IllegalArgumentException(
				"`uiController` must not be null"
			);
		}
		if (settings == null) {
			throw new IllegalArgumentException("`settings` must not be null");
		}

		// Create view //
		MatchThreeUI matchThreeUI = new MatchThreeUI(opponentModel);
		
		// Create grid //
		Container gridView = matchThreeUI.getGrid();
		gridView.add(new GridView(opponentModel, settings.getStyle()));

		// Create header //
		//Container headerView = matchThreeUI.getHeader();
		new MatchThreeHeaderController(
			matchThreeUI,
			uiController,
			settings,
			opponentModel
		);
		
		// Start opponent controller (UDP listener) //
		opponentController = new OpponentController(
				uiController,
				settings,
				opponentModel,
				port,
				gridView
		);
		
		// Start listening for data on UDP port //
		opponentController.start();
		
		// Add view to parent //
		parent.add(matchThreeUI);
	}
	
	/**
	 * Close socket and listening thread.
	 */
	public void close() {
		opponentController.close();
	}
	
}
