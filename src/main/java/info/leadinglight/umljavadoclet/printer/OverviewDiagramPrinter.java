package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;

/**
 * Diagram showing all packages and all classes.
 * No relationships.
 */
public class OverviewDiagramPrinter extends PumlDiagramPrinter {
    public OverviewDiagramPrinter(Model model) {
        super(model);
    }
    
    public void generate() {
        start();
        // The layout for packages is really bad.
        // Just show the classes within all of the packages in the model.
        orthogonalLinesOption();
        for (ModelPackage modelPackage: getModel().packages()) {
            emptyPackage(modelPackage);
            for (ModelClass modelClass: modelPackage.classes()) {
                emptyClass(modelClass);
            }
        }
        end();
    }
}
