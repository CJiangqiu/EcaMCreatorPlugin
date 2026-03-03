if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel) {
	for (Entity entityiterator : net.eca.api.EcaAPI.getEntities(_serverLevel.getServer())) {
		${statement$foreach}
	}
}
