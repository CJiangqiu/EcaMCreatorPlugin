package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.EntityLayerEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
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
    private final JCheckBox enableMask = new JCheckBox(L10N.t("elementgui.entity_extension.entity_layer_enable_mask"));
    private final TextureComboBox maskTexture;
    private final JColor maskColor;
    private final JSpinner maskTolerance = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.05));

    public JEntityLayerEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                             List<JEntityLayerEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        this.mcreator = mcreator;
        ShaderPresetUtil.populateCombo(renderType,
                ShaderPresetUtil.getAvailableShaderPresets(mcreator), null);

        texture = new TextureComboBox(mcreator, TextureType.ENTITY);
        texture.setAddPNGExtension(false);
        texture.setPreferredSize(new Dimension(220, 28));
        maskTexture = new TextureComboBox(mcreator, TextureType.ENTITY);
        maskTexture.setAddPNGExtension(false);
        maskTexture.setPreferredSize(new Dimension(220, 28));
        maskColor = new JColor(mcreator, false, false);
        maskColor.setColor(Color.BLACK);
        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/entity_layer_entry_condition"), mcreator,
                L10N.t("elementgui.entity_extension.entity_layer_entry_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
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

        // 行3: 着色器遮罩（遮罩贴图 + 目标颜色 + 容差）
        JPanel rowMask = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowMask.setOpaque(false);
        rowMask.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/entity_layer_enable_mask"), enableMask));
        rowMask.add(maskTexture);
        rowMask.add(helpLabel(gui, "entity_layer_mask_color"));
        rowMask.add(maskColor);
        rowMask.add(helpLabel(gui, "entity_layer_mask_tolerance"));
        rowMask.add(maskTolerance);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        body.add(rowTop);
        body.add(rowBottom);
        body.add(rowMask);
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
        enableMask.setEnabled(enabled);
        maskTexture.setEnabled(enabled);
        maskColor.setEnabled(enabled);
        maskTolerance.setEnabled(enabled);
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
        e.enableMask = enableMask.isSelected();
        e.maskTexture = maskTexture.getTextureName();
        e.maskColor = colorToHex(maskColor.getColor());
        e.maskTolerance = (double) maskTolerance.getValue();
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
        enableMask.setSelected(e.enableMask);
        maskTexture.setTextureFromTextureName(e.maskTexture != null ? e.maskTexture : "");
        maskColor.setColor(hexToColor(e.maskColor));
        maskTolerance.setValue(e.maskTolerance);
    }

    // 遮罩颜色与 hex 字符串互转，与 JFogEntry 的处理保持一致
    private static Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty()) return Color.BLACK;
        try {
            return new Color(Integer.parseInt(hex, 16));
        } catch (NumberFormatException e) {
            return Color.BLACK;
        }
    }

    private static String colorToHex(Color c) {
        return String.format("%06X", c.getRGB() & 0xFFFFFF);
    }
}
