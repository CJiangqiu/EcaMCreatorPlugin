package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaItemExtensionElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.procedure.StringProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.MCItemHolderValidator;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;

public class EcaItemExtensionGUI extends ModElementGUI<EcaItemExtensionElement> {

    private static final String[] COLOR_EFFECTS = {"NONE", "GRADIENT", "RAINBOW", "SOLID"};

    private final MCItemHolder item = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);

    //文本渲染（第一页）
    private final JCheckBox nameEffectEnabled = new JCheckBox();
    private ProcedureSelector nameCondition;
    private final VTextField nameFixedField = new VTextField(20);
    private StringProcedureSelector name;
    private final JComboBox<String> nameColorEffect = new JComboBox<>(COLOR_EFFECTS);
    private final JSpinner namePeriod = new JSpinner(new SpinnerNumberModel(3000, 0, 100000, 100));
    private JColor nameColor1;
    private JColor nameColor2;
    private final JCheckBox nameShimmer = new JCheckBox();
    private final JSpinner nameShimmerIntensity = new JSpinner(new SpinnerNumberModel(0.15, 0.0, 1.0, 0.05));
    private final JCheckBox nameGlitch = new JCheckBox();
    private final JSpinner nameGlitchIntensity = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.05));
    private final JCheckBox nameBold = new JCheckBox();
    private final JCheckBox nameItalic = new JCheckBox();
    private final JCheckBox nameUnderline = new JCheckBox();
    private final JCheckBox nameStrikethrough = new JCheckBox();
    private JTooltipLinesList tooltipLines;

    //渲染层（第二页）
    private final JComboBox<String> preset = new JComboBox<>();
    private final JCheckBox renderLayerEnabled = new JCheckBox();
    private final JSpinner alpha = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
    private ProcedureSelector shouldRenderCondition;
    private final JCheckBox colorKeyEnabled = new JCheckBox();
    private JColor colorKeyColor;
    private final JSpinner colorKeyTolerance = new JSpinner(new SpinnerNumberModel(0.3, 0.0, 1.0, 0.05));

    public EcaItemExtensionGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        Dependency[] itemDeps = Dependency.fromString("itemstack:itemstack");

        nameColor1 = new JColor(mcreator, false, false);
        nameColor2 = new JColor(mcreator, false, false);
        colorKeyColor = new JColor(mcreator, false, false);

        name = new StringProcedureSelector(
                this.withEntry("ecaitemextension/name"), mcreator,
                L10N.t("elementgui.ecaitemextension.name"),
                AbstractProcedureSelector.Side.CLIENT, nameFixedField, 160, itemDeps);
        nameCondition = new ProcedureSelector(
                this.withEntry("ecaitemextension/name_condition"), mcreator,
                L10N.t("elementgui.ecaitemextension.name_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, itemDeps);
        tooltipLines = new JTooltipLinesList(mcreator, this.withEntry("ecaitemextension/tooltip"), itemDeps);
        shouldRenderCondition = new ProcedureSelector(
                this.withEntry("ecaitemextension/should_render_condition"), mcreator,
                L10N.t("elementgui.ecaitemextension.should_render_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, itemDeps);

        nameEffectEnabled.setSelected(true);
        renderLayerEnabled.setSelected(true);

        item.setValidator(new MCItemHolderValidator(item));

        addPage(L10N.t("elementgui.ecaitemextension.page_name"), buildNamePage());
        addPage(L10N.t("elementgui.ecaitemextension.page_tooltip"), buildTooltipPage());
        addPage(L10N.t("elementgui.ecaitemextension.page_render"), buildRenderPage());

        nameEffectEnabled.addItemListener(e -> refreshTextPageState());
        nameShimmer.addItemListener(e -> refreshTextPageState());
        nameGlitch.addItemListener(e -> refreshTextPageState());
        renderLayerEnabled.addItemListener(e -> refreshRenderPageState());
        colorKeyEnabled.addItemListener(e -> refreshRenderPageState());
        refreshPresetCombo();
        refreshTextPageState();
        refreshRenderPageState();
    }

    //名字效果总开关控制下方控件；颜色控件再受所选颜色效果约束
    private void refreshTextPageState() {
        boolean ne = nameEffectEnabled.isSelected();
        nameCondition.setEnabled(ne);
        name.setEnabled(ne);
        nameFixedField.setEnabled(ne);
        nameColorEffect.setEnabled(ne);
        nameShimmer.setEnabled(ne);
        nameShimmerIntensity.setEnabled(ne && nameShimmer.isSelected());
        nameGlitch.setEnabled(ne);
        nameGlitchIntensity.setEnabled(ne && nameGlitch.isSelected());
        nameBold.setEnabled(ne);
        nameItalic.setEnabled(ne);
        nameUnderline.setEnabled(ne);
        nameStrikethrough.setEnabled(ne);
        namePeriod.setEnabled(ne);
        nameColor1.setEnabled(ne);
        nameColor2.setEnabled(ne);
    }

    //渲染层总开关控制下方控件；Color-Key 颜色与容差再受 Color-Key 开关约束
    private void refreshRenderPageState() {
        boolean rl = renderLayerEnabled.isSelected();
        preset.setEnabled(rl);
        alpha.setEnabled(rl);
        shouldRenderCondition.setEnabled(rl);
        colorKeyEnabled.setEnabled(rl);
        boolean ck = rl && colorKeyEnabled.isSelected();
        colorKeyColor.setEnabled(ck);
        colorKeyTolerance.setEnabled(ck);
    }

    private JComponent buildNamePage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();

        addRowWithHelp(panel, "ecaitemextension/item",
                "elementgui.ecaitemextension.item", PanelUtils.centerInPanel(item), gbc);
        addRowWithHelp(panel, "ecaitemextension/name_effect_enabled",
                "elementgui.ecaitemextension.name_effect_enabled", nameEffectEnabled, gbc);
        addRowWithHelp(panel, "ecaitemextension/color_effect",
                "elementgui.ecaitemextension.color_effect", nameColorEffect, gbc);
        addRowWithHelp(panel, "ecaitemextension/period",
                "elementgui.ecaitemextension.period", namePeriod, gbc);
        addRowWithHelp(panel, "ecaitemextension/color1",
                "elementgui.ecaitemextension.color1", nameColor1, gbc);
        addRowWithHelp(panel, "ecaitemextension/color2",
                "elementgui.ecaitemextension.color2", nameColor2, gbc);
        addRowWithHelp(panel, "ecaitemextension/shimmer",
                "elementgui.ecaitemextension.shimmer", nameShimmer, gbc);
        addRowWithHelp(panel, "ecaitemextension/shimmer_intensity",
                "elementgui.ecaitemextension.shimmer_intensity", nameShimmerIntensity, gbc);
        addRowWithHelp(panel, "ecaitemextension/glitch",
                "elementgui.ecaitemextension.glitch", nameGlitch, gbc);
        addRowWithHelp(panel, "ecaitemextension/glitch_intensity",
                "elementgui.ecaitemextension.glitch_intensity", nameGlitchIntensity, gbc);
        addRowWithHelp(panel, "ecaitemextension/bold",
                "elementgui.ecaitemextension.bold", nameBold, gbc);
        addRowWithHelp(panel, "ecaitemextension/italic",
                "elementgui.ecaitemextension.italic", nameItalic, gbc);
        addRowWithHelp(panel, "ecaitemextension/underline",
                "elementgui.ecaitemextension.underline", nameUnderline, gbc);
        addRowWithHelp(panel, "ecaitemextension/strikethrough",
                "elementgui.ecaitemextension.strikethrough", nameStrikethrough, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(panel, BorderLayout.NORTH);
        content.add(PanelUtils.northAndCenterElement(nameCondition, name, 0, 5), BorderLayout.CENTER);

        return PanelUtils.totalCenterInPanel(content);
    }

    private JComponent buildTooltipPage() {
        return PanelUtils.totalCenterInPanel(tooltipLines);
    }

    private JComponent buildRenderPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();

        addRowWithHelp(panel, "ecaitemextension/preset",
                "elementgui.ecaitemextension.preset", preset, gbc);
        addRowWithHelp(panel, "ecaitemextension/render_layer_enabled",
                "elementgui.ecaitemextension.render_layer_enabled", renderLayerEnabled, gbc);
        addRowWithHelp(panel, "ecaitemextension/alpha",
                "elementgui.ecaitemextension.alpha", alpha, gbc);
        addRowWithHelp(panel, "ecaitemextension/color_key_enabled",
                "elementgui.ecaitemextension.color_key_enabled", colorKeyEnabled, gbc);
        addRowWithHelp(panel, "ecaitemextension/color_key_color",
                "elementgui.ecaitemextension.color_key_color", colorKeyColor, gbc);
        addRowWithHelp(panel, "ecaitemextension/color_key_tolerance",
                "elementgui.ecaitemextension.color_key_tolerance", colorKeyTolerance, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(panel, BorderLayout.NORTH);
        content.add(shouldRenderCondition, BorderLayout.CENTER);

        return PanelUtils.totalCenterInPanel(content);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        name.refreshListKeepSelected();
        nameCondition.refreshListKeepSelected();
        tooltipLines.reloadDataLists();
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
    protected void openInEditingMode(EcaItemExtensionElement element) {
        item.setBlock(element.item);

        nameEffectEnabled.setSelected(element.nameEffectEnabled);
        nameCondition.setSelectedProcedure(element.nameCondition);
        name.setSelectedProcedure(element.name);
        if (element.nameColorEffect != null) nameColorEffect.setSelectedItem(element.nameColorEffect);
        namePeriod.setValue(element.namePeriod);
        nameColor1.setColor(hexToColor(element.nameColor1));
        nameColor2.setColor(hexToColor(element.nameColor2));
        nameShimmer.setSelected(element.nameShimmer);
        nameShimmerIntensity.setValue(element.nameShimmerIntensity);
        nameGlitch.setSelected(element.nameGlitch);
        nameGlitchIntensity.setValue(element.nameGlitchIntensity);
        nameBold.setSelected(element.nameBold);
        nameItalic.setSelected(element.nameItalic);
        nameUnderline.setSelected(element.nameUnderline);
        nameStrikethrough.setSelected(element.nameStrikethrough);
        if (element.tooltipLines != null)
            tooltipLines.setEntries(element.tooltipLines);

        if (element.preset != null) preset.setSelectedItem(element.preset);
        renderLayerEnabled.setSelected(element.renderLayerEnabled);
        alpha.setValue((int)(element.alpha * 100));
        shouldRenderCondition.setSelectedProcedure(element.shouldRenderCondition);
        colorKeyEnabled.setSelected(element.colorKeyEnabled);
        colorKeyColor.setColor(hexToColor(element.colorKeyColor));
        colorKeyTolerance.setValue(element.colorKeyTolerance);

        refreshTextPageState();
        refreshRenderPageState();
    }

    @Override
    public EcaItemExtensionElement getElementFromGUI() {
        EcaItemExtensionElement element = new EcaItemExtensionElement(modElement);
        element.item = item.getBlock();

        element.nameEffectEnabled = nameEffectEnabled.isSelected();
        element.nameCondition = nameCondition.getSelectedProcedure();
        element.name = name.getSelectedProcedure();
        Object colorEffect = nameColorEffect.getSelectedItem();
        element.nameColorEffect = colorEffect != null ? colorEffect.toString() : "NONE";
        element.namePeriod = (int) namePeriod.getValue();
        element.nameColor1 = colorToHex(nameColor1.getColor());
        element.nameColor2 = colorToHex(nameColor2.getColor());
        element.nameShimmer = nameShimmer.isSelected();
        element.nameShimmerIntensity = (double) nameShimmerIntensity.getValue();
        element.nameGlitch = nameGlitch.isSelected();
        element.nameGlitchIntensity = (double) nameGlitchIntensity.getValue();
        element.nameBold = nameBold.isSelected();
        element.nameItalic = nameItalic.isSelected();
        element.nameUnderline = nameUnderline.isSelected();
        element.nameStrikethrough = nameStrikethrough.isSelected();
        element.tooltipLines = tooltipLines.getEntries();

        Object selected = preset.getSelectedItem();
        element.preset = selected != null ? selected.toString() : "Starlight";
        element.renderLayerEnabled = renderLayerEnabled.isSelected();
        element.alpha = (int) alpha.getValue() / 100.0;
        element.shouldRenderCondition = shouldRenderCondition.getSelectedProcedure();
        element.colorKeyEnabled = colorKeyEnabled.isSelected();
        element.colorKeyColor = colorToHex(colorKeyColor.getColor());
        element.colorKeyTolerance = (double) colorKeyTolerance.getValue();
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
        if (hex == null || hex.isEmpty()) return new Color(0xFFFFFF);
        try {
            return new Color(Integer.parseInt(hex, 16));
        } catch (NumberFormatException e) {
            return new Color(0xFFFFFF);
        }
    }

    private static String colorToHex(Color color) {
        if (color == null) return "FFFFFF";
        return String.format("%06X", 0xFFFFFF & color.getRGB());
    }
}
