package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Sockets.MODID);

    public static final RegistryObject<SimpleParticleType> ENERGY_SPARK =
            PARTICLE_TYPES.register("energy_spark", () -> new SimpleParticleType(true));
}
