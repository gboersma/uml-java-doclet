package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelInternalClass;
import info.leadinglight.umljavadoclet.model.ModelRel;

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
                // TODO Other relationships.
            }
        }
    }
    
    private void printClass(ModelClass modelClass) {
        println("Class: " + modelClass.getQualifiedName());
    }
    
    private void printRel(ModelRel rel) {
        println(rel.getType() + ": " + rel.getDestination().getQualifiedName());
    }
    
    private void printSuperclass(ModelClass modelClass) {
        ModelRel generalizationRel = modelClass.getGeneralizationRelationship();
        if (generalizationRel != null) {
            indent();
            printRel(generalizationRel);
        }
    }
    
    private final Model _model;
}
