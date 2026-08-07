if (${input$player} instanceof net.minecraft.server.level.ServerPlayer _bsViewer${cbi}
    && ${input$target} instanceof net.minecraft.world.entity.LivingEntity _bsTarget${cbi}) {
net.eca.api.EcaAPI.launchBossShowEvent(${input$eventName}, _bsViewer${cbi}, _bsTarget${cbi});
}
