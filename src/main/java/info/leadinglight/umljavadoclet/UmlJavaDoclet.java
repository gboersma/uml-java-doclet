package info.leadinglight.umljavadoclet;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.printer.ModelPrinter;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        Model model = new Model(root);
        model.mapToModel();
        
        // Dump results to file.
        ModelPrinter modelPrinter = new ModelPrinter(model);
        modelPrinter.print();
        modelPrinter.toFile("/Users/gerald/tmp/umljavadoclet/model.out");
        
//        // Generate the PUML for the context diagrams.
//        for (ModelClass internalClass: model.getClasses().internal()) {
//            generateContextDiagram(model, internalClass);
//        }
//        
//        // Generate the PUML for the package diagrams.
//        for (ModelPackage modelPackage: mapper.getModel().getPackages().getAll()) {
//            generatePackageDiagram(mapper.getModel(), modelPackage);
//        }
        
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
    
//    private static void generateContextDiagram(Model model, ModelClass modelClass) {
//        ContextDiagramGenerator generator = new ContextDiagramGenerator(model, modelClass);
//        String filename = "/Users/gerald/tmp/umljavadoclet/puml/context_" + modelClass.getQualifiedName().replace(".", "_") + ".puml";
//        generator.generate();
//        generator.toFile(filename);
//    }
//
//    private static void generatePackageDiagram(Model model, ModelPackage modelPackage) {
//        PackageDiagramGenerator generator = new PackageDiagramGenerator(model, modelPackage);
//        String filename = "/Users/gerald/tmp/umljavadoclet/puml/package_" + modelPackage.getName().replace(".", "_") + ".puml";
//        generator.generate();
//        generator.toFile(filename);
//    }
}
