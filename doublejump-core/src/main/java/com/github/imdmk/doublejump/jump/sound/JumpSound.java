package com.github.imdmk.doublejump.jump.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public record JumpSound(Sound sound, float volume, float pitch) {

    public void play(Player player) {
        player.playSound(player, this.sound, this.volume, this.pitch);
    }
}
