package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lookup of relationships.
 */
public class RelFilter {
    public RelFilter() {
        _rels = new ArrayList<>();
    }

    public RelFilter(List<ModelRel> rels) {
        _rels = rels;
    }
    
    public boolean isEmpty() {
        return _rels.isEmpty();
    }
    
    public List<ModelRel> all() {
        return _rels;
    }
    
    public ModelRel first() {
        return _rels.size() > 0 ? _rels.get(0) : null;
    }
    
    public List<ModelClass> sourceClasses() {
        List<ModelClass> classes = new ArrayList<>();
        for (ModelRel rel: _rels) {
            classes.add(rel.source());
        }
        return classes;
    }

    public List<ModelClass> destinationClasses() {
        List<ModelClass> classes = new ArrayList<>();
        for (ModelRel rel: _rels) {
            classes.add(rel.destination());
        }
        return classes;
    }
    
    public void add(ModelRel rel) {
        _rels.add(rel);
    }
    
    public RelFilter source(ModelClass source) {
        RelFilter filter = new RelFilter();
        for (ModelRel rel: _rels) {
            if (rel.source() == source) {
                filter.add(rel);
            }
        }
        return filter;
    }
    
    public RelFilter destination(ModelClass dest) {
        RelFilter filter = new RelFilter();
        for (ModelRel rel: _rels) {
            if (rel.destination() == dest) {
                filter.add(rel);
            }
        }
        return filter;
    }
    
    public RelFilter kind(ModelRel.Kind kind) {
        RelFilter filter = new RelFilter();
        for (ModelRel rel: _rels) {
            if (rel.kind() == kind) {
                filter.add(rel);
            }
        }
        return filter;
    }
    
    public RelFilter between(ModelClass source, ModelClass dest) {
        return source(source).destination(dest);
    }
    
    private final List<ModelRel> _rels;
}
