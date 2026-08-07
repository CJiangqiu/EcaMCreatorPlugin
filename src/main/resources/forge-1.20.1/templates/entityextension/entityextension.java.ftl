<#-- @formatter:off -->
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#macro entityDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "entity">entity<#break><#case "world">entity.level()<#break><#case "x">entity.getX()<#break><#case "y">entity.getY()<#break><#case "z">entity.getZ()<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
<#macro musicExtClass rule>new CombatMusicExtension() {
                @Override public boolean enabled() { return true; }
<#if rule.soundEventId?has_content>
                @Override public net.minecraft.resources.ResourceLocation soundEventId() { return new net.minecraft.resources.ResourceLocation("${rule.soundEventId?replace('CUSTOM:', modid + ':')}"); }
</#if>
                @Override public net.minecraft.sounds.SoundSource soundSource() { return net.minecraft.sounds.SoundSource.${rule.soundSource}; }
                @Override public float volume() { return ${rule.volume?c}f; }
                @Override public float pitch() { return ${rule.pitch?c}f; }
                @Override public boolean loop() { return ${rule.loop?c}; }
                @Override public boolean strictMusicLock() { return ${rule.strictMusicLock?c}; }
            }</#macro>
<#macro entityLayerClass entry>new EntityLayerExtension() {
                @Override public boolean enabled() { return true; }
<#if entry.enableTexture && entry.texture?has_content>
                @Override public net.minecraft.resources.ResourceLocation getTexture() { return ${name}EntityExtension.this.texture("entities/${entry.texture}.png"); }
</#if>
<#if entry.renderType?has_content>
                @Override public RenderType getRenderType() { return <@presetRenderType name=entry.renderType variant="BOSS_LAYER"/>; }
<#if entry.enableMask && entry.maskTexture?has_content>
                @Override public java.util.List<net.eca.client.render.ShaderMaskPass> getShaderPasses() {
                    return java.util.List.of(net.eca.client.render.ShaderMaskPass.masked(<@presetRenderType name=entry.renderType variant="BOSS_LAYER"/>, ${name}EntityExtension.this.texture("entities/${entry.maskTexture}.png"), 0x${entry.maskColor}, ${entry.maskTolerance?c}f, ${entry.alpha?c}f));
                }
</#if>
</#if>
                @Override public boolean isGlow() { return ${entry.glow?c}; }
                @Override public boolean isHurtOverlay() { return ${entry.hurtOverlay?c}; }
                @Override public float getAlpha() { return ${entry.alpha?c}f; }
            }</#macro>
<#macro fogClass entry>new GlobalFogExtension() {
                @Override public boolean enabled() { return true; }
                @Override public boolean globalMode() { return ${entry.globalMode?c}; }
                @Override public float radius() { return ${entry.radius?c}f; }
                @Override public int fogColor() { return 0x${entry.color}; }
                @Override public float terrainFogStart(float rd) { return rd * ${entry.terrainStart?c}f; }
                @Override public float terrainFogEnd(float rd) { return rd * ${entry.terrainEnd?c}f; }
                @Override public float skyFogStart(float rd) { return rd * ${entry.skyStart?c}f; }
                @Override public float skyFogEnd(float rd) { return rd * ${entry.skyEnd?c}f; }
                @Override public FogShape fogShape() { return FogShape.${entry.shape}; }
            }</#macro>
<#macro skyboxClass entry>new GlobalSkyboxExtension() {
                @Override public boolean enabled() { return true; }
<#if entry.enableTexture && entry.texture?has_content>
                @Override public boolean enableTexture() { return true; }
                @Override public net.minecraft.resources.ResourceLocation texture() { return ${name}EntityExtension.this.texture("screens/${entry.texture}.png"); }
</#if>
<#if entry.enableShader && entry.shaderRenderType?has_content>
                @Override public boolean enableShader() { return true; }
                @Override public RenderType shaderRenderType() { return <@presetRenderType name=entry.shaderRenderType variant="SKYBOX"/>; }
</#if>
                @Override public float alpha() { return ${entry.alpha?c}f; }
                @Override public float size() { return ${entry.size?c}f; }
                @Override public float textureUvScale() { return ${entry.textureUvScale?c}f; }
                @Override public float textureRed() { return ${entry.textureRed?c}f; }
                @Override public float textureGreen() { return ${entry.textureGreen?c}f; }
                @Override public float textureBlue() { return ${entry.textureBlue?c}f; }
            }</#macro>
