package org.polyfrost.hytils.client

import net.minecraft.world.level.material.MapColor
import org.polyfrost.hytils.HytilsRebornConstants
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.*
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.polyui.color.argb
import org.polyfrost.polyui.color.rgba

object HytilsRebornConfig : Config(
    "${HytilsRebornConstants.ID}.json",
    "/assets/hytils/hypixel.png",
    HytilsRebornConstants.NAME,
    Category.HYPIXEL
) {
    @JvmStatic
    @Switch(
        title = "Enable Hytils Reborn",
        description = "Master switch to enable/disable the mod",
        category = "General"
    )
    var isEnabled = true

    //region Automatic
    @Switch(
        title = "Auto Start",
        description = "Join Hypixel immediately once the client has loaded to the main menu.",
        category = "General",
        subcategory = "Automatic"
    )
    var autoStart = false

    @Switch(
        title = "Auto-Complete Play Commands",
        description = "Allows tab completion of /play commands.",
        category = "General",
        subcategory = "Automatic"
    )
    var autocompletePlayCommands = true

    @Info(
        title = "Warning",
//        icon = "polyui/warning.svg",
        description = "Auto Queue will require you to interact with the game in a way to prevent abuse.",
        category = "General",
        subcategory = "Automatic"
    )
    @Suppress("unused")
    private val autoQueueInfo = null

    @Switch(
        title = "Auto Queue",
        description = "Automatically queues for another game once you win or die.",
        category = "General",
        subcategory = "Automatic"
    )
    var autoQueue = false

    @Slider(
        title = "Auto Queue Delay",
        description = "Delays how long it takes to automatically queue.\nMeasured in seconds.",
        category = "General",
        subcategory = "Automatic",
        min = 0f,
        max = 10f
    )
    var autoQueueDelay = 0f

    @Switch(
        title = "Automatically Check GEXP",
        description = "Automatically check your GEXP after you win a Hypixel game.",
        category = "General",
        subcategory = "Automatic"
    )
    var autoGetGEXP = false

    @RadioButton(
        title = "GEXP Mode",
        category = "General",
        subcategory = "Automatic",
        description = "Choose which GEXP to get.",
        options = ["Daily", "Weekly"]
    )
    var gexpMode = 0

    @Switch(
        title = "Automatically Check Winstreak",
        description = "Automatically check your winstreak after you win a Hypixel game.",
        category = "General",
        subcategory = "Automatic"
    )
    var autoGetWinstreak = false

    @Switch(
        title = "Notify Mining Fatigue",
        description = "Send a notification when you get mining fatigue.",
        category = "General",
        subcategory = "Potion Effects"
    )
    var notifyMiningFatigue = true

    @RadioButton(
        title = "Mining Fatigue Notification Type",
        description = "Choose how notifications should be displayed.",
        category = "General",
        subcategory = "Potion Effects",
        options = ["Notification", "Title", "Elder Guardian Curse"]
    )
    var miningFatigueNotificationType = 0

    @Checkbox(
        title = "Disable Mining Fatigue Notification in SkyBlock",
        description = "Disable the mining fatigue notification in SkyBlock.",
        category = "General",
        subcategory = "Potion Effects"
    )
    var disableNotifyMiningFatigueSkyblock = true
    //endregion

    //region Maintenance
    @Button(
        title = "Refetch Language Data",
        description = "Refetches all language data from the API.",
        category = "General",
        subcategory = "Maintenance"
    )
    @Suppress("unused")
    private fun refetchLanguageData() {
        Multithreading.submit { LanguageData.load(true) }
    }

    @Switch(
        title = "Cancelled Message Debugger",
        description = "Strikethrough received messages that would have been cancelled",
        category = "General",
        subcategory = "Maintenance"
    )
    var cancelledMessageDebugger = false
    //endregion

    //region Chat
    @Switch(
        title = "Auto GG",
        description = "Send a \"gg\" message at the end of a game.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoGG = true

    @Switch(
        title = "Auto GG Second Message",
        description = "Send a secondary message that will be sent after the first GG message.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoGGSendSecondMessage = false

    @Switch(
        title = "Casual Auto GG",
        description = "Send a \"gg\" message at the end of minigames/events that don't give out Karma, such as SkyBlock and The Pit events.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var casualAutoGG = false

    @Text(
        title = "Auto GG First Message",
        description = "Choose what message is said on game completion.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoGGMessage = "gg"

    @Slider(
        title = "Auto GG First Message Delay",
        description = "Delay after the game ends to say the first message in seconds.",
        category = "Chat",
        subcategory = "Automatic",
        min = 0f,
        max = 5f
    )
    var autoGGFirstMsgDelay = 1f

    @Text(
        title = "Auto GG Second Message",
        description = "Choose the secondary message that will be sent.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoGGSecondMessage = "Have a good day!"

    @Slider(
        title = "Auto GG Second Message Delay",
        description = "Delay after the game ends to say the second message in seconds.",
        category = "Chat",
        subcategory = "Automatic",
        min = 0f,
        max = 5f
    )
    var autoGGSecondMsgDelay = 1f

    @Switch(
        title = "Anti GG",
        description = "Remove GG messages from chat.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var antiGG = false

    @Switch(
        title = "Auto GL",
        description = "Send a message 5 seconds before a Hypixel game starts.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoGL = false

    @Text(
        title = "Auto GL Message",
        description = "Choose what message is said.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoGLMessage = "glhf"

    @Switch(
        title = "Anti GL",
        description = "Remove all GL messages from chat.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var antiGL = false

    @Switch(
        title = "Auto Friend",
        description = "Automatically accept friend requests.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoFriend = false

    @Switch(
        title = "Auto Party Warp Confirm",
        description = "Automatically confirms party warps.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoPartyWarpConfirm = false

    @Switch(
        title = "Auto Reply When AFK",
        description = "Automatically sends a reply to anyone who PMs you while you are AFK.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var autoReplyAfk = false

    @Slider(
        title = "AFK Timeout",
        description = "How long you need to be inactive before being considered AFK in minutes.",
        category = "Chat",
        subcategory = "Automatic",
        min = 1f,
        max = 60f
    )
    var afkTimeout = 5f

    @Text(
        title = "AFK Reply Message",
        description = "Choose what message is sent when someone PMs you while you are AFK. \"%player%\" will be replaced with the player's name.",
        category = "Chat",
        subcategory = "Automatic"
    )
    var afkReplyMessage = "Hey %player%, I am currently AFK!"

    @Switch(
        title = "Game Status Restyle",
        description = "Replace common game status messages with a new style.\nExamples:\n+ Steve (1/12)\n- Steve\n⁎ Game starts in 5 seconds.",
        category = "Chat",
        subcategory = "Restyler"
    )
    var gameStatusRestyle = true

    @Switch(
        title = "Player Count Before Player Name",
        description = "Put the player count before the player name in game join/leave messages.\nExample: + (1/12) Steve",
        category = "Chat",
        subcategory = "Restyler"
    )
    var playerCountBeforePlayerName = true

    @Switch(
        title = "Player Count on Player Leave",
        description = "Include the player count when players leave.\nExample: - Steve (1/12)",
        category = "Chat",
        subcategory = "Restyler"
    )
    var playerCountOnPlayerLeave = true

    @Switch(
        title = "Player Count Padding",
        description = "Place zeros at the beginning of the player count to align with the max player count.\nExample: + Steve (001/100)",
        category = "Chat",
        subcategory = "Restyler"
    )
    var padPlayerCount = false

    // TODO: implement https://modrinth.com/mod/better-hypixel-chat (gplv3)
//    @Switch(
//        title = "Trim Line Separators",
//        description = "Prevent separators from overflowing onto the next chat line.",
//        category = "Chat",
//        subcategory = "Visual"
//    )
//    var lineBreakerTrim = true
//
//    @Switch(
//        title = "Clean Line Separators",
//        description = "Change all line separator to become smoother.",
//        category = "Chat",
//        subcategory = "Visual"
//    )
//    var cleanLineSeparator = true

    @Switch(
        title = "White Chat",
        description = "Make nons' chat messages appear as the normal chat message color.",
        category = "Chat",
        subcategory = "Visual"
    )
    var whiteChat = false

    @Switch(
        title = "White Private Messages",
        description = "Make private messages appear as the normal chat message color.",
        category = "Chat",
        subcategory = "Visual"
    )
    var whitePrivateMessages = true

    @Switch(
        title = "Colored Friend/Guild Statuses",
        description = "Colors the join/leave status of friends and guild members.",
        category = "Chat",
        subcategory = "Visual"
    )
    var coloredStatuses = true

    @Switch(
        title = "Compact Game Start Announcements",
        description = "Compacts game start/counting announcements.\nExample: The game starts in 20 seconds!",
        category = "Chat",
        subcategory = "Visual"
    )
    var compactGameStartAnnouncements = true

    @Switch(
        title = "Short Channel Names",
        description = "Abbreviate chat channel names.\nExample: Guild -> G, Party -> P, Friend -> F",
        category = "Chat",
        subcategory = "Visual"
    )
    var shortChannelNames = false

    @Switch(
        title = "Short Private Message Channel Names",
        description = "Abbreviate private message channel names.\nExample: To and From -> PM",
        category = "Chat",
        subcategory = "Visual"
    )
    var shortPMChannelNames = false

    @Switch(
        title = "Replace Chat Emotes",
        description = "Replace chat emotes.\nExample: ( ﾟ◡ﾟ)/",
        category = "Chat",
        subcategory = "Visual"
    )
    var replaceChatEmotes = false

    @RadioButton(
        title = "Chat Emotes Replacement Mode",
        description = "Choose how chat emotes are replaced.\nRemove Emote will completely remove emotes from messages.\nRemove Formatting will remove colors/formatting from emotes.\nReplace With Shortcuts will replace emotes with their shortcuts. Example: ( ﾟ◡ﾟ)/ -> o/",
        category = "Chat",
        subcategory = "Visual",
        options = ["Remove Emote", "Remove Formatting", "Replace With Shortcuts"],
    )
    var chatEmotesReplacementMode = 0

    @Switch(
        title = "Party Chat Swapper",
        description = "Automatically change to and out of a party channel when joining/leaving a party.",
        category = "Chat",
        subcategory = "Parties"
    )
    var chatSwapper = false

    @Dropdown(
        title = "Chat Swapper Channel",
        description = "The channel to return to when leaving a party.",
        category = "Chat",
        subcategory = "Parties",
        options = ["ALL", "GUILD", "OFFICER"]
    )
    var chatSwapperReturnChannel = 0

    // TODO: waiting for chatting port
//    @Switch(
//        title = "Swap Chatting Tab With Chat Swapper",
//        description = "Automatically switch your Chatting chat tab when Chat Swapper swaps your chat channel.",
//        category = "Chat",
//        subcategory = "Parties"
//    )
//    var chattingIntegration = false

    @Checkbox(
        title = "Remove All Chat Message",
        description = "Hide the \"You are now in the ALL channel\" message when auto-switching.",
        category = "Chat",
        subcategory = "Parties"
    )
    var chatSwapperHideAllChannelMsg = false

    @Switch(
        title = "Notify When Kicked From Game",
        description = "Notify in party chat when you are kicked from the game due to a connection issue.",
        category = "Chat",
        subcategory = "Parties"
    )
    var notifyWhenKick = false

    @Checkbox(
        title = "Put Notify Message In Capital Letters",
        description = "Put the message in capital messages instead of proper formatting.",
        category = "Chat",
        subcategory = "Parties"
    )
    var notifyWhenKickInCaps = false

    @Switch(
        title = "Broadcast Achievements",
        description = "Announce in Guild chat when you get an achievement.",
        category = "Chat",
        subcategory = "Guild"
    )
    var broadcastAchievements = false

    @Switch(
        title = "Broadcast Level Up",
        description = "Announce in Guild chat when you level up.",
        category = "Chat",
        subcategory = "Guild"
    )
    var broadcastLevelUp = false

    @Switch(
        title = "Guild Welcome Message",
        description = "Send a friendly welcome message when a player joins your guild.\nExample: Welcome to the guild Steve!",
        category = "Chat",
        subcategory = "Guild"
    )
    var guildWelcomeMessage = false

    @Switch(
        title = "Thank Watchdog",
        description = "Compliment Watchdog when someone is banned, or a Watchdog announcement is sent.\nExample: Thanks Watchdog!",
        category = "Chat",
        subcategory = "Watchdog"
    )
    var thankWatchdog = false

    @Switch(
        title = "Non Speech Cooldown",
        description = "Show the amount of time remaining until you can speak if you are a non.\nExample: Your freedom of speech is on cooldown. Please wait 3 more seconds.",
        category = "Chat",
        subcategory = "Cooldown"
    )
    var preventNonCooldown = false

    @Switch(
        title = "Shout Cooldown",
        description = "Show the amount of time remaining until /shout can be reused.\nExample: Shout command is on cooldown. Please wait 30 more seconds.",
        category = "Chat",
        subcategory = "Cooldown"
    )
    var preventShoutingOnCooldown = true

    @Switch(
        title = "Remove Karma Messages",
        description = "Remove Karma messages from the chat.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var hideKarmaMessages = false

    @Switch(
        title = "Remove Lobby Join Messages",
        description = "Remove lobby join messages from chat.\nExample: [MVP+] Steve joined the lobby!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeLobbyJoin = false

    @Switch(
        title = "Remove Ticket Machine Rewards",
        description = "Remove ticket machine messages from chat and only show your own.\nExample: Steve has found a COMMON Figurine",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeTicketMachineAnnouncements = false

    @Switch(
        title = "Remove Soul Well Announcements",
        description = "Remove soul well announcements from chat.\nExample: [MVP+] Steve has found a Bulldozer Perk I (Insane) in the Soul Well!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeSoulWellAnnouncements = false

    @Switch(
        title = "Remove Game Announcements",
        description = "Remove game announcements from chat.\nExample: A Mega Skywars game is available to join! CLICK HERE to join!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeGameAnnouncements = false

    @Switch(
        title = "Remove Hype Limit Reminder",
        description = "Remove Hype limit reminders from chat.\nExample: You have reached your Hype limit...",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeHypeLimitReminder = false

    @Switch(
        title = "Player AdBlocker",
        description = "Remove spam messages from players, usually advertising something or begging for ranks.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removePlayerAds = false

    @Switch(
        title = "Remove BedWars Advertisements",
        description = "Remove player messages asking to join BedWars parties.\nExample: [MVP+] Steve: Join BedWars 2/4 party!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removePlayerBedwarsAds = false

    @Switch(
        title = "Remove Friend/Guild Statuses",
        description = "Remove join/quit messages from friend/guild members.\nExample: Friend > Steve joined.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeConnectionStatus = false

    @Switch(
        title = "Remove Guild MOTD",
        description = "Remove the guild Message Of The Day.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeGuildMotd = false

    @Switch(
        title = "Remove Server Connected Messages",
        description = "Remove messages informing you of the lobby name you've just joined, or what lobby you're being sent to.\nExample: You are currently connected to server mini104H.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removedServerConnectedMsgs = false

    @Switch(
        title = "Remove Game Tips Messages",
        description = "Remove tips about the game you are playing.\nExample: Teaming is not allowed on Solo mode!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeGameTips = false

    @Switch(
        title = "Remove Auto Activated Quest Messages",
        description = "Remove automatically activated quest messages.\nExample: Automatically activated: Daily Quest: Duels Winner",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeAutoQuests = false

    @Switch(
        title = "Remove Stats Messages",
        description = "Remove the \"view your stats\" messages.\nExample: Click to view the stats of your SkyWars game!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeViewStats = false

    @Switch(
        title = "Remove Curse of Spam Messages",
        description = "Hides the constant spam of Kali's curse of spam.\nExample: KALI HAS STRIKEN YOU WITH THE CURSE OF SPAM",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeCurseOfSpam = false

    @Switch(
        title = "Remove Bridge Self Goal Death Messages",
        description = "Hides the death message when you jump into your own goal in Bridge.\nExample: You just jumped through your own goal, enjoy the void death! :)",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeBridgeOwnGoalDeathMsg = false

    @Switch(
        title = "Remove Duels No Stats Change Messages",
        description = "Hides the message explaining that your stats did not change for dueling through /duel or within in a party.\nExamples:\nYour stats did not change because you /duel'ed your opponent!\nYour stats did not change because you dueled someone in your party!\nNo stats will be affected in this round!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeDuelsNoStatsChange = false

    @Switch(
        title = "Remove Block Trail Disabled Messages",
        description = "Hides the message explaining that your duel's block trail cosmetic was disabled in specific gamemodes.\nExample: Your block trail aura is disabled in this mode!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeDuelsBlockTrailDisabled = false

    @Switch(
        title = "Remove SkyBlock Welcome Messages",
        description = "Removes \"Welcome to Hypixel SkyBlock!\" messages from chat.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeSkyblockWelcome = false

    @Switch(
        title = "Remove Gift Messages",
        description = "Removes \"They have gifted x ranks so far!\" messages from chat.",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeGiftedRanksAmount = false

    @Switch(
        title = "Remove Seasonal Simulator Collection Messages",
        description = "Removes personal and global collected messages from chat for the Easter, Christmas, and Halloween variants.\nExamples:\nYou found a gift! (5 total)\n[MVP+] Steve has reached 20 gifts!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeSimulatorCollectedMsgs = false

    @Switch(
        title = "Remove Earned Coins and Experience Messages",
        description = "Removes the earned coins and experience messages from chat.\nExamples:\n+25 Bed Wars Experience\n+10 coins!\nYou earned 500 GEXP from playing SkyBlock!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeEarnedCoinsAndExp = false

    @Switch(
        title = "Remove Replay Messages",
        description = "Removes replay messages from chat.\nExample: This game has been recorded. Click here to watch the Replay!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeReplayMessage = false

    @Switch(
        title = "Remove Tip Messages",
        description = "Removes tip messages from chat.\nExample: You tipped 5 players in 10 different games!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeTipMessages = false

    @Switch(
        title = "Remove Online Status Messages",
        description = "Removes the online status messages from chat.\nExample: REMINDER: Your Online Status is currently set to Appear Offline",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeOnlineStatus = false

    @Switch(
        title = "Remove Main Lobby Fishing Announcements",
        description = "Removes Main Lobby Fishing announcements from chat when a player catches a special fish.\nExample: [MVP+] Steve caught Nemo! Maybe he's lost again?",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeLobbyFishingMsgs = false

    @Switch(
        title = "Remove Hot Potato Messages",
        description = "Removes Hot Potato messages from chat.\nExample: Steve burnt to a crisp due to a hot potato!",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeHotPotato = false

    @Switch(
        title = "Remove Discord Safety Warning Messages",
        description = "Removes \"Please be mindful of Discord links in chat as they may pose a security risk\"",
        category = "Chat",
        subcategory = "Toggles"
    )
    var removeDiscordSafetyWarning = false

    // AutoWB
    @Switch(
        title = "AutoWB",
        description = "Says configurable message to your friends/guild when they join.",
        category = "Chat",
        subcategory = "AutoWB"
    )
    var autoWB = false

    @Checkbox(
        title = "Guild AutoWB",
        category = "Chat",
        subcategory = "AutoWB"
    )
    var guildAutoWB = true

    @Checkbox(
        title = "Friend AutoWB",
        category = "Chat",
        subcategory = "AutoWB"
    )
    var friendsAutoWB = true

    @Slider(
        title = "AutoWB Delay",
        category = "Chat",
        subcategory = "AutoWB",
        min = 2f,
        max = 10f
    )
    var autoWBCooldown = 2f

    @Text(
        title = "AutoWB Message",
        description = "Choose what message is said when a friend/guild member joins. \"%player%\" will be replaced with the player's name.",
        category = "Chat",
        subcategory = "AutoWB"
    )
    var autoWBMessage1 = "Welcome Back!"

    @Switch(
        title = "Random AutoWB Messages",
        category = "Chat",
        subcategory = "AutoWB"
    )
    var randomAutoWB = false

    @Text(title = "First Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage2 = "Welcome back... General %player%"

    @Text(title = "Second Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage3 = "WB!"

    @Text(title = "Third Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage4 = "Greetings! %player%"

    @Text(title = "Fourth Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage5 = "Thanks for coming back to hell >:)"

    @Text(title = "Fifth Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage6 = "Its nice having you here today %player%"

    @Text(title = "Sixth Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage7 = "Yooooooooo Mr. %player%"

    @Text(title = "Seventh Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage8 = "Welcome back Padawan %player%"

    @Text(title = "Eighth Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage9 = "Welcome Back! <3"

    @Text(title = "Ninth Random Message", category = "Chat", subcategory = "AutoWB")
    var autoWBMessage10 = "Thanks for coming to my TED talk."

    val wbMessages: List<String>
        get() = listOf(autoWBMessage1, autoWBMessage2, autoWBMessage3, autoWBMessage4, autoWBMessage5, autoWBMessage6, autoWBMessage7, autoWBMessage8, autoWBMessage9, autoWBMessage10)
    //endregion

    //region Tab
    @Dropdown(
        title = "Highlight Self in Tab",
        description = "Add a star to your name in tab.",
        category = "Tab", subcategory = "Highlighters",
        options = ["Off", "Left of Name", "Right of Name"]
    )
    var highlightSelfInTab = 0

    @Switch(
        title = "Hide Guild Tags in Tab",
        description = "Prevent Guild tags from showing up in tab.",
        category = "Tab", subcategory = "Toggles"
    )
    var hideGuildTagsInTab = false

    @Switch(
        title = "Hide Player Ranks in Tab",
        description = "Prevent player ranks from showing up in tab.",
        category = "Tab", subcategory = "Toggles"
    )
    var hidePlayerRanksInTab = false

    @Switch(
        title = "Hide Ping in Tab",
        description = "Prevent ping from showing up in tab while playing games, since the value is misleading. Ping will remain visible in lobbies.",
        category = "Tab", subcategory = "Toggles"
    )
    var hidePingInTab = true

    @Switch(
        title = "Cleaner Tab in SkyBlock",
        description = "Doesn't render player heads or ping for tab entries that aren't players in SkyBlock.",
        category = "Tab", subcategory = "Toggles"
    )
    var cleanerSkyblockTabInfo = true

    @Switch(
        title = "Hide Advertisements in Tab",
        description = "Prevent Hypixel's advertisements from showing up in tab.\nExample: Ranks, Boosters & MORE! STORE.HYPIXEL.NET",
        category = "Tab", subcategory = "Toggles"
    )
    var hideAdsInTab = true
    //endregion

    //region Game
    @Switch(
        title = "Hide HUD Elements",
        description = "Hide HUD elements such as health, hunger, and armor bars where they are the same.",
        category = "Game",
        subcategory = "GUI"
    )
    var hideHudElements = false

    @Switch(
        title = "Hide Advertisements in Bossbars",
        description = "Hide bossbars that advertise Hypixel.\nExample: Playing SKYWARS on MC.HYPIXEL.NET",
        category = "Game",
        subcategory = "GUI"
    )
    var hideGameAdsBossbar = true

    @Switch(
        title = "Hardcore Hearts",
        description = "When your bed is broken/wither is killed in Bedwars/MiniWalls, set the heart style to Hardcore.",
        category = "Game",
        subcategory = "GUI"
    )
    var hardcoreHearts = true

    @Switch(
        title = "Hide Game Starting Titles",
        description = "Hide titles such as gamemode names.\nExample: INSANE MODE",
        category = "Game",
        subcategory = "GUI"
    )
    var hideGameStartingTitles = false

    @Switch(
        title = "Hide Game Ending Titles",
        description = "Hide titles that signify when the game has ended.\nExamples:\nVICTORY!\nGAME OVER!",
        category = "Game",
        subcategory = "GUI"
    )
    var hideGameEndingTitles = false

    @Switch(
        title = "Hide Countdown Titles",
        description = "Hide countdowns that signify when a game is about to begin/end.",
        category = "Game",
        subcategory = "GUI"
    )
    var hideCountdownTitles = false

    @Switch(
        title = "Hide Armor",
        description = "Hide armor in games where armor is always the same.",
        category = "Game",
        subcategory = "Entities"
    )
    var hideArmor = false

    @Switch(
        title = "Hide Useless Game Nametags",
        description = "Hides unnecessary nametags such as those that say \"RIGHT CLICK\" or \"CLICK\" in SkyBlock, BedWars, SkyWars, and Duels, as well as other useless ones.",
        category = "Game",
        subcategory = "Entities"
    )
    var hideUselessArmorStandsGame = false

    @Switch(
        title = "Notify When Blocks Run Out",
        description = "Pings you via a sound when your blocks are running out.",
        category = "Game",
        subcategory = "Sound"
    )
    var blockNotify = false

    @Slider(
        title = "Block Number",
        description = "Modify the number of blocks you (don't?) have for the Notify When Blocks Run Out feature to work.",
        category = "Game",
        subcategory = "Sound",
        min = 4f,
        max = 20f
    )
    var blockNumber = 10f

    @Dropdown(
        title = "Block Notify Sound",
        description = "Choose what sound to play.",
        category = "Game",
        subcategory = "Sound",
        options = ["Hypixel Ding", "Golem Hit", "Blaze Hit", "Anvil Land", "Horse Death", "Ghast Scream", "Guardian Floop", "Cat Meow", "Dog Bark"]
    )
    var blockNotifySound = 0

    @Switch(
        title = "Middle Waypoint Beacon in MiniWalls",
        description = "Adds a beacon at (0,0) when your MiniWither is dead in MiniWalls.",
        category = "Game",
        subcategory = "Arcade"
    )
    var miniWallsMiddleBeacon = false

    @Color(title = "MiniWalls Beacon Color", category = "Game", subcategory = "Arcade")
    var miniWallsMiddleBeaconColor = rgba(255, 0, 0)

    @Switch(
        title = "Hide Arcade Cosmetics",
        description = "Hide Arcade Cosmetics in Hypixel.",
        category = "Game",
        subcategory = "Arcade"
    )
    var hideArcadeCosmetics = false

    @Info(
        title = "Warning",
//        icon = "polyui/warning.svg",
        description = "Height Overlay and its settings will automatically reload chunks when changed.",
        category = "Game",
        subcategory = "Height Overlay"
    )
    @Suppress("unused")
    private val heightOverlayInfo = null

    @Switch(
        title = "Height Overlay",
        description = "Make blocks that are at the height limit a different color in BedWars and The Bridge.",
        category = "Game",
        subcategory = "Height Overlay"
    )
    var heightOverlay = false

    @Switch(
        title = "Height Overlay Minimum Build Limit",
        description = "Enable the height overlay for blocks that are at the minimum build limit.",
        category = "Game",
        subcategory = "Height Overlay"
    )
    var heightOverlayMinBuild = false

    @Slider(
        title = "Height Overlay Tint Multiplier",
        description = "Adjust the tint multiplier.",
        category = "Game",
        subcategory = "Height Overlay",
        min = 1f,
        max = 1000f
    )
    var overlayAmount = 500f

    @Accordion(
        title = "Height Overlay Custom Colors",
        description = "Customize the colors of the height overlay.",
        category = "Game",
        subcategory = "Height Overlay"
    )
    object HeightOverlayCustomColors {
        @Include
        var enabled = false

        @Color(title = "Red")
        var red = argb(MapColor.COLOR_RED.col)

        @Color(title = "Orange")
        var orange = argb(MapColor.COLOR_ORANGE.col)

        @Color(title = "Yellow")
        var yellow = argb(MapColor.COLOR_YELLOW.col)

        @Color(title = "Lime")
        var lime = argb(MapColor.COLOR_LIGHT_GREEN.col)

        @Color(title = "Green")
        var green = argb(MapColor.COLOR_GREEN.col)

        @Color(title = "Cyan")
        var cyan = argb(MapColor.COLOR_CYAN.col)

        @Color(title = "Light Blue")
        var lightBlue = argb(MapColor.COLOR_LIGHT_BLUE.col)

        @Color(title = "Blue")
        var blue = argb(MapColor.COLOR_BLUE.col)

        @Color(title = "Purple")
        var purple = argb(MapColor.COLOR_PURPLE.col)

        @Color(title = "Magenta")
        var magenta = argb(MapColor.COLOR_MAGENTA.col)

        @Color(title = "Pink")
        var pink = argb(MapColor.COLOR_PINK.col)

        @Color(title = "Brown")
        var brown = argb(MapColor.COLOR_BROWN.col)

        @Color(title = "Gray")
        var gray = argb(MapColor.COLOR_GRAY.col)

        @Color(title = "Light Gray")
        var lightGray = argb(MapColor.COLOR_LIGHT_GRAY.col)

        @Color(title = "White")
        var white = argb(MapColor.SNOW.col)

        @Color(title = "Black")
        var black = argb(MapColor.COLOR_BLACK.col)

        fun getColor(mapColor: MapColor) = when (mapColor) {
            MapColor.COLOR_RED, MapColor.TERRACOTTA_RED -> red
            MapColor.COLOR_ORANGE -> orange
            MapColor.COLOR_YELLOW, MapColor.TERRACOTTA_YELLOW -> yellow
            MapColor.COLOR_LIGHT_GREEN -> lime
            MapColor.COLOR_GREEN, MapColor.TERRACOTTA_GREEN -> green
            MapColor.COLOR_CYAN -> cyan
            MapColor.COLOR_LIGHT_BLUE -> lightBlue
            MapColor.COLOR_BLUE, MapColor.TERRACOTTA_BLUE -> blue
            MapColor.COLOR_PURPLE -> purple
            MapColor.COLOR_MAGENTA -> magenta
            MapColor.COLOR_PINK -> pink
            MapColor.COLOR_BROWN -> brown
            MapColor.COLOR_GRAY -> gray
            MapColor.COLOR_LIGHT_GRAY -> lightGray
            MapColor.SNOW -> white
            MapColor.COLOR_BLACK -> black
            else -> error("Unknown color: MapColor{id=${mapColor.id}, col=${mapColor.col}}")
        }
    }

    @Switch(
        title = "Hide Actionbar in Dropper",
        description = "Hide the Actionbar in Dropper.",
        category = "Game",
        subcategory = "Dropper"
    )
    var hideDropperActionBar = false

    @Switch(
        title = "Mute Hurt Sounds in Dropper",
        description = "Mute the sounds of other players failing in Dropper.",
        category = "Game",
        subcategory = "Dropper"
    )
    var muteDropperHurtSound = false

    @Switch(
        title = "Lower Render Distance in Sumo",
        description = "Lowers render distance to your desired value in Sumo Duels.",
        category = "Game",
        subcategory = "Duels"
    )
    var sumoRenderDistance = false

    @Slider(
        title = "Sumo Render Distance",
        description = "Choose your render distance.",
        category = "Game",
        subcategory = "Duels",
        min = 2f,
        max = 5f,
        step = 1f
    )
    var sumoRenderDistanceAmount = 2f

    @Switch(
        title = "Hide Duels Cosmetics",
        description = "Hide Duels Cosmetics in Hypixel.",
        category = "Game",
        subcategory = "Duels"
    )
    var hideDuelsCosmetics = false

    @Switch(
        title = "Hide Actionbar in Housing",
        description = "Hide the Actionbar in Housing.",
        category = "Game",
        subcategory = "Housing"
    )
    var hideHousingActionBar = false

    @Switch(
        title = "Pit Lag Reducer",
        description = "Hide entities at spawn while you are in the PVP area.",
        category = "Game",
        subcategory = "Pit"
    )
    var pitLagReducer = true

    @Switch(
        title = "Remove Non-NPCs in SkyBlock",
        description = "Remove entities that are not NPCs in SkyBlock.",
        category = "Game",
        subcategory = "SkyBlock"
    )
    var hideNonNPCs = false

    @Switch(
        title = "Highlight Opened Chests",
        description = "Highlight chests that have been opened.",
        category = "Game",
        subcategory = "SkyWars"
    )
    var highlightChests = false

    @Color(
        title = "Highlight Color",
        category = "Game",
        subcategory = "SkyWars"
    )
    var highlightChestsColor = rgba(255, 0, 0)

    @Switch(
        title = "UHC Overlay",
        description = "Increase the size of dropped apples/golden apples, gold ingots/blocks/nuggets, and player heads in UHC Champions and Speed UHC.",
        category = "Game",
        subcategory = "UHC"
    )
    var uhcOverlay = false

    @Slider(
        title = "UHC Overlay Scale",
        description = "Adjust the scale multiplier.",
        category = "Game",
        subcategory = "UHC",
        min = 1f,
        max = 5f
    )
    var uhcOverlayScale = 2f

    @Switch(
        title = "UHC Middle Waypoint",
        description = "Adds a waypoint to signify (0,0).",
        category = "Game",
        subcategory = "UHC"
    )
    var uhcMiddleWaypoint = false

    @Text(
        title = "UHC Middle Waypoint Text",
        description = "The text that is displayed on the waypoint.",
        category = "Game",
        subcategory = "UHC"
    )
    var uhcMiddleWaypointText = "0,0"
    //endregion

    //region Lobby
    @Switch(
        title = "Hide Lobby NPCs",
        description = "Hide NPCs in the lobby.",
        category = "Lobby",
        subcategory = "NPCs"
    )
    var hideLobbyNPCs = false

    @Switch(
        title = "Hide Useless Lobby Nametags",
        description = "Hides unnecessary nametags such as those that say \"RIGHT CLICK\" or \"CLICK TO PLAY\" in a lobby, as well as other useless ones.",
        category = "Lobby",
        subcategory = "NPCs"
    )
    var hideUselessArmorStands = false

    @Switch(
        title = "Hide Lobby Bossbars",
        description = "Hide the bossbar in the lobby.",
        category = "Lobby",
        subcategory = "GUI"
    )
    var lobbyBossbar = true

    @Switch(
        title = "Silent Lobby",
        description = "Prevent all sounds from playing when you are in a lobby.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var silentLobby = false

    @Switch(
        title = "Disable Stepping Sounds",
        description = "Remove sounds created by stepping.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableSteppingSounds = false

    @Switch(
        title = "Disable Slime Sounds",
        description = "Remove sounds created by slimes.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableSlimeSounds = false

    @Switch(
        title = "Disable Dragon Sounds",
        description = "Remove sounds created by dragons.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableDragonSounds = false

    @Switch(
        title = "Disable Wither Sounds",
        description = "Remove sounds created by withers & wither skeletons.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableWitherSounds = false

    @Switch(
        title = "Disable Item Pickup Sounds",
        description = "Remove sounds created by picking up an item.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableItemPickupSounds = false

    @Switch(
        title = "Disable Experience Orb Sounds",
        description = "Remove sounds created by experience orbs.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableExperienceOrbSounds = false

    @Switch(
        title = "Disable Primed TNT Sounds",
        description = "Remove sounds created by primed TNT.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisablePrimedTntSounds = false

    @Switch(
        title = "Disable Explosion Sounds",
        description = "Remove sounds created by explosions.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableExplosionSounds = false

    @Switch(
        title = "Disable Delivery Man Sounds",
        description = "Remove sounds created by Delivery Man events.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableDeliveryManSounds = false

    @Switch(
        title = "Disable Note Block Sounds",
        description = "Remove sounds created by note blocks.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableNoteBlockSounds = false

    @Switch(
        title = "Disable Firework Sounds",
        description = "Remove sounds created by fireworks.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableFireworkSounds = false

    @Switch(
        title = "Disable Levelup Sounds",
        description = "Remove sounds created by someone leveling up.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableLevelupSounds = false

    @Switch(
        title = "Disable Arrow Sounds",
        description = "Remove sounds created by arrows.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableArrowSounds = false

    @Switch(
        title = "Disable Bat Sounds",
        description = "Remove sounds created by bats.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableBatSounds = false

    @Switch(
        title = "Disable Fire Sounds",
        description = "Remove sounds created by fire.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableFireSounds = false

    @Switch(
        title = "Disable Enderman Sounds",
        description = "Remove sounds created by endermen.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableEndermanSounds = false

    @Switch(
        title = "Disable Door Sounds",
        description = "Disable sounds caused by doors, trapdoors, and fence gates.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisableDoorSounds = false

    @Switch(
        title = "Disable Portal Sounds",
        description = "Disable sounds caused by nether portals.",
        category = "Lobby",
        subcategory = "Sounds"
    )
    var lobbyDisablePortalSounds = false

    @Switch(
        title = "Limbo Limiter",
        description = "While in Limbo, framerate is limited to 30, then further limited to 10 after 10 minutes, to reduce the load of the game on your computer.",
        category = "Lobby",
        subcategory = "Limbo"
    )
    var limboLimiter = false

    @Switch(
        title = "Limbo PM Ding",
        description = "While in Limbo, play the ding sound if you get a PM. Currently, Hypixel's option does not work in Limbo.",
        category = "Lobby",
        subcategory = "Limbo"
    )
    var limboPmDing = true
    //endregion

    init {
        // automatic dependencies
        addDependency("autoQueueDelay", "autoQueue")
        addDependency("gexpMode", "autoGetGEXP")
        addDependency("disableNotifyMiningFatigueSkyblock", "notifyMiningFatigue")
        addDependency("miningFatigueNotificationType", "notifyMiningFatigue")

        // chat dependencies
        listOf(
            "autoGGSendSecondMessage", "casualAutoGG", "autoGGMessage",
            "autoGGFirstMsgDelay", "autoGGSecondMessage", "autoGGSecondMsgDelay"
        ).forEach { addDependency(it, "autoGG") }

        addDependency("autoGLMessage", "autoGL")
        addDependency("afkTimeout", "autoReplyAfk")
        addDependency("afkReplyMessage", "autoReplyAfk")
        addDependency("chatEmotesReplacementMode", "replaceChatEmotes")
        addDependency("chatSwapperReturnChannel", "chatSwapper")
        addDependency("chatSwapperHideAllChannelMsg", "chatSwapper")
        addDependency("notifyWhenKickInCaps", "notifyWhenKick")

        listOf(
            "guildAutoWB", "friendsAutoWB", "autoWBCooldown", "autoWBMessage1", "randomAutoWB",
            "autoWBMessage2", "autoWBMessage3", "autoWBMessage4", "autoWBMessage5", "autoWBMessage6",
            "autoWBMessage7", "autoWBMessage8", "autoWBMessage9", "autoWBMessage10"
        ).forEach { addDependency(it, "autoWB") }

        // game dependencies/callbacks
        addDependency("blockNumber", "blockNotify")
        addDependency("blockNotifySound", "blockNotify")

        addCallback("heightOverlay") { mc.execute(mc.levelRenderer::allChanged) }
        listOf("heightOverlayMinBuild", "overlayAmount").forEach {
            addDependency(it, "heightOverlay", true)
            addCallback(it) { mc.execute(mc.levelRenderer::allChanged) }
        }

        addCallback("HeightOverlayCustomColors.enabled") { mc.execute(mc.levelRenderer::allChanged) }
        listOf(
            "red", "orange", "yellow", "lime", "green", "cyan", "lightBlue", "blue",
            "purple", "magenta", "pink", "brown", "gray", "lightGray", "white", "black"
        ).forEach {
            addDependency("HeightOverlayCustomColors.$it", "heightOverlay")
            addDependency("HeightOverlayCustomColors.$it", "HeightOverlayCustomColors.enabled")
            addCallback("HeightOverlayCustomColors.$it") { mc.execute(mc.levelRenderer::allChanged) }
        }

        addDependency("sumoRenderDistanceAmount", "sumoRenderDistance")
        addDependency("highlightChestsColor", "highlightChests")
        addDependency("uhcOverlayScale", "uhcOverlay")
        addDependency("uhcMiddleWaypointText", "uhcMiddleWaypoint")
    }
}
