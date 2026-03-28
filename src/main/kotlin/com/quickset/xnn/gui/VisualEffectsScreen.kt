package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class VisualEffectsScreen(parent: Screen?, config: QuickSetConfig) : 
    BaseSettingsScreen(parent, Component.translatable("quickset.screen.visual"), config) {
    
    override fun populateList(list: SettingsList) {
        list.addDoubleSliderEntry(
            "quickset.visual.gamma",
            1.0,
            15.0,
            { config.visualEffects.gamma }
        ) { config.visualEffects.gamma = it }
        list.addDoubleSliderEntry(
            "quickset.visual.fovEffectScale",
            0.0,
            1.0,
            { config.visualEffects.fovEffectScale }
        ) { config.visualEffects.fovEffectScale = it }
        list.addDoubleSliderEntry(
            "quickset.visual.screenEffectScale",
            0.0,
            1.0,
            { config.visualEffects.screenEffectScale }
        ) { config.visualEffects.screenEffectScale = it }
        list.addDoubleSliderEntry(
            "quickset.visual.darknessEffectScale",
            0.0,
            1.0,
            { config.visualEffects.darknessEffectScale }
        ) { config.visualEffects.darknessEffectScale = it }
        list.addDoubleSliderEntry(
            "quickset.visual.entityDistanceScaling",
            0.5,
            2.0,
            { config.visualEffects.entityDistanceScaling }
        ) { config.visualEffects.entityDistanceScaling = it }
    }
}
