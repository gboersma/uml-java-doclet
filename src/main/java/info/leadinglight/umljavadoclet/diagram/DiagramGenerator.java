package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
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
        emptyLine();
    }
    
    public void endUML() {
        emptyLine();
        println("@enduml");
    }
    
    // Display class as a box with the name (within package).
    public void addReferencedClass(ModelClass modelClass) {
        String className = modelClass.getQualifiedName();
        println("class " + className + " {");
        println("}");
        emptyLine();
        println("hide " + className + " fields");
        println("hide " + className + " methods");
        emptyLine();
    }
    
    // Display class with lines for empty attributes / operations.
    public void addClassWithoutDetail(ModelClass modelClass) {
        // TODO
        addReferencedClass(modelClass);
    }
    
    // Display class along with operations and attributes.
    public void addClassWithDetail(ModelClass modelClass) {
        // TODO
        addReferencedClass(modelClass);
    }
    
    private final Model _model;
}
