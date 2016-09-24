package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelType;
import info.leadinglight.umljavadoclet.model.ModelPackage;

/**
 * Generate PUML for package diagram.
 */
public class PackageDiagramGenerator extends DiagramGenerator {
    public PackageDiagramGenerator(Model model, ModelPackage modelPackage) {
        super(model);
        _modelPackage = modelPackage;
    }
    
    @Override
    public void generate() {
        start();
        for (ModelType modelType: _modelPackage.getClasses()) {
            // Display only public methods, no parameters.
            summaryClass(modelType);
            // TODO Display all relationships between classes on the model.
        }
        end();
    }
    
    private final ModelPackage _modelPackage;
}
