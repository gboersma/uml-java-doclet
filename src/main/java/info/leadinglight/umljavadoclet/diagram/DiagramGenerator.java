package info.leadinglight.umljavadoclet.diagram;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import info.leadinglight.umljavadoclet.model.DependencyRel;
import info.leadinglight.umljavadoclet.model.GeneralizationRel;
import info.leadinglight.umljavadoclet.model.InternalClass;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import info.leadinglight.umljavadoclet.printer.Printer;

/**
 * Generate PlantUML diagrams from the model.
 */
public abstract class DiagramGenerator extends Printer {
    public DiagramGenerator(Model model) {
        _model = model;
    }
    
    public Model getModel() {
        return _model;
    }
    
    public void startUML() {
        println("@startuml");
        newline();
    }
    
    public void endUML() {
        newline();
        println("@enduml");
    }
    
    public void emptyClass(ModelClass modelClass) {
        println("class " + modelClass.getQualifiedName() + " {");
        println("}");
    }
    
    public void hiddenClass(ModelClass modelClass) {
        emptyClass(modelClass);
        newline();
        hideFields(modelClass);
        hideMethods(modelClass);
        newline();
    }

    public void classWithFields(InternalClass modelClass) {
        detailedClass(modelClass, true, false);
    }

    public void classWithMethods(InternalClass modelClass) {
        detailedClass(modelClass, false, true);
    }

    public void classWithFieldsAndMethods(InternalClass modelClass) {
        detailedClass(modelClass, true, true);
    }
    
    public void classWithPublicMethods(InternalClass modelClass) {
        
    }

    // Displays the class with all details, and full method signatures (if displayed).
    public void detailedClass(InternalClass modelClass, boolean showFields, boolean showMethods) {
        println("class " + modelClass.getQualifiedName() + " {");
        if (showFields) {
        }
        if (showMethods) {
            for (MethodDoc methodDoc: modelClass.getClassDoc().methods()) {
                method(methodDoc, true);
            }
        }
        println("}");
    }
    
    // Only display public methods, not full signature.
    public void summaryClass(InternalClass modelClass) {
        println("class " + modelClass.getQualifiedName() + " {");
        for (MethodDoc methodDoc: modelClass.getClassDoc().methods()) {
            if (methodDoc.isPublic()) {
                // It is possible for overloaded methods to have the same name.
                // On a summary view, they will appear like the same method multiple times.
                // TODO Only display a single entry for this.
                method(methodDoc, false);
            }
        }
    }
    
    public void method(MethodDoc methodDoc, boolean detailed) {
        if (methodDoc.isStatic()) {
            print("{static} ");
        }
        if (methodDoc.isAbstract()) {
            print("{abstract} ");
        }
        if (detailed) {
            print(methodDoc.returnType().simpleTypeName() + " ");
        }
        print(methodDoc.name());
        print("(");
        if (detailed) {
            Parameter[] params = methodDoc.parameters();
            for (int i=0; i < params.length; i++) {
                Parameter param = params[i];
                print(param.type().simpleTypeName() + " " + param.name());
                if (i != params.length - 1) {
                    print(", ");
                }
            }
        }
        print(")");
        newline();
    }
    
    public void hideFields(ModelClass modelClass) {
        println("hide " + modelClass.getQualifiedName() + " fields");
    }

    public void hideMethods(ModelClass modelClass) {
        println("hide " + modelClass.getQualifiedName() + " methods");
    }
    
    public void relationship(ModelRel rel) {
        if (rel instanceof GeneralizationRel) {
            generalization(rel.getSource(), rel.getDestination());
        } else if (rel instanceof DependencyRel) {
            dependency(rel.getSource(), rel.getDestination());
        }
    }
    
    public void generalization(ModelClass src, ModelClass dest) {
        printRel(src,  "--|>", dest);
    }
    
    public void dependency(ModelClass src, ModelClass dest) {
        printRel(src,  "..>", dest);
    }
    
    public void printRel(ModelClass src, String relText, ModelClass dest) {
        println(src.getQualifiedName() + " " + relText + " " + dest.getQualifiedName());
    }
    
    private final Model _model;
}
