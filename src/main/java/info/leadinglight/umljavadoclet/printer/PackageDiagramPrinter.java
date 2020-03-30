package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.model.ModelRel;

/**
 * Generate PUML for package diagram.
 */
public class PackageDiagramPrinter extends PumlDiagramPrinter {
    public PackageDiagramPrinter(Model model, ModelPackage modelPackage, DiagramOptions options) {
        super(model, options);
        _modelPackage = modelPackage;
    }
    
    public void generate() {
        start();

        // Option for displaying the packages diagram left to right.
        if (getOptions().getPackageOrientation() == DiagramOptions.Orientation.LEFT_TO_RIGHT) {
            leftToRight();
        }

        // Layout of packages is really, really bad.
        // Would love to show relationships between packages, but it is just awful.
        //noPackagesOption();
        addPackage(_modelPackage, "lightyellow");
        addRelationships(_modelPackage);
        // Get all of the sub-packages of the model package and draw them as well.
        for (ModelPackage subPackage: getModel().childPackages(_modelPackage)) {
            addPackage(subPackage, null);
        }
        end();
    }
    
    public void addPackage(ModelPackage modelPackage, String color) {
        String filepath = packageFilepath(_modelPackage, modelPackage);
        packageDefinition(modelPackage, filepath, color);
        for (ModelClass modelClass: modelPackage.classes()) {
            filepath = classFilepath(_modelPackage, modelClass);
            classDefinitionNoDetail(modelClass, false, filepath, null);
        }
    }
    
    public void addRelationships(ModelPackage modelPackage) {
        for (ModelClass modelClass: modelPackage.classes()) {
            // Only draw the relationships between the classes in the package.
            for (ModelRel rel: modelClass.relationships()) {
                if (rel.source() == modelClass && rel.destination().modelPackage() == modelPackage) {
                    relationship(rel);
                }
            }
        }
    }
    
    private final ModelPackage _modelPackage;
}
