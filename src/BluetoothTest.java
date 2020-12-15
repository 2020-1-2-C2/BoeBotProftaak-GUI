import jssc.SerialPort;
import jssc.SerialPortException;

public class BluetoothTest {
    private String portName = "COM3";

    public static void main(String[] args) {
        BluetoothTest main = new BluetoothTest();
        main.run();
    }

    public void run() {
        SerialPort serialPort = new SerialPort("COM3");
        try {
            if (serialPort.isOpened()) {
                serialPort.closePort();
            }
            System.out.println("opening port");
            serialPort.openPort();
            System.out.println("Port openend");
            System.out.println("Setting parameters");
            serialPort.setParams(115200, 8, 1, 0);
            System.out.println("parameters set");
            serialPort.writeString("test");
            System.out.println("Closing port");
            serialPort.closePort();
            System.out.println("Port closed");
        } catch (SerialPortException e) {
            System.out.println(e);
        }
    }
}
