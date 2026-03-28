package com.quickset.xnn.mixin;

import com.quickset.xnn.gui.QuickSetScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    
    protected OptionsScreenMixin(Component title) {
        super(title);
    }
    
    @Inject(method = "init", at = @At("RETURN"))
    private void addQuickSetButton(CallbackInfo ci) {
        this.addRenderableWidget(Button.builder(
            Component.translatable("quickset.title"),
            button -> {
                if (this.minecraft != null) {
                    this.minecraft.setScreen(new QuickSetScreen(this));
                }
            }
        ).bounds(this.width / 2 - 155, this.height / 6 + 168, 150, 20).build());
    }
}
