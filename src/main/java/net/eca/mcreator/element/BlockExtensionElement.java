package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.workspace.elements.ModElement;

public class BlockExtensionElement extends GeneratableElement {

    public MItemBlock block;

    // 渲染层：着色器预设只能用工作区五文件集预设，ECA内置12个预设没有block配置
    public String preset;
    public boolean renderLayerEnabled;
    public double alpha;
    public boolean glow;
    public Procedure shouldRenderCondition;

    // 着色器遮罩：遮罩贴图中目标颜色的像素才会被着色
    public boolean enableMask;
    public String maskTexture;
    public String maskColor;
    public double maskTolerance;

    public BlockExtensionElement(ModElement element) {
        super(element);
        this.preset = "";
        this.renderLayerEnabled = true;
        this.alpha = 1.0;
        this.maskTexture = "";
        this.maskColor = "000000";
        this.maskTolerance = 0.05;
    }
}
