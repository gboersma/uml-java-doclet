package info.leadinglight.umljavadoclet.printer;

import info.leadinglight.umljavadoclet.model.Model;
import info.leadinglight.umljavadoclet.model.ModelClass;
import info.leadinglight.umljavadoclet.model.ModelPackage;

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
            if (modelClass.internal()) {
                printClass(modelClass);
                printSuperclass(modelClass);
//                printDependencies(modelClass);
//                printDependents(modelClass);
                // TODO Other relationships.
            }
        }
    }
    
    private void printClass(ModelClass modelClass) {
        println("Class: " + modelClass.fullName());
    }
    
    private void printSuperclass(ModelClass modelClass) {
        ModelClass superclass = modelClass.superclass();
        if (superclass != null) {
            println("  extends: " + superclass.fullName());
        }
    }
    
    private void printPackages() {
        for (ModelPackage modelPackage: _model.modelPackages()) {
            printPackage(modelPackage);
//                printSuperclass(modelClass);
//                printDependencies(modelClass);
//                printDependents(modelClass);
                // TODO Other relationships.
        }
    }

    private void printPackage(ModelPackage modelPackage) {
        println("Package: " + modelPackage.fullName());
        for (ModelClass modelClass: modelPackage.modelClasses()) {
            print("  ");
            printClass(modelClass);
        }
    }

//    private void printSuperclass(ModelClass modelClass) {
//        GeneralizationRel generalization = modelClass.getGeneralization();
//        if (generalization != null) {
//            indent();
//            printSuperclassRel(generalization);
//        }
//    }
//    
//    private void printDependencies(ModelClass modelClass) {
//        List<DependencyRel> usages = modelClass.getDependencies();
//        for (DependencyRel usage: usages) {
//            indent();
//            printUsesRel(usage);
//        }
//    }
//
//    private void printDependents(ModelClass modelClass) {
//        List<DependencyRel> usages = modelClass.getDependents();
//        for (DependencyRel usage: usages) {
//            indent();
//            printUsedRel(usage);
//        }
//    }
//
//    private void printSuperclassRel(GeneralizationRel rel) {
//        println("extends: " + rel.getDestination().getQualifiedName());
//    }
//
//    private void printUsesRel(DependencyRel rel) {
//        println("uses: " + rel.getDestination().getQualifiedName());
//    }
//    
//    private void printUsedRel(DependencyRel rel) {
//        println("used by: " + rel.getSource().getQualifiedName());
//    }

    private final Model _model;
}
