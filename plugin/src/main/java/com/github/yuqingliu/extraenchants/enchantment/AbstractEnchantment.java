package com.github.yuqingliu.extraenchants.enchantment;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class AbstractEnchantment {
    private Component comma = Component.text(", ", NamedTextColor.BLUE);
    protected final Component name;
    protected final int maxLevel;
    protected final Component description;
    protected final List<Material> applicable;
    protected final List<Component> applicableDisplayNames;
    protected final String requiredLevelFormula;
    protected final String costFormula;

    public Component getDescription() {
        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
        .match(replace)
        .replacement((matchResult, builder) -> {
            String numberStr = matchResult.group(1); // Get the captured number as a string
            String percentage = matchResult.groupCount() > 1 ? matchResult.group(2) : ""; // Get the percentage symbol if it exists
            return Component.text(numberStr + percentage, NamedTextColor.YELLOW); // Replace with multiplied value, preserving style
        })
        .build();

        Component finalComponent = description.replaceText(replacementConfig);
        return finalComponent;
    }
    
    public Component getLeveledDescription(int level) {
        Pattern remove = Pattern.compile("per level");
        Component leveledDescription = description.replaceText(builder -> builder.match(remove).replacement(Component.empty()));

        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
            .match(replace)
            .replacement((matchResult, builder) -> {
                String numberStr = matchResult.group(1); // Get the captured number as a string
                boolean hasPercent = numberStr.endsWith("%");
                if (hasPercent) {
                    numberStr = numberStr.substring(0, numberStr.length() - 1); // Remove the "%" sign
                }
                int number = Integer.parseInt(numberStr); // Convert the string to an integer
                int multipliedValue = number * level; // Multiply by the level
                String result = String.valueOf(multipliedValue);
                if (hasPercent) {
                    result += "%"; // Append "%" sign back if it was present
                }
                return Component.text(result, NamedTextColor.YELLOW); // Replace with multiplied value, preserving style
            })
            .build();

        Component finalComponent = leveledDescription.replaceText(replacementConfig);

        return finalComponent;
    }

        private int getComponentSize(Component component) {
        return component.children().size();
    }

    public List<Component> addOrUpdateEnchantmentFromSection(List<Component> lore, Component enchant, Component eLevel) {
        int maxComponents = 7;
        boolean enchantmentFound = false;

        if (lore.isEmpty()) {
            Component currentLine = Component.empty().append(enchant).append(eLevel);
            lore.add(currentLine);
            return lore;
        } else {
            // Scan components to find if the enchantment exists; if it exists, update its level
            for (int i = 0; i < lore.size(); i++) {
                Component line = lore.get(i);
                List<Component> newChildren = new ArrayList<>();
                boolean found = false;

                for (Component child : line.children()) {
                    if (child.equals(enchant)) {
                        found = true;
                        newChildren.add(enchant);
                        newChildren.add(eLevel);
                    } else if (!found) {
                        newChildren.add(child);
                    }
                }

                if (found) {
                    Component newLine = Component.empty();
                    for (Component child : newChildren) {
                        newLine = newLine.append(child);
                    }
                    lore.set(i, newLine);
                    enchantmentFound = true;
                    break; // Exit the loop once the enchantment is found and updated
                }
            }

            if (!enchantmentFound) {
                // Last line has space, append the component at the end of it
                Component currentLine = lore.get(lore.size() - 1);
                if (getComponentSize(currentLine) < maxComponents) {
                    Component newLine = currentLine.append(Component.text(", ")).append(enchant).append(eLevel);
                    lore.set(lore.size() - 1, newLine);
                } else {
                    // Did not find component and last line is full, add a new line with the enchantment as its first element
                    Component newLine = Component.empty().append(enchant).append(eLevel);
                    lore.add(newLine);
                }
            }

            return lore;
        }
    }

    public List<Component> removeEnchantmentFromSection(List<Component> lore, Component enchant) {
        int maxComponents = 3;
        List<Component> newLore = new ArrayList<>();
        List<Component> filteredComponents = new ArrayList<>();

        // Flatten and filter the lore components in one go, removing all commas
        for (Component line : lore) {
            List<Component> children = line.children();
            for (int i = 0; i < children.size(); i++) {
                Component child = children.get(i);
                if (child.equals(enchant)) {
                    i++;
                } else if (!child.equals(comma) && !child.equals(Component.empty())) {
                    filteredComponents.add(child);
                }
            }
        }

        // Reconstruct the final lore from filtered components, ensuring max 8 components per line
        Component line = Component.empty();
        int componentCount = 0;
        for (int i = 1; i < filteredComponents.size(); i+=2) {
            Component component = filteredComponents.get(i - 1);
            Component level = filteredComponents.get(i);

            if (componentCount < maxComponents) {
                if (componentCount > 0) {
                    line = line.append(comma); // Append comma between components
                }
                line = line.append(component);
                line = line.append(level);
                componentCount++;
            } else {
                newLore.add(line);
                line = Component.empty().append(component);
                line = line.append(level);
                componentCount = 1;
            }
        }
        if (!line.equals(Component.empty())) {
            newLore.add(line);
        }

        return newLore;
    }

    public abstract int getEnchantmentLevel(ItemStack item);

    public abstract boolean canEnchant(ItemStack item);

    public abstract ItemStack applyEnchantment(ItemStack item, int level);

    public abstract ItemStack removeEnchantment(ItemStack item);

    public TextColor getNameColor() {
        return this.name.color();
    }

    public TextColor getDescriptionColor() {
        return this.description.color();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEnchantment that = (AbstractEnchantment) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
