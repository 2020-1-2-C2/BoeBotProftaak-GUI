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
    public void planner(int pos) {
        switch (pos) {
            //Top left
            case 0:
                this.route = 11;
                break;
            //Top center
            case 1:
                this.route = 21;
                break;
            //Top right
            case 2:
                this.route = 31;
                break;
            //Middle left
            case 3:
                this.route = 12;
                break;
            //Middle center
            case 4:
                this.route = 22;
                break;
            //Middle right
            case 5:
                this.route = 32;
                break;
            //Bottom left
            case 6:
                this.route = 13;
                break;
            //Bottom center
            case 7:
                this.route = 23;
                break;
            //Bottom right
            case 8:
                this.route = 33;
                break;
        }
        System.out.println(route);
    }

    /**
     * Send the route attribute to the bot.
     * TODO: Determine start position.
     */
    public void sendRoute() {
        this.connection.sendCommand("" + this.route);
        this.route = 0;
    }

    /**
     * Clears the route list.
     */
    public void clearRoute() {
        this.route = 0;
    }
}
