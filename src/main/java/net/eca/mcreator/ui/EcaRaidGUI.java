package net.eca.mcreator.ui;

import net.eca.mcreator.element.EcaRaidElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.procedure.StringProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableTypeLoader;

import javax.swing.*;
import java.awt.*;

public class EcaRaidGUI extends ModElementGUI<EcaRaidElement> {

    private static final String[] BOSS_BAR_COLORS =
            {"PINK", "BLUE", "RED", "GREEN", "YELLOW", "PURPLE", "WHITE"};

    private final VTextField displayNameFixed = new VTextField(24);
    private StringProcedureSelector displayName;
    private final JComboBox<String> raiderFactionId = new JComboBox<>();
    private final JComboBox<String> structureAnchor =
            new JComboBox<>(new String[]{"NONE", "STRUCTURE", "TAG"});
    private final VTextField targetStructure = new VTextField(24);
    private final JSpinner raiderGoalPriority = new JSpinner(new SpinnerNumberModel(3, 0, 100, 1));
    private final JCheckBox endless = new JCheckBox();
    private final JComboBox<String> bossBarColor = new JComboBox<>(BOSS_BAR_COLORS);
    private final JSpinner maxDurationTicks = new JSpinner(new SpinnerNumberModel(48000, 0, 100000000, 100));
    private final JSpinner waveCooldownTicks = new JSpinner(new SpinnerNumberModel(300, 0, 1000000, 20));
    private final JSpinner participantRadius = new JSpinner(new SpinnerNumberModel(96.0, 0.0, 1024.0, 1.0));
    private final JSpinner celebrationTicks = new JSpinner(new SpinnerNumberModel(600, 0, 1000000, 20));

    private JRaidWavesList waves;

    private ProcedureSelector shouldAdvanceWave;
    private ProcedureSelector checkVictory;
    private ProcedureSelector checkDefeat;
    private ProcedureSelector onStart;
    private ProcedureSelector onWaveStart;
    private ProcedureSelector onWaveEnd;
    private ProcedureSelector onVictory;
    private ProcedureSelector onDefeat;
    private ProcedureSelector onStop;

    public EcaRaidGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        // 回调与判定在服务端求值，位置取自袭击中心（RaidContext.getCenter），世界取自 getLevel
        Dependency[] raidDeps = Dependency.fromString("x:number/y:number/z:number/world:world");

        // getDisplayName() 在注册期读取一次，此时无世界上下文，故不提供依赖
        displayName = new StringProcedureSelector(
                this.withEntry("ecaraid/display_name"), mcreator,
                L10N.t("elementgui.ecaraid.display_name"),
                AbstractProcedureSelector.Side.BOTH, displayNameFixed, 200);

        EcaFactionUtil.populateCombo(raiderFactionId, mcreator, null, true);
        // 不锚定时输入框无意义，直接禁用，避免填了却静默失效
        structureAnchor.addActionListener(e -> refreshAnchorState());
        waves = new JRaidWavesList(mcreator, this.withEntry("ecaraid/waves"));

        shouldAdvanceWave = logicSelector("should_advance_wave", raidDeps);
        checkVictory = logicSelector("check_victory", raidDeps);
        checkDefeat = logicSelector("check_defeat", raidDeps);
        onStart = actionSelector("on_start", raidDeps);
        onWaveStart = actionSelector("on_wave_start", raidDeps);
        onWaveEnd = actionSelector("on_wave_end", raidDeps);
        onVictory = actionSelector("on_victory", raidDeps);
        onDefeat = actionSelector("on_defeat", raidDeps);
        onStop = actionSelector("on_stop", raidDeps);

