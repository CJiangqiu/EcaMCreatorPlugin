if (${input$entity} != null) {
net.eca.api.EcaAPI.remove(${input$entity},
net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
}
