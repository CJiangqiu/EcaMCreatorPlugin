if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel${cbi} && ${input$entity} instanceof net.minecraft.world.entity.LivingEntity _livingEntity${cbi}) {
net.eca.api.EcaAPI.setForceLoading(_livingEntity${cbi}, _serverLevel${cbi}, (boolean) (${input$force_load}));
}
