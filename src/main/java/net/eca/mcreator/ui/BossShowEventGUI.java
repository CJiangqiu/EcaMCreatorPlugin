package net.eca.mcreator.ui;

import net.eca.mcreator.element.BossShowEventElement;
import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.DataListComboBox;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BossShowEventGUI extends ModElementGUI<BossShowEventElement> {

    private DataListComboBox targetEntityType;
    private final JComboBox<String> bossShowId = new SearchableComboBox<>();
    private JMarkerMappingsList markerMappings;

    public BossShowEventGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        Dependency[] markerDeps = Dependency.fromString(
                "x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity");

        targetEntityType = new DataListComboBox(mcreator,
                ElementUtil.loadAllSpawnableEntities(mcreator.getWorkspace()));

        reloadBossShowList();

        markerMappings = new JMarkerMappingsList(mcreator, this.withEntry("bossshowevent/marker_mappings"), markerDeps);

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();
        addRowWithHelp(topPanel, "bossshowevent/target_entity_type",
                "elementgui.bossshowevent.target_entity_type", targetEntityType, gbc);
        addRowWithHelp(topPanel, "bossshowevent/boss_show_id",
                "elementgui.bossshowevent.boss_show_id", bossShowId, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(topPanel, BorderLayout.NORTH);
        content.add(markerMappings, BorderLayout.CENTER);

        addPage(L10N.t("elementgui.bossshowevent.general"), PanelUtils.totalCenterInPanel(content), false);
    }

    private void reloadBossShowList() {
        List<String> names = new ArrayList<>();
        File workspaceFolder = mcreator.getWorkspace().getFolderManager().getWorkspaceFolder();
        String modid = mcreator.getWorkspace().getWorkspaceSettings().getModID();
        File bossShowDir = new File(workspaceFolder, "src/main/resources/data/" + modid + "/bossshow");
        File[] files = bossShowDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            Arrays.stream(files)
                    .map(File::getName)
                    .map(n -> n.substring(0, n.length() - ".json".length()))
                    .sorted(Comparator.naturalOrder())
                    .forEach(names::add);
        }

        String previous = (String) bossShowId.getSelectedItem();
        bossShowId.removeAllItems();
        for (String n : names) bossShowId.addItem(n);
        if (previous != null && names.contains(previous))
            bossShowId.setSelectedItem(previous);
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        reloadBossShowList();
        markerMappings.reloadDataLists();
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(BossShowEventElement element) {
        targetEntityType.setSelectedItem(element.targetEntityType);
        if (element.bossShowId != null && !element.bossShowId.isEmpty()) {
            bossShowId.setSelectedItem(element.bossShowId);
        }
        if (element.markerMappings != null) {
            markerMappings.setEntries(element.markerMappings);
        }
    }

    @Override
    public BossShowEventElement getElementFromGUI() {
        BossShowEventElement element = new BossShowEventElement(modElement);
        element.targetEntityType = new EntityEntry(modElement.getWorkspace(),
                targetEntityType.getSelectedItem());
        Object selected = bossShowId.getSelectedItem();
        element.bossShowId = selected != null ? selected.toString() : "";
        element.markerMappings = markerMappings.getEntries();
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
