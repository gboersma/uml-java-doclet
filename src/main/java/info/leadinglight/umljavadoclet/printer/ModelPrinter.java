package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.DependencyRel;
import info.leadinglight.umljavadoclet.model.GeneralizationRel;
import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelType;
import java.util.List;

public class ModelPrinter extends Printer {
    public ModelPrinter(Model model) {
        _model = model;
    }
    
    public void print() {
        for (ModelType modelType: _model.getTypes().all()) {
            if (modelType.isInternal()) {
                printClass(modelType);
                printSuperclass(modelType);
                printDependencies(modelType);
                printDependents(modelType);
                // TODO Other relationships.
            }
        }
    }
    
    private void printClass(ModelType modelType) {
        println("Class: " + modelType.getQualifiedName());
    }
    
    private void printSuperclass(ModelType modelType) {
        GeneralizationRel generalization = modelType.getGeneralization();
        if (generalization != null) {
            indent();
            printSuperclassRel(generalization);
        }
    }
    
    private void printDependencies(ModelType modelType) {
        List<DependencyRel> usages = modelType.getDependencies();
        for (DependencyRel usage: usages) {
            indent();
            printUsesRel(usage);
        }
    }

    private void printDependents(ModelType modelType) {
        List<DependencyRel> usages = modelType.getDependents();
        for (DependencyRel usage: usages) {
            indent();
            printUsedRel(usage);
        }
    }

    private void printSuperclassRel(GeneralizationRel rel) {
        println("extends: " + rel.getDestination().getQualifiedName());
    }

    private void printUsesRel(DependencyRel rel) {
        println("uses: " + rel.getDestination().getQualifiedName());
    }
    
    private void printUsedRel(DependencyRel rel) {
        println("used by: " + rel.getSource().getQualifiedName());
    }

    private final Model _model;
}
