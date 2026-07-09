package net.eca.mcreator.ui;

import net.eca.mcreator.EcaElementTypes;
import net.eca.mcreator.element.ShaderPresetElement;
import net.mcreator.element.GeneratableElement;
import net.mcreator.ui.MCreator;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** 着色器预设下拉框工具：内置ECA预设 + 工作区ShaderPreset元素 动态列表 */
public final class ShaderPresetUtil {

    private ShaderPresetUtil() {}

    // ECA内置12个预设（与ECA源码EcaPresets.java对应）
    public static final String[] BUILTIN_PRESETS = {
            "TheLastEnd", "DreamSakura", "Forest", "Ocean", "Storm",
            "Volcano", "Arcane", "Aurora", "Hacker", "Starlight", "Cosmos", "BlackHole"
    };

    /** 给消费者元素用： "(None)" + 内置预设 + 工作区ShaderPreset元素名（去重保序） */
    public static String[] getAvailableShaderPresets(MCreator mcreator) {
        Set<String> set = new LinkedHashSet<>();
        set.add("(None)");
        for (String builtin : BUILTIN_PRESETS) set.add(builtin);
        if (mcreator != null && mcreator.getWorkspace() != null) {
            for (ModElement el : mcreator.getWorkspace().getModElements()) {
                if (el.getType() == EcaElementTypes.SHADER_PRESET) {
                    GeneratableElement ge = el.getGeneratableElement();
                    if (ge instanceof ShaderPresetElement spe
                            && spe.presetName != null
                            && !spe.presetName.isEmpty()) {
                        set.add(spe.presetName);
                    }
                }
            }
        }
        return set.toArray(new String[0]);
    }

    /** 给ShaderPreset元素自身用：仅扫描workspace资源文件夹中的五文件集自定义预设 */
    public static String[] getWorkspaceShaderPresets(MCreator mcreator) {
        Set<String> set = new LinkedHashSet<>();
        if (mcreator == null || mcreator.getWorkspace() == null) return set.toArray(new String[0]);
        String modid = mcreator.getWorkspace().getWorkspaceSettings().getModID();
        File shaderDir = new File(mcreator.getWorkspace().getFolderManager().getWorkspaceFolder(),
                "src/main/resources/assets/" + modid + "/shaders/core");
        if (shaderDir.isDirectory()) {
            File[] fshFiles = shaderDir.listFiles((d, n) -> n.endsWith(".fsh"));
            if (fshFiles != null) {
                for (File fsh : fshFiles) {
                    String name = fsh.getName().replace(".fsh", "");
                    if (hasPresetFiles(shaderDir, name)) {
                        set.add(name);
                    }
                }
            }
        }
        return set.toArray(new String[0]);
    }

    /** 检查五文件集是否完整（与ECA源码hasPresetFiles逻辑一致） */
    private static boolean hasPresetFiles(File dir, String name) {
        return new File(dir, name + ".fsh").exists()
                && new File(dir, name + "_block.vsh").exists()
                && new File(dir, name + "_block.json").exists()
                && new File(dir, name + "_entity.vsh").exists()
                && new File(dir, name + "_entity.json").exists();
    }

    /** 刷新JComboBox并保持选中项（无则选第一个） */
    public static void populateCombo(JComboBox<String> combo, String[] items, String keepSelected) {
        combo.removeAllItems();
        for (String item : items) combo.addItem(item);
        if (keepSelected != null) {
            combo.setSelectedItem(keepSelected);
        }
        if (combo.getSelectedItem() == null && combo.getItemCount() > 0) {
            combo.setSelectedIndex(0);
        }
    }
}
