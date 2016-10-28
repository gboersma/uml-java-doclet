package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;

/**
 * Diagram showing all packages and all classes.
 * No relationships.
 */
public class OverviewDiagramPrinter extends PumlDiagramPrinter {
    public OverviewDiagramPrinter(Model model, DiagramOptions options) {
        super(model, options);
    }
    
    public void generate() {
        start();
        // The layout for packages is really bad.
        // Just show the classes within all of the packages in the model.
        for (ModelPackage modelPackage: getModel().rootPackages()) {
            packageDefinition(modelPackage, packageFilepath(modelPackage), null);
            for (ModelClass modelClass: modelPackage.classes()) {
                String filepath = null;
                if (modelClass.modelPackage() != null) {
                    filepath = classFilepath(modelClass);
                }
                classDefinitionNoDetail(modelClass, false, filepath, null);
            }
        }
        end();
    }
}
