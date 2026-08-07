if (${input$player} instanceof net.minecraft.server.level.ServerPlayer _bsViewer${cbi}
    && ${input$target} instanceof net.minecraft.world.entity.LivingEntity _bsTarget${cbi}) {
try {
    net.eca.api.EcaAPI.playBossShowIfNew(_bsViewer${cbi}, _bsTarget${cbi},
        new net.minecraft.resources.ResourceLocation(${input$cutsceneId}));
} catch (Exception _e) {
}
}
