package com.prapps.ved.dto;

import java.util.ArrayList;
import java.util.List;

public enum ScriptType {
    Devnagri("dv", "Devnagri"),
    Roman("ro", "Roman"),
    Bengali("bn", "Bengali");

    private String code;
    private String label;

    private ScriptType(String code, String label) {
        this.code = code; this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static ScriptType getByCode(String code) {
        for (ScriptType type : ScriptType.values()) {
            if (type.getCode().equalsIgnoreCase(code))
                return type;
        }

        return null;
    }

    public static ScriptType getByLabel(String value) {
        for (ScriptType type : ScriptType.values()) {
            if (type.getLabel().equalsIgnoreCase(value))
                return type;
        }

        return null;
    }

    public static List<String> getCodes() {
        List<String> list = new ArrayList<>(ScriptType.values().length);
        for (ScriptType type : ScriptType.values()) {
            list.add(type.getCode());
        }
        return list;
    }

    public static List<String> getLabels() {
        List<String> list = new ArrayList<>(ScriptType.values().length);
        for (ScriptType type : ScriptType.values()) {
            list.add(type.getLabel());
        }
        return list;
    }
}
