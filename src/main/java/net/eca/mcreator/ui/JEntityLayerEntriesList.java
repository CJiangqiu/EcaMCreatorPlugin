package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.EntityLayerEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JEntityLayerEntriesList extends JSimpleEntriesList<JEntityLayerEntry, EntityLayerEntry> {

    private final Dependency[] deps;

    public JEntityLayerEntriesList(MCreator mcreator, IHelpContext gui, Dependency[] deps) {
        super(mcreator, gui);
        this.deps = deps;

        add.setText(L10N.t("elementgui.entity_extension.add_entity_layer"));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.entity_extension.entity_layer_entries"), 0, 0,
                getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        int prefWidth = Math.max(900, (int) (mcreator.getSize().width * 0.7));
        setPreferredSize(new Dimension(prefWidth, (int) (mcreator.getSize().height * 0.35)));
    }

    @Override
    protected JEntityLayerEntry newEntry(JPanel parent, List<JEntityLayerEntry> entryList, boolean userAction) {
        return new JEntityLayerEntry(mcreator, gui, parent, entryList, deps);
    }
}
