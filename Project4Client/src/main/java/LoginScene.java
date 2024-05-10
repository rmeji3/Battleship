import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import jdk.javadoc.internal.doclets.formats.html.PackageUseWriter;

import java.util.*;

public class LoginScene {



    private Text message;

    private HBox messageHbox;

    private VBox playAiVbox;
    private StackPane root;

    private Button playBtn;

    private Scene scene;

    private DropShadow playBtnDropShadow;
    private ImageView backgroundView;

    private ImageView background2View;

    private ImageView backgroundView3;
    private VBox lVbox;

    private TranslateTransition textTrans;

    private Button easyBtn;

    private Button buyBtn;

    private Button lastItemSelected;

    private Button mediumBtn;
    private Button impossibleBtn;
    private TranslateTransition textTransR;

    private TranslateTransition leaveQPaneR;

    private ArrayList<Button> buttons = new ArrayList<>();

    private ArrayList<Button> boughtItems = new ArrayList<>();

    private HashMap<String , Button> currInvEmotes = new HashMap<>();
    private String currInvChars = "/captainImg/CaptainBlue2.png";

    private ArrayList<Button> invEmotes = new ArrayList<>();
    private ArrayList<Button> invChars = new ArrayList<>();
    private TranslateTransition cloudTransitionR;

    private TranslateTransition confirmBoxTransition;
    private TranslateTransition playbtnTransition;

    private TranslateTransition logoViewTransition;

    private Button leaveQueueBtn;

    private Image cloudTransitionImage;

    private TranslateTransition logoShadowViewTransition;
    private ImageView cloudTransitionImageView;
    private TranslateTransition bgTransition1;
    private TranslateTransition bgTransition2;

    private ParallelTransition parallelTransition;
    private Button onlineBtn;

    private ParallelTransition pCloudTrans;

    private TranslateTransition moveLB;

    private AnchorPane leaveQPane;

    private ParallelTransition pMenuTrans;

    private TranslateTransition cloudTransition;
    private TranslateTransition cloudTransitionOffline;

    private Scene nextScene;

    private Button confirmUsernameBtn;

    private Button backBtnInv;
    private Button loginBtn;
    private Button offlineBtn;
    private Text vbuxAmount;
    private Button leaderboardBtn;

    private Button helpBtn;

    private Button mainMenuExitBtn;
    private Button exitTopRBtn;

    private VBox loginVBox = new VBox(40);
    private VBox mainMenuVBox;

    private HBox confirmHBox;


    private TextField loginField;

    private DropShadow loginBoxDropShadow;
    private DropShadow mainmenuBoxDropShadow;

    private TranslateTransition moveMenuBox;

    private HBox WaitTextBox;

    private Text welcomeText;

    private final Stage stage;

    private AnchorPane exitPane;
    private AnchorPane leavePane;
    private ScrollPane charEmoteScroll;
    private ScrollPane charInventoryScroll;

    private Button lastChar;
    private VBox inventoryEmotesVbox;
    private VBox inventoryCharsVbox;

    private  DropShadow boughtShadow;
    public LoginScene(Stage primaryStage){

        boughtShadow = new DropShadow();
        boughtShadow.setRadius(10.0);
        boughtShadow.setOffsetX(5.0);
        boughtShadow.setOffsetY(8.0);
        boughtShadow.setRadius(0);
        boughtShadow.setColor(Color.color(46/255.0, 208/255.0, 92/255.0, 0.83));

        playBtnDropShadow = new DropShadow();
        playBtnDropShadow.setRadius(10.0);
        playBtnDropShadow.setOffsetX(5.0);
        playBtnDropShadow.setOffsetY(8.0);
        playBtnDropShadow.setRadius(0);
        playBtnDropShadow.setColor(Color.web("CA5C5C"));



        loginBoxDropShadow = new DropShadow();
        loginBoxDropShadow.setRadius(10.0);
        loginBoxDropShadow.setOffsetX(5.0);
        loginBoxDropShadow.setOffsetY(8.0);
        loginBoxDropShadow.setRadius(0);
        loginBoxDropShadow.setColor(Color.web("B9B9B9"));
        createBackground();

        createMiddleGraphics();
        createMainMenu();
        createloginVbox();

        createConfirmExit();
        TranslateTransition leaveQtrans = new TranslateTransition(Duration.seconds(1), leaveQPane);
        leaveQtrans.setToY(0); // Moves to the center of the screen vertically
        leaveQtrans.setCycleCount(1);
        leaveQtrans.setAutoReverse(false);

        root.getChildren().add(leaveQPane);
        pCloudTrans = new ParallelTransition(cloudTransition, textTrans, leaveQtrans);
        stage = primaryStage;
        playBtn.setOnAction(e -> parallelTransition.play());


        message = new Text("");
        message.getStyleClass().add("shop-text");
        message.setMouseTransparent(true);
//        message.setVisible(false);


        messageHbox = new HBox();
        messageHbox.setEffect(mainmenuBoxDropShadow);
        messageHbox.setAlignment(Pos.CENTER);
        messageHbox.getStyleClass().add("message-hbox");
        messageHbox.getChildren().add(message);
        messageHbox.setMouseTransparent(true);
        messageHbox.setVisible(false);

        root.getChildren().add(messageHbox);
    }

