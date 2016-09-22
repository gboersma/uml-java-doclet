package info.leadinglight.umljavadoclet.printer;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;

/**
 * Dump all of the info from the RootDoc.
 */
public class RootDocPrinter extends Printer {
    public RootDocPrinter(RootDoc rootDoc) {
        _rootDoc = rootDoc;
    }

    @Override
    public void print() {
        for (ClassDoc classDoc: _rootDoc.classes()) {
            printClassDoc(classDoc);
        }
    }
    
    private void printClassDoc(ClassDoc classDoc) {
        println("Class: " + classDoc.qualifiedTypeName());
        if (classDoc.superclassType() != null) {
            String superClassName = classDoc.superclassType().qualifiedTypeName();
            if (!superClassName.equals("java.lang.Object") && !superClassName.equals("java.lang.Enum")) {
                indent(1);
                println("extends: " + classDoc.superclassType().qualifiedTypeName());
            }
        }
        for (MethodDoc methodDoc: classDoc.methods()) {
            //printMethodDoc(methodDoc);
        }
    }
    
    private void printMethodDoc(MethodDoc methodDoc) {
        indent(1);
        println("Method: " + methodDoc.name());
        for (Parameter param: methodDoc.parameters()) {
            indent(2);
            println("Parameter: " + param.name() + "- " + param.type().qualifiedTypeName());
        }
        indent(2);
        println("Return: " + methodDoc.returnType().qualifiedTypeName());
    }
    
    private final RootDoc _rootDoc;
}
