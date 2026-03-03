if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel) {
	net.minecraft.server.level.ServerLevel _targetLevel = _serverLevel.getServer().getLevel(${generator.map(field$dimension, "dimensions")});
	if (_targetLevel != null) {
		for (Entity entityiterator : net.eca.api.EcaAPI.getEntities(_targetLevel, ${generator.map(field$entity, "entities", 0)}.class)) {
			${statement$foreach}
		}
	}
}
