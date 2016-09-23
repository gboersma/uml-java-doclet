package info.leadinglight.umljavadoclet;

import info.leadinglight.umljavadoclet.printer.RootDocPrinter;
import com.sun.javadoc.*;
import info.leadinglight.umljavadoclet.diagram.ContextDiagramGenerator;
import info.leadinglight.umljavadoclet.diagram.PackageDiagramGenerator;
import info.leadinglight.umljavadoclet.mapper.DocletModelMapper;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
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
        
        // Generate the PUML for the package diagrams.
        for (ModelPackage modelPackage: mapper.getModel().getPackages().getAll()) {
            generatePackageDiagram(mapper.getModel(), modelPackage);
            
        }
        
        return true;
    }
    
    private static void generateContextDiagram(Model model, String qualifiedName) {
        ModelClass modelClass = model.getClass(qualifiedName);
        ContextDiagramGenerator generator = new ContextDiagramGenerator(model, modelClass);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/context_" + qualifiedName.replace(".", "_") + ".puml";
        generator.printToFile(filename);
    }

    private static void generatePackageDiagram(Model model, ModelPackage modelPackage) {
        PackageDiagramGenerator generator = new PackageDiagramGenerator(model, modelPackage);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/package_" + modelPackage.getName().replace(".", "_") + ".puml";
        generator.printToFile(filename);
    }
}
