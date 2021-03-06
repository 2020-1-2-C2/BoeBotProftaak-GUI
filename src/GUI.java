import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Used for warning/error pop up
import java.util.Optional;

/**
 * This is the main class for the GUI. This will create the main window and call the different nodes in {@link GuiLogic}.<p>
 * This class will also create a {@link Connection} object and a {@link RoutePlanner} object.
 * </p>
 *
 * IMPORTANT: Remote control is currently disabled, to enable it change 'private boolean enableRC' to true.
 * @author Lars Hoendervangers, Tom Martens, Berend de Groot
 */
public class GUI extends Application {

    private String port;
    private Connection connection;
    private Button connect;
    private Button disconnect;
    private GuiLogic guiLogic;
    private Stage mainWindowStage;
    private RoutePlanner routePlanner;
    private boolean enableRC = false;

    /**
     * Starts the main window and calls the necessary functions.
     * @param primaryStage Stage object.
     */
    @Override
    public void start(Stage primaryStage) {
        this.port = "";
        this.connection = new Connection(this.port);
        this.connect = new Button();
        this.disconnect = new Button();
        this.guiLogic = new GuiLogic(this.mainWindowStage);
        this.connection.closeConnection();
        this.routePlanner = new RoutePlanner(this.connection);
        this.mainWindowStage = primaryStage;

        //Create components
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox,500,600);
        VBox vBoxBotStatus = new VBox();
        HBox routeButtons = new HBox();
        HBox connectionStatus = new HBox();

        //
        // Menu bar
        //

        //Create menu bar and the menus
        MenuBar menuBar = new MenuBar();
        Menu help = new Menu();
        Menu settings = new Menu();
        Menu control = new Menu();
        menuBar.setPrefWidth(primaryStage.getWidth());

        //Create label to enable click event for a menu
        Label helpLabel = new Label("Help");
        Label settingsLabel = new Label("Instellingen");

        //If this.enableRC is true, then load all the items for remote control.
        if (this.enableRC) {
            Label controlLabel = new Label("Afstandsbediening");
            control.setGraphic(controlLabel);
            menuBar.getMenus().addAll(control, settings, help);
            //Action for the control menu
            controlLabel.setOnMouseClicked(event -> {
                if (this.connection.isConnected()) {
                    this.guiLogic.control(this.connection);
                } else {
                    this.guiLogic.errorPopUp("Geen verbinding", "Er is geen verbinding met de bot", "");
                }
            });
        } else {
            menuBar.getMenus().addAll(settings, help);
        }

        //Put the label on to the corresponding menu
        help.setGraphic(helpLabel);
        settings.setGraphic(settingsLabel);

        //Actions for the help menu
        helpLabel.setOnMouseClicked(event -> this.guiLogic.help());

        //Actions for the settings menu
        settingsLabel.setOnMouseClicked(event -> {
            this.guiLogic.settings(this.connection, this);
        });

        // Route grid size
        Label routeSizeLabel = new Label("Aantal posities: ");
        Label routeSizeMax = new Label("Maximaal: 10 x 10");
        HBox routeHBox = new HBox();
        TextField routeX = new TextField();
        TextField routeY = new TextField();

        routeX.setText("3");
        routeX.setTooltip(new Tooltip("Voer hier een waarde van minimaal 1 en maximaal 10 in. (Dit is de x waarde)"));
        routeY.setText("3");
        routeY.setTooltip(new Tooltip("Voer hier een waarde van minimaal 1 en maximaal 10 in. (Dit is de y waarde)"));
        routeX.textProperty().addListener((observable, oldValue, newValue) -> {
            //If there is something in the textfield
            if (!newValue.isEmpty()) {
                //If the textfield only has integers
                //d means digit, + means one or more
                if (newValue.matches("\\d+")) {
                    //If the value in the textfield has changed
                    if (oldValue != newValue) {
                        //If it only contains numbers
                        if (Integer.parseInt(newValue) > 0) {
                            if (Integer.parseInt(newValue) <= 10) {
                                //Clear current route due to error
                                this.guiLogic.resetRoute();
                                //Reload Routegrid node
                                vBox.getChildren().remove(3);
                                vBox.getChildren().add(3, this.guiLogic.routePanel(this.routePlanner, Integer.parseInt(routeX.getText()), Integer.parseInt(routeY.getText())));
                            }
                        }
                    }
                }
            }
        });

        routeY.textProperty().addListener((observable, oldValue, newValue) -> {
            //If there is something in the textfield
            if (!newValue.isEmpty()) {
                //If the textfield only has integers
                //d means digit, + means one or more
                if (newValue.matches("\\d+")) {
                    //If the value in the textfield has changed
                    if (oldValue != newValue) {
                        //If it only contains numbers
                        if (Integer.parseInt(newValue) > 0) {
                            if (Integer.parseInt(newValue) <= 10) {
                                //Clear current route due to error
                                this.guiLogic.resetRoute();
                                //Reload Routegrid node
                                vBox.getChildren().remove(3);
                                vBox.getChildren().add(3, this.guiLogic.routePanel(this.routePlanner, Integer.parseInt(routeX.getText()), Integer.parseInt(routeY.getText())));
                            }
                        }
                    }
                }
            }
        });

