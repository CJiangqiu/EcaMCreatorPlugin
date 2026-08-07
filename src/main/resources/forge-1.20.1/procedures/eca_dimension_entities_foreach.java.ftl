if (world instanceof net.minecraft.world.level.Level) {
	for (Entity entityiterator : net.eca.api.EcaAPI.getEntities((net.minecraft.world.level.Level) world)) {
		${statement$foreach}
	}
}
