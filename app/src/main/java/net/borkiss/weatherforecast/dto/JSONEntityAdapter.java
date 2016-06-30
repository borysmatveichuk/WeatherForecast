package net.borkiss.weatherforecast.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public abstract class JSONEntityAdapter<T> {

    /**
     * Method create custom Gson object with specific featured for
     * DTO, such as Date marshaling/unmarshaling from/to seconds
     *
     */
    protected Gson buildParser() {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, Type type,
                                    JsonDeserializationContext context)
                    throws JsonParseException {
                // Date in seconds
                return new Date(json.getAsJsonPrimitive().getAsLong()*1000L);
            }
        });

        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {

            @Override
            public JsonElement serialize(Date date, Type arg1,
                                         JsonSerializationContext arg2) {
                // Date in seconds
                return new JsonPrimitive(date.getTime()/1000L);
            }

        });

        return builder.create();
    }

    /**
     * Method which should implement concrete realization for entity class
     *
     * @return Class type of entity for which adapter is implemented
     */
    protected abstract Class<T> getType();

    protected abstract Type getCollectionType();

    public T createFromJSONString(String json) {
        Gson gson = buildParser();
        return gson.fromJson(json, getType());
    }

    public List<T> createListFromJSON(String json) {
        Gson gson = buildParser();
        return gson.fromJson(json, getCollectionType());
    }

    public String createJSONString(T object) {
        Gson gson = buildParser();
        return gson.toJson(object);
    }

    public String createJSONStringFromList(List<T> objects) {
        Gson gson = buildParser();
        return gson.toJson(objects);
    }
}
