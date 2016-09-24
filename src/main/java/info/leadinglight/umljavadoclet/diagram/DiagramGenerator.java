package info.leadinglight.umljavadoclet.diagram;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Type;
import info.leadinglight.umljavadoclet.model.AssociationEndpoint;
import info.leadinglight.umljavadoclet.model.AssociationRel;
import info.leadinglight.umljavadoclet.model.DependencyRel;
import info.leadinglight.umljavadoclet.model.GeneralizationRel;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelType;
import info.leadinglight.umljavadoclet.model.ModelRel;
import info.leadinglight.umljavadoclet.model.Multiplicity;
import info.leadinglight.umljavadoclet.model.RealizationRel;
import info.leadinglight.umljavadoclet.printer.Printer;

/**
 * Generate PlantUML diagrams from the model.
 */
public abstract class DiagramGenerator extends Printer {
    public DiagramGenerator(Model model) {
        _model = model;
    }
    
    public abstract void generate();
    
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
    
    public void emptyClass(ModelType modelType) {
        classType(modelType);
        className(modelType);
        println(" {");
        println("}");
    }
    
    public void classType(ModelType modelType) {
        ClassDoc classDoc = modelType.getClassDoc();
        if (classDoc.isInterface()) {
            print("interface ");
        } else if (classDoc.isEnum()) {
            print("enum ");
        } else {
            print("class ");
        }
    }
    
    public void className(ModelType modelType) {
        print(modelType.getQualifiedName());
        ParameterizedType paramType = modelType.getParameterizedType();
        if (paramType != null) {
            print("<");
            String sep = "";
            for (Type param : paramType.typeArguments()) {
                print(sep);
                print(param.simpleTypeName());
                sep = ", ";
            }
            print(">");
        }
    }
    
    public void hiddenClass(ModelType modelType) {
        emptyClass(modelType);
        newline();
        hideFields(modelType);
        hideMethods(modelType);
        newline();
    }

    public void classWithFields(ModelType modelType) {
        detailedClass(modelType, true, false);
    }

    public void classWithMethods(ModelType modelType) {
        detailedClass(modelType, false, true);
    }

    public void classWithFieldsAndMethods(ModelType modelType) {
        detailedClass(modelType, true, true);
    }
    
    // Displays the class with all details, and full method signatures (if displayed).
    public void detailedClass(ModelType modelType, boolean showFields, boolean showMethods) {
        classType(modelType);
        className(modelType);
        println(" {");
        if (showFields) {
            for (FieldDoc fieldDoc: modelType.getClassDoc().fields()) {
                field(fieldDoc, true);
            }
        }
        if (showMethods) {
            for (MethodDoc methodDoc: modelType.getClassDoc().methods()) {
                method(methodDoc, true);
            }
        }
        println("}");
    }
    
    // Only display public methods, not full signature.
    public void summaryClass(ModelType modelType) {
        classType(modelType);
        className(modelType);
        println(" {");
        for (MethodDoc methodDoc: modelType.getClassDoc().methods()) {
            if (methodDoc.isPublic()) {
                // It is possible for overloaded methods to have the same name.
                // On a summary view, they will appear like the same method multiple times.
                // TODO Only display a single entry in this case.
                method(methodDoc, false);
            }
        }
        println("}");
        // Fields are not shown.
        hideFields(modelType);
    }
    
    public void field(FieldDoc fieldDoc, boolean detailed) {
        if (fieldDoc.isStatic()) {
            printStatic();
        }
        visibility(fieldDoc);
        if (detailed) {
            print(fieldDoc.type().simpleTypeName() + " ");
        }
        print(fieldDoc.name());
        newline();
    }
    
    public void visibility(ProgramElementDoc doc) {
        if (doc.isPublic()) {
            print("+");
        } else if (doc.isProtected()) {
            print("#");
        } else if (doc.isPackagePrivate()) {
            print("~");
        } else {
            print("-");
        }
    }
    
