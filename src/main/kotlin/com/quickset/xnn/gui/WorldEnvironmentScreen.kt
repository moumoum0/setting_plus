package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class WorldEnvironmentScreen(parent: Screen?, config: QuickSetConfig) : 
    BaseSettingsScreen(parent, Component.translatable("quickset.screen.world"), config) {
    
    override fun populateList(list: SettingsList) {
        list.addEnumEntry(
            "quickset.world.gameMode",
            listOf("survival", "creative", "adventure", "spectator"),
            { config.worldEnvironment.gameMode },
            { config.worldEnvironment.gameMode = it },
            { "gameMode.$it" }
        )
        list.addBoolEntry("quickset.world.doDaylightCycle", { config.worldEnvironment.doDaylightCycle }) { config.worldEnvironment.doDaylightCycle = it }
        list.addBoolEntry("quickset.world.doWeatherCycle", { config.worldEnvironment.doWeatherCycle }) { config.worldEnvironment.doWeatherCycle = it }
        list.addBoolEntry("quickset.world.doFireTick", { config.worldEnvironment.doFireTick }) { config.worldEnvironment.doFireTick = it }
        list.addIntSliderEntry(
            "quickset.world.randomTickSpeed",
            0,
            100,
            { config.worldEnvironment.randomTickSpeed }
        ) { config.worldEnvironment.randomTickSpeed = it }
    }
}
