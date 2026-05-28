package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.EntityLayerEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
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

public class JEntityLayerEntry extends JSimpleListEntry<EntityLayerEntry> {

    private static final String[] RENDER_TYPES = {
            "(None)",
            "TheLastEnd", "DreamSakura", "Forest", "Ocean", "Storm",
            "Volcano", "Arcane", "Aurora", "Hacker", "Starlight", "Cosmos", "BlackHole"
    };

    private final ProcedureSelector condition;
    private final JComboBox<String> renderType = new JComboBox<>(RENDER_TYPES);
    private final JCheckBox glow = new JCheckBox(L10N.t("elementgui.entity_extension.entity_layer_glow"));
    private final JCheckBox hurtOverlay = new JCheckBox(L10N.t("elementgui.entity_extension.entity_layer_hurt_overlay"));
    private final JSpinner alpha = new JSpinner(new SpinnerNumberModel(0.8, 0.0, 1.0, 0.05));

    public JEntityLayerEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                             List<JEntityLayerEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/entity_layer_entry_condition"), mcreator,
                L10N.t("elementgui.entity_extension.entity_layer_entry_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setOpaque(false);
        row.add(condition);
        row.add(helpLabel(gui, "entity_layer_render_type"));
        row.add(renderType);
        row.add(helpLabel(gui, "entity_layer_alpha"));
        row.add(alpha);
        row.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/entity_layer_glow"), glow));
        row.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/entity_layer_hurt_overlay"), hurtOverlay));
        line.add(row);
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
        renderType.setEnabled(enabled);
        glow.setEnabled(enabled);
        hurtOverlay.setEnabled(enabled);
        alpha.setEnabled(enabled);
    }

    @Override
    public EntityLayerEntry getEntry() {
        EntityLayerEntry e = new EntityLayerEntry();
        e.condition = condition.getSelectedProcedure();
        String rt = (String) renderType.getSelectedItem();
        e.renderType = "(None)".equals(rt) ? "" : (rt != null ? rt : "");
        e.glow = glow.isSelected();
        e.hurtOverlay = hurtOverlay.isSelected();
        e.alpha = (double) alpha.getValue();
        return e;
    }

    @Override
    public void setEntry(EntityLayerEntry e) {
        if (e.condition != null) condition.setSelectedProcedure(e.condition);
        if (e.renderType != null && !e.renderType.isEmpty()) renderType.setSelectedItem(e.renderType);
        else renderType.setSelectedIndex(0);
        glow.setSelected(e.glow);
        hurtOverlay.setSelected(e.hurtOverlay);
        alpha.setValue(e.alpha);
    }
}
