package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lookup of relationships.
 */
public class ModelRelLookup extends ModelElement {
    public ModelRel addRelationship(ModelRel rel) {
        _rels.add(rel);
        rel.setModel(getModel());
        return rel;
    }
    
    public List<ModelRel> getRelationshipsForSource(ModelClass sourceClass) {
        List<ModelRel> rels = new ArrayList<>();
        for (ModelRel rel: _rels) {
            if (rel.getSource().getQualifiedName().equals(sourceClass.getQualifiedName())) {
                rels.add(rel);
            }
        }
        return rels;
    }
    
    public List<ModelRel> getRelationshipsForDestination(ModelClass sourceClass) {
        List<ModelRel> rels = new ArrayList<>();
        for (ModelRel rel: _rels) {
            if (rel.getDestination().getQualifiedName().equals(sourceClass.getQualifiedName())) {
                rels.add(rel);
            }
        }
        return rels;
    }
    
    private final List<ModelRel> _rels = new ArrayList<>();
}
