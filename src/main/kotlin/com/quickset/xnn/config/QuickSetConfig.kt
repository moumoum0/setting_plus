package com.quickset.xnn.config

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

data class WorldEnvironmentSettings(
    var doDaylightCycle: Boolean = true,
    var doWeatherCycle: Boolean = true,
    var randomTickSpeed: Int = 3,
    var doFireTick: Boolean = true,
    var gameMode: String = "survival"
)

data class MobSettings(
    var mobGriefing: Boolean = true,
    var doMobSpawning: Boolean = true,
    var doInsomnia: Boolean = true,
    var maxEntityCramming: Int = 24
)

data class DamageSettings(
    var fallDamage: Boolean = true,
    var fireDamage: Boolean = true,
    var drowningDamage: Boolean = true,
    var freezeDamage: Boolean = true,
    var naturalRegeneration: Boolean = true
)

data class PlayerExperienceSettings(
    var keepInventory: Boolean = false,
    var immediateRespawn: Boolean = false,
    var playersSleepingPercentage: Int = 100,
    var showDeathMessages: Boolean = true,
    var announceAdvancements: Boolean = true
)

data class VisualEffectSettings(
    var gamma: Double = 1.0,
    var fovEffectScale: Double = 1.0,
    var screenEffectScale: Double = 1.0,
    var darknessEffectScale: Double = 1.0,
    var entityDistanceScaling: Double = 1.0
)

