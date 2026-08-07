<#-- @formatter:off -->
<#include "../mcitems.ftl">
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#function isBuiltin name>
    <#return name == "TheLastEnd" || name == "DreamSakura" || name == "Forest" || name == "Ocean" || name == "Storm" || name == "Volcano" || name == "Arcane" || name == "Aurora" || name == "Hacker" || name == "Starlight" || name == "Cosmos" || name == "BlackHole">
</#function>
<#macro blockDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "x">pos.getX()<#break><#case "y">pos.getY()<#break><#case "z">pos.getZ()<#break><#case "world">level<#break><#case "blockstate">state<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
package ${package}.blockextension;

import net.eca.api.RegisterBlockExtension;
import net.eca.util.block_extension.BlockExtension;
import net.eca.util.block_extension.BlockExtensionManager;

@RegisterBlockExtension
public class ${name}BlockExtension extends BlockExtension {

    static {
        BlockExtensionManager.register(new ${name}BlockExtension());
    }

    public ${name}BlockExtension() {
        super(${mappedBlockToBlock(data.block)});
    }

    @Override
    public boolean enabled() {
        return ${data.renderLayerEnabled?c};
    }
<#if data.renderLayerEnabled && data.preset?has_content>
<#if isBuiltin(data.preset)>

    <#-- 内置着色器把装饰贴图放在 Sampler0，需用各自的 BLOCK 常量绑定，走预设会采到方块图集 -->
    @Override
    public net.minecraft.client.renderer.RenderType getBlockRenderType() {
        return net.eca.client.render.${data.preset}RenderTypes.BLOCK;
    }
<#else>

    @Override
    public net.minecraft.resources.ResourceLocation getShaderPresetId() {
        return new net.minecraft.resources.ResourceLocation("${modid}", "${data.preset}");
    }
</#if>
</#if>

    @Override
    public float getAlpha() {
        return ${data.alpha?c}f;
    }

    @Override
    public boolean isGlow() {
        return ${data.glow?c};
    }
<#if data.renderLayerEnabled && data.enableMask && data.maskTexture?has_content>

    @Override
    public java.util.List<net.eca.client.render.ShaderMaskPass> getBlockShaderPasses() {
        net.minecraft.client.renderer.RenderType _renderType = getBlockRenderType();
        if (_renderType == null) {
            return java.util.List.of();
        }
        return java.util.List.of(net.eca.client.render.ShaderMaskPass.masked(_renderType,
            new net.minecraft.resources.ResourceLocation("${modid}", "textures/block/${data.maskTexture}.png"),
            0x${data.maskColor}, ${data.maskTolerance?c}f, ${data.alpha?c}f));
    }
</#if>
<#if data.shouldRenderCondition?? && hasProc(data.shouldRenderCondition)>

    @Override
    public boolean shouldRender(net.minecraft.world.level.block.state.BlockState state,
                                net.minecraft.world.level.BlockAndTintGetter level,
                                net.minecraft.core.BlockPos pos) {
        return ${package}.procedures.${data.shouldRenderCondition.getName()}Procedure.execute(<@blockDepsCall data.shouldRenderCondition/>);
    }
</#if>
}
