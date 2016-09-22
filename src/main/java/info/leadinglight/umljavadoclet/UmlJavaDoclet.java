package info.leadinglight.umljavadoclet;

import com.sun.javadoc.*;
import info.leadinglight.umljavadoclet.model.DocletModelMapper;

public class UmlJavaDoclet {
    public static boolean start(RootDoc root) {
        //RootDocDumper.dumpRootDoc(root);
        
        // Map to our model representation.
        DocletModelMapper mapper = new DocletModelMapper();
        mapper.map(root);
        // Print out the model.
        System.out.println(mapper.getModel().printModel());
        return true;
    }
}
