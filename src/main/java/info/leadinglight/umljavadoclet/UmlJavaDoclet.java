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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.version.Version;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        // Generate Javadocs using standard doclet.
        System.out.println("Generating Javadocs...");
        generateJavadoc(root);
        
        // Extract the Model.
        Model model = new Model(root);
        model.map();
        
        // Generate the diagrams.
        System.out.println("Using PlantUML version " + Version.versionString());
        System.out.println("Generating diagrams...");
        generateContextDiagrams(root, model);
        generatePackageDiagrams(root, model);
        generateOverviewDiagram(root, model);
        
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
        generator.generate();
        File file = createFile(modelClass.packageName(), modelClass.shortNameWithoutParameters(), "puml");
        boolean success = generator.toFile(file);
        if (success && executePlantUML(modelClass.packageName(), modelClass.shortNameWithoutParameters(), generator.stringBuilder())) {
            System.out.println("Generated diagram for class " + modelClass.fullName());
        } else {
            System.out.println("ERROR: Could not generate diagram for class " + modelClass.fullName());
        }
    }
    
    private static void generatePackageDiagrams(RootDoc rootDoc, Model model) {
        for (ModelPackage modelPackage: model.packages()) {
            generatePackageDiagram(rootDoc, model, modelPackage);
        }
    }

    private static void generatePackageDiagram(RootDoc rootDoc, Model model, ModelPackage modelPackage) {
        PackageDiagramPrinter generator = new PackageDiagramPrinter(model, modelPackage);
        generator.generate();
        File file = createFile(modelPackage.fullName(), "package", "puml");
        boolean success = generator.toFile(file);
        if (success && executePlantUML(modelPackage.fullName(), "package", generator.stringBuilder())) {
            System.out.println("Generated diagram for package " + modelPackage.fullName());
        } else {
            System.out.println("ERROR: Could not generate diagram for package " + modelPackage.fullName());
        }
    }

    private static void generateOverviewDiagram(RootDoc rootDoc, Model model) {
        OverviewDiagramPrinter generator = new OverviewDiagramPrinter(model);
        generator.generate();
        File file = createFile("", "overview", "puml");
        boolean success = generator.toFile(file);
        if (success && executePlantUML("", "overview", generator.stringBuilder())) {
            System.out.println("Generated overview diagram");
        } else {
            System.out.println("ERROR: Could not generate overview diagram");
        }
    }
    
    private static void updateJavadocs(RootDoc rootDoc, Model model) {
        // TODO Update all of the HTML to add the generated diagrams.
    }
    
    private static boolean executePlantUML(String name, String baseName, StringBuilder content) {
        File file = createFile(name, baseName, "svg");
        try {
            OutputStream imageOutput = new BufferedOutputStream(new FileOutputStream(file));
            SourceStringReader reader = new SourceStringReader(content.toString());
            reader.generateImage(imageOutput, new FileFormatOption(FileFormat.SVG));
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }
    }
    
    private static File createFile(String name, String baseName, String extension) {
        try {
            File dir = fileForName(name);
            if (dir.exists() || dir.mkdirs()) {
                File file = new File(dir, baseName + "." + extension);
                if (file.exists() || file.createNewFile()) {
                    return file;
                }
            }
            return null;
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
    }
    
    private static File fileForName(String name) {
        File file = new File(".");
        for (String part : name.split("\\.")) {
            if (part.trim().length() > 0) {
                file = new File(file, part);
            }
        }
        return file;
    }
}
