
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class Client extends Thread{
	private Socket socketClient;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private String username;
	private String opponent;
	private int turn;

	private Message messageReceived;
	private Consumer<Serializable> callback;

	public Client(Consumer<Serializable> call){
		callback = call;
	}

	public void run() {
		try {
			System.out.println("running thread");
			socketClient= new Socket("127.0.0.1",5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		while(true){
			try{
				messageReceived = (Message) in.readObject();

				if (messageReceived != null && messageReceived.getCommand() != null) {

					if (Objects.equals(messageReceived.getCommand(), "FIRST_TURN")) {
						handleFirstTurnResult();
					} else if (Objects.equals(messageReceived.getCommand(), "OPPONENT_FOUND")) {
						handleOpponentFound();
					} else if(Objects.equals(messageReceived.getCommand(), "SET_USERNAME_PASS")){
						handleSetUsernamePass();
					} else if(Objects.equals(messageReceived.getCommand(), "SET_USERNAME_FAIL")){
						handleSetUsernameFail();
					} else if(Objects.equals(messageReceived.getCommand(), "MESSAGE_OPPONENT")) {
						handleMessageFromOpponent();
					}else if(Objects.equals(messageReceived.getCommand(), "SET_EMOTE")){
						handleEmoteFromOpponent();
					}else if(Objects.equals(messageReceived.getCommand(), "SET_GAME_MATRIX")) {
						handleReceiveGameMatrix();
					}else if(Objects.equals(messageReceived.getCommand(), "CHANGE_TURN")){
						handleChangeTurns();
					}else if(Objects.equals(messageReceived.getCommand(), "LEAVE_QUEUE")){
						handleLeaveQueue();
					}else if(Objects.equals(messageReceived.getCommand(), "REFRESH_LEADERBOARD")){
						handleRefreshLeaderBoard();
					}else if(Objects.equals(messageReceived.getCommand(), "OPPONENT_LEFT")){
						handleOpponentLeave();
					}else if(Objects.equals(messageReceived.getCommand(), "UPDATE_MONEY")){
						handleMoneyUpdate();
					} else {
						System.out.println("[SERVER] Error: INVALID COMMAND");
					}
				}

			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void requestDcOpponent(){
		try {
			out.reset();
			out.writeObject(new Message("DC_OPPONENT"));
			out.flush();
		}catch (Exception e){

		}
	}
	private void handleMoneyUpdate(){
		callback.accept(messageReceived);
	}

	public void requestUpdateMoney(int money){
		try {
			System.out.println("[UPDATE_MONEY] Asking server to refresh buy");

			out.reset();
			out.writeObject(new Message("UPDATE_MONEY", money, false));
			out.flush();

			// jose,350
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void handleOpponentLeave(){
		System.out.println("[OPPONENT_LEFT] Opponent has left the match!");

		opponent = null;
		callback.accept(messageReceived);
	}

	private void handleRefreshLeaderBoard(){
		System.out.println("[REFRESH_LEADERBOARD_FINISH] Leaderboard refreshed!");

		callback.accept(messageReceived);
	}

	public void requestRefreshLeaderBoard(){
		try {
			System.out.println("[REFRESH_LEADERBOARD] Asking server to refresh leaderboard");
			out.reset();
			out.writeObject(new Message( "REFRESH_LEADERBOARD"));
			out.flush();

		} catch (Exception e){
			e.printStackTrace();

		}
	}
	public void requestToSendWin(boolean isWon){
		if(isWon){
			try {
				System.out.println("[SEND_NEW_WIN] Sending score to Server");
				out.reset();
				out.writeObject(new Message( "SEND_NEW_WIN"));
				out.flush();

			} catch (Exception e){
				e.printStackTrace();

			}
		}
	}

	private void handleLeaveQueue(){
		System.out.println("[LEAVE_QUEUE] left queue");
		callback.accept(messageReceived);
	}
	public void handleChangeTurns(){
		System.out.println("[CHANGE_TURN] CHANGING TURN");
		callback.accept(messageReceived);
	}

	private void handleReceiveGameMatrix(){
		System.out.println("[SET_GAME_MATRIX] Opponent sent a new game matrix");
		callback.accept(messageReceived);
	}

	public void requestChangeTurn(ArrayList<String> moves){
		try{
			System.out.println("[CHANGE_TURN] requesting change turn");
			out.reset();
			out.writeObject(new Message( "CHANGE_TURN",moves));
			out.flush();

		}catch (Exception e){
			e.printStackTrace();

		}
	}

	public void requestLeaveQueue(){
		System.out.println("[LEAVE_QUEUE] User is trying to leave queue");
		try {
			out.reset();
			out.writeObject(new Message("LEAVE_QUEUE"));
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void requestSendGameMatrix(boolean[][] gameMatrix, ArrayList<ArrayList<String>> shipCoordArray){
		System.out.println("[SET_GAME_MATRIX] User is trying to send the game matrix");

		try {
			out.reset();
			out.writeObject(new Message("SET_GAME_MATRIX", gameMatrix, shipCoordArray, username, opponent));
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleEmoteFromOpponent(){
		System.out.println("[SET_EMOTE] opponent sent emote: " + messageReceived.getMessage());
		callback.accept(messageReceived);
	}

	public void requestToEmote(String emoteName){
		System.out.println("[SET_EMOTE] User is trying to send emote");

		try {
			out.reset();
			out.writeObject(new Message("SET_EMOTE", emoteName));
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleOpponentFound(){
		opponent = messageReceived.getMessage();
		System.out.println("[OPPONENT_FOUND] Found opponent: " + opponent);

		callback.accept(messageReceived);
	}

	private void handleFirstTurnResult(){
		this.turn = messageReceived.getNumber();

		System.out.println("[FIRST_TURN_RESULT] Result: " + turn);
	}

	public void requestToEnterQueue(String avatar){
		try {
			System.out.println("[FIND_OPPONENT] Trying to find opponent");

			out.reset();
			out.writeObject(new Message("FIND_OPPONENT", "",avatar));
			out.flush();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void requestToMessageOpponent(String messageString) {
		System.out.println("message: " + messageString);

		Message messageToSend = new Message("MESSAGE_OPPONENT",username + ": " + messageString);

		try {
			out.reset();
			out.writeObject(messageToSend);
			out.flush();

			System.out.println("[MESSAGE_OPPONENT] Sent message to " + opponent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMessageFromOpponent(){
		String opponentReceivedFrom = messageReceived.getSender();

		System.out.println("[MESSAGE_OPPONENT] Received message from " + opponentReceivedFrom);

		callback.accept(messageReceived);
	}

	private void handleSetUsernamePass(){
		this.username = messageReceived.getMessage();
		System.out.println("[SET_USERNAME_PASS] Username set to: " + username);

		callback.accept(messageReceived);
	}

	private void handleSetUsernameFail(){
		this.username = messageReceived.getMessage();
		System.out.println("[SET_USERNAME_FAIL] Failed to set username to: " + username);

		callback.accept(messageReceived);
	}

	public void requestSetUsername(String username){
		System.out.println("[SET_USERNAME_START] User is trying to set username");

		try {
			out.reset();
			out.writeObject(new Message("SET_USERNAME", username));
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}

	public String getOpponent() {
		return opponent;
	}

	public int getTurn() {
		return turn;
	}
}
