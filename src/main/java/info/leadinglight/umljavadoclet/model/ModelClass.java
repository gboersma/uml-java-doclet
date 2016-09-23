package info.leadinglight.umljavadoclet.model;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class internal or external to the model.
 */
public class ModelClass extends ModelElement {
    public ModelClass(ClassDoc classDoc, boolean isInternal) {
        _classDoc = classDoc;
        _isInternal = isInternal;
    }
    
    public ClassDoc getClassDoc() {
        return _classDoc;
    }
    
    public String getQualifiedName() {
        return _classDoc.qualifiedName();
    }
    
    public boolean isInternal() {
        return _isInternal;
    }
    
    public boolean isExternal() {
        return !isInternal();
    }
    
    public RelLookup getRelationshipLookup() {
        return _relLookup;
    }
    
    public List<ModelRel> getRelationships() {
        return _relLookup.all();
    }
    
    public void addRelationship(ModelRel rel) {
        _relLookup.add(rel);
    }
    
    public GeneralizationRel getGeneralization() {
        return (GeneralizationRel) _relLookup.type(GeneralizationRel.class).source(this).first();
    }
    
    public void addGeneralizationTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        GeneralizationRel rel = new GeneralizationRel(this, dest);
        getModel().addRelationship(rel);
    }
    
    public void addRealizationTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        RealizationRel rel = new RealizationRel(this, dest);
        getModel().addRelationship(rel);
    }

    public List<DependencyRel> getDependencies() {
        List<ModelRel> rels = _relLookup.source(this).type(DependencyRel.class).all();
        return castDependencyRels(rels);
    }
    
    public List<DependencyRel> getDependents() {
        List<ModelRel> rels = _relLookup.destination(this).type(DependencyRel.class).all();
        return castDependencyRels(rels);
    }

    public DependencyRel addDependencyTo(Type type) {
        DependencyRel rel = null;
        ModelClass dest = getModel().getClasses().createExternal(type);
        // Only add dependency to the class if a relationship does not already exist.
        if (dest != this && _relLookup.between(this, dest).isEmpty()) {
            rel = new DependencyRel(this, dest);
            getModel().addRelationship(rel);
        }
        return rel;
    }
    
    public AssociationRel getAssociationWith(ModelClass otherClass) {
        AssociationRel association = (AssociationRel) _relLookup.between(this, otherClass).first();
        if (association == null) {
            association = (AssociationRel) _relLookup.between(otherClass, this).first();
        }
        return association;
    }
    
    public AssociationRel addAssociationTo(Type type) {
        ModelClass dest = getModel().getClasses().createExternal(type);
        // When adding an association with a destination class, each one will have its own label
        // and multiplicity. Do not reuse existing associations- create a new one.
        // Also, if it is an association with itself, draw it explicitly as well.
        AssociationRel association = new AssociationRel(this, dest);
        getModel().addRelationship(association);
        return association;
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        _relLookup.setModel(model);
    }
    
    public ModelPackage getPackage() {
        return _package;
    }
    
    public void setPackage(ModelPackage modelPackage) {
        _package = modelPackage;
    }
    
    private List<DependencyRel> castDependencyRels(List<ModelRel> rels) {
        List<DependencyRel> dependencies = new ArrayList<>();
        for (ModelRel rel: rels) {
            dependencies.add((DependencyRel)rel);
        }
        return dependencies;
    }
    
    private final RelLookup _relLookup = new RelLookup();
    private ModelPackage _package;
    private final ClassDoc _classDoc;
    private final boolean _isInternal;
}
