package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.List;

/**
 * Generate PlantUML diagrams from the model.
 */
public abstract class PumlDiagramPrinter extends Printer {
        public PumlDiagramPrinter(Model model, DiagramOptions options) {
        _model = model;
        _options = options;
    }
    
    public Model getModel() {
        return _model;
    }

    public DiagramOptions getOptions() {
        return _options;
    }

    public void start() {
        println("@startuml");
        // Add include file if specified
        if (_options.hasPumlIncludeFile()) {
            println("!include " + _options.getPumlIncludeFile());
        }
        newline();
        // We want links to go into the same frame as the diagram for Javadocs.
        svglinktargetOption("_parent");
        // Line style option
        printLineTypeOption();
    }

    public void leftToRight() {
        println("left to right direction");
    }

    public void noPackagesOption() {
        println("set namespaceSeparator none");
    }
    
    public void rectangularPackagesOption() {
        println("skinparam packageStyle rect");
    }
    
    public void svglinktargetOption(String target) {
        println("skinparam svgLinkTarget " + target);
    }

    public void end() {
        newline();
        println("@enduml");
    }
    
    public void packageDefinition(ModelPackage modelPackage, String filepath, String color) {
        print("package ");
        print(modelPackage.fullName());
        if (filepath != null && filepath.length() > 0) {
            print(" [[");
            print(filepath);
            print("{" + modelPackage.fullName() + "}");
            print("]]");
        }
        if (color != null && color.length() > 0) {
            print(" #" + color);
        }
        println(" {");
        println("}");
        newline();
    }
    
    // Displays the class with all details, and full method signatures (if displayed).
    public void classDefinition(ModelClass modelClass,
            boolean displayPackageName,
            String filepath, 
            String color,
            boolean showFields, 
            boolean showConstructors, 
            boolean showMethods,
            boolean publicMethodsOnly,
            boolean includeTypeInfo) {
        classDeclaration(modelClass, displayPackageName);
        if (filepath != null && filepath.length() > 0) {
            print(" [[");
            print(filepath);
            print("{" + modelClass.fullNameWithoutParameters() + "}");
            print("]]");
        }
        if (color != null && color.length() > 0) {
            print(" #" + color);
        }
        println(" {");
        if (showFields) {
            for (ModelClass.Field field: modelClass.fields()) {
                field(field, includeTypeInfo);
            }
        }
        if (showConstructors) {
            for (ModelClass.Constructor cons: modelClass.constructors()) {
                if (publicMethodsOnly == false || publicMethodsOnly == true && cons.visibility == ModelClass.Visibility.PUBLIC) {
                    constructor(cons, includeTypeInfo);
                }
            }
        }
        if (showMethods) {
            for (ModelClass.Method method: modelClass.methods()) {
                if (publicMethodsOnly == false || publicMethodsOnly == true && method.visibility == ModelClass.Visibility.PUBLIC) {
                    method(method, includeTypeInfo);
                }
            }
        }
        println("}");
        newline();
        if (!showFields) {
            hideFields(modelClass);
        }
        if (!showConstructors && !showMethods) {
            hideMethods(modelClass);
        }
        newline();
    }
    
    public void classDefinitionNoDetail(ModelClass modelClass, boolean displayPackageName, String filepath, String color) {
        classDefinition(modelClass, displayPackageName, filepath, color, false, false, false, false, false);
    }
    
    public void classDeclaration(ModelClass modelClass, boolean displayPackageName) {
        classType(modelClass);
        print(" \"");
        print("<b><size:14>");
        print(modelClass.shortName());
        print("</b>");
        if (displayPackageName) {
            print("\\n<size:10>");
            print(modelClass.packageName());
        }
        print("\" as ");
        className(modelClass);
        print(" ");
        annotations(modelClass);
    }
    
    public void annotations(ModelClass modelClass) {
        List<String> annotations = modelClass.annotations();
        if (annotations.size() > 0) {
            for (String annotation: modelClass.annotations()) {
                print("<<");
                print(annotation);
                print(">>");
            }
        }
    }
    
