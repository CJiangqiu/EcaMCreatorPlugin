if (world instanceof net.minecraft.world.level.Level _level) {
	final Vec3 _center = new Vec3(${input$x}, ${input$y}, ${input$z});
	List<Entity> _entfound = net.eca.api.EcaAPI.getEntities(_level, new AABB(_center, _center).inflate(${input$range} / 2d))
		.stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
	for (Entity entityiterator : _entfound) {
		${statement$foreach}
	}
}
