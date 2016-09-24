package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lookup of relationships.
 */
public class RelLookup extends ModelElement {
    public List<ModelRel> all() {
        return _rels;
    }
    
    public boolean isEmpty() {
        return _rels.isEmpty();
    }
    
    public ModelRel first() {
        return _rels.size() > 0 ? _rels.get(0) : null;
    }
    
    public void add(ModelRel rel) {
        _rels.add(rel);
    }
    
    public RelLookup source(ModelClass source) {
        RelLookup filtered = new RelLookup();
        for (ModelRel rel: _rels) {
            if (rel.getSource() == source) {
                filtered.add(rel);
            }
        }
        return filtered;
    }
    
    public RelLookup destination(ModelClass dest) {
        RelLookup filtered = new RelLookup();
        for (ModelRel rel: _rels) {
            if (rel.getDestination() == dest) {
                filtered.add(rel);
            }
        }
        return filtered;
    }
    
    public RelLookup type(Class<? extends ModelRel> type) {
        RelLookup filtered = new RelLookup();
        for (ModelRel rel: _rels) {
            if (rel.getClass() == type) {
                filtered.add(rel);
            }
        }
        return filtered;
    }
    
    public RelLookup between(ModelClass source, ModelClass dest) {
        return source(source).destination(dest);
    }
    
    private final List<ModelRel> _rels = new ArrayList<>();
}
