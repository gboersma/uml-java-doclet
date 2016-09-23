package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.InternalClass;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.ArrayList;
import java.util.List;

public class ContextDiagramGenerator extends DiagramGenerator {
    public ContextDiagramGenerator(Model model, ModelClass contextClass) {
        super(model);
        _contextClass = contextClass;
    }
    
    public void generate() {
        start();
        addContextClass(_contextClass);
        for (ModelRel rel: _contextClass.getRelationships()) {
            addRelationshipClass(rel);
            relationship(rel);
        }
        end();
    }
    
    // Highlight the class with a different colour.
    private void addContextClass(ModelClass modelClass) {
        // TODO Show in different color.
        classWithFieldsAndMethods((InternalClass)modelClass);
        _classes.add(modelClass);
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private void addRelationshipClass(ModelRel rel) {
        ModelClass otherClass = (rel.getSource() != _contextClass ? rel.getSource() : rel.getDestination());
        // Only draw the class on the other side of the relationship if it hasn't been added yet.
        if (!_classes.contains(otherClass)) {
            hiddenClass(otherClass);
            _classes.add(otherClass);
        }
    }
    
    private final ModelClass _contextClass;
    private final List<ModelClass> _classes = new ArrayList<>();
}
