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
        ortholinesOption();
        noPackagesOption();
        for (ModelClass modelClass: _modelPackage.classes()) {
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
        if (otherClass.isInternal()) {
            if (!_classes.contains(otherClass)) {
                if (otherClass.modelPackage() == _modelPackage) {
                    classHiddenFieldsAndMethods(otherClass);
                } else {
                    classHiddenFieldsAndMethods(otherClass, "lightgrey");
                }
            }

            // If this is a relationship to a class in the same package, and this class
            // is the destination, do not draw it. It will get drawn when the other class is drawn.
            // Otherwise, the relationship is drawn twice.
            if (otherClass.modelPackage() == _modelPackage) {
                if (rel.destination() == modelClass) {
                    return false;
                }
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    private final ModelPackage _modelPackage;
    private final List<ModelClass> _classes = new ArrayList<>();
}
