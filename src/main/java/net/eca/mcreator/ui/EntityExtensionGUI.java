package net.eca.mcreator.ui;

import net.eca.mcreator.element.EntityExtensionElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.parts.Sound;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.DataListComboBox;
import net.mcreator.ui.dialogs.TypedTextureSelectorDialog;
import net.mcreator.ui.minecraft.SoundSelector;
import net.mcreator.ui.minecraft.TextureHolder;
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
    private final JCheckBox enableForceLoading = new JCheckBox();

    // Condition Procedures
    private ProcedureSelector bossBarCondition;
    private ProcedureSelector entityLayerCondition;
    private ProcedureSelector fogCondition;
    private ProcedureSelector skyboxCondition;
    private ProcedureSelector musicCondition;

    // Boss Bar
    private final JCheckBox bossBarEnabled = new JCheckBox();
    private final JCheckBox bossBarFrameEnableTexture = new JCheckBox();
    private TextureHolder bossBarFrameTexture;
    private final JCheckBox bossBarFillEnableTexture = new JCheckBox();
    private TextureHolder bossBarFillTexture;
    private final JCheckBox bossBarFrameShaderEnabled = new JCheckBox();
    private final JComboBox<String> bossBarFrameRenderType = new JComboBox<>(getRenderTypeOptions());
    private final JSpinner bossBarFrameWidth = new JSpinner(new SpinnerNumberModel(182, 1, 2048, 1));
    private final JSpinner bossBarFrameHeight = new JSpinner(new SpinnerNumberModel(5, 1, 2048, 1));
    private final JCheckBox bossBarFillShaderEnabled = new JCheckBox();
    private final JComboBox<String> bossBarFillRenderType = new JComboBox<>(getRenderTypeOptions());
    private final JSpinner bossBarFillWidth = new JSpinner(new SpinnerNumberModel(182, 1, 2048, 1));
    private final JSpinner bossBarFillHeight = new JSpinner(new SpinnerNumberModel(5, 1, 2048, 1));
    private final JSpinner bossBarFrameOffsetX = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFrameOffsetY = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFillOffsetX = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));
    private final JSpinner bossBarFillOffsetY = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 1));

    // Custom Health Display
    private final JCheckBox customHealthEnabled = new JCheckBox();
    private NumberProcedureSelector customHealthValue;
    private final JCheckBox customMaxHealthEnabled = new JCheckBox();
    private NumberProcedureSelector customMaxHealthValue;

    // Entity Layer
    private final JCheckBox entityLayerEnabled = new JCheckBox();
    private final JComboBox<String> entityLayerRenderType = new JComboBox<>(getRenderTypeOptions());
    private final JCheckBox entityLayerGlow = new JCheckBox();
    private final JCheckBox entityLayerHurtOverlay = new JCheckBox();
    private final JSpinner entityLayerAlpha = new JSpinner(new SpinnerNumberModel(0.8, 0.0, 1.0, 0.05));

    // Global Fog
    private final JCheckBox globalFogEnabled = new JCheckBox();
    private final JCheckBox globalFogGlobalMode = new JCheckBox();
    private final JSpinner globalFogRadius = new JSpinner(new SpinnerNumberModel(32.0, 0.0, 1024.0, 1.0));
    private JColor globalFogColor;
    private final JSpinner globalFogTerrainStart = new JSpinner(new SpinnerNumberModel(0.25, 0.0, 10.0, 0.01));
    private final JSpinner globalFogTerrainEnd = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.01));
    private final JSpinner globalFogSkyStart = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.01));
    private final JSpinner globalFogSkyEnd = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.01));
    private final JComboBox<String> globalFogShape = new JComboBox<>(new String[]{"SPHERE", "CYLINDER"});

    // Global Skybox
    private final JCheckBox globalSkyboxEnabled = new JCheckBox();
    private final JCheckBox globalSkyboxEnableTexture = new JCheckBox();
    private TextureHolder globalSkyboxTexture;
    private final JCheckBox globalSkyboxEnableShader = new JCheckBox();
    private final JComboBox<String> globalSkyboxShaderRenderType = new JComboBox<>(getSkyboxShaderOptions());
    private final JSpinner globalSkyboxAlpha = new JSpinner(new SpinnerNumberModel(0.9, 0.0, 1.0, 0.05));
    private final JSpinner globalSkyboxSize = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 10.0));

    // Combat Music
    private final JCheckBox combatMusicEnabled = new JCheckBox();
    private SoundSelector combatMusicSound;
    private final JComboBox<String> combatMusicSoundSource = new JComboBox<>(
            new String[]{"MUSIC", "MASTER", "RECORDS", "AMBIENT", "HOSTILE", "NEUTRAL", "PLAYERS", "BLOCKS", "VOICE", "WEATHER"});
    private final JSpinner combatMusicVolume = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.1));
    private final JSpinner combatMusicPitch = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 10.0, 0.1));
    private final JCheckBox combatMusicLoop = new JCheckBox();
    private final JCheckBox combatMusicStrictLock = new JCheckBox();

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
        bossBarFrameTexture = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.SCREEN), 28);
        bossBarFrameTexture.setPreferredSize(new Dimension(300, 28));
        bossBarFillTexture = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.SCREEN), 28);
        bossBarFillTexture.setPreferredSize(new Dimension(300, 28));
        globalFogColor = new JColor(mcreator, false, false);
        globalSkyboxTexture = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.SCREEN), 28);
        combatMusicSound = new SoundSelector(mcreator);
        bossBarCondition = new ProcedureSelector(
                this.withEntry("entity_extension/boss_bar_condition"), mcreator,
                L10N.t("elementgui.entity_extension.boss_bar_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, entityDeps);
        entityLayerCondition = new ProcedureSelector(
                this.withEntry("entity_extension/entity_layer_condition"), mcreator,
                L10N.t("elementgui.entity_extension.entity_layer_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, entityDeps);
        fogCondition = new ProcedureSelector(
                this.withEntry("entity_extension/fog_condition"), mcreator,
                L10N.t("elementgui.entity_extension.fog_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, entityDeps);
        skyboxCondition = new ProcedureSelector(
                this.withEntry("entity_extension/skybox_condition"), mcreator,
                L10N.t("elementgui.entity_extension.skybox_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, entityDeps);
        musicCondition = new ProcedureSelector(
                this.withEntry("entity_extension/music_condition"), mcreator,
                L10N.t("elementgui.entity_extension.music_condition"),
                AbstractProcedureSelector.Side.BOTH, true,
                VariableTypeLoader.BuiltInTypes.LOGIC, entityDeps);
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
        addFullWidthComponent(layerPanel, entityLayerCondition, gbc);
        addRowWithHelp(layerPanel, "entity_extension/entity_layer_render_type",
                "elementgui.entity_extension.entity_layer_render_type", entityLayerRenderType, gbc);
        addRowWithHelp(layerPanel, "entity_extension/entity_layer_glow",
                "elementgui.entity_extension.entity_layer_glow", entityLayerGlow, gbc);
        addRowWithHelp(layerPanel, "entity_extension/entity_layer_hurt_overlay",
                "elementgui.entity_extension.entity_layer_hurt_overlay", entityLayerHurtOverlay, gbc);
        addRowWithHelp(layerPanel, "entity_extension/entity_layer_alpha",
                "elementgui.entity_extension.entity_layer_alpha", entityLayerAlpha, gbc);
        addPage(L10N.t("elementgui.entityextension.layer"), PanelUtils.totalCenterInPanel(layerPanel), false);

        // === Global Fog ===
        JPanel fogPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(fogPanel, "entity_extension/global_fog_enabled",
                "elementgui.entity_extension.global_fog_enabled", globalFogEnabled, gbc);
        addFullWidthComponent(fogPanel, fogCondition, gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_global_mode",
                "elementgui.entity_extension.global_fog_global_mode", globalFogGlobalMode, gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_radius",
                "elementgui.entity_extension.global_fog_radius", globalFogRadius, gbc);

        addRowWithHelp(fogPanel, "entity_extension/global_fog_color",
                "elementgui.entity_extension.global_fog_color", globalFogColor, gbc);

        addSectionLabel(fogPanel, "elementgui.entity_extension.global_fog_distance_section", gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_terrain_start",
                "elementgui.entity_extension.global_fog_terrain_start", globalFogTerrainStart, gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_terrain_end",
                "elementgui.entity_extension.global_fog_terrain_end", globalFogTerrainEnd, gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_sky_start",
                "elementgui.entity_extension.global_fog_sky_start", globalFogSkyStart, gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_sky_end",
                "elementgui.entity_extension.global_fog_sky_end", globalFogSkyEnd, gbc);
        addRowWithHelp(fogPanel, "entity_extension/global_fog_shape",
                "elementgui.entity_extension.global_fog_shape", globalFogShape, gbc);
        addPage(L10N.t("elementgui.entityextension.fog"), PanelUtils.totalCenterInPanel(fogPanel), false);

        // === Global Skybox ===
        JPanel skyboxPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(skyboxPanel, "entity_extension/global_skybox_enabled",
                "elementgui.entity_extension.global_skybox_enabled", globalSkyboxEnabled, gbc);
        addFullWidthComponent(skyboxPanel, skyboxCondition, gbc);
        addRowWithHelp(skyboxPanel, "entity_extension/global_skybox_texture",
                "elementgui.entity_extension.global_skybox_texture", buildSkyboxTextureRow(), gbc);
        addRowWithHelp(skyboxPanel, "entity_extension/global_skybox_shader_render_type",
                "elementgui.entity_extension.global_skybox_shader_render_type", buildSkyboxShaderRow(), gbc);
        addRowWithHelp(skyboxPanel, "entity_extension/global_skybox_display",
                "elementgui.entity_extension.global_skybox_display", buildSkyboxDisplayRow(), gbc);
        addPage(L10N.t("elementgui.entityextension.skybox"), PanelUtils.totalCenterInPanel(skyboxPanel), false);

        // === Combat Music ===
        JPanel musicPanel = new JPanel(new GridBagLayout());
        gbc = defaultConstraints();
        addRowWithHelp(musicPanel, "entity_extension/combat_music_enabled",
                "elementgui.entity_extension.combat_music_enabled", combatMusicEnabled, gbc);
        addFullWidthComponent(musicPanel, musicCondition, gbc);
        addRowWithHelp(musicPanel, "entity_extension/combat_music_sound",
                "elementgui.entity_extension.combat_music_sound", combatMusicSound, gbc);
        addRowWithHelp(musicPanel, "entity_extension/combat_music_source",
                "elementgui.entity_extension.combat_music_source", combatMusicSoundSource, gbc);
        addRowWithHelp(musicPanel, "entity_extension/combat_music_volume",
                "elementgui.entity_extension.combat_music_volume", combatMusicVolume, gbc);
        addRowWithHelp(musicPanel, "entity_extension/combat_music_pitch",
                "elementgui.entity_extension.combat_music_pitch", combatMusicPitch, gbc);
        addRowWithHelp(musicPanel, "entity_extension/combat_music_loop",
                "elementgui.entity_extension.combat_music_loop", combatMusicLoop, gbc);
        addRowWithHelp(musicPanel, "entity_extension/combat_music_mute_others",
                "elementgui.entity_extension.combat_music_mute_others", combatMusicStrictLock, gbc);
        addPage(L10N.t("elementgui.entityextension.music"), PanelUtils.totalCenterInPanel(musicPanel), false);

        // Setup all enable/disable toggles
        setupBossBarToggle();
        setupEnableToggle(entityLayerEnabled, entityLayerCondition, entityLayerRenderType, entityLayerGlow, entityLayerHurtOverlay, entityLayerAlpha);
        setupFogToggle();
        setupSkyboxToggle();
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

    private JPanel buildSkyboxTextureRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(globalSkyboxEnableTexture);
        globalSkyboxTexture.setPreferredSize(new Dimension(300, 28));
        row.add(globalSkyboxTexture);
        return row;
    }

    private static JPanel buildTextureRow(JCheckBox enable, TextureHolder holder) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(enable);
        holder.setPreferredSize(new Dimension(300, 28));
        row.add(holder);
        return row;
    }

    private JPanel buildSkyboxShaderRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(globalSkyboxEnableShader);
        row.add(globalSkyboxShaderRenderType);
        return row;
    }

    private JPanel buildSkyboxDisplayRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.add(L10N.label("elementgui.entity_extension.global_skybox_alpha"));
        row.add(globalSkyboxAlpha);
        row.add(L10N.label("elementgui.entity_extension.global_skybox_size"));
        row.add(globalSkyboxSize);
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

    private void setupFogToggle() {
        Runnable update = () -> {
            boolean fg = globalFogEnabled.isSelected();
            fogCondition.setEnabled(fg);
            globalFogGlobalMode.setEnabled(fg);
            globalFogRadius.setEnabled(fg && !globalFogGlobalMode.isSelected());
            globalFogColor.setEnabled(fg);
            globalFogTerrainStart.setEnabled(fg);
            globalFogTerrainEnd.setEnabled(fg);
            globalFogSkyStart.setEnabled(fg);
            globalFogSkyEnd.setEnabled(fg);
            globalFogShape.setEnabled(fg);
        };
        update.run();
        globalFogEnabled.addItemListener(e -> update.run());
        globalFogGlobalMode.addItemListener(e -> update.run());
    }

    private void setupSkyboxToggle() {
        Runnable update = () -> {
            boolean sb = globalSkyboxEnabled.isSelected();
            skyboxCondition.setEnabled(sb);
            globalSkyboxEnableTexture.setEnabled(sb);
            globalSkyboxEnableShader.setEnabled(sb);
            globalSkyboxAlpha.setEnabled(sb);
            globalSkyboxSize.setEnabled(sb);
            globalSkyboxTexture.setEnabled(sb && globalSkyboxEnableTexture.isSelected());
            globalSkyboxShaderRenderType.setEnabled(sb && globalSkyboxEnableShader.isSelected());
        };
        update.run();
        globalSkyboxEnabled.addItemListener(e -> update.run());
        globalSkyboxEnableTexture.addItemListener(e -> update.run());
        globalSkyboxEnableShader.addItemListener(e -> update.run());
    }

    private void setupMusicToggle() {
        Runnable update = () -> {
            boolean mc = combatMusicEnabled.isSelected();
            musicCondition.setEnabled(mc);
            combatMusicSound.setEnabled(mc);
            combatMusicSoundSource.setEnabled(mc);
            combatMusicVolume.setEnabled(mc);
            combatMusicPitch.setEnabled(mc);
            combatMusicLoop.setEnabled(mc);
            combatMusicStrictLock.setEnabled(mc);
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
        priority.setValue(element.priority);
        enableForceLoading.setSelected(element.enableForceLoading);

        if (element.bossBarCondition != null)
            bossBarCondition.setSelectedProcedure(element.bossBarCondition);
        if (element.entityLayerCondition != null)
            entityLayerCondition.setSelectedProcedure(element.entityLayerCondition);
        if (element.fogCondition != null)
            fogCondition.setSelectedProcedure(element.fogCondition);
        if (element.skyboxCondition != null)
            skyboxCondition.setSelectedProcedure(element.skyboxCondition);
        if (element.musicCondition != null)
            musicCondition.setSelectedProcedure(element.musicCondition);

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

        customHealthEnabled.setSelected(element.customHealthEnabled);
        if (element.customHealthValue != null)
            customHealthValue.setSelectedProcedure(element.customHealthValue);
        customMaxHealthEnabled.setSelected(element.customMaxHealthEnabled);
        if (element.customMaxHealthValue != null)
            customMaxHealthValue.setSelectedProcedure(element.customMaxHealthValue);

        entityLayerEnabled.setSelected(element.entityLayerEnabled);
        setCombo(entityLayerRenderType, element.entityLayerRenderType);
        entityLayerGlow.setSelected(element.entityLayerGlow);
        entityLayerHurtOverlay.setSelected(element.entityLayerHurtOverlay);
        entityLayerAlpha.setValue(element.entityLayerAlpha);

        globalFogEnabled.setSelected(element.globalFogEnabled);
        globalFogGlobalMode.setSelected(element.globalFogGlobalMode);
        globalFogRadius.setValue(element.globalFogRadius);
        globalFogColor.setColor(hexToColor(element.globalFogColor));
        globalFogTerrainStart.setValue(element.globalFogTerrainStart);
        globalFogTerrainEnd.setValue(element.globalFogTerrainEnd);
        globalFogSkyStart.setValue(element.globalFogSkyStart);
        globalFogSkyEnd.setValue(element.globalFogSkyEnd);
        setCombo(globalFogShape, element.globalFogShape);

        globalSkyboxEnabled.setSelected(element.globalSkyboxEnabled);
        globalSkyboxEnableTexture.setSelected(element.globalSkyboxEnableTexture);
        globalSkyboxTexture.setTextureFromTextureName(nonNull(element.globalSkyboxTexture));
        globalSkyboxEnableShader.setSelected(element.globalSkyboxEnableShader);
        setCombo(globalSkyboxShaderRenderType, element.globalSkyboxShaderRenderType);
        globalSkyboxAlpha.setValue(element.globalSkyboxAlpha);
        globalSkyboxSize.setValue(element.globalSkyboxSize);

        combatMusicEnabled.setSelected(element.combatMusicEnabled);
        combatMusicSound.setSound(new Sound(modElement.getWorkspace(), nonNull(element.combatMusicSoundEventId)));
        setCombo(combatMusicSoundSource, element.combatMusicSoundSource);
        combatMusicVolume.setValue(element.combatMusicVolume);
        combatMusicPitch.setValue(element.combatMusicPitch);
        combatMusicLoop.setSelected(element.combatMusicLoop);
        combatMusicStrictLock.setSelected(element.combatMusicStrictLock);
    }

    @Override
    public EntityExtensionElement getElementFromGUI() {
        EntityExtensionElement element = new EntityExtensionElement(modElement);

        element.entityType = new EntityEntry(modElement.getWorkspace(),
                entityType.getSelectedItem());
        element.priority = (int) priority.getValue();
        element.enableForceLoading = enableForceLoading.isSelected();

        element.bossBarCondition = bossBarCondition.getSelectedProcedure();
        element.entityLayerCondition = entityLayerCondition.getSelectedProcedure();
        element.fogCondition = fogCondition.getSelectedProcedure();
        element.skyboxCondition = skyboxCondition.getSelectedProcedure();
        element.musicCondition = musicCondition.getSelectedProcedure();

        element.bossBarEnabled = bossBarEnabled.isSelected();
        element.bossBarFrameEnableTexture = bossBarFrameEnableTexture.isSelected();
        element.bossBarFrameTexture = bossBarFrameTexture.getID();
        element.bossBarFillEnableTexture = bossBarFillEnableTexture.isSelected();
        element.bossBarFillTexture = bossBarFillTexture.getID();
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

        element.customHealthEnabled = customHealthEnabled.isSelected();
        element.customHealthValue = customHealthValue.getSelectedProcedure();
        element.customMaxHealthEnabled = customMaxHealthEnabled.isSelected();
        element.customMaxHealthValue = customMaxHealthValue.getSelectedProcedure();

        element.entityLayerEnabled = entityLayerEnabled.isSelected();
        element.entityLayerRenderType = getCombo(entityLayerRenderType);
        element.entityLayerGlow = entityLayerGlow.isSelected();
        element.entityLayerHurtOverlay = entityLayerHurtOverlay.isSelected();
        element.entityLayerAlpha = (double) entityLayerAlpha.getValue();

        element.globalFogEnabled = globalFogEnabled.isSelected();
        element.globalFogGlobalMode = globalFogGlobalMode.isSelected();
        element.globalFogRadius = (double) globalFogRadius.getValue();
        element.globalFogColor = colorToHex(globalFogColor.getColor());
        element.globalFogTerrainStart = (double) globalFogTerrainStart.getValue();
        element.globalFogTerrainEnd = (double) globalFogTerrainEnd.getValue();
        element.globalFogSkyStart = (double) globalFogSkyStart.getValue();
        element.globalFogSkyEnd = (double) globalFogSkyEnd.getValue();
        element.globalFogShape = (String) globalFogShape.getSelectedItem();

        element.globalSkyboxEnabled = globalSkyboxEnabled.isSelected();
        element.globalSkyboxEnableTexture = globalSkyboxEnableTexture.isSelected();
        element.globalSkyboxTexture = globalSkyboxTexture.getID();
        element.globalSkyboxEnableShader = globalSkyboxEnableShader.isSelected();
        element.globalSkyboxShaderRenderType = getCombo(globalSkyboxShaderRenderType);
        element.globalSkyboxAlpha = (double) globalSkyboxAlpha.getValue();
        element.globalSkyboxSize = (double) globalSkyboxSize.getValue();

        element.combatMusicEnabled = combatMusicEnabled.isSelected();
        element.combatMusicSoundEventId = combatMusicSound.getSound().getUnmappedValue();
        element.combatMusicSoundSource = (String) combatMusicSoundSource.getSelectedItem();
        element.combatMusicVolume = (double) combatMusicVolume.getValue();
        element.combatMusicPitch = (double) combatMusicPitch.getValue();
        element.combatMusicLoop = combatMusicLoop.isSelected();
        element.combatMusicStrictLock = combatMusicStrictLock.isSelected();

        return element;
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        bossBarCondition.refreshListKeepSelected();
        entityLayerCondition.refreshListKeepSelected();
        fogCondition.refreshListKeepSelected();
        skyboxCondition.refreshListKeepSelected();
        musicCondition.refreshListKeepSelected();
        customHealthValue.refreshListKeepSelected();
        customMaxHealthValue.refreshListKeepSelected();
    }

    private static String[] getRenderTypeOptions() {
        return new String[]{
                "(None)",
                "TheLastEnd", "DreamSakura", "Forest", "Ocean", "Storm",
                "Volcano", "Arcane", "Aurora", "Hacker", "Starlight", "Cosmos", "BlackHole"
        };
    }

    private static String[] getSkyboxShaderOptions() {
        return new String[]{
                "TheLastEnd", "DreamSakura", "Forest", "Ocean", "Storm",
                "Volcano", "Arcane", "Aurora", "Hacker", "Starlight", "Cosmos", "BlackHole"
        };
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

    private static Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty()) return new Color(0x808080);
        try {
            return new Color(Integer.parseInt(hex, 16));
        } catch (NumberFormatException e) {
            return new Color(0x808080);
        }
    }

    private static String colorToHex(Color color) {
        if (color == null) return "808080";
        return String.format("%06X", 0xFFFFFF & color.getRGB());
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
