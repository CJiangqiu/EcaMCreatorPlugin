package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.EntityEntry;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.parts.procedure.StringProcedure;
import net.mcreator.workspace.elements.ModElement;

import java.util.ArrayList;
import java.util.List;

public class EcaRaidElement extends GeneratableElement {

    // 基础
    public StringProcedure displayName;
    public String raiderFactionId;
    // 锚定方式：NONE / STRUCTURE / TAG —— ECA 侧二者互斥，结构优先且会让标签失效
    public String structureAnchor;
    public String targetStructure;
    public int raiderGoalPriority;
    public boolean endless;
    public String bossBarColor;

    // 节奏与范围
    public int maxDurationTicks;
    public int waveCooldownTicks;
    public double participantRadius;
    public int celebrationTicks;

    // 波次：扁平列表，按波次号分组成 RaidWave
    public List<WaveEntry> waves;

    // 判定（留空则用 ECA 默认逻辑）
    public Procedure shouldAdvanceWave;
    public Procedure checkVictory;
    public Procedure checkDefeat;

    // 生命周期回调
    public Procedure onStart;
    public Procedure onWaveStart;
    public Procedure onWaveEnd;
    public Procedure onVictory;
    public Procedure onDefeat;
    public Procedure onStop;

    public EcaRaidElement(ModElement element) {
        super(element);
        this.raiderFactionId = "";
        this.structureAnchor = "NONE";
        this.targetStructure = "";
        this.raiderGoalPriority = 3;
        this.bossBarColor = "RED";
        this.maxDurationTicks = 48000;
        this.waveCooldownTicks = 300;
        this.participantRadius = 96.0;
        this.celebrationTicks = 600;
        this.waves = new ArrayList<>();
    }

    // 单条生成条目。波次号相同的条目合并为一个 RaidWave；
    // 延迟与半径是波次级属性，同波次取首条的值
    public static class WaveEntry {
        public int wave;
        public EntityEntry entityType;
        public int count;
        public boolean leader;
        public int spawnDelay;
        public double spawnRadius;

        public WaveEntry() {
            this.wave = 1;
            this.count = 1;
            this.spawnRadius = 24.0;
        }
    }
}
