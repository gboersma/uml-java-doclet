package info.leadinglight.umljavadoclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;

/**
 * Dump all of the info from the RootDoc.
 */
public class RootDocDumper {
    public static void dumpRootDoc(RootDoc rootDoc) {
        for (ClassDoc classDoc: rootDoc.classes()) {
            dumpClassDoc(classDoc);
        }
    }
    
    private static void dumpClassDoc(ClassDoc classDoc) {
        System.out.println("Class: " + classDoc.qualifiedTypeName());
        if (classDoc.superclassType() != null) {
            String superClassName = classDoc.superclassType().qualifiedTypeName();
            if (!superClassName.equals("java.lang.Object") && !superClassName.equals("java.lang.Enum")) {
                System.out.println("  Superclass: " + classDoc.superclassType().qualifiedTypeName());
            }
        }
        for (MethodDoc methodDoc: classDoc.methods()) {
            dumpMethodDoc(methodDoc);
        }
    }
    
    private static void dumpMethodDoc(MethodDoc methodDoc) {
        System.out.println("  Method: " + methodDoc.name());
        for (Parameter param: methodDoc.parameters()) {
            System.out.println("    Parameter: " + param.name() + "- " + param.type().qualifiedTypeName());
        }
        System.out.println("    Return: " + methodDoc.returnType().qualifiedTypeName());
    }
}
