package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.Type;
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
    
    public ModelRel getGeneralization(ModelClass sourceClass) {
        return ModelRelFilter.filterFirstForType(
                ModelRelFilter.filterForSourceClass(_rels, sourceClass),
                GeneralizationRel.class);
    }
    
    public ModelRel getUsage(ModelClass sourceClass, ModelClass destClass) {
        return ModelRelFilter.filterFirstForType(
                ModelRelFilter.filterForDestinationClass(
                    ModelRelFilter.filterForSourceClass(_rels, sourceClass),
                        destClass), UsageRel.class);
    }
    
    public List<ModelRel> getUsages(ModelClass sourceClass) {
        return ModelRelFilter.filterForType(
                ModelRelFilter.filterForSourceClass(_rels, sourceClass),
                UsageRel.class);
    }

    private final List<ModelRel> _rels = new ArrayList<>();
}
