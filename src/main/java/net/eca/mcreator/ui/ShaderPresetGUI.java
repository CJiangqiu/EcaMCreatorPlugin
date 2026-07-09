package net.eca.mcreator.ui;

import net.eca.mcreator.element.ShaderPresetElement;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.component.VComboBox;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;

/** ECA着色器预设元素：扫描workspace资源文件夹中的自定义五文件集预设并注册 */
public class ShaderPresetGUI extends ModElementGUI<ShaderPresetElement> {

    private final VComboBox<String> presetName = new SearchableComboBox<>();

    public ShaderPresetGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        reloadPresetList();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();
        addRowWithHelp(panel, "shaderpreset/preset_name",
                "elementgui.shaderpreset.preset_name", presetName, gbc);

        addPage(L10N.t("elementgui.shaderpreset.general"),
                PanelUtils.totalCenterInPanel(panel), false);
    }

    /** 仅扫描workspace五文件集自定义预设 */
    private void reloadPresetList() {
        String[] items = ShaderPresetUtil.getWorkspaceShaderPresets(mcreator);
        String previous = (String) presetName.getSelectedItem();
        ShaderPresetUtil.populateCombo(presetName, items, previous);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        reloadPresetList();
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        Object sel = presetName.getSelectedItem();
        if (sel == null || sel.toString().trim().isEmpty()) {
            return new AggregatedValidationResult.FAIL(
                    L10N.t("elementgui.shaderpreset.error_empty_preset"));
        }
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(ShaderPresetElement element) {
        if (element.presetName != null && !element.presetName.isEmpty()) {
            presetName.setSelectedItem(element.presetName);
        }
    }

    @Override
    public ShaderPresetElement getElementFromGUI() {
        ShaderPresetElement element = new ShaderPresetElement(modElement);
        Object selected = presetName.getSelectedItem();
        element.presetName = selected != null ? selected.toString().trim() : "";
        return element;
    }

    private static GridBagConstraints defaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 8);
        return gbc;
    }

    private void addRowWithHelp(JPanel panel, String helpEntry, String l10nKey,
                                JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(HelpUtils.wrapWithHelpButton(this.withEntry(helpEntry), L10N.label(l10nKey)), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
        gbc.gridy++;
    }
}
