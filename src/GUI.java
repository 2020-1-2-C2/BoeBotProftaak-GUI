import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class GUI extends Application {

    private String port;
    private Connection connection;
    private Button connect;
    private Button disconnect;
    private GuiLogic guiLogic;
    private Stage mainWindowStage;
    private GridPane routeGridPane;
    private RoutePlanner routePlanner;

    /**
     * Default constructor for the GUI class.
     * TODO: Port setter
     */
    public GUI() {
        //Port setter does NOT work yet!
        this.port = "COM7";
        this.connection = new Connection(this.port);
        this.connect = new Button();
        this.disconnect = new Button();
        this.guiLogic = new GuiLogic(this.mainWindowStage);
        this.routeGridPane = new GridPane();
        this.routePlanner = new RoutePlanner(this.connection);
    }

    /**
     * Constructor for the GUI class.
     * @param port String
     */
    public GUI(String port) {
        this.port = port;
        this.connection = new Connection(this.port);
        this.connect = new Button();
        this.disconnect = new Button();
        this.guiLogic = new GuiLogic(this.mainWindowStage);
    }

    /**
     * Starts the main window and calls the necessary functions.
     * @param primaryStage Stage object.
     * @throws Exception Exception.
     * TODO: Check for connection while running, auto disconnect. (Low priority)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.connection.closeConnection();
        this.mainWindowStage = primaryStage;

        //Create components
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox,600,600);
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
        Label controlLabel = new Label("Control");

        //Put the label on to the corresponding menu
        help.setGraphic(helpLabel);
        settings.setGraphic(settingsLabel);
        control.setGraphic(controlLabel);

        menuBar.getMenus().addAll(control, settings, help);

        //Actions for the help menu
        helpLabel.setOnMouseClicked(event -> {
            this.guiLogic.help();
        });

        //Actions for the settings menu
        settingsLabel.setOnMouseClicked(event -> {
            this.guiLogic.settings(this.connection, this);
        });

        //Action for the control menu
        controlLabel.setOnMouseClicked(event -> {
            if (this.connection.isConnected()) {
                this.guiLogic.control(this.connection);
            } else {
                this.guiLogic.errorPopUp("Geen verbinding", "Er is geen verbinding met de bot", "");
            }
        });

        //
        // Start/Cancel/Confirm route buttons
        //
        Button startRoute = new Button("Start route");
        Button confirmRoute = new Button("Bevestig route");
        Button cancelRoute = new Button("Verwijder route");

        startRoute.setOnAction(event -> {
            //Start signal
            this.connection.sendCommand(" ");
            //Send route
            this.routePlanner.sendRoute();
            //Stop signal
            this.connection.sendCommand("~");
        });

        //Not used for now
        confirmRoute.setOnAction(event -> {

        });

        cancelRoute.setOnAction(event -> {
            this.routePlanner.clearRoute();
        });

        routeButtons.getChildren().addAll(startRoute, confirmRoute, cancelRoute);
        //This code is commented for testing.
        //routeButtons.setDisable(true);
        //this.routeGridPane.setDisable(true);

        //Spacing around the routeButtons (clockwise, first int is top, second int is right, third int is bottom, fourth int is left)
        routeButtons.setStyle("-fx-padding: 20 10 10 0");

        //Enable buttons when there is an active connection
        if (this.getConnection().isConnected()) {
            routeButtons.setDisable(false);
            this.routeGridPane.setDisable(false);
        }

        //
        //  Route Controls
        //

        //Create Route control buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.routeGridPane.add(new Button((j + 1) + ", " + (i + 1)), i, j );
            }
        }

        //Set the events for each button
        this.routeGridPane.getChildren().get(0).setOnMouseClicked(event -> this.routePlanner.planner(0));
        this.routeGridPane.getChildren().get(1).setOnMouseClicked(event -> this.routePlanner.planner(1));
        this.routeGridPane.getChildren().get(2).setOnMouseClicked(event -> this.routePlanner.planner(2));
        this.routeGridPane.getChildren().get(3).setOnMouseClicked(event -> this.routePlanner.planner(3));
        this.routeGridPane.getChildren().get(4).setOnMouseClicked(event -> this.routePlanner.planner(4));
        this.routeGridPane.getChildren().get(5).setOnMouseClicked(event -> this.routePlanner.planner(5));
        this.routeGridPane.getChildren().get(6).setOnMouseClicked(event -> this.routePlanner.planner(6));
        this.routeGridPane.getChildren().get(7).setOnMouseClicked(event -> this.routePlanner.planner(7));
        this.routeGridPane.getChildren().get(8).setOnMouseClicked(event -> this.routePlanner.planner(8));

        //Settings for components
        this.routeGridPane.setHgap(10);
        this.routeGridPane.setVgap(10);
        //Spacing around the routeGridPane (clockwise, first int is top, second int is right, third int is bottom, fourth int is left)
        this.routeGridPane.setStyle("-fx-padding: 10 10 10 15");

        //
        // Bot status/information
        // TODO: Improve layout, add bot information if needed.

        //
        // Main window
        //

        //connectButton settings
        this.connect.setDefaultButton(true);
        this.connect.setText("Verbinding maken");
        this.disconnect.setText("Verbinding verbreken");
        this.disconnect.setDisable(true);

        //Connect button event
        this.connect.setOnAction(event -> {
            this.connection.openConnection();
            this.connect.setDisable(true);
            this.connect.setDefaultButton(false);
            this.disconnect.setDisable(false);
            this.disconnect.setDefaultButton(true);
            if (this.connection.getPort().equals(null) || this.connection.getPort().equals("")) {
                this.guiLogic.settings(this.connection, this);
                this.connect.setDefaultButton(true);
                this.disconnect.setDefaultButton(false);
                this.guiLogic.errorPopUp("Kan geen verbinding maken", "Er is geen poort ingevoerd!", "Druk op OK om een poort in te voeren");
                this.connect.setDisable(false);
                this.disconnect.setDisable(true);
            }
        });

        //Disconnect button
        this.disconnect.setOnAction(event -> {
            this.connect.setDefaultButton(true);
            this.disconnect.setDefaultButton(false);
            disconnectPopUp("Verbinding verbreken", "Weet je zeker dat je de verbinding wilt verbreken?", "");
        });

        connectionStatus.getChildren().addAll(this.connect, this.disconnect);

        //Add items to the main Layout-manager
        vBox.getChildren().addAll(menuBar, connectionStatus, routeButtons, this.routeGridPane, vBoxBotStatus);

        //Create the window
        primaryStage.setScene(scene);
        primaryStage.setTitle("iFad");
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
            this.connection.closeConnection();
        }
    }

    /**
     * Getter for the connection port.
     * @return Connection object.
     * TODO: Might get removed if proven unnecessary.
     */
    private Connection getConnection() {
        return this.connection;
    }

    /**
     * Standard port setter.
     * TODO: Make it functional
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Standard port getter.
     * TODO: Make it functional
     */
    public String getPort() {
        return port;
    }
}
