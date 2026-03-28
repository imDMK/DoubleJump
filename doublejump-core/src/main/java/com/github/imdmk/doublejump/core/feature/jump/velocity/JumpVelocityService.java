package com.github.imdmk.doublejump.core.feature.jump.velocity;

import com.github.imdmk.doublejump.core.injector.ComponentPriority;
import com.github.imdmk.doublejump.core.injector.annotations.Service;
import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Service(priority = ComponentPriority.LOWEST)
final class JumpVelocityService {

    private static final double DEG_TO_RAD = Math.PI / 180.0;
    private static final double DIAGONAL_NORMALIZATION = 1.0 / Math.sqrt(2.0);

    public void applyVelocity(Player player, JumpVelocity velocity) {
        Vector direction = resolveMovementDirection(player)
                .multiply(velocity.horizontalBoost());

        Vector result = player.getVelocity();
        result.setX(direction.getX());
        result.setZ(direction.getZ());
        result.setY(velocity.verticalBoost());

        player.setVelocity(result);
    }

    private Vector resolveMovementDirection(Player player) {
        Input input = player.getCurrentInput();

        int forward = axis(input.isForward(), input.isBackward());
        int strafe = axis(input.isLeft(), input.isRight());

        if (forward == 0 && strafe == 0) {
            return new Vector(0, 0, 0);
        }

        Vector forwardVector = getForwardVector(player);
        Vector rightVector = getRightVector(forwardVector);

        Vector direction = forwardVector.clone().multiply(forward)
                .add(rightVector.clone().multiply(strafe));

        if (forward != 0 && strafe != 0) {
            direction.multiply(DIAGONAL_NORMALIZATION);
        }

        return direction;
    }

    private int axis(boolean positive, boolean negative) {
        return (positive ? 1 : 0) - (negative ? 1 : 0);
    }

    private Vector getForwardVector(Player player) {
        double yawRad = player.getLocation().getYaw() * DEG_TO_RAD;

        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);

        return new Vector(x, 0, z);
    }

    private Vector getRightVector(Vector forward) {
        return new Vector(forward.getZ(), 0, -forward.getX());
    }
}