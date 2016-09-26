package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate PUML for package diagram.
 */
public class PackageDiagramPrinter extends PumlDiagramPrinter {
    public PackageDiagramPrinter(Model model, ModelPackage modelPackage) {
        super(model);
        _modelPackage = modelPackage;
    }
    
    public void generate() {
        start();
        for (ModelClass modelClass: _modelPackage.classes()) {
            // Display only public methods, no parameters.
            detailedClass(modelClass, false, false, false, false, false);
            for (ModelRel rel: modelClass.relationships()) {
                if (addRelationshipClass(modelClass, rel)) {
                    relationship(rel);
                }
                newline();
            }
        }
        end();
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private boolean addRelationshipClass(ModelClass modelClass, ModelRel rel) {
        ModelClass otherClass = (rel.source() != modelClass ? rel.source() : rel.destination());
        // Only draw relationships with classes that are internal.
        if (otherClass.isInternal()) {
            // Only draw the class on the other side of the relationship if it hasn't been added yet.
            if (!_classes.contains(otherClass)) {
                // Is the other class in the same package?
                if (otherClass.modelPackage() == _modelPackage) {
                    // Display only public methods, no parameters.
                    detailedClass(otherClass, false, false, false, false, false);
                } else {
                    hiddenClass(otherClass);
                }
                _classes.add(otherClass);
            }
            return true;
        } else {
            return false;
        }
    }
    
    private final ModelPackage _modelPackage;
    private final List<ModelClass> _classes = new ArrayList<>();
}
