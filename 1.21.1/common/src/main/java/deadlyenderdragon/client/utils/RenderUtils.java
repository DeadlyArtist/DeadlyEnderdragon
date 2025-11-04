package deadlyenderdragon.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {
    public static void renderGreyOverlay(MatrixStack matrices, int x, int y) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        float alpha = 0.6f;
        int grey = 80;
        int a = (int) (alpha * 255);

        var matrix = matrices.peek().getPositionMatrix();

        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x, y + 16, 200f).color(grey, grey, grey, a);
        buffer.vertex(matrix, x + 16, y + 16, 200f).color(grey, grey, grey, a);
        buffer.vertex(matrix, x + 16, y, 200f).color(grey, grey, grey, a);
        buffer.vertex(matrix, x, y, 200f).color(grey, grey, grey, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
}
