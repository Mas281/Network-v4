package io.samdev.network.common.database.redis.serialize;

import com.google.gson.JsonObject;
import io.samdev.network.common.database.redis.NetworkPacket;
import io.samdev.network.common.util.UtilJson;

/**
 * Class used to serialize {@link NetworkPacket}
 * instances into JSON strings
 */
public class PacketSerializer
{
    /**
     * Serializes a {@link NetworkPacket}
     * into a JSON string for publishing
     *
     * @param packet The packet
     */
    public String serialize(NetworkPacket packet)
    {
        JsonObject object = new JsonObject();

        object.addProperty("class", packet.getClass().getCanonicalName());
        object.add("data", UtilJson.serialize(packet));

        return object.toString();
    }
}