        routeX.setMaxWidth(50);
        routeY.setMaxWidth(50);

        //
        // Start/Cancel/Confirm route buttons
        //
        VBox routeButtonsVBox = new VBox();
        Button startRoute = new Button("Start route");
        Button confirmRoute = new Button("Bevestig route");
        Button cancelRoute = new Button("Verwijder route");

        startRoute.setOnAction(event -> {
            //Start signal
            this.connection.sendString(" ");
            //Send route
            this.routePlanner.sendRoute();
            //Stop signal
            this.connection.sendString("~");
        });
        startRoute.setTooltip(new Tooltip("Stuur de route door en start met rijden."));

        confirmRoute.setOnAction(event -> System.out.println(this.routePlanner.getRoute()));
        confirmRoute.setTooltip(new Tooltip("Bevestig de route, maar stuur of start deze nog niet."));

        cancelRoute.setOnAction(event -> {
            this.routePlanner.clearRoute();
            this.guiLogic.resetRoute();
            //Reload Routegrid node
            vBox.getChildren().remove(3);
            vBox.getChildren().add(3, this.guiLogic.routePanel(this.routePlanner, Integer.parseInt(routeX.getText()), Integer.parseInt(routeY.getText())));
        });
        cancelRoute.setTooltip(new Tooltip("Verwijder de route"));

        routeButtons.getChildren().addAll(startRoute, confirmRoute, cancelRoute);
        routeHBox.getChildren().addAll(routeSizeLabel, routeX, routeY, routeSizeMax);

        //Vertical spacing
        routeButtonsVBox.setSpacing(2.0);
        routeButtonsVBox.getChildren().addAll(routeButtons, routeHBox);

        //
        // Main window
        //

        //connectButton settings
        this.connect.setDefaultButton(true);
        this.connect.setText("Verbinding maken");
        this.connect.setTooltip(new Tooltip("Verbind met de bot."));
        this.disconnect.setText("Verbinding verbreken");
        this.disconnect.setTooltip(new Tooltip("Verbreek de verbinding met de bot."));
        this.disconnect.setDisable(true);

        //Connect button event
        this.connect.setOnAction(event -> {
            this.connection.openConnection();
            this.connect.setDisable(true);
            this.connect.setDefaultButton(false);
            this.disconnect.setDisable(false);
            this.disconnect.setDefaultButton(true);
            //If no valid port is entered
            if (this.connection.getPort().equals(null) || this.connection.getPort().equals("")) {
                this.guiLogic.settings(this.connection, this);
                this.connect.setDefaultButton(true);
                this.disconnect.setDefaultButton(false);
                this.guiLogic.errorPopUp("Kan geen verbinding maken", "Er is geen poort ingevoerd!", "Druk op OK om een poort in te voeren");
                this.connect.setDisable(false);
                this.disconnect.setDisable(true);
            }
            //Send connect message
            this.connection.sendInteger(17);
            this.guiLogic.playSound("ConnectedJingle.wav");
        });

        //Disconnect button
        this.disconnect.setOnAction(event -> {
            this.connect.setDefaultButton(true);
            this.disconnect.setDefaultButton(false);
            disconnectPopUp("Verbinding verbreken", "Weet je zeker dat je de verbinding wilt verbreken?", "");
        });

        connectionStatus.getChildren().addAll(this.connect, this.disconnect);

        //setStyle is in this case used to set the spacing around the vBox and to draw a nice border around it,
        //in CSS (Cascading Style Sheet)
        routeButtonsVBox.setStyle("-fx-padding: 2;" +
                "-fx-border-width: 2;" +
                "-fx-border-color: lightgray;");

        connectionStatus.setStyle("-fx-padding: 2;" +
                "-fx-border-width: 2;" +
                "-fx-border-color: lightgray;");

        //Add items to the main Layout-manager
        vBox.getChildren().addAll(menuBar, connectionStatus, routeButtonsVBox, guiLogic.routePanel(this.routePlanner,3, 3), vBoxBotStatus);
        vBox.setSpacing(5.0);

        //Create the window
        primaryStage.setScene(scene);
        primaryStage.setTitle("iFad");
        primaryStage.setMinWidth(500);
        primaryStage.show();
    }

    /**
     * Opens a specific confirmation pop-up. This is specifically made for the connect and disconnect buttons.
     * @param title String, the title of the window.
     * @param header String, the reason of the pop-up.
     * @param context String, optional information about the pop-up and/or what the buttons will do.
     */
    private void disconnectPopUp(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("iFad - " + title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.connect.setDisable(false);
            this.disconnect.setDisable(true);
            //Send disconnect message
            this.connection.sendInteger(24);
            this.connection.closeConnection();
            this.guiLogic.playSound("DisconnectedJingle.wav");
        }
    }

    /**
     * This method will delete the old objects, re-initialize them and relaunch the application window.
     * @param port String
     */
    public void refreshConnection(String port) {
        this.connection.refreshConnection(port);
        System.out.println("Nieuwe poort: " + this.connection.getPort());
    }
}
