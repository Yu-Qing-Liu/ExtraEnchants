package com.github.yuqingliu.extraenchants.api.logger;

import org.bukkit.entity.Player;
import net.kyori.adventure.text.format.NamedTextColor;

public interface Logger {
    void sendPlayerErrorMessage(Player player, String message);
    void sendPlayerWarningMessage(Player player, String message);
    void sendPlayerAcknowledgementMessage(Player player, String message);
    void sendPlayerNotificationMessage(Player player, String message);
    void sendPlayerMessage(Player player, String message, NamedTextColor color);
}