package ${package}.entityextension;

import net.eca.api.RegisterEntityExtension;
import net.eca.util.entity_extension.EntityExtension;
import net.eca.util.entity_extension.EntityExtensionManager;
import net.eca.util.entity_extension.BossBarExtension;
import net.eca.util.entity_extension.EntityLayerExtension;
<#if data.globalFogEnabled>
import net.eca.util.entity_extension.GlobalFogExtension;
import com.mojang.blaze3d.shaders.FogShape;
</#if>
<#if data.globalSkyboxEnabled>
import net.eca.util.entity_extension.GlobalSkyboxExtension;
</#if>
<#if data.combatMusicEnabled>
import net.eca.util.entity_extension.CombatMusicExtension;
</#if>
<#if (data.bossBarFrameShaderEnabled && data.bossBarFrameRenderType?has_content || data.bossBarFillShaderEnabled && data.bossBarFillRenderType?has_content || (data.entityLayerEnabled && data.entityLayerEntries?has_content) || (data.globalSkyboxEnabled && data.skyboxEntries?has_content))>
import net.minecraft.client.renderer.RenderType;
</#if>
<#if (data.bossBarFrameTexture?has_content || data.bossBarFillTexture?has_content)>
import net.minecraft.resources.ResourceLocation;
</#if>
<#assign needsLivingEntity = data.customHealthEnabled || data.customMaxHealthEnabled || (data.bossBarEnabled && data.bossBarCondition?? && hasProc(data.bossBarCondition)) || (data.entityLayerEnabled && data.entityLayerEntries?has_content) || (data.globalFogEnabled && data.fogEntries?has_content) || (data.globalSkyboxEnabled && data.skyboxEntries?has_content) || (data.combatMusicEnabled && data.musicRules?has_content)>
<#if needsLivingEntity>
import net.minecraft.world.entity.LivingEntity;
</#if>
<#if data.customHealthEnabled && data.customHealthValue?? && data.customHealthValue.getName()?? && data.customHealthValue.getName()?has_content && data.customHealthValue.getName() != "null">
import ${package}.procedures.${data.customHealthValue.getName()}Procedure;
</#if>
<#if data.customMaxHealthEnabled && data.customMaxHealthValue?? && data.customMaxHealthValue.getName()?? && data.customMaxHealthValue.getName()?has_content && data.customMaxHealthValue.getName() != "null">
import ${package}.procedures.${data.customMaxHealthValue.getName()}Procedure;
</#if>
<#if data.bossBarCondition?? && hasProc(data.bossBarCondition)>
import ${package}.procedures.${data.bossBarCondition.getName()}Procedure;
</#if>
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

<#function isBuiltin name>
    <#return name == "TheLastEnd" || name == "DreamSakura" || name == "Forest" || name == "Ocean" || name == "Storm" || name == "Volcano" || name == "Arcane" || name == "Aurora" || name == "Hacker" || name == "Starlight" || name == "Cosmos" || name == "BlackHole">
</#function>
<#function renderTypeClass name>
    <#switch name>
        <#case "TheLastEnd"><#return "net.eca.client.render.TheLastEndRenderTypes">
        <#case "DreamSakura"><#return "net.eca.client.render.DreamSakuraRenderTypes">
        <#case "Forest"><#return "net.eca.client.render.ForestRenderTypes">
        <#case "Ocean"><#return "net.eca.client.render.OceanRenderTypes">
        <#case "Storm"><#return "net.eca.client.render.StormRenderTypes">
        <#case "Volcano"><#return "net.eca.client.render.VolcanoRenderTypes">
        <#case "Arcane"><#return "net.eca.client.render.ArcaneRenderTypes">
        <#case "Aurora"><#return "net.eca.client.render.AuroraRenderTypes">
        <#case "Hacker"><#return "net.eca.client.render.HackerRenderTypes">
        <#case "Starlight"><#return "net.eca.client.render.StarlightRenderTypes">
        <#case "Cosmos"><#return "net.eca.client.render.CosmosRenderTypes">
        <#case "BlackHole"><#return "net.eca.client.render.BlackHoleRenderTypes">
        <#default><#return "">
    </#switch>
</#function>
<#function ecaPresetMethod variant>
    <#switch variant>
        <#case "BOSS_BAR"><#return "bossBar">
        <#case "BOSS_LAYER"><#return "bossLayer">
        <#case "SKYBOX"><#return "skybox">
        <#case "ITEM"><#return "item">
        <#default><#return "">
    </#switch>
