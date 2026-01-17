// WARNING: This is a DANGEROUS operation! Requires "Enable Radical Logic" in ECA config.
// This will perform return transformation on all boolean and void methods of the target entity's mod.
// Use with extreme caution!
if (${input$entity} != null) {
net.eca.api.EcaAPI.enableAllReturn(${input$entity});
}
