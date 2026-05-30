package net.eca.mcreator.ui;

import net.eca.mcreator.element.BossShowElement.KeyframeMapping;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.ui.modgui.ModElementGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JKeyframeMappingsList extends JSimpleEntriesList<JKeyframeMappingEntry, KeyframeMapping> {

    private final ModElementGUI<?> modElementGUI;
    private final Dependency[] deps;

    //子条目用 modElementGUI 重新生成 ModElementHelpContext 以保留点击行为
    public JKeyframeMappingsList(MCreator mcreator, ModElementGUI<?> modElementGUI, String helpEntry, Dependency[] deps) {
        super(mcreator, modElementGUI.withEntry(helpEntry));
        this.modElementGUI = modElementGUI;
        this.deps = deps;

        add.setText(L10N.t("elementgui.bossshow.add_keyframe_mapping"));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.bossshow.keyframe_mappings"), 0, 0,
                getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        int prefWidth = Math.max(820, (int) (mcreator.getSize().width * 0.6));
        setPreferredSize(new Dimension(prefWidth, (int) (mcreator.getSize().height * 0.5)));
    }

    @Override
    protected JKeyframeMappingEntry newEntry(JPanel parent, List<JKeyframeMappingEntry> entryList, boolean userAction) {
        return new JKeyframeMappingEntry(mcreator, modElementGUI, parent, entryList, deps);
    }
}
