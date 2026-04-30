---
description: "Reskillable Reimagined Wiki"
---

# Reskillable Reimagined

Reskillable is an RPG-style progression mod that introduces a **skill-based locking and leveling system** for Minecraft.  
Players must earn experience and level up skills in order to use powerful gear, defeat strong enemies, and access advanced content.

This guide covers the **entire system**, including skills, perks, configs, commands, and customization options.

---

## 🔥 Core Features

Reskillable adds:

- A full **skill leveling system**
- **Gear gating** by player skill level
- **Entity attack requirements** (mobs can require skill levels to damage)
- **Crafting requirements**
- A modular **perk system**
- Configurable **attribute bonuses**
- XP-scaling and level progression
- Full datapack-style JSON customization

---

## 🎯 Skill System

Each player has multiple skills they can level up using experience.

### Available Skills

| Skill | Purpose |
|------|--------|
| **Attack** | Controls ability to use weapons and defeat strong enemies |
| **Defense** | Controls armor usage and survivability |
| **Mining** | Controls use of mining tools |
| **Gathering** | Controls use of gathering tools like shovels & axes |
| **Farming** | Controls hoe/tool usage for crops |
| **Building** | Controls building-related actions (block reach, etc.) |
| **Agility** | Grants movement and mobility perks |
| **Magic** | Controls magical items & spell-based gear |

---

## 📈 Leveling & XP System

- Players spend XP to upgrade skills via the skills menu.
- XP cost scales dynamically using both:
  - Predefined XP scaling curves
  - A configurable multiplier

### XP Scaling
- XP requirements scale similarly to vanilla Minecraft levels.
- XP progression beyond level 50 uses a fallback scaling system.
- Fully configurable using:

```
xpScalingMultiplier
levelsPerHeart
healthPerHeart
maximumLevel
```

---

## 💥 Attribute Bonuses

Every **5 levels** in a skill grants permanent bonuses.

These bonuses are configurable via the config.

### Example Bonuses

| Skill | Bonus |
|------|-------|
| **Attack** | Extra attack damage |
| **Defense** | Extra armor + armor toughness |
| **Agility** | Movement speed |
| **Magic** | Custom attribute (configurable ID) |
| **Building** | Block reach distance |
| **Gathering** | Bonus XP gain |
| **Mining** | Mining speed |
| **Farming** | Crop growth chance |

All of these are adjustable via config.

---

## 🧩 Perk System

At certain levels, skills unlock **perks** that modify gameplay.

Examples:
- Increase movement speed  
- Bonus mining reach  
- Extra block break power  
- Faster gathering speed  
- Magical attribute boosts  

Perks can be toggled and updated dynamically.

---

## 🛠 Skill Locking System

Reskillable can restrict:

✅ Item usage  
✅ Entity attacks  
✅ Crafting recipes  

By skill level.

### 1. Item Skill Restrictions

Controlled by:
```
config/reskillable/skill_locks.json
```

Example:
```json
{
  "skillLocks": {
    "minecraft:diamond_sword": ["attack:15"],
    "minecraft:netherite_chestplate": ["defense:30"]
  }
}
```

Meaning:
- You need **Attack 15** to use a diamond sword.
- You need **Defense 30** for netherite armor.

---

### 2. Entity Attack Restrictions

You can restrict attacking mobs:

```
config/reskillable/attack_skill_locks.json
```

Example:
```json
{
  "attackSkillLocks": {
    "minecraft:warden": ["attack:25"]
  }
}
```

If your skill is too low:
- You do no damage
- Or reduced damage (depending on config)

---

### 3. Craft Skill Restrictions

Crafting can also be locked using the same system for ALL crafting:

```
config/reskillable/craft_skill_locks.json
```

This works for:
- **Vanilla crafting tables**
- **Furnaces & other vanilla workstations** (via item locking)
- **Farmer's Delight cooking pots** (if installed)
- **Any modded crafting recipe** (just use the item ID)

Example:
```json
{
  "craftSkillLocks": {
    "minecraft:diamond_pickaxe": ["mining:20"],
    "minecraft:netherite_sword": ["attack:25"],
    "minecraft:enchanting_table": ["magic:15"],
    "minecraft:brewing_stand": ["magic:10"],
    "farmersdelight:beef_stew": ["cooking:10"],
    "modid:custom_item": ["skill_name:level"]
  }
}
```

**How it works:**
- When a player tries to craft an item, Reskillable checks if it's in the lock list
- If the player's skill level is too low, the recipe result won't appear in the crafting output
- If they meet the requirement, crafting works normally

**Farmer's Delight Integration:**
- Lock Farmer's Delight cooking outputs using item-level restrictions
- Add items like `farmersdelight:beef_stew` to `craft_skill_locks.json` to restrict them the same way
- Players won't see the item as a result in their inventory until they meet the skill requirement
- To use: just add `"farmersdelight:beef_stew": ["cooking:10"]` to your craft_skill_locks.json

