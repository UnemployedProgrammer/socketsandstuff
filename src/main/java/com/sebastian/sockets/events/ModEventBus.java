package com.sebastian.sockets.events;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.particle.EnergySparkParticle;
import com.sebastian.sockets.reg.AllParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sockets.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBus {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(AllParticles.ENERGY_SPARK.get(), EnergySparkParticle::provider);
    }
}