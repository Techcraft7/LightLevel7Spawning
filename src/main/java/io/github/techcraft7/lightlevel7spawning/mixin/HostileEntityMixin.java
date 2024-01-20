package io.github.techcraft7.lightlevel7spawning.mixin;

import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {

    @SuppressWarnings("resource")
    @Inject(
        method = "isSpawnDark(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z",
        at = @At("HEAD"),
        cancellable = true)
    private static void isSpawnDark(ServerWorldAccess world, BlockPos pos, net.minecraft.util.math.random.Random random, CallbackInfoReturnable<Boolean> cir) {
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            cir.setReturnValue(false);
            return;
        }
        DimensionType dimensionType = world.getDimension();
        if (world.getLightLevel(LightType.BLOCK, pos) > 7) {
            cir.setReturnValue(false);
        } else {
            int j = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
            cir.setReturnValue(j <= dimensionType.monsterSpawnLightTest().get(random));
        }
    }
}