        addPage(L10N.t("elementgui.ecaraid.page_general"), buildGeneralPage());
        refreshAnchorState();
        addPage(L10N.t("elementgui.ecaraid.page_waves"), PanelUtils.totalCenterInPanel(waves));
        addPage(L10N.t("elementgui.ecaraid.page_events"), buildEventsPage());
    }

    private void refreshAnchorState() {
        targetStructure.setEnabled(!"NONE".equals(structureAnchor.getSelectedItem()));
    }

    private ProcedureSelector logicSelector(String key, Dependency[] deps) {
        return new ProcedureSelector(this.withEntry("ecaraid/" + key), mcreator,
                L10N.t("elementgui.ecaraid." + key),
                AbstractProcedureSelector.Side.SERVER, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, deps);
    }

    private ProcedureSelector actionSelector(String key, Dependency[] deps) {
        return new ProcedureSelector(this.withEntry("ecaraid/" + key), mcreator,
                L10N.t("elementgui.ecaraid." + key),
                AbstractProcedureSelector.Side.SERVER, deps);
    }

    private JComponent buildGeneralPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();

        addRowWithHelp(panel, "ecaraid/raider_faction_id", "elementgui.ecaraid.raider_faction_id", raiderFactionId, gbc);
        addRowWithHelp(panel, "ecaraid/structure_anchor", "elementgui.ecaraid.structure_anchor", structureAnchor, gbc);
        addRowWithHelp(panel, "ecaraid/target_structure", "elementgui.ecaraid.target_structure", targetStructure, gbc);
        addRowWithHelp(panel, "ecaraid/raider_goal_priority", "elementgui.ecaraid.raider_goal_priority", raiderGoalPriority, gbc);
        addRowWithHelp(panel, "ecaraid/endless", "elementgui.ecaraid.endless", endless, gbc);
        addRowWithHelp(panel, "ecaraid/boss_bar_color", "elementgui.ecaraid.boss_bar_color", bossBarColor, gbc);
        addRowWithHelp(panel, "ecaraid/max_duration_ticks", "elementgui.ecaraid.max_duration_ticks", maxDurationTicks, gbc);
        addRowWithHelp(panel, "ecaraid/wave_cooldown_ticks", "elementgui.ecaraid.wave_cooldown_ticks", waveCooldownTicks, gbc);
        addRowWithHelp(panel, "ecaraid/participant_radius", "elementgui.ecaraid.participant_radius", participantRadius, gbc);
        addRowWithHelp(panel, "ecaraid/celebration_ticks", "elementgui.ecaraid.celebration_ticks", celebrationTicks, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(PanelUtils.northAndCenterElement(displayName, panel, 0, 5), BorderLayout.NORTH);
        return PanelUtils.totalCenterInPanel(content);
    }

    private JComponent buildEventsPage() {
        JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
        grid.setOpaque(false);
        grid.add(shouldAdvanceWave);
        grid.add(checkVictory);
        grid.add(checkDefeat);
        grid.add(onStart);
        grid.add(onWaveStart);
        grid.add(onWaveEnd);
        grid.add(onVictory);
        grid.add(onDefeat);
        grid.add(onStop);
        return PanelUtils.totalCenterInPanel(grid);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        displayName.refreshListKeepSelected();
        EcaFactionUtil.populateCombo(raiderFactionId, mcreator, null, true);
        waves.reloadDataLists();
        for (ProcedureSelector s : new ProcedureSelector[]{shouldAdvanceWave, checkVictory, checkDefeat,
                onStart, onWaveStart, onWaveEnd, onVictory, onDefeat, onStop}) {
            s.refreshListKeepSelected();
        }
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(EcaRaidElement element) {
        displayName.setSelectedProcedure(element.displayName);
        raiderFactionId.setSelectedItem(element.raiderFactionId != null && !element.raiderFactionId.isEmpty()
                ? element.raiderFactionId : EcaFactionUtil.NONE);
        if (element.structureAnchor != null) structureAnchor.setSelectedItem(element.structureAnchor);
        targetStructure.setText(element.targetStructure != null ? element.targetStructure : "");
        refreshAnchorState();
        raiderGoalPriority.setValue(element.raiderGoalPriority);
        endless.setSelected(element.endless);
        if (element.bossBarColor != null) bossBarColor.setSelectedItem(element.bossBarColor);
        maxDurationTicks.setValue(element.maxDurationTicks);
        waveCooldownTicks.setValue(element.waveCooldownTicks);
        participantRadius.setValue(element.participantRadius);
        celebrationTicks.setValue(element.celebrationTicks);
        if (element.waves != null) waves.setEntries(element.waves);
        shouldAdvanceWave.setSelectedProcedure(element.shouldAdvanceWave);
        checkVictory.setSelectedProcedure(element.checkVictory);
        checkDefeat.setSelectedProcedure(element.checkDefeat);
        onStart.setSelectedProcedure(element.onStart);
        onWaveStart.setSelectedProcedure(element.onWaveStart);
        onWaveEnd.setSelectedProcedure(element.onWaveEnd);
        onVictory.setSelectedProcedure(element.onVictory);
        onDefeat.setSelectedProcedure(element.onDefeat);
        onStop.setSelectedProcedure(element.onStop);
    }

    @Override
    public EcaRaidElement getElementFromGUI() {
        EcaRaidElement element = new EcaRaidElement(modElement);
        element.displayName = displayName.getSelectedProcedure();
        element.raiderFactionId = EcaFactionUtil.toStoredValue(raiderFactionId);
        Object anchor = structureAnchor.getSelectedItem();
        element.structureAnchor = anchor != null ? anchor.toString() : "NONE";
        element.targetStructure = targetStructure.getText().trim();
        element.raiderGoalPriority = (int) raiderGoalPriority.getValue();
        element.endless = endless.isSelected();
        Object c = bossBarColor.getSelectedItem();
        element.bossBarColor = c != null ? c.toString() : "RED";
        element.maxDurationTicks = (int) maxDurationTicks.getValue();
        element.waveCooldownTicks = (int) waveCooldownTicks.getValue();
        element.participantRadius = (double) participantRadius.getValue();
        element.celebrationTicks = (int) celebrationTicks.getValue();
        element.waves = waves.getEntries();
        element.shouldAdvanceWave = shouldAdvanceWave.getSelectedProcedure();
        element.checkVictory = checkVictory.getSelectedProcedure();
        element.checkDefeat = checkDefeat.getSelectedProcedure();
        element.onStart = onStart.getSelectedProcedure();
        element.onWaveStart = onWaveStart.getSelectedProcedure();
        element.onWaveEnd = onWaveEnd.getSelectedProcedure();
        element.onVictory = onVictory.getSelectedProcedure();
        element.onDefeat = onDefeat.getSelectedProcedure();
        element.onStop = onStop.getSelectedProcedure();
        return element;
    }

    private static GridBagConstraints defaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 8);
        return gbc;
    }

    private void addRowWithHelp(JPanel panel, String helpEntry, String l10nKey,
                                JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(HelpUtils.wrapWithHelpButton(this.withEntry(helpEntry), L10N.label(l10nKey)), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
        gbc.gridy++;
    }
}
