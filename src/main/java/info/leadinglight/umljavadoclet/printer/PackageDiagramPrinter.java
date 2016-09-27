package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.ArrayList;
import java.util.List;

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
        // Layout of packages is really, really bad.
        // Would love to show relationships between packages, but it is just awful.
        //noPackagesOption();
        orthogonalLinesOption();
        emptyPackage(_modelPackage);
        for (ModelClass modelClass: _modelPackage.classes()) {
            emptyClass(modelClass);
        }
        addRelationships();
        end();
    }
    
    public void addRelationships() {
        for (ModelClass modelClass: _modelPackage.classes()) {
            emptyClass(modelClass);
            // Only draw the relationships between the classes in the package.
            for (ModelRel rel: modelClass.relationships()) {
                if (rel.source() == modelClass && rel.destination().modelPackage() == _modelPackage) {
                    relationship(rel);
                }
            }
        }
    }
    
    private final ModelPackage _modelPackage;
    private final List<ModelPackage> _packages = new ArrayList<>();
}
