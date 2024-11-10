package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.time.Duration;
import java.util.HashSet;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Immolate extends CustomEnchantment {
    private int count = 0;
    private Particle.DustOptions beam = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 0.5f);

    public Immolate(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.GROWTH,
            Component.text("Immolate", nameColor),
            Component.text("Burns your target. Caster takes a quarter of the damage dealt", descriptionColor),
            2,
            itemRepository.getItems().get(ItemCategory.MELEE),
            new HashSet<>(),
            "x^2",
            "x"
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        int level = this.getEnchantmentLevel(item);
        if (level > 0) {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getEntity();
                int duration = 3 * level;
                if (target.hasMetadata("Immolate")) {
                    return;
                }
                target.setMetadata("Immolate", new FixedMetadataValue(plugin, true));
                applyEffect(player, target, duration);
            }
        }
    }

    private void applyEffect(Player player, LivingEntity target, int duration) {
        BukkitTask particleTask = Scheduler.runTimer(task -> {
            target.getWorld().spawnParticle(Particle.DUST, target.getLocation(), 1000, 0.25, 0.75, 0.25, 0, beam);
        }, Duration.ofMillis(50), Duration.ZERO);
        Scheduler.runTimer(task -> {
            player.damage(0.25);
            target.damage(1.0, player);
            count++;
            if(count >= duration || target.isDead()) {
                target.removeMetadata("Immolate", plugin);
                task.cancel();
                particleTask.cancel();
                count = 0;
            }
        }, Duration.ofSeconds(1), Duration.ZERO);
    }
}
