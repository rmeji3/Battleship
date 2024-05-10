
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public class Server{
	private int clientCount = 1;
	private ArrayList<ClientThread> clientThreads = new ArrayList<>();

	private HashMap<String, ClientThread> usernamesMap = new HashMap<>(); // Username and ClientThread

	private Queue<ClientThread> gameQueue = new LinkedList<>();

	private ArrayList<String> leaderboard = new ArrayList<>();
	private ArrayList<String> moneyList = new ArrayList<>();

	private TheServer server;
	private Consumer<Serializable> callback;

	public Server(Consumer<Serializable> call){
		loadLeaderboardFromFile();
		loadMoneyListFromFile();

		callback = call;
		server = new TheServer();
		server.start();
	}

	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(5555);){
				System.out.println("Server is waiting for a client!");

				while(true) {
					ClientThread c = new ClientThread(mysocket.accept(), clientCount);
					callback.accept("New Client has connected to server: Client #" + clientCount);
					clientThreads.add(c);
					c.start();

					clientCount++;

				}
			}
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}
	}
	class ClientThread extends Thread{
		Socket connection;
		ObjectInputStream in;
		ObjectOutputStream out;

		boolean[][] currMatrix;

		ArrayList<ArrayList<String>> currShipCoordArray;
		String username;
		int count;

		boolean ready = false;
		ClientThread currOpponent;

		String avatar;

		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;
		}

		public void run(){
			// Setting Streams
			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams are not open!");
			}

			while (true) {
				try {
					Message commandReceived = (Message) in.readObject();
					System.out.println("[CLIENT #" + count + "] CALLED COMMAND: " + commandReceived.getCommand());

					if (Objects.equals(commandReceived.getCommand(), "FIND_OPPONENT")) {
						getOpponent(commandReceived);
					} else if (Objects.equals(commandReceived.getCommand(), "MESSAGE_OPPONENT")) {
						updateClients(commandReceived, true);
					} else if (Objects.equals(commandReceived.getCommand(), "SET_USERNAME")) {
						getUsername(commandReceived);
					} else if(Objects.equals(commandReceived.getCommand(), "SET_EMOTE")){
						emote(commandReceived);
					} else if(Objects.equals(commandReceived.getCommand(), "SET_GAME_MATRIX")) {
						setGameMatrix(commandReceived);
					} else if(Objects.equals(commandReceived.getCommand(), "CHANGE_TURN")){
						changeTurn(commandReceived);
					} else if(Objects.equals(commandReceived.getCommand(), "LEAVE_QUEUE")){
						leaveQueue(commandReceived);
					} else if(Objects.equals(commandReceived.getCommand(), "SEND_NEW_WIN")){
						handleNewWin();
					} else if(Objects.equals(commandReceived.getCommand(), "UPDATE_MONEY")){
						handleNewMoney(commandReceived.getNumber());
					} else if(Objects.equals(commandReceived.getCommand(), "DC_OPPONENT")){
						handleDcOpponent();

					} else if(Objects.equals(commandReceived.getCommand(), "REFRESH_LEADERBOARD")){
						refreshLeaderBoard();
					} else {
						System.out.println("[CLIENT #" + count + "] Error: INVALID COMMAND");
					}
				}
				catch (Exception e) {
					callback.accept("Client #" + count + " has left the server!");
					clientThreads.remove(this);
					usernamesMap.remove(username);
					gameQueue.remove(this);

					notifyOpponentLeave();

					break;
				}
			}
		}//end of run

		public void handleDcOpponent(){
			System.out.println("dc opponent");
			this.currOpponent = null;
		}
		public void updateMoney(boolean b){
			try {
				int money = findMoney();

				if(money != -1) {
					System.out.println("[UPDATE_MONEY] Sending " + this.username + " their money: " + money);

					out.reset();
					out.writeObject(new Message("UPDATE_MONEY", money, true));
					out.flush();
				}
				else{
					System.out.println("[UPDATE_MONEY] Sending " + this.username + " NEW DEFAULT money: " + money);

					moneyList.add(username +"," + 500);
					if(b)
						saveMoneyListToFile();

					out.reset();
					out.writeObject(new Message("UPDATE_MONEY", 500, true));
					out.flush();
				}
			} catch (Exception e){

			}
		}

		public int findMoney() {
			String filePath = "/money.txt";

			try (InputStream inputStream = Server.class.getResourceAsStream(filePath);
				 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length == 2) {
						String name = parts[0].trim();
						if (name.equals(username)) {
							int money = Integer.parseInt(parts[1].trim());
							reader.close();
							return money;
						}
					}
				}
				reader.close();

				return -1; // Username not found
			}
			catch (Exception e){
				e.printStackTrace();
			}

			return -1;
		}

		public void handleNewMoney(int money) {
			for (int i = 0; i < moneyList.size(); i++) {
				String[] parts = moneyList.get(i).split(",");
				if (parts[0].equals(username)) {
					int totalMoney = Integer.parseInt(parts[1]);
					totalMoney = money;

					moneyList.set(i, username + "," + totalMoney);
					break;
				}

				moneyList.add(username + "," + money);
			}

				saveMoneyListToFile();
		}

		public void notifyOpponentLeave(){
			if(currOpponent != null){
				try {
					currOpponent.out.reset();
					currOpponent.out.writeObject(new Message("OPPONENT_LEFT"));
					currOpponent.out.flush();
				} catch (Exception e){

				}
			}

		}

		public void refreshLeaderBoard(){
			try {
				out.reset();
				out.writeObject(new Message("REFRESH_LEADERBOARD", leaderboard));
				out.flush();
			} catch (Exception e){

			}
		}

		public void handleNewWin() {
			boolean found = false;

			for (int i = 0; i < leaderboard.size(); i++) {
				String[] parts = leaderboard.get(i).split(",");
				if (parts[0].equals(username)) {
					int wins = Integer.parseInt(parts[1]);
					wins++;

					leaderboard.set(i, username + "," + wins);
					found = true;

					break;
				}
			}
			if (!found) {
				leaderboard.add(username + ",1");
			}
			System.out.println(leaderboard);

			saveLeaderboardToFile();
			updateLeaderboard();
		}



		private void leaveQueue(Message message){
			System.out.println("[LEAVE_QUEUE] leaving queue");
			try{
				gameQueue.remove(this);
				out.reset();
				out.writeObject(message);
				out.flush();
			}catch (Exception e){

			}
		}
		private void changeTurn(Message message){
			System.out.println("[CHANGE_TURN] changing turns");
			try{
				currOpponent.out.reset();
				currOpponent.out.writeObject(message);
				currOpponent.out.flush();
			}catch (Exception e){

			}
		}

		private void setGameMatrix(Message command){
			this.ready = true;

			if(currOpponent.ready){
				try{
					currOpponent.out.reset();
					currOpponent.out.writeObject(new Message("SET_GAME_MATRIX", command.getMatrix(), command.getShipCoordArray()));
					currOpponent.out.flush();

					out.reset();
					out.writeObject(new Message("SET_GAME_MATRIX", currOpponent.currMatrix, currOpponent.currShipCoordArray));
					out.flush();



				}catch (Exception e) {

				}

			}
			currShipCoordArray = command.getShipCoordArray();
			currMatrix = command.getMatrix();
		}

		//will send a random num to op and you to decide who goes first
		//1 = go first
		//2 = go second
		private void requestToSetFirstTurn(){
			System.out.println("[FIRST_TURN_SETUP] Setting first turns for " + this.username + "(host) AND " + currOpponent.username + "(Opponent)");

			Random rand = new Random();
			int randomTurnValue = rand.nextInt(1,3); // random 1 or 2

			if(randomTurnValue == 1) {
				try {
					System.out.println("[FIRST_TURN_RESULT] First turn result: " + this.username + " goes first");

					out.reset();
					out.writeObject(new Message("FIRST_TURN", 1, true));
					out.flush();

					currOpponent.out.reset();
					currOpponent.out.writeObject(new Message("FIRST_TURN", 2, true));
					currOpponent.out.flush();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			else if(randomTurnValue == 2){
				try {
					System.out.println("[FIRST_TURN_RESULT] First turn result: " + this.currOpponent.username + " goes first");

					out.reset();
					out.writeObject(new Message("FIRST_TURN", 2, true));
					out.flush();

					currOpponent.out.reset();
					currOpponent.out.writeObject(new Message("FIRST_TURN", 1, true));
					currOpponent.out.flush();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		private void getOpponent(Message message) {
			if (!gameQueue.isEmpty()) {
				avatar = message.getAvatar();
				currOpponent = gameQueue.remove();
				currOpponent.currOpponent = this;

				System.out.println("[FIND_OPPONENT] Found opponent: " + this.username + " VS " + currOpponent.username + " (Opponent)");

				try {
					out.reset();
					out.writeObject(new Message("OPPONENT_FOUND", currOpponent.username, currOpponent.avatar ));
					out.flush();

					currOpponent.out.reset();
					currOpponent.out.writeObject(new Message("OPPONENT_FOUND", this.username, avatar));
					currOpponent.out.flush();

					requestToSetFirstTurn();

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				gameQueue.add(this);
				avatar = message.getAvatar();
				System.out.println("[OPPONENT_QUEUE] Adding " + this.username + " to the queue");
			}
		}

		private void getUsername(Message commandReceived) {
			System.out.println("[SET_USERNAME] Client #" + count + " is trying to set username");

			String username = commandReceived.getMessage();

			// Check if the username already exists
			try {
				if (usernamesMap.containsKey(username)) {
					this.username = null;

					out.reset();
					out.writeObject(new Message("SET_USERNAME_FAIL"));
					out.flush();

					System.out.println("[SET_USERNAME_EXISTS] Username already exists!");
				}
				// Check if the username is good
				else {
					this.username = username;

					usernamesMap.put(username, this);

					out.reset();
					out.writeObject(new Message("SET_USERNAME_PASS", this.username));
					out.flush();

					System.out.println("[SET_USERNAME_GOOD] Username made successfully! " + username);

					updateMoney(commandReceived.isB());
				}
			}catch(Exception e){}
		}

		public void emote(Message message){
			try{
				System.out.println("[SET_EMOTE] sending " + currOpponent.username + " " + message);
				currOpponent.out.reset();
				currOpponent.out.writeObject(new Message("SET_EMOTE", message.getMessage()));
				currOpponent.out.flush();
			}catch (Exception e){

			}
		}


		public void updateClients(Message message, boolean isPersonal) {
			if (isPersonal) {
				// Send message to specific client

				if (currOpponent != null) {
					try {
						currOpponent.out.reset();
						currOpponent.out.writeObject(message);
						currOpponent.out.flush();

						out.reset();
						out.writeObject(message);
						out.flush();
						System.out.println("[updateClients] " + this.username + " sent to " + currOpponent.username);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("[updateClients] Error: Receiver not found");
				}
			} else {
				// Send message to all clients
				for (ClientThread t : clientThreads) {
					try {
						t.out.reset();
						t.out.writeObject(message);
						t.out.flush();
						System.out.println("[updateClients] Client #" + count + " sent to everyone");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}//end of client thread

	private void loadLeaderboardFromFile() {
		String filePath = "/leaderboard.txt";

		// Try-with-resources statement to ensure that each resource is closed at the end of the statement
		try (InputStream inputStream = Server.class.getResourceAsStream(filePath);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line); // Print each line
				leaderboard.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace(); // Handle possible IO exceptions like file not found or unreadable file.
		}
	}

	private void saveLeaderboardToFile() {
		String filePath = "src/main/resources/leaderboard.txt"; // Ensure this is the correct path for your project
		File file = new File(filePath);
		try (PrintWriter out = new PrintWriter(file)) {
			for (int i = 0; i < leaderboard.size(); i++) {
				if (i < leaderboard.size() - 1) {
					out.println(leaderboard.get(i));
				} else {
					out.print(leaderboard.get(i));  // No newline for the last entry
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateLeaderboard() {
		// Sort the leaderboard based on the number of wins in descending order
		leaderboard.sort((a, b) -> {
			int winsA = Integer.parseInt(a.split(",")[1]);
			int winsB = Integer.parseInt(b.split(",")[1]);
			return Integer.compare(winsB, winsA);
		});
	}

	private void loadMoneyListFromFile() {
		String filePath = "/money.txt";

		// Try-with-resources statement to ensure that each resource is closed at the end of the statement
		try (InputStream inputStream = Server.class.getResourceAsStream(filePath);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line); // Print each line
				moneyList.add(line);
			}
			System.out.println("moneylist size: " + moneyList.size());

		} catch (IOException e) {
			e.printStackTrace(); // Handle possible IO exceptions like file not found or unreadable file.
		}
	}

	private void saveMoneyListToFile() {
		System.out.println("here");
		String filePath = "src/main/resources/money.txt"; // Ensure this is the correct path for your project
		File file = new File(filePath);
		try (PrintWriter out = new PrintWriter(file)) {
			for (int i = 0; i < moneyList.size(); i++) {
				System.out.println("line");
				if (i < moneyList.size() - 1) {
					out.println(moneyList.get(i));
				} else {
					out.print(moneyList.get(i));  // No newline for the last entry
				}
				if(i == 20){
					break;
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open file: " + e.getMessage());
			e.printStackTrace();
		}
	}
}