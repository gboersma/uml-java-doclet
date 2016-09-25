package info.leadinglight.umljavadoclet;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import info.leadinglight.umljavadoclet.printer.ContextDiagramPrinter;
import info.leadinglight.umljavadoclet.printer.PackageDiagramPrinter;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.printer.ModelPrinter;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        Model model = new Model(root);
        model.map();
        
        // Dump results to file.
        ModelPrinter modelPrinter = new ModelPrinter(model);
        modelPrinter.generate();
        modelPrinter.toFile("/Users/gerald/tmp/umljavadoclet/model.out");
        
        // Generate the PUML for the context diagrams.
        for (ModelClass modelClass: model.classes()) {
            if (modelClass.isInternal()) {
                generateContextDiagram(model, modelClass);
            }
        }
        
        // Generate the PUML for the package diagrams.
        for (ModelPackage modelPackage: model.packages()) {
            generatePackageDiagram(model, modelPackage);
        }
        
        return true;
    }

    /**
     * Specify the language version.
     * This is EXTREMELY important. It it is not set, none of the generic parameters
     * are properly returned. Grrrr. Thanks Java.
     * @return Version of the language.
     */
    public static LanguageVersion languageVersion() {
        return Standard.languageVersion();
    }
    
    private static void generateContextDiagram(Model model, ModelClass modelClass) {
        ContextDiagramPrinter generator = new ContextDiagramPrinter(model, modelClass);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/context_" + modelClass.qualifiedName().replace(".", "_") + ".puml";
        generator.generate();
        generator.toFile(filename);
    }

    private static void generatePackageDiagram(Model model, ModelPackage modelPackage) {
        PackageDiagramPrinter generator = new PackageDiagramPrinter(model, modelPackage);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/package_" + modelPackage.fullName().replace(".", "_") + ".puml";
        generator.generate();
        generator.toFile(filename);
    }
}
