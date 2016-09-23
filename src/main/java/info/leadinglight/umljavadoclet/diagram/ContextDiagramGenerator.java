package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.InternalClass;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelRel;

public class ContextDiagramGenerator extends DiagramGenerator {
    public ContextDiagramGenerator(Model model, ModelClass contextClass) {
        super(model);
        _contextClass = contextClass;
    }
    
    @Override
    public void print() {
        startUML();
        addContextClass(_contextClass);
        for (ModelRel rel: _contextClass.getRelationships()) {
            addRelationshipClass(rel);
            relationship(rel);
        }
        endUML();
    }
    
    // Highlight the class with a different colour.
    private void addContextClass(ModelClass modelClass) {
        // TODO Show in different color.
        classWithFieldsAndMethods((InternalClass)modelClass);
    }
    
    // Put the class on the other side of the relationship on the diagram.
    private void addRelationshipClass(ModelRel rel) {
        ModelClass otherClass = (rel.getSource() != _contextClass ? rel.getSource() : rel.getDestination());
        if (otherClass != _contextClass) {
            if (otherClass instanceof InternalClass) {
                emptyClass(otherClass);
            } else {
                hiddenClass(otherClass);
            }
        }
    }
    
    private final ModelClass _contextClass;
}
