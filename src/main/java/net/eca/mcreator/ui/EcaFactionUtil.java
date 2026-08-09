package net.eca.mcreator.ui;

import net.eca.mcreator.EcaElementTypes;
import net.mcreator.ui.MCreator;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 阵营下拉框工具：列出工作区中已注册的 ECA 阵营元素。
 * 不用 DataListComboBox，是因为它对自定义元素固定按方块/物品查图标，
 * 阵营元素查不到会显示缺失贴图图标。
 */
public final class EcaFactionUtil {

    /** 实体扩展用：表示"不加入任何阵营"，存回元素时转为空字符串 */
    public static final String NONE = "(None)";

    /** 关系列表用：本行针对的是"没有阵营的实体"，对应 ECA 的默认关系而非阵营间关系 */
    public static final String NO_FACTION = "(No faction)";

    private EcaFactionUtil() {}

    /** 工作区中所有阵营元素名；exclude 用于排除自身，可为 null */
    public static List<String> getFactionElementNames(MCreator mcreator, String exclude) {
        List<String> names = new ArrayList<>();
        if (mcreator == null || mcreator.getWorkspace() == null) return names;
        for (ModElement el : mcreator.getWorkspace().getModElements()) {
            if (el.getType() == EcaElementTypes.ECA_FACTION
                    && !el.getName().equals(exclude)) {
                names.add(el.getName());
            }
        }
        return names;
    }

    /** 刷新下拉框并保持选中项；withNone 为真时首项为 (None) */
    public static void populateCombo(JComboBox<String> combo, MCreator mcreator,
                                     String exclude, boolean withNone) {
        populateCombo(combo, mcreator, exclude, withNone, false);
    }

    /** withNoFaction 为真时首项为 (No faction)，供阵营关系列表使用 */
    public static void populateCombo(JComboBox<String> combo, MCreator mcreator,
                                     String exclude, boolean withNone, boolean withNoFaction) {
        Object keep = combo.getSelectedItem();
        combo.removeAllItems();
        if (withNone) combo.addItem(NONE);
        if (withNoFaction) combo.addItem(NO_FACTION);
        for (String n : getFactionElementNames(mcreator, exclude)) combo.addItem(n);
        if (keep != null) combo.setSelectedItem(keep);
        if (combo.getSelectedItem() == null && combo.getItemCount() > 0) combo.setSelectedIndex(0);
    }

    /** 元素中表示"无阵营"这一行的存储值；元素名不会以 $ 开头，故不会冲突 */
    public static final String NO_FACTION_VALUE = "$noFaction";

    /** 下拉框当前值转为元素存储值：(None) 存为空，(No faction) 存为哨兵值 */
    public static String toStoredValue(JComboBox<String> combo) {
        Object v = combo.getSelectedItem();
        String s = v != null ? v.toString() : "";
        if (NONE.equals(s)) return "";
        if (NO_FACTION.equals(s)) return NO_FACTION_VALUE;
        return s;
    }

    /** 存储值转回下拉框显示值 */
    public static String toDisplayValue(String stored) {
        return NO_FACTION_VALUE.equals(stored) ? NO_FACTION : stored;
    }
}
