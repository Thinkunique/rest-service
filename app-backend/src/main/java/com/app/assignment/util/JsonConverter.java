package com.app.assignment.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonConverter {

	private static GsonBuilder builder;

	public static GsonBuilder getBuilder() {
		if (builder == null) {
			builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			});
		}
		return builder;
	}

	public static Gson getGson()
	{
		GsonBuilder builder= getBuilder();
		Gson gson = builder.create();
		return gson;
	}

}
