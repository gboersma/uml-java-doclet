package info.leadinglight.umljavadoclet;

import info.leadinglight.umljavadoclet.printer.RootDocPrinter;
import com.sun.javadoc.*;
import info.leadinglight.umljavadoclet.model.DocletModelMapper;
import info.leadinglight.umljavadoclet.printer.ModelPrinter;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        DocletModelMapper mapper = new DocletModelMapper();
        mapper.map(root);
        
        // Dump results to file.
        RootDocPrinter rootDocPrinter = new RootDocPrinter(root);
        rootDocPrinter.printToFile("/Users/gerald/tmp/rootdoc.out");
        ModelPrinter modelPrinter = new ModelPrinter(mapper.getModel());
        modelPrinter.printToFile("/Users/gerald/tmp/model.out");

        return true;
    }
}
