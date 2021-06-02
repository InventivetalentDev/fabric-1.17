package net.fabricmc.example.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets="net.minecraft.network.NetworkState$PacketHandler")
public interface NetworkState_PacketHandlerAccessor {

    @Accessor("packetIds")
    Object2IntMap<Class<? extends Packet>> getPacketIds();

}
