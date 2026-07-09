package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.parts.procedure.StringProcedure;
import net.mcreator.workspace.elements.ModElement;

import java.util.ArrayList;
import java.util.List;

public class EcaItemExtensionElement extends GeneratableElement {

    public MItemBlock item;

    //文本渲染（第一页）
    public boolean nameEffectEnabled;
    public Procedure nameCondition;
    public StringProcedure name;
    public String nameColorEffect;
    public int namePeriod;
    public String nameColor1;
    public String nameColor2;
    public boolean nameShimmer;
    public double nameShimmerIntensity;
    public boolean nameGlitch;
    public double nameGlitchIntensity;
    public boolean nameBold;
    public boolean nameItalic;
    public boolean nameUnderline;
    public boolean nameStrikethrough;
    public List<TooltipLine> tooltipLines;

    //渲染层（第二页）
    public String preset;
    public boolean renderLayerEnabled;
    public double alpha;
    public Procedure shouldRenderCondition;
    public boolean colorKeyEnabled;
    public String colorKeyColor;
    public double colorKeyTolerance;

    public EcaItemExtensionElement(ModElement element) {
        super(element);
        this.nameColorEffect = "NONE";
        this.namePeriod = 3000;
        this.nameColor1 = "FF4080";
        this.nameColor2 = "8040FF";
        this.nameShimmerIntensity = 0.15;
        this.nameGlitchIntensity = 0.05;
        this.tooltipLines = new ArrayList<>();
        this.preset = "Starlight";
        this.renderLayerEnabled = true;
        this.alpha = 1.0;
        this.colorKeyColor = "FFFFFF";
        this.colorKeyTolerance = 0.3;
    }

    //单条 tooltip 行：文本 + 效果 + 位置 + 排序 + 条件
    public static class TooltipLine {
        public StringProcedure text;
        public String colorEffect;
        public int period;
        public String color1;
        public String color2;
        public boolean shimmer;
        public double shimmerIntensity;
        public boolean glitch;
        public double glitchIntensity;
        public boolean bold;
        public boolean italic;
        public boolean underline;
        public boolean strikethrough;
        public Procedure condition;
        public String position;
        public int order;

        public TooltipLine() {
            this.colorEffect = "NONE";
            this.period = 3000;
            this.color1 = "FFFFFF";
            this.color2 = "AAAAAA";
            this.shimmerIntensity = 0.15;
            this.glitchIntensity = 0.05;
            this.position = "BODY";
            this.order = 0;
        }
    }
}
