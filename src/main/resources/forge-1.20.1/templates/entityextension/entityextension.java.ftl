<#-- @formatter:off -->
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
<#if (data.bossBarFrameShaderEnabled && data.bossBarFrameRenderType?has_content || data.bossBarFillShaderEnabled && data.bossBarFillRenderType?has_content || data.entityLayerRenderType?has_content || data.globalSkyboxShaderRenderType?has_content)>
import net.minecraft.client.renderer.RenderType;
</#if>
<#if (data.bossBarFrameTexture?has_content || data.bossBarFillTexture?has_content || data.globalSkyboxTexture?has_content)>
import net.minecraft.resources.ResourceLocation;
</#if>
<#if data.customHealthEnabled || data.customMaxHealthEnabled>
import net.minecraft.world.entity.LivingEntity;
</#if>
<#if data.customHealthEnabled && data.customHealthValue?? && data.customHealthValue.getName()?? && data.customHealthValue.getName()?has_content && data.customHealthValue.getName() != "null">
import ${package}.procedures.${data.customHealthValue.getName()}Procedure;
</#if>
<#if data.customMaxHealthEnabled && data.customMaxHealthValue?? && data.customMaxHealthValue.getName()?? && data.customMaxHealthValue.getName()?has_content && data.customMaxHealthValue.getName() != "null">
import ${package}.procedures.${data.customMaxHealthValue.getName()}Procedure;
</#if>
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#macro entityDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "entity">entity<#break><#case "world">entity.level()<#break><#case "x">entity.getX()<#break><#case "y">entity.getY()<#break><#case "z">entity.getZ()<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

<#function renderTypeClass name>
    <#switch name>
        <#case "TheLastEnd">
            <#return "net.eca.client.render.TheLastEndRenderTypes">
        <#case "DreamSakura">
            <#return "net.eca.client.render.DreamSakuraRenderTypes">
        <#case "Forest">
            <#return "net.eca.client.render.ForestRenderTypes">
        <#case "Ocean">
            <#return "net.eca.client.render.OceanRenderTypes">
        <#case "Storm">
            <#return "net.eca.client.render.StormRenderTypes">
        <#case "Volcano">
            <#return "net.eca.client.render.VolcanoRenderTypes">
        <#case "Arcane">
            <#return "net.eca.client.render.ArcaneRenderTypes">
        <#case "Aurora">
            <#return "net.eca.client.render.AuroraRenderTypes">
        <#case "Hacker">
            <#return "net.eca.client.render.HackerRenderTypes">
        <#case "Starlight">
            <#return "net.eca.client.render.StarlightRenderTypes">
        <#case "Cosmos">
            <#return "net.eca.client.render.CosmosRenderTypes">
        <#case "BlackHole">
            <#return "net.eca.client.render.BlackHoleRenderTypes">
        <#default>
            <#return "">
    </#switch>