---

## ⚙ Config Options (Config File)

The main config controls mod behavior.

### Important Config Options

| Config | Description |
|--------|------------|
| `disableWoolDrops` | Disables wool drops unless shears are used |
| `deathSkillReset` | Reset skills on player death |
| `startingCost` | XP cost to reach level 2 |
| `maximumLevel` | Max level a skill can reach |
| `xpScalingMultiplier` | Global XP scaling multiplier |
| `enableSkillLeveling` | Toggle leveling entirely |
| `enableSkillUpMessage` | Shows skill-up chat messages |
| `levelsPerHeart` | Total levels required per extra heart |
| `healthPerHeart` | Amount of health per milestone |
| `magicAttribute` | Attribute ID used for magic skill |
| `attackDamageBonus` | Attack attribute scaling |
| `armorBonus` | Defense attribute scaling |
| `movementSpeedBonus` | Agility speed bonus |
| `blockReachBonus` | Building reach bonus |
| `miningSpeedMultiplier` | Mining efficiency scaling |
| `cropGrowthChancePer5Levels` | Farming growth bonus |
| `gatheringXpBonus` | Extra XP from gathering |

### Disabling Features in TOML

The config file is generated under `config/reskillable-server.toml`.
Set boolean values to `false` to turn features off, and use an empty list to clear list-based options.

Example:

```toml
disableWoolDrops = false
enableSkillLeveling = false
enableSkillUpMessage = false
showTabButtons = false
showSubpageTitles = false
disabledBaseSkills = ["attack", "magic"]
skillAliases = []
```

Common disable options:

- `disableWoolDrops` turns off the wool-drop restriction.
- `enableSkillLeveling` disables leveling through the GUI or skill selection.
- `enableSkillUpMessage` hides the skill-up chat message.
- `showTabButtons` hides the inventory tab buttons.
- `showSubpageTitles` hides the built-in and custom subpage titles.
- `disabledBaseSkills` removes the listed base skills from the mod.

**📝 Migration Note:** `enableSecondSkillPage` is now **deprecated** and has been removed. Pagination is now automatic based on content:
- Skills and perks are now **dynamically paginated** (8 items per page)
- Additional pages are created automatically when needed
- **If you have an existing config with `enableSecondSkillPage`, remove this line** — it will be ignored and is no longer necessary

---

## ❤️ Health Scaling

Reskillable allows players to gain bonus health:

- Every X total skill levels → gain a heart  
- Fully controlled via:
  - `levelsPerHeart`
  - `healthPerHeart`

This makes progression feel more RPG-like.

---

## 🧠 Magic Skill Integration

The **Magic** skill connects to other mods using attributes.

You can define a custom mod attribute:

```
magicAttribute = "modid:spell_power"
```

If missing or invalid, it defaults to `minecraft:generic.luck`.

---

## 📟 Commands

Reskillable provides powerful commands:

### `/skills`

Base command

### `/skills set`

Set a player's skill level manually.

### `/skills get`

Check a player's skill levels.

### `/skills reload`

Reloads all configs and JSON files live.
(Works in singleplayer only and only if your file is a certain size after it gets to big you will need to restart game.)

### `/skills scanmod <modid>`

Automatically scans a mod and adds all its items into your skill lock config. (Singleplayer only)
(1.21.1 adds all items/1.20.1 only adds weapons/tools/armor. Values added(if any)are merely placeholders
you should triple check anything this mod command adds(it does not overwrite existing.))

---

## 🔍 JSON Presets & Defaults

Reskillable automatically generates:

- `skill_locks.json`
- `attack_skill_locks.json`
- `craft_skill_locks.json`

It also includes default vanilla benchmarks for:
- Armor tiers
- Weapon power
- Tool progression

---

## 🔌 Modpack & Developer Tools

Reskillable is made for:

- Modpacks
- RPG progression systems
- Hardcore difficulty packs
- Servers with progression gates

It supports:
- Easy integration with other mods
- Custom attribute linking
- Datapack-style progression

---

## 🌟 Who Should Use Reskillable?

Perfect for:

✅ RPG modpacks  
✅ Progression-based servers  
✅ Hardcore survival worlds  
✅ Magic-focused packs  
✅ Adventure maps  

---

## ❓ FAQ

### Can I completely disable leveling?
Yes:
```
enableSkillLeveling = false
```
Skills then must be granted via commands or datapacks.

---

### Can I lock modded items?
Yes!  Just enter modid:itemid


---

### Can I use my custom magic attribute for perks?
Yes.  
Just set:
```
magicAttribute = "yourmod:attribute"
```

---

### Can players bypass skill locks?
Only via creative mode or commands.

---

## ✅ Final Notes

Reskillable is designed to be:
- Modular  
- Datapack-friendly  
- Highly configurable  
- Easy to expand  

Every aspect of player progression is fully customizable.

---

⚔️ Reskill your world.  
📈 Level with purpose.  
🔥 Earn your power.
