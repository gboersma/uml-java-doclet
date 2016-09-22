package info.leadinglight.umljavadoclet.model;

import java.util.List;

/**
 * A representation of a set of classes and the relationships between them.
 */
public class Model {
    public Model() {
        _classLookup = new ModelClassLookup();
        _classLookup.setModel(this);
        _relLookup = new ModelRelLookup();
        _relLookup.setModel(this);
    }
    
    public ModelClassLookup getClassLookup() {
        return _classLookup;
    }
    
    public ModelRelLookup getRelationshipLookup() {
        return _relLookup;
    }

    public String printModel() {
        StringBuilder sb = new StringBuilder();
        for (ModelClass modelClass: _classLookup.getClasses()) {
            if (modelClass instanceof ModelInternalClass) {
                sb.append(modelClass.printModel());
                sb.append("\n");
                List<ModelRel> rels = _relLookup.getRelationshipsForSource(modelClass);
                if (rels.size() > 0) {
                    sb.append("  Relationships:");
                    for (ModelRel rel: rels) {
                        sb.append("\n    ");
                        sb.append(rel.printModel());
                        sb.append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    private final ModelClassLookup _classLookup;
    private final ModelRelLookup _relLookup;
}
