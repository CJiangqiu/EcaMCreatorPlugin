package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.FogEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JFogEntry extends JSimpleListEntry<FogEntry> {

    private final ProcedureSelector condition;
    private final JCheckBox globalMode = new JCheckBox(L10N.t("elementgui.entity_extension.global_fog_global_mode"));
    private final JSpinner radius = new JSpinner(new SpinnerNumberModel(32.0, 0.0, 1024.0, 1.0));
    private final JColor color;
    private final JSpinner terrainStart = new JSpinner(new SpinnerNumberModel(0.25, 0.0, 10.0, 0.01));
    private final JSpinner terrainEnd = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.01));
    private final JSpinner skyStart = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.01));
    private final JSpinner skyEnd = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.01));
    private final JComboBox<String> shape = new JComboBox<>(new String[]{"SPHERE", "CYLINDER"});

    public JFogEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                     List<JFogEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        color = new JColor(mcreator, false, false);
        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/fog_entry_condition"), mcreator,
                L10N.t("elementgui.entity_extension.fog_entry_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowTop.setOpaque(false);
        rowTop.add(condition);
        rowTop.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/global_fog_global_mode"), globalMode));
        rowTop.add(helpLabel(gui, "global_fog_radius"));
        rowTop.add(radius);
        rowTop.add(helpLabel(gui, "global_fog_color"));
        rowTop.add(color);
        rowTop.add(helpLabel(gui, "global_fog_shape"));
        rowTop.add(shape);

        JPanel rowBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowBottom.setOpaque(false);
        rowBottom.add(helpLabel(gui, "global_fog_terrain_start"));
        rowBottom.add(terrainStart);
        rowBottom.add(helpLabel(gui, "global_fog_terrain_end"));
        rowBottom.add(terrainEnd);
        rowBottom.add(helpLabel(gui, "global_fog_sky_start"));
        rowBottom.add(skyStart);
        rowBottom.add(helpLabel(gui, "global_fog_sky_end"));
        rowBottom.add(skyEnd);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        body.add(rowTop);
        body.add(rowBottom);
        line.add(body);
    }

    // 标签 + help 按钮：复用已有的 entity_extension/<key> 帮助文档
    private static Component helpLabel(IHelpContext gui, String key) {
        return HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/" + key),
                L10N.label("elementgui.entity_extension." + key));
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        condition.refreshListKeepSelected();
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        condition.setEnabled(enabled);
        globalMode.setEnabled(enabled);
        radius.setEnabled(enabled);
        color.setEnabled(enabled);
        terrainStart.setEnabled(enabled);
        terrainEnd.setEnabled(enabled);
        skyStart.setEnabled(enabled);
        skyEnd.setEnabled(enabled);
        shape.setEnabled(enabled);
    }

    @Override
    public FogEntry getEntry() {
        FogEntry e = new FogEntry();
        e.condition = condition.getSelectedProcedure();
        e.globalMode = globalMode.isSelected();
        e.radius = (double) radius.getValue();
        e.color = colorToHex(color.getColor());
        e.terrainStart = (double) terrainStart.getValue();
        e.terrainEnd = (double) terrainEnd.getValue();
        e.skyStart = (double) skyStart.getValue();
        e.skyEnd = (double) skyEnd.getValue();
        Object sh = shape.getSelectedItem();
        e.shape = sh != null ? sh.toString() : "SPHERE";
        return e;
    }

    @Override
    public void setEntry(FogEntry e) {
        if (e.condition != null) condition.setSelectedProcedure(e.condition);
        globalMode.setSelected(e.globalMode);
        radius.setValue(e.radius);
        color.setColor(hexToColor(e.color));
        terrainStart.setValue(e.terrainStart);
        terrainEnd.setValue(e.terrainEnd);
        skyStart.setValue(e.skyStart);
        skyEnd.setValue(e.skyEnd);
        if (e.shape != null) shape.setSelectedItem(e.shape);
    }

    private static Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty()) return new Color(0x808080);
        try {
            return new Color(Integer.parseInt(hex, 16));
        } catch (NumberFormatException ex) {
            return new Color(0x808080);
        }
    }

    private static String colorToHex(Color c) {
        if (c == null) return "808080";
        return String.format("%06X", 0xFFFFFF & c.getRGB());
    }
}
