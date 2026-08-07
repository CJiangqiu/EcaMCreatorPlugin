if (${input$entity} instanceof net.minecraft.world.entity.LivingEntity _livingEntity${cbi}) {
net.minecraft.world.damagesource.DamageSource src =
_livingEntity${cbi}.level().damageSources().generic();
net.eca.api.EcaAPI.kill(_livingEntity${cbi}, src);
}
