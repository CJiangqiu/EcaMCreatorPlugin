package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaRaidElement.WaveEntry;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JRaidWavesList extends JSimpleEntriesList<JRaidWaveEntry, WaveEntry> {

    public JRaidWavesList(MCreator mcreator, IHelpContext gui) {
        super(mcreator, gui);

        add.setText(L10N.t("elementgui.ecaraid.add_wave_entry"));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.ecaraid.waves"), 0, 0,
                getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        int prefWidth = Math.max(900, (int) (mcreator.getSize().width * 0.7));
        setPreferredSize(new Dimension(prefWidth, (int) (mcreator.getSize().height * 0.45)));
    }

    @Override
    protected JRaidWaveEntry newEntry(JPanel parent, List<JRaidWaveEntry> entryList, boolean userAction) {
        return new JRaidWaveEntry(mcreator, gui, parent, entryList);
    }
}
