if (world instanceof net.minecraft.server.level.ServerLevel _serverLevel${cbi}) {
<#switch field$color>
<#case "PINK"><#assign _r=1.0><#assign _g=0.41><#assign _b=0.71><#break>
<#case "BLUE"><#assign _r=0.0><#assign _g=0.0><#assign _b=1.0><#break>
<#case "RED"><#assign _r=1.0><#assign _g=0.0><#assign _b=0.0><#break>
<#case "GREEN"><#assign _r=0.0><#assign _g=1.0><#assign _b=0.0><#break>
<#case "YELLOW"><#assign _r=1.0><#assign _g=1.0><#assign _b=0.0><#break>
<#case "PURPLE"><#assign _r=0.5><#assign _g=0.0><#assign _b=0.5><#break>
<#case "WHITE"><#assign _r=1.0><#assign _g=1.0><#assign _b=1.0><#break>
<#default><#assign _r=0.5><#assign _g=0.5><#assign _b=0.5><#break>
</#switch>
net.eca.api.EcaAPI.setGlobalFog(_serverLevel${cbi}, new net.eca.network.EntityExtensionOverridePacket.FogData(
    ${(field$mode == "GLOBAL")?c}, (float)(${input$radius}),
    ${_r}f, ${_g}f, ${_b}f,
    0.25f, 1.0f, 0.0f, 1.0f, 1));
}
