package net.fabricmc.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.mixin.NetworkStateAccessor;
import net.fabricmc.example.mixin.NetworkState_PacketHandlerAccessor;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkState;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExampleMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("========");
		System.out.println("========");
		System.out.println("========");
		System.out.println("Hello Fabric world!");
		System.out.println("========");
		System.out.println("========");
		System.out.println("========");


		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				JsonObject root = new JsonObject();
				for (NetworkState state : NetworkState.values()) {
					JsonObject json = new JsonObject();

					Map<NetworkSide, Object> map = ((NetworkStateAccessor) (Object) state).getPacketHandlers();
					for (Map.Entry<NetworkSide, Object> entry : map.entrySet()) {
						NetworkSide side = entry.getKey();
						System.out.println(entry.getValue());

						JsonObject obj = new JsonObject();
						obj.addProperty("side", side.name());

						List<JsonObject> list = new ArrayList<>();
						Object2IntMap<Class<? extends Packet>> packetIds = ((NetworkState_PacketHandlerAccessor) (Object) entry.getValue()).getPacketIds();
						for (Object2IntMap.Entry<Class<? extends Packet>> ent : packetIds.object2IntEntrySet()) {
							JsonObject o = new JsonObject();
							o.addProperty("n", ent.getIntValue());
							String x = Integer.toHexString( ent.getIntValue()).toUpperCase();
							if (x.length() == 1) {
								x="0"+x;
							}
							o.addProperty("id", "0x" + x);
							o.addProperty("name", ent.getKey().getSimpleName());

							list.add(o);
						}
						list.sort(new Comparator<JsonObject>() {
							@Override
							public int compare(JsonObject o1, JsonObject o2) {
								return Integer.compare(o1.get("n").getAsInt(), o2.get("n").getAsInt());
							}
						});
						JsonArray ids = new JsonArray();
						list.forEach(ids::add);
						obj.add("packets", ids);

						json.add(side.name(), obj);
					}

					root.add(state.name(), json);
				}

				System.out.println(root);
			}
		}).start();
	}

}
