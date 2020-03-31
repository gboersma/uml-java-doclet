package info.leadinglight.umljavadoclet.printer;

import java.util.ArrayList;
import java.util.List;

public class DiagramOption {
    public DiagramOption(String name, int length) {
        this.name = name;
        this.length = length;
        // No valid values / default options- free-form option.
        this.validValues = null;
        this.defaultValue = null;
    }

    public DiagramOption(String name, String validValues, String defaultValue, int length) {
        this.name = name;
        // Valid values are separated by a comma.
        this.validValues = new ArrayList<>();
        String[] parts = validValues.split(",");
        for (String part: parts) {
            this.validValues.add(part.trim());
        }
        this.defaultValue = defaultValue;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValidValues() {
        return validValues;
    }

    public boolean isValidValue(String value) {
        return validValues != null ? validValues.contains(value) : true;
    }

    public void setValidValues(List<String> validValues) {
        this.validValues = validValues;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValue() {
        return value != null ? value : defaultValue;
    }

    public void setValue(String value) {
        if (validValues != null) {
            for (String validValue : validValues) {
                if (validValue.equalsIgnoreCase(value)) {
                    this.value = validValue;
                    return;
                }
            }
        } else {
            // Free-form value.
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "DiagramOption{" +
            "name='" + name + '\'' +
            ", validValues=" + validValues +
            ", defaultValue='" + defaultValue + '\'' +
            ", length=" + length +
            ", value='" + value + '\'' +
            '}';
    }

    private String name;
    private List<String> validValues;
    private String defaultValue;
    private int length;
    private String value;
}