    private void createConfirmExit(){
        Text confirmText = new Text("Are you sure you want to \nexit?");
        confirmText.getStyleClass().add("confirm-text");
        VBox confirmVBox = new VBox(20);
        confirmVBox.setAlignment(Pos.CENTER);
        confirmVBox.getStyleClass().add("login-vbox");;

        confirmVBox.setEffect(loginBoxDropShadow);
        confirmVBox.getChildren().add(confirmText);

        Button yesBtn = new Button("Exit");
        yesBtn.getStyleClass().add("online-btn");
        yesBtn.setEffect(playBtnDropShadow);
        yesBtn.setOnAction(e->{
            System.exit(0);
        });
        Button noBtn = new Button("Cancel");
        noBtn.getStyleClass().add("online-btn");
        noBtn.setEffect(playBtnDropShadow);

        HBox confirmHBox = new HBox(15);
        confirmHBox.getChildren().addAll(yesBtn, noBtn);
        confirmHBox.getStyleClass().add("btn-hbox");
        confirmVBox.getChildren().add(confirmHBox);
        root.getChildren().add(confirmVBox);
        confirmVBox.setTranslateY(-1100);

        TranslateTransition bgTransitionReverse1 = new TranslateTransition(Duration.seconds(1), backgroundView3);
        bgTransitionReverse1.setToY(-1080); // Move the image to its normal position

        TranslateTransition bgTransitionReverse2 = new TranslateTransition(Duration.seconds(1), backgroundView);
        bgTransitionReverse2.setToY(0); // Move the image to its normal position


        TranslateTransition leaveQueueBtn = new TranslateTransition(Duration.seconds(1), this.exitTopRBtn);
        leaveQueueBtn.setToY(0);  // Adjust the Y value based on your screen size
        leaveQueueBtn.setCycleCount(1);
        leaveQueueBtn.setAutoReverse(false);

        confirmBoxTransition = new TranslateTransition(Duration.seconds(1), confirmVBox);
        confirmBoxTransition.setToY(0); // Moves to the center of the screen vertically
        confirmBoxTransition.setCycleCount(1);
        confirmBoxTransition.setAutoReverse(false);

        TranslateTransition playAgainBoxTransition = new TranslateTransition(Duration.seconds(1), loginVBox);
        playAgainBoxTransition.setToY(0); // Moves to the center of the screen vertically
        playAgainBoxTransition.setCycleCount(1);
        playAgainBoxTransition.setAutoReverse(false);

        TranslateTransition animatedVboxTransition = new TranslateTransition(Duration.seconds(1), loginVBox);
        animatedVboxTransition.setToY(1200); // Moves to the center of the screen vertically
        animatedVboxTransition.setCycleCount(1);
        animatedVboxTransition.setAutoReverse(false);

        TranslateTransition exitbtnReverseTransition = new TranslateTransition(Duration.seconds(1), this.exitTopRBtn);
        exitbtnReverseTransition.setToY(0);  // Adjust the Y value based on your screen size
        exitbtnReverseTransition.setCycleCount(1);
        exitbtnReverseTransition.setAutoReverse(false);


        TranslateTransition animatedVboxReverseTransition = new TranslateTransition(Duration.seconds(1), confirmVBox);
        animatedVboxReverseTransition.setToY(-1200); // Moves to the center of the screen vertically
        animatedVboxReverseTransition.setCycleCount(1);
        animatedVboxReverseTransition.setAutoReverse(false);



        TranslateTransition bgTransition3 = new TranslateTransition(Duration.seconds(1), backgroundView3);
        bgTransition3.setToY(0); // Move the image to its normal position
        bgTransition3.setCycleCount(1);
        bgTransition3.setAutoReverse(false);




        ParallelTransition parallelTransition2 = new ParallelTransition(leaveQueueBtn, animatedVboxTransition, bgTransition3, bgTransition1, confirmBoxTransition);
        this.exitTopRBtn.setOnAction(e-> {
            parallelTransition2.play();
            bgTransition1.setToY(1080);

        });
        ParallelTransition reverseTransition = new ParallelTransition(exitbtnReverseTransition, animatedVboxReverseTransition, bgTransitionReverse1, bgTransitionReverse2, playAgainBoxTransition);
        noBtn.setOnAction(e->reverseTransition.play());
    }

    private void createBackground(){
        root = new StackPane();
        scene = new Scene(root, 1900, 1000);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/loginScene.css")).toExternalForm());
        Image background = new Image("/transitionImages/background.jpg");
        backgroundView = new ImageView(background);
        background2View = new ImageView(background);
        backgroundView3 = new ImageView(background);
        backgroundView3.setTranslateY(-1080);
    }

    private void createMiddleGraphics(){
        VBox imageVbox = new VBox();
        HBox topSpacer = new HBox();
        playBtn = new Button("Play");
        playBtn.setEffect(playBtnDropShadow);
        topSpacer.getStyleClass().add("top-spacer");
        imageVbox.setAlignment(Pos.CENTER);



        Image logo = new Image("/Logo.png");
        ImageView logoView = new ImageView(logo);

        Image logoShadow = new Image("/transitionImages/cloudshadow.png");
        ImageView logoShadowView = new ImageView(logoShadow);


        playBtn.getStyleClass().add("play-btn");


        root.getChildren().addAll(background2View ,backgroundView, backgroundView3, imageVbox);
        backgroundView.setTranslateY(-1080);

        VBox.setMargin(logoShadowView, new Insets(250, 0, -300, 0));
        imageVbox.getChildren().addAll(topSpacer, logoView, logoShadowView);

        DropShadow playAgainBoxDropShadow = new DropShadow();
        playAgainBoxDropShadow.setRadius(10.0);
        playAgainBoxDropShadow.setOffsetX(5.0);
        playAgainBoxDropShadow.setOffsetY(8.0);
        playAgainBoxDropShadow.setRadius(0);
        playAgainBoxDropShadow.setColor(Color.web("CA5C5C"));


        Image exit = new Image("/btnImages/Exit.png");
        ImageView exitView = new ImageView(exit);
        exitView.setPreserveRatio(true);
        exitView.setFitWidth(40);  // Set the fit width
        exitView.setFitHeight(40);
        exitTopRBtn = new Button();
        exitTopRBtn.setGraphic(exitView);
        exitPane = new AnchorPane();
        exitTopRBtn.getStyleClass().add("exit-btn");
        exitPane.getChildren().add(exitTopRBtn);
        exitTopRBtn.setEffect(playAgainBoxDropShadow);
        AnchorPane.setTopAnchor(exitTopRBtn, 10.0);
        AnchorPane.setRightAnchor(exitTopRBtn, 10.0);
        exitPane.setTranslateY(-1080);
        root.getChildren().add(exitPane);

        leaveQueueBtn = new Button();
        leaveQueueBtn.setDisable(true);
        leaveQueueBtn.setGraphic(new ImageView(new Image("/btnImages/backArrow.png")));
        leaveQPane = new AnchorPane();
        leaveQueueBtn.getStyleClass().add("exit-btn");
        leaveQPane.getChildren().add(leaveQueueBtn);
        leaveQueueBtn.setEffect(playAgainBoxDropShadow);
        AnchorPane.setTopAnchor(leaveQueueBtn, 10.0);
        AnchorPane.setRightAnchor(leaveQueueBtn, 10.0);
        leaveQPane.setTranslateY(1080);


        StackPane.setMargin(playBtn, new Insets(800, 0, 0, 0));
        root.getChildren().add(playBtn);

        playbtnTransition = new TranslateTransition(Duration.seconds(1), playBtn);
        playbtnTransition.setToY(1200);  // Adjust the Y value based on your screen size
        playbtnTransition.setCycleCount(1);
        playbtnTransition.setAutoReverse(false);

        logoViewTransition = new TranslateTransition(Duration.seconds(1), logoView);
        logoViewTransition.setToY(1200);  // Adjust the Y value based on your screen size
        logoViewTransition.setCycleCount(1);
        logoViewTransition.setAutoReverse(false);

        logoShadowViewTransition = new TranslateTransition(Duration.seconds(1), logoShadowView);
        logoShadowViewTransition.setToY(1200);  // Adjust the Y value based on your screen size
        logoShadowViewTransition.setCycleCount(1);
        logoShadowViewTransition.setAutoReverse(false);

        bgTransition1 = new TranslateTransition(Duration.seconds(1), backgroundView);
        bgTransition1.setToY(0); // Move the image to its normal position

        bgTransition2 = new TranslateTransition(Duration.seconds(1), background2View);
        bgTransition2.setToY(1080); // Move the image to its normal position
    }


