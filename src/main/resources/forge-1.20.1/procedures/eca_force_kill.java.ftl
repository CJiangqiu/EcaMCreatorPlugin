if (${input$entity} instanceof net.minecraft.world.entity.LivingEntity _livingEntity) {
net.minecraft.world.damagesource.DamageSource src =
_livingEntity.level().damageSources().generic();
net.eca.api.EcaAPI.killEntity(_livingEntity, src);
}
