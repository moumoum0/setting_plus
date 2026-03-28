# QuickSet+ 快捷设置模组

一个Minecraft Java版Fabric模组，将常用的GameRule和客户端隐藏设置整合到一个便捷的GUI界面中。

## 功能特性

设置按照功能类型分为5个分类，方便快速找到需要的选项：

### � 世界环境
控制世界运行的基础规则：
- **昼夜循环** (doDaylightCycle) - 停止或启用时间流动
- **天气循环** (doWeatherCycle) - 控制天气变化
- **随机刻速度** (randomTickSpeed) - 控制作物生长、树叶腐烂等速度

### 🐾 生物设置
与生物行为和生成相关的设置：
- **生物破坏方块** (mobGriefing) - 控制苦力怕、末影人等生物是否能破坏方块
- **生物生成** (doMobSpawning) - 控制自然刷怪
- **幻翼生成** (doInsomnia) - 控制幻翼是否生成
- **最大实体拥挤数** (maxEntityCramming) - 控制同一位置最多能容纳多少实体

### ⚔️ 伤害设置
各种伤害类型的开关：
- **摔落伤害** (fallDamage) - 控制摔落伤害
- **火焰伤害** (fireDamage) - 控制火焰伤害
- **溺水伤害** (drowningDamage) - 控制溺水伤害
- **冰冻伤害** (freezeDamage) - 控制冰冻伤害
- **自然生命恢复** (naturalRegeneration) - 控制饥饿值自动回血
- **火焰蔓延** (doFireTick) - 控制火焰传播

### 👤 玩家体验
玩家游戏体验相关的设置：
- **死亡不掉落** (keepInventory) - 死亡时保留物品
- **立即重生** (immediateRespawn) - 死亡后跳过重生界面
- **死亡消息** (showDeathMessages) - 显示死亡消息
- **进度公告** (announceAdvancements) - 公告玩家获得的进度
- **睡觉跳过比例** (playersSleepingPercentage) - 需要多少百分比的玩家睡觉才能跳过夜晚

### �️ 视觉效果
客户端视觉相关的隐藏设置，无需手动编辑`options.txt`：
- **伽马值 (Gamma)** - 最常用！调整亮度实现夜视效果（1.0-15.0）
- **视野效果缩放** (fovEffectScale) - 控制速度效果对视野的影响
- **屏幕效果缩放** (screenEffectScale) - 控制恶心等屏幕效果
- **黑暗效果缩放** (darknessEffectScale) - 控制黑暗效果的强度
- **实体距离缩放** (entityDistanceScaling) - 控制实体渲染距离

## 使用方法

### 打开设置界面
1. **按键绑定**: 按 `K` 键（可在控制设置中修改）
2. **设置菜单**: 在游戏设置界面中点击"快捷设置+"按钮

### 导航和设置
1. 在主菜单中选择你需要调整的设置分类（世界环境、生物设置、伤害设置、玩家体验、视觉效果）
2. 点击进入对应的设置界面
3. 调整你需要的设置
4. 点击"应用设置"按钮，设置会立即生效并保存
5. 点击"完成"返回上一级菜单

## 注意事项

- **GameRule设置**仅在单人世界或拥有OP权限时可用
- **客户端设置**随时可用，仅影响本地客户端
- 所有设置会自动保存到配置文件 `config/quickset.json`

## 技术信息

- **Minecraft版本**: 1.20.1
- **Mod加载器**: Fabric
- **开发语言**: Kotlin
- **依赖**: Fabric API, Fabric Language Kotlin

## 为什么需要这个模组？

在原版Minecraft中，很多常用设置需要：
- 输入复杂的`/gamerule`指令
- 手动编辑`options.txt`文件
- 记住各种参数名称和数值

QuickSet+将这些设置整合到一个友好的GUI界面中，让你可以：
- ✅ 一键切换常用设置
- ✅ 无需记忆指令
- ✅ 设置自动保存
- ✅ 支持中英文界面

## 许可证

CC0-1.0
