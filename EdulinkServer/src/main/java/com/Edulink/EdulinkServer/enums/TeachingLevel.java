package com.Edulink.EdulinkServer.enums;

import java.util.List;

public enum TeachingLevel {

    HIGHSCHOOL(List.of("WAEC","NECO","UTME")),
    UNDERGRADUATE(List.of("BSC", "OND", "PHD" ,"HND")),
    POSTGRADUATE(List.of("MSC","PHD"));

    private final List<String> allowedTypes;

    TeachingLevel(List<String> allowedTypes){
        this.allowedTypes = allowedTypes;
    }

    public List<String> getAllowedTypes() {
        return allowedTypes;
    }

    public boolean isValidType(String value){
        return allowedTypes.contains(value.toUpperCase());
    }
}
