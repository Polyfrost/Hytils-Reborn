# Hytils Reborn

[![Modrinth downloads](https://img.shields.io/modrinth/dt/nF6YaBfO?style=flat&logo=modrinth&label=Modrinth&color=1bd96a)](https://modrinth.com/mod/hytils)
[![CurseForge downloads](https://img.shields.io/curseforge/dt/1147244?style=flat&logo=curseforge&label=CurseForge&color=f16436)](https://www.curseforge.com/minecraft/mc-mods/hytils)

Hytils Reborn is a mod for the [Hypixel](https://hypixel.net) server. It removes and restyles Hypixel chat messages, hides parts of the tab list and HUD that carry no information, and sends common chat messages and commands for you. It is of no use on other servers.

Hytils Reborn started as a fork of [Hytilities](https://github.com/Sk1erLLC/Hytilities) by Sk1er LLC.

## Features

Most options are off until you turn them on. These are on by default:

- Auto GG and tab completion for `/play`.
- Restyled join, leave and countdown messages, white private messages and colored friend and guild statuses.
- The shout cooldown warning.
- No ping in the tab list during games, a shorter SkyBlock tab list and no store adverts in the tab list or bossbar.
- No bossbar in lobbies.
- Hardcore hearts, the Pit lag reducer and the mining fatigue notification.
- A ding for private messages in Limbo.

### Automatic messages

- Auto GG sends `gg` to all chat one second after a game ends. You can change the message and the delay, and add a second message. Casual Auto GG also covers events that give no Karma, such as SkyBlock and Pit events.
- Auto GL sends `glhf` when the game announces that it starts in 5 seconds.
- Auto Queue sends `/play` for the mode you just played after the game ends. It waits for you to press a key or click first, and you can add a delay of up to 10 seconds.
- Auto Friend accepts friend requests.
- Auto Party Warp Confirm confirms a party warp for you.
- Auto Reply When AFK answers private messages after you have given no input for 5 minutes. You set the time between 1 and 60 minutes and write the reply.
- AutoWB greets friends and guild members when they join. You can write up to 10 messages and have the mod pick one at random.
- Guild options welcome new guild members, and announce your achievements and level ups in guild chat.
- Thank Watchdog sends "Thanks Watchdog!" after a Watchdog announcement or ban.
- Notify When Kicked From Game tells your party to requeue when a connection issue kicks you from a game.
- Party Chat Swapper switches to party chat when you join a party and back to all, guild or officer chat when you leave. If you use [Chatting](https://modrinth.com/mod/chatting), it can switch the chat tab too.
- After a win, Hytils Reborn can show your daily or weekly guild experience and your winstreak in BedWars, SkyWars or Duels.

### Chat appearance

- Game Status Restyle shortens join and leave messages to a green `+` or red `-`, the player count and the name. It restyles the start countdown the same way.
- Compact Game Start Announcements replaces the previous countdown message when a new one arrives.
- White Chat and White Private Messages show messages from players without a rank, and private messages, in white.
- Colored Friend/Guild Statuses colors the "joined" and "left" in friend and guild status messages.
- Short Channel Names turns `Guild`, `Party` and `Friend` into `G`, `P` and `F`. A second option shortens `To` and `From` on private messages.
- Replace Chat Emotes removes Hypixel chat emotes, strips their colors, or shows their shortcut such as `:wizard:`.
- Shout Cooldown blocks `/shout` while it is on cooldown and tells you how many seconds are left. Non Speech Cooldown does the same for the 3 second chat cooldown on accounts without a rank.

### Chat filters

Each of these messages has its own switch. All are off by default.

- `gg` and `gl` messages from other players.
- Player adverts, rank begging and BedWars party requests.
- Lobby join messages, friend and guild join and leave messages, and the guild message of the day.
- Karma, tip, earned coins and experience, and gifted rank messages.
- Game announcements, game tips, stats links and replay links.
- Soul Well announcements, main lobby fishing announcements and Hot Potato messages.
- "You are currently connected to server" messages and SkyBlock welcome messages.
- Automatically activated quests, Hype limit reminders and online status reminders.
- Curse of Spam, Bridge own goal deaths, Duels "stats did not change" notes and block trail notes.
- Seasonal Simulator collection messages and the Discord link safety warning.

### Tab list

- Hide ping during games, where the value is misleading.
- In SkyBlock, hide the head and ping on tab entries that are not players.
- Remove the store adverts from the tab list header and footer.
- Hide player ranks and guild tags in lobbies.
- Put a star to the left or right of your own name.

### HUD and titles

- Hide HUD Elements hides the health, hunger, armor and air bars in lobbies and in games where they never change.
- Hide the bossbar in lobbies, and bossbars in games that only advertise Hypixel.
- Hardcore Hearts switches your hearts to the hardcore style once your bed or wither is gone in BedWars and Mini Walls.
- Hide game start titles, game end titles and countdown titles.
- Hide the action bar in Dropper and Housing.

### Games

- Height Overlay darkens wool and terracotta at the build limit in BedWars and The Bridge. You can set the strength, include the lowest build layer and pick your own color for each of the 16 block colors. It works with Sodium.
- Hide Armor hides armor that is the same for every player, such as leather armor in BedWars and The Bridge.
- Highlight Opened Chests draws a colored box on each chest you have opened in SkyWars, Blitz SG and UHC. A chest refill clears the boxes.
- Notify When Blocks Run Out plays a sound when the stack in your hand drops to 10 blocks or fewer. You set the number between 4 and 20 and choose one of 9 sounds.
- Notify Mining Fatigue shows a notification, a title or the elder guardian effect when you get mining fatigue.
- Middle Waypoint Beacon draws a beacon beam at the center of the map once your wither dies in Mini Walls.
- Lower Render Distance in Sumo sets your render distance to between 2 and 5 chunks during Sumo Duels and restores it afterwards.
- Pit Lag Reducer stops drawing the players and mobs at spawn while you are down in the fighting area of The Pit.
- Hide cosmetics in Arcade games and Duels.
- Hide nametags such as "RIGHT CLICK" and "CLICK" in SkyBlock, BedWars, SkyWars and The Bridge.
- Remove entities that are not NPCs in SkyBlock.

### Lobbies and Limbo

- Hide lobby NPCs and "CLICK TO PLAY" nametags.
- Silent Lobby mutes every sound in a lobby except interface sounds. If you only want some sounds gone, 18 separate switches mute sounds such as footsteps, note blocks, fireworks and doors.
- Limbo Limiter caps the game at 30 FPS after 5 seconds in Limbo and at 10 FPS after 10 minutes.
- Limbo PM Ding plays a ding when you get a private message in Limbo.
- Auto Start joins Hypixel as soon as the game has loaded.

### Commands

- `/hytils` opens the settings.
- `/play <game>` gains tab completion and accepts short names for game modes.
- `/requeue` or `/rq` joins a new game of the mode you are in or last played.
- `/housingvisit <player>` or `/hvisit` goes to the Housing lobby and visits that player's house.
- `/skyblockvisit <player>` or `/sbvisit` joins SkyBlock and visits that player's island.
- `/silentremove add <player>` or `/sremove` removes that player from your friends list the next time they log off. `remove`, `list` and `clear` manage the queue.
- `/hytils gexp [player] [daily|weekly]` shows guild experience.
- `/hytils winstreak <player> <game>` shows a winstreak.

## Hypixel only

Hytils Reborn is built for Hypixel. The chat, HUD and game features check that you are on Hypixel before they run.

## Supported versions

- Minecraft 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11, 26.1, 26.2 and 26.3 on Fabric.

Hytils Reborn needs [OneConfig](https://modrinth.com/mod/oneconfig), Fabric API and Fabric Language Kotlin. It has no 1.8.9 build.

## Support

- Report bugs and ask questions on the [Polyfrost Discord](https://polyfrost.org/discord).
- Check [Polyfrost Status](https://status.polyfrost.org/) if a Polyfrost service is down.

## Building

```sh
./gradlew ":26.3:build"
```

Replace `26.3` with any supported Minecraft version. The build needs JDK 21 and JDK 25 installed.
