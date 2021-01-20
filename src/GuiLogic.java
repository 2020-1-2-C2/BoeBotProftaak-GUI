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

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class contains most of the logic that is needed for the GUI. This class also provides the nodes for all the help
 * pages, the settings tab, the remote control tab and the route selector.
 *
 * @author Lars Hoendervangers, Tom Martens, Berend de Groot
 */
public class GuiLogic extends Application {

    private Stage stageApplication;
    //LIST_WIDTH is used for the help list.
    private final static int LIST_WIDTH = 200;
    private ArrayList<String> positions;
    private int positionsIndex;
    private int buttonsPressed;
    private String routeLabelText;

    /**
     * Constructor for GuiLogic class.
     * @param stageApplication Stage object.
     */
    public GuiLogic(Stage stageApplication) {
        this.stageApplication = stageApplication;
        this.positions = new ArrayList<>();
        this.positionsIndex = 0;
        this.buttonsPressed = 0;
        this.routeLabelText = "Huidige route: ";
    }

    /**
     * Reset class attributes.
     */
    public void resetRoute() {
        this.positionsIndex = 0;
        this.positions.clear();
        this.buttonsPressed = 0;
        this.routeLabelText = "Huidige route: ";
    }

    /**
     * This method creates a VBox containing: <p>
     * - GridPane: this stores the given amount of buttons.
     * - Label: this will let the user know if the maximum amount of positions is selected.
     * - {@link #routeLabel()}: this shows the selected positions.</p>
     *
     * @param routePlanner {@link RoutePlanner} object.
     * @param x int (gridsize)
     * @param y int (gridsize)
     * @return Node, in this case the VBox containing the above mentioned items.
     */
    public Node routePanel(RoutePlanner routePlanner, int x, int y) {
        GridPane gridPane = new GridPane();
        VBox vBox = new VBox();
        Label maxReached = new Label();

        if (x > 0 && y > 0) {
            //Create Route control buttons
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    Button button = new Button((i) + "," + ((y - j) - 1));
                    gridPane.add(button, i, j);
                        button.setOnAction(event -> {
                            if (this.buttonsPressed < 5) {
                                maxReached.setText("");
                                routePlanner.planner(button.getText());
                                button.setDisable(true);
                                this.positions.add(button.getText());
                                this.buttonsPressed++;
                                vBox.getChildren().remove(2);
                                vBox.getChildren().add(routeLabel());
                                if (this.buttonsPressed == 5) {
                                    gridPane.setDisable(true);
                                }
                            } else {
                                gridPane.setDisable(true);
                                maxReached.setText("U kunt maximaal 5 posities opgeven");
                            }
                        });
                }
            }
        } else {
            return new Label("Voer een geldige afmeting in!");
        }
        vBox.getChildren().addAll(gridPane, maxReached, routeLabel());
        //setStyle is in this case used to set the spacing around the vBox and to draw a nice border around it,
        //in CSS (Cascading Style Sheet) format.
        vBox.setStyle("-fx-padding: 2;" +
                "-fx-border-width: 2;" +
                "-fx-border-color: lightgray;");
        return vBox;
    }

    /**
     * This will display the positions ArrayList which is created in the constructor.
     * @return Label which is used in {@link #routePanel}
     */
    public Label routeLabel() {
        Label label = new Label(this.routeLabelText);
        if (this.positions.size() != 0) {
            label.setText(this.routeLabelText + " -> " + this.positions.get(this.positionsIndex));
            this.routeLabelText = label.getText();
            this.positionsIndex++;
        }
        return label;
    }

    /**
     * Opens a new window for the settings.
     * @param connection {@link Connection} object
     */
    public void settings(Connection connection, GUI gui) {
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        Label portLabel = new Label("Poort: ");
        TextField portText = new TextField();
        Button ok = new Button("Ok");
        Button apply = new Button("Toepassen");
        Button autoSensorTweak = new Button("Automatisch");
        Label sensorTweakLabel = new Label("Lijnsensor drempelwaarde: ");
        HBox buttonBox = new HBox();
        Scene scene = new Scene(gridPane);
        stage.setResizable(false);
        portText.setMaxWidth(110);

        gridPane.add(portLabel, 1, 1);
        gridPane.add(portText, 2, 1);
        gridPane.add(sensorTweakLabel, 1, 2);
        gridPane.add(autoSensorTweak, 2, 2);
        gridPane.add(buttonBox, 2, 4);
        buttonBox.getChildren().addAll(apply, ok);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stageApplication);

        //If the connection port label is NOT empty, then get the port and print it in the text field.
        if (!connection.getPort().isEmpty()) {
            portText.setText(connection.getPort());
        }

        //Automatic button
        autoSensorTweak.setOnAction(event -> {
            connection.sendInteger('%');
        });

        //OK button
        ok.setOnAction(event -> {
            connection.setPort(portText.getText());
            gui.refreshConnection(portText.getText());
            stage.close();
        });

        //Apply button
        apply.setOnAction(event -> {
            connection.setPort(portText.getText());
            gui.refreshConnection(portText.getText());
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
     * Opens a new window for the help menu and calls {@link #helpRemote()}, {@link #helpRoute()},
     * {@link #helpProgram()} and {@link #helpAbout()}.
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
        ObservableList<String> items = FXCollections.observableArrayList("Afstandsbediening", "Route", "About");
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
                    helpMenus.setMinWidth(LIST_WIDTH);
                    helpMenus.setMinHeight(scene.getHeight());
                    break;
                case 1:
                    //Route information
                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(helpMenus, helpRoute());
                    helpMenus.setMinWidth(LIST_WIDTH);
                    helpMenus.setMinHeight(scene.getHeight());
                    break;
                case 2:
                    //About page
                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(helpMenus, helpAbout());
                    helpMenus.setMinWidth(LIST_WIDTH);
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
     * @return Node object (HBox)
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
     * @return Node object (HBox)
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
     *
     * NOTE: This method is not used, but has to be in the code if we need to show the Remote control page.
     *
     * @return HBox
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
     * @return Node object (HBox)
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
                "Source: rij in een driehoek");

        //Image
        Image imageRemote = new Image("file:resources/remote.png");
        ImageView imageViewRemote = new ImageView(imageRemote);
        //Set the width
        imageViewRemote.setFitWidth(150);
        //Lock the ratio, so it scales nicely
        imageViewRemote.setPreserveRatio(true);
        //Smooth the image so it is less pixelated edges.
        imageViewRemote.setSmooth(true);

        hBox.getChildren().addAll(buttons, imageViewRemote);
        return hBox;
    }

    /**
     * Opens a new window for the bluetooth control.
     *
     * NOTE: This method will only be used if the enableRC boolean in GUI is set to true.
     *
     * @return a new window.
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
        //Set minimum and maximum values.
        speedSlider.setMin(10);
        speedSlider.setMax(100);
        //MajorTickUnit defines which values are printed below the slider.
        speedSlider.setMajorTickUnit(50);
        //MinorTickCount defines what the smallest increment can be.
        speedSlider.setMinorTickCount(10);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        //Start/default value.
        speedSlider.setValue(40);
        //Move the slider to the closest to the closest value which is divisible by 10.
        speedSlider.setBlockIncrement(10);
        speedSlider.setSnapToTicks(true);
        //setStyle is in this case used to set the spacing around the slider, in CSS (Cascading Style Sheet) format.
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
        //Spacing around the routeGridPane (clockwise, first int is top, second int is right, third int is bottom,
        // fourth int is left). Made available by CSS format.
        gridPane.setStyle("-fx-padding: 10 10 10 15");
        gridPane.setAlignment(Pos.CENTER);

        //Add button to a logical position on the gridPane.
        gridPane.add(forward, 1, 0);
        gridPane.add(reverse, 1, 1);
        gridPane.add(left, 0, 1);
        gridPane.add(right, 2, 1);
        gridPane.add(stop, 0, 0);
        gridPane.add(light, 2, 0);

        //Disable going through buttons using arrow or tab key.
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
                connection.sendString("W");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Reverse button event
        reverse.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendString("S");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Left button event
        left.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendString("A");
            } else {
                errorPopUp("Geen verbinding" , "De verbinding met de bot is verbroken", "");
            }
        });

        //Right button event
        right.setOnAction(event -> {
            if (connection.isConnected()) {
                connection.sendString("D");
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
     * Plays a .WAV file from the resources folder. <p>
     * <b>Note: this can cause an exception.</b>
     * @param name The filename + extension to play.
     */
    void playSound(String name){
        try { //Had to handle exceptions according to getClip().
            URL connectedJingleURLPath = this.getClass().getResource("/" + name);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(connectedJingleURLPath)); //Creates an AudioInputStream from the URL, which accesses the Resources folder.
            clip.start(); //Plays the clip.
        } catch (Exception e){
            System.out.println(e.getMessage()); //Should never happen. If there is time we should test switching audio-devices while using the application.
        }
    }

    /**
     * Standard start method that is auto-generated by Application.
     * @param primaryStage Stage
     * @throws Exception Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
