package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.workspace.elements.ModElement;

import java.util.ArrayList;
import java.util.List;

public class BossShowEventElement extends GeneratableElement {

    public EntityEntry targetEntityType;
    public String bossShowId;
    public List<MarkerMapping> markerMappings;

    public BossShowEventElement(ModElement element) {
        super(element);
        this.bossShowId = "";
        this.markerMappings = new ArrayList<>();
    }

    public static class MarkerMapping {
        public String eventId;
        public Procedure procedure;

        public MarkerMapping() {
            this.eventId = "";
        }
    }
}
