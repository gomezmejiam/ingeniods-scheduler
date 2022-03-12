package com.ingeniods.scheduler.domain.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.util.StringUtils;

public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(JsonNode attribute) {
    if(Objects.isNull(attribute)) {
      return null;
    }
    try {
		return objectMapper.writeValueAsString(attribute);
	} catch (JsonProcessingException e) {
		throw new RuntimeException(e.getMessage());
	}
  }

  @Override
  public JsonNode convertToEntityAttribute(String dbData) {
    if(StringUtils.isEmpty(dbData) ) {
      return null;
    }
    
    try {
		return objectMapper.readValue(dbData, JsonNode.class);
	} catch (JsonMappingException e) {
		throw new RuntimeException(e.getMessage());
	} catch (JsonProcessingException e) {
		throw new RuntimeException(e.getMessage());
	}
  }

}
