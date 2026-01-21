// WARNING: This requires "Enable Radical Logic" in ECA config.
// Removes entity using LWJGL API for deep memory cleanup.
if (${input$entity} != null) {
net.eca.api.EcaAPI.memoryRemoveEntity(${input$entity});
}
