import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class RoutePlanner {
    private GridPane gridPane;
    private Connection connection;
    private ArrayList<Integer> route;

    public RoutePlanner(GridPane gridPane, Connection connection) {
        this.gridPane = gridPane;
        this.connection = connection;
        this.route = new ArrayList<>();
    }

    /**
     * Add the position to the route list.
     * @param pos integer
     */
    public void planner(int pos) {
        switch (pos) {
            //Top left
            case 0:
                System.out.println("1.1");
                this.route.add(pos);
                break;
            //Top center
            case 1:
                this.route.add(pos);
                System.out.println("2.1");
                break;
            //Top right
            case 2:
                this.route.add(pos);
                System.out.println("3.1");
                break;
            //Middle left
            case 3:
                this.route.add(pos);
                System.out.println("1.2");
                break;
            //Middle center
            case 4:
                this.route.add(pos);
                System.out.println("2.2");
                break;
            //Middle right
            case 5:
                this.route.add(pos);
                System.out.println("3.2");
                break;
            //Bottom left
            case 6:
                this.route.add(pos);
                System.out.println("1.3");
                break;
            //Bottom center
            case 7:
                this.route.add(pos);
                System.out.println("2.3");
                break;
            //Bottom right
            case 8:
                this.route.add(pos);
                System.out.println("3.3");
                break;
        }

        System.out.println(this.route.toString());
    }

    /**
     * Convert the route list to a string which can get 'decoded' on the bot.
     * TODO: Implement algorithm and send the result to the bot.
     */
    public void calculateRoute() {
        String direction = "";
        for (int i = 0; i < this.route.size(); i++) {

        }
        System.out.println(toString());
    }

    /**
     * Clears the route list.
     */
    public void clearRoute() {
        this.route.clear();
    }
}
