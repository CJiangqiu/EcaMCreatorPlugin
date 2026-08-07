if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel${cbi}) {
try {
    java.util.UUID _uuid = java.util.UUID.fromString(${input$uuid});
    net.eca.api.EcaAPI.revive(_serverLevel${cbi}, _uuid);
} catch (IllegalArgumentException _e) {
}
}
