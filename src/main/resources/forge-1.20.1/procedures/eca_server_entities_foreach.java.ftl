if (world instanceof net.minecraft.server.level.ServerLevel) {
	for (Entity entityiterator : net.eca.api.EcaAPI.getEntities(((net.minecraft.server.level.ServerLevel) world).getServer())) {
		${statement$foreach}
	}
}
