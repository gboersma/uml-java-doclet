package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import info.leadinglight.umljavadoclet.model.Multiplicity;

/**
 * Generate PlantUML diagrams from the model.
 */
public abstract class PumlDiagramPrinter extends Printer {
        public PumlDiagramPrinter(Model model) {
        _model = model;
    }
    
    public Model getModel() {
        return _model;
    }
    
    public void start() {
        println("@startuml");
        // Orthogonal lines
        println("skinparam linetype ortho");
        newline();
    }
    
    public void end() {
        newline();
        println("@enduml");
    }
    
    public void emptyClass(ModelClass modelClass) {
        classType(modelClass);
        className(modelClass);
        println( " {");
        println("}");
        newline();
    }
    
    public void className(ModelClass modelClass) {
        print(modelClass.fullName());
    }
    
    public void classType(ModelClass modelClass) {
        switch(modelClass.type()) {
            case INTERFACE:
                print("interface ");
                break;
            case ENUM:
                print("enum ");
                break;
            default:
                print("class ");
        }
    }
    
    public void hiddenClass(ModelClass modelClass) {
        emptyClass(modelClass);
        newline();
        hideFields(modelClass);
        hideMethods(modelClass);
        newline();
    }

    public void classWithFields(ModelClass modelClass) {
        detailedClass(modelClass, true, false);
    }

    public void classWithMethods(ModelClass modelClass) {
        detailedClass(modelClass, false, true);
    }

    public void classWithFieldsAndMethods(ModelClass modelClass) {
        detailedClass(modelClass, true, true);
    }
    
    // Displays the class with all details, and full method signatures (if displayed).
    public void detailedClass(ModelClass modelClass, boolean showFields, boolean showMethods) {
        classType(modelClass);
        className(modelClass);
        println(" {");
        if (showFields) {
            // TODO Show the fields here.
        }
        if (showMethods) {
            // TODO Show the method here.
        }
        println("}");
        newline();
    }
    
    // Only display public methods, not full signature.
    public void summaryClass(ModelClass modelClass) {
        classType(modelClass);
        className(modelClass);
        println(" {");
        // TODO Print methods.
        println("}");
        // Fields are not shown.
        hideFields(modelClass);
    }
    
    public void field(ModelClass.Field field, boolean detailed) {
        if (field.isStatic) {
            printStatic();
        }
        visibility(field.visibility);
        if (detailed) {
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
    
    public void method(ModelClass.Method method, boolean detailed) {
        if (method.isStatic) {
            printStatic();
        }
        if (method.isAbstract) {
            printAbstract();
        }
        visibility(method.visibility);
        if (detailed) {
            print(method.returnType);
            print(" ");
        }
        print(method.name);
        print("(");
        if (detailed) {
            String sep = "";
            for (String param: method.parameters) {
                print(sep);
                print(param);
                sep = ",";
            }
        }
        print(")");
        newline();
    }
    
    public void hideFields(ModelClass modelClass) {
        println("hide " + modelClass.qualifiedName() + " fields");
    }

    public void hideMethods(ModelClass modelClass) {
        println("hide " + modelClass.qualifiedName() + " methods");
    }
    
    public void relationship(ModelRel rel) {
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
    
    public void generalization(ModelClass src, ModelClass dest) {
        printRel(src,  "--|>", dest);
    }
    
    public void realization(ModelClass src, ModelClass dest) {
        printRel(src,  "..|>", dest);
    }

    public void dependency(ModelClass src, ModelClass dest) {
        printRel(src,  "..>", dest);
    }
    
    public void association(ModelClass src, ModelClass dest, String destRole, String destCardinality) {
        printRel(src, null, null, "-->", dest, destRole, destCardinality);
    }
    
    public String multiplicityLabel(Multiplicity mult) {
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
    
    public void printAbstract() {
        print("{abstract} ");
    }
    
    public void printStatic() {
        print("{static} ");
    }

    public void printRel(ModelClass src, String relText, ModelClass dest) {
        println(src.qualifiedName() + " " + relText + " " + dest.qualifiedName());
    }
    
    public void printRel(ModelClass src, String srcRole, String srcCardinality, String relText, ModelClass dest, String destRole, String destCardinality) {
        // PUML does not allow labels to be specified for each end.
        // We'll fake it by overloading the multiplicity label.
        String srcLabel = (srcRole != null ? srcRole + " " : "") + (srcCardinality != null ? srcCardinality : "");
        srcLabel = srcLabel.length() > 0 ? "\"" + srcLabel + "\"" + " " : "";
        String destLabel = (destRole != null ? destRole + " " : "") + (destCardinality != null ? destCardinality : "");
        destLabel = destLabel.length() > 0 ? "\"" + destLabel + "\"" + " " : "";
        println(src.qualifiedName() + " " + srcLabel + relText + " " + destLabel + dest.qualifiedName());
    }

    private final Model _model;
}
