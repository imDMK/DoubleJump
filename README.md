# DoubleJump


[![Build Status](https://github.com/imDMK/DoubleJump/actions/workflows/gradle.yml/badge.svg)](https://github.com/imDMK/DoubleJump/actions/workflows/gradle.yml)
![JDK](https://img.shields.io/badge/JDK-1.17-blue.svg)
![Supported versions](https://img.shields.io/badge/Minecraft-1.17--1.20.1-green.svg)
[![SpigotMC](https://img.shields.io/badge/SpigotMC-yellow.svg)](https://www.spigotmc.org/resources/doublejump-1-17-1-20-1.110632/)
[![Bukkit](https://img.shields.io/badge/Bukkit-blue.svg)](https://dev.bukkit.org/projects/d-doublejump)
[![PaperMC](https://img.shields.io/badge/Paper-004ee9.svg)](https://hangar.papermc.io/imDMK/DoubleJump)
[![bStats](https://img.shields.io/badge/bStats-00695c)](https://bstats.org/plugin/bukkit/Double-Jump/19387)

Efficient double jump plugin with many features and configuration possibilities.

# Features
* Spawn particles when a player uses double jump,
* Playing sound when a player uses double jump,
* Double jump streaks,
* Double jump item (You can completely configure this item),
* Possibility to customize messages (disable or type, e.g. ACTIONBAR or CHAT),
* Possibility to disable using double jump in selected worlds,
* Possibility to enable or disable double jump mode for the selected player.
* Possibility to enable double jump mode when joining the server (U can set it only for administrators),
* Ability to disable using double jump in selected game modes,
* Ability to set double jump series to reset after dying or touching the ground.
* [WorldGuard](https://github.com/EngineHub/WorldGuard) support - Possibility to disable using double jump in selected regions
* [Adventure](https://github.com/KyoriPowered/adventure) components support.
* [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) support.
* Easy to use API.

# FAQ
#### **Q: What are the available placeholders?**
**A:** Available placeholders:
* `jump-player-delay` - Displays the remaining delay time until the next double jump can be used.
* `jump-player-is-delay` - Returns "yes" or "no" depending on whether the player has a jump delay.
* `jump-player-regeneration-delay` - Shows the remaining time to regenerate one jump in human-readable.
* `jump-player-has-jumps` - Returns "yes" or "no" depending on whether the player has an unused double jump available.
* `jump-player-jumps-limit` - Returns the jump limit value for the player.
* `jump-player-jumps` - Returns the total value of all double jumps performed.
* `jump-player-streak` - Returns the player's current double jump streak.

**ATTENTION:** All placeholders will return null if the player does not have an active double jump (placeholder will not be applied).
  
#### **Q: What are the notification types?**
**A:** CHAT, ACTIONBAR, TITLE, SUBTITLE, DISABLED

#### **Q: An error occurred while configuring particles. How to fix it?**
**A:** Probably the error is because the particle type needs additional data. 
Currently, the plugin supports particle types that contain **no data** or only **DustOptions**. 
[Here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html) you have a list of all (The description indicates the data).

# API
To start using API you have to include a double-jump-API jar to your libraries or use:
### Maven
```xml
  <repositories>
    <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>com.github.imDMK.DoubleJump</groupId>
      <artifactId>doublejump-api</artifactId>
      <version>VERSION</version>
    </dependency>
  </dependencies>
```
### Gradle
```groovy
  repositories {
    maven { url 'https://jitpack.io' }
  }

  dependencies {
    implementation 'com.github.imDMK.DoubleJump:doublejump-api:VERSION'
  }
```
### Usage
You can access the API using:
```java
DoubleJumpApiProvider.get();
```
Example:
```java
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        DoubleJumpApi doubleJumpApi = DoubleJumpApiProvider.get();

        JumpPlayerManager jumpPlayerManager = doubleJumpApi.getJumpPlayerManager();
        JumpPlayerService jumpPlayerService = doubleJumpApi.getJumpPlayerService();

        if (message.equalsIgnoreCase("!testdoublejump")) {
            if (this.jumpPlayerManager.isDoubleJumpMode(player)) {
                return;
            }

            event.setCancelled(true);
            
            this.jumpPlayerService.enable(player, true); //true to skip checking if player can use double jump

            player.sendMessage("Now u can test our double jump plugin!");
        }
    }
```
### Events
You can listen to events that are called before action.
Example usage:
```java
    @EventHandler
    public void onPlayerDoubleJump(DoubleJumpEvent event) {
        Player player = event.getPlayer();

        System.out.println(player.getName() + " used double jump.");
    }
    
    @EventHandler
    public void onJumpStreakReset(JumpStreakResetEvent event) {
        Player player = event.getPlayer();
        
        if (player.isOp()) {
            event.setCancelled(true);
            return;
        }
        
        if (event.getResetReason() == JumpStreakResetReason.PLAYER_ON_GROUND) {
            player.teleport(
                new Location(player.getWorld(), 100, 100, 100)
            );
        }
    }
```
# Information
If you have any suggestions or found a bug, please report it using [this](https://github.com/imDMK/DoubleJump/issues/new/choose) site.
