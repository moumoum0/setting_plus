package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class PlayerExperienceScreen(parent: Screen?, config: QuickSetConfig) : 
    BaseSettingsScreen(parent, Component.translatable("quickset.screen.player"), config) {
    
    override fun populateList(list: SettingsList) {
        list.addBoolEntry("quickset.player.keepInventory", { config.playerExperience.keepInventory }) { config.playerExperience.keepInventory = it }
        list.addBoolEntry("quickset.player.immediateRespawn", { config.playerExperience.immediateRespawn }) { config.playerExperience.immediateRespawn = it }
        list.addBoolEntry("quickset.player.showDeathMessages", { config.playerExperience.showDeathMessages }) { config.playerExperience.showDeathMessages = it }
        list.addBoolEntry("quickset.player.announceAdvancements", { config.playerExperience.announceAdvancements }) { config.playerExperience.announceAdvancements = it }
        list.addIntSliderEntry(
            "quickset.player.playersSleepingPercentage",
            0,
            100,
            { config.playerExperience.playersSleepingPercentage },
            suffix = "%"
        ) { config.playerExperience.playersSleepingPercentage = it }
    }
}
