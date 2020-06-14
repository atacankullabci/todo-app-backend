package com.atacankullabci.todoapp.common.enums;

public enum EnumItemStatus {
    DONE("Done"),
    UNDONE("Undone");

    private String label;

    private EnumItemStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isDone() {
        return this.equals(DONE);
    }

    public boolean isUndone() {
        return this.equals(UNDONE);
    }
}
