package com.sebastian.sockets.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sebastian.sockets.blockentities.SocketBlockEntity;
import com.sebastian.sockets.blockentities.SocketPluggableEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class SocketRenderer implements BlockEntityRenderer<SocketBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public SocketRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(SocketBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if(blockEntity.isConnected()) {
            SocketPluggableEntity otherBE = blockEntity.getOtherBlockEntity();
            if(otherBE == null) return;
            Vec3 posA = new Vec3(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ());
            Vec3 posB = new Vec3(blockEntity.getDeviceX(), blockEntity.getDeviceY(), blockEntity.getDeviceZ()).add(otherBE.getConnectorPos(otherBE.getBlockState()));
            renderLeash(posA, posB, matrixStack, buffer, combinedLight);
        }
    }

    private void renderLeash(Vec3 vecA, Vec3 vecB, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {

        // Control point for Bezier curve to create the hanging effect
        Vec3 controlPoint = new Vec3((vecA.x + vecB.x) / 2, Math.min(vecA.y, vecB.y) - 1, (vecA.z + vecB.z) / 2);

        float r = 0.4f, g = 0.3f, b = 0.2f, a = 1.0f;

        matrixStack.pushPose();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.leash());

        int segments = 16;
        for (int i = 0; i <= segments; ++i) {
            float t = (float) i / segments;
            double x = bezierPoint(t, vecA.x, controlPoint.x, vecB.x);
            double y = bezierPoint(t, vecA.y, controlPoint.y, vecB.y);
            double z = bezierPoint(t, vecA.z, controlPoint.z, vecB.z);
            vertexConsumer.vertex(matrixStack.last().pose(), (float)x, (float)y, (float)z).color(r, g, b, a).uv2(combinedLight).endVertex();
        }

        matrixStack.popPose();
    }

    private double bezierPoint(float t, double start, double control, double end) {
        return (1 - t) * (1 - t) * start + 2 * (1 - t) * t * control + t * t * end;
    }
}
