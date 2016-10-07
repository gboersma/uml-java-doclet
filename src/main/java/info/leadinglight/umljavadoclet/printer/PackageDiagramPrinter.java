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
        // Layout of packages is really, really bad.
        // Would love to show relationships between packages, but it is just awful.
        //noPackagesOption();
        String filepath = packageFilepath(_modelPackage, _modelPackage);
        packageDefinition(_modelPackage, filepath, null);
        for (ModelClass modelClass: _modelPackage.classes()) {
            filepath = classFilepath(_modelPackage, modelClass);
            classDefinitionNoDetail(modelClass, false, filepath, null);
        }
        addRelationships();
        end();
    }
    
    public void addRelationships() {
        for (ModelClass modelClass: _modelPackage.classes()) {
            // Only draw the relationships between the classes in the package.
            for (ModelRel rel: modelClass.relationships()) {
                if (rel.source() == modelClass && rel.destination().modelPackage() == _modelPackage) {
                    relationship(rel);
                }
            }
        }
    }
    
    private final ModelPackage _modelPackage;
}
