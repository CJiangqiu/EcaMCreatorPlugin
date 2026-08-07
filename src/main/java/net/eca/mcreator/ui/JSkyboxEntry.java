package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.SkyboxEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.dialogs.TypedTextureSelectorDialog;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.TextureHolder;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JSkyboxEntry extends JSimpleListEntry<SkyboxEntry> {

    private final MCreator mcreator;
    private final ProcedureSelector condition;
    private final JCheckBox enableTexture = new JCheckBox(L10N.t("elementgui.entity_extension.global_skybox_enable_texture"));
    private final TextureHolder texture;
    private final JCheckBox enableShader = new JCheckBox(L10N.t("elementgui.entity_extension.global_skybox_enable_shader"));
    private final JComboBox<String> shaderRenderType = new JComboBox<>();
    private final JSpinner alpha = new JSpinner(new SpinnerNumberModel(90, 0, 100, 1));
    private final JSpinner size = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 10.0));
    // 贴图色彩调制
    private final JSpinner textureUvScale = new JSpinner(new SpinnerNumberModel(16.0, 0.1, 256.0, 1.0));
    private final JSpinner textureRed = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.05));
    private final JSpinner textureGreen = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.05));
    private final JSpinner textureBlue = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.05));

    public JSkyboxEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                        List<JSkyboxEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        this.mcreator = mcreator;
        ShaderPresetUtil.populateCombo(shaderRenderType,
                ShaderPresetUtil.getAvailableShaderPresets(mcreator), null);

        texture = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.SCREEN), 28);
        texture.setPreferredSize(new Dimension(220, 28));
        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/skybox_entry_condition"), mcreator,
                L10N.t("elementgui.entity_extension.skybox_entry_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowTop.setOpaque(false);
        rowTop.add(condition);
        rowTop.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/global_skybox_enable_texture"), enableTexture));
        rowTop.add(texture);

        JPanel rowMid = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowMid.setOpaque(false);
        rowMid.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/global_skybox_enable_shader"), enableShader));
        rowMid.add(shaderRenderType);
        rowMid.add(helpLabel(gui, "global_skybox_alpha"));
        rowMid.add(alpha);
        rowMid.add(helpLabel(gui, "global_skybox_size"));
        rowMid.add(size);

        // 贴图色彩调制行：仅在 enableTexture 时可见
        JPanel rowColor = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowColor.setOpaque(false);
        rowColor.add(helpLabel(gui, "global_skybox_texture_uv_scale"));
        rowColor.add(textureUvScale);
        rowColor.add(new JLabel("R:"));
        rowColor.add(textureRed);
        rowColor.add(new JLabel("G:"));
        rowColor.add(textureGreen);
        rowColor.add(new JLabel("B:"));
        rowColor.add(textureBlue);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        body.add(rowTop);
        body.add(rowMid);
        body.add(rowColor);
        line.add(body);

        // enableTexture 控制色彩调制行的可见性
        enableTexture.addItemListener(e -> {
            boolean tex = enableTexture.isSelected();
            textureUvScale.setEnabled(tex);
            textureRed.setEnabled(tex);
            textureGreen.setEnabled(tex);
            textureBlue.setEnabled(tex);
        });
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
        ShaderPresetUtil.populateCombo(shaderRenderType,
                ShaderPresetUtil.getAvailableShaderPresets(mcreator),
                (String) shaderRenderType.getSelectedItem());
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        condition.setEnabled(enabled);
        enableTexture.setEnabled(enabled);
        boolean tex = enabled && enableTexture.isSelected();
        texture.setEnabled(enabled);
        textureUvScale.setEnabled(tex);
        textureRed.setEnabled(tex);
        textureGreen.setEnabled(tex);
        textureBlue.setEnabled(tex);
        enableShader.setEnabled(enabled);
        shaderRenderType.setEnabled(enabled);
        alpha.setEnabled(enabled);
        size.setEnabled(enabled);
    }

    @Override
    public SkyboxEntry getEntry() {
        SkyboxEntry e = new SkyboxEntry();
        e.condition = condition.getSelectedProcedure();
        e.enableTexture = enableTexture.isSelected();
        e.texture = texture.getID();
        e.enableShader = enableShader.isSelected();
        String rt = (String) shaderRenderType.getSelectedItem();
        e.shaderRenderType = "(None)".equals(rt) ? "" : (rt != null ? rt : "");
        e.alpha = (int) alpha.getValue() / 100.0;
        e.size = (double) size.getValue();
        e.textureUvScale = (double) textureUvScale.getValue();
        e.textureRed = (double) textureRed.getValue();
        e.textureGreen = (double) textureGreen.getValue();
        e.textureBlue = (double) textureBlue.getValue();
        return e;
    }

    @Override
    public void setEntry(SkyboxEntry e) {
        if (e.condition != null) condition.setSelectedProcedure(e.condition);
        enableTexture.setSelected(e.enableTexture);
        texture.setTextureFromTextureName(e.texture != null ? e.texture : "");
        enableShader.setSelected(e.enableShader);
        if (e.shaderRenderType != null && !e.shaderRenderType.isEmpty()) shaderRenderType.setSelectedItem(e.shaderRenderType);
        else shaderRenderType.setSelectedIndex(0);
        alpha.setValue((int)(e.alpha * 100));
        size.setValue(e.size);
        textureUvScale.setValue(e.textureUvScale);
        textureRed.setValue(e.textureRed);
        textureGreen.setValue(e.textureGreen);
        textureBlue.setValue(e.textureBlue);
    }
}
