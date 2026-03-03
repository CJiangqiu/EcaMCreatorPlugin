if (world instanceof net.minecraft.world.level.Level _level) {
	for (Entity entityiterator : net.eca.api.EcaAPI.getEntities(_level)) {
		${statement$foreach}
	}
}