    private void createloginVbox(){
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.getStyleClass().add("login-vbox");
        loginBtn = new Button("login");
        loginBtn.getStyleClass().add("login-btn");
        loginBtn.setEffect(playBtnDropShadow);

        loginVBox.setEffect(loginBoxDropShadow);

        // Initially position the VBox above the screen
        loginVBox.setTranslateY(-1100);  // Assuming screen height, adjust accordingly

        loginField = new TextField();
        loginField.setPromptText("Username");
        loginField.getStyleClass().add("login-text-field");
        loginVBox.getChildren().addAll(loginField);

        HBox btnHbox = new HBox(15);
        btnHbox.getChildren().addAll(loginBtn);
        btnHbox.getStyleClass().add("btn-hbox");
        root.getChildren().add(loginVBox);
        loginVBox.getChildren().add(btnHbox);


        TranslateTransition animatedVboxTransition = new TranslateTransition(Duration.seconds(1), loginVBox);
        animatedVboxTransition.setToY(0); // Moves to the center of the screen vertically
        animatedVboxTransition.setCycleCount(1);
        animatedVboxTransition.setAutoReverse(false);

        TranslateTransition exitBtnTransition = new TranslateTransition(Duration.seconds(1), exitPane);
        exitBtnTransition.setToY(0); // Moves to the center of the screen vertically
        exitBtnTransition.setCycleCount(1);
        exitBtnTransition.setAutoReverse(false);


        parallelTransition = new ParallelTransition(playbtnTransition, logoViewTransition, logoShadowViewTransition, animatedVboxTransition, bgTransition1, bgTransition2, exitBtnTransition);


        System.setProperty("javafx.animation.fullspeed", "true");

        WaitTextBox = new HBox(0); // Tight packing of characters
        Text t = new Text("Looking for Game");
        t.getStyleClass().add("queue-txt");
        WaitTextBox.getChildren().add(t);
        String content = "...";
        for (int i = 0; i < content.length(); i++) {
            Text text = new Text(content.substring(i, i + 1));
            text.getStyleClass().add("queue-txt");
            WaitTextBox.getChildren().add(text);

            // Create a TranslateTransition for each character
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), text);
            // Delay should consider only the start time of the first cycle
            tt.setDelay(Duration.millis(i * 500)); // Starts one after another, half second apart
            tt.setByY(-10); // Smaller vertical movement
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.setAutoReverse(true);
            tt.setInterpolator(Interpolator.EASE_BOTH);
            tt.play();
        }

        WaitTextBox.setAlignment(Pos.CENTER);

        textTrans = new TranslateTransition(Duration.seconds(2), WaitTextBox);
        textTrans.setToY(0);
        textTrans.setToX(0);

        cloudTransitionImage = new Image("/transitionImages/cloudTransition.png");
        cloudTransitionImageView = new ImageView(cloudTransitionImage);

        root.getChildren().add(cloudTransitionImageView);
        root.getChildren().add(WaitTextBox);
        WaitTextBox.setTranslateY(1080);
        cloudTransitionImageView.setTranslateY(cloudTransitionImage.getHeight());

        cloudTransition = new TranslateTransition(Duration.seconds(1), cloudTransitionImageView);
        cloudTransition.setToY(0); // Moves to the center of the screen vertically
        cloudTransition.setCycleCount(1);
        cloudTransition.setAutoReverse(false);

        cloudTransitionOffline = new TranslateTransition(Duration.seconds(1), cloudTransitionImageView);
        cloudTransitionOffline.setToY(0); // Moves to the center of the screen vertically
        cloudTransitionOffline.setCycleCount(1);
        cloudTransitionOffline.setAutoReverse(false);
        Image exit = new Image("/btnImages/backArrow.png");
        ImageView exitView = new ImageView(exit);
        exitView.setPreserveRatio(true);
        exitView.setFitWidth(30);  // Set the fit width
        exitView.setFitHeight(30);
    }

    private void createMainMenu(){
        mainMenuVBox = new VBox(20);
        mainMenuVBox.setAlignment(Pos.CENTER);
        mainMenuVBox.getStyleClass().add("main-menu-vbox");

        welcomeText = new Text("welcome, ");
        welcomeText.getStyleClass().add("welcome-text");

        mainMenuVBox.getChildren().add(welcomeText);
        mainmenuBoxDropShadow = new DropShadow();
        mainmenuBoxDropShadow.setRadius(10.0);
        mainmenuBoxDropShadow.setOffsetX(5.0);
        mainmenuBoxDropShadow.setOffsetY(8.0);
        mainmenuBoxDropShadow.setRadius(0);
        mainmenuBoxDropShadow.setColor(Color.web("B9B9B9"));
        mainMenuVBox.setEffect(mainmenuBoxDropShadow);




        // Initially position the VBox above the screen
        mainMenuVBox.setTranslateY(-2200);  // Assuming screen height, adjust accordingly

        onlineBtn = new Button("Online");
        offlineBtn = new Button("VS AI");
        leaderboardBtn = new Button("Leaderboard");
        helpBtn = new Button("Help");
        mainMenuExitBtn = new Button("Exit");


        onlineBtn.getStyleClass().add("offline-btn");
        onlineBtn.setEffect(playBtnDropShadow);

        offlineBtn.getStyleClass().add("offline-btn");
        offlineBtn.setEffect(playBtnDropShadow);

        leaderboardBtn.getStyleClass().add("offline-btn");
        leaderboardBtn.setEffect(playBtnDropShadow);

        helpBtn.getStyleClass().add("offline-btn");
        helpBtn.setEffect(playBtnDropShadow);

        mainMenuExitBtn.getStyleClass().add("offline-btn");
        mainMenuExitBtn.setEffect(playBtnDropShadow);

        Text confirmText = new Text("Are you sure you want to \nexit?");
        confirmText.getStyleClass().add("confirm-text");
        VBox confirmVBox = new VBox(20);
        confirmVBox.setAlignment(Pos.CENTER);
        confirmVBox.getStyleClass().add("login-vbox");;

        confirmVBox.setEffect(loginBoxDropShadow);
        confirmVBox.getChildren().add(confirmText);

        Button yesBtn = new Button("Exit");
        yesBtn.getStyleClass().add("online-btn");
        yesBtn.setEffect(playBtnDropShadow);
        yesBtn.setOnAction(e->{
            System.exit(0);
        });
        Button noBtn = new Button("Cancel");
        noBtn.getStyleClass().add("online-btn");
        noBtn.setEffect(playBtnDropShadow);

        confirmHBox = new HBox(15);
        confirmHBox.getChildren().addAll(yesBtn, noBtn);
        confirmHBox.getStyleClass().add("btn-hbox");
        confirmVBox.getChildren().add(confirmHBox);
        root.getChildren().add(confirmVBox);
        confirmVBox.setTranslateY(-1100);


        HBox btnH = new HBox(10);
        Button shopBtn = new Button();
        shopBtn.setGraphic(new ImageView(new Image("/btnImages/shop.png")));
        AnchorPane shopInvAnchor = new AnchorPane();
        shopBtn.getStyleClass().add("shop-btn");
        shopBtn.setEffect(playBtnDropShadow);

        Button invBtn = new Button();
        invBtn.setGraphic(new ImageView(new Image("/btnImages/inv.png")));
        invBtn.getStyleClass().add("shop-btn");

        invBtn.setEffect(playBtnDropShadow);
        AnchorPane.setTopAnchor(btnH, 10.0);
        AnchorPane.setRightAnchor(btnH, 10.0);
        btnH.getChildren().addAll(shopBtn,invBtn);

        HBox currencyHbox = new HBox(10);
        currencyHbox.getStyleClass().add("currency-box");
        currencyHbox.setEffect(mainmenuBoxDropShadow);
        ImageView vbux = new ImageView(new Image("/vbux.png"));
        vbux.setFitWidth(50);  // Set the width of the image
        vbux.setFitHeight(50); // Set the height of the image
        vbuxAmount = new Text("9999");
        vbuxAmount.getStyleClass().add("vbux-amount");

        currencyHbox.getChildren().addAll(vbux, vbuxAmount);
        currencyHbox.setAlignment(Pos.CENTER);





        shopInvAnchor.getChildren().addAll(btnH, currencyHbox);
        root.getChildren().add(shopInvAnchor);
        shopInvAnchor.setTranslateY(-1080);

        TranslateTransition conBox = new TranslateTransition(Duration.seconds(.5), confirmVBox);
        conBox.setToY(0);

        TranslateTransition conBoxR = new TranslateTransition(Duration.seconds(.5), confirmVBox);
        conBoxR.setToY(-1080);

        TranslateTransition moveMenu = new TranslateTransition(Duration.seconds(.5), mainMenuVBox);
        moveMenu.setToY(1080);

        TranslateTransition moveMenuR = new TranslateTransition(Duration.seconds(.5), mainMenuVBox);
        moveMenuR.setToY(0);

        TranslateTransition moveTop = new TranslateTransition(Duration.seconds(.5), shopInvAnchor);
        moveTop.setToY(1080);

        TranslateTransition moveTopR = new TranslateTransition(Duration.seconds(.5), shopInvAnchor);
        moveTopR.setToY(0);


        ParallelTransition pMenu1 = new ParallelTransition(moveMenu, conBox, moveTop);


        ParallelTransition pMenu2 = new ParallelTransition(moveMenuR, conBoxR, moveTopR);

        noBtn.setOnAction(e->{
            pMenu2.play();
        });

        mainMenuExitBtn.setOnAction(e-> {
//            System.out.println("moving");
            pMenu1.play();

        });

//        Button shopBtn = new Button("Shop");
//        shopBtn.getStyleClass().add("offline-btn");
//        shopBtn.setEffect(playBtnDropShadow);
//        Button inventoryBtn = new Button("Inventory");
//        inventoryBtn.getStyleClass().add("offline-btn");
//        inventoryBtn.setEffect(playBtnDropShadow);

        VBox btnVbox = new VBox(25);
        btnVbox.setAlignment(Pos.CENTER);
        btnVbox.getChildren().addAll(onlineBtn, offlineBtn,leaderboardBtn, helpBtn, mainMenuExitBtn);
        btnVbox.getStyleClass().add("btn-vbox");
        root.getChildren().add(mainMenuVBox);
        mainMenuVBox.getChildren().add(btnVbox);
        TranslateTransition moveLogin = new TranslateTransition(Duration.seconds(.5), loginVBox);
        moveLogin.setToY(1080); // Moves to the center of the screen vertically
        moveLogin.setCycleCount(1);
        moveLogin.setAutoReverse(false);

        TranslateTransition moveExitBtnTrans = new TranslateTransition(Duration.seconds(.5) , exitTopRBtn);
        moveExitBtnTrans.setToY(1080); // Moves to the center of the screen vertically
        moveExitBtnTrans.setCycleCount(1);
        moveExitBtnTrans.setAutoReverse(false);

        TranslateTransition menuTransition = new TranslateTransition(Duration.seconds(.5), mainMenuVBox);
        menuTransition.setToY(0); // Moves to the center of the screen vertically
        menuTransition.setCycleCount(1);
        menuTransition.setAutoReverse(false);

        TranslateTransition shopTrans = new TranslateTransition(Duration.seconds(.5), shopInvAnchor);
        shopTrans.setToY(0); // Moves to the center of the screen vertically
        shopTrans.setCycleCount(1);
        shopTrans.setAutoReverse(false);


        pMenuTrans = new ParallelTransition(moveLogin, menuTransition, moveExitBtnTrans, shopTrans);


        playAiVbox = new VBox(10);
        playAiVbox.setEffect(loginBoxDropShadow);
        easyBtn = new Button("easy");
        easyBtn.setEffect(playBtnDropShadow);

        mediumBtn = new Button("medium");
        mediumBtn.setEffect(playBtnDropShadow);

        impossibleBtn = new Button("impossible");
        impossibleBtn.setEffect(playBtnDropShadow);

        Button backBtn = new Button("back");
        backBtn.setEffect(playBtnDropShadow);


        playAiVbox.setAlignment(Pos.CENTER);
        playAiVbox.getChildren().addAll(easyBtn, mediumBtn, impossibleBtn, backBtn);

        playAiVbox.getStyleClass().add("diff-vbox");
        easyBtn.getStyleClass().add("diff-btn");
        mediumBtn.getStyleClass().add("diff-btn");
        impossibleBtn.getStyleClass().add("diff-btn");
        backBtn.getStyleClass().add("diff-btn");

        root.getChildren().add(playAiVbox);
        playAiVbox.setTranslateY(-1080);

        TranslateTransition moveAiBox = new TranslateTransition(Duration.seconds(.5), playAiVbox);
        moveAiBox.setToY(0);

        moveMenuBox = new TranslateTransition(Duration.seconds(.5), mainMenuVBox);
        moveMenuBox.setToY(0);

        offlineBtn.setOnAction(e->{
            moveAiBox.setToY(0);
            moveMenuBox.setToY(1080);
            moveAiBox.play();
            moveMenuBox.play();

        });
        backBtn.setOnAction(e->{
            moveAiBox.setToY(-1080);
            moveMenuBox.setToY(0);
            moveAiBox.play();
            moveMenuBox.play();
        });


        VBox leaderBoardVbox = new VBox(20);
        leaderBoardVbox.getStyleClass().add("leader-vbox");
        leaderBoardVbox.setEffect(loginBoxDropShadow);
        leaderBoardVbox.setAlignment(Pos.CENTER);
        leaderBoardVbox.setTranslateY(-1080);

        root.getChildren().add(leaderBoardVbox);

        Text lBText = new Text("Leaderboard");
        lBText.getStyleClass().add("leader-text");

        HBox titles = new HBox();
        titles.setAlignment(Pos.CENTER);

        Text titles1 = new Text("USER");
        titles1.getStyleClass().add("titles-text");
        Text titles2 = new Text("WINS");
        titles2.getStyleClass().add("titles-text");
        Pane spacer1 = new Pane();
        Pane spacer2 = new Pane();
        spacer1.setMinWidth(400);
        titles.getChildren().addAll(titles1, spacer1, titles2);

        lVbox = new VBox(10);
//        lVbox.getStyleClass().add("leader-vbox-inner");
        lVbox.setAlignment(Pos.CENTER);

        Button back2 = new Button("back");
        back2.getStyleClass().add("back-btn");
        back2.setEffect(playBtnDropShadow);


        moveLB = new TranslateTransition(Duration.seconds(.5), leaderBoardVbox);
        moveAiBox.setToY(0);
        leaderBoardVbox.getChildren().addAll(lBText,titles ,lVbox, back2);


        back2.setOnAction(e->{
            moveLB.setToY(-1080);
            moveMenuBox.setToY(0);
            moveLB.play();
            moveMenuBox.play();
        });



        VBox helpBox = new VBox(10);
        helpBox.setAlignment(Pos.CENTER);
        helpBox.getStyleClass().add("help-box");


        ImageView help = new ImageView(new Image("/Help.png"));

        root.getChildren().add(helpBox);
        helpBox.setTranslateY(-1080);
        helpBox.setEffect(loginBoxDropShadow);

        Button b2 = new Button("back");

        b2.getStyleClass().add("back2-btn");
        b2.setEffect(playBtnDropShadow);


        helpBox.getChildren().addAll(help, b2);

        TranslateTransition helpTr = new TranslateTransition(Duration.seconds(.5), helpBox);
        helpTr.setToY(0);

        helpBtn.setOnAction(e->{
            helpTr.setToY(0);
            moveMenuBox.setToY(1080);
            helpTr.play();
            moveMenuBox.play();
        });
        b2.setOnAction(e->{

//            System.out.println("pressing back");
            helpTr.setToY(-1080);
            moveMenuBox.setToY(0);
            helpTr.play();
            moveMenuBox.play();
        });




        VBox shopVbox = new VBox(20);
        shopVbox.setEffect(mainmenuBoxDropShadow);
        shopVbox.setAlignment(Pos.CENTER);
        shopVbox.getStyleClass().add("shop-vbox");


        Text shopText = new Text("SHOP");
        shopText.getStyleClass().add("shop-text");

        HBox pricesHbox = new HBox();
        pricesHbox.setAlignment(Pos.CENTER);

        Text charPrices = new Text("1,000");
        charPrices.getStyleClass().add("shop-text");
        Text emotePrices = new Text("500");
        emotePrices.getStyleClass().add("shop-text");

        Pane spacerr = new Pane();
        spacerr.setMinWidth(200);
        Pane spacerrr = new Pane();
        spacerrr.setMinWidth(200);
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        pricesHbox.getChildren().addAll(spacerr, charPrices, spacer, emotePrices, spacerrr);

        HBox itemsHbox = new HBox(50);
        itemsHbox.setAlignment(Pos.CENTER);
        HBox charHbox = new HBox(20);
        Button char1 = new Button();
        buttons.add(char1);
        char1.getStyleClass().add("char-box");
        char1.setEffect(playBtnDropShadow);
        ImageView char1View = new ImageView(new Image("/captainImg/mccarty.png"));
        char1.setUserData("/captainImg/mccarty.png");
        char1.setGraphic(char1View);
        setListener(char1);

        Button char2 = new Button();
        char2.getStyleClass().add("char-box");
        char2.setEffect(playBtnDropShadow);
        char2.setUserData("/captainImg/fortnite.png");
        buttons.add(char2);
        ImageView char2View = new ImageView(new Image("/captainImg/fortnite.png"));
        char2.setGraphic(char2View);
        setListener(char2);


        charHbox.getChildren().addAll(char1,char2);

        

        VBox emoteVbox = new VBox(20);
        HBox emoteHbox1 = new HBox(20);
        HBox emoteHbox2 = new HBox(20);
        emoteVbox.getChildren().addAll(emoteHbox1, emoteHbox2);

        Button emote1 = new Button("",new ImageView(new Image("/emotes/Emote10.png")));
        emote1.getStyleClass().add("emote-box");
        emote1.setEffect(playBtnDropShadow);
        emote1.setUserData("/emotes/Emote10.png");
        buttons.add(emote1);
        setListener(emote1);
        Button emote2 = new Button("",new ImageView(new Image("/emotes/Emote11.png")));
        emote2.getStyleClass().add("emote-box");
        emote2.setEffect(playBtnDropShadow);
        emote2.setUserData("/emotes/Emote11.png");
        buttons.add(emote2);
        setListener(emote2);
        Button emote3 = new Button("",new ImageView(new Image("/emotes/Emote12.png")));
        emote3.getStyleClass().add("emote-box");
        emote3.setEffect(playBtnDropShadow);
        emote3.setUserData("/emotes/Emote12.png");
        buttons.add(emote3);
        setListener(emote3);
        emoteHbox1.getChildren().addAll(emote1, emote2, emote3);

        Button emote4 = new Button("", new ImageView(new Image("/emotes/Emote13.png")));
        emote4.getStyleClass().add("emote-box");
        emote4.setEffect(playBtnDropShadow);
        emote4.setUserData("/emotes/Emote13.png");
        buttons.add(emote4);
        setListener(emote4);
        Button emote5 = new Button("",new ImageView(new Image("/emotes/Emote14.png")));
        emote5.getStyleClass().add("emote-box");
        emote5.setEffect(playBtnDropShadow);
        emote5.setUserData("/emotes/Emote14.png");
        buttons.add(emote5);
        setListener(emote5);
        Button emote6 = new Button("",new ImageView(new Image("/emotes/Emote15.png")));
        emote6.getStyleClass().add("emote-box");
        emote6.setEffect(playBtnDropShadow);
        emote6.setUserData("/emotes/Emote15.png");
        buttons.add(emote6);
        setListener(emote6);

        emoteHbox2.getChildren().addAll(emote4, emote5, emote6);

        itemsHbox.getChildren().addAll(charHbox, emoteVbox);

        VBox inventoryVbox = new VBox(20);
        inventoryVbox.setAlignment(Pos.CENTER);
        inventoryVbox.setTranslateY(-1080);
        inventoryVbox.getStyleClass().add("shop-vbox");
        inventoryVbox.setEffect(mainmenuBoxDropShadow);

        Text inventoryText = new Text("INVENTORY");
        Text emoteLimit = new Text("must equip 9 emotes");
        inventoryText.getStyleClass().add("shop-text");
        emoteLimit.getStyleClass().add("shop-text");

        HBox centerItems = new HBox(20);
        centerItems.setAlignment(Pos.CENTER);


        inventoryCharsVbox = new VBox(20);
        inventoryCharsVbox.setAlignment(Pos.CENTER);
        charInventoryScroll = new ScrollPane();
        charInventoryScroll.setMaxHeight(350);
        charInventoryScroll.setMinWidth(450);
        inventoryCharsVbox.setStyle("-fx-background-color: #D9D9D9");

        charInventoryScroll.setContent(new StackPane(inventoryCharsVbox));
        charInventoryScroll.setStyle("-fx-background: #D9D9D9;");

        inventoryEmotesVbox = new VBox(20);
        inventoryEmotesVbox.setAlignment(Pos.CENTER);
        inventoryEmotesVbox.setTranslateY(-5);
        inventoryEmotesVbox.setTranslateX(10);

        inventoryEmotesVbox.setStyle("-fx-background-color: #D9D9D9;");
        charEmoteScroll = new ScrollPane();


        charEmoteScroll.setMaxHeight(350);
        charEmoteScroll.setMinWidth(550);
        charEmoteScroll.setContent(new StackPane(inventoryEmotesVbox));
        inventoryEmotesVbox.setTranslateX(20);
        charEmoteScroll.setStyle("-fx-background: #D9D9D9;");
        backBtnInv = new Button("Back");
        backBtnInv.getStyleClass().add("back-btn");
        backBtnInv.setEffect(playBtnDropShadow);



        TranslateTransition invBoxTrans = new TranslateTransition(Duration.seconds(.5), inventoryVbox);
        shopTrans.setToY(0);
        TranslateTransition shopBtnTrans = new TranslateTransition(Duration.seconds(.5), btnH);
        shopBtnTrans.setToY(-1080);

        centerItems.getChildren().addAll(charInventoryScroll, charEmoteScroll);
        inventoryVbox.getChildren().addAll(inventoryText, emoteLimit, centerItems, backBtnInv);
        root.getChildren().add(inventoryVbox);
        invBtn.setOnAction(e->{

            moveMenuBox.setToY(1080);
            invBoxTrans.setToY(0);
            shopBtnTrans.setToY(-1080);
            shopBtnTrans.play();
            moveMenuBox.play();
            invBoxTrans.play();
        });

        backBtnInv.setOnAction(e->{

            moveMenuBox.setToY(0);
            invBoxTrans.setToY(-1080);
            shopBtnTrans.setToY(0);
            shopBtnTrans.play();
            moveMenuBox.play();
            invBoxTrans.play();
            buyBtn.setDisable(true);
            System.out.println(currInvEmotes);

        });

        HBox shopBtnHbox = new HBox(20);
        shopBtnHbox.setAlignment(Pos.CENTER);
        Button backBtnItem = new Button("Back");
        backBtnItem.setEffect(playBtnDropShadow);
        backBtnItem.getStyleClass().add("online-btn");
        buyBtn = new Button("Buy");
        buyBtn.setEffect(playBtnDropShadow);
        buyBtn.getStyleClass().add("online-btn");
        buyBtn.setDisable(true);

        shopBtnHbox.getChildren().addAll(backBtnItem, buyBtn);

        shopVbox.getChildren().addAll(shopText, pricesHbox, itemsHbox, shopBtnHbox);

        root.getChildren().add(shopVbox);
        shopVbox.setTranslateY(-1080);

        TranslateTransition shopBoxTrans = new TranslateTransition(Duration.seconds(.5), shopVbox);
        shopTrans.setToY(0);


        shopBtn.setOnAction(e->{

            moveMenuBox.setToY(1080);
            shopBoxTrans.setToY(0);
            shopBtnTrans.setToY(-1080);
            shopBtnTrans.play();
            moveMenuBox.play();
            shopBoxTrans.play();
        });

        backBtnItem.setOnAction(e->{

            moveMenuBox.setToY(0);
            shopBoxTrans.setToY(-1080);
            shopBtnTrans.setToY(0);
            shopBtnTrans.play();
            moveMenuBox.play();
            shopBoxTrans.play();
            buyBtn.setDisable(true);
        });







    }

    private Button createInvEmotes(String filename){
        Button b = new Button();
        b.getStyleClass().add("emote-box");
        b.setEffect(playBtnDropShadow);
        b.setUserData(filename);
        setInvListenerEmoji(b);
        invEmotes.add(b);
        ImageView i = new ImageView(new Image(filename));
//        i.setFitWidth(200);  // Set the width of the image
//        i.setFitHeight(330); // Set the height of the image
////        i.setPreserveRatio(true);
        b.setGraphic(i);


        return b;
    }
    private Button createInvChars(String filename){
        Button b = new Button();
        b.getStyleClass().add("char-box");
        b.setUserData(filename);
        b.setEffect(playBtnDropShadow);
        setInvListenerChars(b);
        ImageView i = new ImageView(new Image(filename));
        i.setFitWidth(200);  // Set the width of the image
//        i.setFitHeight(330); // Set the height of the image
        i.setPreserveRatio(true);
        b.setGraphic(i);
        return b;
    }

    public void refreshInvEmotes(ArrayList<String> inv, boolean isStart){
        inventoryEmotesVbox.getChildren().clear();
        int i = 0;
        HBox h = new HBox(20);
        inventoryEmotesVbox.getChildren().add(h);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-background-color: #D9D9D9;");
        for(String emote : inv){

            if(i % 3 == 0){
                h = new HBox(20);
                inventoryEmotesVbox.getChildren().add(h);
                h.setAlignment(Pos.CENTER);

                h.setStyle("-fx-background-color: #D9D9D9;");
            }
            Button b = createInvEmotes(emote);
            h.getChildren().add(b);
            if(currInvEmotes.containsKey(emote)){
                b.setStyle("-fx-background-color: rgba(58, 255, 114, 0.83)");
                b.setEffect(boughtShadow);
            }

            currInvEmotes.put(emote, b);
            if(isStart){
                currInvEmotes.put(emote, b);
                b.setStyle("-fx-background-color: rgba(58, 255, 114, 0.83)");
                b.setEffect(boughtShadow);
            }else{

            }
            blockAllOtherInvEmotes();
            i++;
        }



    }

    public void setInvListenerChars(Button b){

        b.setOnAction(e->{

            currInvChars = (String) b.getUserData();
            System.out.println("here" + (String) b.getUserData());
            b.setStyle("-fx-background-color: rgba(58, 255, 114, 0.83)");
            b.setEffect(boughtShadow);
            b.setDisable(true);
            lastChar.setStyle("-fx-background-color: #F87474");
            lastChar.setEffect(playBtnDropShadow);
            lastChar.setDisable(false);
            lastChar = b;

        });
    }
    public void setInvListenerEmoji(Button b){

        b.setOnAction(e->{

            String filename = (String) b.getUserData();
            if(currInvEmotes.containsKey(filename)) {
                b.setStyle("-fx-background-color: #F87474");
                b.setEffect(playBtnDropShadow);
                currInvEmotes.remove(filename);
            }else{
                if(currInvEmotes.size() < 9) {
                    currInvEmotes.put(filename, b);
                    b.setStyle("-fx-background-color: rgba(58, 255, 114, 0.83)");
                    b.setEffect(boughtShadow);
                }
            }
            backBtnInv.setDisable(currInvEmotes.size() < 9);
            if(currInvEmotes.size() == 9){
                blockAllOtherInvEmotes();
            }else {
                unblockAllOtherInvEmotes();
            }

        });
    }
    public void unblockAllOtherInvEmotes(){
        for(Button b : invEmotes){
            b.setDisable(false);
        }
    }
    public void blockAllOtherInvEmotes(){
        for(Button b : invEmotes){
            if(!currInvEmotes.containsValue(b)){
                b.setDisable(true);
            }
        }
    }


    public void refreshInvChars(ArrayList<String> inv, boolean isStart){
        inventoryCharsVbox.getChildren().clear();
        int i = 0;
        HBox h = new HBox(20);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-background-color: #D9D9D9;");
        inventoryCharsVbox.getChildren().add(h);
        for(String character : inv){

            if(i % 2 == 0){
                h = new HBox(20);
                inventoryCharsVbox.getChildren().add(h);
                h.setAlignment(Pos.CENTER);
                h.setStyle("-fx-background-color: #D9D9D9;");
            }
            Button b = createInvChars(character);
            h.getChildren().add(b);
            if((isStart && i == 0) || character.equals(currInvChars)){
                b.setStyle("-fx-background-color: rgba(58, 255, 114, 0.83)");
                b.setEffect(boughtShadow);
                lastChar = b;
                b.setDisable(true);
            }


            i++;
        }
    }
    public void moveLb(){
        moveLB.setToY(0);
        moveMenuBox.setToY(1080);
        moveLB.play();
        moveMenuBox.play();
    }

    private void setListener(Button b){
        b.setOnAction(e->{
            buyBtn.setDisable(false);
            b.setDisable(true);
//            b.setStyle("-fx-background-color: blue");
            lastItemSelected = b;
            resetOthers(b);
        });

    }

    private void resetOthers(Button b){
        for(Button btn : buttons){
            if(btn != lastItemSelected && !boughtItems.contains(btn)){
                btn.setDisable(false);
            }
        }
    }
    public void refreshLeaderBoard(ArrayList<String> list){
        int i = 1;
        lVbox.getChildren().clear();
        for(String message : list)
        {
//            System.out.println("line: " + message);
            String user = i + ".    " + message.substring(0,message.indexOf(','));
            String wins = message.substring(message.indexOf(',')+1);

            HBox box = new HBox();

            Text usersText = new Text(user);
            usersText.getStyleClass().add("titles-text");

            Text winsText = new Text(wins);
            winsText.getStyleClass().add("titles-text");

            Pane spacer1 = new Pane();

            spacer1.setMinWidth(100);

            Pane spacer2 = new Pane();

            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Pane spacer3 = new Pane();

            spacer3.setMinWidth(150);


            box.getChildren().addAll(spacer1, usersText,spacer2, winsText, spacer3);

            lVbox.getChildren().add(box);

            i++;
            if(i == 11)
                break;
        }
    }
    public Scene getLoginScene(){return scene;}

    public Button getLoginBtn(){return loginBtn;}
    public Button getOfflineBtn(){return offlineBtn;}

    public Button getLeaderboardBtn() {
        return leaderboardBtn;
    }

    public Button getConfirmUsernameBtn() {
        return confirmUsernameBtn;
    }

    public ParallelTransition pCloudTrans(){return pCloudTrans;}

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }

    public TextField getLoginField(){return this.loginField;}
    public TranslateTransition getTextTrans(){return textTrans;}
    public TranslateTransition getCloudTransition(){return cloudTransitionOffline;}

    public HBox getWaitText(){return WaitTextBox;}

    public ImageView cloudTransitionImageView(){return cloudTransitionImageView;}

    public Button getOnlineBtn(){
        return onlineBtn;
    }

    public void playMenuTrans(){
        pMenuTrans.play();
    }

    public void setWelcomeText(String name){welcomeText.setText(welcomeText.getText() + name);}

    public void leaveQueue(){
        cloudTransitionR = new TranslateTransition(Duration.seconds(1), cloudTransitionImageView);
        cloudTransitionR.setToY(cloudTransitionImage.getHeight()); // Moves to the center of the screen vertically
        cloudTransitionR.setCycleCount(1);
        cloudTransitionR.setAutoReverse(false);

        textTransR = new TranslateTransition(Duration.seconds(1), WaitTextBox);
        textTransR.setToY(1080); // Moves to the center of the screen vertically
        textTransR.setCycleCount(1);
        textTransR.setAutoReverse(false);

        leaveQPaneR = new TranslateTransition(Duration.seconds(1), leaveQPane);
        leaveQPaneR.setToY(1080); // Moves to the center of the screen vertically
        leaveQPaneR.setCycleCount(1);
        leaveQPaneR.setAutoReverse(false);

        ParallelTransition leaveQP = new ParallelTransition(cloudTransitionR, textTransR, leaveQPaneR);
        leaveQueueBtn.setDisable(true);

        leaveQP.play();
    }

    public Button getEasyBtn() {
        return easyBtn;
    }

    public Button getMediumBtn() {
        return mediumBtn;
    }

    public Button getImpossibleBtn() {
        return impossibleBtn;
    }

    public Button getLeaveQueueBtn() {
        return leaveQueueBtn;
    }

    public Button getHelpBtn(){return helpBtn;}
    public VBox getPlayAiVbox(){return playAiVbox;}

    public VBox getMainMenuVBox(){return mainMenuVBox;}

    public AnchorPane getLeaveQPane() {
        return leaveQPane;
    }

    public ArrayList<Button> getButtonsList(){return buttons;}
    public ArrayList<Button> getBoughtItems(){return boughtItems;}

    public Button getLastItemSelected(){return lastItemSelected;}

    public Button getBuy(){
        return buyBtn;
    }
    public void setMoney(int money){
        vbuxAmount.setText(Integer.toString(money));
    }

    public void showText(String message){
        this.message.setText(message);
        messageHbox.setVisible(true);
        PauseTransition p = new PauseTransition(Duration.seconds(1));
        p.play();
        p.setOnFinished(e->{
            this.message.setText("");
            messageHbox.setVisible(false);
        });
    }

    public String getCurrInvChars(){
        return currInvChars;
    }
    public ArrayList<String> getCurrInvEmotes(){
        Set<String> s = currInvEmotes.keySet();
        return new ArrayList<>(s);

    }
}
