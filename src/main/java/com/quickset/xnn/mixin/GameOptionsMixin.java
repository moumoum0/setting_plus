package com.quickset.xnn.mixin;

import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionInstance.class)
public interface GameOptionsMixin {
    @Accessor("value")
    Object quickset_getValue();

    @Accessor("value")
    void quickset_setValue(Object value);
}
