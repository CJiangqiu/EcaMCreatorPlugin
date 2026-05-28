package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.SkyboxEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
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

    private static final String[] SKYBOX_SHADERS = {
            "(None)",
            "TheLastEnd", "DreamSakura", "Forest", "Ocean", "Storm",
            "Volcano", "Arcane", "Aurora", "Hacker", "Starlight", "Cosmos", "BlackHole"
    };

    private final ProcedureSelector condition;
    private final JCheckBox enableTexture = new JCheckBox(L10N.t("elementgui.entity_extension.global_skybox_enable_texture"));
    private final TextureHolder texture;
    private final JCheckBox enableShader = new JCheckBox(L10N.t("elementgui.entity_extension.global_skybox_enable_shader"));
    private final JComboBox<String> shaderRenderType = new JComboBox<>(SKYBOX_SHADERS);
    private final JSpinner alpha = new JSpinner(new SpinnerNumberModel(0.9, 0.0, 1.0, 0.05));
    private final JSpinner size = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 10.0));

    public JSkyboxEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                        List<JSkyboxEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        texture = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.SCREEN), 28);
        texture.setPreferredSize(new Dimension(220, 28));
        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/skybox_entry_condition"), mcreator,
                L10N.t("elementgui.entity_extension.skybox_entry_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowTop.setOpaque(false);
        rowTop.add(condition);
        rowTop.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/global_skybox_enable_texture"), enableTexture));
        rowTop.add(texture);

        JPanel rowBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowBottom.setOpaque(false);
        rowBottom.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/global_skybox_enable_shader"), enableShader));
        rowBottom.add(shaderRenderType);
        rowBottom.add(helpLabel(gui, "global_skybox_alpha"));
        rowBottom.add(alpha);
        rowBottom.add(helpLabel(gui, "global_skybox_size"));
        rowBottom.add(size);

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
        enableTexture.setEnabled(enabled);
        texture.setEnabled(enabled);
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
        e.alpha = (double) alpha.getValue();
        e.size = (double) size.getValue();
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
        alpha.setValue(e.alpha);
        size.setValue(e.size);
    }
}
