import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EndScene {
    private VBox playAgainVBox;
    private DropShadow playAgainBoxDropShadow;
    private Button onlineBtn;
    private Button offlineBtn;
    private Button exitBtn;
    private HBox btnHbox;
    private Text text;
    private TranslateTransition exitbtnTransition;
    private TranslateTransition exitbtnReverseTransition;

    private TranslateTransition animatedVboxTransition;
    private TranslateTransition animatedVboxReverseTransition;

    private TranslateTransition bgTransition1;
    private TranslateTransition bgTransition2;
    private TranslateTransition bgTransitionReverse1;
    private TranslateTransition bgTransitionReverse2;
    private TranslateTransition confirmBoxTransition;
    private TranslateTransition playAgainBoxTransition;
    private ParallelTransition parallelTransition;
    private ParallelTransition reverseTransition;
    private StackPane root;
    private Scene scene;
    private Image background;
    private ImageView backgroundView;
    private Image background2;
    private ImageView background2View;
    private DropShadow boxDropShadow;
    private Image exit;
    private ImageView exitView;
    private Button noBtn;
    private HBox confirmHBox;

    //setgraphic
    AnchorPane exitPane;

    public EndScene(Stage primaryStage) {
        createBackground();
        createPlayAgainVbox();
        createConfirmExit();
        exitBtn.setOnAction(e -> parallelTransition.play());
        noBtn.setOnAction(e -> reverseTransition.play());

    }

    private void createBackground() {

        root = new StackPane();
        scene = new Scene(root, 1900, 1000);
        scene.getStylesheets().add(getClass().getResource("/CSS/EndScene.css").toExternalForm());
        background = new Image("/transitionImages/background.jpg");
        backgroundView = new ImageView(background);
        background2 = new Image("/transitionImages/background.jpg");
        background2View = new ImageView(background2);

    }

    private void createPlayAgainVbox() {
        playAgainVBox = new VBox(20);
        playAgainVBox.setAlignment(Pos.CENTER);
        playAgainVBox.getStyleClass().add("login-vbox");

        playAgainBoxDropShadow = new DropShadow();
        playAgainBoxDropShadow.setRadius(10.0);
        playAgainBoxDropShadow.setOffsetX(5.0);
        playAgainBoxDropShadow.setOffsetY(8.0);
        playAgainBoxDropShadow.setRadius(0);
        playAgainBoxDropShadow.setColor(Color.web("CA5C5C"));
//        playAgainVBox.setEffect(playAgainBoxDropShadow);

        boxDropShadow = new DropShadow();
        boxDropShadow.setRadius(10.0);
        boxDropShadow.setOffsetX(5.0);
        boxDropShadow.setOffsetY(8.0);
        boxDropShadow.setRadius(0);
        boxDropShadow.setColor(Color.web("B9B9B9"));
        playAgainVBox.setEffect(boxDropShadow);


        text = new Text("You Win!");
        text.getStyleClass().add("login-text");
        playAgainVBox.getChildren().addAll(text);
        onlineBtn = new Button("Online");
        offlineBtn = new Button("Offline");

        onlineBtn.getStyleClass().add("online-btn");
        onlineBtn.setEffect(playAgainBoxDropShadow);
        offlineBtn.getStyleClass().add("offline-btn");
        offlineBtn.setEffect(playAgainBoxDropShadow);

        exit = new Image("/btnImages/Exit.png");
        exitView = new ImageView(exit);
        exitView.setPreserveRatio(true);
        exitView.setFitWidth(40);  // Set the fit width
        exitView.setFitHeight(40);
        exitBtn = new Button();
        exitBtn.setGraphic(exitView);
        exitPane = new AnchorPane();
        exitBtn.getStyleClass().add("exit-btn");
        exitPane.getChildren().add(exitBtn);
        exitBtn.setEffect(playAgainBoxDropShadow);

        btnHbox = new HBox(15);
        btnHbox.getChildren().addAll(onlineBtn, offlineBtn);
        btnHbox.getStyleClass().add("btn-hbox");
        playAgainVBox.getChildren().add(btnHbox);



        root.getChildren().addAll(background2View ,backgroundView);
        AnchorPane.setTopAnchor(exitBtn, 10.0);
        AnchorPane.setRightAnchor(exitBtn, 10.0);

        root.getChildren().addAll(exitPane,playAgainVBox);
        backgroundView.setTranslateY(-1080);

        exitbtnTransition = new TranslateTransition(Duration.seconds(1), exitBtn);
        exitbtnTransition.setToY(1200);  // Adjust the Y value based on your screen size
        exitbtnTransition.setCycleCount(1);
        exitbtnTransition.setAutoReverse(false);

        exitbtnReverseTransition = new TranslateTransition(Duration.seconds(1), exitBtn);
        exitbtnReverseTransition.setToY(0);  // Adjust the Y value based on your screen size
        exitbtnReverseTransition.setCycleCount(1);
        exitbtnReverseTransition.setAutoReverse(false);


        bgTransition1 = new TranslateTransition(Duration.seconds(1), backgroundView);
        bgTransition1.setToY(0); // Move the image to its normal position

        bgTransition2 = new TranslateTransition(Duration.seconds(1), background2View);
        bgTransition2.setToY(1080); // Move the image to its normal position

        animatedVboxTransition = new TranslateTransition(Duration.seconds(1), playAgainVBox);
        animatedVboxTransition.setToY(1200); // Moves to the center of the screen vertically
        animatedVboxTransition.setCycleCount(1);
        animatedVboxTransition.setAutoReverse(false);


    }
    private void createConfirmExit(){
        Text confirmText = new Text("Are you sure you want to \nexit?");
        confirmText.getStyleClass().add("confirm-text");
        VBox confirmVBox = new VBox(20);
        confirmVBox.setAlignment(Pos.CENTER);
        confirmVBox.getStyleClass().add("login-vbox");;

        confirmVBox.setEffect(boxDropShadow);
        confirmVBox.getChildren().add(confirmText);

        Button yesBtn = new Button("Exit");
        yesBtn.getStyleClass().add("online-btn");
        yesBtn.setEffect(playAgainBoxDropShadow);
        yesBtn.setOnAction(e->{
            System.exit(0);
        });
        noBtn = new Button("Cancel");
        noBtn.getStyleClass().add("offline-btn");
        noBtn.setEffect(playAgainBoxDropShadow);

        confirmHBox = new HBox(15);
        confirmHBox.getChildren().addAll(yesBtn, noBtn);
        confirmHBox.getStyleClass().add("btn-hbox");
        confirmVBox.getChildren().add(confirmHBox);
        root.getChildren().add(confirmVBox);
        confirmVBox.setTranslateY(-1100);

        bgTransitionReverse1 = new TranslateTransition(Duration.seconds(1), backgroundView);
        bgTransitionReverse1.setToY(-1080); // Move the image to its normal position

        bgTransitionReverse2 = new TranslateTransition(Duration.seconds(1), background2View);
        bgTransitionReverse2.setToY(0); // Move the image to its normal position



        confirmBoxTransition = new TranslateTransition(Duration.seconds(1), confirmVBox);
        confirmBoxTransition.setToY(0); // Moves to the center of the screen vertically
        confirmBoxTransition.setCycleCount(1);
        confirmBoxTransition.setAutoReverse(false);

        playAgainBoxTransition = new TranslateTransition(Duration.seconds(1), playAgainVBox);
        playAgainBoxTransition.setToY(0); // Moves to the center of the screen vertically
        playAgainBoxTransition.setCycleCount(1);
        playAgainBoxTransition.setAutoReverse(false);

        animatedVboxReverseTransition = new TranslateTransition(Duration.seconds(1), confirmVBox);
        animatedVboxReverseTransition.setToY(-1200); // Moves to the center of the screen vertically
        animatedVboxReverseTransition.setCycleCount(1);
        animatedVboxReverseTransition.setAutoReverse(false);
        parallelTransition = new ParallelTransition(exitbtnTransition, animatedVboxTransition, bgTransition1, bgTransition2,confirmBoxTransition);

        reverseTransition = new ParallelTransition(exitbtnReverseTransition, animatedVboxReverseTransition, bgTransitionReverse1, bgTransitionReverse2,playAgainBoxTransition);
    }
}
