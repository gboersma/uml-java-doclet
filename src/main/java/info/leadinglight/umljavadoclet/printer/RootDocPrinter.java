package info.leadinglight.umljavadoclet.printer;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;

/**
 * Dump all of the info from the RootDoc.
 */
public class RootDocPrinter extends Printer {
    public RootDocPrinter(RootDoc rootDoc) {
        _rootDoc = rootDoc;
    }

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
                println(1, "extends: " + classDoc.superclassType().qualifiedTypeName());
            }
        }
        for (MethodDoc methodDoc: classDoc.methods(false)) {
            printMethodDoc(methodDoc);
        }
    }
    
    private void printMethodDoc(MethodDoc methodDoc) {
        indent(1);
        println("Method: " + methodDoc.name());
        for (Parameter param: methodDoc.parameters()) {
            print(2, "Parameter: " + param.name() + "- ");
            printType(param.type());
            print(" ");
            newline();
        }
        print(2, "Return: ");
        printType(methodDoc.returnType());
        print(" ");
        newline();
    }
    
    private void printType(Type type) {
        print(type.typeName());
        ParameterizedType paramType = type.asParameterizedType();
        if (paramType != null) {
            print("<");
            Type[] args = paramType.typeArguments();
            String sep = "";
            for (Type arg: args) {
                print(sep);
                printType(arg);
                sep = ", ";
            }
            print(">");
        }
    }
    
    private final RootDoc _rootDoc;
}
