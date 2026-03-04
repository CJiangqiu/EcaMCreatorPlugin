# Epic Core API MCreator Plugin

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![MCreator](https://img.shields.io/badge/MCreator-2024.1+-orange.svg)](https://mcreator.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)

[English](#english) | [中文](#中文)

---

## English

### About

I created this plugin to provide convenient and powerful entity manipulation APIs for MCreator developers. Although I rarely use MCreator to create mods anymore, I still want to help those who continue using MCreator and struggle with entity-related operations.

This plugin integrates [Epic Core API](https://github.com/CJiangqiu/EpicCoreAPI) as a dependency and provides several entity manipulation procedure blocks that would otherwise be difficult or impossible to implement in vanilla MCreator.

### Procedure Blocks

- **Force Kill Entity** `<Entity>` - Set health to 0, trigger die(), drop loot, grant advancements, and remove the entity (death messages are not sent)
- **Force Set Health** `<Entity> <Health>` - Modify entity health through multi-phase process: vanilla fields, smart field scanning, and bytecode reverse tracking
- **Force Revive** `<Entity>` - Clear the entity's death flag and reset deathTime using VarHandle
- **Set Force Invulnerable** `<Entity> <Boolean>` - Enable/disable invulnerability with automatic health locking (locks health when enabled, unlocks when disabled)
- **Force Remove Entity** `<Entity>` - Deep cleanup including AI, boss bars, riding relationships, and all server/client containers
- **Lock Health** `<Entity> <Value>` - Lock health via bytecode hook (getHealth() returns locked value) and tick-based reset
- **Unlock Health** `<Entity>` - Remove health lock, allowing getHealth() to return actual health
- **Is Health Locked** `<Entity>` - Check if entity health is locked
- **Get Locked Health Value** `<Entity>` - Get the locked health value, or 0 if not locked
- **Force Get Health** `<Entity>` - Read real health directly from DATA_HEALTH_ID using VarHandle, bypassing custom implementations
- **Is Force Invulnerable** `<Entity>` - Check ECA invulnerability state via EntityData
- **Force Teleport** `<Entity> <X> <Y> <Z>` - Directly modify position fields using VarHandle with automatic client sync
- **Cleanup Boss Bar** `<Entity>` - Scan entity instance fields and remove all ServerBossEvent instances
- **Enable AllReturn** `<Entity>` ⚠️ **[DANGER]** - Requires config enabled. Transform all boolean/void methods in the entity's mod package
- **Disable AllReturn** - Turn off AllReturn and clear all transformation targets
- **Is AllReturn Enabled** - Check if AllReturn is active
- **Set Global AllReturn** `<Boolean>` ⚠️ **[DANGER]** - Requires config enabled. Enable/disable global AllReturn mode affecting ALL mods' boolean/void methods
- **Memory Remove Entity** `<Entity>` ⚠️ **[DANGER]** - Requires config enabled. Remove entity via LWJGL internal channel
- **Add Health Whitelist Keyword** `<Keyword>` - Add a keyword to health whitelist. Fields containing this keyword will be modified during health changes
- **Remove Health Whitelist Keyword** `<Keyword>` - Remove a keyword from health whitelist
- **Add Health Blacklist Keyword** `<Keyword>` - Add a keyword to health blacklist. Fields containing this keyword will be skipped during health changes
- **Remove Health Blacklist Keyword** `<Keyword>` - Remove a keyword from health blacklist
- **Add Spawn Ban** `<EntityType> <Seconds>` - Ban an entity type from spawning in the current dimension for specified time
- **Is Spawn Banned** `<EntityType>` - Check if an entity type is banned from spawning
- **Get Spawn Ban Time** `<EntityType>` - Get the remaining time (seconds) for an entity type's spawn ban
- **Clear Spawn Ban** `<EntityType>` - Clear the spawn ban for a specific entity type
- **Clear All Spawn Bans** - Clear all spawn bans in the current dimension
- **Add Protected Package** `<PackagePrefix>` - Add a package prefix to the whitelist. Classes in protected packages will not be affected by AllReturn
- **Remove Protected Package** `<PackagePrefix>` - Remove a package prefix from the whitelist. Built-in protections cannot be removed
- **Is Package Protected** `<ClassName>` - Check if a class name is protected by the whitelist
- **Set Max Health** `<Entity> <Value>` - Set precise max health value
- **Lock Max Health** `<Entity> <Value>` - Lock max health at a specific value
- **Unlock Max Health** `<Entity>` - Unlock max health
- **Is Max Health Locked** `<Entity>` - Check if max health is locked
- **Get Locked Max Health** `<Entity>` - Get locked max health value
- **Ban Healing** `<Entity> <Value>` - Ban healing for entity, locking health at value
- **Unban Healing** `<Entity>` - Remove healing ban
- **Is Healing Banned** `<Entity>` - Check if healing is banned
- **Get Heal Ban Value** `<Entity>` - Get the heal ban value
- **Lock Location** `<Entity>` - Lock entity at current position
- **Unlock Location** `<Entity>` - Unlock entity position
- **Is Location Locked** `<Entity>` - Check if entity position is locked
- **Set Global Fog** `<Color> <Mode> <Radius>` - Override dimension fog
- **Clear Global Fog** - Remove fog override
- **Set Global Skybox** `<Preset> <Alpha>` - Override dimension skybox with shader preset
- **Clear Global Skybox** - Remove skybox override
- **Set Global Music** `<Sound> <Source> <Volume> <Pitch> <Loop>` - Override dimension music
- **Clear Global Music** - Remove music override
- **Clear All Global Effects** - Remove all global effect overrides
- **For Each Entity in Dimension** - Iterate over all entities in the current dimension
- **For Each Typed Entity in Dimension** `<EntityType>` - Iterate over entities of a specific type in a dimension
- **For Each Entity on Server** - Iterate over all entities on the entire server
- **For Each Entity in Range** `<X> <Y> <Z> <Range>` - Iterate over entities within range

All procedure blocks are located in the **"Epic Core API"** category (purple) in the procedure editor.

### Entity Extension (Mod Element)

A new mod element type for visually enhancing specific entity types. Create an Entity Extension element to attach:

- **Custom Boss Bar** — Custom frame/fill textures with optional shader effects, configurable size and offset
- **Custom Health Display** — Override displayed health/max health values via procedures
- **Entity Layer** — Additional render layer with glow, hurt overlay, and alpha settings
- **Global Fog** — Custom fog color/distance, global or radius-based, with configurable shape
- **Global Skybox** — Custom skybox with texture and/or shader (12 built-in presets: TheLastEnd, DreamSakura, Forest, Ocean, Storm, Volcano, Arcane, Aurora, Hacker, Starlight, Cosmos, BlackHole), alpha, and size
- **Combat Music** — Custom combat music with source, volume, pitch, loop, and strict lock options

When multiple extension entities exist in the same dimension, the one with highest priority controls global effects.

### Requirements

- **MCreator 2024.1 or higher** (tested up to 2025.3)
  - **MCreator 2024.1-2024.4**: No additional setup needed (1.20.1 Forge generator built-in)
  - **MCreator 2025.1+**: Requires developers to manually install the 1.20.1 Forge generator plugin
- **Minecraft Forge 1.20.1**
- **Epic Core API Mod** (runtime dependency for players)

### Installation for Developers

#### Step 1: Install 1.20.1 Forge Generator (MCreator 2025.1+ Only)

**Skip this step if you're using MCreator 2024.1-2024.4** (1.20.1 Forge support is built-in)

If you're using **MCreator 2025.1 or higher**:

Visit the [MCreator Plugins page](https://mcreator.net/plugins) to find the **1.20.1 Forge generator plugin** for your MCreator version and install it alongside the ECA plugin.

#### Step 2: Install the Plugin

1. Download the plugin ZIP file from releases
2. Place it in:
   ```
   <user home>/.mcreator/plugins/
   ```
3. Restart MCreator

#### Step 3: Enable Java Plugins

1. Open MCreator preferences (File → Preferences)
2. Go to the "Plugins" section
3. **Enable "Java Plugins" option**
4. Restart MCreator if prompted

#### Step 4: Configure Your Workspace

1. Open or create a workspace (Forge 1.20.1)
2. Go to **Workspace → Workspace settings → External APIs**
3. **Check "Epic Core API"** in the external dependencies list
4. Click **Save** and **regenerate code**
5. Wait for Gradle to sync

#### Step 5: Use the Procedure Blocks

1. Create a new procedure
2. In the procedure editor, find the **"Epic Core API"** category (purple color)
3. Drag and drop the blocks you need

### For Players

All mods created with this plugin require [Epic Core API](https://www.curseforge.com/minecraft/mc-mods/epic-core-api) as a **mandatory dependency**.

Players must download and install the Epic Core API mod from CurseForge to use any mods built with this plugin.

### Links

- **Epic Core API Mod**: https://www.curseforge.com/minecraft/mc-mods/epic-core-api
- **Source Code**: https://github.com/CJiangqiu/EpicCoreAPI

### License

This plugin is based on [MCreator Demo Plugin](https://github.com/Pylo/MCreatorDemoPlugin) by Pylo.

MIT License - See [LICENSE](LICENSE) file for details.

---

## 中文

### 关于

我创建这个插件是为了提供一些便捷且强大的实体操作 API。尽管现在我几乎不再使用 MCreator 制作 Mod，但我仍然想要去帮助那些依然使用 MCreator 且一直苦恼于实体相关操作的开发者。

这个插件将 [Epic Core API](https://github.com/CJiangqiu/EpicCoreAPI) Mod 添加为依赖，并提供了一些在原版 MCreator 中难以或无法实现的实体操作流程块。

### 流程块

- **强制杀死实体** `<实体>` - 设置血量为0、触发die()、掉落战利品、给予成就并移除实体（不会发送死亡消息）
- **强制设置生命值** `<实体> <血量>` - 通过多阶段修改实体血量：原版字段、智能字段扫描、字节码逆向追踪
- **强制复活实体** `<实体>` - 使用VarHandle清除实体的死亡标志并重置deathTime
- **设置强制无敌** `<实体> <布尔值>` - 开启/关闭无敌并自动处理血量锁定（开启时锁血，关闭时解锁）
- **强制移除实体** `<实体>` - 深度清理包括AI、Boss血条、骑乘关系以及所有服务端/客户端容器
- **锁定血量** `<实体> <数值>` - 通过字节码hook锁定血量（getHealth()返回锁定值）并每tick重置真实血量
- **解锁血量** `<实体>` - 移除血量锁定，getHealth()恢复返回实际血量
- **血量是否已锁定** `<实体>` - 检查实体血量是否已锁定
- **获取锁定血量值** `<实体>` - 获取锁定的血量值，未锁定时返回0
- **强制获取真实血量** `<实体>` - 使用VarHandle直接从DATA_HEALTH_ID读取真实血量，绕过自定义实现
- **是否处于强制无敌状态** `<实体>` - 通过EntityData检查ECA无敌状态
- **强制传送** `<实体> <X> <Y> <Z>` - 使用VarHandle直接修改位置字段并自动同步到客户端
- **清理Boss血条** `<实体>` - 扫描实体实例字段并移除所有ServerBossEvent实例
- **启用AllReturn** `<实体>` ⚠️ **【危险】** - 需配置文件启用。对实体所属mod包内所有boolean/void方法进行转换
- **禁用AllReturn** - 关闭AllReturn并清空所有转换目标
- **AllReturn是否已启用** - 检查AllReturn是否激活
- **设置全局AllReturn** `<布尔值>` ⚠️ **【危险】** - 需配置文件启用。启用/禁用全局AllReturn模式，影响所有mod的boolean/void方法
- **内存移除实体** `<实体>` ⚠️ **【危险】** - 需配置文件启用。通过LWJGL内部通道移除实体
- **添加血量白名单关键字** `<关键字>` - 添加血量白名单关键字，包含此关键字的字段将在血量修改时被修改
- **移除血量白名单关键字** `<关键字>` - 从血量白名单中移除关键字
- **添加血量黑名单关键字** `<关键字>` - 添加血量黑名单关键字，包含此关键字的字段将在血量修改时被跳过
- **移除血量黑名单关键字** `<关键字>` - 从血量黑名单中移除关键字
- **禁止生成** `<实体类型> <秒数>` - 在当前维度禁止指定实体类型生成一段时间
- **是否被禁止生成** `<实体类型>` - 检查实体类型是否被禁止生成
- **获取禁生成剩余时间** `<实体类型>` - 获取实体类型禁生成的剩余时间（秒）
- **清除禁生成** `<实体类型>` - 清除指定实体类型的禁生成
- **清除所有禁生成** - 清除当前维度的所有禁生成
- **添加受保护的包名** `<包名前缀>` - 将包名前缀添加到白名单，受保护包中的类不会受到AllReturn影响
- **移除受保护的包名** `<包名前缀>` - 从白名单中移除包名前缀，内置保护无法移除
- **是否受保护** `<类名>` - 检查类名是否在白名单保护中
- **设置最大血量** `<实体> <数值>` - 精确设置实体最大血量
- **锁定最大血量** `<实体> <数值>` - 将最大血量锁定在指定值
- **解锁最大血量** `<实体>` - 解锁最大血量
- **最大血量是否已锁定** `<实体>` - 检查最大血量是否锁定
- **获取锁定最大血量值** `<实体>` - 获取锁定的最大血量值
- **禁止治疗** `<实体> <数值>` - 禁止实体治疗，将血量锁定在指定值
- **解禁治疗** `<实体>` - 移除治疗禁令
- **是否被禁止治疗** `<实体>` - 检查是否被禁止治疗
- **获取禁治疗数值** `<实体>` - 获取禁止治疗的血量值
- **锁定位置** `<实体>` - 锁定实体在当前位置
- **解锁位置** `<实体>` - 解锁实体位置
- **位置是否已锁定** `<实体>` - 检查实体位置是否锁定
- **设置全局迷雾** `<颜色> <模式> <半径>` - 覆盖维度迷雾
- **清除全局迷雾** - 移除迷雾覆盖
- **设置全局天空盒** `<预设> <透明度>` - 使用着色器预设覆盖维度天空盒
- **清除全局天空盒** - 移除天空盒覆盖
- **设置全局音乐** `<音效> <音源> <音量> <音调> <循环>` - 覆盖维度音乐
- **清除全局音乐** - 移除音乐覆盖
- **清除所有全局效果** - 移除所有全局效果覆盖
- **遍历维度内所有实体** - 遍历当前维度中的所有实体
- **按类型遍历维度内实体** `<实体类型>` - 遍历维度中指定类型的实体
- **遍历全服实体** - 遍历整个服务器的所有实体
- **遍历范围内实体** `<X> <Y> <Z> <范围>` - 遍历指定范围内的实体

所有流程块位于流程编辑器中的 **"Epic Core API"** 分类（紫色）。

### 实体拓展（模组元素）

一种新的模组元素类型，可为指定实体类型添加可视化增强。创建一个实体拓展元素可附加以下功能：

- **自定义Boss血条** — 自定义框架/填充纹理，可选着色器效果，可配置大小和偏移
- **自定义血量显示** — 通过流程块覆盖显示的血量/最大血量数值
- **实体图层** — 额外渲染图层，支持发光、受伤叠加和透明度设置
- **全局迷雾** — 自定义迷雾颜色/距离，全局或半径模式，可配置形状
- **全局天空盒** — 自定义天空盒纹理和/或着色器（12种内置预设：TheLastEnd、DreamSakura、Forest、Ocean、Storm、Volcano、Arcane、Aurora、Hacker、Starlight、Cosmos、BlackHole），透明度和大小
- **战斗音乐** — 自定义战斗音乐，支持音源、音量、音调、循环和严格锁定选项

当同一维度存在多个拓展实体时，优先级最高的控制全局效果。

### 环境要求

- **MCreator 2024.1 或更高版本**
  - **MCreator 2024.1-2024.4**：无需额外设置（内置 1.20.1 Forge 生成器）
  - **MCreator 2025.1 及以上**：需要开发者手动下载安装 1.20.1 Forge 生成器插件
- **Minecraft Forge 1.20.1**
- **Epic Core API Mod**（玩家运行时依赖）

### 开发者安装教程

#### 第 1 步：安装 1.20.1 Forge 生成器（仅限 MCreator 2025.1+）

**如果使用 MCreator 2024.1-2024.4，请跳过此步骤**（内置 1.20.1 Forge 支持）

如果使用 **MCreator 2025.1 或更高版本**：

请前往 [MCreator 插件页面](https://mcreator.net/plugins) 寻找对应 MCreator 版本的 **1.20.1 Forge 生成器插件**，并与 ECA 插件一起安装。

#### 第 2 步：安装插件

1. 下载插件 ZIP 文件
2. 将其放置在：
   ```
   <用户目录>/.mcreator/plugins/
   ```
3. 重启 MCreator

#### 第 3 步：启用 Java 插件

1. 打开 MCreator 首选项（文件 → 首选项）
2. 进入"插件"部分
3. **启用"Java 插件"选项**
4. 如果提示，重启 MCreator

#### 第 4 步：配置工作区

1. 打开或创建一个工作区（Forge 1.20.1）
2. 进入 **工作区 → 工作区设置 → 外部 API**
3. 在外部依赖列表中**勾选"Epic Core API"**
4. 点击**保存**并**重新生成代码**
5. 等待 Gradle 同步完成

#### 第 5 步：使用流程块

1. 创建一个新流程
2. 在流程编辑器中，找到 **"Epic Core API"** 分类（紫色）
3. 拖放你需要的流程块

### 玩家须知

所有使用该插件制作的 Mod 都需要将 [Epic Core API](https://www.curseforge.com/minecraft/mc-mods/epic-core-api) 作为**必要的依赖**。

玩家必须从 CurseForge 下载并安装 Epic Core API mod，才能使用基于此插件构建的任何 Mod。

### 相关链接

- **Epic Core API Mod**: https://www.curseforge.com/minecraft/mc-mods/epic-core-api
- **源代码**: https://github.com/CJiangqiu/EpicCoreAPI

### 许可证

本插件基于 Pylo 的 [MCreator Demo Plugin](https://github.com/Pylo/MCreatorDemoPlugin) 修改。

MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。
