package com.secretescapes.codingChallenge.modules;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.inject.AbstractModule;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GsonModule extends AbstractModule {

    @Override
    protected void configure() {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            @Override
            public void write(JsonWriter jsonWriter, LocalDateTime o) throws IOException {
                jsonWriter.value(o.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }

            @Override
            public LocalDateTime read(JsonReader jsonReader) throws IOException {
                return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        }).create();

        bind(Gson.class).toInstance(gson);
    }
}
