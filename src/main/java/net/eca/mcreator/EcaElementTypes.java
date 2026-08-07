package net.eca.mcreator;

import net.eca.mcreator.element.BlockExtensionElement;
import net.eca.mcreator.element.BossShowElement;
import net.eca.mcreator.element.EntityExtensionElement;
import net.eca.mcreator.element.EcaItemExtensionElement;
import net.eca.mcreator.element.ShaderPresetElement;
import net.eca.mcreator.ui.BlockExtensionGUI;
import net.eca.mcreator.ui.BossShowGUI;
import net.eca.mcreator.ui.EntityExtensionGUI;
import net.eca.mcreator.ui.EcaItemExtensionGUI;
import net.eca.mcreator.ui.ShaderPresetGUI;
import net.mcreator.element.ModElementType;
import net.mcreator.element.ModElementTypeLoader;

public class EcaElementTypes {

    public static ModElementType<?> ENTITY_EXTENSION;
    public static ModElementType<?> BOSS_SHOW;
    public static ModElementType<?> ECA_ITEM_EXTENSION;
    public static ModElementType<?> SHADER_PRESET;
    public static ModElementType<?> BLOCK_EXTENSION;

    public static void load() {
        ENTITY_EXTENSION = ModElementTypeLoader.register(
                new ModElementType<>("entityextension", null, EntityExtensionGUI::new,
                        EntityExtensionElement.class));
        BOSS_SHOW = ModElementTypeLoader.register(
                new ModElementType<>("bossshow", null, BossShowGUI::new,
                        BossShowElement.class));
        ECA_ITEM_EXTENSION = ModElementTypeLoader.register(
                new ModElementType<>("ecaitemextension", null, EcaItemExtensionGUI::new,
                        EcaItemExtensionElement.class));
        SHADER_PRESET = ModElementTypeLoader.register(
                new ModElementType<>("shaderpreset", null, ShaderPresetGUI::new,
                        ShaderPresetElement.class));
        BLOCK_EXTENSION = ModElementTypeLoader.register(
                new ModElementType<>("blockextension", null, BlockExtensionGUI::new,
                        BlockExtensionElement.class));
    }
}
