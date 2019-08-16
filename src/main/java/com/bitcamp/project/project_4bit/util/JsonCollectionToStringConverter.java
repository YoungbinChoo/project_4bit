package com.bitcamp.project.project_4bit.util;

import com.bitcamp.project.project_4bit.model.StAnswer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/* 저희 테이블 중 StudentTest에서 JSON을 사용 >> 이 Converter을 사용하는 건 StAnswer뿐 */
public class JsonCollectionToStringConverter<T extends Object>
            implements AttributeConverter<Collection<T>, String> {

    private final ObjectMapper objectMapper;

    public JsonCollectionToStringConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String convertToDatabaseColumn(Collection<T> attribute) {
        String jsonString = null;
        try {
            // convert collection of POJO to json
            jsonString = objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return jsonString;
    }

    @Override
    public Collection<T> convertToEntityAttribute(String dbData) {
        Collection<T> objectArray = new ArrayList<>();
        try {
            // convert json to collection of POJO
            // TypeReference<> 에서 JSON을 사용하는 StAnswer 넣음
            objectArray = objectMapper.readValue(dbData, new TypeReference<StAnswer>() {});
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return objectArray;
    }
}
