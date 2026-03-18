if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel) {
try {
    java.util.UUID _uuid = java.util.UUID.fromString(${input$uuid});
    net.eca.api.EcaAPI.reviveEntity(_serverLevel, _uuid);
} catch (IllegalArgumentException _e) {
}
}
