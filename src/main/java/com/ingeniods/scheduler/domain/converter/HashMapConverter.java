package com.ingeniods.scheduler.domain.converter;

import java.util.HashMap;
import java.util.Objects;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.util.StringUtils;

public class HashMapConverter implements AttributeConverter<HashMap<String, String>, String> {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(HashMap<String, String> attribute) {
		if (Objects.isNull(attribute)) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> convertToEntityAttribute(String dbData) {
		if (StringUtils.isEmpty(dbData)) {
			return null;
		}

		try {
			return objectMapper.readValue(dbData, HashMap.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
