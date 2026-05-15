package org.polyfrost.hytils.client.data.providers

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import org.intellij.lang.annotations.Language
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.HytilsRebornClient
import org.polyfrost.hytils.client.data.DataProvider
import org.polyfrost.oneconfig.api.ui.v1.Notifications
import org.polyfrost.oneconfig.utils.v1.JsonUtils
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object LanguageData : DataProvider {
    @Volatile private var fetchedPatterns: Map<String, String> = emptyMap()
    @Volatile private var fetchedComponents: Map<String, Component> = emptyMap()
    @Volatile private var fetchedStrings: Map<String, String> = emptyMap()

    val FORMATTING_CODES = Regex("(?i)§[0-9A-FK-OR]")
    val VALID_USERNAME = Regex("\\w{1,16}")

    override fun load() = load(showNotification = false)

    fun load(showNotification: Boolean) {
        val response: JsonElement? = JsonUtils.parseFromUrl("$apiBase/language.json")

        if (response == null || response.isJsonNull) {
            HytilsRebornClient.LOGGER.error("Failed to fetch language data, using defaults.")
            mc.execute {
                Notifications.enqueue(
                    Notifications.Type.Error,
                    HytilsRebornConstants.NAME,
                    "Failed to fetch language data, using defaults. Some features may not work as intended."
                )
            }
            return
        }

        try {
            val patterns = mutableMapOf<String, String>()
            response.asJsonObject?.getAsJsonObject("regexes")?.entrySet()?.forEach {
                if (it.value.isJsonObject) {
                    val prefix = it.value.asJsonObject.get("prefix")?.asString ?: ""
                    val suffix = it.value.asJsonObject.get("suffix")?.asString ?: ""
                    val array = it.value.asJsonObject.getAsJsonArray("patterns")
                    patterns[it.key] = prefix + array.joinToString("|") { e -> "(?:${e.asString})" } + suffix
                } else {
                    patterns[it.key] = it.value.asString
                }
            }
            fetchedPatterns = patterns

            val components = mutableMapOf<String, Component>()
            response.asJsonObject?.getAsJsonObject("components")?.entrySet()?.forEach {
                val result = ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, it.value)
                result.ifSuccess { pair ->
                    components[it.key] = pair.first
                }.ifError { error ->
                    HytilsRebornClient.LOGGER.error("Failed to parse component for ${it.key}: $error")
                }
            }
            fetchedComponents = components

            val strings = mutableMapOf<String, String>()
            response.asJsonObject?.getAsJsonObject("strings")?.entrySet()?.forEach {
                strings[it.key] = it.value.asString
            }
            fetchedStrings = strings

            HytilsRebornClient.LOGGER.info("Successfully fetched ${fetchedPatterns.size} regexes, ${fetchedComponents.size} components and ${fetchedStrings.size} strings.")
            if (showNotification) {
                mc.execute {
                    Notifications.enqueue(
                        Notifications.Type.Info,
                        HytilsRebornConstants.NAME,
                        "Successfully fetched ${fetchedPatterns.size} regexes, ${fetchedComponents.size} components and ${fetchedStrings.size} strings."
                    )
                }
            }
        } catch (e: Exception) {
            HytilsRebornClient.LOGGER.error("Failed to parse language data, using defaults.", e)
            mc.execute {
                Notifications.enqueue(
                    Notifications.Type.Error,
                    HytilsRebornConstants.NAME,
                    "Failed to parse language data, using defaults. Some features may not work as intended. Check logs for more details."
                )
            }
        }
    }

    //region default regexes
    val SKYBLOCK_TAB_PLAYER_ENTRY_NAME by regex("\\[\\d+] \\w{1,16}(?: .{1,3}|$)")
    val SKYBLOCK_TAB_INFO_ENTRY_NAME by regex("![A-D]-[a-v]")

    val FRIEND_REQUEST by regex("Friend request from (?<player>.+)\\n\\[ACCEPT] - \\[DENY] - \\[BLOCK].*")
    val GUILD_JOIN by regex("(?:\\[.*] )?(?<player>\\S{1,16}) joined the guild!")

    // https://regex101.com/r/QRhTgs
    val CHAT_ADVERTISEMENTS by regex(
        "(?i)/?(?:party join|join party|p join|party me|duel me|guild join|join guild|g join) \\w{1,16}", // commands with optional slash
        "/(?:visit|ah|duel \\w{1,16})", // commands with mandatory slash (i.e. "commonly" used words)
        "youtube\\.com|youtu\\.be|twitch\\.tv|ttv/|discord\\.gg", // socials
        "my ah|my smp", // "my" advertisements
        "lowballing|lowbaling|lowvaling|lowvaluing|lowballer" // skyblock lowballers
    )
    val CHAT_RANK_BEGGING by regex(
        "[^\\[](vip|mvp|mpv|vpi|\\+\\+)",
        "(please|pls|plz|rank ?up|rank ?upgrade)",
        "(buy|upgrade|gift|give) (rank|me)",
        "(gifting|gifters)",
        "( beg |begging|beggers)",
        prefix = "(?i)(?!.+: )(", suffix = ")"
    )
    val CHAT_BEDWARS_ADVERTISEMENT by regex("(?!.+: )(([1-8]/[1-8]|[1-8]v[1-8]|[2-8]s)|(any|rbw|ranked))")

    val KARMA_MESSAGES by regex("\\+(?<karma>\\d)+ Karma!")
    val LOBBY_JOIN by regex("(?:sled into|slid into|joined|spooked into) the lobby")
    val TICKET_ANNOUNCER by regex("(?<player>(?!You )\\w{1,16} )has found an? .+")
    val SOUL_WELL_FIND by regex(".+ has found .+ in the Soul Well!")
    val GAME_ANNOUNCEMENT by regex("➤ A (?:.+ game is available to join|game of .+ is (?:available to join|starting in .+ seconds))! CLICK HERE to join!")

    val RANK_GIFTING by regex("They have gifted \\d+ (?:rank|ranks) so far!")
    val SIMULATOR_MESSAGES by regex(
        "You found (?:an egg|a gift|a candy)! .\\d{1,3} total.",
        "\\W{0,3}\\w*\\S{0,3}\\s?\\w{1,16} has reached \\d{2,3} (?:gifts|eggs|candy)!"
    )
    val SERVER_CONNECTED by regex(
        "You are currently connected to server \\S+",
        "Sending you to \\S+!",
        "Sending you to \\S+",
        "Sending to server \\S+",
        "SERVER FOUND! Sending to \\S+!",
        "Warping you to your SkyBlock island\\.{3}",
        "Warping\\.{3}",
        "Sending a visit request\\.{3}",
        "Finding player\\.{3}",
        "Request join for (?:Hub|Dungeon Hub) (?:.{2,4} \\S+|\\S+)",
        "Found an in-progress .+ game! Teleporting you to \\S+",
        "Returning you to the lobby!",
        "Teleporting you to suspect"
    )
    val EARNED_COINS_AND_EXP by regex(
        "\\W\\d+ .* Experience.*",
        "\\W\\d+ Soul.*",
        "\\W\\d+ coins.*",
        "\\W\\d+ gold.*",
        ".*\\W\\d+ Event EXP.*",
        "\\W\\d+ tokens.*",
        "You earned \\d+ GEXP from playing.+!",
        ".+ just earned .+ as a Guild Level Reward!",
        "YOU GOT LUCKY!You will receive DOUBLE EXP this game!"
    )
    val REPLAY_RECORDED by regex("This game has been recorded\\. Click here to watch the Replay!")

    val TIP_MESSAGES by regex(
        // tipping messages
        "You (?:tipped|\\(anonymously\\) tipped) (?:\\d+ players? in \\d+ (?:game|different games)!|\\w{1,16} in [^!]+!|\\d+ players?!)",
        "You were tipped by \\d+ players? in the last \\S+!",

        // error messages
        "You already tipped everyone that has boosters active, so there isn't anybody to be tipped right now!",
        "You've already tipped that person today in [^!]+! Try another user!",
        "You've already tipped someone in the past hour in [^!]+! Wait a bit and try again!",
        "That player is not online, try another user!",
        "No one has a network booster active right now! Try again later\\.",
        "Slow down! You can only use /tip every few seconds\\.",

        // booster announcements
        "\\n?➲ (?:"
            + "\\w{1,16}(?: and (?:\\d+ others|1 other player))? activated .+? coins for this game!"
            + "|\\w{1,16}(?: and (?:\\d+ others|1 other player))? activated .+? coins for [^!]+"
            + "|Your game was boosted by (?:\\w{1,16}'s .+? coins Network Booster!|.+? coins Network Booster by \\w{1,16}(?: and (?:\\d+ others|1 other player))?!)"
            + "|Get free coins by clicking on this message"
        + ")(?: Gain XP and coins by » CLICKING HERE! «)?",
        "✘ \\w{1,16}'s Network Booster expired!",

        // store advertisements
        "(?:➲ )?(?:Buy Network Boosters|Network Boosters are available) at https?://store\\.hypixel\\.net/?"
    )
    val ONLINE_STATUS by regex("REMINDER: Your Online Status is currently set to (?:Appear Offline|Busy|Away)")
    val GAME_TIPS by regex(
        "If you get disconnected use /rejoin to join back in the game\\.",
        "You may use /mmreport <skin name> to chat report in this mode!",
        "Teaming with the .+ is not allowed!",
        "Teaming is not allowed.+",
        "Cross Teaming / Teaming with other teams is not allowed!",
        "Cross-teaming is not allowed! Report cross-teamers using /report\\.",
        "Cages opened! FIGHT!",
        "Queued! Use the bed to return to lobby!",
        "Queued! Use the bed to cancel!",
        "You can use /ic <message> to communicate with your fellow infected!",
        "To leave .+, type /lobby",
        "Consider sharing some of your resources with your team mates by clicking the Banker NPC at your base\\.",
        "You didn't pick up any more \\S+ because you have too much on you!",
        "As a Spectator, you can talk in chat with fellow Spectators\\.",
        "Contents of .+ Ender Chest have been dropped into their fountain\\.",
        "Alive players cannot see dead players' chat\\.",
        "The game has started!",
        "You have 15s to spread out before it starts!",
        "You will respawn next round!",
        "Jumping before dropping can sometimes give you an advantage!",
        "You can skip the level if you fail too many times on easy or medium difficulties!",
        "DROP!",
        "Reset location!",
        "Dropper is currently in the Prototype Lobby, please report bugs at https://hypixel\\.net/bugs!",
        "Atlas verdict submitted! Thank you :\\)",
        "You can activate bridge building by left clicking with wool in your hand!"
    )
    val GAME_STATS by regex("Click to view the stats of your .* game!")
    val LOBBY_FISHING_ANNOUNCEMENT by regex("(?<rank>\\[\\S+] )?(?<player>(?!You )\\w{1,16} )caught .+")
    val HOT_POTATO by regex("\\w{1,16} burnt to a crisp due to a hot potato!")
    val DUELS_NO_STATS_CHANGE by regex("Your stats did not change because you /duel'ed your opponent!|Your stats did not change because you dueled someone in your party!|No stats will be affected in this round!")

    val GAME_JOIN by regex("(?<player>\\w{1,16}) has joined (?<amount>.+)!")
    val GAME_LEAVE by regex("(?<player>\\w{1,16}) has quit!")
    val GAME_STARTING by regex(
        "The game starts in", "The game is starting in", "Cages open in:", "You will respawn in",
        "The Murderer gets their sword in", "You get your sword in", "The alpha infected will be chosen in",
        "Kill contracts will be issued in", "The Murderers get their swords in", "You can start shooting in", "The door opens in",
        // "⁎" prefix needed to ensure compat between game start compactor & game status restyler
        prefix = "(?:⁎ )?(?<title>", suffix = ") (?<time>\\d{1,3}) (?<unit>(seconds?!))(?: .\\d+.|)"
    )

    // game end regexes from AutoGG by Sk1er LLC
    val GAME_END by regex(
        " +1st Killer - ?\\[?\\w*\\+*]? \\w+ - \\d+(?: Kills?)?",
        " *1st (?:Place ?)?[-:]? ?\\[?\\w*\\+*]? \\w+(?: : \\d+| - \\d+(?: Points?)?| - \\d+(?: x .)?| \\(\\w+ .{1,6}\\) - \\d+ Kills?|: \\d+:\\d+| - \\d+ (?:Zombie )?(?:Kills?|Blocks? Destroyed)| - \\[LINK])?",
        " +Winn(?:er: None! Too many players alive!|er #1 \\(\\d+ Kills\\): \\w+ \\(\\w+\\)(?:, \\w+ \\(\\w+\\))*|er(?::| - )(?:Hiders|Seekers|Defenders|Attackers|PLAYERS?|MURDERERS?|Red|Blue|RED|BLU|\\w+)(?: Team)?|ers?: ?\\[?\\w*\\+*]? \\w+(?:, ?\\[?\\w*\\+*]? \\w+)?|ing Team ?[:-] (?:Animals|Hunters|Red|Green|Blue|Yellow|RED|BLU|Survivors|Vampires))",
        " +Alpha Infected: \\w+ \\(\\d+ infections?\\)",
        " +Murderer: \\w+ \\(\\d+ Kills?\\)",
        " +You survived \\d+ rounds!",
        " +(?:UHC|SkyWars|Bridge|Sumo|Classic|OP|MegaWalls|Bow|NoDebuff|Blitz|Combo|Bow Spleef|Boxing|Hypixel) (?:Duel|Doubles|Teams|Deathmatch|2v2v2v2|3v3v3v3|Parkour)? ?- \\d+:\\d+",
        " +They captured all wools!",
        " +Game over!",
        " +[\\d.]+k?/[\\d.]+k? \\w+",
        " +(?:Criminal|Cop)s won the game!",
        " +\\[?\\w*\\+*]? \\w+ - \\d+ Final Kills",
        " +Zombies - \\d*:?\\d+:\\d+ \\(Round \\d+\\)",
        " +. YOUR STATISTICS .",
        " {35,36}Winner(s?)",
        " {21}Bridge CTF [a-zA-Z]+ - \\d\\d:\\d\\d",
        " {32}Party Games",
        " +\\w+ won the game!",
        " +GAME OVER!",
        " +Player of the match: \\w+ \\(\\d+ Goals\\)",
        "\\s*?.+ WINNER! {2}.+",
        "\\s*?.+ Bridge [a-zA-Z0-9]* - .+",
        " {6}#1 (?:\\[.+] )?.{1,16} \\(\\d+:\\d+:\\d+\\)",
        " +(?:Survivors \\(\\d+\\):|Nobody survived!|Survivor:)"
    )
    val CASUAL_GAME_END by regex(
        "MINOR EVENT! .+ in .+ ended",
        "DRAGON EGG OVER! Earned [\\d,]+XP [\\d,]g clicking the egg \\d+ times",
        "GIANT CAKE! Event ended! Cake's gone!",
        "PIT EVENT ENDED: .+ \\[INFO]"
    )

    val PARTY_CHANNEL by regex("§9Party (?:§8|§9)> (.*)")
    val GUILD_CHANNEL by regex("§2Guild > (.*)")
    val FRIEND_CHANNEL by regex("Friend > (.*)")
    val OFFICER_CHANNEL by regex("§3Officer > (.*)")

    val NON_MESSAGE by regex("(?<player>.+)§7: (?<message>.*)")
    val PRIVATE_MESSAGE by regex("(?<type>To|From|Voicemail) (?:\\[.*] )?(?<player>.+): (?<message>.*)")

    val PLAYER_CONNECTION_STATUS by regex("(?<type>Friend|Guild) > (?<player>\\w{1,16}) (?<status>joined|left)\\.")

    val PARTY_JOIN by regex(
        "You have joined (?:\\[.+] )?.* party!",
        "Party Members(?:\\[.+] )?\\w{1,100}",
        "(?:\\[.+] )?\\w{1,100} joined the.* party.*",
        "Party Finder > .* joined the dungeon group! \\(\\w+ Level \\d+\\)"
    )
    val PARTY_LEAVE by regex(
        "You have been kicked from the party by (?:\\[.+] )?\\w{1,16}",
        "(?:\\[.+] )?\\w{1,16} has disbanded the party!",
        "You left the party\\.",
        "You are not in a party\\.",
        "The party was disbanded because (?:all invites expired and the party was empty|the party leader disconnected)\\."
    )
    val CHANNEL_SWAP by regex("You are now in the (?<channel>ALL|GUILD|OFFICER) channel")

    val ACHIEVEMENT_UNLOCKED by regex("a>> {3}Achievement Unlocked: (?<achievement>.+) {3}<<a")
    val LEVEL_UP by regex("You are now Hypixel Level (?<level>\\d+)!")

    val GG_MESSAGES by regex(
        "❤", "gg", "GG", "gf", "Good Game", "Good Fight", "Good Round! :D", "Have a good day!", "<3",
        "AutoGG By Sk1er!", "AutoGG By Hytils Reborn!", "Good Round", ":D", "Well Played!", "wp",
        prefix = "(?:.* )?(?:\\[.+] )?\\w{1,16}(?: .+)?: (?:", suffix = ")"
    )
    val GL_MESSAGES by regex("(?i)(?!.+: )(gl|glhf|good luck|have a good game|autogl by sk1er)")

    val GAME_BOSSBAR_ADVERTISEMENT by regex("§e§lPlaying §f§l.+ §e§lon §\\S§lMC\\.HYPIXEL\\.NET")
    //endregion

    //region default components
    val HEADER_ADVERTISEMENT by component(
        Component.literal("You are playing on ").withStyle(ChatFormatting.AQUA)
            .append(Component.literal("MC.HYPIXEL.NET").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD))
    )
    val FOOTER_ADVERTISEMENT by component(
        Component.literal("Ranks, Boosters & MORE! ").withStyle(ChatFormatting.GREEN)
            .append(Component.literal("STORE.HYPIXEL.NET").withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
    )
    //endregion

    //region default strings
    val HYPE_LIMIT by string("  ➤ You have reached your Hype limit!")
    val BRIDGE_OWN_GOAL by string("You just jumped through your own goal, enjoy the void death! :)")
    val CURSE_OF_SPAM by string("KALI HAS STRIKEN YOU WITH THE CURSE OF SPAM")
    val DUELS_BLOCK_TRAIL by string("Your block trail aura is disabled in this mode!")
    val SKYBLOCK_WELCOME by string("Welcome to Hypixel SkyBlock!")
    val DISCORD_SAFETY_WARNING by string("Please be mindful of Discord links in chat as they may pose a security risk")
    val ALREADY_IN_CHANNEL by string("You're already in this channel!")
    val PARTY_CONFIRM_WARP by string("Some players are still in-game, run the command again to confirm warp!")

    val WATCHDOG_ANNOUNCEMENT by string("[WATCHDOG ANNOUNCEMENT]")
    val WATCHDOG_BAN by string("A player has been removed from your")

    val CANNOT_SHOUT_BEFORE_SKYWARS by string("You can't shout until the game has started!")
    val CANNOT_SHOUT_BEFORE_GAME by string("You can't use /shout before the game has started.")
    val CANNOT_SHOUT_AFTER_GAME by string("You can't use /shout after the game has finished.")
    val NO_SPECTATOR_COMMANDS by string("You are not allowed to use commands as a spectator!")

    val GAME_START_CANCELLED by string("We don't have enough players! Start cancelled.")
    val GAME_START_DELAYED by string("We don't have enough players! Start delayed.")
    //endregion

    fun String.removeFormattingCodes(): String {
        return this.replace(FORMATTING_CODES, "")
    }

    private fun regex(
        @Language("RegExp") vararg defaultPatterns: String,
        prefix: String = "",
        suffix: String = ""
    ) = object : ReadOnlyProperty<Any?, Regex> {
        private var cachedRegex: Regex? = null
        private var cachedPattern: String? = null

        override fun getValue(thisRef: Any?, property: KProperty<*>): Regex {
            // use patterns fetched from the api, otherwise use the default pattern
            val corePattern = if (defaultPatterns.size == 1) defaultPatterns.first() else defaultPatterns.joinToString("|") { "(?:$it)" }
            val fallback = prefix + corePattern + suffix
            val pattern = fetchedPatterns[property.name] ?: fallback

            if (cachedPattern != pattern) {
                cachedPattern = pattern
                cachedRegex = runCatching { Regex(pattern) }.getOrElse { e ->
                    HytilsRebornClient.LOGGER.error(
                        "Failed to compile regex for ${property.name}, falling back to default.",
                        e
                    )
                    Regex(fallback)
                }
            }

            return cachedRegex!!
        }
    }

    private fun component(default: Component) = ReadOnlyProperty<Any?, Component> { _, property ->
        fetchedComponents[property.name] ?: default
    }

    private fun string(default: String) = ReadOnlyProperty<Any?, String> { _, property ->
        fetchedStrings[property.name] ?: default
    }
}
