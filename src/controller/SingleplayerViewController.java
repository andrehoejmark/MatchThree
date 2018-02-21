package controller;

import java.awt.Container;
import model.Settings;
import view.SingleplayerView;

/**
 * Controller for singleplayer game screen.
 */
public class SingleplayerViewController
{
	/** Default game size. */
	private static final int GAME_SIZE = Settings.getGameSize();
	
	/** Reference to UI controller. */
	private UIController uiController = null;
	
	/**
	 * Constructor.
	 *
	 * @param parent       Parent container view to use.
	 * @param uiController UI controller to use for navigation.
	 * @param settings     ...
	 */
	public SingleplayerViewController(
		final Container    parent,
		final UIController uiController,
		final Settings     settings
	) {
		// Validate arguments //
		if (parent == null) {
			throw new NullPointerException();
		}
		if (uiController == null) {
			throw new NullPointerException();
		}
		
		this.uiController = uiController;
		
		// Create singleplayer view //
		// TODO: Add separate controller for button panel?
		SingleplayerView singleplayerView = new SingleplayerView();
		
		// Create MatchThree game //
		Container gameView = singleplayerView.getGame();
		MatchThreeUIController matchThreeUIController =
			new MatchThreeUIController(gameView, uiController, settings, null);
		
		// Create button panel //
		Container buttonPanel = singleplayerView.getButtonPanel();
		GridViewController gridViewController =
			matchThreeUIController.getGridViewController();
		ButtonPanelController buttonPanelController = new ButtonPanelController(
			buttonPanel,
			uiController,
			gridViewController,
			settings
		);
		
		// Add view to parent //
		parent.add(singleplayerView);
	}
}
