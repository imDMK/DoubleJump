# DoubleJump

[![Release](https://jitpack.io/v/imDMK/DoubleJump.svg)](https://jitpack.io/#imDMK/DoubleJump)
[![Build Status](https://github.com/imDMK/DoubleJump/actions/workflows/maven.yml/badge.svg)](https://github.com/imDMK/DoubleJump/actions/workflows/maven.yml)
![JDK](https://img.shields.io/badge/JDK-1.17-blue.svg)
![Supported versions](https://img.shields.io/badge/Minecraft-1.17--1.20.1-green.svg)
[![SpigotMC](https://img.shields.io/badge/SpigotMC-yellow.svg)](https://www.spigotmc.org/resources/doublejump-1-17-1-20-1.110632/)
[![Bukkit](https://img.shields.io/badge/Bukkit-blue.svg)](https://dev.bukkit.org/projects/d-doublejump)

Simple and efficient double jump plugin with many features and configuration possibilities.

# Features
* Spawn particles when player use double jump, 
* Playing sound when player use double jump,
* Double jump streaks,
* Double jump item (You can completely configure this item),
* Possibility to customize messages (disable or type, e.g. ACTIONBAR or CHAT),
* Possibility to disable using double jump in selected worlds,
* Possibility to enable double jump mode when joining the server (U can set only for administrators),
* Ability to disable using double jump in selected game modes,
* Ability to set double jump series to reset after dying or touching the ground.
* Supports [Adventure](https://github.com/KyoriPowered/adventure) components.
* Easy to use API

# FAQ
#### **Q: What are the notification types?**
**A:** CHAT, ACTIONBAR, TITLE, SUBTITLE, DISABLED

#### **Q: An error occurred while configuring particles. How to fix it?**
**A:** Probably the error is because the particle type needs additional data. 
Currently, the plugin supports particle types that contain **no data** or only **DustOptions**. 
[Here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html) you have a list of all (The description indicates the data).

# API
To start using API you have to include doublejump-api jar to your libraries or use:
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

        if (message.equalsIgnoreCase("!testdoublejump")) {
            if (jumpPlayerManager.isDoubleJumpMode(player)) {
                return;
            }

            event.setCancelled(true);
            
            jumpPlayerManager.enable(player, true); //true to skip checking if player can use double jump

            player.sendMessage("Now u can test our double jump plugin!");
        }
    }
```
### Events
You can listen events which are called before action.
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
