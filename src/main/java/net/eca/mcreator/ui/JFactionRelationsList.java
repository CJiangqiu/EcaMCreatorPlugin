package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaFactionElement.RelationEntry;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JFactionRelationsList extends JSimpleEntriesList<JFactionRelationEntry, RelationEntry> {

    private final Dependency[] deps;
    private final String selfElementName;

    public JFactionRelationsList(MCreator mcreator, IHelpContext gui, Dependency[] deps,
                                 String selfElementName) {
        super(mcreator, gui);
        this.deps = deps;
        this.selfElementName = selfElementName;

        add.setText(L10N.t("elementgui.ecafaction.add_relation"));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.ecafaction.relations"), 0, 0,
                getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        int prefWidth = Math.max(800, (int) (mcreator.getSize().width * 0.65));
        setPreferredSize(new Dimension(prefWidth, (int) (mcreator.getSize().height * 0.45)));
    }

    @Override
    protected JFactionRelationEntry newEntry(JPanel parent, List<JFactionRelationEntry> entryList, boolean userAction) {
        return new JFactionRelationEntry(mcreator, gui, parent, entryList, deps, selfElementName);
    }
}
