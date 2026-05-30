<#-- @formatter:off -->
<#include "../mcitems.ftl">
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#function hasValue obj>
    <#return obj?? && (hasProc(obj) || obj.getFixedValue()?has_content)>
</#function>
<#macro itemDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "itemstack">stack<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
<#-- 文本来源：选了流程则调用流程返回值，否则用转义后的固定文本。名字与 tooltip 行共用 -->
<#macro strSource sp><#if hasProc(sp)>${package}.procedures.${sp.getName()}Procedure.execute(<@itemDepsCall sp/>)<#else>"${JavaConventions.escapeStringForJava(sp.getFixedValue())}"</#if></#macro>
<#-- 拼接 ItemUtil.of(...).addEffect....build() 链，名字与 tooltip 行共用 -->
<#macro buildEcaText textExpr colorEffect period color1 color2 shimmer shimmerIntensity glitch glitchIntensity bold italic underline strikethrough>net.eca.util.ItemUtil.of(${textExpr})<#if colorEffect == "GRADIENT">.addEffect.GRADIENT(${period?c}, 0x${color1}, 0x${color2})<#elseif colorEffect == "RAINBOW">.addEffect.RAINBOW(${period?c})<#elseif colorEffect == "SOLID">.addEffect.SOLID(0x${color1})</#if><#if shimmer>.addEffect.SHIMMER(${shimmerIntensity?c}f)</#if><#if glitch>.addEffect.GLITCH(${glitchIntensity?c}f)</#if><#if bold>.addEffect.BOLD()</#if><#if italic>.addEffect.ITALIC()</#if><#if underline>.addEffect.UNDERLINE()</#if><#if strikethrough>.addEffect.STRIKETHROUGH()</#if>.build()</#macro>
package ${package}.ecaitemextension;

import net.eca.api.RegisterItemExtension;
import net.eca.util.item_extension.ItemExtension;
import net.eca.util.item_extension.ItemExtensionManager;
import net.minecraft.client.renderer.RenderType;

@RegisterItemExtension
public class ${name}ItemExtension extends ItemExtension {

    static {
        ItemExtensionManager.register(new ${name}ItemExtension());
    }

    public ${name}ItemExtension() {
        super(${mappedMCItemToItem(data.item)});
    }

    @Override
    protected String getModId() {
        return "${modid}";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public RenderType getRenderType() {
<#if data.renderLayerEnabled>
        return net.eca.client.render.${data.preset}RenderTypes.ITEM;
<#else>
        return null;
</#if>
    }
<#if data.shouldRenderCondition?? && hasProc(data.shouldRenderCondition)>

    @Override
    public boolean shouldRender(net.minecraft.world.item.ItemStack stack) {
        return ${package}.procedures.${data.shouldRenderCondition.getName()}Procedure.execute(<@itemDepsCall data.shouldRenderCondition/>);
    }
</#if>
<#if data.colorKeyEnabled>

    @Override
    public float[] getColorKey() {
        int _rgb = 0x${data.colorKeyColor};
        return new float[] { ((_rgb >> 16) & 0xFF) / 255f, ((_rgb >> 8) & 0xFF) / 255f, (_rgb & 0xFF) / 255f };
    }

    @Override
    public float getColorKeyTolerance() {
        return ${data.colorKeyTolerance?c}f;
    }
</#if>
<#if data.nameEffectEnabled>

    @Override
    public net.minecraft.network.chat.MutableComponent getItemName(net.minecraft.world.item.ItemStack stack) {
<#if hasProc(data.nameCondition)>
        if (!${package}.procedures.${data.nameCondition.getName()}Procedure.execute(<@itemDepsCall data.nameCondition/>)) return null;
</#if>
<#if hasValue(data.name)>
<#assign _nameTxt><@strSource data.name/></#assign>
<#else>
<#assign _nameTxt>stack.getItem().getDescription().getString()</#assign>
</#if>
        return <@buildEcaText textExpr=_nameTxt?trim colorEffect=data.nameColorEffect period=data.namePeriod color1=data.nameColor1 color2=data.nameColor2 shimmer=data.nameShimmer shimmerIntensity=data.nameShimmerIntensity glitch=data.nameGlitch glitchIntensity=data.nameGlitchIntensity bold=data.nameBold italic=data.nameItalic underline=data.nameUnderline strikethrough=data.nameStrikethrough/>;
    }
</#if>
<#if data.tooltipLines?has_content>

    @Override
    public void appendTooltip(net.minecraft.world.item.ItemStack stack, net.minecraft.world.item.TooltipFlag flag, java.util.List<net.minecraft.network.chat.Component> lines) {
<#list data.tooltipLines as tline>
<#assign _lineTxt><@strSource tline.text/></#assign>
<#if hasProc(tline.condition)>
        if (${package}.procedures.${tline.condition.getName()}Procedure.execute(<@itemDepsCall tline.condition/>)) {
            lines.add(<@buildEcaText textExpr=_lineTxt?trim colorEffect=tline.colorEffect period=tline.period color1=tline.color1 color2=tline.color2 shimmer=tline.shimmer shimmerIntensity=tline.shimmerIntensity glitch=tline.glitch glitchIntensity=tline.glitchIntensity bold=tline.bold italic=tline.italic underline=tline.underline strikethrough=tline.strikethrough/>);
        }
<#else>
        lines.add(<@buildEcaText textExpr=_lineTxt?trim colorEffect=tline.colorEffect period=tline.period color1=tline.color1 color2=tline.color2 shimmer=tline.shimmer shimmerIntensity=tline.shimmerIntensity glitch=tline.glitch glitchIntensity=tline.glitchIntensity bold=tline.bold italic=tline.italic underline=tline.underline strikethrough=tline.strikethrough/>);
</#if>
</#list>
    }
</#if>
}
