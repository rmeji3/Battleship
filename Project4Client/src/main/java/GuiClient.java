import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;


public class GuiClient extends Application{

	boolean[][] gameMatrix;
	Random rand = new Random();
	ArrayList<String> emoteInv = new ArrayList<>();
	ArrayList<String> charInv = new ArrayList<>();

	ArrayList<String> currEmoteInv = new ArrayList<>();
	ArrayList<String> currCharInv = new ArrayList<>();
	ArrayList<String> aiCharsList = new ArrayList<>();


	ArrayList<ArrayList<String>> shipCoordArray;

	LoginScene loginScene;
	TranslateTransition hidePanel;

	GameScene gameScene;
	PreGameScene preGameScene;

	EndScene endScene;
	AiGameScene aiGameScene;

	int currMoney = 0;
	int totalMoney = 0;

	Client client;

	boolean vsAi = true;

	GameLogic gameLogic;

	String username;

	public static void main(String[] args) {
		launch(args);
	}

	public void playBgSound(String file){
		try {
			InputStream audioSrc = getClass().getResourceAsStream(file);
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

			gainControl.setValue(-40.0f);

			// Loop indefinitely
			clip.loop(Clip.LOOP_CONTINUOUSLY);

			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playEmoteSound(String file){
		//   /emotes/emote.png
		file = file.substring(0,file.lastIndexOf('.'));
		file += ".wav";
		try {
			InputStream audioSrc = getClass().getResourceAsStream(file);
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

			// Set the gain (volume). Reduce volume by 30 decibels.
			if(!file.equals("/emotes/Emote2.wav"))
				gainControl.setValue(-30.0f);

			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startMatch(Stage primaryStage){
		try {
			InputStream audioSrc = getClass().getResourceAsStream("/Sounds/matchFound.wav");
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

			// Set the gain (volume). Reduce volume by 20 decibels.
			gainControl.setValue(-20.0f);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		HBox h = loginScene.getWaitText();
		loginScene.getLeaveQueueBtn().setDisable(true);

		Text t = (Text) h.getChildren().get(0);
		t.setText(client.getUsername() + " vs " + client.getOpponent());
		PauseTransition transitionToJose = new PauseTransition(Duration.seconds(2));
		transitionToJose.play();
		transitionToJose.setOnFinished(ev -> {
			primaryStage.setScene(preGameScene.getScene());
			loginScene.getLeaveQueueBtn().setDisable(false);
			gameScene.setGameAfterTurn(client.getTurn());
			t.setText("");
			preGameScene.getCloudTransition().play();
		});
	}
	private String randomChar(){
		return aiCharsList.get(rand.nextInt(aiCharsList.size()));
	}
	@Override
	public void start(Stage primaryStage) throws Exception {

		aiCharsList.add("/captainImg/CaptainBlue2.png");
		aiCharsList.add("/captainImg/CaptainRed2.png");
		aiCharsList.add("/captainImg/fortnite.png");
		aiCharsList.add("/captainImg/mccarty.png");

		for (int i = 1; i < 10;i++)
			emoteInv.add("/emotes/Emote" + i + ".png");
		primaryStage.setTitle("Battleship");
		primaryStage.getIcons().add(new Image("/icon.png"));

		charInv.add("/captainImg/CaptainBlue2.png");
		charInv.add("/captainImg/CaptainRed2.png");

		gameMatrix = new boolean[10][10];
		loginScene = new LoginScene(primaryStage);
		loginScene.refreshInvEmotes(emoteInv, true);
		loginScene.refreshInvChars(charInv, true);
		playBgSound("/Sounds/bgMusic.wav");
		gameScene = new GameScene(primaryStage);
		preGameScene = new PreGameScene(primaryStage, gameScene.getScene(), gameMatrix, null, null);
		loginScene.getBuy().setOnAction(event -> {

			ArrayList<Button> boughtItems = loginScene.getBoughtItems();
			Button lastItemSelected = loginScene.getLastItemSelected();
			if(!boughtItems.contains(lastItemSelected)) {
				String fileName = (String)lastItemSelected.getUserData();
				int cost;
				if(fileName.contains("Emote"))
					cost = 500;
				else
					cost = 1000;
				if(currMoney >= cost){
					currMoney -= cost;
					loginScene.setMoney(currMoney);
					if(fileName.contains("Emote")){
						emoteInv.add(fileName);
						loginScene.refreshInvEmotes(emoteInv, false);
					}else {
						charInv.add(fileName);
						loginScene.refreshInvChars(charInv, false);
					}


//					client.requestUpdateMoney(-cost);
					System.out.println("purchased: " + fileName + ", new balance: " + currMoney);
					boughtItems.add(lastItemSelected);
					lastItemSelected.setStyle("-fx-background-color: rgba(58, 255, 114, 0.83)");
					DropShadow boughtShadow = new DropShadow();
					boughtShadow.setRadius(10.0);
					boughtShadow.setOffsetX(5.0);
					boughtShadow.setOffsetY(8.0);
					boughtShadow.setRadius(0);
					boughtShadow.setColor(Color.color(46/255.0, 208/255.0, 92/255.0, 0.83));
					lastItemSelected.setDisable(true);
					lastItemSelected.setEffect(boughtShadow);
					if(boughtItems.size() == 8)
						loginScene.getBuy().setDisable(true);
				}else {
					System.out.println("not enough for : " + fileName + ", costs: " + cost);
					loginScene.showText("insufficient funds");
				}
			}
		});


		endScene = new EndScene(primaryStage);
		client = new Client(data -> {
			Platform.runLater(() -> {
				Message message = (Message) data;

				switch (message.getCommand()) {
					case "OPPONENT_FOUND":
						gameScene.setOpName(message.getMessage());
						gameScene.setYourName(client.getUsername());
						gameScene.setOpChar(message.getAvatar());
						startMatch(primaryStage);
						break;
					case "SET_USERNAME_PASS":
						loginScene.setWelcomeText(username);
						loginScene.playMenuTrans();
						gameScene.setUsername(username);
						break;
					case "SET_USERNAME_FAIL":
						loginScene.getLoginField().clear();
						loginScene.getLoginField().setPromptText("user name taken");
						break;
					case "MESSAGE_OPPONENT":
						String messageString = message.getMessage();

						System.out.println(messageString);
						gameScene.updateMessageUI(messageString);
						break;
					case "SET_EMOTE":
						gameScene.enemyEmote(message.getMessage());
						if(primaryStage.getScene().equals(gameScene.getScene()))
						{
							playEmoteSound(message.getMessage());
						}
						break;
					case "SET_GAME_MATRIX":
						gameScene.revertAfterReady();
						gameLogic = new GameLogic(preGameScene.getGameMatrix(),message.getMatrix());
						gameLogic.setShipCoordArrays(shipCoordArray, message.getShipCoordArray());
						gameScene.setGameLogic(gameLogic);
						break;
					case "CHANGE_TURN":
						System.out.println("changing turn");
						gameScene.playAnimations(message.getStringArrayList());
						break;
					case "LEAVE_QUEUE":
						System.out.println("leaving queue");
						loginScene.leaveQueue();
						break;
					case "REFRESH_LEADERBOARD":
						System.out.println("refreshing leaderboard");
						loginScene.moveLb();
						loginScene.refreshLeaderBoard(message.getStringArrayList());
						break;
					case "OPPONENT_LEFT":
						System.out.println("opponent left");
						client.requestToSendWin(true);
						gameScene.endGame("player", true);
						// opponent is set to null in client side
						// unfinished

						break;
					case "UPDATE_MONEY":
						totalMoney = message.getNumber();
						currMoney = message.getNumber();
						System.out.println("updating money: " + currMoney);
						loginScene.setMoney(totalMoney);
						break;
				}

			});

		});



		client.start();


		Font font = Font.loadFont(getClass().getResourceAsStream("/Blockletter.otf"), 24);

		if (font == null) {
			System.out.println("Font not loaded. Using default font.");
			font = Font.getDefault();
		}

		loginScene.getEasyBtn().setOnAction(e->{




			vsAi = true;
			aiGameScene = new AiGameScene(primaryStage,  "easy");
			aiGameScene.setUsername(username);
			gameMatrix = new boolean[10][10];

			preGameScene = new PreGameScene(primaryStage,aiGameScene.getScene(), gameMatrix, aiGameScene, this);
			preGameScene.setChar(loginScene.getCurrInvChars());
			aiGameScene.setChar(loginScene.getCurrInvChars());
			//TODO JOSE MAKE THE RANDOM CHAR TO PICK A RANDOM AI IMG BUT NOT FOR IMPOSSIBLE THAT WILL JUST BE MCCARTY
			aiGameScene.setAiChar(randomChar());
			setAiGame(primaryStage);

		});
		loginScene.getMediumBtn().setOnAction(e->{




			vsAi = true;
			aiGameScene = new AiGameScene(primaryStage,  "medium");
			aiGameScene.setUsername(username);
			gameMatrix = new boolean[10][10];

			preGameScene = new PreGameScene(primaryStage,aiGameScene.getScene(), gameMatrix, aiGameScene, this);

			preGameScene.setChar(loginScene.getCurrInvChars());
			aiGameScene.setChar(loginScene.getCurrInvChars());
			aiGameScene.setAiChar(randomChar());
			setAiGame(primaryStage);

		});

		loginScene.getImpossibleBtn().setOnAction(e->{




			vsAi = true;
			aiGameScene = new AiGameScene(primaryStage,  "impossible");
			aiGameScene.setUsername(username);
			gameMatrix = new boolean[10][10];

			preGameScene = new PreGameScene(primaryStage,aiGameScene.getScene(), gameMatrix, aiGameScene, this);

			preGameScene.setChar(loginScene.getCurrInvChars());
			aiGameScene.setChar(loginScene.getCurrInvChars());
			aiGameScene.setAiChar("/captainImg/mccarty.png");
			setAiGame(primaryStage);
		});






		loginScene.getLeaderboardBtn().setOnAction(e->{
			client.requestRefreshLeaderBoard();
		});
		loginScene.getLeaveQueueBtn().setOnAction(e->{
			client.requestLeaveQueue();
			loginScene.getLeaveQueueBtn().setDisable(true);
		});

		loginScene.setNextScene(preGameScene.getScene());
		loginScene.getLoginBtn().setOnAction(e -> {
			System.out.println("pressing login");
			String username = loginScene.getLoginField().getText();
			if (username.isEmpty()) {
				loginScene.getLoginField().clear();
				loginScene.getLoginField().setPromptText("enter valid username");
			} else {
				client.requestSetUsername(username.toLowerCase());
				this.username = username;

			}
		});
		loginScene.getOnlineBtn().setOnAction(e -> {
			gameMatrix = new boolean[10][10];
			vsAi = false;
			gameScene = new GameScene(primaryStage);

			preGameScene = new PreGameScene(primaryStage, gameScene.getScene(), gameMatrix, null, null);

			preGameScene.setChar(loginScene.getCurrInvChars());
			System.out.println("setting char: " + loginScene.getCurrInvChars());

			gameScene.setChar(loginScene.getCurrInvChars());
			gameScene.setEmotes(loginScene.getCurrInvEmotes());

			loginScene.pCloudTrans().play();
			gameScene.setUsername(username);
			gameScene.setMovesReceivedListener(data->{
				System.out.println("here");
				client.requestChangeTurn(data);
			});

			gameScene.setWinReceivedListener(didWin -> {
				if(didWin)
					client.requestUpdateMoney(500);
				else
					client.requestUpdateMoney(100);

				client.requestToSendWin(didWin);
			});
			loginScene.pCloudTrans().setOnFinished(ev -> {
				client.requestToEnterQueue(loginScene.getCurrInvChars());
				loginScene.getLeaveQueueBtn().setDisable(false);
			});
			preGameScene.getConfirmBtn().setOnAction(event->{
				shipCoordArray =  preGameScene.getShipCoordArray();
				client.requestSendGameMatrix(gameMatrix, shipCoordArray);

				preGameScene.getWaveTransition().play();
				preGameScene.getConfirmBtn().setDisable(true);

				preGameScene.getWaveTransition().setOnFinished(r -> {

					primaryStage.setScene(gameScene.getScene());
					primaryStage.show(); // Ensure the stage is visible

					loginScene.leaveQueue();
					preGameScene.getConfirmBtn().setDisable(false);
					// Directly start the next scene's transition
					Platform.runLater( gameScene.getWaveTransition()::play);
				});



				hidePanel = new TranslateTransition(Duration.seconds(0.5),preGameScene.getPanelHbox());
				hidePanel.setToY(-1080); // Moves to the center of the screen vertically
				hidePanel.setCycleCount(1);
				hidePanel.setAutoReverse(false);
				hidePanel.play();
				// Add a listener to play when the transition finishes

				hidePanel.setOnFinished(e4 -> {
					System.out.println("done playing");
					Platform.runLater(() -> {


						gameScene.setYourStackPane(preGameScene.getYourGame());
						System.out.println("Panel HBox moved to GameScene on UI thread.");
						preGameScene.getYourGame().setVisible(true);
						preGameScene.getYourGame().setManaged(true);
						preGameScene.getYourGame().requestLayout();

					});

				});

				TextField chatField = gameScene.getChatField();

				chatField.setOnKeyPressed(e3->{
					if (e3.getCode().equals(KeyCode.ENTER) && !chatField.getText().isEmpty()){
						System.out.println(chatField.getText());
						client.requestToMessageOpponent(chatField.getText());

						chatField.clear();

					}

				});



			});
			GridPane emoteGrid = gameScene.getEmoteGrid();
			StackPane emoteStack = gameScene.getEmoteStack();
			gameScene.getEmoteBtns().forEach(button -> {
				button.setOnAction(e2->{
					emoteGrid.setMouseTransparent(true);
					emoteGrid.setOpacity(.5);
					String filename = (String)button.getUserData();
					playEmoteSound(filename);
					ImageView i = new ImageView(filename);
					emoteStack.getChildren().add(i);
					i.setTranslateY(-80);
					i.setTranslateX(+80);
					client.requestToEmote(filename);

					PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
					pause.setOnFinished(v -> {
						emoteStack.getChildren().remove(i);
						emoteGrid.setMouseTransparent(false);
						emoteGrid.setOpacity(1);
					});  // Hide the ImageView
					pause.play();  // Start the pause
				});
//			}

			});

			gameScene.getLeaveBtn().setOnAction(e3->{
				client.requestDcOpponent();
				loginScene.getWaitText().setTranslateY(1080);
				loginScene.cloudTransitionImageView().setTranslateY(1400);
				primaryStage.setScene(loginScene.getLoginScene());
			});
		});








		primaryStage.setScene(loginScene.getLoginScene());

//		primaryStage.setScene(endScene.getEndScene());
		// Show the stage
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				client.requestUpdateMoney(totalMoney);
				Platform.exit();
				System.exit(0);
			}
		});


	}

	public void setAiGame(Stage primaryStage) {
		aiGameScene.setEmotes(loginScene.getCurrInvEmotes());
		preGameScene.getConfirmBtn().setOnAction(e1->{



			System.out.println("vs ai");
			aiGameScene.setGameBoard(preGameScene.getGameMatrix());
			aiGameScene.setYourGameBoard(gameMatrix);
			preGameScene.getWaveTransition().play();
			preGameScene.getConfirmBtn().setDisable(true);

			preGameScene.getWaveTransition().setOnFinished(ev->{
				primaryStage.setScene(aiGameScene.getScene());
				aiGameScene.getWaveTransition().play();

			});

			hidePanel = new TranslateTransition(Duration.seconds(0.5),preGameScene.getPanelHbox());
			hidePanel.setToY(-1080); // Moves to the center of the screen vertically
			hidePanel.setCycleCount(1);
			hidePanel.setAutoReverse(false);

			aiGameScene.getWaveTransition().setOnFinished(E->{
				System.out.println("here");
				hidePanel.play();
			});

			hidePanel.setOnFinished(e4 -> {
				System.out.println("done playing");
				Platform.runLater(() -> {


					aiGameScene.setYourStackPane(preGameScene.getYourGame());
					System.out.println("Panel HBox moved to GameScene on UI thread.");
					preGameScene.getYourGame().setVisible(true);
					preGameScene.getYourGame().setManaged(true);
					preGameScene.getYourGame().requestLayout();

				});

			});
			shipCoordArray =  preGameScene.getShipCoordArray();
			aiGameScene.setYourShipCoordArray(shipCoordArray);
			aiGameScene.ai.setPlayerShipCoordArray(preGameScene.getShipCoordArray());

		});


		preGameScene.setGui(this);
		loginScene.pCloudTrans().play();
		loginScene.pCloudTrans().setOnFinished(ev->{

			Platform.runLater(()->{
				primaryStage.setScene(preGameScene.getScene());
				preGameScene.getCloudTransition().play();
			});
		});
		System.out.println("here2");

		aiGameScene.getLeaveBtn().setOnAction(e3->{
			loginScene.getWaitText().setTranslateY(1080);
			loginScene.getPlayAiVbox().setTranslateY(-1080);
			loginScene.cloudTransitionImageView().setTranslateY(1400);
			loginScene.getMainMenuVBox().setTranslateY(0);
			loginScene.getLeaveQPane().setTranslateY(-1080);
			primaryStage.setScene(loginScene.getLoginScene());
		});

		TextField chatField = aiGameScene.getChatField();

		chatField.setOnKeyPressed(e3->{
			if (e3.getCode().equals(KeyCode.ENTER) && !chatField.getText().isEmpty()){
				aiGameScene.updateMessageUI(username + ": " + chatField.getText());
				chatField.clear();

			}

		});

		GridPane emoteGrid = aiGameScene.getEmoteGrid();
		StackPane emoteStack = aiGameScene.getEmoteStack();
		aiGameScene.getEmoteBtns().forEach(button -> {
			button.setOnAction(e2->{
				emoteGrid.setMouseTransparent(true);
				emoteGrid.setOpacity(.5);
				String filename = (String)button.getUserData();
				playEmoteSound(filename);
				ImageView i = new ImageView(filename);
				emoteStack.getChildren().add(i);
				i.setTranslateY(-80);
				i.setTranslateX(+80);
//						client.requestToEmote(filename);

				PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
				pause.setOnFinished(v -> {
					emoteStack.getChildren().remove(i);
					emoteGrid.setMouseTransparent(false);
					emoteGrid.setOpacity(1);
				});  // Hide the ImageView
				pause.play();  // Start the pause
			});
//			}

		});
	}

}
