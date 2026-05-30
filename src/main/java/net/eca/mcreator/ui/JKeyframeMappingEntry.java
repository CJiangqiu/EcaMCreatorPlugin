package net.eca.mcreator.ui;

import net.eca.mcreator.element.BossShowElement.KeyframeMapping;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JKeyframeMappingEntry extends JSimpleListEntry<KeyframeMapping> {

    private final JTextField eventId = new JTextField(16);
    private final ProcedureSelector procedure;

    //2024.4 的 HelpUtils 只对 ModElementHelpContext 挂点击事件，链式 withEntry 会丢类型，故直接拿 ModElementGUI 调
    public JKeyframeMappingEntry(MCreator mcreator, ModElementGUI<?> modElementGUI, JPanel parent,
                               List<JKeyframeMappingEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        procedure = new ProcedureSelector(
                modElementGUI.withEntry("bossshow/keyframe_procedure"),
                mcreator,
                L10N.t("elementgui.bossshow.keyframe_procedure"),
                AbstractProcedureSelector.Side.SERVER,
                true,
                null,
                deps);

        line.add(L10N.label("elementgui.bossshow.event_id"));
        line.add(eventId);
        line.add(procedure);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        procedure.refreshListKeepSelected();
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        eventId.setEnabled(enabled);
        procedure.setEnabled(enabled);
    }

    @Override
    public KeyframeMapping getEntry() {
        KeyframeMapping m = new KeyframeMapping();
        m.eventId = eventId.getText();
        m.procedure = procedure.getSelectedProcedure();
        return m;
    }

    @Override
    public void setEntry(KeyframeMapping e) {
        eventId.setText(e.eventId != null ? e.eventId : "");
        if (e.procedure != null)
            procedure.setSelectedProcedure(e.procedure);
    }
}
