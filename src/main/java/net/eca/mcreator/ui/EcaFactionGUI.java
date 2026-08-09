package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaFactionElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.StringProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;

public class EcaFactionGUI extends ModElementGUI<EcaFactionElement> {

    private final VTextField displayNameFixed = new VTextField(24);
    private StringProcedureSelector displayName;
    private JColor color;

    private JFactionRelationsList relations;

    public EcaFactionGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        // 关系条件在服务端求值：entity 为被判定的目标，sourceentity 为本阵营成员（可能为空）
        Dependency[] relationDeps = Dependency.fromString(
                "x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity");

        // getDisplayName() 在注册期读取一次，此时无世界/实体上下文，故不提供依赖
        displayName = new StringProcedureSelector(
                this.withEntry("ecafaction/display_name"), mcreator,
                L10N.t("elementgui.ecafaction.display_name"),
                AbstractProcedureSelector.Side.BOTH, displayNameFixed, 200);

        color = new JColor(mcreator, false, false);
        color.setColor(Color.WHITE);

        relations = new JFactionRelationsList(mcreator, this.withEntry("ecafaction/relations"),
                relationDeps, modElement.getName());

        addPage(L10N.t("elementgui.ecafaction.page_general"), buildGeneralPage());
        addPage(L10N.t("elementgui.ecafaction.page_relations"), buildRelationsPage());
    }

    private JComponent buildGeneralPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();

        addRowWithHelp(panel, "ecafaction/color", "elementgui.ecafaction.color", color, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(PanelUtils.northAndCenterElement(displayName, panel, 0, 5), BorderLayout.NORTH);
        return PanelUtils.totalCenterInPanel(content);
    }

    private JComponent buildRelationsPage() {
        return PanelUtils.totalCenterInPanel(relations);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        displayName.refreshListKeepSelected();
        relations.reloadDataLists();
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(EcaFactionElement element) {
        displayName.setSelectedProcedure(element.displayName);
        color.setColor(hexToColor(element.color));
        if (element.relations != null) relations.setEntries(element.relations);
    }

    @Override
    public EcaFactionElement getElementFromGUI() {
        EcaFactionElement element = new EcaFactionElement(modElement);
        element.displayName = displayName.getSelectedProcedure();
        element.color = colorToHex(color.getColor());
        element.relations = relations.getEntries();
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
        if (hex == null || hex.isEmpty()) return Color.WHITE;
        try {
            return new Color(Integer.parseInt(hex, 16));
        } catch (NumberFormatException e) {
            return Color.WHITE;
        }
    }

    private static String colorToHex(Color c) {
        if (c == null) return "FFFFFF";
        return String.format("%06X", c.getRGB() & 0xFFFFFF);
    }
}
