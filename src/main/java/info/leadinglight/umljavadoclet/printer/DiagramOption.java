package info.leadinglight.umljavadoclet.printer;

import java.util.ArrayList;
import java.util.List;

public class DiagramOption {
    public DiagramOption(String name, String validValues, String defaultValue, int length) {
        this.name = name;
        // Valid values are separated by a comma.
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
        return validValues.contains(value);
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
        for (String validValue: validValues) {
            if (validValue.equalsIgnoreCase(value)) {
                this.value = validValue;
                return;
            }
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
    private List<String> validValues = new ArrayList<>();
    private String defaultValue;
    private int length;
    private String value;
}
