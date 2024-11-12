Installation:
```
No dependencies required.
Simply place the .jar in the plugins folder.
```
Directory tree:
```
plugins/
│   ├── ExtraEnchants/
│   │   ├── anvilcombinations/  # Directory for anvil combinations
│   │   │   └── [Allium].json  
│   │   │   └── ...  
│   │   ├── enchantments/       # Directory for enchantments
│   │   │   └── PUNCH.json
│   │   │   └── ...
│   │   └── customblocks/       # Directory for custom ui blocks
│   │       └── BLOCKS.json
│   ├── ExtraEnchants.jar       # Plugin binary
│   ├── config.yml              # Config file
```

Example of config file:
```
EnableEtableGUIEverywhere: false        # Set to true if you want to set the etable UI to all etable blocks
EnableAnvilGUIEverywhere: false         # Set to true if you want to set the anvil UI to all anvil blocks
EnableGrindstoneGUIEverywhere: false    # Set to true if you want to set the grindstone UI to all grindstone blocks
AnvilRepairCostPerMaterial: 1.5         # Anvil repair cost per material
AnvilLevelUpgradeCost: 2.0              # Anvil level upgrade cost per enchantment level
```

Commands:
```
- /wand etable; /wand anvil; /wand grindstone

Mark blocks/entities which can view GUI, Right Click to mark.
Only works on anvils, etables and grindstone blocks as well as anything that is an entity (Villagers, ...).
```
```
- /enchant <EnchantID> <Level>

Enchant main hand item
```
```
- /level set <EnchantID> <MaxLevel>

Set an enchantment's maximum level
```
```
- /list 

List all available enchantments
```
```
- /anvil add

Add item in main hand and item in off hand as valid combinable items in the anvil.
```
```
- /enchantable <EnchantID>

Make item in main hand enchantable by EnchantID
```

Example of an enchantment .json file:
```
{
  "enchantID": "POWER",
  "name": "{\"color\":\"blue\",\"text\":\"Power\"}",
  "description": "{\"color\":\"dark_gray\",\"text\":\"Increases arrow damage.\"}",
  "cooldown": null,
  "maxLevel": 5,
  "applicable": [
    {
      "material": "CROSSBOW",
      "displayName": "[Crossbow]"
    }
  ],
  "conflicting": [],
  "requiredLevelFormula": "x^2",
  "costFormula": "x",
  "leveledColors": [
    "#ccccff",
    "#9c9cff",
    "#8484ff",
    "#6c6cff",
    "#5555ff"
  ]
}
```
Example of a anvil combination .json file
```
{
  "item": {
    "material": "PAPER",
    "displayName": "[Paper]"
  },
  "combinable": [
    {
      "material": "BOW",
      "displayName": "[Bow]"
    }   
  ]
}
```
