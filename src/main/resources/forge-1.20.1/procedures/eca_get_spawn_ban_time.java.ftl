(${input$entity} != null && ${input$entity}.level() instanceof net.minecraft.server.level.ServerLevel _serverLevel ? net.eca.api.EcaAPI.getSpawnBanTime(_serverLevel, ${input$entity}.getType()) : 0)
