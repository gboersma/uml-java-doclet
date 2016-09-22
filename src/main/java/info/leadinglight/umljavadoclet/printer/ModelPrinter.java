package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelInternalClass;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.List;

public class ModelPrinter extends Printer {
    public ModelPrinter(Model model) {
        _model = model;
    }
    
    @Override
    public void print() {
        for (ModelClass modelClass: _model.getClassLookup().getClasses()) {
            if (modelClass instanceof ModelInternalClass) {
                printClass(modelClass);
                printSuperclass(modelClass);
                printUsages(modelClass);
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
            printRel(generalization);
        }
    }
    
    private void printUsages(ModelClass modelClass) {
        List<ModelRel> usages = modelClass.getUsages();
        for (ModelRel usage: usages) {
            indent();
            printRel(usage);
        }
    }

    private void printRel(ModelRel rel) {
        println(rel.getType() + ": " + rel.getDestination().getQualifiedName());
    }
    
    private final Model _model;
}
