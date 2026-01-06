# Epic Core API MCreator Plugin

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![MCreator](https://img.shields.io/badge/MCreator-2024.4-orange.svg)](https://mcreator.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)

[English](#english) | [中文](#中文)

---

## English

### About

I created this plugin to provide convenient and powerful entity manipulation APIs for MCreator developers. Although I rarely use MCreator to create mods anymore, I still want to help those who continue using MCreator and struggle with entity-related operations.

This plugin integrates [Epic Core API](https://github.com/CJiangqiu/EpicCoreAPI) as a dependency and provides several entity manipulation procedure blocks that would otherwise be difficult or impossible to implement in vanilla MCreator.

### Features

- **Force Kill Entity** - Instantly kill an entity with proper damage source handling
- **Force Set Health** - Set entity health using multi-phase smart modification
- **Force Revive** - Revive dead entities by clearing death state
- **Set Invulnerable** - Toggle strong invulnerability with automatic health locking
- **Force Remove Entity** - Completely remove entities with deep cleanup

All procedure blocks are located in the **"Epic Core API"** category (purple) in the procedure editor.

### Requirements

- **MCreator 2024.4**
- **Minecraft Forge 1.20.1**
- **Epic Core API Dev JAR** (for development)
- **Epic Core API Mod** (runtime dependency for players)

### Installation for Developers

#### Step 1: Download the Dev Version

Download `eca-1.20.1-forge-1.0.5-fix-dev.jar` from [Epic Core API Releases](https://github.com/CJiangqiu/EpicCoreAPI/releases/tag/v1.0.5)

> **Important:** You must use the **Dev version** during development, otherwise you will encounter Mixin obfuscation issues when running the workspace.

#### Step 2: Install the Dev JAR

Place the dev jar file in:
```
<user home>/.mcreator/lib/eca-1.20.1-forge-1.0.5-fix-dev.jar
```

**Locations:**
- **Windows**: `C:\Users\<YourName>\.mcreator\lib\`
- **macOS/Linux**: `~/.mcreator/lib/`

Create the `lib` folder if it doesn't exist.

#### Step 3: Install the Plugin

1. Download the plugin ZIP file from releases
2. Place it in:
   ```
   <user home>/.mcreator/plugins/
   ```
3. Restart MCreator

#### Step 4: Enable Java Plugins

1. Open MCreator preferences (File → Preferences)
2. Go to the "Plugins" section
3. **Enable "Java Plugins" option**
4. Restart MCreator if prompted

#### Step 5: Configure Your Workspace

1. Open or create a workspace (Forge 1.20.1)
2. Go to **Workspace → Workspace settings → External APIs**
3. **Check "Epic Core API"** in the external dependencies list
4. Click **Save** and **regenerate code**
5. Wait for Gradle to sync

#### Step 6: Use the Procedure Blocks

1. Create a new procedure
2. In the procedure editor, find the **"Epic Core API"** category (purple color)
3. Drag and drop the blocks you need

### For Players (End Users)

All mods created with this plugin require [Epic Core API](https://www.curseforge.com/minecraft/mc-mods/epic-core-api) as a **mandatory dependency**.

Players must download and install the Epic Core API mod from CurseForge to use any mods built with this plugin.

### Links

- **Epic Core API Mod**: https://www.curseforge.com/minecraft/mc-mods/epic-core-api
- **Source Code**: https://github.com/CJiangqiu/EpicCoreAPI
- **Dev Releases**: https://github.com/CJiangqiu/EpicCoreAPI/releases

### License

This plugin is based on [MCreator Demo Plugin](https://github.com/Pylo/MCreatorDemoPlugin) by Pylo.

MIT License - See [LICENSE](LICENSE) file for details.

---

## 中文

### 关于

我创建这个插件是为了提供一些便捷且强大的实体操作 API。尽管现在我几乎不再使用 MCreator 制作 Mod，但我仍然想要去帮助那些依然使用 MCreator 且一直苦恼于实体相关操作的开发者。

这个插件将 [Epic Core API](https://github.com/CJiangqiu/EpicCoreAPI) Mod 添加为依赖，并提供了一些在原版 MCreator 中难以或无法实现的实体操作流程块。

### 功能特性

- **强制杀死实体** - 使用正确的伤害源立即杀死实体
- **强制设置生命值** - 使用多阶段智能修改设置实体生命值
- **强制复活实体** - 通过清除死亡状态复活实体
- **设置无敌状态** - 切换强无敌状态并自动锁定生命值
- **强制移除实体** - 完整清除实体并进行深度清理

所有流程块位于流程编辑器中的 **"Epic Core API"** 分类（紫色）。

### 环境要求

- **MCreator 2024.4**
- **Minecraft Forge 1.20.1**
- **Epic Core API Dev JAR**（开发时需要）
- **Epic Core API Mod**（玩家运行时依赖）

### 开发者安装教程

#### 第 1 步：下载 Dev 版本

从 [Epic Core API Releases](https://github.com/CJiangqiu/EpicCoreAPI/releases/tag/v1.0.5) 下载 `eca-1.20.1-forge-1.0.5-fix-dev.jar`

> **重要提示：** 开发时必须使用 **Dev 版本**，否则在运行工作区时会遇到 Mixin 混淆问题。

#### 第 2 步：安装 Dev JAR

将 dev jar 文件放置在：
```
<用户目录>/.mcreator/lib/eca-1.20.1-forge-1.0.5-fix-dev.jar
```

**路径参考：**
- **Windows**: `C:\Users\<你的用户名>\.mcreator\lib\`
- **macOS/Linux**: `~/.mcreator/lib/`

如果 `lib` 文件夹不存在，请手动创建。

#### 第 3 步：安装插件

1. 下载插件 ZIP 文件
2. 将其放置在：
   ```
   <用户目录>/.mcreator/plugins/
   ```
3. 重启 MCreator

#### 第 4 步：启用 Java 插件

1. 打开 MCreator 首选项（文件 → 首选项）
2. 进入"插件"部分
3. **启用"Java 插件"选项**
4. 如果提示，重启 MCreator

#### 第 5 步：配置工作区

1. 打开或创建一个工作区（Forge 1.20.1）
2. 进入 **工作区 → 工作区设置 → 外部 API**
3. 在外部依赖列表中**勾选"Epic Core API"**
4. 点击**保存**并**重新生成代码**
5. 等待 Gradle 同步完成

#### 第 6 步：使用流程块

1. 创建一个新流程
2. 在流程编辑器中，找到 **"Epic Core API"** 分类（紫色）
3. 拖放你需要的流程块

### 玩家须知（最终用户）

所有使用该插件制作的 Mod 都需要将 [Epic Core API](https://www.curseforge.com/minecraft/mc-mods/epic-core-api) 作为**必要的依赖**。

玩家必须从 CurseForge 下载并安装 Epic Core API mod，才能使用基于此插件构建的任何 Mod。

### 相关链接

- **Epic Core API Mod**: https://www.curseforge.com/minecraft/mc-mods/epic-core-api
- **源代码**: https://github.com/CJiangqiu/EpicCoreAPI
- **Dev 版本发布**: https://github.com/CJiangqiu/EpicCoreAPI/releases

### 许可证

本插件基于 Pylo 的 [MCreator Demo Plugin](https://github.com/Pylo/MCreatorDemoPlugin) 修改。

MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。