    public void className(ModelClass modelClass) {
        // Parameterized classes have < and , in the name, which confuses PlantUML.
        // Strip out these characters from the name. 
        // The name will remain unique across the model, and this 
        // does not affect the way the class is displayed.
        print(modelClass.fullNameWithoutParameters());
        for (String param: modelClass.parameters()) {
            // It is also possible for a parameter to be a generic, and have <> embedded in the name.
            // For example: List<Optional<String>>.
            // We'll do the same thing here- just remove the <>.
            // That should still guarantee a unique name in all cases.
            param = param.replace("<", "").replace(">", "");
            print(param);
        }
    }
    
    public void classType(ModelClass modelClass) {
        switch(modelClass.type()) {
            case INTERFACE:
                print("interface");
                break;
            case ENUM:
                print("enum");
                break;
            case ABSTRACT:
                print("abstract");
                break;
            default:
                print("class");
        }
    }

    public void field(ModelClass.Field field, boolean includeTypeInfo) {
        if (field.isStatic) {
            printStatic();
        }
        visibility(field.visibility);
        if (includeTypeInfo) {
            print(field.type + " ");
        }
        print(field.name);
        newline();
    }
    
    public void visibility(ModelClass.Visibility visibility) {
        switch(visibility) {
            case PUBLIC:
                print("+");
                break;
            case PROTECTED:
                print("#");
                break;
            case PACKAGE_PRIVATE:
                print("~");
                break;
            default:
                print("-");
        }
    }
    
    public void constructor(ModelClass.Constructor constructor, boolean includeTypeInfo) {
        visibility(constructor.visibility);
        print(constructor.name);
        print("(");
        if (includeTypeInfo) {
            String sep = "";
            for (ModelClass.MethodParameter param: constructor.parameters) {
                print(sep);
                print(param.type);
                print(" ");
                print(param.name);
                sep = ",";
            }
        }
        print(")");
        newline();
    }
    
    public void method(ModelClass.Method method, boolean includeTypeInfo) {
        if (method.isStatic) {
            printStatic();
        }
        if (method.isAbstract) {
            printAbstract();
        }
        visibility(method.visibility);
        if (includeTypeInfo) {
            print(method.returnType);
            print(" ");
        }
        print(method.name);
        print("(");
        if (includeTypeInfo) {
            String sep = "";
            for (ModelClass.MethodParameter param: method.parameters) {
                print(sep);
                print(param.type);
                print(" ");
                print(param.name);
                sep = ",";
            }
        }
        print(")");
        newline();
    }
    
    public void hideFields(ModelClass modelClass) {
        print("hide ");
        className(modelClass);
        print(" fields");
        newline();
    }

    public void hideMethods(ModelClass modelClass) {
        print("hide ");
        className(modelClass);
        print(" methods");
        newline();
    }
    
    public void relationship(ModelRel rel) {
        if (isRelationshipVisible(rel)) {
            switch (rel.kind()) {
                case GENERALIZATION:
                    generalization(rel.source(), rel.destination());
                    break;
                case DEPENDENCY:
                    dependency(rel.source(), rel.destination());
                    break;
                case REALIZATION:
                    realization(rel.source(), rel.destination());
                    break;
                case DIRECTED_ASSOCIATION:
                    association(rel.source(), rel.destination(), rel.destinationRole(), multiplicityLabel(rel.destinationCardinality()));
                    break;
            }
        }
    }
    
