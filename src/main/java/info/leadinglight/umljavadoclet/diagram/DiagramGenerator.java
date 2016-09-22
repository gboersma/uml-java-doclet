package info.leadinglight.umljavadoclet.diagram;

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
    
    // Highlight the class with a different colour.
    public void addSpecifiedClass(ModelClass modelClass) {
        // TODO
        addReferencedClass(modelClass);
    }
    
    public void addClassRelationships(ModelClass modelClass) {
        addGeneralization(modelClass);
        addGeneralized(modelClass);
        addDependencies(modelClass);
        addDependents(modelClass);
    }
    
    public void addGeneralization(ModelClass modelClass) {
        ModelRel generalization = modelClass.getGeneralization();
        if (generalization != null) {
            addReferencedClass(generalization.getDestination());
            printGeneralization(modelClass, generalization.getDestination());
        }
    }
    
    public void addGeneralized(ModelClass modelClass) {
        for (ModelRel rel: modelClass.getGeneralized()) {
            // TODO Distinguish between external / internal classes. 
            addReferencedClass(rel.getSource());
            printGeneralization(rel.getSource(), modelClass);
        }
    }

    public void addDependencies(ModelClass modelClass) {
        // Add all of the classes for relationships.
        for (ModelRel rel: modelClass.getDependencies()) {
            // TODO Distinguish between external / internal classes. 
            addReferencedClass(rel.getDestination());
            printDependency(modelClass, rel.getDestination());
        }
    }
    
    public void addDependents(ModelClass modelClass) {
        for (ModelRel rel: modelClass.getDependents()) {
            // TODO Distinguish between external / internal classes. 
            addReferencedClass(rel.getSource());
            printDependency(rel.getSource(), modelClass);
        }
    }
    
    public void printGeneralization(ModelClass src, ModelClass dest) {
        printRel(src,  "--|>", dest);
    }
    
    public void printDependency(ModelClass src, ModelClass dest) {
        printRel(src,  "..>", dest);
    }
    
    public void printRel(ModelClass src, String relText, ModelClass dest) {
        println(src.getQualifiedName() + " " + relText + " " + dest.getQualifiedName());
    }

    private final Model _model;
}
