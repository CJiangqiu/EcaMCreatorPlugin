<#-- @formatter:off -->
<#function hasProc obj>
    <#return obj?? && obj.getName()?? && obj.getName()?has_content && obj.getName() != "null">
</#function>
<#macro markerDepsCall obj>
<#list obj.getDependencies(generator.getWorkspace()) as dep><#switch dep.getName()><#case "entity">ctx.target()<#break><#case "world">ctx.target().level()<#break><#case "x">ctx.target().getX()<#break><#case "y">ctx.target().getY()<#break><#case "z">ctx.target().getZ()<#break><#case "sourceentity">ctx.viewer()<#break><#default>${dep.getName()}<#break></#switch><#if dep?has_next>, </#if></#list>
</#macro>
package ${package}.bossshow;

import net.eca.api.RegisterBossShow;
import net.eca.util.bossshow.BossShow;
import net.eca.util.bossshow.BossShowContext;
import net.eca.util.bossshow.BossShowManager;
import net.minecraft.resources.ResourceLocation;
<#list data.markerMappings as mapping>
<#if hasProc(mapping.procedure)>
import ${package}.procedures.${mapping.procedure.getName()}Procedure;
</#if>
</#list>

@RegisterBossShow
public class ${name}BossShow extends BossShow {

    static {
        BossShowManager.register(new ${name}BossShow());
    }

    public ${name}BossShow() {
        super(new ResourceLocation("${modid}", "${data.bossShowId}"), ${data.targetEntityType.getMappedValue(1)});
    }

    @Override
    public void onMarkerEvent(String eventId, BossShowContext ctx) {
        switch (eventId) {
        <#list data.markerMappings as mapping>
        <#if hasProc(mapping.procedure) && mapping.eventId?has_content>
            case "${mapping.eventId}" -> ${mapping.procedure.getName()}Procedure.execute(<@markerDepsCall mapping.procedure/>);
        </#if>
        </#list>
            default -> {}
        }
    }
}
