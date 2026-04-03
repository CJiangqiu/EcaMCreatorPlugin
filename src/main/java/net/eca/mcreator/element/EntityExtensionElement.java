package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.parts.procedure.NumberProcedure;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.workspace.elements.ModElement;

public class EntityExtensionElement extends GeneratableElement {

    public EntityEntry entityType;
    public int priority;
    public boolean enableForceLoading;

    // Condition Procedures
    public Procedure bossBarCondition;
    public Procedure fogCondition;
    public Procedure skyboxCondition;
    public Procedure musicCondition;

    // Boss Bar
    public boolean bossBarEnabled;
    public String bossBarFrameTexture;
    public String bossBarFillTexture;
    public boolean bossBarFrameShaderEnabled;
    public String bossBarFrameRenderType;
    public int bossBarFrameWidth;
    public int bossBarFrameHeight;
    public boolean bossBarFillShaderEnabled;
    public String bossBarFillRenderType;
    public int bossBarFillWidth;
    public int bossBarFillHeight;
    public int bossBarFrameOffsetX;
    public int bossBarFrameOffsetY;
    public int bossBarFillOffsetX;
    public int bossBarFillOffsetY;

    // Custom Health Display
    public boolean customHealthEnabled;
    public NumberProcedure customHealthValue;
    public boolean customMaxHealthEnabled;
    public NumberProcedure customMaxHealthValue;

    // Entity Layer
    public boolean entityLayerEnabled;
    public String entityLayerRenderType;
    public boolean entityLayerGlow;
    public boolean entityLayerHurtOverlay;
    public double entityLayerAlpha;

    // Global Fog
    public boolean globalFogEnabled;
    public boolean globalFogGlobalMode;
    public double globalFogRadius;
    public String globalFogColor;
    public double globalFogTerrainStart;
    public double globalFogTerrainEnd;
    public double globalFogSkyStart;
    public double globalFogSkyEnd;
    public String globalFogShape;

    // Global Skybox
    public boolean globalSkyboxEnabled;
    public boolean globalSkyboxEnableTexture;
    public String globalSkyboxTexture;
    public boolean globalSkyboxEnableShader;
    public String globalSkyboxShaderRenderType;
    public double globalSkyboxAlpha;
    public double globalSkyboxSize;

    // Combat Music
    public boolean combatMusicEnabled;
    public String combatMusicSoundEventId;
    public String combatMusicSoundSource;
    public double combatMusicVolume;
    public double combatMusicPitch;
    public boolean combatMusicLoop;
    public boolean combatMusicStrictLock;

    public EntityExtensionElement(ModElement element) {
        super(element);

        this.priority = 5;
        this.bossBarFrameWidth = 182;
        this.bossBarFrameHeight = 5;
        this.bossBarFillWidth = 182;
        this.bossBarFillHeight = 5;
        this.customHealthValue = new NumberProcedure(null, 20);
        this.customMaxHealthValue = new NumberProcedure(null, 20);
        this.entityLayerAlpha = 0.8;
        this.globalFogRadius = 32.0;
        this.globalFogColor = "808080";
        this.globalFogTerrainStart = 0.25;
        this.globalFogTerrainEnd = 1.0;
        this.globalFogSkyStart = 0.0;
        this.globalFogSkyEnd = 1.0;
        this.globalFogShape = "SPHERE";
        this.globalSkyboxAlpha = 0.9;
        this.globalSkyboxSize = 100.0;
        this.combatMusicVolume = 1.0;
        this.combatMusicPitch = 1.0;
        this.combatMusicSoundSource = "MUSIC";
    }
}
