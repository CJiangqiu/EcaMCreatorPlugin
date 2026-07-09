package net.eca.mcreator.element;

import net.mcreator.element.GeneratableElement;
import net.mcreator.workspace.elements.ModElement;

/** ECA着色器预设元素：注册自定义着色器预设（五文件集）供其他元素引用 */
public class ShaderPresetElement extends GeneratableElement {

    /** 预设文件名（不含扩展名），即五文件集的 &lt;name&gt; 部分 */
    public String presetName;

    public ShaderPresetElement(ModElement element) {
        super(element);
        this.presetName = "";
    }
}
