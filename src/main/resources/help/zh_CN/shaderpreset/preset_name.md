### 创建自定义着色器预设

1. **游戏内**：使用 ECA 着色器生成器（`/eca shaderGenerator` 指令）可视化作曲自定义着色器，完成后点击**导出**。
2. 导出会在游戏运行目录的 `config/eca/shadergenerator/<命名空间>/<名称>/` 下生成 5 个文件：
   - `<名称>.fsh` — 片段着色器
   - `<名称>_block.vsh` + `<名称>_block.json` — BLOCK 配置（天空盒、Boss 血条）
   - `<名称>_entity.vsh` + `<名称>_entity.json` — NEW_ENTITY 配置（实体层、物品叠加）
3. **复制**全部 5 个文件到你的工作区：
   ```
   <工作区>/src/main/resources/assets/<modid>/shaders/core/
   ```
4. **回到 MCreator**：创建一个 ECA 着色器预设元素。下拉框会自动扫描上述文件夹中有效的五文件集。选择你的预设，或手动输入名称。

注册后，你的预设将出现在所有 ECA 着色器下拉框中（实体扩展图层、天空盒、Boss 血条、物品扩展渲染层等），与 12 个 ECA 内置预设并列可选。
