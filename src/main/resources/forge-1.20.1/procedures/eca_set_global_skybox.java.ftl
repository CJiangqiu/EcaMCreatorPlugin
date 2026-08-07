if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel${cbi}) {
<#switch field$preset>
<#case "TheLastEnd"><#assign _presetId="the_last_end"><#break>
<#case "DreamSakura"><#assign _presetId="dream_sakura"><#break>
<#case "Forest"><#assign _presetId="forest"><#break>
<#case "Ocean"><#assign _presetId="ocean"><#break>
<#case "Storm"><#assign _presetId="storm"><#break>
<#case "Volcano"><#assign _presetId="volcano"><#break>
<#case "Arcane"><#assign _presetId="arcane"><#break>
<#case "Aurora"><#assign _presetId="aurora"><#break>
<#case "Hacker"><#assign _presetId="hacker"><#break>
<#case "Starlight"><#assign _presetId="starlight"><#break>
<#case "Cosmos"><#assign _presetId="cosmos"><#break>
<#case "BlackHole"><#assign _presetId="black_hole"><#break>
<#default><#assign _presetId="the_last_end"><#break>
</#switch>
net.eca.api.EcaAPI.setGlobalSkybox(_serverLevel${cbi}, new net.eca.network.EntityExtensionOverridePacket.SkyboxData(
    false, null, true, new net.minecraft.resources.ResourceLocation("eca", "${_presetId}"),
    (float)(${input$alpha}), 100.0f, 16.0f, 1.0f, 1.0f, 1.0f));
}
