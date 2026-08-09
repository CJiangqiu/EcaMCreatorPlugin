package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.DataListComboBox;
import net.mcreator.ui.minecraft.TextureComboBox;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.NumberProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;

public class EntityExtensionGUI extends ModElementGUI<EntityExtensionElement> {

    // General
    private DataListComboBox entityType;
    private final JSpinner priority = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
    private final JComboBox<String> factionId = new JComboBox<>();
    private final JCheckBox enableForceLoading = new JCheckBox();

    // Condition Procedures
    private ProcedureSelector bossBarCondition;

    // Boss Bar
    private final JCheckBox bossBarEnabled = new JCheckBox();
    private final JCheckBox bossBarFrameEnableTexture = new JCheckBox();
    private TextureComboBox bossBarFrameTexture;
    private final JCheckBox bossBarFillEnableTexture = new JCheckBox();
    private TextureComboBox bossBarFillTexture;
    private final JCheckBox bossBarFrameShaderEnabled = new JCheckBox();
    private final JComboBox<String> bossBarFrameRenderType = new JComboBox<>();
    private final JSpinner bossBarFrameWidth = new JSpinner(new SpinnerNumberModel(182, 1, 2048, 1));
    private final JSpinner bossBarFrameHeight = new JSpinner(new SpinnerNumberModel(5, 1, 2048, 1));
    private final JCheckBox bossBarFillShaderEnabled = new JCheckBox();
    private final JComboBox<String> bossBarFillRenderType = new JComboBox<>();
    private final JSpinner bossBarFillWidth = new JSpinner(new SpinnerNumberModel(182, 1, 2048, 1));
    private final JSpinner bossBarFillHeight = new JSpinner(new SpinnerNumberModel(5, 1, 2048, 1));
    private final JSpinner bossBarFrameOffsetX = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFrameOffsetY = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFillOffsetX = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFillOffsetY = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFrameAlpha = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
    private final JSpinner bossBarFillAlpha = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));

    // Custom Health Display
    private final JCheckBox customHealthEnabled = new JCheckBox();
    private NumberProcedureSelector customHealthValue;
    private final JCheckBox customMaxHealthEnabled = new JCheckBox();
    private NumberProcedureSelector customMaxHealthValue;

    // Entity Layer
    private final JCheckBox entityLayerEnabled = new JCheckBox();
    private JEntityLayerEntriesList entityLayerEntries;

    // Global Fog
    private final JCheckBox globalFogEnabled = new JCheckBox();
    private JFogEntriesList fogEntries;

    // Global Skybox
    private final JCheckBox globalSkyboxEnabled = new JCheckBox();
    private JSkyboxEntriesList skyboxEntries;

    // Combat Music
    private final JCheckBox combatMusicEnabled = new JCheckBox();
    private JMusicRulesList musicRules;

    public EntityExtensionGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        Dependency[] entityDeps = Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity");

        entityType = new DataListComboBox(mcreator,
                ElementUtil.loadAllSpawnableEntities(mcreator.getWorkspace()));
        // 本类型实体自动加入的阵营；首项 (None) 表示不加入任何阵营
        EcaFactionUtil.populateCombo(factionId, mcreator, null, true);
        bossBarFrameTexture = new TextureComboBox(mcreator, TextureType.SCREEN);
        bossBarFrameTexture.setAddPNGExtension(false);
        bossBarFrameTexture.setPreferredSize(new Dimension(300, 28));
        bossBarFillTexture = new TextureComboBox(mcreator, TextureType.SCREEN);
        bossBarFillTexture.setAddPNGExtension(false);
        bossBarFillTexture.setPreferredSize(new Dimension(300, 28));
        musicRules = new JMusicRulesList(mcreator, this.withEntry("entity_extension/music_rules"), entityDeps);
        fogEntries = new JFogEntriesList(mcreator, this.withEntry("entity_extension/fog_entries"), entityDeps);
        skyboxEntries = new JSkyboxEntriesList(mcreator, this.withEntry("entity_extension/skybox_entries"), entityDeps);
        bossBarCondition = new ProcedureSelector(
                this.withEntry("entity_extension/boss_bar_condition"), mcreator,
                L10N.t("elementgui.entity_extension.boss_bar_condition"),
                AbstractProcedureSelector.Side.CLIENT, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, entityDeps);
        entityLayerEntries = new JEntityLayerEntriesList(mcreator, this.withEntry("entity_extension/entity_layer_entries"), entityDeps);
        customHealthValue = new NumberProcedureSelector(
                this.withEntry("entity_extension/custom_health_value"), mcreator,
                L10N.t("elementgui.entity_extension.custom_health_value"),
                AbstractProcedureSelector.Side.BOTH,
                new JSpinner(new SpinnerNumberModel(20, 0.0, 1000000, 1)), 0, entityDeps);
        customMaxHealthValue = new NumberProcedureSelector(
                this.withEntry("entity_extension/custom_max_health_value"), mcreator,
                L10N.t("elementgui.entity_extension.custom_max_health_value"),
                AbstractProcedureSelector.Side.BOTH,
                new JSpinner(new SpinnerNumberModel(20, 0.0, 1000000, 1)), 0, entityDeps);

        // === General ===
        JPanel generalPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();

        addRowWithHelp(generalPanel, "entity_extension/entity_type",
                "elementgui.entity_extension.entity_type", entityType, gbc);
        addRowWithHelp(generalPanel, "entity_extension/priority",
                "elementgui.entity_extension.priority", priority, gbc);
        addRowWithHelp(generalPanel, "entity_extension/faction_id",
                "elementgui.entity_extension.faction_id", factionId, gbc);
        addRowWithHelp(generalPanel, "entity_extension/enable_force_loading",
                "elementgui.entity_extension.enable_force_loading", enableForceLoading, gbc);
        addPage(L10N.t("elementgui.entityextension.general"), PanelUtils.totalCenterInPanel(generalPanel), false);

        // === Boss Bar ===
        JPanel bossBarPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_enabled",
                "elementgui.entity_extension.boss_bar_enabled", bossBarEnabled, gbc);
        addFullWidthComponent(bossBarPanel, bossBarCondition, gbc);

        // --- Frame section ---
        addSectionLabel(bossBarPanel, "elementgui.entity_extension.boss_bar_frame_section", gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_frame_texture",
                "elementgui.entity_extension.boss_bar_frame_texture",
                buildTextureRow(bossBarFrameEnableTexture, bossBarFrameTexture), gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_frame_shader",
                "elementgui.entity_extension.boss_bar_frame_shader", buildShaderRow(
                        bossBarFrameShaderEnabled, bossBarFrameRenderType,
                        bossBarFrameWidth, bossBarFrameHeight), gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_frame_offset",
                "elementgui.entity_extension.boss_bar_frame_offset", buildOffsetRow(
                        bossBarFrameOffsetX, bossBarFrameOffsetY), gbc);

        // --- Fill section ---
        addSectionLabel(bossBarPanel, "elementgui.entity_extension.boss_bar_fill_section", gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_fill_texture",
                "elementgui.entity_extension.boss_bar_fill_texture",
                buildTextureRow(bossBarFillEnableTexture, bossBarFillTexture), gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_fill_shader",
                "elementgui.entity_extension.boss_bar_fill_shader", buildShaderRow(
                        bossBarFillShaderEnabled, bossBarFillRenderType,
                        bossBarFillWidth, bossBarFillHeight), gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_fill_offset",
                "elementgui.entity_extension.boss_bar_fill_offset", buildOffsetRow(
                        bossBarFillOffsetX, bossBarFillOffsetY), gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_frame_alpha",
                "elementgui.entity_extension.boss_bar_frame_alpha", bossBarFrameAlpha, gbc);
        addRowWithHelp(bossBarPanel, "entity_extension/boss_bar_fill_alpha",
                "elementgui.entity_extension.boss_bar_fill_alpha", bossBarFillAlpha, gbc);

        // --- Custom Health Display section ---
        addSectionLabel(bossBarPanel, "elementgui.entity_extension.custom_health_section", gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/custom_health_enabled",
                "elementgui.entity_extension.custom_health_enabled", customHealthEnabled, gbc);
        // NumberProcedureSelector has its own label+help, add spanning both columns
        addFullWidthComponent(bossBarPanel, customHealthValue, gbc);

        addRowWithHelp(bossBarPanel, "entity_extension/custom_max_health_enabled",
                "elementgui.entity_extension.custom_max_health_enabled", customMaxHealthEnabled, gbc);
        addFullWidthComponent(bossBarPanel, customMaxHealthValue, gbc);

        addPage(L10N.t("elementgui.entityextension.bossbar"), PanelUtils.totalCenterInPanel(bossBarPanel), false);

        // === Entity Layer ===
        JPanel layerPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(layerPanel, "entity_extension/entity_layer_enabled",
                "elementgui.entity_extension.entity_layer_enabled", entityLayerEnabled, gbc);
        addFullWidthComponent(layerPanel, entityLayerEntries, gbc);
        addPage(L10N.t("elementgui.entityextension.layer"), PanelUtils.totalCenterInPanel(layerPanel), false);

        // === Global Fog ===
        JPanel fogPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(fogPanel, "entity_extension/global_fog_enabled",
                "elementgui.entity_extension.global_fog_enabled", globalFogEnabled, gbc);
        addFullWidthComponent(fogPanel, fogEntries, gbc);
        addPage(L10N.t("elementgui.entityextension.fog"), PanelUtils.totalCenterInPanel(fogPanel), false);

        // === Global Skybox ===
        JPanel skyboxPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(skyboxPanel, "entity_extension/global_skybox_enabled",
                "elementgui.entity_extension.global_skybox_enabled", globalSkyboxEnabled, gbc);
        addFullWidthComponent(skyboxPanel, skyboxEntries, gbc);
        addPage(L10N.t("elementgui.entityextension.skybox"), PanelUtils.totalCenterInPanel(skyboxPanel), false);

        // === Combat Music ===
        JPanel musicPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(musicPanel, "entity_extension/combat_music_enabled",
                "elementgui.entity_extension.combat_music_enabled", combatMusicEnabled, gbc);
        addFullWidthComponent(musicPanel, musicRules, gbc);
        addPage(L10N.t("elementgui.entityextension.music"), PanelUtils.totalCenterInPanel(musicPanel), false);

        // Setup all enable/disable toggles
        refreshShaderCombos();
        EcaFactionUtil.populateCombo(factionId, mcreator, null, true);
        setupBossBarToggle();
        setupEnableToggle(entityLayerEnabled, entityLayerEntries);
        setupEnableToggle(globalFogEnabled, fogEntries);
        setupEnableToggle(globalSkyboxEnabled, skyboxEntries);
        setupMusicToggle();
    }

    private JPanel buildShaderRow(JCheckBox enableBox, JComboBox<String> presetCombo,
                                  JSpinner widthSpinner, JSpinner heightSpinner) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(enableBox);
        row.add(L10N.label("elementgui.entity_extension.shader_preset"));
        row.add(presetCombo);
        row.add(L10N.label("elementgui.entity_extension.shader_width"));
        row.add(widthSpinner);
        row.add(L10N.label("elementgui.entity_extension.shader_height"));
        row.add(heightSpinner);
        return row;
    }

    private static JPanel buildOffsetRow(JSpinner offsetX, JSpinner offsetY) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(new JLabel("X:"));
        row.add(offsetX);
        row.add(new JLabel("Y:"));
        row.add(offsetY);
        return row;
    }

    private static JPanel buildTextureRow(JCheckBox enable, TextureComboBox holder) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(enable);
        holder.setPreferredSize(new Dimension(300, 28));
        row.add(holder);
        return row;
    }

    private void setupBossBarToggle() {
        Runnable update = () -> {
            boolean bb = bossBarEnabled.isSelected();
            bossBarCondition.setEnabled(bb);
            bossBarFrameEnableTexture.setEnabled(bb);
            bossBarFrameTexture.setEnabled(bb && bossBarFrameEnableTexture.isSelected());
            bossBarFillEnableTexture.setEnabled(bb);
            bossBarFillTexture.setEnabled(bb && bossBarFillEnableTexture.isSelected());
            bossBarFrameShaderEnabled.setEnabled(bb);
            bossBarFillShaderEnabled.setEnabled(bb);
            bossBarFrameOffsetX.setEnabled(bb);
            bossBarFrameOffsetY.setEnabled(bb);
            bossBarFillOffsetX.setEnabled(bb);
            bossBarFillOffsetY.setEnabled(bb);
            bossBarFrameAlpha.setEnabled(bb);
            bossBarFillAlpha.setEnabled(bb);
            bossBarFrameRenderType.setEnabled(bb && bossBarFrameShaderEnabled.isSelected());
            bossBarFrameWidth.setEnabled(bb && bossBarFrameShaderEnabled.isSelected());
            bossBarFrameHeight.setEnabled(bb && bossBarFrameShaderEnabled.isSelected());
            bossBarFillRenderType.setEnabled(bb && bossBarFillShaderEnabled.isSelected());
            bossBarFillWidth.setEnabled(bb && bossBarFillShaderEnabled.isSelected());
            bossBarFillHeight.setEnabled(bb && bossBarFillShaderEnabled.isSelected());
            // Custom health sub-section
            customHealthEnabled.setEnabled(bb);
            customHealthValue.setEnabled(bb && customHealthEnabled.isSelected());
            customMaxHealthEnabled.setEnabled(bb);
            customMaxHealthValue.setEnabled(bb && customMaxHealthEnabled.isSelected());
        };
        update.run();
        bossBarEnabled.addItemListener(e -> update.run());
        bossBarFrameEnableTexture.addItemListener(e -> update.run());
        bossBarFillEnableTexture.addItemListener(e -> update.run());
        bossBarFrameShaderEnabled.addItemListener(e -> update.run());
        bossBarFillShaderEnabled.addItemListener(e -> update.run());
        customHealthEnabled.addItemListener(e -> update.run());
        customMaxHealthEnabled.addItemListener(e -> update.run());
    }

    private void setupMusicToggle() {
        Runnable update = () -> {
            boolean mc = combatMusicEnabled.isSelected();
            musicRules.setEnabled(mc);
        };
        update.run();
        combatMusicEnabled.addItemListener(e -> update.run());
    }

    private static void setupEnableToggle(JCheckBox enableBox, JComponent... components) {
        Runnable update = () -> {
            boolean sel = enableBox.isSelected();
            for (JComponent c : components) c.setEnabled(sel);
        };
        update.run();
        enableBox.addItemListener(e -> update.run());
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(EntityExtensionElement element) {
        entityType.setSelectedItem(element.entityType);
        factionId.setSelectedItem(element.factionId != null && !element.factionId.isEmpty()
                ? element.factionId : EcaFactionUtil.NONE);
        priority.setValue(element.priority);
        enableForceLoading.setSelected(element.enableForceLoading);

        if (element.bossBarCondition != null)
            bossBarCondition.setSelectedProcedure(element.bossBarCondition);

        bossBarEnabled.setSelected(element.bossBarEnabled);
        bossBarFrameEnableTexture.setSelected(element.bossBarFrameEnableTexture);
        bossBarFrameTexture.setTextureFromTextureName(nonNull(element.bossBarFrameTexture));
        bossBarFillEnableTexture.setSelected(element.bossBarFillEnableTexture);
        bossBarFillTexture.setTextureFromTextureName(nonNull(element.bossBarFillTexture));
        bossBarFrameShaderEnabled.setSelected(element.bossBarFrameShaderEnabled);
        setCombo(bossBarFrameRenderType, element.bossBarFrameRenderType);
        bossBarFrameWidth.setValue(element.bossBarFrameWidth);
        bossBarFrameHeight.setValue(element.bossBarFrameHeight);
        bossBarFillShaderEnabled.setSelected(element.bossBarFillShaderEnabled);
        setCombo(bossBarFillRenderType, element.bossBarFillRenderType);
        bossBarFillWidth.setValue(element.bossBarFillWidth);
        bossBarFillHeight.setValue(element.bossBarFillHeight);
        bossBarFrameOffsetX.setValue(element.bossBarFrameOffsetX);
        bossBarFrameOffsetY.setValue(element.bossBarFrameOffsetY);
        bossBarFillOffsetX.setValue(element.bossBarFillOffsetX);
        bossBarFillOffsetY.setValue(element.bossBarFillOffsetY);
        bossBarFrameAlpha.setValue(element.bossBarFrameAlpha);
        bossBarFillAlpha.setValue(element.bossBarFillAlpha);

        customHealthEnabled.setSelected(element.customHealthEnabled);
        if (element.customHealthValue != null)
            customHealthValue.setSelectedProcedure(element.customHealthValue);
        customMaxHealthEnabled.setSelected(element.customMaxHealthEnabled);
        if (element.customMaxHealthValue != null)
            customMaxHealthValue.setSelectedProcedure(element.customMaxHealthValue);

        entityLayerEnabled.setSelected(element.entityLayerEnabled);
        if (element.entityLayerEntries != null)
            entityLayerEntries.setEntries(element.entityLayerEntries);

        globalFogEnabled.setSelected(element.globalFogEnabled);
        if (element.fogEntries != null)
            fogEntries.setEntries(element.fogEntries);

        globalSkyboxEnabled.setSelected(element.globalSkyboxEnabled);
        if (element.skyboxEntries != null)
            skyboxEntries.setEntries(element.skyboxEntries);

        combatMusicEnabled.setSelected(element.combatMusicEnabled);
        if (element.musicRules != null)
            musicRules.setEntries(element.musicRules);
    }

    @Override
    public EntityExtensionElement getElementFromGUI() {
        EntityExtensionElement element = new EntityExtensionElement(modElement);

        element.entityType = new EntityEntry(modElement.getWorkspace(),
                entityType.getSelectedItem());
        element.priority = (int) priority.getValue();
        // 存阵营元素名，生成时再解析为注册名；(None) 存为空
        element.factionId = EcaFactionUtil.toStoredValue(factionId);
        element.enableForceLoading = enableForceLoading.isSelected();

        element.bossBarCondition = bossBarCondition.getSelectedProcedure();

        element.bossBarEnabled = bossBarEnabled.isSelected();
        element.bossBarFrameEnableTexture = bossBarFrameEnableTexture.isSelected();
        element.bossBarFrameTexture = bossBarFrameTexture.getTextureName();
        element.bossBarFillEnableTexture = bossBarFillEnableTexture.isSelected();
        element.bossBarFillTexture = bossBarFillTexture.getTextureName();
        element.bossBarFrameShaderEnabled = bossBarFrameShaderEnabled.isSelected();
        element.bossBarFrameRenderType = getCombo(bossBarFrameRenderType);
        element.bossBarFrameWidth = (int) bossBarFrameWidth.getValue();
        element.bossBarFrameHeight = (int) bossBarFrameHeight.getValue();
        element.bossBarFillShaderEnabled = bossBarFillShaderEnabled.isSelected();
        element.bossBarFillRenderType = getCombo(bossBarFillRenderType);
        element.bossBarFillWidth = (int) bossBarFillWidth.getValue();
        element.bossBarFillHeight = (int) bossBarFillHeight.getValue();
        element.bossBarFrameOffsetX = (int) bossBarFrameOffsetX.getValue();
        element.bossBarFrameOffsetY = (int) bossBarFrameOffsetY.getValue();
        element.bossBarFillOffsetX = (int) bossBarFillOffsetX.getValue();
        element.bossBarFillOffsetY = (int) bossBarFillOffsetY.getValue();
        element.bossBarFrameAlpha = (int) bossBarFrameAlpha.getValue();
        element.bossBarFillAlpha = (int) bossBarFillAlpha.getValue();

        element.customHealthEnabled = customHealthEnabled.isSelected();
        element.customHealthValue = customHealthValue.getSelectedProcedure();
        element.customMaxHealthEnabled = customMaxHealthEnabled.isSelected();
        element.customMaxHealthValue = customMaxHealthValue.getSelectedProcedure();

        element.entityLayerEnabled = entityLayerEnabled.isSelected();
        element.entityLayerEntries = entityLayerEntries.getEntries();

        element.globalFogEnabled = globalFogEnabled.isSelected();
        element.fogEntries = fogEntries.getEntries();

        element.globalSkyboxEnabled = globalSkyboxEnabled.isSelected();
        element.skyboxEntries = skyboxEntries.getEntries();

        element.combatMusicEnabled = combatMusicEnabled.isSelected();
        element.musicRules = musicRules.getEntries();

        return element;
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        refreshShaderCombos();
        EcaFactionUtil.populateCombo(factionId, mcreator, null, true);
        bossBarCondition.refreshListKeepSelected();
        entityLayerEntries.reloadDataLists();
        fogEntries.reloadDataLists();
        skyboxEntries.reloadDataLists();
        musicRules.reloadDataLists();
        customHealthValue.refreshListKeepSelected();
        customMaxHealthValue.refreshListKeepSelected();
    }

    private void refreshShaderCombos() {
        String[] presets = ShaderPresetUtil.getAvailableShaderPresets(mcreator);
        ShaderPresetUtil.populateCombo(bossBarFrameRenderType, presets,
                (String) bossBarFrameRenderType.getSelectedItem());
        ShaderPresetUtil.populateCombo(bossBarFillRenderType, presets,
                (String) bossBarFillRenderType.getSelectedItem());
    }

    private static void setCombo(JComboBox<String> combo, String value) {
        if (value != null && !value.isEmpty()) {
            combo.setSelectedItem(value);
        } else {
            combo.setSelectedIndex(0);
        }
    }

    private static String getCombo(JComboBox<String> combo) {
        String val = (String) combo.getSelectedItem();
        return "(None)".equals(val) ? "" : (val != null ? val : "");
    }

    private static String nonNull(String s) {
        return s != null ? s : "";
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

    private static void addFullWidthComponent(JPanel panel, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
    }

    private static void addSectionLabel(JPanel panel, String l10nKey, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 8, 2, 8);
        JLabel label = L10N.label(l10nKey);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 8, 4, 8);
    }

}
