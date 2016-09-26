package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;

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

    // Displays the class with all details, and full method signatures (if displayed).
    public void detailedClass(ModelClass modelClass, 
            boolean showFields, 
            boolean showConstructors, 
            boolean showMethods,
            boolean publicMethodsOnly,
            boolean includeTypeInfo) {
        classType(modelClass);
        className(modelClass);
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
        // TODO PlantUML does not like the full name. But how does it differentiate between parameterized classes?
        println("hide " + modelClass.fullNameWithoutParameters() + " fields");
    }

    public void hideMethods(ModelClass modelClass) {
        // TODO PlantUML does not like the full name. But how does it differentiate between parameterized classes?
        println("hide " + modelClass.fullNameWithoutParameters() + " methods");
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
    
    public void printAbstract() {
        print("{abstract} ");
    }
    
    public void printStatic() {
        print("{static} ");
    }

    public void printRel(ModelClass src, String relText, ModelClass dest) {
        // TODO PlantUML does not like parameterized name. But how does it differentiate?
        println(src.fullNameWithoutParameters()+ " " + relText + " " + dest.fullNameWithoutParameters());
    }
    
    public void printRel(ModelClass src, String srcRole, String srcCardinality, String relText, ModelClass dest, String destRole, String destCardinality) {
        // PUML does not allow labels to be specified for each end.
        // We'll fake it by overloading the multiplicity label.
        String srcLabel = (srcRole != null ? srcRole + " " : "") + (srcCardinality != null ? srcCardinality : "");
        srcLabel = srcLabel.length() > 0 ? "\"" + srcLabel + "\"" + " " : "";
        String destLabel = (destRole != null ? destRole + " " : "") + (destCardinality != null ? destCardinality : "");
        destLabel = destLabel.length() > 0 ? "\"" + destLabel + "\"" + " " : "";
        // TODO PlantUML does not like parameterized name. But how does it differentiate?
        println(src.fullNameWithoutParameters() + " " + srcLabel + relText + " " + destLabel + dest.fullNameWithoutParameters());
    }

    private final Model _model;
}
