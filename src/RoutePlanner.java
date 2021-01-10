public class RoutePlanner {
    private Connection connection;
    int route;
    String tempRoute;

    /**
     * Constructor for the RoutePlanner class.
     * @param connection
     */
    public RoutePlanner(Connection connection) {
        this.connection = connection;
        this.route = 0;
        this.tempRoute = "";
    }

    /**
     * Add the position to the route attribute.
     * @param pos integer
     */
    public void planner(String pos) {
        this.tempRoute = this.tempRoute + pos.substring(0, pos.indexOf(',')) + "" + pos.substring(pos.indexOf(',') + 1);
        this.route = Integer.parseInt(this.tempRoute);
    }

    /**
     * Send the route attribute to the bot.
     */
    public void sendRoute() {
        this.connection.sendString(this.tempRoute);
        this.route = 0;
    }

    /**
     * Clears the route list.
     */
    public void clearRoute() {
        this.route = 0;
        this.tempRoute = "";
    }
}
