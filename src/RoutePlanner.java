public class RoutePlanner {
    private Connection connection;
    int route;

    /**
     * Constructor for the RoutePlanner class.
     * @param connection
     */
    public RoutePlanner(Connection connection) {
        this.connection = connection;
        this.route = 0;
    }

    /**
     * Add the position to the route attribute.
     * @param pos integer
     */
    public void planner(String pos) {
        pos = pos.substring(0, pos.indexOf(',')) + "" + pos.substring(pos.indexOf(',') + 1);
        System.out.println(pos);
        this.route = Integer.parseInt(pos);
    }

    /**
     * Send the route attribute to the bot.
     * TODO: Determine start position.
     */
    public void sendRoute() {
        this.connection.sendString("" + this.route);
        this.route = 0;
    }

    /**
     * Clears the route list.
     */
    public void clearRoute() {
        this.route = 0;
    }
}
