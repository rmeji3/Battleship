import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class AiGameScene {
    AI ai;
    // Callback interface declaration within GameScene
    public interface onMovesReceivedListener {
        void onReceived(ArrayList<String> movesList);
    }
    private GameScene.onMovesReceivedListener movesListener;
    public void setMovesReceivedListener(GameScene.onMovesReceivedListener listener){this.movesListener = listener;}


    public interface onWinReceivedListener {
        void onReceived(boolean didWin);
    }
    private GameScene.onWinReceivedListener winListener;
    public void setWinReceivedListener(GameScene.onWinReceivedListener listener){this.winListener = listener;}
    private static final int NUM_ROWS = 11;
    private static final int NUM_COLS = 11;
    private static final int CELL_SIZE = 77;

    private ImageView playerImgView;
    private int turn = 0;

    private Scene scene;

    private String username;

    private HashMap<String, Button> gridMap = new HashMap<>();

    private GameLogic gameLogic = new GameLogic(null, null);


    private StackPane root;

    private ImageView opponentImgView;

    private TranslateTransition endBoxTransition;

    private GridPane grid;

    private VBox rightVbox;
    private Text endBoxText;
    private VBox righterVbox;
    private GridPane emoteGrid;

    private ImageView backgroundView1;
    private ImageView backgroundView2;
    private TranslateTransition cloudTransition;

    private Client client;

    private Button leaveBtn;

    private TranslateTransition yourGameTranslation;
    private TranslateTransition theirGameTranslation;

    private DropShadow greyBoxDropShadow;

    private TranslateTransition waveTransition;

    private StackPane enemyStackPane;
    private StackPane yourStackPane = new StackPane();

    private StackPane getYourStackPaneWrapper = new StackPane();

    private HBox rightPanel;

    private TextField chatField = new TextField();
    private StackPane yourPanelBox = new StackPane();

    private HBox wrapperPanel;

    private HBox wrapperPanel2;

    private VBox chatVbox;

    private ArrayList<String> movesCoord = new ArrayList<>();

    private DropShadow redBoxDropShadow;

    private boolean currTurn;

    private VBox chatLogVbox;

    private HBox gridPanel;

    private  HBox spacer;

    private String[] botMessages = {"LOSER!", "YOU SUCK!", "GET GOOD", "I'M THE BEST", "LOSING TO ME?", "I'M UNSTOPPABLE", "LOSING TO ZEROES AND ONES LOL"};
    Random rand = new Random();


//    private Text waiting;


    private StackPane blueStack;


    private HashMap<String,Button> gridMapAni = new HashMap<>();
    private StackPane emoteStack;

    private boolean pressedFire = false;

    private StackPane emoteStackOp;

    private Button fireBtn;
    private boolean isShowing = false;
    private HBox blueHbox;

    private VBox endBox;
    private ArrayList<Button> emoteBtnList;

    ParallelTransition p1;
    private Text shipSunkTextYours;
    private Text yourName;

    private Text opponentName;

    private Button lastPressedCell = null;

    private GridPane yourGridAnimationCells;
    private String difficulty;

    public AiGameScene(Stage primaryStage, String difficulty){

        ai = new AI(difficulty);
        this.difficulty = difficulty;
        spacer = new HBox(40);
        spacer.setAlignment(Pos.CENTER);
        spacer.getStyleClass().add("panel-hbox3");
        spacer.setMouseTransparent(true);
        createBackground();
        createGrid();
        createQuitBtn();
        createRightVbox();
        createRighterVbox();
        assemblePanel();
        createBeginningTransition();
        createEndBox();
        this.difficulty = difficulty;
        gameLogic.setOpponentBoard(ai.getBoard());

        gameLogic.setOpponentShipCoordArray(ai.getAiShipCoordArray());
        opponentName.setText(ai.getName());
    }

    public void setChar(String filename){
        playerImgView.setImage(new Image(filename));
    }

    public void setAiChar(String filename){
        if (difficulty.equals("impossible")){
            opponentImgView.setImage(new Image("/captainImg/mccarty.png"));
        }
        else {
            opponentImgView.setImage(new Image(filename));
        }
    }
    private void createGrid() {
        enemyStackPane = new StackPane();
        grid = new GridPane();
        enemyStackPane.getChildren().add(grid);
        Text shipSunkText = new Text("SHIP SUNK!!!");
        shipSunkText.setVisible(false);
        shipSunkText.getStyleClass().add("waiting-text");
        enemyStackPane.getChildren().add(shipSunkText);
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                Button cellBtn = new Button();
                Coord coord; // Default coord adjustment

                if (row == 0 && col == 0) {
                    coord = new Coord(-1, -1);
                    cellBtn.setText(""); // Top-left corner of the grid
                    cellBtn.getStyleClass().add("cell-btn-nums");
                    cellBtn.setDisable(true);
                } else if (row == 0) {
                    coord = new Coord(-1, col - 1);
                    cellBtn.setText(Integer.toString(col)); // Column numbers
                    cellBtn.getStyleClass().add("cell-btn-nums");
                    cellBtn.setDisable(true);
                } else if (col == 0) {
                    coord = new Coord(row - 1, -1);
                    cellBtn.setText(String.valueOf((char) ('A' + row - 1))); // Row letters
                    cellBtn.getStyleClass().add("cell-btn-nums");
                    cellBtn.setDisable(true);
                } else {
                    coord = new Coord(row - 1, col - 1);
                    // Special cells with custom styles
                    String styleClass = "cell-btn-enemy"; // Default class
                    cellBtn.getStyleClass().add(styleClass);
                    gridMap.put(coord.toString(), cellBtn);
                }

                cellBtn.setUserData(coord);
                cellBtn.setPrefSize(CELL_SIZE, CELL_SIZE);
                cellBtn.setMinSize(CELL_SIZE, CELL_SIZE);
                cellBtn.setMaxSize(CELL_SIZE, CELL_SIZE);
                grid.add(cellBtn, col, row);


                // This hides the ImageView
                cellBtn.setOnAction(e -> {
//                    lastPressedCell = cellBtn;
                    fireBtn.setDisable(false);
                    cellBtn.setStyle("-fx-background-color: #2872A4;");

                    if(lastPressedCell != null){
                        if(!pressedFire){
                            lastPressedCell.setStyle("-fx-background-color: #3AB0FF");
                        }
                    }

                    handleFireAction(cellBtn,shipSunkText, true);



                    pressedFire = false;

                    lastPressedCell = cellBtn;


                });

            }
        }
    }
    public void playEmoteSound(String file){
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

    public void playSound(String file){
        try {
            InputStream audioSrc = getClass().getResourceAsStream(file);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if(file.equals("/Sounds/swoosh.wav"))
                gainControl.setValue(-20.0f);
            else
                gainControl.setValue(-30.0f);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void handleFireLogic(Button cellBtn, Text shipSunkText, Boolean yourTurn){
        boolean canContinue = false;
        if(yourTurn)
        {
            Coord c = (Coord) cellBtn.getUserData();
            movesCoord.add(c.toString());
            pressedFire = true;

        }

        PauseTransition del = new PauseTransition(Duration.seconds(.9)); // Adjust the time based on GIF length
        PauseTransition spl = new PauseTransition(Duration.seconds(1));
        if(yourTurn){
            canContinue = gameLogic.makePlayerMove((Coord)cellBtn.getUserData());
        }else{
            canContinue = gameLogic.makeOpponentMove((Coord)cellBtn.getUserData());
        }
        if(canContinue){
            ImageView explosionView = new ImageView(new Image("/explosion.gif"));
            cellBtn.setGraphic(explosionView);
            cellBtn.setDisable(true);
            explosionView.setVisible(true);
            if(!yourTurn)
            {
                Platform.runLater(()->{
                    int index = rand.nextInt(botMessages.length);

                    if(rand.nextBoolean()) {


                        updateMessageUI(ai.getName() + ": " + botMessages[index]);
                        // 3 4 7 8
                        int randEmote = rand.nextInt(4);
                        switch (randEmote) {
                            case 0:
                                enemyEmote("/emotes/Emote3.png");
                                playEmoteSound("/emotes/Emote3.wav");
                                break;
                            case 1:
                                enemyEmote("/emotes/Emote4.png");
                                enemyEmote("/emotes/Emote4.wav");
                                break;
                            case 2:
                                enemyEmote("/emotes/Emote7.png");
                                playEmoteSound("/emotes/Emote7.wav");
                                break;
                            case 3:
                                enemyEmote("/emotes/Emote8.png");
                                playEmoteSound("/emotes/Emote8.wav");
                                break;
                        }

                    }
                });




                PauseTransition p = new PauseTransition(Duration.seconds(2));
                p.play();
                p.setOnFinished(e->{ del.play();
                });
            }else
                del.play();

            playSound("/Sounds/explosion.wav");
//            System.out.println(cellBtn.getUserData());
            del.setOnFinished(event ->{
                explosionView.setVisible(false);
//                cellBtn.setDisable(false);
//                fireBtn.setDisable(false);
            } );
            cellBtn.setDisable(true);
            cellBtn.setStyle(
                    "-fx-background-color: rgba(255, 0, 0, 0.5);"
            );
        }else{
            ImageView splashView = new ImageView(new Image("/splash.gif"));
            cellBtn.setDisable(true);
            cellBtn.setGraphic(splashView);
            splashView.setVisible(true);
            if(!yourTurn) {
                PauseTransition p = new PauseTransition(Duration.seconds(2));
                p.play();
                p.setOnFinished(e->{
                    spl.play();
                });


            }else {
                spl.play();
                gridPanel.setMouseTransparent(true);
            }

            fireBtn.setDisable(true);
            playSound("/Sounds/splash.wav");
//            System.out.println(cellBtn.getUserData());
            spl.setOnFinished(event ->{
                cellBtn.setGraphic(new ImageView(new Image("/cross.png")));
                cellBtn.setStyle("-fx-background-color: #3AB0FF");
                PauseTransition someTransitionIDKwat2Name = new PauseTransition(Duration.seconds(1));
                someTransitionIDKwat2Name.play();
                if(yourTurn){

                    someTransitionIDKwat2Name.setOnFinished(ev->{
                        triggerMiss(false);
                    });
                }
                System.out.println("miss");

            } );




        }


        if(yourTurn && gameLogic.checkOpponentShipSunk((Coord)cellBtn.getUserData())){


            PauseTransition shipSunkTransition = new PauseTransition(Duration.seconds(2));
            playSound("/Sounds/shipSunk.wav");
            shipSunkText.setVisible(true);
            enemyStackPane.setMouseTransparent(true);
            shipSunkTransition.play();
//            System.out.println("hereeeeee");
            shipSunkTransition.setOnFinished(ev->{
                enemyStackPane.setMouseTransparent(false);
                flameSunkShip(gameLogic.getLastShipSunk(), gridMap, yourTurn);
                shipSunkText.setVisible(false);
            });
            int randEmote = rand.nextInt(4);
            switch (randEmote) {
                case 0:
                    enemyEmote("/emotes/Emote1.png");
                    playEmoteSound("/emotes/Emote1.wav");
                    break;
                case 1:
                    enemyEmote("/emotes/Emote6.png");
                    enemyEmote("/emotes/Emote6.wav");
                    break;
                case 2:
                    enemyEmote("/emotes/Emote5.png");
                    playEmoteSound("/emotes/Emote5.wav");
                    break;
                case 3:
                    enemyEmote("/emotes/Emote9.png");
                    playEmoteSound("/emotes/Emote9.wav");
                    break;
            }
        }else if(!yourTurn && gameLogic.checkPlayerShipSunkAnimation((Coord) cellBtn.getUserData())) {

            PauseTransition shipSunkTransition = new PauseTransition(Duration.seconds(2));
            shipSunkText.setVisible(true);
            playSound("/Sounds/shipSunk.wav");
            enemyStackPane.setMouseTransparent(true);
            shipSunkTransition.play();

            System.out.println("hereeeeee2");
            shipSunkTransition.setOnFinished(ev->{
                enemyStackPane.setMouseTransparent(false);
                shipSunkText.setVisible(false);
            });
        }
    }

    public void handleFireAction(Button cellBtn, Text shipSunkText, Boolean yourTurn){
        if(yourTurn){
            fireBtn.setOnAction(e->{
                handleFireLogic(cellBtn,shipSunkText,yourTurn);
                fireBtn.setDisable(true);
//                System.out.println("is here");
                if(!gameLogic.checkGameOver().equals("none"))
                    endGame(gameLogic.checkGameOver());
            });
        }
        else{

            handleFireLogic(cellBtn,shipSunkText,yourTurn);
            if(!gameLogic.checkGameOver().equals("none"))
                endGame(gameLogic.checkGameOver());

        }
    }

    private void endGame(String whoWon){

        boolean didWin = false;
//        System.out.println("who won: " + whoWon);
        if(whoWon.equals("player"))
        {
            playSound("/Sounds/win.wav");
            didWin = true;
            String endString = "AMAZING DEFEAT,\n WELL PLAYED CAPTAIN " + username + "!";
            endBoxText.setText(endString);
        }else{
            playSound("/Sounds/lose.wav");
            String endString = "YOU FOUGHT HARD CAPTAIN " + username + ",\n WE'LL GET EM NEXT TIME!";
            endBoxText.setText(endString);
        }

        gridPanel.setMouseTransparent(true);
        endBoxTransition.play();
//        winListener.onReceived(didWin);


    }

    private void createEndBox(){
        endBox = new VBox();
        endBox.getStyleClass().add("end-box");
        endBox.setAlignment(Pos.CENTER);
        endBox.setEffect(greyBoxDropShadow);
        endBox.setTranslateY(-1080);
        endBox.setTranslateX(-300);
        endBoxText = new Text("PLACE HOLDER TEXT");
        endBoxText.setTextAlignment(TextAlignment.CENTER);
        endBox.getChildren().add(endBoxText);
        endBoxText.getStyleClass().add("end-text");
        endBoxTransition = new TranslateTransition(Duration.seconds(.5), endBox);
        endBoxTransition.setToY(0);
        endBoxTransition.setOnFinished(e->{
            gridPanel.setMouseTransparent(true);
            fireBtn.setDisable(true);
            triggerMiss(true);
        });
        leaveBtn = new Button();
        leaveBtn.getStyleClass().add("leave-btn");
        leaveBtn.setText("Leave game");
        leaveBtn.setEffect(redBoxDropShadow);
        endBox.getChildren().add(leaveBtn);

    }
    public void setEmotes(ArrayList<String> emotes){
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                Image emoteImg = new Image(emotes.get(index));
                ImageView emoteImgView = new ImageView(emoteImg);
                emoteBtnList.get(index).setGraphic(emoteImgView);
                emoteBtnList.get(index).setUserData(emotes.get(index));
//                System.out.println(emoteNum);

                emoteImgView.setPreserveRatio(false);
                emoteImgView.setFitHeight(85);
                emoteImgView.setFitWidth(82);

                index++;
            }
        }
    }
    private void triggerMiss(boolean isGameOver){
//        System.out.println("triggering miss");
        enemyStackPane.setMouseTransparent(true);
        //callback here to send to guiclient
        if(movesListener != null)
            movesListener.onReceived(movesCoord);
//        client.requestChangeTurn(movesCoord);
        if(!isGameOver){
            if(turn == 1) {
                playSound("/Sounds/swoosh.wav");
                transitionTurnToMove();
//                System.out.println("playing swoosh");

            }else
                transitionTurnToMove2();
        }
//        ArrayList<String> arr = new ArrayList<>();
//        Coord c;
//        do{
//            c = ai.pickRandomMove();
//            arr.add(c.toString());
//        }
//        while(gameLogic.makeOpponentMove(c) && !isGameOver);




        if(!isGameOver){
            ArrayList<String> arr = ai.pickRandomMove();
//            if (arr.size() > 1){
//                int index = rand.nextInt(botMessages.length);
//                updateMessageUI(ai.getName() + botMessages[index]);
//
//            }
            gridPanel.setMouseTransparent(false);
            playAnimations(arr);
        }
    }

    public void playAnimations(ArrayList<String> movesList) {
        Timeline timeline = new Timeline();
        double timeOffset = 0;

        for (String coord : movesList) {
            Button b = gridMapAni.get(coord);

            if (b == null) {
//                System.out.println("Button not found for coord: " + coord);
                continue;
            }

            // Adding a keyframe to handle the action
            KeyFrame actionFrame = new KeyFrame(Duration.seconds(timeOffset), e -> handleFireAction(b, shipSunkTextYours, false));
            timeline.getKeyFrames().add(actionFrame);

            // Increment time offset for the pause
            timeOffset += .5; // Adjust according to how long handleFireAction's animations take

            // Adding a keyframe for the pause
            KeyFrame pauseFrame = new KeyFrame(Duration.seconds(timeOffset));
            timeline.getKeyFrames().add(pauseFrame);

            // Increment time offset for the next action
            timeOffset += 1;
        }

        // Additional keyframe to re-enable UI elements after all animations
        KeyFrame finalFrame = new KeyFrame(Duration.seconds(timeOffset), e -> {
            if (turn == 1) {
                playSound("/Sounds/swoosh.wav");
                transitionTurnToMove();
            }else
                transitionTurnToMove2();

//            p1.play();
//            fireBtn.setDisable(false);
            enemyStackPane.setMouseTransparent(false);
//            System.out.println("All animations and pauses completed");
        });

        timeline.getKeyFrames().add(finalFrame);
        timeline.play();
    }

    private void transitionTurnToMove(){
        movesCoord.clear();
//        System.out.println("turn: " + turn);
        if(currTurn){

            yourGameTranslation.setToY(0);
            theirGameTranslation.setToY(1080);
            p1.play();
        }else
        {
            yourGameTranslation.setToY(-1080);
            theirGameTranslation.setToY(0);
            p1.play();

        }

        currTurn = !currTurn;

    }

    private void transitionTurnToMove2(){
        movesCoord.clear();
//        System.out.println("turn: " + turn);
        if(!currTurn){

            yourGameTranslation.setToY(0);
            theirGameTranslation.setToY(1080);
            p1.play();
        }else
        {
            yourGameTranslation.setToY(-1080);
            theirGameTranslation.setToY(0);
            p1.play();

        }

        currTurn = !currTurn;

    }

    private void flameSunkShip(ArrayList<String> coords, HashMap<String, Button> gridMap, boolean yourTurn) {
        for(String coord : coords)
        {
//            System.out.println("coord: " + coord);
            Button b1;

            if(yourTurn)
                b1 = gridMap.get(coord);
            else
                b1 = gridMapAni.get(coord);

//            b1.setStyle(
//                "-fx-background-color: rgba(255, 0, 0, 0.5);"
//            );
            b1.setGraphic(new ImageView(new Image("/fire.gif")));
            b1.setDisable(true);

        }

    }


    private void createAnimationGrid(){
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                Button cellBtn = new Button();

                // Create a new Coord instance for each cell
                Coord coord;
                if (row == 0 || col == 0) {
                    coord = new Coord(-1, -1); // Using -1,-1 for all first row and column cells
                    cellBtn.setDisable(true);
                } else {
                    coord = new Coord(row - 1, col - 1); // Appropriate coordinate for other cells
                    cellBtn.setDisable(true);
                }

                cellBtn.setUserData(coord);
                cellBtn.setStyle("-fx-background-color: transparent;");
//                cellBtn.setText(coord.toString());
                cellBtn.setPrefSize(CELL_SIZE, CELL_SIZE);
                cellBtn.setMinSize(CELL_SIZE, CELL_SIZE);
                cellBtn.setMaxSize(CELL_SIZE, CELL_SIZE);

                // Save the button to the map and the grid
                gridMapAni.put(coord.toString(), cellBtn);
                yourGridAnimationCells.add(cellBtn, col, row);
                yourGridAnimationCells.getStyleClass().add("ani-grid");
            }
        }
    }

    public void setGameAfterTurn(int turn){
        this.turn = turn;
        currTurn = turn == 1;
        TranslateTransition bgTransition1;
        TranslateTransition bgTransition2;
//        yourPanelBox = new HBox();
        yourPanelBox.getStyleClass().add("your-panel-hbox");
        yourPanelBox.setEffect(greyBoxDropShadow);
//        yourPanelBox.getChildren().add(yourGameBoats);
        root.getChildren().add(gridPanel);
        root.getChildren().add(yourPanelBox);

        wrapperPanel.setTranslateX(500);
        gridPanel.setTranslateX(-350);
        yourPanelBox.setTranslateX(-350);

//        System.out.println("setting ani grid");
        yourGridAnimationCells = new GridPane();
        createAnimationGrid();
        shipSunkTextYours = new Text("SHIP SUNK!!!");
        shipSunkTextYours.setVisible(false);
        shipSunkTextYours.getStyleClass().add("waiting-text");

        yourPanelBox.setTranslateY(-1080);
//            yourPanelBox.getChildren().add(t);


//            waiting = new Text("Waiting for opponent...");

//            enemyStackPane.getChildren().add(waiting);
//            waiting.getStyleClass().add("waiting-text");
        yourGameTranslation = new TranslateTransition(Duration.seconds(.5), yourPanelBox);
        yourGameTranslation.setToY(0);
        yourGameTranslation.setCycleCount(1);
        yourGameTranslation.setAutoReverse(false);

        theirGameTranslation = new TranslateTransition(Duration.seconds(.5), gridPanel);
        theirGameTranslation.setToY(1080);
        theirGameTranslation.setCycleCount(1);
        theirGameTranslation.setAutoReverse(false);

        p1 = new ParallelTransition(yourGameTranslation, theirGameTranslation);
        root.getChildren().add(endBox);
    }

    public void revertAfterReady(){
        if(turn == 1){

            gridPanel.setMouseTransparent(false);

            gridPanel.setOpacity(1);
        }else{

            yourPanelBox.setMouseTransparent(false);

            yourPanelBox.setOpacity(1);
        }
    }

    private void assemblePanel(){
        wrapperPanel = new HBox(20);
        wrapperPanel.setAlignment(Pos.CENTER);

        wrapperPanel2 = new HBox(20);
        wrapperPanel2.setAlignment(Pos.CENTER);
        wrapperPanel2.setTranslateX(-380);
        wrapperPanel2.getStyleClass().add("wrapperPanel2");




        gridPanel = new HBox();
        gridPanel.setAlignment(Pos.CENTER);
        gridPanel.getChildren().add(enemyStackPane);
        gridPanel.setEffect(greyBoxDropShadow);
        rightPanel = new HBox(40);
        rightPanel.setAlignment(Pos.CENTER);
        gridPanel.getStyleClass().add("panel-hbox2");
        rightPanel.getStyleClass().add("panel-hbox");
        rightPanel.setEffect(greyBoxDropShadow);
        Pane spacer = new Pane();
        spacer.setStyle(
                "-fx-pref-width: 900px;" +
                        "-fx-max-width: 900px;" +
                        "-fx-min-width: 900px;" +
                        "-fx-pref-height: 902px;" +
                        "-fx-max-height: 902px;" +
                        "-fx-min-height: 902px;"
        );
        wrapperPanel.getChildren().addAll(rightPanel);
        root.getChildren().add(wrapperPanel);

        rightPanel.getChildren().addAll(rightVbox, righterVbox);
    }
    private void createEmoteBox(){

        emoteGrid.getStyleClass().add("emote-grid");
        int emoteNum = 1;
        emoteBtnList = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button emoteBtn = new Button();
                emoteBtn.getStyleClass().add("emote-btn");
                emoteBtn.setUserData("/emotes/Emote" + emoteNum + ".png");
//                System.out.println(emoteNum);
                Image emoteImg = new Image("/emotes/Emote" + ((row*3) + col + 1) + ".png");
                ImageView emoteImgView = new ImageView(emoteImg);
                emoteImgView.setPreserveRatio(false);
                emoteImgView.setFitHeight(85);
                emoteImgView.setFitWidth(82);
                emoteBtn.setGraphic(emoteImgView);
                emoteBtn.getStyleClass().add("emote-btn");
                emoteGrid.add(emoteBtn, col, row);

                emoteBtnList.add(emoteBtn);

                emoteNum++;
            }
        }
    }
    private void createRightVbox(){
        rightVbox = new VBox(17);
        rightVbox.setAlignment(Pos.CENTER);


        greyBoxDropShadow = new DropShadow();
        greyBoxDropShadow.setRadius(10.0);
        greyBoxDropShadow.setOffsetX(5.0);
        greyBoxDropShadow.setOffsetY(8.0);
        greyBoxDropShadow.setRadius(0);
        greyBoxDropShadow.setColor(Color.web("B9B9B9"));

        ScrollPane chatBox = new ScrollPane();
        chatVbox = new VBox();
        chatLogVbox = new VBox(5);
        chatBox.getStyleClass().add("chat-box");
        chatVbox.getStyleClass().add("chat-box-vbox");

        chatField = new TextField();
        chatBox.setContent(chatLogVbox);
        chatField.getStyleClass().add("chat-field");
        chatField.setPromptText("Message");
        chatField.setMinWidth(200);
        redBoxDropShadow = new DropShadow();
        redBoxDropShadow.setRadius(10.0);
        redBoxDropShadow.setOffsetX(5.0);
        redBoxDropShadow.setOffsetY(8.0);
        redBoxDropShadow.setRadius(0);
        redBoxDropShadow.setColor(Color.web("CA5C5C"));

        fireBtn = new Button("FIRE");
        fireBtn.setDisable(true);
        fireBtn.getStyleClass().add("fire-btn");
        fireBtn.setEffect(redBoxDropShadow);
        chatBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        emoteGrid = new GridPane();
        createEmoteBox();
        chatLogVbox.heightProperty().addListener((observable) -> {
            chatBox.setVvalue(1.0); // This moves the scroll to the bottom
        });
        chatVbox.setAlignment(Pos.TOP_CENTER);
        chatVbox.getChildren().addAll(chatBox, chatField);
        rightVbox.getChildren().addAll(chatVbox, emoteGrid, fireBtn);

    }

    public void updateMessageUI(String message){
        HBox chatHbox = new HBox();
        Text chatText = new Text(message);
        chatText.getStyleClass().add("chat-text");
        chatHbox.getChildren().add(chatText);
        chatLogVbox.getChildren().add(chatHbox);
//        chatField.clear();
    }
    private void createRighterVbox(){
        righterVbox = new VBox(20);

        yourName = new Text("You");
        yourName.getStyleClass().add("name-text");
        opponentName = new Text("Opponent");
        opponentName.getStyleClass().add("name-text");
        righterVbox.setAlignment(Pos.CENTER);
        righterVbox.getStyleClass().add("righter-vbox");
        blueHbox = new HBox();

        emoteStack = new StackPane();
        VBox b1Wrapper = new VBox();
        b1Wrapper.getStyleClass().add("player-img");
        b1Wrapper.setAlignment(Pos.CENTER);

        Image playerImg = new Image("/captainImg/CaptainBlue2.png");

        playerImgView = new ImageView(playerImg);
        emoteStack.getChildren().add(playerImgView);
        b1Wrapper.getChildren().addAll(yourName, emoteStack);
        playerImgView.setPreserveRatio(false);
        playerImgView.setFitHeight(300);
        playerImgView.setFitWidth(300);

        emoteStackOp = new StackPane();
        VBox b2Wrapper = new VBox();
        b2Wrapper.getStyleClass().add("player-img");
        b2Wrapper.setAlignment(Pos.CENTER);

        Image opponentImg = new Image("/captainImg/CaptainRed2.png");

        opponentImgView = new ImageView(opponentImg);
        emoteStackOp.getChildren().add(opponentImgView);
        b2Wrapper.getChildren().addAll(opponentName, emoteStackOp);
        opponentImgView.setPreserveRatio(false);
        opponentImgView.setFitHeight(300);
        opponentImgView.setFitWidth(300);


//        HBox redHbox = new HBox();
//        redHbox.getChildren().add(b2);

        righterVbox.getChildren().addAll(b1Wrapper, b2Wrapper);
    }
    private void createQuitBtn(){
        Image quitImg = new Image("/btnImages/Quit.png");
        ImageView quitImgView = new ImageView(quitImg);
        Button quitBtn = new Button();
        quitBtn.getStyleClass().add("quit-btn");
        quitBtn.setGraphic(quitImgView);
    }




    private void createBackground(){
        root = new StackPane();
        scene = new Scene(root, 1900, 1000);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/GameScene.css")).toExternalForm());
        Image background = new Image("/transitionImages/background.jpg");
        backgroundView1 = new ImageView(background);
        backgroundView2 = new ImageView(background);
        root.getChildren().addAll(backgroundView1,backgroundView2);
        backgroundView2.setTranslateY(-1080);
    }

    private void createBeginningTransition(){
        Image waveImg = new Image("/transitionImages/Wave.png");
        ImageView waveImgView = new ImageView(waveImg);
        root.getChildren().add(waveImgView);
        waveImgView.setTranslateY(0);
        waveTransition = new TranslateTransition(Duration.seconds(2), waveImgView);
        waveTransition.setToY(waveImg.getHeight()); // Moves to the center of the screen vertically
        waveTransition.setCycleCount(1);
        waveTransition.setAutoReverse(false);
    }

    public void enemyEmote(String emoteName){
        ImageView i = new ImageView(emoteName);
        emoteStackOp.getChildren().add(i);
        i.setTranslateY(-80);
        i.setTranslateX(+80);

        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(v -> {
            emoteStackOp.getChildren().remove(i);
        });  // Hide the ImageView
        pause.play();  // Start the pause
    }
    public Scene getScene(){return scene;}

    public TranslateTransition getCloudTransition() {
        return cloudTransition;
    }

    public TranslateTransition getWaveTransition(){return waveTransition;}

    public void setYourStackPane(StackPane yourStackPane){
        this.yourStackPane = yourStackPane;
        yourPanelBox.getChildren().addAll(yourStackPane,yourGridAnimationCells, shipSunkTextYours);

    }
    public void setTurn(int turn){this.turn = turn;}

    public TextField getChatField(){return chatField;}

    public ArrayList<Button> getEmoteBtns(){
        return emoteBtnList;
    }

    public GridPane getEmoteGrid(){return emoteGrid;}

    public StackPane getEmoteStack(){return emoteStack;}

    public void setOpName(String opName){
        opponentName.setText(opName);
    }

    public void setYourName(String yourName){
        this.yourName.setText(yourName);
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
    public void setClient(Client c){this.client = c;}

    public Button getLeaveBtn(){
        return this.leaveBtn;
    }

    public void setUsername(String username){this.username = username;}

    public void setGameBoard(boolean[][]gameMatrix){this.ai.setBoard(gameMatrix);}

    public void setYourGameBoard(boolean[][]gameMatrix){this.gameLogic.setPlayerBoard(gameMatrix);}

    public void setYourShipCoordArray(ArrayList<ArrayList<String>> arr){gameLogic.setYourShipCoordArray(arr);}
}


