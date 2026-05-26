package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.workspace.elements.ModElement;

import java.util.ArrayList;
import java.util.List;

public class BossShowElement extends GeneratableElement {

    public EntityEntry targetEntityType;
    public String bossShowId;
    public List<KeyframeMapping> keyframeMappings;

    public BossShowElement(ModElement element) {
        super(element);
        this.bossShowId = "";
        this.keyframeMappings = new ArrayList<>();
    }

    public static class KeyframeMapping {
        public String eventId;
        public Procedure procedure;

        public KeyframeMapping() {
            this.eventId = "";
        }
    }
}
