# Epic Core API MCreator Plugin

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![MCreator](https://img.shields.io/badge/MCreator-2024.1+-orange.svg)](https://mcreator.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)

[English](#english) | [中文](#中文)

---

## English

### About

I created this plugin to provide convenient and powerful entity manipulation APIs for MCreator developers. Although I rarely use MCreator to create mods anymore, I still want to help those who continue using MCreator and struggle with entity-related operations.

This plugin integrates [Epic Core API](https://github.com/CJiangqiu/EpicCoreAPI) as a dependency and provides procedure blocks that would otherwise be difficult or impossible to implement in vanilla MCreator, plus seven mod element types — Entity Extension, BossShow, Item Extension, Block Extension, Faction, Raid and Shader Preset — that expose ECA's extension, faction, raid and shader systems without writing any Java.

### Procedure Blocks

- **Force Kill Entity** `<Entity>` - Set health to 0, trigger die(), drop loot, grant advancements, and remove the entity (death messages are not sent)
- **Force Set Health** `<Entity> <Health>` - Modify entity health through multi-phase process: vanilla fields, smart field scanning, and bytecode reverse tracking
- **Force Deal Damage** `<Amount> <Entity> <DamageSource>` - Deal damage that is guaranteed to land: clears the hurt cooldown, runs the vanilla damage pipeline (armor, resistance, absorption, knockback, aggro), then force-writes health if it did not actually drop. A lethal hit stops at zero health and lets vanilla play the death animation
- **Force Revive** `<Entity>` - Clear the entity's death flag and reset deathTime using VarHandle
- **Force Revive by UUID** `<UUID>` - Revive an entity by UUID, useful when the entity has been removed from the world
- **Set Force Invulnerable** `<Entity> <Boolean>` - Enable/disable invulnerability with automatic health locking (locks health when enabled, unlocks when disabled)
- **Force Remove Entity** `<Entity>` - Deep cleanup including AI, boss bars, riding relationships, and all server/client containers
- **Lock Health** `<Entity> <Value>` - Lock health via bytecode hook (getHealth() returns locked value) and tick-based reset
- **Unlock Health** `<Entity>` - Remove health lock, allowing getHealth() to return actual health
- **Is Health Locked** `<Entity>` - Check if entity health is locked
- **Get Locked Health Value** `<Entity>` - Get the locked health value, or 0 if not locked
- **Force Get Health** `<Entity>` - Read the entity's real health value directly from DATA_HEALTH_ID using VarHandle
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
- **Add AllReturn Whitelist** `<PackagePrefix>` - Add a package prefix to the AllReturn whitelist. Classes in this package will not be affected by ECA AllReturn transformation
- **Remove AllReturn Whitelist** `<PackagePrefix>` - Remove a package prefix from the AllReturn whitelist. Built-in protections cannot be removed
- **Is In AllReturn Whitelist** `<ClassName>` - Check if a class name is in the AllReturn whitelist
- **Add Transform Whitelist** `<PackagePrefix>` - Add a package prefix to the transform whitelist. Classes in this package will not be affected by any ECA transformation
- **Remove Transform Whitelist** `<PackagePrefix>` - Remove a package prefix from the transform whitelist. Built-in entries (JDK, Minecraft, Forge, etc.) cannot be removed
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
- **Set Force Loading** `<Entity> <Boolean>` - Enable/disable force chunk loading for an entity
- **Is Force Loaded** `<Entity>` - Check if an entity is force loaded
- **Enable Filter** `<Player> <Filter>` - Enable a screen filter effect for a player. Filter state is per-player and synced to the client
- **Disable Filter** `<Player> <Filter>` - Disable a screen filter effect for a player
- **Is Filter Enabled** `<Player> <Filter>` - Check if a screen filter effect is currently enabled for a player
- **Play BossShow** `<Player> <Target> <BossShowID>` - Start playing a BossShow cutscene for a player. Plays even if the player has seen it before
- **Play BossShow (If Not Seen)** `<Player> <Target> <BossShowID>` - Start playing a BossShow cutscene only if the player has not seen it before
- **Stop BossShow** `<Player>` - Stop the currently playing BossShow cutscene for a player
- **Is BossShow Playing** `<Player>` - Check if a player currently has an active BossShow cutscene playing
- **Trigger Custom-type BossShow** `<Name> <Player> <Target>` - Trigger the BossShow with the given name that is configured with trigger type "Custom". Starts a new playback and does not interact with any currently playing BossShow
- **Start Resurrection Daemon** - Start the resurrection daemon thread, which continuously monitors tracked entities and auto-revives any that die or lose container registration
- **Stop Resurrection Daemon** - Stop the resurrection daemon thread; tracked entities are no longer auto-revived
- **Is Resurrection Daemon Running** - Check whether the resurrection daemon thread is currently running
- **Add to Resurrection Tracking** `<Entity>` - Add an entity to resurrection tracking; while the daemon runs it is auto-revived shortly after death. Do not use on entities that spawn in large numbers
- **Remove from Resurrection Tracking** `<Entity>` - Remove an entity from resurrection tracking so it is no longer auto-revived

**Factions** — every faction parameter below is a dropdown listing the ECA Faction elements registered in your workspace, so you never type an ID by hand:

- **Create Faction** `<Faction> <Name> <Colour>` - Create a new faction at runtime and persist it to the world
- **Remove Faction** `<Faction>` - Delete a faction and unbind all of its members
- **Merge Factions** `<From> <Into>` - Move every member of the first faction into the second, then delete the first; returns how many members were moved
- **Does Faction Exist** `<Faction>` - Check whether a faction with this ID is registered
- **Join Faction** `<Entity> <Faction>` - Bind an entity to a faction
- **Leave Faction** `<Entity>` - Unbind an entity from whatever faction it belongs to
- **Get Entity Faction** `<Entity>` - Get the faction ID an entity belongs to, or an empty string if it has none
- **Are Same Faction** `<Entity> <Entity>` - Check whether two entities belong to the same faction
- **Faction Member Count** `<Faction>` - Get how many members a faction currently has
- **Kick All From Faction** `<Faction>` - Unbind every member without deleting the faction itself
- **Set Faction Relation** `<Faction> <Faction> <Relation>` - Set the runtime relation between two factions, overriding the preset lists
- **Get Faction Relation** `<Faction> <Faction>` - Get the relation set between two factions
- **Get Effective Relation** `<Entity> <Entity>` - Resolve the final relation between two entities, taking same-faction, dynamic overrides, preset lists and defaults into account
- **Can Harm** `<Entity> <Entity>` - Check whether the faction relation allows the first entity to damage the second
- **Can Target** `<Entity> <Entity>` - Check whether the faction relation allows the first entity to set the second as its attack target
- **Are Friendly** `<Entity> <Entity>` - Check whether two entities are friendly or in the same faction
- **Alert Faction Members** `<Faction> <Attacker> <Victim>` - Notify a faction's members that one of them was attacked, so they can react
- **Set Faction Leader** `<Faction> <Entity>` - Assign an entity as the faction's leader
- **Clear Faction Leader** `<Faction>` - Remove the faction's current leader
- **Is Faction Leader** `<Entity>` - Check whether an entity is the leader of its faction
- **Get Faction Leader** `<Faction>` - Get the faction's leader entity, or nothing if it has no leader or the leader is not loaded

**Raids** — the raid parameter is a dropdown listing the ECA Raid elements registered in your workspace. Starting a raid returns a numeric instance ID, which the other blocks take:

- **Start Raid At** `<Raid> <X> <Y> <Z>` - Start a raid with an explicit centre, bypassing the structure lookup. Returns the raid instance ID, or -1 on failure
- **Start Raid In Structure** `<Raid> <X> <Y> <Z>` - Start a raid inside its target structure; the centre is taken from the structure bounds. Returns -1 if the position is not inside that structure
- **End Raid** `<RaidID> <Victory>` - End an active raid, counting it as a victory or a defeat
- **Get Nearest Raid** `<X> <Y> <Z> <MaxDistance>` - Get the instance ID of the nearest active raid within the distance, or -1
- **Active Raid Count** - How many raids are currently active in this dimension
- **Does Raid Exist** `<RaidID>` - Check whether a raid instance is currently tracked
- **Is Raid Over** `<RaidID>` - Check whether a raid instance has finished
- **Get Raid Status** `<RaidID>` - Get the status as text: ONGOING, VICTORY, DEFEAT or STOPPED
- **Get Raid Waves Spawned** `<RaidID>` - How many waves the raid has spawned so far
- **Get Raid Wave Count** `<RaidID>` - How many waves the raid's definition declares
- **Get Raid Alive Raiders** `<RaidID>` - How many raiders of this raid are still alive
- **Get Raid Definition ID** `<RaidID>` - The raid definition ID this instance was started from

All procedure blocks are located in the **"Epic Core API"** category in the procedure editor.

### Entity Extension (Mod Element)

A new mod element type for visually enhancing specific entity types. Create an Entity Extension element to attach:

- **Force Loading** — Force-load entities of this type; do not use for entities that spawn in large numbers
- **Custom Boss Bar** — Custom frame/fill textures with optional shader effects, configurable size and offset
- **Custom Fill Ratio** — Override the health/max health values used to calculate boss bar fill ratio (fill = current / max)
- **Entity Layer** — Additional render layer with 3 modes: texture-only, shader-only, or mixed (texture + shader two-pass). Also supports glow, hurt overlay, and alpha settings
- **Global Fog** — Custom fog color/distance, global or radius-based, with configurable shape
- **Global Skybox** — Custom skybox with texture and/or shader (12 built-in presets + custom ShaderPreset elements), alpha, size, and texture color modulation (UV scale + RGB channels)
- **Combat Music** — Custom combat music with source, volume, pitch, loop, and strict lock options
- **Conditional Triggers** — Each sub-module (Boss Bar, Fog, Skybox, Music) supports an optional logic procedure to dynamically control whether the effect is active per entity per tick

**Note!** These conditions are evaluated on the client, so the dependencies they use must be available on the client side, otherwise the condition will not work. An entity's attack target, its last attacker and its AI state only exist on the server and are never synchronized to clients, so a condition built on them is permanently false — drive those cases from a server-side procedure instead (for combat music, use the global music procedure blocks). Custom Fill Ratio is the exception: it is evaluated on the server.

When multiple extension entities exist in the same dimension, the one with highest priority controls global effects.

### BossShow (Mod Element)

**BossShow** is ECA's cinematic system: it plays a cutscene that locks the player's camera onto a pre-recorded path around a target entity, with subtitles and server-side event callbacks. Camera paths are recorded in-game with ECA's built-in editor (command: */eca bossShow edit*) and saved as JSON — you don't write keyframes by hand. Each keyframe can carry an *event_id* that fires a server-side callback when playback reaches it. For the full system (in-game editor, recording workflow, JSON format, triggers, subtitle translation), see the [Epic Core API documentation](https://github.com/CJiangqiu/EpicCoreAPI).

This mod element is the MCreator-side handler: it binds procedures to an existing cutscene's keyframe events, so you can react to a cutscene from your own logic without writing Java. Configure:

- **Target Entity Type** — The entity type this BossShow handler is associated with
- **BossShow ID** — The BossShow cutscene identifier to bind to
- **Keyframe Event Mappings** — A list of event ID → procedure pairs; each procedure fires when the matching keyframe (with that *event_id*) is reached during playback

Use the BossShow procedure blocks (Play / Stop / Is Playing / Trigger Custom-type) to control cutscenes from within other procedures.

**Note!** The files you record and save in-game are located at:

```
<game run directory>/config/eca/bossshow/<modid>/<id>.json
```

You need to manually copy the BossShow file for the corresponding entity into the matching location in your own mod project:

```
<mod project>/src/main/resources/data/<modid>/bossshow/<id>.json
```

### ECA Item Extension (Mod Element)

A mod element for enhancing an existing item with animated/styled text and an ECA shader preset render layer. The editor is split into three pages:

**Name Rendering** — override the item's display name with animated styled text:
- **Target Item** — The item this extension applies to (one extension per item)
- **Enable Name Effect** — Master switch for the name override; player-set custom names (anvil) always take priority
- **Name Condition** — Optional logic procedure (with an itemstack dependency) evaluated per stack; return true to apply the styled name, leave empty to always apply
- **Name Text** — A fixed string or a procedure that returns text
- **Color Effect** — NONE, GRADIENT (slides between two colors), RAINBOW (full hue cycle), or SOLID; with configurable animation period and colors
- **Shimmer / Glitch** — Optional toggles (each with an intensity) that make random characters flash brighter or turn to obfuscated gibberish
- **Bold / Italic / Underline / Strikethrough** — Stackable text styles

**Tooltip** — a list of custom tooltip lines, each independently configured:
- **Text** — A fixed string or a procedure that returns text
- **Color/style effects** — The same color, shimmer, glitch and style options as the name
- **Condition** — Optional per-line logic procedure; return true to show that line, leave empty to always show it

**Render Layer** — an ECA shader preset drawn as an overlay pass on top of normal item rendering (GUI, first/third person, dropped item, item frames):
- **Shader Preset** — Select from the 12 built-in presets plus any custom ShaderPreset elements defined in the workspace. Its ITEM render type is used for the overlay
- **Shader Alpha** — Control the opacity of the shader overlay (0.0 = fully transparent, 1.0 = fully opaque)
- **Enable Render Layer** — Master switch; when off, no shader overlay is drawn (text effects still work)
- **Render Condition** — Optional logic procedure evaluated per stack; return true to draw the overlay, leave empty to always render
- **Color-Key Mask** — Optionally restrict the shader to pixels matching a target color within a tolerance; otherwise the shader covers the whole texture

**Note!** The Name, Tooltip and Render conditions are all evaluated on the client, so the dependencies they use must be available on the client side, otherwise the condition will not work.

### ECA Block Extension (Mod Element)

A mod element that draws an ECA shader preset as an overlay pass on top of an existing block, both for placed blocks and for falling blocks. Configure:

- **Block** — The block this extension applies to (one extension per block)
- **Enable Render Layer** — Master switch; when off, the extension stays registered but nothing is drawn
- **Shader Preset** — Select from the 12 built-in presets plus any custom ShaderPreset elements defined in the workspace
- **Shader Alpha** — Control the opacity of the shader overlay (0.0 = fully transparent, 1.0 = fully opaque)
- **Full Brightness** — Draw the overlay at full brightness, ignoring the light level at the block position
- **Shader Mask** — Optionally restrict the shader to the areas marked by a mask texture, with a configurable target color and tolerance; otherwise the shader covers the whole block
- **Render Condition** — Optional logic procedure evaluated per block position; return true to draw the overlay, leave empty to always render

**Note!** The render condition is evaluated on the client, so the dependencies it uses must be available on the client side, otherwise the condition will not work.

### ECA Faction (Mod Element)

Declares a faction: its identity, and how it treats other factions and entities that belong to no faction. Membership is bound at runtime with the faction procedure blocks — this element defines the faction, not its members. The faction ID is the element's registry name, so the procedure blocks can offer it in a dropdown.

- **Display Name** — A fixed string, a procedure that returns text, or a translation key. Falls back to the registry name when empty
- **Faction Colour** — Used wherever the faction is shown in UI, such as name tags and glow outlines
- **Relations** — A list where each row gives one faction one relation, optionally gated by a condition. Hostile members attack each other normally, Friendly ones never target or damage each other, and Neutral ones will not target each other but accidental damage still applies. Pick **(No faction)** to target entities with no faction at all

Rows without a condition are static presets; rows with one are checked at runtime and take priority, falling back to the presets when false. So "hostile to the guards, but friendly while they are asleep" is two rows on the same faction. ECA resolves relations in this order: same faction → conditional rows → static rows → the other faction's definition → default relation.

**Note!** The display name is read once when the faction is registered at mod load, so a procedure there runs before any world exists and must not rely on world or entity dependencies. Relation conditions are evaluated on the server at query time instead, where entity is the target being judged and sourceentity is a member of this faction — it may be empty when the query has no member context.

### ECA Raid (Mod Element)

Declares a raid: the waves it sends, the faction its raiders belong to, when it is won or lost, and what happens along the way.

**Raids never start on their own.** ECA deliberately keeps the trigger out of the definition — this element describes what the raid is, while when it happens is up to you. Start it from your own procedure with **Start Raid At** or **Start Raid In Structure**, on whatever trigger you like. That is why there are victory, defeat and wave-advance conditions here, but no start condition.

- **Display Name** — Shown on the raid boss bar; a fixed string, a procedure, or a translation key
- **Raider Faction** — What spawned raiders are bound to. This binding is what makes vanilla AI and ECA's attack rules treat them as hostile to defenders; without it the raid relies entirely on each entity's own AI and wave leaders degrade into ordinary raiders
- **Structure Anchor** — How the raid finds its centre: use the position passed to the start block directly, anchor to one specific structure, or anchor to any structure carrying a tag. The last two are mutually exclusive in ECA, so there is one field for both
- **Raider Goal Priority**, **Endless**, **Boss Bar Colour**, **Max Duration**, **Wave Cooldown**, **Participant Radius**, **Celebration** — Behaviour, appearance and pacing
- **Waves** — A list of spawn entries. Rows sharing a wave number are merged into one wave and waves run in ascending order, so you type the number on each row instead of nesting lists. A row can be marked as the wave's **Leader**, which requires a Raider Faction because the leader is made that faction's leader. Delay and radius are wave-level, so the first row of each wave decides them
- **Conditions and events** — Nine optional procedures, all evaluated on the server with the raid centre as their position: advance-wave, victory and defeat conditions, plus callbacks for raid start, wave start, wave end, victory, defeat and stop

Leaving a condition empty uses ECA's default: the previous wave is dead, all waves spawned and cleared, or the target structure is gone. Overriding replaces that default entirely — a custom victory condition also drops the built-in "endless raids never win" guard, and an unanchored raid never loses by default because it has no structure to check. The stop callback runs on every termination path, so it also fires after victory or defeat.

### ECA Shader Preset (Mod Element)

A mod element for registering custom shader presets so they become selectable in all ECA shader dropdowns (Entity Extension layers, skyboxes, boss bars, item extension render layers, etc.).

**Workflow:**
1. **In-game**: Use `/eca shaderGenerator` to visually compose your shader, then click **Export**.
2. The export creates 5 files under `config/eca/shadergenerator/<namespace>/<name>/`:
   `<name>.fsh`, `<name>_block.vsh`, `<name>_block.json`, `<name>_entity.vsh`, `<name>_entity.json`
3. **Copy** all 5 files into your workspace: `src/main/resources/assets/<modid>/shaders/core/`
4. **In MCreator**: Create an ECA Shader Preset element — the dropdown auto-scans the folder above and lists any valid five-file sets.

- **Preset Name** — The shader preset name (without file extension). The dropdown automatically scans your workspace's shader resources for custom presets; ECA's 12 built-in presets are not listed here (they are already registered by ECA and selectable in the consumer dropdowns of other elements). You can also type a custom name manually.

When generated, this produces a lightweight `@RegisterShaderPreset` annotation class. ECA auto-discovers it at startup — no manual registration code needed.

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

By default the build automatically pulls the ECA dev artifact from the Modrinth Maven repository, so no manual download is needed.

**(Optional) Use a local dev jar:** If you prefer not to rely on the Maven repository (e.g. offline, or to pin a specific version), download the dev jar from the **CurseForge** files page. The file name looks like this:

```
epic-core-api-<version>-dev.jar
```

Place it in:

```
<user home>/.mcreator/lib/
```

The build automatically detects any matching ECA dev jar in that folder and uses the newest one, falling back to the Modrinth Maven repository only when none is present.

#### Step 5: Use the Procedure Blocks

1. Create a new procedure
2. In the procedure editor, find the **"Epic Core API"** category
3. Drag and drop the blocks you need

### For Players

All mods created with this plugin require [Epic Core API](https://www.curseforge.com/minecraft/mc-mods/epic-core-api) as a **mandatory dependency**.

Players must download and install the Epic Core API mod from CurseForge to use any mods built with this plugin.

### Links

- **Epic Core API Mod (downloads, incl. dev jar)**: https://www.curseforge.com/minecraft/mc-mods/epic-core-api
- **Source Code**: https://github.com/CJiangqiu/EpicCoreAPI

### License

This plugin is based on [MCreator Demo Plugin](https://github.com/Pylo/MCreatorDemoPlugin) by Pylo.

MIT License - See [LICENSE](LICENSE) file for details.

---

## 中文

### 关于

我创建这个插件是为了提供一些便捷且强大的实体操作 API。尽管现在我几乎不再使用 MCreator 制作 Mod，但我仍然想要去帮助那些依然使用 MCreator 且一直苦恼于实体相关操作的开发者。

这个插件将 [Epic Core API](https://github.com/CJiangqiu/EpicCoreAPI) Mod 添加为依赖，提供了一些在原版 MCreator 中难以或无法实现的流程块，以及 7 种模组元素——实体拓展、BossShow 演出、物品扩展、方块扩展、阵营、袭击、着色器预设——让你无需编写 Java 即可使用 ECA 的扩展、阵营、袭击与着色器系统。

### 流程块

- **强制杀死实体** `<实体>` - 设置血量为0、触发die()、掉落战利品、给予成就并移除实体（不会发送死亡消息）
- **强制设置生命值** `<实体> <血量>` - 通过多阶段修改实体血量：原版字段、智能字段扫描、字节码逆向追踪
- **强制造成伤害** `<伤害值> <实体> <伤害来源>` - 确保掉血真正生效：清除受伤冷却，走原版伤害流程（护甲、抗性、伤害吸收、击退、仇恨），若血量实际未下降则兜底强制改血。致死时停在0血由原版播放死亡动画
- **强制复活实体** `<实体>` - 使用VarHandle清除实体的死亡标志并重置deathTime
- **通过UUID强制复活实体** `<UUID>` - 通过UUID复活实体，适用于实体已被移除的情况
- **设置强制无敌** `<实体> <布尔值>` - 开启/关闭无敌并自动处理血量锁定（开启时锁血，关闭时解锁）
- **强制移除实体** `<实体>` - 深度清理包括AI、Boss血条、骑乘关系以及所有服务端/客户端容器
- **锁定血量** `<实体> <数值>` - 通过字节码hook锁定血量（getHealth()返回锁定值）并每tick重置真实血量
- **解锁血量** `<实体>` - 移除血量锁定，getHealth()恢复返回实际血量
- **血量是否已锁定** `<实体>` - 检查实体血量是否已锁定
- **获取锁定血量值** `<实体>` - 获取锁定的血量值，未锁定时返回0
- **强制获取真实血量** `<实体>` - 使用VarHandle直接从DATA_HEALTH_ID读取实体的真实血量值
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
- **添加AllReturn白名单** `<包名前缀>` - 将包名前缀加入AllReturn白名单，该包下的类不会受到ECA AllReturn转换的影响
- **移除AllReturn白名单** `<包名前缀>` - 从AllReturn白名单中移除包名前缀，内置条目无法移除
- **是否在AllReturn白名单中** `<类名>` - 检查类名是否在AllReturn白名单中
- **添加转换白名单** `<包名前缀>` - 将包名前缀加入转换白名单，该包下的类不会受到ECA任何转换的影响
- **移除转换白名单** `<包名前缀>` - 从转换白名单中移除包名前缀，内置条目（JDK、Minecraft、Forge等）无法移除
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
- **设置强制加载** `<实体> <布尔值>` - 启用/禁用实体的强制区块加载
- **是否被强制加载** `<实体>` - 检查实体是否被强制加载
- **启用滤镜** `<玩家> <滤镜>` - 为玩家启用屏幕滤镜效果，滤镜状态按玩家独立保存并同步到客户端
- **禁用滤镜** `<玩家> <滤镜>` - 为玩家禁用屏幕滤镜效果
- **滤镜是否已启用** `<玩家> <滤镜>` - 检查玩家当前是否启用了指定的屏幕滤镜效果
- **播放BossShow演出** `<玩家> <目标> <演出ID>` - 为玩家播放BossShow演出，即使玩家已看过也会播放
- **播放BossShow演出（仅首次）** `<玩家> <目标> <演出ID>` - 仅当玩家未看过时为其播放BossShow演出
- **停止BossShow演出** `<玩家>` - 停止玩家当前正在播放的BossShow演出
- **是否正在观看演出** `<玩家>` - 检查玩家当前是否有活跃的BossShow演出会话
- **触发名为指定名称的自定义触发类型BossShow** `<名称> <玩家> <目标>` - 触发触发器类型为Custom且名称匹配的BossShow演出，启动一场新的播放，与当前正在播放的BossShow无关
- **启动复活守护线程** - 启动复活守护线程，它会持续监控被追踪的实体，并自动复活任何死亡或丢失容器注册的实体
- **停止复活守护线程** - 停止复活守护线程，被追踪的实体将不再被自动复活
- **复活守护线程是否运行中** - 检查复活守护线程当前是否正在运行
- **加入复活追踪** `<实体>` - 将实体加入复活追踪，守护线程运行期间该实体会在死亡后很快被自动复活。请勿用于会大量生成的实体
- **移出复活追踪** `<实体>` - 将实体移出复活追踪，使其不再被自动复活

**阵营** —— 下列所有阵营参数都是下拉列表，列出工作区中已注册的 ECA 阵营元素，无需手动输入 ID：

- **创建阵营** `<阵营> <名称> <颜色>` - 在运行时创建一个新阵营并持久化到世界存档
- **删除阵营** `<阵营>` - 删除阵营并解除其所有成员的归属
- **合并阵营** `<源阵营> <目标阵营>` - 把前一个阵营的全部成员转入后一个阵营，然后删除前者；返回转移的成员数量
- **阵营是否存在** `<阵营>` - 检查是否已注册该 ID 的阵营
- **加入阵营** `<实体> <阵营>` - 把实体绑定到指定阵营
- **移出阵营** `<实体>` - 解除实体当前的阵营归属
- **获取实体阵营** `<实体>` - 获取实体所属的阵营 ID，无阵营时返回空字符串
- **是否同阵营** `<实体> <实体>` - 检查两个实体是否属于同一阵营
- **阵营成员数量** `<阵营>` - 获取阵营当前的成员数量
- **清空阵营成员** `<阵营>` - 解除阵营全部成员的归属，但不删除阵营本身
- **设置阵营关系** `<阵营> <阵营> <关系>` - 在运行时设置两个阵营之间的关系，覆盖预设列表
- **获取阵营关系** `<阵营> <阵营>` - 获取两个阵营之间已设置的关系
- **获取有效关系** `<实体> <实体>` - 解析两个实体之间的最终关系，会综合同阵营、动态覆盖、预设列表与默认关系
- **能否伤害** `<实体> <实体>` - 检查阵营关系是否允许前者对后者造成伤害
- **能否设为目标** `<实体> <实体>` - 检查阵营关系是否允许前者将后者设为攻击目标
- **是否友好** `<实体> <实体>` - 检查两个实体是否友好或同阵营
- **警报阵营成员** `<阵营> <攻击者> <受害者>` - 通知阵营成员其同伴遭到攻击，使其做出反应
- **设置阵营首领** `<阵营> <实体>` - 将实体指定为该阵营的首领
- **清除阵营首领** `<阵营>` - 移除该阵营当前的首领
- **是否为阵营首领** `<实体>` - 检查实体是否为其所属阵营的首领
- **获取阵营首领** `<阵营>` - 获取该阵营的首领实体；无首领或首领未加载时返回空

**袭击** —— 袭击参数是下拉列表，列出工作区中已注册的 ECA 袭击元素。发起袭击会返回一个数字实例 ID，其余块都以它为参数：

- **在坐标发起袭击** `<袭击> <X> <Y> <Z>` - 以指定坐标为中心强制发起，跳过结构查询。返回袭击实例 ID，失败返回 -1
- **在结构中发起袭击** `<袭击> <X> <Y> <Z>` - 在目标结构内发起，中心取自结构包围盒。坐标不在该结构内时返回 -1
- **结束袭击** `<袭击ID> <是否胜利>` - 结束一场进行中的袭击，按胜利或失败计入
- **获取最近的袭击** `<X> <Y> <Z> <最大距离>` - 获取该距离内最近的活跃袭击实例 ID，没有则返回 -1
- **活跃袭击数量** - 当前维度中正在进行的袭击数量
- **袭击是否存在** `<袭击ID>` - 检查该袭击实例当前是否被追踪
- **袭击是否已结束** `<袭击ID>` - 检查该袭击实例是否已经结束
- **获取袭击状态** `<袭击ID>` - 以文本获取状态：ONGOING、VICTORY、DEFEAT 或 STOPPED
- **获取袭击已生成波数** `<袭击ID>` - 该袭击目前已生成的波次数量
- **获取袭击总波数** `<袭击ID>` - 该袭击定义声明的波次总数
- **获取袭击存活袭击者数量** `<袭击ID>` - 该袭击中仍然存活的袭击者数量
- **获取袭击定义ID** `<袭击ID>` - 该实例所基于的袭击定义 ID

所有流程块位于流程编辑器中的 **"Epic Core API"** 分类。

### 实体拓展（模组元素）

一种新的模组元素类型，可为指定实体类型添加可视化增强。创建一个实体拓展元素可附加以下功能：

- **强制加载** — 将该类型实体设为强加载实体，请勿用于会大量生成的实体
- **自定义Boss血条** — 自定义框架/填充纹理，可选着色器效果，可配置大小和偏移
- **自定义填充比例** — 覆盖用于计算Boss血条填充比例的血量/最大血量值（填充比例 = 当前血量 / 最大血量）
- **实体图层** — 额外渲染图层，支持3种模式：纯贴图、纯着色器或混合（贴图+着色器双层叠加）。同时支持发光、受伤叠加和透明度设置
- **全局迷雾** — 自定义迷雾颜色/距离，全局或半径模式，可配置形状
- **全局天空盒** — 自定义天空盒纹理和/或着色器（12种内置预设 + 自定义着色器预设元素），支持透明度、大小和贴图色彩调制（UV缩放 + RGB通道）
- **战斗音乐** — 自定义战斗音乐，支持音源、音量、音调、循环和严格锁定选项
- **条件触发** — 每个子模块（Boss血条、迷雾、天空盒、战斗音乐）支持可选的逻辑过程块，可按实体每tick动态控制效果是否激活

**注意！** 这些条件在客户端求值，所使用的依赖必须客户端侧可见，否则条件不会生效。实体的攻击目标、最后攻击者、AI 状态仅存在于服务端且不会同步到客户端，基于它们的条件永远为假——这类需求请改用服务端流程驱动（战斗音乐可用全局音乐流程块）。自定义填充比例是例外，它在服务端求值。

当同一维度存在多个拓展实体时，优先级最高的控制全局效果。

### BossShow 演出（模组元素）

**BossShow** 是 ECA 的演出（过场动画）系统：播放时将玩家镜头锁定到围绕目标实体预先录制的运镜路径上，配有字幕和服务端事件回调。运镜路径通过 ECA 内置的游戏内编辑器（指令：*/eca bossShow edit*）录制并保存为 JSON——无需手写关键帧。每个关键帧可携带一个 *event_id*，播放到该帧时触发服务端回调。完整系统（游戏内编辑器、录制流程、JSON 格式、触发器、字幕翻译）请见 [Epic Core API 文档](https://github.com/CJiangqiu/EpicCoreAPI)。

本模组元素是 MCreator 侧的处理器：把流程绑定到已有演出的关键帧事件上，让你无需写 Java 就能在演出播放时响应。可配置：

- **目标实体类型** — 该 BossShow 处理器关联的实体类型
- **BossShow ID** — 要绑定的演出标识符
- **关键帧事件映射** — 事件 ID → 流程的映射列表，当播放到带有该 *event_id* 的关键帧时触发对应流程

可在其他流程中使用 BossShow 相关流程块（播放 / 停止 / 是否播放中 / 触发自定义类型）来控制演出。

**注意！** 您在游戏中录制和保存的文件位于：

```
<游戏运行目录>/config/eca/bossshow/<modid>/<id>.json
```

您需要手动复制对应实体的 BossShow 文件到自己的 mod 项目文件的对应位置：

```
<mod 项目>/src/main/resources/data/<modid>/bossshow/<id>.json
```

### ECA物品扩展（模组元素）

一种模组元素类型，用于为已有物品附加动态样式文本以及 ECA 着色器预设渲染层。编辑器分为三页：

**名字渲染** — 用动态样式文本覆盖物品的显示名：
- **目标物品** — 该扩展作用的物品（每个物品只能有一个扩展）
- **启用名字效果** — 名字覆盖的总开关；玩家用铁砧改的名字始终优先
- **名字条件** — 可选的逻辑流程（带 itemstack 依赖），按堆叠逐个求值，返回 true 时应用样式名字，留空则始终应用
- **名字文本** — 固定字符串或返回文本的流程
- **颜色效果** — NONE、GRADIENT（双色滑动渐变）、RAINBOW（整段彩虹循环）或 SOLID，可配置动画周期与颜色
- **闪烁 / 乱码** — 可选开关（各带强度），让随机字符变亮或变成混淆乱码
- **加粗 / 斜体 / 下划线 / 删除线** — 可叠加的文字样式

**Tooltip** — 一组自定义 tooltip 行，每行独立配置：
- **文本** — 固定字符串或返回文本的流程
- **颜色/样式效果** — 与名字相同的颜色、闪烁、乱码和样式选项
- **条件** — 可选的逐行逻辑流程，返回 true 时显示该行，留空则始终显示

**渲染层** — ECA 着色器预设，作为额外的叠加渲染层绘制在物品正常渲染之上（GUI、第一/第三人称、掉落物、物品展示框）：
- **着色器预设** — 可从 12 种内置预设以及工作区中自定义的着色器预设元素中选择，使用其 ITEM 渲染类型进行叠加
- **着色器透明度** — 控制着色器叠加层的不透明度（0.0 = 完全透明，1.0 = 完全不透明）
- **启用渲染层** — 总开关；关闭时不绘制着色器叠加（文本效果仍生效）
- **渲染条件** — 可选的逻辑流程，按堆叠逐个求值，返回 true 时绘制叠加，留空则始终渲染
- **Color-Key 蒙版** — 可选地仅在与目标颜色匹配（在容差内）的像素上叠加着色器；否则着色器覆盖整个贴图

**注意！** 名字条件、Tooltip 行条件、渲染条件均在客户端求值，所使用的依赖必须客户端侧可见，否则条件不会生效。

### ECA方块扩展（模组元素）

用于为已有方块附加 ECA 着色器预设渲染层的模组元素，叠加绘制在已放置方块与下落方块之上。可配置：

- **方块** — 本扩展作用的方块（每个方块一个扩展）
- **启用渲染层** — 总开关；关闭时扩展仍会注册，但不绘制任何内容
- **着色器预设** — 可从 12 种内置预设以及工作区中自定义的着色器预设元素中选择
- **着色器透明度** — 控制着色器叠加层的不透明度（0.0 = 完全透明，1.0 = 完全不透明）
- **全亮度渲染** — 以全亮度绘制叠加层，忽略方块所在位置的光照等级
- **着色器遮罩** — 可选地将着色器限制在遮罩贴图标记的区域内，可配置目标颜色与容差；否则着色器覆盖整个方块
- **渲染条件** — 可选的逻辑流程，按方块位置逐个求值，返回 true 时绘制叠加，留空则始终渲染

**注意！** 渲染条件在客户端求值，所使用的依赖必须客户端侧可见，否则条件不会生效。

### ECA阵营（模组元素）

声明一个阵营：它的身份，以及它如何对待其他阵营和无阵营实体。成员归属在运行时用阵营流程块绑定——本元素定义阵营，而非其成员。阵营 ID 取自元素注册名，因此流程块里可以直接下拉选取。

- **显示名** — 固定文本、返回文本的流程，或翻译键。留空则回退为注册名
- **阵营颜色** — 名牌、发光轮廓等界面显示处使用的颜色
- **关系列表** — 每条针对一个阵营给出一种关系，可附带条件。敌对表示可正常互相攻击；友好表示互不设为目标、也不造成伤害；中立表示不主动设为目标，但误伤仍生效。选择 **(No faction)** 则针对完全没有阵营的实体

不带条件的条目是静态预设；带条件的在运行时判定，优先级更高，条件不成立时回退到预设。所以「平时与守卫敌对，但他们睡着时友好」就是针对同一阵营写两条。ECA 按此顺序解析关系：同阵营 → 带条件的条目 → 静态条目 → 对方阵营的定义 → 默认关系。

**注意！** 显示名在模组加载注册阵营时读取一次，那里的流程运行时世界尚不存在，不能依赖世界或实体。关系条件则是在查询时于服务端求值，entity 是被判定的目标，sourceentity 是本阵营的某个成员——当查询没有成员上下文时它可能为空。

### ECA袭击（模组元素）

声明一场袭击：派出哪些波次、袭击者属于哪个阵营、何时判定胜负、以及过程中触发什么。

**袭击不会自行开始。** ECA 刻意把触发留在定义之外——本元素描述的是「这场袭击是什么样」，「什么时候打」由你决定。请在自己的流程里用**在坐标发起袭击**或**在结构中发起袭击**，配合任意触发时机来驱动。这也是这里有胜利、失败、推进波次的判定，却没有开始条件的原因。

- **显示名** — 显示在袭击 Boss 血条上；可填固定文本、流程或翻译键
- **袭击者阵营** — 生成的袭击者绑定到哪个阵营。这个绑定是让原版 AI 与 ECA 攻击规则把它们视为防守方敌人的关键；不设置的话袭击完全依赖每个实体自身的 AI，波次首领也会退化为普通袭击者
- **结构锚定方式** — 袭击如何确定中心：直接用发起时传入的坐标、锚定到某个具体结构、或锚定到带某标签的任意结构。后两者在 ECA 中互斥，所以共用一个输入框
- **袭击者目标优先级**、**无尽模式**、**Boss血条颜色**、**最长持续时间**、**波次冷却**、**参与半径**、**庆祝时长** — 行为、外观与节奏
- **波次** — 生成条目列表。波次号相同的行合并为同一波，各波按波次号升序进行，因此波次号是每行手填的，不用嵌套列表。某行可标记为本波**首领**，这需要设置袭击者阵营，因为首领会被设为该阵营的首领。延迟与半径是波次级属性，取每波首行的值
- **条件与事件** — 9 个可选流程，全部在服务端求值、位置依赖为袭击中心：推进波次、胜利、失败三个判定，以及袭击开始、波次开始、波次结束、胜利、失败、停止六个回调

判定留空则使用 ECA 默认规则：上一波全灭、所有波次已生成且清空、目标结构不再覆盖中心。自定义会完全取代默认规则——自定义胜利条件会连带取消「无尽袭击永不胜利」的内置保护，而未锚定结构的袭击因为没有结构可查，默认永远不会失败。停止回调在任何结束路径上都会执行，因此它也会在胜利或失败之后再触发一次。

### ECA着色器预设（模组元素）

用于注册自定义着色器预设的模组元素，注册后即可在所有 ECA 着色器下拉框中选用（实体扩展图层、天空盒、Boss 血条、物品扩展渲染层等）。

**工作流程：**
1. **游戏内**：使用 `/eca shaderGenerator` 可视化组合着色器，完成后点击**导出**。
2. 导出会在 `config/eca/shadergenerator/<命名空间>/<名称>/` 下生成 5 个文件：
   `<名称>.fsh`、`<名称>_block.vsh`、`<名称>_block.json`、`<名称>_entity.vsh`、`<名称>_entity.json`
3. **复制**全部 5 个文件到工作区：`src/main/resources/assets/<modid>/shaders/core/`
4. **在 MCreator 中**：创建一个 ECA 着色器预设元素——下拉框自动扫描上述文件夹中的有效五文件集。

- **预设名称** — 着色器预设名称（不含扩展名）。下拉框自动扫描工作区着色器资源中的自定义预设；ECA 的 12 个内置预设不在此列出（它们已由 ECA 自行注册，可在其他元素的消费者下拉框中选用）。也可手动输入自定义名称。

此元素生成的代码为轻量级的 `@RegisterShaderPreset` 注解类，ECA 启动时自动发现，无需手动编写注册代码。

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

默认情况下，构建会自动从 Modrinth Maven 仓库拉取 ECA dev 构件，无需手动下载。

**（可选）使用本地 dev jar：** 如果你不想依赖 Maven 仓库（例如离线，或想锁定特定版本），可从 **CurseForge** 的文件页面下载 dev jar。文件名形如：

```
epic-core-api-<版本>-dev.jar
```

放入：

```
<用户目录>/.mcreator/lib/
```

构建会自动检测该目录下匹配的 ECA dev jar 并使用版本最新的那个；仅当不存在时才回退到 Modrinth Maven 仓库。

#### 第 5 步：使用流程块

1. 创建一个新流程
2. 在流程编辑器中，找到 **"Epic Core API"** 分类
3. 拖放你需要的流程块

### 玩家须知

所有使用该插件制作的 Mod 都需要将 [Epic Core API](https://www.curseforge.com/minecraft/mc-mods/epic-core-api) 作为**必要的依赖**。

玩家必须从 CurseForge 下载并安装 Epic Core API mod，才能使用基于此插件构建的任何 Mod。

### 相关链接

- **Epic Core API Mod（下载，含 dev 版）**: https://www.curseforge.com/minecraft/mc-mods/epic-core-api
- **源代码**: https://github.com/CJiangqiu/EpicCoreAPI

### 许可证

本插件基于 Pylo 的 [MCreator Demo Plugin](https://github.com/Pylo/MCreatorDemoPlugin) 修改。

MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。
