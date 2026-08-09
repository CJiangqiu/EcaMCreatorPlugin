if (world instanceof net.minecraft.world.level.Level) {
net.eca.api.EcaAPI.alertFactionMembers("${generator.getRegistryNameForModElement(generator.getElementPlainName(field$faction))}", ${input$attacker}, ${input$victim}, (net.minecraft.world.level.Level) world);
}