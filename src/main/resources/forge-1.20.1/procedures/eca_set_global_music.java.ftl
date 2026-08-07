<#assign sound = generator.map(field$sound, "sounds")?replace("CUSTOM:", "${modid}:")>
<#if sound?has_content>
if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel${cbi}) {
net.eca.api.EcaAPI.setGlobalMusic(_serverLevel${cbi}, new net.eca.network.EntityExtensionOverridePacket.MusicData(
    new net.minecraft.resources.ResourceLocation("${sound}"),
    net.minecraft.sounds.SoundSource.${field$soundSource}.ordinal(),
    (float)(${input$volume}), (float)(${input$pitch}),
    ${(field$loop == "TRUE")?c}, false));
}
</#if>
