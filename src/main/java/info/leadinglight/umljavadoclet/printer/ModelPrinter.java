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
    public String print() {
        StringBuilder sb = new StringBuilder();
        for (ModelClass modelClass: _model.getClassLookup().getClasses()) {
            if (modelClass instanceof ModelInternalClass) {
                println(sb, modelClass.printModel());
                List<ModelRel> rels = _model.getRelationshipLookup().getRelationshipsForSource(modelClass);
                if (rels.size() > 0) {
                    println(sb, "  Relationships:");
                    for (ModelRel rel: rels) {
                        println(sb, rel.printModel());
                    }
                }
            }
        }
        return sb.toString();
    }
    
    private final Model _model;
}
