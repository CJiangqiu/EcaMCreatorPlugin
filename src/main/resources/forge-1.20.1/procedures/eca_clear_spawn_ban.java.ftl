if (${input$entity} != null && ${input$entity}.level() instanceof net.minecraft.server.level.ServerLevel _serverLevel) {
net.eca.api.EcaAPI.clearSpawnBan(_serverLevel, ${input$entity}.getType());
}
