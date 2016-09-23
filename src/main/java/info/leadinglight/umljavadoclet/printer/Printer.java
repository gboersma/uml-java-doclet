package info.leadinglight.umljavadoclet.printer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Printer {
    public abstract void print();
    
    public void printToFile(String filename) {
        print();
        dumpToFile(filename, _sb.toString());
    }
    
    public void print(String str) {
        _sb.append(str);
    }
    
    public void print(int level, String str) {
        indent(level);
        _sb.append(str);
    }

    public void println(String str) {
        _sb.append(str);
        _sb.append("\n");
    }
    
    public void println(int level, String str) {
        indent(level);
        _sb.append(str);
        _sb.append("\n");
    }

    public void indent() {
        indent(1);
    }
    
    public void indent(int level) {
        for (int i=0; i<level; i++) {
            print("  ");
        }
    }
    
    public void newline() {
        _sb.append("\n");
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
    
    private final StringBuilder _sb = new StringBuilder();
}