</#function>

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
    @OnlyIn(Dist.CLIENT)
    public BossBarExtension bossBarExtension() {
        return new BossBarExtension() {
            @Override
            public boolean enabled() {
                return true;
            }

    <#if data.bossBarFrameTexture?has_content>
            @Override
            public ResourceLocation getFrameTexture() {
                return texture("screen/${data.bossBarFrameTexture}");
            }

    </#if>
    <#if data.bossBarFillTexture?has_content>
            @Override
            public ResourceLocation getFillTexture() {
                return texture("screen/${data.bossBarFillTexture}");
            }

    </#if>
    <#if data.bossBarFrameShaderEnabled && data.bossBarFrameRenderType?has_content>
            @Override
            public RenderType getFrameRenderType() {
                return ${renderTypeClass(data.bossBarFrameRenderType)}.BOSS_BAR_FRAME;
            }

            @Override public int getFrameWidth() { return ${data.bossBarFrameWidth}; }
            @Override public int getFrameHeight() { return ${data.bossBarFrameHeight}; }
    </#if>
    <#if data.bossBarFillShaderEnabled && data.bossBarFillRenderType?has_content>
            @Override
            public RenderType getFillRenderType() {
                return ${renderTypeClass(data.bossBarFillRenderType)}.BOSS_BAR_FILL;
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
        };
    }
<#else>
    @Override
    @OnlyIn(Dist.CLIENT)
    public BossBarExtension bossBarExtension() {
        return null;
    }
</#if>

<#if data.entityLayerEnabled>
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityLayerExtension entityLayerExtension() {
        return new EntityLayerExtension() {
            @Override
            public boolean enabled() {
                return true;
            }

    <#if data.entityLayerRenderType?has_content>
            @Override
            public RenderType getRenderType() {
                return ${renderTypeClass(data.entityLayerRenderType)}.BOSS_LAYER;
            }

    </#if>
            @Override
            public boolean isGlow() {
                return ${data.entityLayerGlow?c};
            }

            @Override
            public boolean isHurtOverlay() {
                return ${data.entityLayerHurtOverlay?c};
            }

            @Override
            public float getAlpha() {
                return ${data.entityLayerAlpha?c}f;
            }
        };
    }
<#else>
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityLayerExtension entityLayerExtension() {
        return null;
    }
</#if>

<#if data.globalFogEnabled>
    @Override
    @OnlyIn(Dist.CLIENT)
    public GlobalFogExtension globalFogExtension() {
        return new GlobalFogExtension() {
            @Override public boolean enabled() { return true; }
            @Override public boolean globalMode() { return ${data.globalFogGlobalMode?c}; }
            @Override public float radius() { return ${data.globalFogRadius?c}f; }
            @Override public int fogColor() { return 0x${data.globalFogColor}; }
            @Override public float terrainFogStart(float rd) { return rd * ${data.globalFogTerrainStart?c}f; }
            @Override public float terrainFogEnd(float rd) { return rd * ${data.globalFogTerrainEnd?c}f; }
            @Override public float skyFogStart(float rd) { return rd * ${data.globalFogSkyStart?c}f; }
            @Override public float skyFogEnd(float rd) { return rd * ${data.globalFogSkyEnd?c}f; }
            @Override public FogShape fogShape() { return FogShape.${data.globalFogShape}; }
        };
    }
</#if>

<#if data.globalSkyboxEnabled>
    @Override
    @OnlyIn(Dist.CLIENT)
    public GlobalSkyboxExtension globalSkyboxExtension() {
        return new GlobalSkyboxExtension() {
            @Override public boolean enabled() { return true; }
    <#if data.globalSkyboxEnableTexture && data.globalSkyboxTexture?has_content>
            @Override public boolean enableTexture() { return true; }
            @Override public ResourceLocation texture() { return texture("${data.globalSkyboxTexture}"); }
    </#if>
    <#if data.globalSkyboxEnableShader && data.globalSkyboxShaderRenderType?has_content>
            @Override public boolean enableShader() { return true; }
            @Override public RenderType shaderRenderType() { return ${renderTypeClass(data.globalSkyboxShaderRenderType)}.SKYBOX; }
    </#if>
            @Override public float alpha() { return ${data.globalSkyboxAlpha?c}f; }
            @Override public float size() { return ${data.globalSkyboxSize?c}f; }
        };
    }
</#if>

<#if data.combatMusicEnabled>
    @Override
    @OnlyIn(Dist.CLIENT)
    public CombatMusicExtension combatMusicExtension() {
        return new CombatMusicExtension() {
            @Override public boolean enabled() { return true; }
    <#if data.combatMusicSoundEventId?has_content>
            @Override public ResourceLocation soundEventId() { return sound("${data.combatMusicSoundEventId}"); }
    </#if>
            @Override public net.minecraft.sounds.SoundSource soundSource() { return net.minecraft.sounds.SoundSource.${data.combatMusicSoundSource}; }
            @Override public float volume() { return ${data.combatMusicVolume?c}f; }
            @Override public float pitch() { return ${data.combatMusicPitch?c}f; }
            @Override public boolean loop() { return ${data.combatMusicLoop?c}; }
            @Override public boolean strictMusicLock() { return ${data.combatMusicStrictLock?c}; }
        };
    }
</#if>
}
