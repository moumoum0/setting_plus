package com.quickset.xnn

import com.quickset.xnn.config.QuickSetConfig
import com.quickset.xnn.gui.QuickSetScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.Screens
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW

object SettingClient : ClientModInitializer {
    
    private lateinit var openSettingsKey: KeyMapping
    private var lastLevel: net.minecraft.client.multiplayer.ClientLevel? = null
    private var hasAppliedGameRules = false
    
    override fun onInitializeClient() {
        openSettingsKey = KeyBindingHelper.registerKeyBinding(
            KeyMapping(
                "key.quickset.open",
                GLFW.GLFW_KEY_K,
                "key.categories.misc"
            )
        )
        
        // 使用 ScreenEvents API 添加按钮到选项界面
        ScreenEvents.AFTER_INIT.register { client, screen, scaledWidth, scaledHeight ->
            if (screen is OptionsScreen) {
                val buttons = Screens.getButtons(screen)
                
                // 找到左侧列最下方的按钮（遥感数据或鸣谢按钮）
                var maxLeftY = 0
                for (button in buttons) {
                    // 左侧列的按钮 x 坐标是 width/2 - 155
                    if (button.x == screen.width / 2 - 155 && button.y > maxLeftY) {
                        maxLeftY = button.y
                    }
                }
                
                // 在左侧列最下方按钮下方 24 像素处添加快捷设置按钮
                val quickSetButtonY = maxLeftY + 24
                buttons.add(
                    Button.builder(
                        Component.translatable("quickset.title")
                    ) { _ ->
                        client.setScreen(QuickSetScreen(screen))
                    }.bounds(screen.width / 2 - 155, quickSetButtonY, 150, 20).build()
                )
                
                // 找到"完成"按钮并将其下移
                for (button in buttons) {
                    if (button.message.string.contains("完成") || button.message.string.contains("Done")) {
                        button.y = quickSetButtonY + 24
                        break
                    }
                }
            }
        }
        
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (openSettingsKey.consumeClick()) {
                client.setScreen(QuickSetScreen(client.screen))
            }
            
            // 检测世界变化，在进入世界时自动应用配置
            val currentLevel = client.level
            if (currentLevel != lastLevel) {
                lastLevel = currentLevel
                hasAppliedGameRules = false
                
                if (currentLevel != null) {
                    val config = QuickSetConfig.forceLoad()
                    config.applyVisualEffects()
                }
            }
            
            // 单人游戏中，等待服务器完全初始化后再应用GameRule
            if (client.isSingleplayer && !hasAppliedGameRules && client.level != null) {
                val server = client.singleplayerServer
                if (server != null && server.isReady) {
                    val config = QuickSetConfig.forceLoad()
                    config.applyGameRules()
                    hasAppliedGameRules = true
                }
            }
        }
    }
}
