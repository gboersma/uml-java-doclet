package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lookup of relationships.
 */
public class RelLookup extends ModelElement {
    public ModelRel addRelationship(ModelRel rel) {
        _rels.add(rel);
        rel.setModel(getModel());
        return rel;
    }
    
    public List<ModelRel> getRelationshipsForSource(ModelClass sourceClass) {
        return RelFilter.filterForSourceClass(_rels, sourceClass);
    }
    
    public List<ModelRel> getRelationshipsForDestination(ModelClass destClass) {
        return RelFilter.filterForDestinationClass(_rels, destClass);
    }
    
    public ModelRel getGeneralization(ModelClass sourceClass) {
        return RelFilter.filterFirstForType(
                RelFilter.filterForSourceClass(_rels, sourceClass),
                GeneralizationRel.class);
    }
    
    public ModelRel getDependency(ModelClass sourceClass, ModelClass destClass) {
        return RelFilter.filterFirstForType(
                RelFilter.filterForDestinationClass(
                    RelFilter.filterForSourceClass(_rels, sourceClass),
                        destClass), DependencyRel.class);
    }
    
    public List<ModelRel> getDependencies(ModelClass sourceClass) {
        return RelFilter.filterForType(
                RelFilter.filterForSourceClass(_rels, sourceClass),
                DependencyRel.class);
    }

    public List<ModelRel> getDependents(ModelClass sourceClass) {
        return RelFilter.filterForType(
                RelFilter.filterForDestinationClass(_rels, sourceClass),
                DependencyRel.class);
    }

    private final List<ModelRel> _rels = new ArrayList<>();
}
