<#-- @formatter:off -->
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#function hasValue obj>
    <#return obj?? && ((obj.getName())?? && obj.getName()?has_content && obj.getName() != "null" || obj.getFixedValue()?has_content)>
</#function>
<#-- 选了流程则调流程返回值，否则用转义后的固定文本；注册期求值，故流程无依赖 -->
<#macro strSource sp><#if hasProc(sp)>${package}.procedures.${sp.getName()}Procedure.execute()<#else>"${JavaConventions.escapeStringForJava(sp.getFixedValue())}"</#if></#macro>
<#-- 关系条目存的是阵营元素名，取其注册名，与 getId() 和阵营程序块下拉同源 -->
<#function factionId entry><#return generator.getRegistryNameForModElement(entry.faction)></#function>
<#assign NO_FACTION = "$noFaction"/>
<#function isNoFaction e><#return e.faction?has_content && e.faction == NO_FACTION></#function>
<#-- 针对具体阵营的条目：排除自身（同阵营由 SAME_FACTION 自动派生）与无阵营行 -->
<#function usable e><#return e.faction?has_content && !isNoFaction(e) && factionId(e) != registryname></#function>
<#-- 无阵营行：无条件的作静态默认关系，带条件的作动态默认关系 -->
<#assign noFactionStatic = ""/>
<#assign noFactionDynamic = []/>
<#list data.relations as e><#if isNoFaction(e)><#if (e.condition)?? && hasProc(e.condition)><#assign noFactionDynamic = noFactionDynamic + [e]/><#else><#assign noFactionStatic = e.relation/></#if></#if></#list>
<#function hasStatic rel><#list data.relations as e><#if !((e.condition)?? && hasProc(e.condition)) && e.relation == rel && usable(e)><#return true></#if></#list><#return false></#function>
<#macro staticIds rel><#assign _first=true/><#list data.relations as e><#if !((e.condition)?? && hasProc(e.condition)) && e.relation == rel && usable(e)><#if !_first>, </#if>"${factionId(e)}"<#assign _first=false/></#if></#list></#macro>
<#-- self 可能为 null；target 恒非空，位置/世界依赖一律取自 target -->
<#macro relationDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "entity">target<#break><#case "sourceentity">self<#break><#case "world">target.level()<#break><#case "x">target.getX()<#break><#case "y">target.getY()<#break><#case "z">target.getZ()<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
<#assign conditional = []/>
<#list data.relations as e><#if (e.condition)?? && hasProc(e.condition) && usable(e)><#assign conditional = conditional + [e]/></#if></#list>
package ${package}.ecafaction;

import net.eca.api.RegisterFaction;
import net.eca.util.faction.FactionDefinition;
import net.eca.util.faction.FactionRelation;

@RegisterFaction
public class ${name}Faction extends FactionDefinition {

    <#-- ID 取元素注册名，与阵营程序块下拉列表所解析的值同源，二者必然一致 -->
    @Override
    public String getId() {
        return "${registryname}";
    }

    @Override
    public String getDisplayName() {
        return <#if (data.displayName)?? && hasValue(data.displayName)><@strSource data.displayName/><#else>"${registryname}"</#if>;
    }

    @Override
    public int getColor() {
        return 0xFF${data.color};
    }

<#if noFactionStatic?has_content>
    @Override
    public FactionRelation getStaticDefaultRelation() {
        return FactionRelation.${noFactionStatic};
    }
</#if>
<#if hasStatic("HOSTILE")>

    @Override
    public String[] getHostileTo() {
        return new String[]{<@staticIds "HOSTILE"/>};
    }
</#if>
<#if hasStatic("FRIENDLY")>

    @Override
    public String[] getFriendlyTo() {
        return new String[]{<@staticIds "FRIENDLY"/>};
    }
</#if>
<#if hasStatic("NEUTRAL")>

    @Override
    public String[] getNeutralTo() {
        return new String[]{<@staticIds "NEUTRAL"/>};
    }
</#if>
<#if noFactionDynamic?has_content>

    <#-- 无阵营行且带条件：目标没有阵营且条件成立时给出所选关系，否则回退到静态默认关系 -->
    @Override
    public FactionRelation getDefaultRelation(net.minecraft.world.entity.LivingEntity self,
                                              net.minecraft.world.entity.Entity target) {
        if (target == null) {
            return null;
        }
<#list noFactionDynamic as e>
        if (${package}.procedures.${e.condition.getName()}Procedure.execute(<@relationDepsCall e.condition/>)) {
            return FactionRelation.${e.relation};
        }
</#list>
        return null;
    }
</#if>
<#if conditional?has_content>

    <#-- 带条件的条目：目标属于该阵营且条件成立时给出所选关系，否则返回 null 交还给静态预设 -->
    @Override
    public FactionRelation getRelation(net.minecraft.world.entity.LivingEntity self,
                                       net.minecraft.world.entity.Entity target) {
        if (target == null) {
            return null;
        }
        String _targetFaction = net.eca.api.EcaAPI.getEntityFaction(target);
        if (_targetFaction == null) {
            return null;
        }
<#list conditional as e>
        if (_targetFaction.equals("${factionId(e)}")
                && ${package}.procedures.${e.condition.getName()}Procedure.execute(<@relationDepsCall e.condition/>)) {
            return FactionRelation.${e.relation};
        }
</#list>
        return null;
    }
</#if>
}
