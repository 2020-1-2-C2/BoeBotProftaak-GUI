import javafx.scene.layout.GridPane;

public class RoutePlanner {
    private GridPane gridPane;
    private Connection connection;

    public RoutePlanner(GridPane gridPane, Connection connection) {
        this.gridPane = gridPane;
        this.connection = connection;
    }

    public void planner(int pos) {
        switch (pos) {
            //Top left
            case 0:
                System.out.println("1.1");
                break;
            //Top center
            case 1:
                System.out.println("2.1");
                break;
            //Top right
            case 2:
                System.out.println("3.1");
                break;
            //Middle left
            case 3:
                System.out.println("1.2");
                break;
            //Middle center
            case 4:
                System.out.println("2.2");
                break;
            //Middle right
            case 5:
                System.out.println("3.2");
                break;
            //Bottom left
            case 6:
                System.out.println("1.3");
                break;
            //Bottom center
            case 7:
                System.out.println("2.3");
                break;
            //Bottom right
            case 8:
                System.out.println("3.3");
                break;
        }
    }
}
