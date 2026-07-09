### Creating a Custom Shader Preset

1. **In-game**: Use ECA's Shader Generator (`/eca shaderGenerator` command) to visually compose your custom shader, then click **Export**.
2. The export creates 5 files under `config/eca/shadergenerator/<namespace>/<name>/` in the game run directory:
   - `<name>.fsh` — fragment shader
   - `<name>_block.vsh` + `<name>_block.json` — BLOCK profile (skybox, boss bar)
   - `<name>_entity.vsh` + `<name>_entity.json` — NEW_ENTITY profile (entity layer, item overlay)
3. **Copy** all 5 files to your workspace:
   ```
   <workspace>/src/main/resources/assets/<modid>/shaders/core/
   ```
4. **Back in MCreator**: Create an ECA Shader Preset element. The dropdown auto-scans the folder above and lists any valid five-file sets found there. Select yours, or type the name manually.

Once registered, your preset appears in all ECA shader dropdowns (Entity Extension layers, skyboxes, boss bars, item extension render layers, etc.) alongside the 12 built-in ECA presets.
