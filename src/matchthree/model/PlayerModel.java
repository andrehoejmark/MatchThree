package matchthree.model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import matchthree.controller.Server;
import matchthree.message.Message;
import matchthree.message.UpdateBoard;
import matchthree.message.UpdateCell;
import matchthree.message.UpdateGameFinished;
import matchthree.message.UpdateMovesLeft;
import matchthree.message.UpdateScore;

/**
 * Extension of MatchThreeModel used for the player view to send datagrams to
 * opponent.
 *
 * @author Linus Aronsson
 * @author Erik Selstam
 */
public class PlayerModel
	extends MatchThreeModel
{
	/** Socket for sending data to the opponent. */
	private DatagramSocket client = null;
	
	/** Packet to be sent over the socket. */
	private DatagramPacket out = null;
	
	/** Opponent address. */
	private InetAddress ip = null;
	
	/** Opponent port number. */
	private int port;
	
	/** Whether currently in-game. */
	private boolean gameStarted = false;
	
	/**
	 * Overloaded constructor for also initializing the model
	 * with a board.
	 *
	 * @author Linus Aronsson
	 * @param board Initial board.
	 * @param width Size of the board.
	 * @param host  IP of opponent.
	 * @param port  Port of opponent.
	 */
	public PlayerModel(
		final Jewel[]     board,
		final int         width,
		final InetAddress host,
		final int         port)
	{
		super(board, width);
		
		// Create socket //
		try {
			client = new DatagramSocket();
		} catch (final SocketException exception) {
			exception.printStackTrace();
		}
		
		// Assign fields //
		this.ip   = host;
		this.port = port;
	}
	
	/**
	 * Initializes the object and creates a new DatagramSocket
	 * for sending datagram packets.
	 *
	 * @author Linus Aronsson
	 * @param width Size of the board.
	 * @param host  IP of opponent.
	 * @param port  Port of opponent.
	 */
	public PlayerModel(
		final int         width,
		final InetAddress host,
		final int         port)
	{
		this(null, width, host, port);
	}
	
	/**
	 * Sets if the game was started.
	 *
	 * @author Erik Selstam
	 * @param b Game started flag
	 */
	public void setGameStarted(final boolean b) {
		gameStarted = b;
	}
	
	/**
	 * Set the value of a cell. May leave the board in an inconsistent state.
	 * Also sends the updated cell to the opponent.
	 *
	 * <p>
	 * This override also notifies the opponent of the change by sending
	 * a datagram packet.
	 * </p>
	 *
	 * @author Linus Aronsson
	 * @param x     X-coordinate of the cell.
	 * @param y     Y-coordinate of the cell.
	 * @param value Value to set.
	 */
	@Override
	public void set(final int x, final int y, final Jewel value) {
		super.set(x, y, value);
		// Notify opponent //
		if (gameStarted) {
			notifyOpponent(new UpdateCell(x, y, value));
		}
	}
	
	/**
	 * Sets the moves left. Also notifies the opponent the the moves left
	 * value has changed.
	 *
	 * @author Linus Aronsson
	 */
	@Override
	public void setMovesLeft() {
		super.setMovesLeft();
		if (gameStarted) {
			notifyOpponent(new UpdateMovesLeft(movesLeft));
			if (movesLeft == 0) {
				notifyOpponent(new UpdateGameFinished());
			}
		}
	}
	
	/**
	 * Move a cell and clear any generated chains. Leaves the board in a
	 * consistent state. Also sends the new score to opponent.
	 *
	 * <p>
	 * Also notifies the opponent of the new score.
	 * </p>
	 *
	 * @author Linus Aronsson
	 * @param from Source coordinates.
	 * @param to   Destination coordinates.
	 * @return Whether the move was successful, invalid or canceled.
	 */
	@Override
	public MoveType move(final Coordinate from, final Coordinate to) {
		MoveType type = super.move(from, to);
		if (gameStarted) {
			notifyOpponent(new UpdateScore(score));
		}
		return type;
	}
	
	/**
	 * Sends the entire board over. This is only done once at the start.
	 *
	 * @author Linus Aronsson
	 * @param port Port of opponent.
	 */
	public void sendBoard(final int port) {
		Server.sendDatagram(new UpdateBoard(board), client, ip, port);
	}
	
	/**
	 * General method for notifying the opponent with a specified
	 * message.
	 *
	 * @author Linus Aronsson
	 * @param message The message to be sent in a datagram packet.
	 */
	private void notifyOpponent(final Message message) {
		Server.sendDatagram(message, client, ip, port);
	}
}
