package io.samdev.network.common.database.mongo;

import org.mongodb.morphia.ObjectFactory;
import org.mongodb.morphia.mapping.DefaultCreator;
import org.mongodb.morphia.mapping.MappingException;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ObjectFactory} implementation for
 * creating instances of classes without
 * the need of a no-args constructor
 */
class NetworkObjectFactory extends DefaultCreator
{
    NetworkObjectFactory()
    {
        try
        {
            objectClassConstructor = Object.class.getConstructor();
        } catch (NoSuchMethodException ignored) {}
    }

    /**
     * The empty {@link Object} class constructor
     */
    private Constructor<Object> objectClassConstructor;

    /**
     * Map of classes to their generated empty constructors
     */
    private final Map<Class<?>, Constructor<?>> constructorCache = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createInstance(Class<T> clazz)
    {
        try
        {
            return (T) constructorCache.computeIfAbsent(clazz, this::generateConstructor).newInstance();
        }
        catch (ReflectiveOperationException ex)
        {
            throw new MappingException("Unable to generate constructor for " + clazz.getName());
        }
    }

    /**
     * Generates and returns an
     * empty constructor for a class
     *
     * @param clazz The class
     *
     * @return The constructor
     */
    @SuppressWarnings("unchecked")
    private <T> Constructor<T> generateConstructor(Class<T> clazz)
    {
        return (Constructor<T>) ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, objectClassConstructor);
    }
}
