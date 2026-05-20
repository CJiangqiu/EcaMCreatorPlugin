package net.eca.mcreator.ui;

import net.eca.mcreator.element.BossShowElement.MarkerMapping;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JMarkerMappingsList extends JSimpleEntriesList<JMarkerMappingEntry, MarkerMapping> {

    private final Dependency[] deps;

    public JMarkerMappingsList(MCreator mcreator, IHelpContext gui, Dependency[] deps) {
        super(mcreator, gui);
        this.deps = deps;

        add.setText(L10N.t("elementgui.bossshow.add_marker_mapping"));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.bossshow.marker_mappings"), 0, 0,
                getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        setPreferredSize(new Dimension(getPreferredSize().width, (int) (mcreator.getSize().height * 0.5)));
    }

    @Override
    protected JMarkerMappingEntry newEntry(JPanel parent, List<JMarkerMappingEntry> entryList, boolean userAction) {
        return new JMarkerMappingEntry(mcreator, gui, parent, entryList, deps);
    }
}
