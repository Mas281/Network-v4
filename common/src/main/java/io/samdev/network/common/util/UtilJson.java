package io.samdev.network.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@UtilityClass
public class UtilJson
{
    /**
     * The {@link Gson} instance
     */
    private static final Gson gson = new GsonBuilder()
        .create();

    /**
     * The {@link JsonParser} instance
     */
    private static final JsonParser parser = new JsonParser();

    /**
     * Parses an object of type {@link T}
     * from a {@link JsonElement}
     *
     * @param clazz The class to produce an instance of
     * @param <T> The class type
     *
     * @return The parsed object
     */
    public static <T> T fromJson(Class<T> clazz, JsonElement element)
    {
        return gson.fromJson(element, clazz);
    }

    /**
     * Parses a {@link JsonObject} from a {@link File}
     *
     * @param file The file
     *
     * @return The {@link JsonObject}
     */
    public static JsonObject parse(File file)
    {
        try
        {
            return parser.parse(new FileReader(file)).getAsJsonObject();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}