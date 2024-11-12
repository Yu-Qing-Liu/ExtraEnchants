package com.github.yuqingliu.extraenchants.managers;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


import com.github.yuqingliu.extraenchants.api.cooldown.Cooldown;
import com.github.yuqingliu.extraenchants.api.managers.CooldownManager;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CooldownManagerImpl implements CooldownManager {
    private final Map<UUID, Map<String, Cooldown>> cooldowns = new ConcurrentHashMap<>();

    @Override
    public void addCooldown(UUID entity, Cooldown cooldown) {
        if(cooldowns.containsKey(entity)) {
            cooldowns.get(entity).put(cooldown.getId(), cooldown);
        } else {
            Map<String, Cooldown> map = new ConcurrentHashMap<>();
            map.put(cooldown.getId(), cooldown);
            cooldowns.put(entity, map);
        }
    }

    @Override
    public Cooldown getCooldown(UUID entity, String id) {
        if(cooldowns.containsKey(entity)) {
            if(cooldowns.get(entity).containsKey(id)) {
                return cooldowns.get(entity).get(id);
            }
        }
        return null;
    }

    @Override
    public Cooldown computeIfAbsent(UUID entity, Cooldown cooldown) {
        if(cooldowns.containsKey(entity)) {
            if(cooldowns.get(entity).containsKey(cooldown.getId())) {
                return cooldowns.get(entity).get(cooldown.getId());
            }
        }
        addCooldown(entity, cooldown);
        return cooldown;
    }
}
