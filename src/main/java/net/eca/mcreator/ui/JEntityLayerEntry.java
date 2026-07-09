package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.EntityLayerEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.TextureComboBox;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JEntityLayerEntry extends JSimpleListEntry<EntityLayerEntry> {

    private final MCreator mcreator;
    private final ProcedureSelector condition;
    private final JCheckBox enableTexture = new JCheckBox(L10N.t("elementgui.entity_extension.entity_layer_enable_texture"));
    private final TextureComboBox texture;
    private final JComboBox<String> renderType = new JComboBox<>();
    private final JCheckBox glow = new JCheckBox(L10N.t("elementgui.entity_extension.entity_layer_glow"));
    private final JCheckBox hurtOverlay = new JCheckBox(L10N.t("elementgui.entity_extension.entity_layer_hurt_overlay"));
    private final JSpinner alpha = new JSpinner(new SpinnerNumberModel(80, 0, 100, 1));

    public JEntityLayerEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                             List<JEntityLayerEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        this.mcreator = mcreator;
        ShaderPresetUtil.populateCombo(renderType,
                ShaderPresetUtil.getAvailableShaderPresets(mcreator), null);

        texture = new TextureComboBox(mcreator, TextureType.SCREEN);
        texture.setAddPNGExtension(false);
        texture.setPreferredSize(new Dimension(220, 28));
        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/entity_layer_entry_condition"), mcreator,
                L10N.t("elementgui.entity_extension.entity_layer_entry_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        // 行1: 条件 + 贴图 + 着色器
        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowTop.setOpaque(false);
        rowTop.add(condition);
        rowTop.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/entity_layer_enable_texture"), enableTexture));
        rowTop.add(texture);
        rowTop.add(helpLabel(gui, "entity_layer_render_type"));
        rowTop.add(renderType);

        // 行2: alpha + glow + hurt
        JPanel rowBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowBottom.setOpaque(false);
        rowBottom.add(helpLabel(gui, "entity_layer_alpha"));
        rowBottom.add(alpha);
        rowBottom.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/entity_layer_glow"), glow));
        rowBottom.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/entity_layer_hurt_overlay"), hurtOverlay));

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
        ShaderPresetUtil.populateCombo(renderType,
                ShaderPresetUtil.getAvailableShaderPresets(mcreator),
                (String) renderType.getSelectedItem());
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        condition.setEnabled(enabled);
        enableTexture.setEnabled(enabled);
        texture.setEnabled(enabled);
        renderType.setEnabled(enabled);
        glow.setEnabled(enabled);
        hurtOverlay.setEnabled(enabled);
        alpha.setEnabled(enabled);
    }

    @Override
    public EntityLayerEntry getEntry() {
        EntityLayerEntry e = new EntityLayerEntry();
        e.condition = condition.getSelectedProcedure();
        e.enableTexture = enableTexture.isSelected();
        e.texture = texture.getTextureName();
        String rt = (String) renderType.getSelectedItem();
        e.renderType = "(None)".equals(rt) ? "" : (rt != null ? rt : "");
        e.glow = glow.isSelected();
        e.hurtOverlay = hurtOverlay.isSelected();
        e.alpha = (int) alpha.getValue() / 100.0;
        return e;
    }

    @Override
    public void setEntry(EntityLayerEntry e) {
        if (e.condition != null) condition.setSelectedProcedure(e.condition);
        enableTexture.setSelected(e.enableTexture);
        texture.setTextureFromTextureName(e.texture != null ? e.texture : "");
        if (e.renderType != null && !e.renderType.isEmpty()) renderType.setSelectedItem(e.renderType);
        else renderType.setSelectedIndex(0);
        glow.setSelected(e.glow);
        hurtOverlay.setSelected(e.hurtOverlay);
        alpha.setValue((int)(e.alpha * 100));
    }
}
