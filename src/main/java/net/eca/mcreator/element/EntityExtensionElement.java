package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.parts.procedure.NumberProcedure;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.workspace.elements.ModElement;

import java.util.ArrayList;
import java.util.List;

public class EntityExtensionElement extends GeneratableElement {

    public EntityEntry entityType;
    public int priority;
    // 本类型实体自动加入的阵营（元素名，空=不加入）
    public String factionId;
    public boolean enableForceLoading;

    // Condition Procedures
    public Procedure bossBarCondition;

    // Boss Bar
    public boolean bossBarEnabled;
    public boolean bossBarFrameEnableTexture;
    public String bossBarFrameTexture;
    public boolean bossBarFillEnableTexture;
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
    public int bossBarFrameAlpha;
    public int bossBarFillAlpha;

    // Custom Health Display
    public boolean customHealthEnabled;
    public NumberProcedure customHealthValue;
    public boolean customMaxHealthEnabled;
    public NumberProcedure customMaxHealthValue;

    // Entity Layer — a list of layer entries, first matching condition wins
    public boolean entityLayerEnabled;
    public List<EntityLayerEntry> entityLayerEntries;

    // Global Fog — a list of fog entries, first matching condition wins
    public boolean globalFogEnabled;
    public List<FogEntry> fogEntries;

    // Global Skybox — a list of skybox entries, first matching condition wins
    public boolean globalSkyboxEnabled;
    public List<SkyboxEntry> skyboxEntries;

    // Combat Music — a list of music entries, first matching condition wins
    public boolean combatMusicEnabled;
    public List<MusicRule> musicRules;

    public EntityExtensionElement(ModElement element) {
        super(element);
        this.factionId = "";

        this.priority = 5;
        this.bossBarFrameWidth = 182;
        this.bossBarFrameHeight = 5;
        this.bossBarFillWidth = 182;
        this.bossBarFillHeight = 5;
        this.bossBarFrameAlpha = 100;
        this.bossBarFillAlpha = 100;
        this.customHealthValue = new NumberProcedure(null, 20);
        this.customMaxHealthValue = new NumberProcedure(null, 20);
        this.entityLayerEntries = new ArrayList<>();
        this.fogEntries = new ArrayList<>();
        this.skyboxEntries = new ArrayList<>();
        this.musicRules = new ArrayList<>();
    }

    // 条件实体图层：条件为真时使用本条图层，按列表顺序首个匹配生效；无条件作默认兜底
    // 支持3种渲染模式：仅贴图(texture非空+renderType空)、仅着色器(renderType非空+texture空)、混合(两者都非空)
    public static class EntityLayerEntry {
        public Procedure condition;
        public boolean enableTexture;
        public String texture;
        public String renderType;
        public boolean glow;
        public boolean hurtOverlay;
        public double alpha;
        // 着色器遮罩：仅在启用着色器时生效，遮罩贴图中目标颜色的像素才会被着色
        public boolean enableMask;
        public String maskTexture;
        public String maskColor;
        public double maskTolerance;

        public EntityLayerEntry() {
            this.texture = "";
            this.renderType = "";
            this.alpha = 0.8;
            this.maskTexture = "";
            this.maskColor = "000000";
            this.maskTolerance = 0.05;
        }
    }

    // 条件全局天空盒：条件为真时使用本条天空盒，按列表顺序首个匹配生效；无条件作默认兜底
    public static class SkyboxEntry {
        public Procedure condition;
        public boolean enableTexture;
        public String texture;
        public boolean enableShader;
        public String shaderRenderType;
        public double alpha;
        public double size;
        // 贴图色彩调制（仅 enableTexture 时生效）
        public double textureUvScale;
        public double textureRed;
        public double textureGreen;
        public double textureBlue;

        public SkyboxEntry() {
            this.texture = "";
            this.shaderRenderType = "";
            this.alpha = 0.9;
            this.size = 100.0;
            this.textureUvScale = 16.0;
            this.textureRed = 1.0;
            this.textureGreen = 1.0;
            this.textureBlue = 1.0;
        }
    }

    // 条件全局雾：条件为真时使用本条雾，按列表顺序首个匹配生效；无条件作默认兜底
    public static class FogEntry {
        public Procedure condition;
        public boolean globalMode;
        public double radius;
        public String color;
        public double terrainStart;
        public double terrainEnd;
        public double skyStart;
        public double skyEnd;
        public String shape;

        public FogEntry() {
            this.radius = 32.0;
            this.color = "808080";
            this.terrainStart = 0.25;
            this.terrainEnd = 1.0;
            this.skyStart = 0.0;
            this.skyEnd = 1.0;
            this.shape = "SPHERE";
        }
    }

    // 条件音乐规则：条件为真时使用本条音乐，按列表顺序首个匹配生效
    public static class MusicRule {
        public Procedure condition;
        public String soundEventId;
        public String soundSource;
        public double volume;
        public double pitch;
        public boolean loop;
        public boolean strictMusicLock;

        public MusicRule() {
            this.soundSource = "MUSIC";
            this.volume = 1.0;
            this.pitch = 1.0;
        }
    }
}
