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
        return ModelRelFilter.filterForSourceClass(_rels, sourceClass);
    }
    
    public List<ModelRel> getRelationshipsForDestination(ModelClass destClass) {
        return ModelRelFilter.filterForSourceClass(_rels, destClass);
    }
    
    public ModelRel getGeneralizationRel(ModelClass sourceClass) {
        return ModelRelFilter.filterFirstForType(
                ModelRelFilter.filterForSourceClass(_rels, sourceClass),
                GeneralizationRel.class);
    }
    
    private final List<ModelRel> _rels = new ArrayList<>();
}
