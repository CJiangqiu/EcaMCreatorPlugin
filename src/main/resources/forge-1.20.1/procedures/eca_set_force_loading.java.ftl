if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel && ${input$entity} instanceof net.minecraft.world.entity.LivingEntity _livingEntity) {
net.eca.api.EcaAPI.setForceLoading(_livingEntity, _serverLevel, (boolean) (${input$force_load}));
}
