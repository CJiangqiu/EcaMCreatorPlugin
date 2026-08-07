if (world instanceof net.minecraft.world.level.Level) {
	final Vec3 _ecaCenter = new Vec3(${input$x}, ${input$y}, ${input$z});
	List<Entity> _ecaFound = net.eca.api.EcaAPI.getEntities((net.minecraft.world.level.Level) world, new AABB(_ecaCenter, _ecaCenter).inflate(${input$range} / 2d))
		.stream().sorted(Comparator.comparingDouble(_ecaCnd -> _ecaCnd.distanceToSqr(_ecaCenter))).toList();
	for (Entity entityiterator : _ecaFound) {
		${statement$foreach}
	}
}
