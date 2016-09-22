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
                List<ModelRel> rels = modelClass.getSourceRelationships();
                if (rels.size() > 0) {
                    println("  Relationships:");
                    for (ModelRel rel: rels) {
                        print("    ");
                        printRel(rel);
                    }
                }
            }
        }
    }
    
    private void printClass(ModelClass modelClass) {
        println("Class: " + modelClass.getQualifiedName());
    }
    
    private void printRel(ModelRel rel) {
        println(rel.getType() + ": " + rel.getDestination().getQualifiedName());
    }
    
    private final Model _model;
}
