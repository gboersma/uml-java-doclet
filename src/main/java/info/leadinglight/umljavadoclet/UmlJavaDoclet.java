package info.leadinglight.umljavadoclet;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import info.leadinglight.umljavadoclet.printer.ContextDiagramPrinter;
import info.leadinglight.umljavadoclet.printer.PackageDiagramPrinter;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.printer.DiagramOptions;
import info.leadinglight.umljavadoclet.printer.OverviewDiagramPrinter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.version.Version;

public class UmlJavaDoclet extends Standard {
    public static boolean start(RootDoc root) {
        // Generate Javadocs using standard doclet.
        System.out.println("Generating Javadocs...");
        generateJavadoc(root);
        
        // Set the options.
        DiagramOptions options = new DiagramOptions();
        options.set(root.options());
        
        // Extract the Model.
        Model model = new Model(root);
        model.map();
        
        // Generate the diagrams.
        System.out.println("Using PlantUML version " + Version.versionString());
        System.out.println("Generating diagrams...");
        generateContextDiagrams(model, options);
        generatePackageDiagrams(model, options);
        generateOverviewDiagram(model, options);
        
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
    
    public static int optionLength(String option) {
        if (DiagramOptions.isValidOption(option)) {
            return DiagramOptions.getOptionLength(option);
        } else {
            return Standard.optionLength(option);
        }
    }
    
    public static boolean validOptions(String[][] options, DocErrorReporter reporter) {
        // Iterate through all of the options, checking to see if an option is valid.
        List<String[]> standardOptions = new ArrayList<>();
        for (String[] option: options) {
            String name = option[0];
            if (DiagramOptions.isValidOption(name)) {
                String error = DiagramOptions.checkOption(option);
                if (error != null && error.length() > 0) {
                    reporter.printError(error);
                }
            } else {
                standardOptions.add(option);
            }
        }
        return Standard.validOptions(standardOptions.toArray(new String[][]{}), reporter);
    }
    
    private static void generateJavadoc(RootDoc rootDoc) {
        Standard.start(rootDoc);
    }
    
    private static void generateContextDiagrams(Model model, DiagramOptions options) {
        for (ModelClass modelClass: model.classes()) {
            if (modelClass.isInternal()) {
                generateContextDiagram(model, modelClass, options);
            }
        }
    }
    
    private static void generateContextDiagram(Model model, ModelClass modelClass, DiagramOptions options) {
        ContextDiagramPrinter generator = new ContextDiagramPrinter(model, modelClass, options);
        generator.generate();
        File file = createFile(modelClass.packageName(), modelClass.shortNameWithoutParameters(), "puml");
        boolean success = generator.toFile(file);
        if (success && executePlantUML(modelClass.packageName(), modelClass.shortNameWithoutParameters(), generator.stringBuilder())) {
            if (updateHtml(
                    fileForName(modelClass.packageName()), 
                    modelClass.shortNameWithoutParameters(), 
                    Pattern.compile(".*(Class|Interface|Enum) " + modelClass.shortNameWithoutParameters() + ".*"))) {
                System.out.println("Generated diagram for class " + modelClass.fullName());
            } else {
                System.out.println("ERROR: Could not update html page for class " + modelClass.fullName());
            }
        } else {
            System.out.println("ERROR: Could not generate diagram for class " + modelClass.fullName());
        }
    }
    
    private static void generatePackageDiagrams(Model model, DiagramOptions options) {
        for (ModelPackage modelPackage: model.packages()) {
            generatePackageDiagram(model, modelPackage, options);
        }
    }

    private static void generatePackageDiagram(Model model, ModelPackage modelPackage, DiagramOptions options) {
        PackageDiagramPrinter generator = new PackageDiagramPrinter(model, modelPackage, options);
        generator.generate();
        File file = createFile(modelPackage.fullName(), "package-summary", "puml");
        boolean success = generator.toFile(file);
        if (success && executePlantUML(modelPackage.fullName(), "package-summary", generator.stringBuilder())) {
            if (updateHtml(
                    fileForName(modelPackage.fullName()), 
                    "package-summary",
                    Pattern.compile("(</[Hh]2>)|(<h1 title=\"Package\").*"))) {
                System.out.println("Generated diagram for package " + modelPackage.fullName());
            } else {
                System.out.println("ERROR: Could not update html page for package " + modelPackage.fullName());
            }
        } else {
            System.out.println("ERROR: Could not generate diagram for package " + modelPackage.fullName());
        }
    }

    private static void generateOverviewDiagram(Model model, DiagramOptions options) {
        OverviewDiagramPrinter generator = new OverviewDiagramPrinter(model, options);
        generator.generate();
        File file = createFile("", "overview-summary", "puml");
        boolean success = generator.toFile(file);
        if (success && executePlantUML("", "overview-summary", generator.stringBuilder())) {
            if (updateHtml(
                    fileForName(""),
                    "overview-summary",
                    Pattern.compile("<div class=\"contentContainer\">"))) {
                System.out.println("Generated overview diagram");
            } else {
                System.out.println("ERROR: Could not update html page for overview diagram");
            }
        } else {
            System.out.println("ERROR: Could not generate overview diagram");
        }
    }
    
    private static boolean executePlantUML(String name, String baseName, StringBuilder content) {
        File file = createFile(name, baseName, "svg");
        try {
            OutputStream imageOutput = new BufferedOutputStream(new FileOutputStream(file));
            SourceStringReader reader = new SourceStringReader(content.toString());
            // http://plantuml.sourceforge.net/qa/?qa=4969/skinparam-svglinktarget-not-working-for-api
            reader.generateImage(imageOutput, new FileFormatOption(FileFormat.SVG).withSvgLinkTarget("_parent"));
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
    
    private static boolean updateHtml(File directory, String baseName, Pattern insertPointPattern) {
        File htmlFile = new File(directory, baseName + ".html");
        if (!htmlFile.exists()) {
            System.out.println("ERROR: Could not find html file " + htmlFile.getName());
            return false;
        }

        File svgFile = new File(directory, baseName + ".svg");
        if (!svgFile.exists()) {
            System.out.println("ERROR: Could not find svg file " + svgFile.getName());
            return false;
        }

        File updatedHtml = new File(directory, baseName + ".uml");

        boolean matched = false;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(updatedHtml), "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                if (!matched && insertPointPattern.matcher(line).matches()) {
                    matched = true;
                    String tag = String.format(UML_DIV_TAG, baseName);
                    writer.newLine();
                    writer.write(tag);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
            } catch(IOException e) {
                //e.printStackTrace();
                return false;
            }
        }
        
	// if altered, delete old file and rename new one to the old file name
	if (matched) {
	    htmlFile.delete();
	    updatedHtml.renameTo(htmlFile);
            return true;
	} else {
            System.out.println("ERROR: Could not insert diagram into HTML file " + htmlFile.getName());
	    htmlFile.delete();
            return false;
	}
    }
    
    // TODO Not specifying the width and height of diagrams means that they may be bigger than the page.
    // However, if I specify a width and height, the diagrams do not resize if I zoom in and out.
    // Need to investigate how to do this to keep the diagrams consistent and the browser happy.
    private static final String UML_DIV_TAG = 
	"<div align=\"center\">" +
	    "<object type=\"image/svg+xml\" data=\"%1$s.svg\" alt=\"Package class diagram package %1$s\" border=0></object>" +
	"</div>";
}
