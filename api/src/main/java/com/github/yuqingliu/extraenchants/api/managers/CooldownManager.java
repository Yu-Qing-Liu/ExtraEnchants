package com.github.yuqingliu.extraenchants.api.managers;

import java.util.Map;
import java.util.UUID;

import com.github.yuqingliu.extraenchants.api.cooldown.Cooldown;

public interface CooldownManager {
    Map<UUID, Map<String, Cooldown>> getCooldowns();
    void addCooldown(UUID entity, Cooldown cooldown);
    Cooldown getCooldown(UUID entity, String id);
    Cooldown computeIfAbsent(UUID entity, Cooldown cooldown);
}
