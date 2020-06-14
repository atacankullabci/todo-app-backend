package com.atacankullabci.todoapp.common.enums;

public enum EnumItemDependency {
    DEPENDENT("Dependent"),
    NOT_DEPENDENT("Not Dependent");

    private String label;

    private EnumItemDependency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isDependent() {
        return this.equals(DEPENDENT);
    }

    public boolean isNotDependent() {
        return this.equals(NOT_DEPENDENT);
    }
}
