package info.leadinglight.umljavadoclet.printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle options specific to uml-java-doclet.
 */
public class DiagramOptions {
    public DiagramOptions() {
        addOption(LINETYPE, "polyline,spline,ortho", "ortho", 2);
        addOption(DEPENDENCIES, "public,protected,package,private", "public", 2);
        addOption(PACKAGE_ORIENTATION, "left-to-right,top-to-bottom", "top-to-bottom", 2);
        addOption(OUTPUT_MODEL, "true,false", "false", 2);
        addOption(PUML_INCLUDE_FILE, 2);
    }

    // Helpers for getting the value for a specific option

    public enum LineType { SPLINE, POLYLINE, ORTHO };
    public enum Visibility { PUBLIC, PROTECTED, PACKAGE, PRIVATE };
    public enum Orientation { LEFT_TO_RIGHT, TOP_TO_BOTTOM };

    public LineType getLineType() {
        return LineType.valueOf(getOptionEnumValue(LINETYPE));
    }

    public Visibility getDependenciesVisibility() {
        return Visibility.valueOf(getOptionEnumValue(DEPENDENCIES));
    }

    public Orientation getPackageOrientation() {
        return Orientation.valueOf(getOptionEnumValue(PACKAGE_ORIENTATION));
    }

    public boolean isOutputModel() {
        return getOptionValue(OUTPUT_MODEL).equals("true");
    }

    public String getPumlIncludeFile() {
        return getOptionValue(PUML_INCLUDE_FILE);
    }

    public boolean hasPumlIncludeFile() {
        return getPumlIncludeFile() != null && getPumlIncludeFile().length() > 0;
    }

    private static final String LINETYPE = "linetype";
    private static final String DEPENDENCIES = "dependencies";
    private static final String PACKAGE_ORIENTATION = "package-orientation";
    private static final String OUTPUT_MODEL = "output-model";
    private static final String PUML_INCLUDE_FILE = "puml-include-file";

    /**
     * Set the options as provided in the strings.
     * Invalid options are ignored.
     * @param docletOptions Options provided in javadoc format for options: an array of string arrays, each
     * array indicating the name of the option (index 0) and the associated value.
     */
    public void set(String[][] docletOptions) {
        for (String[] docletOption : docletOptions) {
            String docletName = docletOption[0];
            DiagramOption option = getOptionForDocletName(docletName);
            if (option != null) {
                String docletValue = docletOption[1];
                option.setValue(docletValue);
            }
        }
    }

    /**
     * Check to see if the specified option is valid.
     * @param docletName Name of the option to check.
     * @return Whether or not the option is valid.
     */
    public boolean isValidOption(String docletName) {
        return getOptionForDocletName(docletName) != null;
    }

    /**
     * Get the number of parameters for the specified option.
     * @param docletName Name of option to get parameters for.
     * @return Number of parameters.
     */
    public int getOptionLength(String docletName) {
        DiagramOption option = getOptionForDocletName(docletName);
        return option != null ? option.getLength() : 0;
    }

    /**
     * Check to see if the specified setting is a valid option.
     * @param setting Setting to check.
     * @return Error associated with the option, null if no error.
     */
    public String checkOption(String[] setting) {
        String docletName = setting[0];
        DiagramOption option = getOptionForDocletName(docletName);
        if (option == null) {
            return "Invalid option " + docletName;
        }
        String value = setting[1];
        if (!option.isValidValue(value)) {
            return "Invalid value " + value + " for option " + docletName + "; valid values are " + option.getValidValues();
        }
        return null;
    }

    public String getOptionValuesAsString() {
        String result = "";
        for (DiagramOption option: options) {
            result += option.getName() + "=" + option.getValue() + " ";
        }
        return result;
    }

    // Helpers

    private void addOption(String name, int length) {
        DiagramOption option = new DiagramOption(name, length);
        options.add(option);
    }

    private void addOption(String name, String validValues, String defaultValue, int length) {
        DiagramOption option = new DiagramOption(name, validValues, defaultValue, length);
        options.add(option);
    }

    private DiagramOption getOption(String name) {
        for (DiagramOption option: options) {
            if (option.getName().equals(name)) {
                return option;
            }
        }
        return null;
    }

    private DiagramOption getOptionForDocletName(String nameWithHyphen) {
        String name = nameWithHyphen.substring(1);
        return getOption(name);
    }

    private String getOptionValue(String name) {
        DiagramOption option = getOption(name);
        return option != null ? option.getValue() : null;
    }

    private String getOptionEnumValue(String name) {
        String value = getOptionValue(name);
        // Any hyphens in the name are treated as _ in the enum value.
        return value != null ? value.toUpperCase().replace("-", "_") : null;
    }

    @Override
    public String toString() {
        return "DiagramOptions{" +
            "options=" + options +
            '}';
    }

    private List<DiagramOption> options = new ArrayList<>();
}
