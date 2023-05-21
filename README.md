# DoubleJump

[![Release](https://jitpack.io/v/imDMK/DoubleJump.svg)](https://jitpack.io/#imDMK/DoubleJump)
[![Build Status](https://github.com/imDMK/DoubleJump/actions/workflows/maven.yml/badge.svg)](https://github.com/imDMK/DoubleJump/actions/workflows/maven.yml)
![Supported versions](https://img.shields.io/badge/Minecraft-1.17--1.19.2-green.svg)
![JDK](https://img.shields.io/badge/JDK-1.17-blue.svg)

A simple double jump plugin with a lot of configuration options.

# Features
* Spawn particles when player use double jump, 
* Playing sound when player use double jump,
* Double jump streaks,
* Possibility to customize messages (also type, e.g. ACTIONBAR or CHAT),
* Possibility to disable using double jump in selected worlds,
* Possibility to enable double jump mode when joining the server (U can set only for administrators),
* Ability to disable using double jump in selected game modes,
* Ability to set double jump series to reset after dying or touching the ground.
* Easy to use API

# FAQ
#### **Q: What are the notification types?**
**A:** CHAT, ACTIONBAR, TITLE, SUBTITLE

#### **Q: An error occurred while configuring particles. How to fix it?**
**A:** Propably the error is because the particle type needs additional data. 
Currently, the plugin supports particle types that contain **no data** or only **DustOptions**. 
[Here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html) you have a list of all (The description indicates the data).

# API
To start using API you have to include DoubleJump jar to your libraries or use Maven.
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
    <groupId>com.github.imDMK</groupId>
    <artifactId>DoubleJump</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
### Usage
You can access the API using:
```java
DoubleJump.getApi();
```
Example:
```java
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        DoubleJumpApi doubleJumpApi = DoubleJump.getApi();

        if (message.equalsIgnoreCase("!testdoublejump")) {
            if (doubleJumpApi.isDoubleJumpMode(player)) {
                return;
            }

            event.setCancelled(true);

            doubleJumpApi.enable(player);
            player.sendMessage("Now u can test our double jump plugin!");
        }
    }
```
### Events
You can listen events which are called before action.
Example usage:
```java
    @EventHandler
    public void onPlayerDoubleJump(PlayerDoubleJumpEvent event) {
        Player player = event.getPlayer();

        System.out.println(player.getName() + " used double jump.");
    }
    
    @EventHandler
    public void onPlayerJumpStreakReset(PlayerJumpStreakResetEvent event) {
        Player player = event.getPlayer();
        
        if (player.isOp()) {
            event.setCancelled(true);
            return;
        }
        
        player.teleport(
                new Location(player.getWorld(), 100, 100, 100)
        );
    }
```
