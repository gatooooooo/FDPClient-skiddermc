/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.handler.irc.packet

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import me.zywl.fdpclient.config.FileManager.Companion.PRETTY_GSON
import net.ccbluex.liquidbounce.handler.irc.packet.packets.Packet
import java.lang.reflect.Type

/**
 * Packet Serializer
 *
 * Allows to serialize packets from class to json
 */
class PacketSerializer : JsonSerializer<Packet> {

    private val packetRegistry = hashMapOf<Class<out Packet>, String>()

    /**
     * Register packet
     */
    fun registerPacket(packetName: String, packetClass: Class<out Packet>) {
        packetRegistry[packetClass] = packetName
    }

    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     *
     * In the implementation of this call-back method, you should consider invoking
     * [JsonSerializationContext.serialize] method to create JsonElements for any
     * non-trivial field of the `src` object. However, you should never invoke it on the
     * `src` object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @return a JsonElement corresponding to the specified object.
     */
    override fun serialize(src: Packet, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val packetName = packetRegistry.getOrDefault(src.javaClass, "UNKNOWN")
        val serializedPacket = SerializedPacket(packetName, if(src.javaClass.constructors.none { it.parameterCount != 0 }) null else src )

        return PRETTY_GSON.toJsonTree(serializedPacket)
    }

}