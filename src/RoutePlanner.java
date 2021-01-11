/**
 * This class will handle the gathering and sending of route data.
 * @author Lars Hoendervangers, Tom Martens
 */
public class RoutePlanner {
    private Connection connection;
    private String tempRoute;

    /**
     * Constructor for the RoutePlanner class.
     * @param connection {@link Connection} object
     */
    public RoutePlanner(Connection connection) {
        this.connection = connection;
        this.tempRoute = "";
    }

    /**
     * Add the position to the route attribute.
     * @param pos integer
     */
    public void planner(String pos) {
        this.tempRoute = this.tempRoute + pos.substring(0, pos.indexOf(',')) + "" + pos.substring(pos.indexOf(',') + 1);
    }

    /**
     * Send the route attribute to the bot.
     */
    public void sendRoute() {
        this.connection.sendString(this.tempRoute);
    }

    /**
     * Used for testing only
     */
    public String getRoute() {
        return this.tempRoute;
    }

    /**
     * Clears the route list.
     */
    public void clearRoute() {
        this.tempRoute = "";
    }
}
