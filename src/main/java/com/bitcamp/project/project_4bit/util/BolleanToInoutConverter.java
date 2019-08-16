package com.bitcamp.project.project_4bit.util;

import javax.persistence.AttributeConverter;

public class BolleanToInoutConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute ? "IN" : "OUT";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "IN".equalsIgnoreCase(dbData);
    }
}
