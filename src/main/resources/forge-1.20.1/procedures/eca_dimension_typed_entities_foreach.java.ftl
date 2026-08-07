if (world instanceof net.minecraft.server.level.ServerLevel) {
	net.minecraft.server.level.ServerLevel _ecaTargetLevel = ((net.minecraft.server.level.ServerLevel) world).getServer().getLevel(${generator.map(field$dimension, "dimensions")});
	if (_ecaTargetLevel != null) {
		for (Entity entityiterator : net.eca.api.EcaAPI.getEntities(_ecaTargetLevel, ${generator.map(field$entity, "entities", 0)}.class)) {
			${statement$foreach}
		}
	}
}
