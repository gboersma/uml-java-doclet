package info.leadinglight.umljavadoclet.printer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Printer {
    public abstract String print();
    
    public void printToFile(String filename) {
        String str = print();
        dumpToFile(filename, str);
    }
    
    public void println(StringBuilder sb, String str) {
        sb.append(str);
        sb.append("\n");
    }

    private void dumpToFile(String filename, String str) {
        try {
            FileOutputStream os = new FileOutputStream(new File(filename));
            os.write (str.getBytes());
            os.close();
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }
}
