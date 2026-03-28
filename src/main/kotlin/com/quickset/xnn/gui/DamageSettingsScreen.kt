package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class DamageSettingsScreen(parent: Screen?, config: QuickSetConfig) : 
    BaseSettingsScreen(parent, Component.translatable("quickset.screen.damage"), config) {
    
    override fun populateList(list: SettingsList) {
        list.addBoolEntry("quickset.damage.fallDamage", { config.damageSettings.fallDamage }) { config.damageSettings.fallDamage = it }
        list.addBoolEntry("quickset.damage.fireDamage", { config.damageSettings.fireDamage }) { config.damageSettings.fireDamage = it }
        list.addBoolEntry("quickset.damage.drowningDamage", { config.damageSettings.drowningDamage }) { config.damageSettings.drowningDamage = it }
        list.addBoolEntry("quickset.damage.freezeDamage", { config.damageSettings.freezeDamage }) { config.damageSettings.freezeDamage = it }
        list.addBoolEntry("quickset.damage.naturalRegeneration", { config.damageSettings.naturalRegeneration }) { config.damageSettings.naturalRegeneration = it }
    }
}