data class QuickSetConfig(
    var worldEnvironment: WorldEnvironmentSettings = WorldEnvironmentSettings(),
    var mobSettings: MobSettings = MobSettings(),
    var damageSettings: DamageSettings = DamageSettings(),
    var playerExperience: PlayerExperienceSettings = PlayerExperienceSettings(),
    var visualEffects: VisualEffectSettings = VisualEffectSettings()
) {
    companion object {
        private val CONFIG_DIR = File(FabricLoader.getInstance().configDir.toFile(), "quickset")
        private val GSON = GsonBuilder().setPrettyPrinting().create()
        
        private var currentWorldName: String? = null
        private val configCache = mutableMapOf<String, QuickSetConfig>()
        
        private fun getConfigFile(worldName: String?): File {
            CONFIG_DIR.mkdirs()
            val fileName = if (worldName != null) "$worldName.json" else "default.json"
            return File(CONFIG_DIR, fileName)
        }
        
        fun load(worldName: String? = null): QuickSetConfig {
            val name = worldName ?: getCurrentWorldName()
            
            if (configCache.containsKey(name)) {
                return configCache[name]!!
            }
            
            return forceLoad(worldName)
        }
        
        fun forceLoad(worldName: String? = null): QuickSetConfig {
            val name = worldName ?: getCurrentWorldName()
            val configFile = getConfigFile(name)
            val config = if (configFile.exists()) {
                try {
                    FileReader(configFile).use { reader ->
                        GSON.fromJson(reader, QuickSetConfig::class.java)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    QuickSetConfig()
                }
            } else {
                QuickSetConfig()
            }
            
            configCache[name] = config
            return config
        }
        
        fun save(config: QuickSetConfig, worldName: String? = null) {
            val name = worldName ?: getCurrentWorldName()
            val configFile = getConfigFile(name)
            
            try {
                CONFIG_DIR.mkdirs()
                FileWriter(configFile).use { writer ->
                    GSON.toJson(config, writer)
                }
                configCache[name] = config
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        private fun getCurrentWorldName(): String {
            val minecraft = net.minecraft.client.Minecraft.getInstance()
            return minecraft.level?.let { level ->
                // 尝试获取存档名称
                if (minecraft.isSingleplayer) {
                    minecraft.singleplayerServer?.worldData?.levelSettings?.levelName() ?: "default"
                } else {
                    // 多人游戏使用服务器地址
                    minecraft.currentServer?.ip ?: "multiplayer"
                }
            } ?: "default"
        }
        
        fun loadFromGame(): QuickSetConfig {
            val config = QuickSetConfig()
            val minecraft = net.minecraft.client.Minecraft.getInstance()
            
            val savedConfig = load()
            
            config.visualEffects.gamma = savedConfig.visualEffects.gamma
            config.visualEffects.fovEffectScale = savedConfig.visualEffects.fovEffectScale
            config.visualEffects.screenEffectScale = savedConfig.visualEffects.screenEffectScale
            config.visualEffects.darknessEffectScale = savedConfig.visualEffects.darknessEffectScale
            config.visualEffects.entityDistanceScaling = savedConfig.visualEffects.entityDistanceScaling
            
            val level = minecraft.level
            if (level != null) {
                val gameRules = level.gameRules
                
                config.worldEnvironment.doDaylightCycle = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_DAYLIGHT)
                config.worldEnvironment.doWeatherCycle = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_WEATHER_CYCLE)
                config.worldEnvironment.randomTickSpeed = gameRules.getInt(net.minecraft.world.level.GameRules.RULE_RANDOMTICKING)
                config.worldEnvironment.doFireTick = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_DOFIRETICK)
                
                val player = minecraft.player
                if (player != null) {
                    config.worldEnvironment.gameMode = when (player.abilities.instabuild) {
                        true -> if (player.abilities.mayfly) "creative" else "adventure"
                        false -> if (player.isSpectator) "spectator" else "survival"
                    }
                }
                
                config.mobSettings.mobGriefing = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING)
                config.mobSettings.doMobSpawning = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBSPAWNING)
                config.mobSettings.doInsomnia = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_DOINSOMNIA)
                config.mobSettings.maxEntityCramming = gameRules.getInt(net.minecraft.world.level.GameRules.RULE_MAX_ENTITY_CRAMMING)
                
                config.damageSettings.fallDamage = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_FALL_DAMAGE)
                config.damageSettings.fireDamage = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_FIRE_DAMAGE)
                config.damageSettings.drowningDamage = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_DROWNING_DAMAGE)
                config.damageSettings.freezeDamage = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_FREEZE_DAMAGE)
                config.damageSettings.naturalRegeneration = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_NATURAL_REGENERATION)
                
                config.playerExperience.keepInventory = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_KEEPINVENTORY)
                config.playerExperience.immediateRespawn = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_DO_IMMEDIATE_RESPAWN)
                config.playerExperience.playersSleepingPercentage = gameRules.getInt(net.minecraft.world.level.GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE)
                config.playerExperience.showDeathMessages = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_SHOWDEATHMESSAGES)
                config.playerExperience.announceAdvancements = gameRules.getBoolean(net.minecraft.world.level.GameRules.RULE_ANNOUNCE_ADVANCEMENTS)
            }
            
            return config
        }
    }
    
    fun applyVisualEffects() {
        val minecraft = net.minecraft.client.Minecraft.getInstance()
        val options = minecraft.options
        
        options.fovEffectScale().set(visualEffects.fovEffectScale)
        options.screenEffectScale().set(visualEffects.screenEffectScale)
        options.darknessEffectScale().set(visualEffects.darknessEffectScale)
        options.entityDistanceScaling().set(visualEffects.entityDistanceScaling)
        
        try {
            val gammaField = net.minecraft.client.OptionInstance::class.java.getDeclaredField("value")
            gammaField.isAccessible = true
            
            gammaField.set(options.gamma(), 1.0)
            options.save()
            
            gammaField.set(options.gamma(), visualEffects.gamma)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun applyGameRules(): Boolean {
        val minecraft = net.minecraft.client.Minecraft.getInstance()
        
        if (!minecraft.isSingleplayer) {
            minecraft.player?.displayClientMessage(
                net.minecraft.network.chat.Component.translatable("quickset.error.multiplayer"),
                false
            )
            return false
        }
        
        val server = minecraft.singleplayerServer ?: return false
        val serverLevel = server.overworld()
        val gameRules = serverLevel.gameRules
        
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_DAYLIGHT).set(this.worldEnvironment.doDaylightCycle, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_WEATHER_CYCLE).set(this.worldEnvironment.doWeatherCycle, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_RANDOMTICKING).set(this.worldEnvironment.randomTickSpeed, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_DOFIRETICK).set(this.worldEnvironment.doFireTick, server)
        
        val player = minecraft.player
        if (player != null) {
            val gameType = when (this.worldEnvironment.gameMode) {
                "creative" -> net.minecraft.world.level.GameType.CREATIVE
                "adventure" -> net.minecraft.world.level.GameType.ADVENTURE
                "spectator" -> net.minecraft.world.level.GameType.SPECTATOR
                else -> net.minecraft.world.level.GameType.SURVIVAL
            }
            server.playerList.getPlayer(player.uuid)?.setGameMode(gameType)
        }
        
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING).set(this.mobSettings.mobGriefing, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_DOMOBSPAWNING).set(this.mobSettings.doMobSpawning, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_DOINSOMNIA).set(this.mobSettings.doInsomnia, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_MAX_ENTITY_CRAMMING).set(this.mobSettings.maxEntityCramming, server)
        
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_FALL_DAMAGE).set(this.damageSettings.fallDamage, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_FIRE_DAMAGE).set(this.damageSettings.fireDamage, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_DROWNING_DAMAGE).set(this.damageSettings.drowningDamage, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_FREEZE_DAMAGE).set(this.damageSettings.freezeDamage, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_NATURAL_REGENERATION).set(this.damageSettings.naturalRegeneration, server)
        
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_KEEPINVENTORY).set(this.playerExperience.keepInventory, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(this.playerExperience.immediateRespawn, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE).set(this.playerExperience.playersSleepingPercentage, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_SHOWDEATHMESSAGES).set(this.playerExperience.showDeathMessages, server)
        gameRules.getRule(net.minecraft.world.level.GameRules.RULE_ANNOUNCE_ADVANCEMENTS).set(this.playerExperience.announceAdvancements, server)
        
        return true
    }
}
