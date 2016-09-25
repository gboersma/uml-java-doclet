package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.model.ModelRel;
import java.util.List;

public class ModelPrinter extends Printer {
    public ModelPrinter(Model model) {
        _model = model;
    }
    
    public void print() {
        printClasses();
        printPackages();
    }
    
    private void printClasses() {
        for (ModelClass modelClass: _model.modelClasses()) {
            if (modelClass.isInternal()) {
                printClass(modelClass);
                printSuperclass(modelClass);
                printInterfaces(modelClass);
                printAssociationsTo(modelClass);
                printAssociationsFrom(modelClass);
                printDependencies(modelClass);
                printDependents(modelClass);
            }
        }
    }
    
    private void printClass(ModelClass modelClass) {
        println("Class: " + modelClass.fullName());
    }
    
    private void printSuperclass(ModelClass modelClass) {
        ModelClass superclass = modelClass.superclass();
        if (superclass != null) {
            println(1, "extends: " + superclass.fullName());
        }
    }
    
    private void printInterfaces(ModelClass modelClass) {
        for (ModelClass intrface: modelClass.interfaces()) {
            println(1, "implements: " + intrface.fullName());
        }
    }
    
    private void printAssociationsTo(ModelClass modelClass) {
        for (ModelRel assocTo: modelClass.sourceAssociations()) {
            println(1, "to: " + assocTo.sourceRole() + " (" + assocTo.destination().fullName() + ")");
        }
    }

    private void printAssociationsFrom(ModelClass modelClass) {
        for (ModelRel assocFrom: modelClass.destinationAssociations()) {
            println(1, "from: " + assocFrom.sourceRole() + " (" + assocFrom.source().fullName() + ")");
        }
    }

    private void printDependencies(ModelClass modelClass) {
        for (ModelClass dependency: modelClass.dependencies()) {
            println(1, "uses: " + dependency.fullName());
        }
    }

    private void printDependents(ModelClass modelClass) {
        for (ModelClass dependent: modelClass.dependents()) {
            println(1, "used by: " + dependent.fullName());
        }
    }

    private void printPackages() {
        for (ModelPackage modelPackage: _model.modelPackages()) {
            printPackage(modelPackage);
        }
    }

    private void printPackage(ModelPackage modelPackage) {
        println("Package: " + modelPackage.fullName());
        for (ModelClass modelClass: modelPackage.modelClasses()) {
            print("  ");
            printClass(modelClass);
        }
    }

    private final Model _model;
}
