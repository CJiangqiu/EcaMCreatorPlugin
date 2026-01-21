if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel && ${input$entity} != null) {
net.eca.api.EcaAPI.clearSpawnBan(_serverLevel, ${input$entity}.getType());
}
