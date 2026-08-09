<#-- @formatter:off -->
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#function hasValue obj>
    <#return obj?? && ((obj.getName())?? && obj.getName()?has_content && obj.getName() != "null" || obj.getFixedValue()?has_content)>
</#function>
<#-- 选了流程则调流程返回值，否则用转义后的固定文本；注册期求值，故流程无依赖 -->
<#macro strSource sp><#if hasProc(sp)>${package}.procedures.${sp.getName()}Procedure.execute()<#else>"${JavaConventions.escapeStringForJava(sp.getFixedValue())}"</#if></#macro>
<#-- 回调与判定的位置取自袭击中心，世界取自 ctx.getLevel() -->
<#macro ctxDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "world">ctx.getLevel()<#break><#case "x">ctx.getCenter().getX()<#break><#case "y">ctx.getCenter().getY()<#break><#case "z">ctx.getCenter().getZ()<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
<#-- 波次号去重后升序，作为 RaidWave 的分组依据 -->
<#assign waveIndexes = []/>
<#list data.waves as e><#if !waveIndexes?seq_contains(e.wave)><#assign waveIndexes = waveIndexes + [e.wave]/></#if></#list>
<#assign waveIndexes = waveIndexes?sort/>
package ${package}.ecaraid;

import net.eca.api.RegisterRaid;
import net.eca.util.raid.RaidContext;
import net.eca.util.raid.RaidDefinition;
import net.eca.util.raid.RaidWave;

import java.util.ArrayList;
import java.util.List;

@RegisterRaid
public class ${name}Raid extends RaidDefinition {

    <#-- ID 取元素注册名，与袭击程序块下拉列表所解析的值同源 -->
    @Override
    public String getId() {
        return "${registryname}";
    }

    @Override
    public String getDisplayName() {
        return <#if (data.displayName)?? && hasValue(data.displayName)><@strSource data.displayName/><#else>"${registryname}"</#if>;
    }

    <#-- 同一波次号的条目合并为一个 RaidWave；延迟与半径取该波次首条的值 -->
    @Override
    public List<RaidWave> getWaves() {
        List<RaidWave> _waves = new ArrayList<>();
<#list waveIndexes as wi>
<#assign rows = []/>
<#list data.waves as e><#if e.wave == wi><#assign rows = rows + [e]/></#if></#list>
        RaidWave _wave${wi} = new RaidWave();
<#list rows as r>
<#if r.leader>
        _wave${wi}.setLeader(${r.entityType.getMappedValue(1)});
<#else>
        _wave${wi}.addEntry(${r.entityType.getMappedValue(1)}, ${r.count});
</#if>
</#list>
        _wave${wi}.spawnDelay(${rows[0].spawnDelay}).spawnRadius(${rows[0].spawnRadius?c});
        _waves.add(_wave${wi});
</#list>
        return _waves;
    }
<#if data.raiderFactionId?has_content>

    @Override
    public String getRaiderFactionId() {
        return "${generator.getRegistryNameForModElement(data.raiderFactionId)}";
    }
</#if>
<#-- 结构与标签在 ECA 侧互斥（结构优先、命中即返回），故按锚定方式只生成其中一个 -->
<#if data.targetStructure?has_content && data.structureAnchor == "STRUCTURE">

    @Override
    public net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.structure.Structure> getTargetStructure() {
        return net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.STRUCTURE,
            new net.minecraft.resources.ResourceLocation("${JavaConventions.escapeStringForJava(data.targetStructure)}"));
    }
</#if>
<#if data.targetStructure?has_content && data.structureAnchor == "TAG">

    @Override
    public net.minecraft.tags.TagKey<net.minecraft.world.level.levelgen.structure.Structure> getTargetStructureTag() {
        return net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.STRUCTURE,
            new net.minecraft.resources.ResourceLocation("${JavaConventions.escapeStringForJava(data.targetStructure)}"));
    }
</#if>

    @Override
    public int getRaiderGoalPriority() {
        return ${data.raiderGoalPriority};
    }

    @Override
    public boolean isEndless() {
        return ${data.endless?c};
    }

    @Override
    public net.minecraft.world.BossEvent.BossBarColor getBossBarColor() {
        return net.minecraft.world.BossEvent.BossBarColor.${data.bossBarColor};
    }

    @Override
    public int getMaxDurationTicks() {
        return ${data.maxDurationTicks};
    }

    @Override
    public int getWaveCooldownTicks() {
        return ${data.waveCooldownTicks};
    }

    @Override
    public double getParticipantRadius() {
        return ${data.participantRadius?c};
    }

    @Override
    public int getCelebrationTicks() {
        return ${data.celebrationTicks};
    }
<#if (data.shouldAdvanceWave)?? && hasProc(data.shouldAdvanceWave)>

    @Override
    public boolean shouldAdvanceWave(RaidContext ctx) {
        return ${package}.procedures.${data.shouldAdvanceWave.getName()}Procedure.execute(<@ctxDepsCall data.shouldAdvanceWave/>);
    }
</#if>
<#if (data.checkVictory)?? && hasProc(data.checkVictory)>

    @Override
    public boolean checkVictory(RaidContext ctx) {
        return ${package}.procedures.${data.checkVictory.getName()}Procedure.execute(<@ctxDepsCall data.checkVictory/>);
    }
</#if>
<#if (data.checkDefeat)?? && hasProc(data.checkDefeat)>

    @Override
    public boolean checkDefeat(RaidContext ctx) {
        return ${package}.procedures.${data.checkDefeat.getName()}Procedure.execute(<@ctxDepsCall data.checkDefeat/>);
    }
</#if>
<#list [{"m":"onStart","p":data.onStart}, {"m":"onVictory","p":data.onVictory},
        {"m":"onDefeat","p":data.onDefeat}, {"m":"onStop","p":data.onStop}] as cb>
<#if (cb.p)?? && hasProc(cb.p)>

    @Override
    public void ${cb.m}(RaidContext ctx) {
        ${package}.procedures.${cb.p.getName()}Procedure.execute(<@ctxDepsCall cb.p/>);
    }
</#if>
</#list>
<#if (data.onWaveStart)?? && hasProc(data.onWaveStart)>

    @Override
    public void onWaveStart(RaidContext ctx, int waveIndex) {
        ${package}.procedures.${data.onWaveStart.getName()}Procedure.execute(<@ctxDepsCall data.onWaveStart/>);
    }
</#if>
<#if (data.onWaveEnd)?? && hasProc(data.onWaveEnd)>

    @Override
    public void onWaveEnd(RaidContext ctx, int waveIndex) {
        ${package}.procedures.${data.onWaveEnd.getName()}Procedure.execute(<@ctxDepsCall data.onWaveEnd/>);
    }
</#if>
}
