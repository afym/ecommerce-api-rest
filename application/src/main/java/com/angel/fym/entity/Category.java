package com.angel.fym.entity;

public class Category {
    public static final String[] CATEGORY_CODES = {"cat001", "cat002", "cat003", "cat004", "cat005"};
    private String code;
    private String name;
    private String icon;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
