package system.utils;

public final class FieldInfo {
    private final String fieldName;
    private final String fieldValue;
    private String description;
    private boolean isValid = false;

    public FieldInfo(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
