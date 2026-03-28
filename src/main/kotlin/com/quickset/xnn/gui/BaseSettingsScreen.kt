package com.quickset.xnn.gui

import com.quickset.xnn.config.QuickSetConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractSliderButton
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.CycleButton
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

abstract class BaseSettingsScreen(
    private val parent: Screen?,
    title: Component,
    protected val config: QuickSetConfig
) : Screen(title) {

    private var settingsList: SettingsList? = null

    override fun init() {
        super.init()

        addRenderableWidget(Button.builder(Component.translatable("quickset.apply")) {
            applySettings()
        }.bounds(width / 2 - 155, height - 28, 150, 20).build())

        addRenderableWidget(Button.builder(Component.translatable("gui.done")) {
            minecraft?.setScreen(parent)
        }.bounds(width / 2 + 5, height - 28, 150, 20).build())

        val list = SettingsList(minecraft!!, width, height - 28 - 32, 32, height - 28, 25)
        populateList(list)
        addWidget(list)
        settingsList = list
    }

    abstract fun populateList(list: SettingsList)

    private fun applySettings() {
        QuickSetConfig.save(config)
        config.applyVisualEffects()
        minecraft?.player?.displayClientMessage(Component.translatable("quickset.visual.applied"), false)
        if (config.applyGameRules()) {
            minecraft?.player?.displayClientMessage(Component.translatable("quickset.gamerule.applied"), false)
        }
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(graphics)
        settingsList?.render(graphics, mouseX, mouseY, partialTick)
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF)
        super.render(graphics, mouseX, mouseY, partialTick)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, delta: Double): Boolean {
        return settingsList?.mouseScrolled(mouseX, mouseY, delta) ?: false || super.mouseScrolled(mouseX, mouseY, delta)
    }

    inner class SettingsList(
        mc: Minecraft,
        width: Int,
        height: Int,
        topY: Int,
        bottomY: Int,
        itemHeight: Int
    ) : ContainerObjectSelectionList<SettingsList.Entry>(mc, width, height, topY, bottomY, itemHeight) {

        fun addBoolEntry(key: String, getter: () -> Boolean, setter: (Boolean) -> Unit) {
            addEntry(BoolEntry(key, getter, setter))
        }

        fun addIntSliderEntry(
            key: String,
            minValue: Int,
            maxValue: Int,
            getter: () -> Int,
            suffix: String = "",
            setter: (Int) -> Unit
        ) {
            addEntry(IntSliderEntry(key, minValue, maxValue, getter, suffix, setter))
        }

        fun addDoubleSliderEntry(
            key: String,
            minValue: Double,
            maxValue: Double,
            getter: () -> Double,
            setter: (Double) -> Unit
        ) {
            addEntry(DoubleSliderEntry(key, minValue, maxValue, getter, setter))
        }

        fun addEnumEntry(
            key: String,
            values: List<String>,
            getter: () -> String,
            setter: (String) -> Unit,
            valueTranslationKey: ((String) -> String)? = null
        ) {
            addEntry(EnumEntry(key, values, getter, setter, valueTranslationKey))
        }

        abstract inner class Entry : ContainerObjectSelectionList.Entry<Entry>()

        inner class BoolEntry(
            private val key: String,
            private val getter: () -> Boolean,
            private val setter: (Boolean) -> Unit
        ) : Entry() {
            private val btn = CycleButton.booleanBuilder(
                Component.translatable("quickset.on"),
                Component.translatable("quickset.off")
            ).withInitialValue(getter())
                .create(0, 0, 150, 20, Component.translatable(key)) { _, v -> setter(v) }

            override fun render(graphics: GuiGraphics, index: Int, top: Int, left: Int, w: Int, h: Int, mouseX: Int, mouseY: Int, hovered: Boolean, delta: Float) {
                btn.x = left + w / 2 - 75
                btn.y = top
                btn.render(graphics, mouseX, mouseY, delta)
            }
            override fun children(): List<GuiEventListener> = listOf(btn)
            override fun narratables(): List<NarratableEntry> = listOf(btn)
        }

        inner class IntSliderEntry(
            private val key: String,
            private val minValue: Int,
            private val maxValue: Int,
            private val getter: () -> Int,
            private val suffix: String,
            private val setter: (Int) -> Unit
        ) : Entry() {
            private var currentValue = getter()
            private val slider = object : AbstractSliderButton(0, 0, 150, 20, Component.empty(), 0.0) {
                init {
                    value = (currentValue - minValue).toDouble() / (maxValue - minValue).toDouble()
                    updateMessage()
                }

                override fun updateMessage() {
                    val displayValue = minValue + (value * (maxValue - minValue)).toInt()
                    message = Component.translatable(key).append(": $displayValue$suffix")
                }

                override fun applyValue() {
                    currentValue = minValue + (value * (maxValue - minValue)).toInt()
                    setter(currentValue)
                }
            }

            override fun render(graphics: GuiGraphics, index: Int, top: Int, left: Int, w: Int, h: Int, mouseX: Int, mouseY: Int, hovered: Boolean, delta: Float) {
                slider.x = left + w / 2 - 75
                slider.y = top
                slider.render(graphics, mouseX, mouseY, delta)
            }
            override fun children(): List<GuiEventListener> = listOf(slider)
            override fun narratables(): List<NarratableEntry> = listOf(slider)
        }

        inner class DoubleSliderEntry(
            private val key: String,
            private val minValue: Double,
            private val maxValue: Double,
            private val getter: () -> Double,
            private val setter: (Double) -> Unit
        ) : Entry() {
            private var currentValue = getter()
            private val slider = object : AbstractSliderButton(0, 0, 150, 20, Component.empty(), 0.0) {
                init {
                    value = (currentValue - minValue) / (maxValue - minValue)
                    updateMessage()
                }

                override fun updateMessage() {
                    val displayValue = minValue + value * (maxValue - minValue)
                    message = Component.translatable(key).append(": ${String.format("%.1f", displayValue)}")
                }

                override fun applyValue() {
                    currentValue = minValue + value * (maxValue - minValue)
                    setter(currentValue)
                }
            }

            override fun render(graphics: GuiGraphics, index: Int, top: Int, left: Int, w: Int, h: Int, mouseX: Int, mouseY: Int, hovered: Boolean, delta: Float) {
                slider.x = left + w / 2 - 75
                slider.y = top
                slider.render(graphics, mouseX, mouseY, delta)
            }
            override fun children(): List<GuiEventListener> = listOf(slider)
            override fun narratables(): List<NarratableEntry> = listOf(slider)
        }

        inner class EnumEntry(
            private val key: String,
            private val values: List<String>,
            private val getter: () -> String,
            private val setter: (String) -> Unit,
            private val valueTranslationKey: ((String) -> String)? = null
        ) : Entry() {
            private val btn = CycleButton.builder<String> { 
                Component.translatable(valueTranslationKey?.invoke(it) ?: "$key.$it") 
            }
                .withValues(values)
                .withInitialValue(getter())
                .create(0, 0, 150, 20, Component.translatable(key)) { _, v -> setter(v) }

            override fun render(graphics: GuiGraphics, index: Int, top: Int, left: Int, w: Int, h: Int, mouseX: Int, mouseY: Int, hovered: Boolean, delta: Float) {
                btn.x = left + w / 2 - 75
                btn.y = top
                btn.render(graphics, mouseX, mouseY, delta)
            }
            override fun children(): List<GuiEventListener> = listOf(btn)
            override fun narratables(): List<NarratableEntry> = listOf(btn)
        }
    }
}
