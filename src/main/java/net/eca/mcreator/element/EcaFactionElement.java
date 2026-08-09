package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.parts.procedure.StringProcedure;
import net.mcreator.workspace.elements.ModElement;

import java.util.ArrayList;
import java.util.List;

public class EcaFactionElement extends GeneratableElement {

    // 基础元数据
    public StringProcedure displayName;
    public String color;

    // 关系列表：每条为「目标阵营 + 关系 + 可选条件」；
    // faction 为哨兵值 $noFaction 时表示针对无阵营实体，对应 ECA 的默认关系
    public List<RelationEntry> relations;

    public EcaFactionElement(ModElement element) {
        super(element);
        this.color = "FFFFFF";
        this.relations = new ArrayList<>();
    }

    // 单条阵营关系。无条件的进静态预设列表；带条件的进 getRelation 动态判定
    public static class RelationEntry {
        public String faction;
        public String relation;
        public Procedure condition;

        public RelationEntry() {
            this.faction = "";
            this.relation = "HOSTILE";
        }
    }
}
