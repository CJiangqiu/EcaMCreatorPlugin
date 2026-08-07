package net.eca.mcreator.ui;

import net.eca.mcreator.element.BlockExtensionElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.minecraft.TextureComboBox;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.validators.MCItemHolderValidator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;

public class BlockExtensionGUI extends ModElementGUI<BlockExtensionElement> {

    private final MCItemHolder block = new MCItemHolder(mcreator, ElementUtil::loadBlocks);

    private final JComboBox<String> preset = new JComboBox<>();
    private final JCheckBox renderLayerEnabled = new JCheckBox();
    private final JSpinner alpha = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
    private final JCheckBox glow = new JCheckBox();
    private ProcedureSelector shouldRenderCondition;

    private final JCheckBox enableMask = new JCheckBox();
    private TextureComboBox maskTexture;
    private JColor maskColor;
    private final JSpinner maskTolerance = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.05));

    public BlockExtensionGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        Dependency[] blockDeps = Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate");

        maskColor = new JColor(mcreator, false, false);
        maskColor.setColor(Color.BLACK);
        maskTexture = new TextureComboBox(mcreator, TextureType.BLOCK);
        maskTexture.setAddPNGExtension(false);

        shouldRenderCondition = new ProcedureSelector(
                this.withEntry("blockextension/should_render_condition"), mcreator,
                L10N.t("elementgui.block_extension.should_render_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, blockDeps);

        renderLayerEnabled.setSelected(true);
        block.setValidator(new MCItemHolderValidator(block));

        addPage(L10N.t("elementgui.block_extension.page_render"), buildRenderPage());

        renderLayerEnabled.addItemListener(e -> refreshRenderPageState());
        enableMask.addItemListener(e -> refreshRenderPageState());
        refreshPresetCombo();
        refreshRenderPageState();
    }

    // 渲染层总开关控制下方控件；遮罩控件再受遮罩开关约束
    private void refreshRenderPageState() {
        boolean rl = renderLayerEnabled.isSelected();
        preset.setEnabled(rl);
        alpha.setEnabled(rl);
        glow.setEnabled(rl);
        shouldRenderCondition.setEnabled(rl);
        enableMask.setEnabled(rl);
        boolean mask = rl && enableMask.isSelected();
        maskTexture.setEnabled(mask);
        maskColor.setEnabled(mask);
        maskTolerance.setEnabled(mask);
    }

    private JComponent buildRenderPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();

        addRowWithHelp(panel, "blockextension/block",
                "elementgui.block_extension.block", block, gbc);
        addRowWithHelp(panel, "blockextension/render_layer_enabled",
                "elementgui.block_extension.render_layer_enabled", renderLayerEnabled, gbc);
        addRowWithHelp(panel, "blockextension/preset",
                "elementgui.block_extension.preset", preset, gbc);
        addRowWithHelp(panel, "blockextension/alpha",
                "elementgui.block_extension.alpha", alpha, gbc);
        addRowWithHelp(panel, "blockextension/glow",
                "elementgui.block_extension.glow", glow, gbc);
        addRowWithHelp(panel, "blockextension/enable_mask",
                "elementgui.block_extension.enable_mask", enableMask, gbc);
        addRowWithHelp(panel, "blockextension/mask_texture",
                "elementgui.block_extension.mask_texture", maskTexture, gbc);
        addRowWithHelp(panel, "blockextension/mask_color",
                "elementgui.block_extension.mask_color", maskColor, gbc);
        addRowWithHelp(panel, "blockextension/mask_tolerance",
                "elementgui.block_extension.mask_tolerance", maskTolerance, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(panel, BorderLayout.NORTH);
        content.add(shouldRenderCondition, BorderLayout.CENTER);

        return PanelUtils.totalCenterInPanel(content);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        shouldRenderCondition.refreshListKeepSelected();
        refreshPresetCombo();
    }

    private void refreshPresetCombo() {
        ShaderPresetUtil.populateCombo(preset,
                ShaderPresetUtil.getAvailableShaderPresets(mcreator),
                (String) preset.getSelectedItem());
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(BlockExtensionElement element) {
        block.setBlock(element.block);
        if (element.preset != null && !element.preset.isEmpty()) preset.setSelectedItem(element.preset);
        renderLayerEnabled.setSelected(element.renderLayerEnabled);
        alpha.setValue((int) (element.alpha * 100));
        glow.setSelected(element.glow);
        shouldRenderCondition.setSelectedProcedure(element.shouldRenderCondition);
        enableMask.setSelected(element.enableMask);
        maskTexture.setTextureFromTextureName(element.maskTexture != null ? element.maskTexture : "");
        maskColor.setColor(hexToColor(element.maskColor));
        maskTolerance.setValue(element.maskTolerance);
        refreshRenderPageState();
    }

    @Override
    public BlockExtensionElement getElementFromGUI() {
        BlockExtensionElement element = new BlockExtensionElement(modElement);
        element.block = block.getBlock();
        Object selected = preset.getSelectedItem();
        String presetName = selected != null ? selected.toString() : "";
        element.preset = "(None)".equals(presetName) ? "" : presetName;
        element.renderLayerEnabled = renderLayerEnabled.isSelected();
        element.alpha = (int) alpha.getValue() / 100.0;
        element.glow = glow.isSelected();
        element.shouldRenderCondition = shouldRenderCondition.getSelectedProcedure();
        element.enableMask = enableMask.isSelected();
        element.maskTexture = maskTexture.getTextureName();
        element.maskColor = colorToHex(maskColor.getColor());
        element.maskTolerance = (double) maskTolerance.getValue();
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

    private static Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty()) return Color.BLACK;
        try {
            return new Color(Integer.parseInt(hex, 16));
        } catch (NumberFormatException e) {
            return Color.BLACK;
        }
    }

    private static String colorToHex(Color color) {
        if (color == null) return "000000";
        return String.format("%06X", 0xFFFFFF & color.getRGB());
    }
}
