package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Sockets.MODID);

    public static final RegistryObject<SoundEvent> TOASTER_IN = registerSoundEvents("toaster_in");
    public static final RegistryObject<SoundEvent> TOASTER_POP = registerSoundEvents("toaster_pop");
    public static final RegistryObject<SoundEvent> ENERGY_ZAP = registerSoundEvents("energy_zap");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Sockets.MODID, name)));
    }
}
