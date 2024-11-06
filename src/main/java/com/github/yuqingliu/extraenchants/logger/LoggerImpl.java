package com.github.yuqingliu.extraenchants.logger;

import org.bukkit.entity.Player;

import com.github.yuqingliu.extraenchants.api.logger.Logger;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@NoArgsConstructor
public class LoggerImpl implements Logger {
    @Override
    public void sendPlayerErrorMessage(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.RED));
    } 
    
    @Override
    public void sendPlayerWarningMessage(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.YELLOW));
    }

    @Override
    public void sendPlayerAcknowledgementMessage(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.GREEN));
    }

    @Override
    public void sendPlayerNotificationMessage(Player player, String message) {
        player.sendMessage(Component.text(message, NamedTextColor.GOLD));
    }

    @Override
    public void sendPlayerMessage(Player player, String message, NamedTextColor color) {
        player.sendMessage(Component.text(message, color));
    }
}
