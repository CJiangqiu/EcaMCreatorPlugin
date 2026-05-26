package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaItemExtensionElement.TooltipLine;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.procedure.StringProcedureSelector;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JTooltipLineEntry extends JSimpleListEntry<TooltipLine> {

    private static final String[] COLOR_EFFECTS = {"NONE", "GRADIENT", "RAINBOW", "SOLID"};

    private final StringProcedureSelector text;
    private final JComboBox<String> colorEffect = new JComboBox<>(COLOR_EFFECTS);
    private final JSpinner period = new JSpinner(new SpinnerNumberModel(3000, 0, 100000, 100));
    private final JColor color1;
    private final JColor color2;
    private final JCheckBox shimmer = new JCheckBox();
    private final JSpinner shimmerIntensity = new JSpinner(new SpinnerNumberModel(0.15, 0.0, 1.0, 0.05));
    private final JCheckBox glitch = new JCheckBox();
    private final JSpinner glitchIntensity = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.05));
    private final JCheckBox bold = new JCheckBox(L10N.t("elementgui.ecaitemextension.bold"));
    private final JCheckBox italic = new JCheckBox(L10N.t("elementgui.ecaitemextension.italic"));
    private final JCheckBox underline = new JCheckBox(L10N.t("elementgui.ecaitemextension.underline"));
    private final JCheckBox strikethrough = new JCheckBox(L10N.t("elementgui.ecaitemextension.strikethrough"));
    private final ProcedureSelector condition;

    public JTooltipLineEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                             List<JTooltipLineEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        color1 = new JColor(mcreator, false, false);
        color2 = new JColor(mcreator, false, false);
        text = new StringProcedureSelector(
                gui.withEntry("ecaitemextension/tooltip_line_text"), mcreator,
                L10N.t("elementgui.ecaitemextension.tooltip_line_text"),
                AbstractProcedureSelector.Side.CLIENT, new VTextField(16), 140, deps);
        condition = new ProcedureSelector(
                gui.withEntry("ecaitemextension/tooltip_line_condition"), mcreator,
                L10N.t("elementgui.ecaitemextension.tooltip_line_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel rowText = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowText.setOpaque(false);
        rowText.add(text);
        rowText.add(condition);

        JPanel rowColor = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowColor.setOpaque(false);
        rowColor.add(L10N.label("elementgui.ecaitemextension.color_effect"));
        rowColor.add(colorEffect);
        rowColor.add(L10N.label("elementgui.ecaitemextension.period"));
        rowColor.add(period);
        rowColor.add(L10N.label("elementgui.ecaitemextension.color1"));
        rowColor.add(color1);
        rowColor.add(L10N.label("elementgui.ecaitemextension.color2"));
        rowColor.add(color2);

        JPanel rowStyle = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowStyle.setOpaque(false);
        rowStyle.add(L10N.label("elementgui.ecaitemextension.shimmer"));
        rowStyle.add(shimmer);
        rowStyle.add(shimmerIntensity);
        rowStyle.add(L10N.label("elementgui.ecaitemextension.glitch"));
        rowStyle.add(glitch);
        rowStyle.add(glitchIntensity);
        rowStyle.add(bold);
        rowStyle.add(italic);
        rowStyle.add(underline);
        rowStyle.add(strikethrough);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        body.add(rowText);
        body.add(rowColor);
        body.add(rowStyle);
        line.add(body);

        shimmer.addItemListener(e -> refreshState());
        glitch.addItemListener(e -> refreshState());
        refreshState();
    }

    //闪烁/乱码强度仅在各自勾选时可用
    private void refreshState() {
        shimmerIntensity.setEnabled(shimmer.isSelected());
        glitchIntensity.setEnabled(glitch.isSelected());
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        text.refreshListKeepSelected();
        condition.refreshListKeepSelected();
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        text.setEnabled(enabled);
        colorEffect.setEnabled(enabled);
        period.setEnabled(enabled);
        color1.setEnabled(enabled);
        color2.setEnabled(enabled);
        shimmer.setEnabled(enabled);
        shimmerIntensity.setEnabled(enabled && shimmer.isSelected());
        glitch.setEnabled(enabled);
        glitchIntensity.setEnabled(enabled && glitch.isSelected());
        bold.setEnabled(enabled);
        italic.setEnabled(enabled);
        underline.setEnabled(enabled);
        strikethrough.setEnabled(enabled);
        condition.setEnabled(enabled);
    }

    @Override
    public TooltipLine getEntry() {
        TooltipLine l = new TooltipLine();
        l.text = text.getSelectedProcedure();
        Object eff = colorEffect.getSelectedItem();
        l.colorEffect = eff != null ? eff.toString() : "NONE";
        l.period = (int) period.getValue();
        l.color1 = colorToHex(color1.getColor());
        l.color2 = colorToHex(color2.getColor());
        l.shimmer = shimmer.isSelected();
        l.shimmerIntensity = (double) shimmerIntensity.getValue();
        l.glitch = glitch.isSelected();
        l.glitchIntensity = (double) glitchIntensity.getValue();
        l.bold = bold.isSelected();
        l.italic = italic.isSelected();
        l.underline = underline.isSelected();
        l.strikethrough = strikethrough.isSelected();
        l.condition = condition.getSelectedProcedure();
        return l;
    }

    @Override
    public void setEntry(TooltipLine l) {
        if (l.text != null) text.setSelectedProcedure(l.text);
        if (l.colorEffect != null) colorEffect.setSelectedItem(l.colorEffect);
        period.setValue(l.period);
        color1.setColor(hexToColor(l.color1));
        color2.setColor(hexToColor(l.color2));
        shimmer.setSelected(l.shimmer);
        shimmerIntensity.setValue(l.shimmerIntensity);
        glitch.setSelected(l.glitch);
        glitchIntensity.setValue(l.glitchIntensity);
        bold.setSelected(l.bold);
        italic.setSelected(l.italic);
        underline.setSelected(l.underline);
        strikethrough.setSelected(l.strikethrough);
        if (l.condition != null) condition.setSelectedProcedure(l.condition);
        refreshState();
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
