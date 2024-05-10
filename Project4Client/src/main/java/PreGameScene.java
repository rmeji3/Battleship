import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class PreGameScene {

    Map<Coord, Button> buttonMap = new HashMap<>();
    private static final int NUM_ROWS = 10;
    private static final int NUM_COLS = 10;
    private static final int CELL_SIZE = 77;

    private Scene scene;

    private StackPane root;

    private GridPane grid;


    private VBox rightVbox;

    private Button confirmBtn;

    private StackPane characterBox;
    private ImageView characterView;
    private TranslateTransition cloudTransition;
    private TranslateTransition waveTransition;

    private DropShadow greyBoxDropShadow;

    private AnchorPane wrapper;
    private AnchorPane wrapper2;
    private ImageView shipImageView;

    private Image currShipImage;

    private boolean isHorizontal = true;


    private boolean[][] gameMatrix;


    private int currBoatSize = 2;

    private final ArrayList<Image> boatList = new ArrayList<>();

    private int currIndex = 0;

    private int boatsLeft = 5;


    private Button rotateBtn;

    private double offsetX = 0;
    private double offsetY = 0;
    
    private StackPane backStack;

    private Coord lastHoveredCoord = null;

    private Button boatsLeftBtn;

    public HBox panelHbox;

    private GuiClient guiClient;

    private AiGameScene aiGameScene;

    private ArrayList<ArrayList<String>> shipCoordArray = new ArrayList<>();



    public PreGameScene(Stage primaryStage, Scene nextScene, boolean[][] gameMatrix, AiGameScene aiGameScene, GuiClient guiClient){
        this.aiGameScene = aiGameScene;
        this.guiClient = guiClient;
        confirmBtn = new Button();
        confirmBtn.setDisable(true);
        fillBoatList();
        backStack = new StackPane();
        this.gameMatrix = gameMatrix;
        grid = new GridPane();
        wrapper = new AnchorPane();
//        wrapper.setMouseTransparent(true);
        wrapper.setPrefSize(847,847);
        wrapper.setMaxSize(847,847);
        wrapper.setMinSize(847,847);
        wrapper2 = new AnchorPane();
        wrapper2.setMouseTransparent(true);
        wrapper2.setPrefSize(847,847);
        wrapper2.setMaxSize(847,847);
        wrapper2.setMinSize(847,847);

        backStack.getChildren().addAll(wrapper, wrapper2);
        currShipImage = changeCurrBoat();
        shipImageView = new ImageView(currShipImage);
        shipImageView.setOpacity(.5);
        shipImageView.setMouseTransparent(true);
        shipImageView.setVisible(false); // Initially hide the ship image
        wrapper2.getChildren().add(shipImageView);
        wrapper.getChildren().add(grid);
        createBackground();
        createGrid();
        createQuitBtn();
        createRightVbox(aiGameScene);
        assemblePanel();
        createBeginningTransition();
        createWaveTransition(primaryStage, nextScene);




    }


    public void setChar(String filename){
        characterView.setImage(new Image(filename));
    }
    public void setGui(GuiClient guiClient){
        this.guiClient = guiClient;
    }

    private Image changeCurrBoat(){
        Image img = boatList.get(currIndex);
        changeOffset(currIndex);
//        System.out.println("index here: " + currIndex);
        switch (currIndex){
            case 0:
                currBoatSize = 2;
                break;
            case 2:
            case 1:

                currBoatSize = 3;
                break;
            case 3:
                currBoatSize = 4;
                break;
            default:
                currBoatSize = 5;
                break;
        }

//        System.out.println("last boat size: " + currBoatSize);
        currIndex++;
        return img;
    }

    private void changeOffset(int currIndex){
        offsetY = 0;
        offsetX = 0;
//        System.out.println("index: " + currIndex);
        switch (currIndex){
            case 0:
                if(isHorizontal)
                {

                    offsetX += CELL_SIZE;
                    offsetY += CELL_SIZE;

                    offsetX += (double) CELL_SIZE / 6;
                    offsetY += (double) CELL_SIZE / 3;
                }
                else
                {
                    offsetY = CELL_SIZE + (double) CELL_SIZE /1.2;
                    offsetX = (double) CELL_SIZE/1.5;
                }

                break;
            case 1:
            case 2:
                if(isHorizontal)
                {

                    offsetX += CELL_SIZE;
                    offsetY += CELL_SIZE;

                    offsetX += ((double) CELL_SIZE / 4) - 10;
                    offsetY += ((double) CELL_SIZE / 5) + 5;
                }
                else
                {
                    offsetY = CELL_SIZE*2+ (double) CELL_SIZE /5;
                    offsetX =  16;
                }

                break;
            case 3:
                if(isHorizontal)
                {
                    offsetX += CELL_SIZE + 10;
                    offsetY += CELL_SIZE+20;

                }
                else
                {
                    offsetY = CELL_SIZE*2+ (double) CELL_SIZE /2 + 20;
                    offsetX = -29;
                }
                break;
            default:
                if(isHorizontal)
                {
                    offsetX += CELL_SIZE+3;
                    offsetY += CELL_SIZE+5;

                }
                else
                {
                    offsetY = (CELL_SIZE*2) + ((double) CELL_SIZE /2) + 45;
                    offsetX = -70;
                }
                break;
        }
    }

    private void fillBoatList(){

        Image boat1 = new Image("/ships/2ship.png");

        Image boat2 = new Image("/ships/3ship.png");

        Image boat3 = new Image("/ships/3ship2.png");

        Image boat4 = new Image("/ships/4ship.png");

        Image boat5 = new Image("/ships/5ship.png");

        boatList.add(boat1);
        boatList.add(boat2);
        boatList.add(boat3);
        boatList.add(boat4);
        boatList.add(boat5);


    }


    private void createWaveTransition(Stage primaryStage, Scene nextScene){
        Image waveImg = new Image("/transitionImages/Wave.png");
        ImageView waveImgView = new ImageView(waveImg);
        root.getChildren().add(waveImgView);
        waveImgView.setTranslateY(-waveImg.getHeight());
        waveTransition = new TranslateTransition(Duration.seconds(2), waveImgView);
        waveTransition.setToY(0); // Moves to the center of the screen vertically
        waveTransition.setCycleCount(1);
        waveTransition.setAutoReverse(false);
        waveTransition.setOnFinished(e->{
            primaryStage.setScene(nextScene);
        });
    }

    private void createGrid() {
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                Button cellBtn = new Button();
                Coord coord; // Default coord adjustment

                if (row == 0 && col == 0) {
                    coord = new Coord(-1, -1);
                    cellBtn.setText(""); // Top-left corner of the grid
                    cellBtn.getStyleClass().add("cell-btn-nums");
                } else if (row == 0) {
                    coord = new Coord(-1, col - 1);
                    cellBtn.setText(Integer.toString(col)); // Column numbers
                    cellBtn.getStyleClass().add("cell-btn-nums");
                } else if (col == 0) {
                    coord = new Coord(row - 1, -1);
                    cellBtn.setText(String.valueOf((char) ('A' + row - 1))); // Row letters
                    cellBtn.getStyleClass().add("cell-btn-nums");
                } else {
                    coord = new Coord(row - 1, col - 1);
                    // Special cells with custom styles
                    String styleClass = "cell-btn"; // Default class
                    cellBtn.getStyleClass().add(styleClass);
                }

                cellBtn.setUserData(coord);
                cellBtn.setMinSize(CELL_SIZE, CELL_SIZE);
                cellBtn.setMaxSize(CELL_SIZE, CELL_SIZE);
                grid.add(cellBtn, col, row);
                buttonMap.put(coord, cellBtn); // Map the button to its coordinates

                // Event handling for hover effects
                cellBtn.setOnMouseEntered(e -> {

                    if(currIndex <= boatList.size())
                    {
                        lastHoveredCoord = coord;
                        highlightButtons(coord, true, currBoatSize);
                        showShip(coord, cellBtn);
                    }

                });
                cellBtn.setOnMouseExited(e ->{
                    lastHoveredCoord = coord;
                    highlightButtons(coord, false, currBoatSize);
                    hideShip();
                });
            }
        }
    }

    private void showShip(Coord coord, Button cellBtn) {
        boolean outOfBoundsX = (coord.col + currBoatSize > NUM_COLS);
        boolean outOfBoundsY = (coord.row + currBoatSize > NUM_ROWS);
        if(currIndex <= boatList.size()){
            if(isHorizontal)
            {
                if (coord.row >= 0 && coord.col >= 0 && !outOfBoundsX) {
                    double x = coord.col * CELL_SIZE;
                    double y = coord.row * CELL_SIZE;
                    x += offsetX;
                    y += offsetY;


                    AnchorPane.setLeftAnchor(shipImageView, x);
                    AnchorPane.setTopAnchor(shipImageView, y);
                    shipImageView.setVisible(true);
                    double finalX = x;
                    double finalY = y;
                    cellBtn.setOnAction(e->{
                        if(checkGameBoard(coord, currBoatSize, isHorizontal) && currIndex <= boatList.size())
                        {
                            placeOnGameBoard(coord,isHorizontal);
                            placeShipOnAnchorPane(finalX, finalY);
                            boatsLeftBtn.setText(Integer.toString(--boatsLeft));

                        }
//                    printMatrix();
                    });
                }
            }else{

                if (coord.row >= 0 && coord.col >= 0 && !outOfBoundsY) {
                    double x = coord.col * CELL_SIZE;
                    double y = coord.row * CELL_SIZE;

                    x += offsetX;
                    y += offsetY;


                    AnchorPane.setLeftAnchor(shipImageView, x);
                    AnchorPane.setTopAnchor(shipImageView, y);
                    shipImageView.setVisible(true);
                    double finalX = x;
                    double finalY = y;
                    cellBtn.setOnAction(e->{
                        if(checkGameBoard(coord, currBoatSize, isHorizontal) && currIndex <= boatList.size()) {
                            placeOnGameBoard(coord, isHorizontal);
                            placeShipOnAnchorPane(finalX, finalY);
                            boatsLeftBtn.setText(Integer.toString(--boatsLeft));
//                        }else
//                            System.out.println("cant place");

//                    printMatrix();
                        }
                    });

                }


            }
        }

    }

    private void hideShip() {
        shipImageView.setVisible(false);
    }

    private boolean checkGameBoard(Coord coord, int shipSize, boolean isHorizontal){
        if(isHorizontal)
        {
            for(int i = coord.col; i < coord.col + shipSize-1; i++)
                if(gameMatrix[coord.row][i])
                    return false;
        }
        else {
            for(int i = coord.row; i < coord.row + shipSize-1; i++)
                if(gameMatrix[i][coord.col])
                    return false;
        }
        return true;
    }
    private void placeOnGameBoard(Coord coord, boolean isHorizontal){
//        System.out.println(coord.row + " " + coord.row);
        if(isHorizontal){
//            System.out.println("placing horizontally " + currIndex + " " + currBoatSize);
            ArrayList<String> coordArray = new ArrayList<>();
            for(int i = coord.col; i < coord.col + currBoatSize; i++)
            {
//                System.out.println(i);
                gameMatrix[coord.row][i] = true;
                Coord newCoord = new Coord(coord.row, i);
                coordArray.add(newCoord.toString());
            }
            shipCoordArray.add(coordArray);
        }
        else{

//            System.out.println("placing vertically");
            ArrayList<String> coordArray = new ArrayList<>();
            for (int i = coord.row; i < coord.row + currBoatSize; i++){
//                System.out.println(i);
                gameMatrix[i][coord.col] = true;
                Coord newCoord = new Coord(i, coord.col);
                coordArray.add(newCoord.toString());
            }
            shipCoordArray.add(coordArray);
        }
    }
    private void placeShipOnAnchorPane(Double x, Double y) {
        ImageView placedShipImageView = new ImageView(currShipImage);
        placedShipImageView.setMouseTransparent(true);
        placedShipImageView.setOpacity(1);  // Adjust opacity if needed

        if (!isHorizontal) {
            // For vertical, you might rotate the image or use a different image
            Rotate rotate = new Rotate();
            rotate.setAngle(90);
            rotate.setPivotX(currShipImage.getWidth() / 2);
            rotate.setPivotY(currShipImage.getHeight() / 2);
            placedShipImageView.getTransforms().add(rotate);
            // Adjust y to center the image if needed
        }
//        System.out.println("updating ship, index: " + currIndex);

        if(currIndex < boatList.size())
            currShipImage = changeCurrBoat();
        else
        {
//            System.out.println("placed last ship");
            currIndex++;
            rotateBtn.setDisable(true);
            confirmBtn.setDisable(false);
            if(aiGameScene!=null)
                aiGameScene.setGameAfterTurn(1);

        }

        shipImageView.setImage(currShipImage);

        AnchorPane.setLeftAnchor(placedShipImageView, x);
        AnchorPane.setTopAnchor(placedShipImageView, y);
        wrapper.getChildren().add(placedShipImageView);
    }

    private void highlightButtons(Coord coord, boolean enter, int shipSize) {
        if (isHorizontal) {
            if (coord.row != -1 && coord.col != -1) {
                boolean outOfBounds = (coord.col + shipSize - 1 >= NUM_COLS);
                boolean shipPresent = false; // Flag to check if any ship is present in the span

                // First pass to check for ship presence or out of bounds
                for (int i = coord.col; i <= coord.col + shipSize - 1; i++) {
                    if (i >= NUM_COLS || gameMatrix[coord.row][i]) {
                        shipPresent = true;
                        break;
                    }
                }

                // Second pass to set the color
                for (int i = coord.col; i <= coord.col + shipSize - 1 && i < NUM_COLS; i++) {
                    Button b = buttonMap.get(new Coord(coord.row, i));
                    if (b != null) {
                        if (enter) {
                            String color = shipPresent || outOfBounds ? "rgba(255, 0, 0, 0.5)" : "rgba(58, 255, 114, 0.83)";
                            b.setStyle("-fx-background-color: " + color + ";");
                        } else {
                            revertButtonStyle(b);
                        }
                    }
                }
            }
        } else {
            if (coord.row != -1 && coord.col != -1) {
                boolean outOfBounds = (coord.row + shipSize - 1 >= NUM_ROWS);
                boolean shipPresent = false; // Flag to check if any ship is present in the span

                // First pass to check for ship presence or out of bounds
                for (int i = coord.row; i <= coord.row + shipSize - 1; i++) {
                    if (i >= NUM_ROWS || gameMatrix[i][coord.col]) {
                        shipPresent = true;
                        break;
                    }
                }

                // Second pass to set the color
                for (int i = coord.row; i <= coord.row + shipSize - 1 && i < NUM_ROWS; i++) {
                    Button b = buttonMap.get(new Coord(i, coord.col));
                    if (b != null) {
                        if (enter) {
                            String color = shipPresent || outOfBounds ? "rgba(255, 0, 0, 0.5)" : "rgba(58, 255, 114, 0.83)";
                            b.setStyle("-fx-background-color: " + color + ";");
                        } else {
                            revertButtonStyle(b);
                        }
                    }
                }
            }
        }
    }


    private void clearHighlights() {
        for (Button button : buttonMap.values()) {
            Coord coord = (Coord) button.getUserData();
            if(coord.row > -1 && coord.col > -1)
                revertButtonStyle(button);  // Reset style to default
        }
    }


    private void revertButtonStyle(Button button) {
        button.setStyle(""); // Clears any inline styles set previously
        button.getStyleClass().clear(); // Clear all style classes
        button.getStyleClass().add("cell-btn"); // Re-add the original style class
    }





    private void assemblePanel(){
        panelHbox = new HBox(40);
        panelHbox.getStyleClass().add("panel-hbox");
        panelHbox.setEffect(greyBoxDropShadow);
        root.getChildren().add(panelHbox);

        panelHbox.getChildren().addAll(backStack, rightVbox);
    }
    private void createRightVbox(AiGameScene aiGameScene){
        rightVbox = new VBox(17);
        rightVbox.setAlignment(Pos.CENTER);


        greyBoxDropShadow = new DropShadow();
        greyBoxDropShadow.setRadius(10.0);
        greyBoxDropShadow.setOffsetX(5.0);
        greyBoxDropShadow.setOffsetY(8.0);
        greyBoxDropShadow.setRadius(0);
        greyBoxDropShadow.setColor(Color.web("B9B9B9"));

        DropShadow boatBoxDropShadow = new DropShadow();
        boatBoxDropShadow.setRadius(10.0);
        boatBoxDropShadow.setOffsetX(5.0);
        boatBoxDropShadow.setOffsetY(8.0);
        boatBoxDropShadow.setRadius(0);
        boatBoxDropShadow.setColor(Color.web("2872A4"));
        characterBox = new StackPane();
        characterView  = new ImageView(new Image("/captainImg/CaptainBlue2.png"));
        characterView.setPreserveRatio(false);
        characterView.setFitHeight(300);
        characterView.setFitWidth(300);

        characterBox.setEffect(boatBoxDropShadow);
        characterBox.getStyleClass().add("boat-box");
        characterBox.getChildren().add(characterView);


        DropShadow redBoxDropShadow = new DropShadow();
        redBoxDropShadow.setRadius(10.0);
        redBoxDropShadow.setOffsetX(5.0);
        redBoxDropShadow.setOffsetY(8.0);
        redBoxDropShadow.setRadius(0);
        redBoxDropShadow.setColor(Color.web("CA5C5C"));

        rotateBtn = new Button();
        Image rotateImg = new Image("/btnImages/Rotate.png");
        ImageView rotateImgView = new ImageView(rotateImg);
        rotateBtn.getStyleClass().add("rotate-btn");
        rotateBtn.setEffect(redBoxDropShadow);
        rotateBtn.setGraphic(rotateImgView);

        rotateBtn.setOnAction(e->{
            rotateShip();
            clearHighlights();
            changeOffset(currIndex-1);
//            System.out.println("Rotating Ship");

//            KeyEvent event = new KeyEvent(
//                    KeyEvent.KEY_PRESSED,
//                    "R",
//                    "R",
//                    KeyCode.R,
//                    false,
//                    false,
//                    false,
//                    false
//            );
//            scene.fireEvent(event);

        });
        scene.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("R") && currIndex <= boatList.size()){
                rotateShip();
                clearHighlights();
                changeOffset(currIndex-1);
//                System.out.println("changing offset");
            }

        });




        Image confirmImg = new Image("/btnImages/Confirm.png");
        ImageView confirmImgView = new ImageView(confirmImg);
        confirmBtn.getStyleClass().add("confirm-btn");
        confirmBtn.setEffect(redBoxDropShadow);
        confirmBtn.setGraphic(confirmImgView);


        HBox btnHbox = new HBox(20);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1); // Small min size to ensure it takes up space

        btnHbox.getChildren().addAll(rotateBtn, spacer, confirmBtn);


        VBox boatsLeftVbox = new VBox(20);
        boatsLeftVbox.getStyleClass().add("boats-left-box");

        Text boatsLeftText = new Text("Ships Left");
        boatsLeftVbox.setEffect(redBoxDropShadow);
        boatsLeftText.getStyleClass().add("boats-left-text");

        boatsLeftBtn = new Button(Integer.toString(boatsLeft));
        boatsLeftBtn.setEffect(greyBoxDropShadow);
        boatsLeftBtn.getStyleClass().add("boats-left-btn");


        boatsLeftVbox.getChildren().addAll(boatsLeftText, boatsLeftBtn);
        boatsLeftVbox.setAlignment(Pos.CENTER);


        rightVbox.getChildren().addAll(characterBox, btnHbox, boatsLeftVbox);



    }

    private void rotateShip() {
//        System.out.println("Attempting to rotate: isHorizontal = " + isHorizontal + ", isVisible = " + shipImageView.isVisible());
//        if (shipImageView.isVisible()) {
            double width = shipImageView.getBoundsInLocal().getWidth();
            double height = shipImageView.getBoundsInLocal().getHeight();

            Rotate rotate = new Rotate();
            rotate.setPivotX(width / 2);
            rotate.setPivotY(height / 2);

            if (isHorizontal) {
                // Rotating from horizontal to vertical
                rotate.setAngle(90);

            } else {
                // Rotating from vertical to horizontal
                rotate.setAngle(-90);
            }

            isHorizontal = !isHorizontal;
            shipImageView.getTransforms().add(rotate);

            // Update highlighting and image placement logic if needed
            clearHighlights();
            if (lastHoveredCoord != null) {
                highlightButtons(lastHoveredCoord, true, currBoatSize);
                showShip(lastHoveredCoord, buttonMap.get(lastHoveredCoord));
            }
//        }
    }


    private void printMatrix(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                System.out.print("(x: " + i+ ", " + "y: " + j + ") " + gameMatrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();     System.out.println();

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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGameScene.css")).toExternalForm());
        Image background = new Image("/transitionImages/background.jpg");
        ImageView backgroundView = new ImageView(background);
        root.getChildren().add(backgroundView);
    }

    private void createBeginningTransition(){
        Image cloudTransitionImage = new Image("/transitionImages/cloudTransition.png");
        ImageView cloudTransitionImageView = new ImageView(cloudTransitionImage);

        root.getChildren().add(cloudTransitionImageView);
        cloudTransition = new TranslateTransition(Duration.seconds(2), cloudTransitionImageView);
        cloudTransition.setToY(-cloudTransitionImage.getHeight()); // Moves to the center of the screen vertically
        cloudTransition.setCycleCount(1);
        cloudTransition.setAutoReverse(false);
    }
    public Scene getScene(){return scene;}

    public TranslateTransition getCloudTransition() {
        return cloudTransition;
    }


    public Button getConfirmBtn(){return confirmBtn;}

    public TranslateTransition getWaveTransition(){
        return waveTransition;
    }

    public StackPane getYourGame(){return backStack;}

    public HBox getPanelHbox(){return panelHbox;}


    public boolean[][] getGameMatrix(){return gameMatrix;}

    public ArrayList<ArrayList<String>> getShipCoordArray(){
        return shipCoordArray;
    }

}
