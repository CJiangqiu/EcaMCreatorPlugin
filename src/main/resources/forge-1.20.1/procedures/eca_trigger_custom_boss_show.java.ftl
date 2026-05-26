if (${input$player} instanceof net.minecraft.server.level.ServerPlayer _bsViewer
    && ${input$target} instanceof net.minecraft.world.entity.LivingEntity _bsTarget) {
net.eca.api.EcaAPI.launchBossShowEvent(${input$eventName}, _bsViewer, _bsTarget);
}
