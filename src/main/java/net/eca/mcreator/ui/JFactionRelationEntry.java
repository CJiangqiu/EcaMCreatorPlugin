package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaFactionElement.RelationEntry;
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

public class JFactionRelationEntry extends JSimpleListEntry<RelationEntry> {

    // SAME_FACTION 是解析结果而非可配置项
    private static final String[] RELATIONS = {"HOSTILE", "NEUTRAL", "FRIENDLY"};

    private final MCreator mcreator;
    private final String selfElementName;
    private final JComboBox<String> faction = new JComboBox<>();
    private final JComboBox<String> relation = new JComboBox<>(RELATIONS);
    private final ProcedureSelector condition;

    public JFactionRelationEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                                 List<JFactionRelationEntry> entryList, Dependency[] deps,
                                 String selfElementName) {
        super(parent, entryList);

        this.mcreator = mcreator;
        this.selfElementName = selfElementName;
        // 排除本阵营自身：同阵营关系由 ECA 依 id 相同自动派生（SAME_FACTION），配置自身只会产生无效条目
        EcaFactionUtil.populateCombo(faction, mcreator, selfElementName, false, true);
        faction.setPreferredSize(new Dimension(220, 28));

        condition = new ProcedureSelector(
                gui.withEntry("ecafaction/relation_condition"), mcreator,
                L10N.t("elementgui.ecafaction.relation_condition"),
                AbstractProcedureSelector.Side.SERVER, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setOpaque(false);
        row.add(helpLabel(gui, "relation_faction"));
        row.add(faction);
        row.add(helpLabel(gui, "relation_value"));
        row.add(relation);
        row.add(condition);
        line.add(row);
    }

    private static Component helpLabel(IHelpContext gui, String key) {
        return HelpUtils.wrapWithHelpButton(gui.withEntry("ecafaction/" + key),
                L10N.label("elementgui.ecafaction." + key));
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        condition.refreshListKeepSelected();
        EcaFactionUtil.populateCombo(faction, mcreator, selfElementName, false, true);
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        faction.setEnabled(enabled);
        relation.setEnabled(enabled);
        condition.setEnabled(enabled);
    }

    @Override
    public RelationEntry getEntry() {
        RelationEntry e = new RelationEntry();
        // 存元素名，生成时再解析为注册名
        e.faction = EcaFactionUtil.toStoredValue(faction);
        Object r = relation.getSelectedItem();
        e.relation = r != null ? r.toString() : "HOSTILE";
        e.condition = condition.getSelectedProcedure();
        return e;
    }

    @Override
    public void setEntry(RelationEntry e) {
        if (e.faction != null && !e.faction.isEmpty())
            faction.setSelectedItem(EcaFactionUtil.toDisplayValue(e.faction));
        if (e.relation != null) relation.setSelectedItem(e.relation);
        condition.setSelectedProcedure(e.condition);
    }
}
