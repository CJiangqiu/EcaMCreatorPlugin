package net.eca.mcreator.ui;

import net.eca.mcreator.element.BossShowElement;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BossShowGUI extends ModElementGUI<BossShowElement> {

    private DataListComboBox targetEntityType;
    private final JComboBox<String> bossShowId = new SearchableComboBox<>();
    private JKeyframeMappingsList keyframeMappings;

    public BossShowGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        initGUI();
        finalizeGUI();
    }

    @Override
    protected void initGUI() {
        Dependency[] keyframeDeps = Dependency.fromString(
                "x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity");

        targetEntityType = new DataListComboBox(mcreator,
                ElementUtil.loadAllSpawnableEntities(mcreator.getWorkspace()));

        reloadBossShowList();

        keyframeMappings = new JKeyframeMappingsList(mcreator, this.withEntry("bossshow/keyframe_mappings"), keyframeDeps);

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultConstraints();
        addRowWithHelp(topPanel, "bossshow/target_entity_type",
                "elementgui.bossshow.target_entity_type", targetEntityType, gbc);
        addRowWithHelp(topPanel, "bossshow/boss_show_id",
                "elementgui.bossshow.boss_show_id", bossShowId, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(topPanel, BorderLayout.NORTH);
        content.add(keyframeMappings, BorderLayout.CENTER);

        addPage(L10N.t("elementgui.bossshow.general"), PanelUtils.totalCenterInPanel(content), false);
    }

    private void reloadBossShowList() {
        Set<String> found = new LinkedHashSet<>();
        File workspaceFolder = mcreator.getWorkspace().getFolderManager().getWorkspaceFolder();
        String modid = mcreator.getWorkspace().getWorkspaceSettings().getModID();
        File dataRoot = new File(workspaceFolder, "src/main/resources/data/" + modid);
        collectBossShows(new File(dataRoot, "eca/bossshow"), new File(dataRoot, "eca/bossshow"), found);
        collectBossShows(new File(dataRoot, "bossshow"), new File(dataRoot, "bossshow"), found);
        List<String> names = found.stream().sorted(Comparator.naturalOrder()).toList();

        String previous = (String) bossShowId.getSelectedItem();
        bossShowId.removeAllItems();
        for (String n : names) bossShowId.addItem(n);
        if (previous != null && names.contains(previous))
            bossShowId.setSelectedItem(previous);
    }

    private static void collectBossShows(File root, File directory, Set<String> output) {
        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.stream(files)
                    .sorted(Comparator.comparing(File::getName))
                    .forEach(file -> {
                        if (file.isDirectory()) {
                            collectBossShows(root, file, output);
                        } else if (file.getName().endsWith(".json")) {
                            String relative = root.toPath().relativize(file.toPath()).toString()
                                    .replace(File.separatorChar, '/');
                            output.add(relative.substring(0, relative.length() - ".json".length()));
                        }
                    });
        }
    }

    @Override
    public void reloadDataLists() {
        super.reloadDataLists();
        reloadBossShowList();
        keyframeMappings.reloadDataLists();
    }

    @Override
    protected AggregatedValidationResult validatePage(int page) {
        return new AggregatedValidationResult.PASS();
    }

    @Override
    protected void openInEditingMode(BossShowElement element) {
        targetEntityType.setSelectedItem(element.targetEntityType);
        if (element.bossShowId != null && !element.bossShowId.isEmpty()) {
            bossShowId.setSelectedItem(element.bossShowId);
        }
        if (element.keyframeMappings != null) {
            keyframeMappings.setEntries(element.keyframeMappings);
        }
    }

    @Override
    public BossShowElement getElementFromGUI() {
        BossShowElement element = new BossShowElement(modElement);
        element.targetEntityType = new EntityEntry(modElement.getWorkspace(),
                targetEntityType.getSelectedItem());
        Object selected = bossShowId.getSelectedItem();
        element.bossShowId = selected != null ? selected.toString() : "";
        element.keyframeMappings = keyframeMappings.getEntries();
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
