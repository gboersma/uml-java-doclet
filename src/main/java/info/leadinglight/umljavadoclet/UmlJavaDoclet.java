package info.leadinglight.umljavadoclet;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import info.leadinglight.umljavadoclet.printer.ContextDiagramPrinter;
import info.leadinglight.umljavadoclet.printer.PackageDiagramPrinter;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.printer.OverviewDiagramPrinter;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        // Generate Javadocs using standard doclet.
        System.out.println("Generating Javadocs...");
        generateJavadoc(root);
        
        // Extract the Model.
        Model model = new Model(root);
        model.map();
        
        // Generate the diagrams.
        System.out.println("Generating diagrams...");
        generateContextDiagrams(root, model);
        generatePackageDiagrams(root, model);
        generateOverviewDiagram(root, model);
        
        // Execute PlantUML.
        executePlantUML(root, model);
        
        // Update the Javadocs with the generated diagrams / maps.
        System.out.println("Updating Javadocs...");
        updateJavadocs(root, model);
        
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
    
    private static void generateJavadoc(RootDoc rootDoc) {
        Standard.start(rootDoc);
    }
    
    private static void generateContextDiagrams(RootDoc rootDoc, Model model) {
        for (ModelClass modelClass: model.classes()) {
            if (modelClass.isInternal()) {
                generateContextDiagram(rootDoc, model, modelClass);
            }
        }
    }
    
    private static void generateContextDiagram(RootDoc rootDoc, Model model, ModelClass modelClass) {
        ContextDiagramPrinter generator = new ContextDiagramPrinter(model, modelClass);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/context_" + modelClass.fullNameWithoutParameters().replace(".", "_") + ".puml";
        generator.generate();
        generator.toFile(filename);
    }
    
    private static void generatePackageDiagrams(RootDoc rootDoc, Model model) {
        for (ModelPackage modelPackage: model.packages()) {
            generatePackageDiagram(rootDoc, model, modelPackage);
        }
    }

    private static void generatePackageDiagram(RootDoc rootDoc, Model model, ModelPackage modelPackage) {
        PackageDiagramPrinter generator = new PackageDiagramPrinter(model, modelPackage);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/package_" + modelPackage.fullName().replace(".", "_") + ".puml";
        generator.generate();
        generator.toFile(filename);
    }

    private static void generateOverviewDiagram(RootDoc rootDoc, Model model) {
        OverviewDiagramPrinter generator = new OverviewDiagramPrinter(model);
        String filename = "/Users/gerald/tmp/umljavadoclet/puml/overview.puml";
        generator.generate();
        generator.toFile(filename);
    }
    
    private static void executePlantUML(RootDoc rootDoc, Model model) {
        // TODO Call PlantUML to generate the diagrams.
    }
    
    private static void updateJavadocs(RootDoc rootDoc, Model model) {
        // TODO Update all of the HTML to add the generated diagrams.
    }
}
