if (${input$entity} != null) {
net.eca.api.EcaAPI.removeEntity(${input$entity},
net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
}
