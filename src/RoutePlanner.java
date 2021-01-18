/**
 * This class will handle the gathering and sending of route data.
 * @author Lars Hoendervangers, Tom Martens
 */
public class RoutePlanner {
    private Connection connection;
    private String route;

    /**
     * Constructor for the RoutePlanner class.
     * @param connection {@link Connection} object
     */
    public RoutePlanner(Connection connection) {
        this.connection = connection;
        this.route = "";
    }

    /**
     * Add the position to the route attribute.
     * @param pos integer
     */
    public void planner(String pos) {
        this.route = this.route + pos.substring(0, pos.indexOf(',')) + "" + pos.substring(pos.indexOf(',') + 1);
    }

    /**
     * Send the route attribute to the bot.
     */
    public void sendRoute() {
        this.connection.sendString(this.route);
    }

    /**
     * Used for testing only
     */
    public String getRoute() {
        return this.route;
    }

    /**
     * Clears the route list.
     */
    public void clearRoute() {
        this.route = "";
    }
}
