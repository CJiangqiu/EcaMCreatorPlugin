package net.eca.mcreator.ui;

import net.eca.mcreator.element.BossShowElement.MarkerMapping;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JMarkerMappingEntry extends JSimpleListEntry<MarkerMapping> {

    private final JTextField eventId = new JTextField(16);
    private final ProcedureSelector procedure;

    public JMarkerMappingEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                               List<JMarkerMappingEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        procedure = new ProcedureSelector(
                gui.withEntry("bossshow/marker_procedure"),
                mcreator,
                L10N.t("elementgui.bossshow.marker_procedure"),
                AbstractProcedureSelector.Side.SERVER,
                true,
                null,
                deps);
        procedure.setPreferredSize(new Dimension(360, 42));

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
    public MarkerMapping getEntry() {
        MarkerMapping m = new MarkerMapping();
        m.eventId = eventId.getText();
        m.procedure = procedure.getSelectedProcedure();
        return m;
    }

    @Override
    public void setEntry(MarkerMapping e) {
        eventId.setText(e.eventId != null ? e.eventId : "");
        if (e.procedure != null)
            procedure.setSelectedProcedure(e.procedure);
    }
}
