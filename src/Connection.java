import jssc.SerialPort;
import jssc.SerialPortException;

public class Connection {
    private String port;
    private SerialPort serialPort;
    private boolean expectData;

    /**
     * Constructor for the Connection class.
     * @param port String
     */
    public Connection(String port) {
        this.port = port;
        this.serialPort = new SerialPort(this.port);
        this.expectData = false;
    }

    /**
     * Send an integer that will be received as an integer. Example: sendInteger(16)
     * result on the bot: 16
     * @param integer int
     */
    public void sendInteger(int integer) {
        try {
            this.serialPort.writeInt(integer);
        } catch (SerialPortException e) {
            System.out.println("JSSC error in methode sendInteger()\n" + e);
        }
    }

    /**
     * Sends a string to the bot. Specifically used to send single letters, to make it easier to convert on the bot.
     * @param string String
     */
    public void sendString(String string) {
        try {
            System.out.println("Send: " + string);
            this.serialPort.writeString(string);
        } catch (SerialPortException e) {
            System.out.println("JSSC error in methode sendString()\n" + e);
        }
    }

    /**
     * Opens a connection on port (Class parameter), if a port is opened it will be closed and re-opened.
     */
    public void openConnection() {
        try {
            if (!this.serialPort.isOpened()) {
                this.serialPort.openPort();
            } else {
                this.serialPort.closePort();
                this.serialPort.openPort();
            }
        } catch (SerialPortException e) {
            System.out.println("JSSC error in methode openConnection()\n" + e);
        }
    }

    /**
     * Only closes the connection if there was one to begin with.
     */
    public void closeConnection() {
        try {
            if (this.serialPort.isOpened()) {
                this.serialPort.closePort();
            }
        } catch (SerialPortException e) {
            System.out.println("JSSC error in methode closeConnection()\n" + e);
        }
    }

    /**
     * True if connected.
     * @return boolean if there is a connection.
     */
    public boolean isConnected() {
        return this.serialPort.isOpened();
    }

    /**
     * Setter for the port parameter.
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Getter for the port parameter.
     * @return port as String.
     */
    public String getPort() {
        return this.port;
    }
}