    public boolean isRelationshipVisible(ModelRel rel) {
        // Check to see if the relationship is visible according to diagram options.
        if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PUBLIC) {
            return rel.isVisible(ModelRel.Visibility.PUBLIC);
        } else if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PROTECTED) {
            return rel.isVisible(ModelRel.Visibility.PROTECTED);
        } else if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PACKAGE) {
            return rel.isVisible(ModelRel.Visibility.PACKAGE);
        } else if (_options.getDependenciesVisibility() == DiagramOptions.Visibility.PRIVATE) {
            return rel.isVisible(ModelRel.Visibility.PRIVATE);
        }
        return false;
   }
    
    public void generalization(ModelClass src, ModelClass dest) {
        printRel(src,  "--|>", dest);
    }
    
    public void realization(ModelClass src, ModelClass dest) {
        printRel(src,  "..|>", dest);
    }

    public void dependency(ModelClass src, ModelClass dest) {
        printRel(src,  "..>", dest);
    }
    
    public void packageDependency(ModelPackage src, ModelPackage dest) {
        print(src.fullName());
        print(" ..> ");
        print(dest.fullName());
        newline();
    }

    public void association(ModelClass src, ModelClass dest, String destRole, String destCardinality) {
        printRel(src, null, null, "-->", dest, destRole, destCardinality);
    }
    
    public String multiplicityLabel(ModelRel.Multiplicity mult) {
        if (mult != null) {
            switch(mult) {
                case ONE:
                    return "1";
                case ZERO_OR_ONE:
                    return "0..1";
                case MANY:
                    return "*";
                default:
                    return null;
            }
        } else {
            return null;
        }
    }
    
    private void printAbstract() {
        print("{abstract} ");
    }
    
    private void printStatic() {
        print("{static} ");
    }

    private void printRel(ModelClass src, String relText, ModelClass dest) {
        className(src);
        print (" " + relText + " ");
        className(dest);
        newline();
    }
    
    private void printRel(ModelClass src, String srcRole, String srcCardinality, String relText, ModelClass dest, String destRole, String destCardinality) {
        className(src);
        printRelLabel(srcRole, srcCardinality);
        print (" " + relText + " ");
        printRelLabel(destRole, destCardinality);
        className(dest);
        newline();
    }
    
    private void printRelLabel(String role, String cardinality) {
        if ((role != null && role.length() > 0) || (cardinality != null && cardinality.length() > 0)) {
            print(" \"");
            if (role != null && role.length() > 0) {
                print(role);
            }
            if (cardinality != null && cardinality.length() > 0) {
                print(" " + cardinality);
            }
            print("\" ");
        }
    }
    
    private void printLineTypeOption() {
        if (_options.getLineType() == DiagramOptions.LineType.ORTHO) {
            println("skinparam linetype ortho");
        } else if (_options.getLineType() == DiagramOptions.LineType.POLYLINE) {
            println("skinparam linetype polyline");
        }
        // Otherwise, splines is the default. No skinparam required.
    }
    
    // Filepath
    
    public String classFilepath(ModelClass modelClass, ModelClass classFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(relativePathToRoot(modelClass.packageName()));
        sb.append(pathToPackage(classFile.packageName()));
        sb.append(classFile.shortNameWithoutParameters());
        sb.append(".html");
        return sb.toString();
    }
    
    public String packageFilepath(ModelClass modelClass, ModelPackage packageFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(relativePathToRoot(modelClass.packageName()));
        sb.append(pathToPackage(packageFile.fullName()));
        sb.append("pacakge-summary.html");
        return sb.toString();
    }
    
    public String classFilepath(ModelPackage modelPackage, ModelClass classFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(relativePathToRoot(modelPackage.fullName()));
        sb.append(pathToPackage(classFile.packageName()));
        sb.append(classFile.shortNameWithoutParameters());
        sb.append(".html");
        return sb.toString();
    }
    
    public String packageFilepath(ModelPackage modelPackage, ModelPackage packageFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(relativePathToRoot(modelPackage.fullName()));
        sb.append(pathToPackage(packageFile.fullName()));
        sb.append("package-summary.html");
        return sb.toString();
    }

    public String classFilepath(ModelClass classFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(pathToPackage(classFile.packageName()));
        sb.append(classFile.shortNameWithoutParameters());
        sb.append(".html");
        return sb.toString();
    }
    
    public String packageFilepath(ModelPackage packageFile) {
        StringBuilder sb = new StringBuilder();
        sb.append(pathToPackage(packageFile.fullName()));
        sb.append("package-summary.html");
        return sb.toString();
    }

    public String relativePathToRoot(String packageName) {
        StringBuilder sb = new StringBuilder();
        String[] parts = packageName.split("\\.");
        if (parts.length > 0) {
            for(int i=0; i<parts.length; i++) {
                sb.append("../");
            }
        }
        return sb.toString();
    }
    
    public String pathToPackage(String packageName) {
        StringBuilder sb = new StringBuilder();
        String[] parts = packageName.split("\\.");
        for (int i=0; i<parts.length; i++) {
            sb.append(parts[i]);
            sb.append("/");
        }
        return sb.toString();
    }
    
    private final Model _model;
    private final DiagramOptions _options;
}
