import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GuiLogic extends Application {

    private Stage stageApplication;
    private final int LIST_WIDTH = 200;

    /**
     * Constructor for GuiLogic class.
     * @param stageApplication
     */
    public GuiLogic(Stage stageApplication) {
        this.stageApplication = stageApplication;
    }

    /**
     * Opens a new window for the settings.
     * TODO: language, light button, etc (TBD)
     * @param connection Connection object
     */
    public void settings(Connection connection, GUI gui) {
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        Label portLabel = new Label("Poort: ");
        TextField portText = new TextField();
        Button ok = new Button("Ok");
        Button apply = new Button("Toepassen");

        gridPane.add(portLabel, 1, 1);
        gridPane.add(portText, 2, 1);
        gridPane.add(apply, 3, 4);
        gridPane.add(ok, 4, 4);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stageApplication);

        if (!connection.getPort().isEmpty()) {
            portText.setText(connection.getPort());
        }

        //OK button
        ok.setOnAction(event -> {
            connection.setPort(portText.getText());
            stage.close();
        });

        //Apply button
        apply.setOnAction(event -> {
            connection.setPort(portText.getText());
            if (portText.getText().equals(connection.getPort())) {
                apply.setDisable(true);
            } else {
                apply.setDisable(false);
            }
        });

        //Check if the value in the text field changes, if it does change: enable apply button
        portText.textProperty().addListener((observable, oldValue, newValue) -> {
            apply.setDisable(false);
        });

        stage.setScene(scene);
        stage.setTitle("iFad - Instellingen");
        stage.show();
    }


    /**
     * Opens a new window for the help menu.
     */
    public void help() {
        Stage stage = new Stage();
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 621, 600);
        hBox.setMinWidth(620);
        hBox.setSpacing(10);

        //ListView
        ListView<String> helpMenus = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList("Afstandsbediening", "Route", "Programma", "About");
        helpMenus.setItems(items);
        helpMenus.setMinWidth(150);
        helpMenus.setMinHeight(scene.getHeight());

        //Select the first item in the list
        helpMenus.getSelectionModel().selectFirst();

        //List menu event
        helpMenus.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            int index = helpMenus.getSelectionModel().getSelectedIndex();
            //Check which item is selected in the list.
            switch (index) {
                case 0:
                    //Remote information
                    //Clear the panel
                    hBox.getChildren().clear();
                    //Redraw the window, not very efficient
                    hBox.getChildren().addAll(helpMenus, helpRemote());
                    //Align parts in the window
                    helpMenus.setMinWidth(this.LIST_WIDTH);
                    helpMenus.setMinHeight(scene.getHeight());
                    break;
                case 1:
                    //Route information
                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(helpMenus, helpRoute());
                    helpMenus.setMinWidth(this.LIST_WIDTH);
                    helpMenus.setMinHeight(scene.getHeight());
                    break;
                case 2:
                    //GUI information
                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(helpMenus, helpProgram());
                    helpMenus.setMinWidth(this.LIST_WIDTH);
                    helpMenus.setMinHeight(scene.getHeight());
                    break;
                case 3:
                    //About page
                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(helpMenus, helpAbout());
                    helpMenus.setMinWidth(this.LIST_WIDTH);
                    break;
            }
        }));

        hBox.getChildren().addAll(helpMenus, helpRemote());
        vBox.getChildren().add(hBox);

        stage.setScene(scene);
        stage.setTitle("iFad - Help");
        stage.show();
    }

    /**
     * This method will return a HBox object with the information about the bot.
     * @return Node object
     */
    private Node helpRoute() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label label = new Label("Om een route te starten moet de computer verbinding hebben met\n" +
                "de iFad bot. Vervolgens kan er op de gewenste knop/positie in het\n" +
                "programma geklikt worden. Daarna klikt u op 'Start route'.\n\n" +
                "Mocht de verkeerde positie aangewezen zijn kan er op een andere\n" +
                "positie geklikt worden, of kunt u op 'Verwijder route' klikken.");

        hBox.getChildren().add(label);
        return hBox;
    }

    /**
     * This method will return a HBox with information about RoboWorks.
     * @return Node object
     */
    private Node helpAbout() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label label = new Label("Dit programma is gemaakt door RoboWorks.\n\nCopyright 2020");

        hBox.getChildren().add(label);
        return hBox;
    }

    /**
     * This method will return a HBox with information about the GUI and what functions it has.
     * @return Node object
     */
    private Node helpProgram() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label label = new Label("De bot kan ook bediend worden door dit\nprogramma. Hiervoor moet de bot aanstaan, dan\n" +
                "moet er een poort worden ingevuld. Daarna drukt\n" +
                "u op de knop 'Verbinden' op het start scherm.\nVervolgens kunt u een positie kiezen om de bot\n" +
                "naartoe te laten rijden. Mocht het nodig zijn om de\nbot te besturen via dit programma, dan kunt u\n" +
                "linksboven naar 'Control' gaan.\n\nOp het 'Control' scherm krijgt u een aantal\n" +
                "knoppen te zien: vooruit, achteruit, links, rechts,\nstop en licht. Deze knoppen kunnen met het toetsenbord\n" +
                "bediend worden.\nVooruit: 'W' of pijltje omhoog\nAchteruit: 'S' of pijltje omlaag\nLinks: 'A' of pijltje naar links\n" +
                "Rechts: 'D' of pijltje naar rechts\nStop: spatiebalk\n\nOnder deze knoppen zit een schuifregelaar. Deze wordt\ngebruikt om de snelheid te bepalen.");

        hBox.getChildren().add(label);
        return hBox;
    }

    /**
     * This method will return a HBox with information about the IR-remote. This includes the button layout.
     * @return Node object
     */
    private Node helpRemote() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        Label buttons = new Label("De bot kan op afstand bediend\nworden d.m.v. de bijgeleverde\nafstandsbediening. Hieronder\nstaat aangegeven welke\nfunctie bij welke knop hoort.\n\n" +
                "Aan/uit: zet de bot aan of uit.\n" +
                "0 t/m 9: snelheid\n" +
                "Vol -: naar links sturen\n" +
                "Vol +: naar rechts sturen\n" +
                "CH +: naar voor rijden\n" +
                "CH -: naar achter rijden\n" +
                "TV/VCR: in een cirkel rijden\n" +
                "Vierkant (knop boven de 1): rij in een driehoek");

        //Image
        Image imageRemote = new Image("file:resources/remote.png");
        ImageView imageViewRemote = new ImageView(imageRemote);
        imageViewRemote.setFitWidth(150);
        imageViewRemote.setPreserveRatio(true);
        imageViewRemote.setSmooth(true);

        hBox.getChildren().addAll(buttons, imageViewRemote);
        return hBox;
    }

    /**
     * Opens a new window for the bluetooth control.
     * TODO: Change the way the speed slider behaves. Depends on what the client wants, or if it impacts performance too much.
     */
    public Window control(Connection connection) {
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        Slider speedSlider = new Slider();
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 300, 250);
        Button forward = new Button("Vooruit");
        Button reverse = new Button("Achteruit");
        Button left = new Button("Links");
        Button right = new Button("Rechts");
        Button stop = new Button("Stop");
        Button light = new Button("Licht");

        //Slider
        speedSlider.setMin(10);
        speedSlider.setMax(100);
        speedSlider.setMajorTickUnit(50);
        speedSlider.setMinorTickCount(10);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setValue(40);
        speedSlider.setBlockIncrement(10);
        speedSlider.setSnapToTicks(true);
        speedSlider.setStyle("-fx-padding: 10 10 10 15");

        //If the slider gets moved this listener will fire.
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Round double to nearest 10.
            int send = (int) Math.round((double) newValue / 10.0) * 10;
            if (send < 100) {
                send = send / 10;
            } else if (send >= 100) {
                send = 10;
            }
            connection.sendInteger(send);
            System.out.println(Math.round((double) newValue / 10.0) * 10);
        });

        //Settings for gridpane
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        //Spacing around the routeGridPane (clockwise, first int is top, second int is right, third int is bottom, fourth int is left)
        gridPane.setStyle("-fx-padding: 10 10 10 15");
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(forward, 1, 0);
        gridPane.add(reverse, 1, 1);
        gridPane.add(left, 0, 1);
        gridPane.add(right, 2, 1);
        gridPane.add(stop, 0, 0);
        gridPane.add(light, 2, 0);

        forward.setFocusTraversable(false);
        reverse.setFocusTraversable(false);
        left.setFocusTraversable(false);
        right.setFocusTraversable(false);
        speedSlider.setFocusTraversable(false);
        stop.setFocusTraversable(false);
        light.setFocusTraversable(false);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode().equals(KeyCode.SPACE)) {
                stop.fire();
            } else if (key.getCode().equals(KeyCode.W) || key.getCode().equals(KeyCode.UP)) {
                forward.fire();
            } else if (key.getCode().equals(KeyCode.S) || key.getCode().equals(KeyCode.DOWN)) {
                reverse.fire();
            } else if (key.getCode().equals(KeyCode.A) || key.getCode().equals(KeyCode.LEFT)) {
                left.fire();
            } else if (key.getCode().equals(KeyCode.D) || key.getCode().equals(KeyCode.RIGHT)) {
                right.fire();
            }
        });

        //Forward button event
        forward.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendCommand("W");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Reverse button event
        reverse.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendCommand("S");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Left button event
        left.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendCommand("A");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Right button event
        right.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendCommand("D");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }

        });

        //Stop button event
        stop.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendInteger(16);
            } else {
                errorPopUp("Geen verbinding", "De verbinding met de bot is verbroken", "");
            }
        });

        //Light button event
        light.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendInteger(16);
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Button spacing
        gridPane.setHalignment(forward, HPos.CENTER);
        gridPane.setHalignment(stop, HPos.CENTER);
        gridPane.setHalignment(light, HPos.CENTER);

        vBox.getChildren().addAll(gridPane, speedSlider);

        stage.initOwner(this.stageApplication);
        stage.setScene(scene);
        stage.setTitle("iFad - Afstandsbediening");
        stage.show();
        return stage;
    }

    /**
     * Opens a new pop-up window with error icon and OK button.
     * @param title String, the title of the window.
     * @param header String, the reason of the pop-up.
     * @param context String, optional information about the pop-up.
     */
    public void errorPopUp(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("iFad - " + title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.show();
    }

    /**
     * Opens a new pop-up window with '?'-icon and OK and cancel button.
     * @param title String, the title of the window.
     * @param header String, the reason of the pop-up.
     * @param context String, optional information about the pop-up and/or what the buttons will do.
     */
    public void confirmationPopUp(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("iFad - " + title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
