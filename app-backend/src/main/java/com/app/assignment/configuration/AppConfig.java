package com.app.assignment.configuration;

import java.lang.reflect.Type;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@Configuration
public class AppConfig {

	@Bean
	public GsonBuilder getBuilder() {
		GsonBuilder	builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			});
		return builder;
	}

	@Bean
	public Gson getGson()
	{
		GsonBuilder builder= getBuilder();
		Gson gson = builder.create();
		return gson;
	}
	
}
