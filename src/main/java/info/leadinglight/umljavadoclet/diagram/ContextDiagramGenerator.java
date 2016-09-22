package info.leadinglight.umljavadoclet.diagram;

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
        addClassWithDetail(_contextClass);
        // Add all of the classes for relationships.
        for (ModelRel rel: _contextClass.getSourceRelationships()) {
            // TODO Distinguish between external / internal classes. 
            addReferencedClass(rel.getDestination());
        }
        for (ModelRel rel: _contextClass.getDestinationRelationships()) {
            // TODO Distinguish between external / internal classes. 
            addReferencedClass(rel.getSource());
        }
        endUML();
    }
    
    private final ModelClass _contextClass;
}
