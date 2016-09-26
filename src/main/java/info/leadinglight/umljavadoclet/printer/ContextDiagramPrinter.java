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
        addContextClass(_contextClass);
        for (ModelRel rel: _contextClass.relationships()) {
            addRelationshipClass(rel);
            relationship(rel);
            newline();
        }
        end();
    }
    
    // Highlight the class with a different colour.
    private void addContextClass(ModelClass modelClass) {
        // TODO Show in different color.
        detailedClass(modelClass, true, true, true, false, true);
        _classes.add(modelClass);
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private void addRelationshipClass(ModelRel rel) {
        ModelClass otherClass = (rel.source() != _contextClass ? rel.source() : rel.destination());
        // Only draw the class on the other side of the relationship if it hasn't been added yet.
        if (!_classes.contains(otherClass)) {
            hiddenClass(otherClass);
            _classes.add(otherClass);
        }
    }
    
    private final ModelClass _contextClass;
    private final List<ModelClass> _classes = new ArrayList<>();
}
