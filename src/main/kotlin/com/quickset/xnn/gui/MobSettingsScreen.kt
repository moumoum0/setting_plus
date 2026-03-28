package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class MobSettingsScreen(parent: Screen?, config: QuickSetConfig) : 
    BaseSettingsScreen(parent, Component.translatable("quickset.screen.mob"), config) {
    
    override fun populateList(list: SettingsList) {
        list.addBoolEntry("quickset.mob.mobGriefing", { config.mobSettings.mobGriefing }) { config.mobSettings.mobGriefing = it }
        list.addBoolEntry("quickset.mob.doMobSpawning", { config.mobSettings.doMobSpawning }) { config.mobSettings.doMobSpawning = it }
        list.addBoolEntry("quickset.mob.doInsomnia", { config.mobSettings.doInsomnia }) { config.mobSettings.doInsomnia = it }
        list.addIntSliderEntry(
            "quickset.mob.maxEntityCramming",
            0,
            64,
            { config.mobSettings.maxEntityCramming }
        ) { config.mobSettings.maxEntityCramming = it }
    }
}