    public void method(MethodDoc methodDoc, boolean detailed) {
        if (methodDoc.isStatic()) {
            printStatic();
        }
        if (methodDoc.isAbstract()) {
            printAbstract();
        }
        visibility(methodDoc);
        if (detailed) {
            typeName(methodDoc.returnType());
            print(" ");
        }
        print(methodDoc.name());
        print("(");
        if (detailed) {
            Parameter[] params = methodDoc.parameters();
            for (int i=0; i < params.length; i++) {
                Parameter param = params[i];
                typeName(param.type());
                print(" ");
                print(param.name());
                if (i != params.length - 1) {
                    print(", ");
                }
            }
        }
        print(")");
        newline();
    }
    
    public void typeName(Type type) {
        print(type.simpleTypeName());
        ParameterizedType paramType = type.asParameterizedType();
        if (paramType != null) {
            Type[] generics = paramType.typeArguments();
            print("<");
            for (int i=0; i < generics.length; i++) {
                Type arg = generics[i];
                typeName(arg);
                if (i < generics.length - 1) {
                    print(", ");
                }
            }
            print(">");
        }
    }
    
    public void hideFields(ModelType modelType) {
        println("hide " + modelType.getQualifiedName() + " fields");
    }

    public void hideMethods(ModelType modelType) {
        println("hide " + modelType.getQualifiedName() + " methods");
    }
    
    public void relationship(ModelRel rel) {
        if (rel instanceof GeneralizationRel) {
            generalization(rel.getSource(), rel.getDestination());
        } else if (rel instanceof DependencyRel) {
            dependency(rel.getSource(), rel.getDestination());
        } else if (rel instanceof RealizationRel) {
            realization(rel.getSource(), rel.getDestination());
        } else if (rel instanceof AssociationRel) {
            AssociationRel association = (AssociationRel) rel;
            AssociationEndpoint destEndpoint = association.getDestinationEndpoint();
            AssociationEndpoint srcEndpoint = association.getSourceEndpoint();
            association(rel.getSource(), srcEndpoint, rel.getDestination(), destEndpoint);
        }
    }
    
    public void generalization(ModelType src, ModelType dest) {
        printRel(src,  "--|>", dest);
    }
    
    public void realization(ModelType src, ModelType dest) {
        printRel(src,  "..|>", dest);
    }

    public void dependency(ModelType src, ModelType dest) {
        printRel(src,  "..>", dest);
    }
    
    public void association(ModelType src, AssociationEndpoint srcEndpoint, ModelType dest, AssociationEndpoint destEndpoint) {
        String relText = null;
        if (srcEndpoint == null) {
            relText = "-->";
        } else if (destEndpoint == null) {
            relText = "<--";
        } else {
            relText = "--";
        }
        printRel(src, 
                (srcEndpoint != null ? srcEndpoint.getRole() : null), 
                (srcEndpoint != null ? multiplicityLabel(srcEndpoint.getMultiplicity()) : null),
                relText,
                dest,
                (destEndpoint != null ? destEndpoint.getRole() : null), 
                (destEndpoint != null ? multiplicityLabel(destEndpoint.getMultiplicity()) : null));
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

    public void printRel(ModelType src, String relText, ModelType dest) {
        println(src.getQualifiedName() + " " + relText + " " + dest.getQualifiedName());
    }
    
    public void printRel(ModelType src, String srcRole, String srcCardinality, String relText, ModelType dest, String destRole, String destCardinality) {
        // PUML does not allow labels to be specified for each end.
        // We'll fake it by overloading the multiplicity label.
        String srcLabel = (srcRole != null ? srcRole + " " : "") + (srcCardinality != null ? srcCardinality : "");
        srcLabel = srcLabel.length() > 0 ? "\"" + srcLabel + "\"" + " " : "";
        String destLabel = (destRole != null ? destRole + " " : "") + (destCardinality != null ? destCardinality : "");
        destLabel = destLabel.length() > 0 ? "\"" + destLabel + "\"" + " " : "";
        println(src.getQualifiedName() + " " + srcLabel + relText + " " + destLabel + dest.getQualifiedName());
    }

    private final Model _model;
}
