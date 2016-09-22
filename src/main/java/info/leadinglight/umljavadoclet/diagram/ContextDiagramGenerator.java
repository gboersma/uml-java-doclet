package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;

public class ContextDiagramGenerator extends DiagramGenerator {
    public ContextDiagramGenerator(Model model, ModelClass contextClass) {
        super(model);
        _contextClass = contextClass;
    }
    
    @Override
    public void print() {
        startUML();
        addClassWithDetail(_contextClass);
        addClassRelationships(_contextClass);
        endUML();
    }
    
    private final ModelClass _contextClass;
}
