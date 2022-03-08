package LineFollow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LineFollowSensorLogger {

    File f;
    PrintWriter pw;

    public LineFollowSensorLogger() throws FileNotFoundException {
        this.f = new File("C:\\Users\\eholl\\OneDrive\\Desktop\\sensor-log\\" + System.currentTimeMillis() + ".csv");
        this.pw = new PrintWriter(f);
    }

    public void logAnalogValues(int left, int center, int right) {
        pw.println(System.currentTimeMillis() + "," + left + "," + center + "," + right);
        System.out.println(System.currentTimeMillis() + "," + left + "," + center + "," + right);
    }

    public void newFile() {
        pw.close();
        this.f = new File("C:\\Users\\eholl\\OneDrive\\Desktop\\sensor-log\\" + System.currentTimeMillis() + ".csv");
        try {
            this.pw = new PrintWriter(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
