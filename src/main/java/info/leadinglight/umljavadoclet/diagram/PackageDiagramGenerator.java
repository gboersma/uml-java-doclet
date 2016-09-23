package info.leadinglight.umljavadoclet.diagram;

import info.leadinglight.umljavadoclet.model.InternalClass;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;

/**
 * Generate PUML for package diagram.
 */
public class PackageDiagramGenerator extends DiagramGenerator {
    public PackageDiagramGenerator(Model model, ModelPackage modelPackage) {
        super(model);
        _modelPackage = modelPackage;
    }
    
    public void generate() {
        start();
        for (ModelClass modelClass: _modelPackage.getClasses()) {
            // Display only public methods, no parameters.
            summaryClass((InternalClass)modelClass);
            // TODO Display all relationships between classes on the model.
        }
        end();
    }
    
    private final ModelPackage _modelPackage;
}
