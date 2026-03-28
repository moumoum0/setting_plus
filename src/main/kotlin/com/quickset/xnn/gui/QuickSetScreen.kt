package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class QuickSetScreen(private val parent: Screen?) : Screen(Component.translatable("quickset.title")) {

    private val config = QuickSetConfig.forceLoad()

    override fun init() {
        super.init()

        val buttonWidth = 200
        val buttonHeight = 20
        val spacing = 24
        val startY = height / 6 + 24

        addRenderableWidget(Button.builder(Component.translatable("quickset.button.world")) {
            minecraft?.setScreen(WorldEnvironmentScreen(this, config))
        }.bounds(width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight).build())

        addRenderableWidget(Button.builder(Component.translatable("quickset.button.mob")) {
            minecraft?.setScreen(MobSettingsScreen(this, config))
        }.bounds(width / 2 - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight).build())

        addRenderableWidget(Button.builder(Component.translatable("quickset.button.damage")) {
            minecraft?.setScreen(DamageSettingsScreen(this, config))
        }.bounds(width / 2 - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight).build())

        addRenderableWidget(Button.builder(Component.translatable("quickset.button.player")) {
            minecraft?.setScreen(PlayerExperienceScreen(this, config))
        }.bounds(width / 2 - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight).build())

        addRenderableWidget(Button.builder(Component.translatable("quickset.button.visual")) {
            minecraft?.setScreen(VisualEffectsScreen(this, config))
        }.bounds(width / 2 - buttonWidth / 2, startY + spacing * 4, buttonWidth, buttonHeight).build())

        addRenderableWidget(Button.builder(Component.translatable("gui.done")) {
            minecraft?.setScreen(parent)
        }.bounds(width / 2 - 100, height - 28, 200, 20).build())
    }


    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(graphics)
        graphics.drawCenteredString(font, title, width / 2, 20, 0xFFFFFF)
        super.render(graphics, mouseX, mouseY, partialTick)
    }
}
