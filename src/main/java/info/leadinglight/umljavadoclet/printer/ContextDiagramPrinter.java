package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.ArrayList;
import java.util.List;

public class ContextDiagramPrinter extends PumlDiagramPrinter {
    public ContextDiagramPrinter(Model model, ModelClass contextClass, DiagramOptions options) {
        super(model, options);
        _contextClass = contextClass;
    }
    
    public void generate() {
        start();
        noPackagesOption();
        addContextClass(_contextClass);
        for (ModelRel rel: _contextClass.relationships()) {
            addRelationship(rel);
            newline();
        }
        end();
    }
    
    // Highlight the class with a different colour.
    private void addContextClass(ModelClass modelClass) {
        // TODO Show in different color.
        String filepath = classFilepath(modelClass, modelClass);
        classDefinition(modelClass, true, filepath, null, true, true, true, false, true);
        _classes.add(modelClass);
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private void addRelationship(ModelRel rel) {
        ModelClass otherClass = (rel.source() != _contextClass ? rel.source() : rel.destination());
        boolean isJavaUtilClass = otherClass.fullName().startsWith("java.util.");
        // Don't show relationships with java.util (Collection) classes, unless it is as a superclass.
        if (!isJavaUtilClass ||
            (isJavaUtilClass && (rel.kind() == ModelRel.Kind.GENERALIZATION || rel.kind() == ModelRel.Kind.REALIZATION))) {
            // Check to see if the class in the relationship is excluded from the diagram, either
            // by the name of the class or the name of the package.
            if (getOptions().isExcludedPackage(otherClass) || getOptions().isExcludedClass(otherClass)) {
                return;
            }

            if (isRelationshipVisible(rel)) {
                // Only draw the class on the other side of the relationship if it hasn't been added yet.
                if (!_classes.contains(otherClass)) {
                    String filepath = null;
                    if (otherClass.modelPackage() != null) {
                        filepath = classFilepath(_contextClass, otherClass);
                    }
                    if (otherClass.modelPackage() == _contextClass.modelPackage()) {
                        classDefinitionNoDetail(otherClass, true, filepath, null);
                    } else if (otherClass.isInternal()) {
                        classDefinitionNoDetail(otherClass, true, filepath, "white");
                    } else {
                        classDefinitionNoDetail(otherClass, true, filepath, "lightgrey");
                    }
                    _classes.add(otherClass);
                }
                
                // Draw the relationship with the other class.
                relationship(rel);
            }
        }
    }
    
    private final ModelClass _contextClass;
    private final List<ModelClass> _classes = new ArrayList<>();
}
