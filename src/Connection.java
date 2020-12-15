import jssc.SerialPort;
import jssc.SerialPortException;

public class Connection {
    private String port;
    private SerialPort serialPort;
    private boolean connected;
    private boolean expectData;

    /**
     * Constructor for Connection class.
     * @param port
     */
    public Connection(String port) {
        this.port = port;
        this.serialPort = new SerialPort(this.port);
        this.connected = false;
        this.expectData = false;
    }

    /**
     * Enums that can be send to the bot for control.
     */
    public enum Commands {
        FORWARD, REVERSE, LEFT, RIGHT, STOP, BUZZ,
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
            System.out.println(e);
        }
    }

    public String receiveCommand() {
        this.expectData = true;
        try {
            while (this.expectData) {
                return this.serialPort.readString();
            }
        } catch (SerialPortException e) {
        }
        return null;
    }

    /**
     * Sends a string to the bot. Specifically used to send single letters, to make it easier to convert on the bot.
     * @param string String
     */
    public void sendCommand(String string) {
        try {
            this.serialPort.writeString(string);
        } catch (SerialPortException e) {
            System.out.println(e);
        }
    }

    /**
     * Opens a connection on port (Class parameter), if a port is opened it will be closed and re-opened.
     */
    public boolean openConnection() {
        try {
            if (!this.serialPort.isOpened()) {
                this.serialPort.openPort();
            } else {
                this.serialPort.closePort();
                this.serialPort.openPort();
            }
            return true;
        } catch (SerialPortException e) {
            System.out.println(e);
            return false;
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
            System.out.println(e);
        }
    }

    /**
     * True if connected.
     * @return boolean if there is a connection.
     */
    public boolean isConnected() {
        if (this.serialPort.isOpened()) {
            this.connected = true;
        } else {
            this.connected = false;
        }
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

    /**
     * Getter for the serialPort parameter. Can be useful for jssc methods outside of this class.
     * @return serialPort object.
     */
    public SerialPort getSerialPort() {
        return this.serialPort;
    }
}
