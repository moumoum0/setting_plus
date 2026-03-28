package com.quickset.xnn

import com.quickset.xnn.config.QuickSetConfig
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Setting : ModInitializer {
    const val MOD_ID = "setting"
    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		logger.info("QuickSet+ 正在加载...")
		
		QuickSetConfig.load()
		
		logger.info("QuickSet+ 加载完成！")
	}
}