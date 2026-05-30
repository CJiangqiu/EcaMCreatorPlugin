package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaItemExtensionElement.TooltipLine;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.ui.modgui.ModElementGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JTooltipLinesList extends JSimpleEntriesList<JTooltipLineEntry, TooltipLine> {

    private final ModElementGUI<?> modElementGUI;
    private final Dependency[] deps;

    //同时传 ModElementGUI 和顶层 help 入口：父类拿 ModElementHelpContext 用于本身的边框，子条目用 modElementGUI 重新生成 ModElementHelpContext 以保留点击行为
    public JTooltipLinesList(MCreator mcreator, ModElementGUI<?> modElementGUI, String helpEntry, Dependency[] deps) {
        super(mcreator, modElementGUI.withEntry(helpEntry));
        this.modElementGUI = modElementGUI;
        this.deps = deps;

        add.setText(L10N.t("elementgui.ecaitemextension.add_tooltip_line"));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.ecaitemextension.tooltip"), 0, 0,
                getFont().deriveFont(12.0f),
                Theme.current().getForegroundColor()));
        int prefWidth = Math.max(900, (int) (mcreator.getSize().width * 0.7));
        setPreferredSize(new Dimension(prefWidth, (int) (mcreator.getSize().height * 0.4)));
    }

    @Override
    protected JTooltipLineEntry newEntry(JPanel parent, List<JTooltipLineEntry> entryList, boolean userAction) {
        return new JTooltipLineEntry(mcreator, modElementGUI, parent, entryList, deps);
    }
}
