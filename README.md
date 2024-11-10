Installation:
```
No dependencies required.
Simply place the .jar in the plugins folder.
```
Directory tree:
```
plugins/
│   ├── ExtraEnchants/
│   │   └── anvilcombinations/  # Directory for anvil combinations
│   │   └── enchantments/       # Directory for enchantments
│   │       └── PUNCH.json
│   │       └── ...
│   ├── ExtraEnchants.jar
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