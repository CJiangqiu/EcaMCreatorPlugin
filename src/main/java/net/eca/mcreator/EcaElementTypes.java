package net.eca.mcreator;

import net.eca.mcreator.element.BossShowEventElement;
import net.eca.mcreator.element.EntityExtensionElement;
import net.eca.mcreator.ui.BossShowEventGUI;
import net.eca.mcreator.ui.EntityExtensionGUI;
import net.mcreator.element.ModElementType;
import net.mcreator.element.ModElementTypeLoader;

public class EcaElementTypes {

    public static ModElementType<?> ENTITY_EXTENSION;
    public static ModElementType<?> BOSS_SHOW_EVENT;

    public static void load() {
        ENTITY_EXTENSION = ModElementTypeLoader.register(
                new ModElementType<>("entityextension", null, EntityExtensionGUI::new,
                        EntityExtensionElement.class));
        BOSS_SHOW_EVENT = ModElementTypeLoader.register(
                new ModElementType<>("bossshowevent", null, BossShowEventGUI::new,
                        BossShowEventElement.class));
    }
}
