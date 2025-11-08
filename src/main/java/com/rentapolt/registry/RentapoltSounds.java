package com.rentapolt.registry;

import com.rentapolt.RentapoltMod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class RentapoltSounds {
    public static final SoundEvent CHAOS_EXPLOSION = register("chaos_explosion");
    public static final SoundEvent IONIC_LIGHTNING = register("ionic_lightning");
    public static final SoundEvent PORTAL_WOOSH = register("portal_woosh");
    public static final SoundEvent MUTANT_MUSIC = register("music.mutant_zone");
    public static final SoundEvent PLASMA_BEAM = register("plasma_beam");

    public static void register() {
        // class load
    }

    private static SoundEvent register(String name) {
        Identifier id = RentapoltMod.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
