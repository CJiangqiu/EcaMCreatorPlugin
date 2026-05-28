package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement.MusicRule;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.SoundSelector;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JMusicRuleEntry extends JSimpleListEntry<MusicRule> {

    private static final String[] SOURCES = {"MUSIC", "MASTER", "RECORDS", "AMBIENT", "HOSTILE",
            "NEUTRAL", "PLAYERS", "BLOCKS", "VOICE", "WEATHER"};

    private final ProcedureSelector condition;
    private final SoundSelector sound;
    private final JComboBox<String> source = new JComboBox<>(SOURCES);
    private final JSpinner volume = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.1));
    private final JSpinner pitch = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.1));
    private final JCheckBox loop = new JCheckBox(L10N.t("elementgui.entity_extension.combat_music_loop"));
    private final JCheckBox strictMusicLock = new JCheckBox(L10N.t("elementgui.entity_extension.combat_music_mute_others"));

    public JMusicRuleEntry(MCreator mcreator, IHelpContext gui, JPanel parent,
                           List<JMusicRuleEntry> entryList, Dependency[] deps) {
        super(parent, entryList);

        sound = new SoundSelector(mcreator);
        condition = new ProcedureSelector(
                gui.withEntry("entity_extension/music_rule_condition"), mcreator,
                L10N.t("elementgui.entity_extension.music_rule_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowTop.setOpaque(false);
        rowTop.add(condition);
        rowTop.add(helpLabel(gui, "combat_music_sound"));
        rowTop.add(sound);

        JPanel rowBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowBottom.setOpaque(false);
        rowBottom.add(helpLabel(gui, "combat_music_source"));
        rowBottom.add(source);
        rowBottom.add(helpLabel(gui, "combat_music_volume"));
        rowBottom.add(volume);
        rowBottom.add(helpLabel(gui, "combat_music_pitch"));
        rowBottom.add(pitch);
        rowBottom.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/combat_music_loop"), loop));
        rowBottom.add(HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/combat_music_mute_others"), strictMusicLock));

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
        body.add(rowTop);
        body.add(rowBottom);
        line.add(body);
    }

    // 标签 + help 按钮：复用已有的 entity_extension/<key> 帮助文档
    private static Component helpLabel(IHelpContext gui, String key) {
        return HelpUtils.wrapWithHelpButton(gui.withEntry("entity_extension/" + key),
                L10N.label("elementgui.entity_extension." + key));
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        condition.refreshListKeepSelected();
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        condition.setEnabled(enabled);
        sound.setEnabled(enabled);
        source.setEnabled(enabled);
        volume.setEnabled(enabled);
        pitch.setEnabled(enabled);
        loop.setEnabled(enabled);
        strictMusicLock.setEnabled(enabled);
    }

    @Override
    public MusicRule getEntry() {
        MusicRule r = new MusicRule();
        r.condition = condition.getSelectedProcedure();
        r.soundEventId = sound.getSound().getUnmappedValue();
        Object src = source.getSelectedItem();
        r.soundSource = src != null ? src.toString() : "MUSIC";
        r.volume = (double) volume.getValue();
        r.pitch = (double) pitch.getValue();
        r.loop = loop.isSelected();
        r.strictMusicLock = strictMusicLock.isSelected();
        return r;
    }

    @Override
    public void setEntry(MusicRule r) {
        if (r.condition != null) condition.setSelectedProcedure(r.condition);
        sound.setSound(r.soundEventId != null ? r.soundEventId : "");
        if (r.soundSource != null) source.setSelectedItem(r.soundSource);
        volume.setValue(r.volume);
        pitch.setValue(r.pitch);
        loop.setSelected(r.loop);
        strictMusicLock.setSelected(r.strictMusicLock);
    }
}
