package info.leadinglight.umljavadoclet.printer;

/**
 * Handle options specific to uml-java-doclet.
 */
public class DiagramOptions {
    public DiagramOptions() {
        // Defaults
        _lineType = LineType.ORTHO;
    }
    
    public enum LineType { SPLINE, POLYLINE, ORTHO };
    
    public LineType getLineType() {
        return _lineType;
    }
    
    public void setLineType(LineType lineType) {
        _lineType = lineType;
    }
    
    public void setLineType(String[] option) {
        if (option[0].equals(OPTION_LINE_TYPE)) {
            if (option[1].equals(OPTION_LINE_TYPE_ORTHO)) {
                setLineType(DiagramOptions.LineType.ORTHO);
            } else if (option[1].equals(OPTION_LINE_TYPE_POLYLINE)) {
                setLineType(DiagramOptions.LineType.POLYLINE);
            } else if (option[1].equals(OPTION_LINE_TYPE_SPLINE)) {
                setLineType(DiagramOptions.LineType.SPLINE);
            }
        }
    }
    
    public static String checkLineTypeOption(String[] option) {
        if (option[0].equals(OPTION_LINE_TYPE)) {
            if (!option[1].equals(OPTION_LINE_TYPE_ORTHO) && 
                    !option[1].equals(OPTION_LINE_TYPE_POLYLINE) &&
                    !option[1].equals(OPTION_LINE_TYPE_SPLINE)) {
                return "Invalid line type (polyline, ortho, or spline).";
            }
        }
        return null;
    }
    
    /**
     * Set the options as provided in the strings.
     * Invalid options are ignored.
     * @param options Options provided in javadoc format for options: an array of string arrays, each
     * array indicating the name of the option (index 0) and the associated options.
     */
    public void set(String[][] options) {
        for (String[] option: options) {
            setLineType(option);
        }
    }
    
    /**
     * Check to see if the specified option is valid.
     * @param option Option to check.
     * @return Whether or not the option is valid.
     */
    public static boolean isValidOption(String option) {
        return option.equals(OPTION_LINE_TYPE);
    }
    
    /**
     * Get the number of parameters for the specified option.
     * @param option Option to get parameters for.
     * @return Number of parameters.
     */
    public static int getOptionLength(String option) {
        if (option.equals(OPTION_LINE_TYPE)) {
            return 2;
        } else {
            return 0;
        }
    }
    
    public static String checkOption(String[] option) {
        String error = checkLineTypeOption(option);
        if (error != null && error.length() > 0) {
            return error;
        }
        return null;
    }
    
    private LineType _lineType;

    private static final String OPTION_LINE_TYPE = "-linetype";
    private static final String OPTION_LINE_TYPE_ORTHO = "ortho";
    private static final String OPTION_LINE_TYPE_POLYLINE = "polyline";
    private static final String OPTION_LINE_TYPE_SPLINE = "spline";
}
