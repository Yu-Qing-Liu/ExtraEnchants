package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Thunder extends CustomEnchantment {
    private Map<UUID, Map<UUID, int[]>> playerEntityHits = new ConcurrentHashMap<>();
    private final int hitInterval = 3;

    public Thunder(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository,
            ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
                managerRepository, enchantmentRepository,
                EnchantID.THUNDER,
                Component.text("Thunder", nameColor),
                Component.text(
                        "Strike target with lightning every %d consecutive hits. Increases lignthing damage per level.",
                        descriptionColor),
                5,
                itemRepository.getItems().get(ItemCategory.MELEE),
                new HashSet<>(),
                "x^2",
                "x");
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity target = event.getEntity();
            ItemStack weapon = player.getInventory().getItemInMainHand();

            int level = this.getEnchantmentLevel(weapon);
            if (level > 0) {
                if (canStrike(player, target)) {
                    strikeLightning(getStandingBlock(target));
                    target.setMetadata("Thunder", new FixedMetadataValue(plugin, level));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.LIGHTNING) {
            Entity target = event.getEntity();
            if (target.hasMetadata("Thunder")) {
                List<MetadataValue> metadata = target.getMetadata("Thunder");
                if (!metadata.isEmpty()) {
                    int level = metadata.get(0).asInt();
                    event.setDamage(2 * level);
                    target.removeMetadata("Thunder", plugin);
                }
            }
        }
    }

    private boolean canStrike(Player player, Entity target) {
        if (!playerEntityHits.containsKey(player.getUniqueId())) {
            Map<UUID, int[]> hits = new HashMap<>();
            hits.put(target.getUniqueId(), new int[] { hitInterval - 1 });
            playerEntityHits.put(player.getUniqueId(), hits);
            return true;
        }
        if (!playerEntityHits.get(player.getUniqueId()).containsKey(target.getUniqueId())) {
            playerEntityHits.get(player.getUniqueId()).put(target.getUniqueId(), new int[] { hitInterval - 1 });
            return true;
        }
        if (playerEntityHits.get(player.getUniqueId()).get(target.getUniqueId())[0] > 0) {
            playerEntityHits.get(player.getUniqueId()).get(target.getUniqueId())[0]--;
            return false;
        }
        playerEntityHits.get(player.getUniqueId()).get(target.getUniqueId())[0] = hitInterval - 1;
        return true;
    }

    private void strikeLightning(Block block) {
        World world = block.getWorld();
        if (world != null) {
            world.strikeLightning(block.getLocation());
            Scheduler.runLater(task -> {
                Block ignition = block.getRelative(BlockFace.UP);
                Block c1 = ignition.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST);
                Block c2 = ignition.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST);
                Block c3 = ignition.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST);
                Block c4 = ignition.getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST);
                for (BlockFace face : BlockFace.values()) {
                    if (face == BlockFace.UP || face == BlockFace.DOWN) {
                        continue;
                    }
                    checkAndExtinguishConnectedFire(ignition.getRelative(face));
                }
                checkAndExtinguishConnectedFire(c1);
                checkAndExtinguishConnectedFire(c2);
                checkAndExtinguishConnectedFire(c3);
                checkAndExtinguishConnectedFire(c4);
            }, Duration.ofMillis(100));
        }
    }

    private void checkAndExtinguishConnectedFire(Block block) {
        if (block.getType() != Material.FIRE) {
            return;
        }
        block.setType(Material.AIR);
        for (BlockFace face : BlockFace.values()) {
            if (face == BlockFace.SELF) {
                continue;
            }
            Block neighbor = block.getRelative(face);
            if (neighbor.getType() == Material.FIRE) {
                checkAndExtinguishConnectedFire(neighbor);
            }
        }
    }

    private Block getStandingBlock(Entity entity) {
        if (entity == null) {
            return null;
        }
        Vector direction = new Vector(0, -1, 0);
        RayTraceResult result = entity.getWorld().rayTraceBlocks(entity.getLocation(), direction,
                entity.getWorld().getMaxHeight());
        if (result != null && result.getHitBlock() != null) {
            return result.getHitBlock();
        }
        return null;
    }
}
