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
    
    public String print() {
        StringBuilder sb = new StringBuilder();
        for (ClassDoc classDoc: _rootDoc.classes()) {
            printClassDoc(sb, classDoc);
        }
        return sb.toString();
    }
    
    private void printClassDoc(StringBuilder sb, ClassDoc classDoc) {
        println(sb, "Class: " + classDoc.qualifiedTypeName());
        if (classDoc.superclassType() != null) {
            String superClassName = classDoc.superclassType().qualifiedTypeName();
            if (!superClassName.equals("java.lang.Object") && !superClassName.equals("java.lang.Enum")) {
                println(sb, "  Superclass: " + classDoc.superclassType().qualifiedTypeName());
            }
        }
//        for (MethodDoc methodDoc: classDoc.methods()) {
//            printMethodDoc(sb, methodDoc);
//        }
    }
    
    private void printMethodDoc(StringBuilder sb, MethodDoc methodDoc) {
        println(sb, "  Method: " + methodDoc.name());
        for (Parameter param: methodDoc.parameters()) {
            println(sb, "    Parameter: " + param.name() + "- " + param.type().qualifiedTypeName());
        }
        println(sb, "    Return: " + methodDoc.returnType().qualifiedTypeName());
    }
    
    private final RootDoc _rootDoc;
}
