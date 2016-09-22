package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.InternalClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.List;

public class ModelPrinter extends Printer {
    public ModelPrinter(Model model) {
        _model = model;
    }
    
    @Override
    public void print() {
        for (ModelClass modelClass: _model.getClassLookup().getClasses()) {
            if (modelClass instanceof InternalClass) {
                printClass(modelClass);
                printSuperclass(modelClass);
                printDependencies(modelClass);
                printDependents(modelClass);
                // TODO Other relationships.
            }
        }
    }
    
    private void printClass(ModelClass modelClass) {
        println("Class: " + modelClass.getQualifiedName());
    }
    
    private void printSuperclass(ModelClass modelClass) {
        ModelRel generalization = modelClass.getGeneralization();
        if (generalization != null) {
            indent();
            printSuperclassRel(generalization);
        }
    }
    
    private void printDependencies(ModelClass modelClass) {
        List<ModelRel> usages = modelClass.getDependencies();
        for (ModelRel usage: usages) {
            indent();
            printUsesRel(usage);
        }
    }

    private void printDependents(ModelClass modelClass) {
        List<ModelRel> usages = modelClass.getDependents();
        for (ModelRel usage: usages) {
            indent();
            printUsedRel(usage);
        }
    }

    private void printSuperclassRel(ModelRel rel) {
        println("extends: " + rel.getDestination().getQualifiedName());
    }

    private void printUsesRel(ModelRel rel) {
        println("uses: " + rel.getDestination().getQualifiedName());
    }
    
    private void printUsedRel(ModelRel rel) {
        println("used by: " + rel.getSource().getQualifiedName());
    }

    private final Model _model;
}
