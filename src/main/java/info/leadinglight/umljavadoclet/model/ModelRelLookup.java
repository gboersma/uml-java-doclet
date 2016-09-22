/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.leadinglight.umljavadoclet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lookup of all relationships in the model.
 */
public class ModelRelLookup extends ModelElement {
    public ModelRel addRelationship(ModelRel rel) {
        _rels.add(rel);
        rel.setModel(getModel());
        // TODO Add to set of relationships for src, dest class. Better lookup algorithm below.
        return rel;
    }
    
    public List<ModelRel> getRelationshipsForSource(ModelClass sourceClass) {
        List<ModelRel> rels = new ArrayList<ModelRel>();
        for (ModelRel rel: _rels) {
            if (rel.getSource().getQualifiedName().equals(sourceClass.getQualifiedName())) {
                rels.add(rel);
            }
        }
        return rels;
    }
    
    public List<ModelRel> getRelationshipsForDestination(ModelClass sourceClass) {
        List<ModelRel> rels = new ArrayList<ModelRel>();
        for (ModelRel rel: _rels) {
            if (rel.getDestination().getQualifiedName().equals(sourceClass.getQualifiedName())) {
                rels.add(rel);
            }
        }
        return rels;
    }
    
    private final List<ModelRel> _rels = new ArrayList<ModelRel>();
}
