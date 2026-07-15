package net.eca.mcreator;

import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.ApplicationLoadedEvent;
import net.mcreator.ui.init.L10N;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class EcaPlugin extends JavaPlugin {

    private static final Logger LOG = LogManager.getLogger("ECA Plugin");

    public EcaPlugin(Plugin plugin) {
        super(plugin);

        addListener(ApplicationLoadedEvent.class, event -> {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
            while (!translationsAreReady()) {
                if (System.nanoTime() >= deadline) {
                    LOG.error("Timed out waiting for translations; ECA element types were not registered");
                    return;
                }
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1));
            }

            EcaElementTypes.load();
            LOG.info("ECA element types registered before generator loading");
        });

        LOG.info("ECA Java plugin loaded");
    }

    private static boolean translationsAreReady() {
        try {
            L10N.t("modelement.entityextension");
            return true;
        } catch (NullPointerException ignored) {
            return false;
        }
    }
}
