package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaRaidElement.WaveEntry;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.DataListComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JRaidWaveEntry extends JSimpleListEntry<WaveEntry> {

    private final MCreator mcreator;
    private final JSpinner wave = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final DataListComboBox entityType;
    private final JComboBox<String> factionId = new JComboBox<>();
    private final JSpinner count = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final JSpinner weight = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final JCheckBox leader = new JCheckBox(L10N.t("elementgui.ecaraid.wave_leader"));
    private final JSpinner spawnDelay = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 20));
    private final JSpinner spawnRadius = new JSpinner(new SpinnerNumberModel(24.0, 1.0, 512.0, 1.0));
    private boolean entryEnabled = true;

    public JRaidWaveEntry(MCreator mcreator, IHelpContext gui, JPanel parent, List<JRaidWaveEntry> entryList) {
        super(parent, entryList);

        this.mcreator = mcreator;
        entityType = new DataListComboBox(mcreator,
                ElementUtil.loadAllSpawnableEntities(mcreator.getWorkspace()));
        entityType.setPreferredSize(new Dimension(240, 28));
        EcaFactionUtil.populateCombo(factionId, mcreator, null, true);
        factionId.setPreferredSize(new Dimension(180, 28));
        factionId.addActionListener(e -> refreshFactionState());

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setOpaque(false);
        row.add(helpLabel(gui, "wave_index"));
        row.add(wave);
        row.add(entityType);
        row.add(helpLabel(gui, "wave_faction"));
        row.add(factionId);
        row.add(helpLabel(gui, "wave_count"));
        row.add(count);
        row.add(helpLabel(gui, "wave_weight"));
        row.add(weight);
        row.add(HelpUtils.wrapWithHelpButton(gui.withEntry("ecaraid/wave_leader"), leader));
        row.add(helpLabel(gui, "wave_spawn_delay"));
        row.add(spawnDelay);
        row.add(helpLabel(gui, "wave_spawn_radius"));
        row.add(spawnRadius);
        line.add(row);
        refreshFactionState();
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        EcaFactionUtil.populateCombo(factionId, mcreator, null, true);
        refreshFactionState();
    }

    private boolean isFactionGroup() {
        return factionId.getSelectedItem() != null
                && !EcaFactionUtil.NONE.equals(factionId.getSelectedItem());
    }

    private void refreshFactionState() {
        boolean factionGroup = isFactionGroup();
        weight.setEnabled(entryEnabled && factionGroup);
        leader.setEnabled(entryEnabled && !factionGroup);
        if (factionGroup) leader.setSelected(false);
    }

    private static Component helpLabel(IHelpContext gui, String key) {
        return HelpUtils.wrapWithHelpButton(gui.withEntry("ecaraid/" + key),
                L10N.label("elementgui.ecaraid." + key));
    }

    @Override
    protected void setEntryEnabled(boolean enabled) {
        entryEnabled = enabled;
        wave.setEnabled(enabled);
        entityType.setEnabled(enabled);
        factionId.setEnabled(enabled);
        count.setEnabled(enabled);
        spawnDelay.setEnabled(enabled);
        spawnRadius.setEnabled(enabled);
        refreshFactionState();
    }

    @Override
    public WaveEntry getEntry() {
        WaveEntry e = new WaveEntry();
        e.wave = (int) wave.getValue();
        e.entityType = new EntityEntry(mcreator.getWorkspace(), entityType.getSelectedItem());
        e.count = (int) count.getValue();
        e.factionId = EcaFactionUtil.toStoredValue(factionId);
        e.weight = (int) weight.getValue();
        e.leader = leader.isSelected();
        e.spawnDelay = (int) spawnDelay.getValue();
        e.spawnRadius = (double) spawnRadius.getValue();
        return e;
    }

    @Override
    public void setEntry(WaveEntry e) {
        wave.setValue(e.wave);
        if (e.entityType != null) entityType.setSelectedItem(e.entityType);
        count.setValue(e.count);
        factionId.setSelectedItem(e.factionId != null && !e.factionId.isEmpty()
                ? e.factionId : EcaFactionUtil.NONE);
        weight.setValue(e.weight > 0 ? e.weight : 1);
        leader.setSelected(e.leader);
        spawnDelay.setValue(e.spawnDelay);
        spawnRadius.setValue(e.spawnRadius);
        refreshFactionState();
    }
}
