package net.eca.mcreator;

import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.PreGeneratorsLoadingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EcaPlugin extends JavaPlugin {

    private static final Logger LOG = LogManager.getLogger("ECA Plugin");

    public EcaPlugin(Plugin plugin) {
        super(plugin);

        addListener(PreGeneratorsLoadingEvent.class, event -> {
            EcaElementTypes.load();
            LOG.info("ECA entity extension element type registered");
        });

        LOG.info("ECA Java plugin loaded");
    }
}