</#function>
<#macro presetRenderType name variant>
    <#if isBuiltin(name)>${renderTypeClass(name)}.${variant}<#else>net.eca.client.render.preset.EcaPresets.${ecaPresetMethod(variant)}("${modid}:${name}")</#if>
</#macro>

@RegisterEntityExtension
public class ${name}EntityExtension extends EntityExtension {

    static {
        EntityExtensionManager.register(new ${name}EntityExtension());
    }

    public ${name}EntityExtension() {
        super(${data.entityType.getMappedValue(1)}, ${data.priority});
    }

    @Override
    public boolean enableForceLoading() {
        return ${data.enableForceLoading?c};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected String getModId() {
        return "${modid}";
    }

<#if data.customHealthEnabled>
    @Override
    public boolean enableCustomHealthOverride() {
        return true;
    }

    @Override
    public Number getCustomHealthValue(LivingEntity entity) {
    <#if hasProc(data.customHealthValue)>
        return (float) ${data.customHealthValue.getName()}Procedure.execute(<@entityDepsCall data.customHealthValue/>);
    <#else>
        return ${data.customHealthValue.getFixedValue()}f;
    </#if>
    }

</#if>
<#if data.customMaxHealthEnabled>
    @Override
    public boolean enableCustomMaxHealthOverride() {
        return true;
    }

    @Override
    public Number getCustomMaxHealthValue(LivingEntity entity) {
    <#if hasProc(data.customMaxHealthValue)>
        return (float) ${data.customMaxHealthValue.getName()}Procedure.execute(<@entityDepsCall data.customMaxHealthValue/>);
    <#else>
        return ${data.customMaxHealthValue.getFixedValue()}f;
    </#if>
    }

</#if>
<#if data.bossBarEnabled>
    @Override
    public boolean enableBossBar() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BossBarExtension bossBarExtension() {
        return new BossBarExtension() {
            @Override
            public boolean enabled() {
                return true;
            }

    <#if data.bossBarFrameEnableTexture && data.bossBarFrameTexture?has_content>
            @Override
            public ResourceLocation getFrameTexture() {
                return texture("screens/${data.bossBarFrameTexture}.png");
            }

    </#if>
    <#if data.bossBarFillEnableTexture && data.bossBarFillTexture?has_content>
            @Override
            public ResourceLocation getFillTexture() {
                return texture("screens/${data.bossBarFillTexture}.png");
            }

    </#if>
    <#if data.bossBarFrameShaderEnabled && data.bossBarFrameRenderType?has_content>
            @Override
            public RenderType getFrameRenderType() {
                return <@presetRenderType name=data.bossBarFrameRenderType variant="BOSS_BAR"/>;
            }

            @Override public int getFrameWidth() { return ${data.bossBarFrameWidth}; }
            @Override public int getFrameHeight() { return ${data.bossBarFrameHeight}; }
    </#if>
    <#if data.bossBarFillShaderEnabled && data.bossBarFillRenderType?has_content>
            @Override
            public RenderType getFillRenderType() {
                return <@presetRenderType name=data.bossBarFillRenderType variant="BOSS_BAR"/>;
            }

            @Override public int getFillWidth() { return ${data.bossBarFillWidth}; }
            @Override public int getFillHeight() { return ${data.bossBarFillHeight}; }
    </#if>
    <#if (data.bossBarFrameOffsetX != 0)>
            @Override public int getFrameOffsetX() { return ${data.bossBarFrameOffsetX}; }
    </#if>
    <#if (data.bossBarFrameOffsetY != 0)>
            @Override public int getFrameOffsetY() { return ${data.bossBarFrameOffsetY}; }
    </#if>
    <#if (data.bossBarFillOffsetX != 0)>
            @Override public int getFillOffsetX() { return ${data.bossBarFillOffsetX}; }
    </#if>
    <#if (data.bossBarFillOffsetY != 0)>
            @Override public int getFillOffsetY() { return ${data.bossBarFillOffsetY}; }
    </#if>
    <#if (data.bossBarFrameAlpha != 100)>
            @Override public float getFrameAlpha() { return ${(data.bossBarFrameAlpha / 100.0)?c}f; }
    </#if>
    <#if (data.bossBarFillAlpha != 100)>
            @Override public float getFillAlpha() { return ${(data.bossBarFillAlpha / 100.0)?c}f; }
    </#if>
        };
    }
<#else>
    @Override
    @OnlyIn(Dist.CLIENT)
    public BossBarExtension bossBarExtension() {
        return null;
    }
</#if>

<#if data.entityLayerEnabled && data.entityLayerEntries?has_content>
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityLayerExtension entityLayerExtension() {
        return entityLayerExtension(null);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityLayerExtension entityLayerExtension(LivingEntity entity) {
<#list data.entityLayerEntries as entry>
<#if entry.condition?? && hasProc(entry.condition)>
        if (entity != null && ${package}.procedures.${entry.condition.getName()}Procedure.execute(<@entityDepsCall entry.condition/>)) {
            return <@entityLayerClass entry/>;
        }
</#if>
</#list>
<#assign hasDefault = false>
<#list data.entityLayerEntries as entry>
<#if !(entry.condition?? && hasProc(entry.condition))>
        return <@entityLayerClass entry/>;
<#assign hasDefault = true>
<#break>
</#if>
</#list>
<#if !hasDefault>
        return null;
</#if>
    }
<#else>
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityLayerExtension entityLayerExtension() {
        return null;
    }
</#if>

<#if data.globalFogEnabled && data.fogEntries?has_content>
    @Override
    @OnlyIn(Dist.CLIENT)
    public GlobalFogExtension globalFogExtension() {
        return globalFogExtension(null);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GlobalFogExtension globalFogExtension(LivingEntity entity) {
<#list data.fogEntries as entry>
<#if entry.condition?? && hasProc(entry.condition)>
        if (entity != null && ${package}.procedures.${entry.condition.getName()}Procedure.execute(<@entityDepsCall entry.condition/>)) {
            return <@fogClass entry/>;
        }
</#if>
</#list>
<#assign hasDefault = false>
<#list data.fogEntries as entry>
<#if !(entry.condition?? && hasProc(entry.condition))>
        return <@fogClass entry/>;
<#assign hasDefault = true>
<#break>
</#if>
</#list>
<#if !hasDefault>
        return null;
</#if>
    }
</#if>

<#if data.globalSkyboxEnabled && data.skyboxEntries?has_content>
    @Override
    @OnlyIn(Dist.CLIENT)
    public GlobalSkyboxExtension globalSkyboxExtension() {
        return globalSkyboxExtension(null);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GlobalSkyboxExtension globalSkyboxExtension(LivingEntity entity) {
<#list data.skyboxEntries as entry>
<#if entry.condition?? && hasProc(entry.condition)>
        if (entity != null && ${package}.procedures.${entry.condition.getName()}Procedure.execute(<@entityDepsCall entry.condition/>)) {
            return <@skyboxClass entry/>;
        }
</#if>
</#list>
<#assign hasDefault = false>
<#list data.skyboxEntries as entry>
<#if !(entry.condition?? && hasProc(entry.condition))>
        return <@skyboxClass entry/>;
<#assign hasDefault = true>
<#break>
</#if>
</#list>
<#if !hasDefault>
        return null;
</#if>
    }
</#if>

<#if data.combatMusicEnabled && data.musicRules?has_content>
    @Override
    @OnlyIn(Dist.CLIENT)
    public CombatMusicExtension combatMusicExtension() {
        return combatMusicExtension(null);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public CombatMusicExtension combatMusicExtension(LivingEntity entity) {
<#-- 先按列表顺序判定带条件的音乐 -->
<#list data.musicRules as rule>
<#if rule.condition?? && hasProc(rule.condition)>
        if (entity != null && ${package}.procedures.${rule.condition.getName()}Procedure.execute(<@entityDepsCall rule.condition/>)) {
            return <@musicExtClass rule/>;
        }
</#if>
</#list>
<#-- 无条件的那条作为默认兜底（取首个），没有则不播放 -->
<#assign hasDefault = false>
<#list data.musicRules as rule>
<#if !(rule.condition?? && hasProc(rule.condition))>
        return <@musicExtClass rule/>;
<#assign hasDefault = true>
<#break>
</#if>
</#list>
<#if !hasDefault>
        return null;
</#if>
    }
</#if>

<#if data.bossBarEnabled && data.bossBarCondition?? && hasProc(data.bossBarCondition)>
    @Override
    public boolean shouldShowBossBar(LivingEntity entity) {
        return ${data.bossBarCondition.getName()}Procedure.execute(<@entityDepsCall data.bossBarCondition/>);
    }
</#if>


}
