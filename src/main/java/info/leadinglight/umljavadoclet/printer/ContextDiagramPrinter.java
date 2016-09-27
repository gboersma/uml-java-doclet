package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.ArrayList;
import java.util.List;

public class ContextDiagramPrinter extends PumlDiagramPrinter {
    public ContextDiagramPrinter(Model model, ModelClass contextClass) {
        super(model);
        _contextClass = contextClass;
    }
    
    public void generate() {
        start();
        orthogonalLinesOption();
        noPackagesOption();
        addContextClass(_contextClass);
        for (ModelRel rel: _contextClass.relationships()) {
            if (addRelationshipClass(rel)) {
                relationship(rel);
            }
            newline();
        }
        end();
    }
    
    // Highlight the class with a different colour.
    private void addContextClass(ModelClass modelClass) {
        // TODO Show in different color.
        detailedClass(modelClass, true, true, true, true, false, true);
        _classes.add(modelClass);
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private boolean addRelationshipClass(ModelRel rel) {
        ModelClass otherClass = (rel.source() != _contextClass ? rel.source() : rel.destination());
        if (!otherClass.fullName().startsWith("java.util.")) {
            // Only draw the class on the other side of the relationship if it hasn't been added yet.
            if (!_classes.contains(otherClass)) {
                if (otherClass.modelPackage() == _contextClass.modelPackage()) {
                    classHiddenFieldsAndMethods(otherClass, true);
                } else if (otherClass.isInternal()) {
                    classHiddenFieldsAndMethods(otherClass, true, "white");
                } else {
                    classHiddenFieldsAndMethods(otherClass, true, "lightgrey");
                }
                _classes.add(otherClass);
            }
            return true;
        } else {
            return false;
        }
    }
    
    private final ModelClass _contextClass;
    private final List<ModelClass> _classes = new ArrayList<>();
}
