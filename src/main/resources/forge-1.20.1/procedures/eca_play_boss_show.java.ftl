if (${input$player} instanceof net.minecraft.server.level.ServerPlayer _bsViewer
    && ${input$target} instanceof net.minecraft.world.entity.LivingEntity _bsTarget) {
try {
    net.eca.api.EcaAPI.playBossShow(_bsViewer, _bsTarget,
        new net.minecraft.resources.ResourceLocation(${input$cutsceneId}));
} catch (Exception _e) {
}
}
