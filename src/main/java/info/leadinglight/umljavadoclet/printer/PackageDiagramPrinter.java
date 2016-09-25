package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;

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
        for (ModelClass modelClass: _modelPackage.classes()) {
            // Display only public methods, no parameters.
            summaryClass(modelClass);
            // TODO Display all relationships between classes on the model.
        }
        end();
    }
    
    private final ModelPackage _modelPackage;
}
