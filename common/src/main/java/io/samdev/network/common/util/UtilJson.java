package io.samdev.network.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.samdev.network.common.database.Credentials;
import io.samdev.network.common.error.ErrorHandler;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * JSON parsing and serialization utilities
 */
@UtilityClass
public class UtilJson
{
    /**
     * The {@link Gson} instance
     */
    private static final Gson GSON = new GsonBuilder()
        .create();

    /**
     * The {@link JsonParser} instance
     */
    private static final JsonParser PARSER = new JsonParser();

    /**
     * Serializes an {@link Object} to a {@link JsonObject}
     *
     * @param object The object
     *
     * @return The JSON object
     */
    public static JsonObject serialize(Object object)
    {
        return GSON.toJsonTree(object).getAsJsonObject();
    }

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
        return GSON.fromJson(element, clazz);
    }

    /**
     * Parses a {@link JsonObject} from a {@link String}
     *
     * @see JsonParser#parse(String)
     *
     * @param string The string
     *
     * @return The parsed {@link JsonObject}
     */
    public static JsonObject parseObject(String string) throws JsonSyntaxException
    {
        return PARSER.parse(string).getAsJsonObject();
    }

    /**
     * Parses a {@link JsonObject} from a {@link File}
     *
     * @see JsonParser#parse(Reader)
     *
     * @param file The file
     *
     * @return The parsed {@link JsonObject}
     */
    public static JsonObject parseObject(File file)
    {
        try
        {
            return PARSER.parse(new FileReader(file)).getAsJsonObject();
        }
        catch (FileNotFoundException ex)
        {
            Logging.severe("Unable to find file " + file.getName());

            ErrorHandler.report(ex);
            return null;
        }
    }

    /**
     * The network configuration file as a JSON object
     */
    private static JsonObject networkConfig = parseObject(Paths.NETWORK_CONFIG);

    /**
     * Parses a {@link Credentials} object from
     * a section in the network configuration
     *
     * @see #networkConfig
     *
     * @param clazz The credentials class
     * @param key The key of the object in the config
     *
     * @return The credentials object
     */
    public static <T extends Credentials> T parseCredentials(Class<? extends T> clazz, String key)
    {
        JsonObject object = networkConfig.getAsJsonObject(key);
        return fromJson(clazz, object);
    }
}