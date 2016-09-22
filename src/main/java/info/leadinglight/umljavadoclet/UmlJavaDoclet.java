package info.leadinglight.umljavadoclet;

import info.leadinglight.umljavadoclet.printer.RootDocPrinter;
import com.sun.javadoc.*;
import info.leadinglight.umljavadoclet.diagram.ContextDiagramGenerator;
import info.leadinglight.umljavadoclet.model.DocletModelMapper;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.printer.ModelPrinter;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        DocletModelMapper mapper = new DocletModelMapper();
        mapper.map(root);
        
        // Dump results to file.
        RootDocPrinter rootDocPrinter = new RootDocPrinter(root);
        rootDocPrinter.printToFile("/Users/gerald/tmp/umljavadoclet/rootdoc.out");
        ModelPrinter modelPrinter = new ModelPrinter(mapper.getModel());
        modelPrinter.printToFile("/Users/gerald/tmp/umljavadoclet/model.out");
        
        // Generate the PUML for the context diagrams.
        for (ClassDoc classDoc: root.classes()) {
            generateContextDiagram(mapper.getModel(), classDoc.qualifiedTypeName());
        }
        
        return true;
    }
    
    private static void generateContextDiagram(Model model, String qualifiedName) {
        ModelClass modelClass = model.getClass(qualifiedName);
        ContextDiagramGenerator generator = new ContextDiagramGenerator(model, modelClass);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/context_" + qualifiedName.replace(".", "_") + ".puml";
        generator.printToFile(filename);
    }
}
