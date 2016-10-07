package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;
import info.leadinglight.umljavadoclet.model.ModelRel;

public class ModelPrinter extends Printer {
    public ModelPrinter(Model model) {
        _model = model;
    }
    
    public void generate() {
        printClasses();
        printPackages();
    }
    
    private void printClasses() {
        for (ModelClass modelClass: _model.classes()) {
            if (modelClass.isInternal()) {
                printClass(modelClass);
                for (ModelRel rel: modelClass.relationships()) {
                    printRel(modelClass, rel);
                }
            }
        }
    }
    
    private void printClass(ModelClass modelClass) {
        println("Class: " + modelClass.fullName());
    }
    
    private void printRel(ModelClass modelClass, ModelRel rel) {
        indent(1);
        boolean fromSource = (rel.source() == modelClass);
        printKind(rel.kind(), fromSource);
        print(": ");
        print(fromSource ? rel.destination().fullName() :rel.source().fullName());
        if (rel.destinationRole() != null) {
            print (" " + rel.destinationRole());
        }
        if (rel.destinationCardinality() != null) {
            print (" ");
            printMultiplicity(rel.destinationCardinality());
        }
        if (rel.destinationVisibility() != null) {
            print(" ");
            printVisibility(rel.destinationVisibility());
        }
        newline();
    }
    
    private void printKind(ModelRel.Kind kind, boolean fromSource) {
        switch (kind) {
            case GENERALIZATION:
                print (fromSource ? "extends" : "extended by");
                return;
            case REALIZATION:
                print (fromSource ? "implements" : "implemented by");
                return;
            case DIRECTED_ASSOCIATION:
                print (fromSource ? "has" : "had by");
                return;
            case DEPENDENCY:
                print (fromSource ? "uses" : "used by");
        }
    }
    
    private void printMultiplicity(ModelRel.Multiplicity mult) {
        switch (mult) {
            case ONE:
                print("one");
                return;
            case ZERO_OR_ONE:
                print("zero or one");
                return;
            case MANY:
                print("many");
        }
    }
    
    private void printVisibility(ModelRel.Visibility visibility) {
        switch (visibility) {
            case PUBLIC:
                print("public");
                return;
            case PROTECTED:
                print("protected");
                return;
            case PACKAGE:
                print("package");
                return;
            case PRIVATE:
                print("private");
        }
    }

    private void printPackages() {
        for (ModelPackage modelPackage: _model.packages()) {
            printPackage(modelPackage);
        }
    }

    private void printPackage(ModelPackage modelPackage) {
        println("Package: " + modelPackage.fullName());
        for (ModelPackage dependency: modelPackage.dependencies()) {
            println(1, "depends on: " + dependency.fullName());
        }
        for (ModelPackage dependent: modelPackage.dependents()) {
            println(1, "is dependency for: " + dependent.fullName());
        }
        for (ModelClass modelClass: modelPackage.classes()) {
            indent(1);
            printClass(modelClass);
        }
    }

    private final Model _model;
}
