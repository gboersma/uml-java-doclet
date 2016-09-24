package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelType;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.ArrayList;
import java.util.List;

public class ContextDiagramGenerator extends DiagramGenerator {
    public ContextDiagramGenerator(Model model, ModelType contextClass) {
        super(model);
        _contextClass = contextClass;
    }
    
    @Override
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
    private void addContextClass(ModelType modelType) {
        // TODO Show in different color.
        classWithFieldsAndMethods(modelType);
        _classes.add(modelType);
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private void addRelationshipClass(ModelRel rel) {
        ModelType otherClass = (rel.getSource() != _contextClass ? rel.getSource() : rel.getDestination());
        // Only draw the class on the other side of the relationship if it hasn't been added yet.
        if (!_classes.contains(otherClass)) {
            hiddenClass(otherClass);
            _classes.add(otherClass);
        }
    }
    
    private final ModelType _contextClass;
    private final List<ModelType> _classes = new ArrayList<>();
}
