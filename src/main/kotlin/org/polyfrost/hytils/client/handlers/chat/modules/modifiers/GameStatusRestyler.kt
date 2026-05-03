package org.polyfrost.hytils.client.handlers.chat.modules.modifiers

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.polyfrost.hytils.client.HytilsRebornConfig
import org.polyfrost.hytils.client.data.providers.LanguageData
import org.polyfrost.hytils.client.events.ChatReceiveEvent
import org.polyfrost.hytils.client.handlers.chat.ChatReceiveModule

object GameStatusRestyler : ChatReceiveModule {
    private var playerCount = -1
    private var maxPlayerCount = -1

    override fun onChatReceived(event: ChatReceiveEvent) {
        if (event.isOverlay) return

        val joinMatch = LanguageData.GAME_JOIN.find(event.plainMessage)
        joinMatch?.let { match ->
            val amount = match.groups["amount"]?.value?.removeSurrounding("(", ")") ?: return@let

            val amounts = amount.split('/')
            if (amounts.size == 2) {
                playerCount = amounts[0].toIntOrNull() ?: playerCount
                maxPlayerCount = amounts[1].toIntOrNull() ?: maxPlayerCount
            }
        }

        val cleaned = event.message.copy().apply { siblings.removeIf { it.string.isEmpty() } }
        val playerName = cleaned.siblings.firstOrNull() ?: return

        when {
            joinMatch != null -> {
                val amount = amountComponent(playerCount)

                event.message = if (HytilsRebornConfig.gameStatusRestyle) {
                    val prefix = Component.empty().append(Component.literal("+ ").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD))
                    if (HytilsRebornConfig.playerCountBeforePlayerName) {
                        prefix.append(amount).append(" ").append(playerName)
                    } else {
                        prefix.append(playerName).append(" ").append(amount)
                    }
                } else {
                    val base = Component.empty().withStyle(ChatFormatting.YELLOW)
                    if (HytilsRebornConfig.playerCountBeforePlayerName) {
                        base.append(amount).append(" ").append(playerName).append(" has joined!")
                    } else if (HytilsRebornConfig.padPlayerCount) {
                        base.append(playerName.copy()).append(" has joined ").append(amount).append("!")
                    } else {
                        event.message
                    }
                }
            }

            event.plainMessage.contains(LanguageData.GAME_LEAVE) -> {
                val amount = amountComponent(--playerCount)

                event.message = if (HytilsRebornConfig.gameStatusRestyle) {
                    val prefix = Component.empty().append(Component.literal("- ").withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
                    if (!HytilsRebornConfig.playerCountOnPlayerLeave) {
                        prefix.append(playerName)
                    } else if (HytilsRebornConfig.playerCountBeforePlayerName) {
                        prefix.append(amount).append(" ").append(playerName)
                    } else {
                        prefix.append(playerName).append(" ").append(amount)
                    }
                } else {
                    if (HytilsRebornConfig.playerCountOnPlayerLeave) {
                        if (HytilsRebornConfig.playerCountBeforePlayerName) {
                            Component.empty().append(amount).append(" ").append(event.message)
                        } else {
                            Component.empty().withStyle(ChatFormatting.YELLOW)
                                .append(playerName.copy()).append(" has quit ").append(amount).append("!")
                        }
                    } else {
                        event.message
                    }
                }
            }

            HytilsRebornConfig.gameStatusRestyle -> {
                event.message = when {
                    event.plainMessage.matches(LanguageData.GAME_STARTING) -> {
                        Component.empty()
                            .append(Component.literal("⁎ ").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD))
                            .append(cleaned.siblings[0].copy().withStyle(ChatFormatting.GREEN))
                            .append(cleaned.siblings[1].copy().withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD))
                            .append(cleaned.siblings[2].copy().withStyle(ChatFormatting.GREEN))
                    }

                    event.plainMessage == LanguageData.GAME_START_CANCELLED -> {
                        Component.literal("⁎ ").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                            .append(Component.literal("Start cancelled.").withStyle(ChatFormatting.RED))
                    }

                    event.plainMessage == LanguageData.GAME_START_DELAYED -> {
                        Component.literal("⁎ ").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                            .append(Component.literal("Start delayed.").withStyle(ChatFormatting.RED))
                    }

                    else -> event.message
                }
            }
        }
    }

    // this should run before game start compactor
    override fun getPriority() = 1

    private fun amountComponent(count: Int) = Component.literal("(").withStyle(ChatFormatting.YELLOW)
        .append(Component.literal(pad(count)).withStyle(ChatFormatting.AQUA))
        .append(Component.literal("/").withStyle(ChatFormatting.YELLOW))
        .append(Component.literal(maxPlayerCount.toString()).withStyle(ChatFormatting.AQUA))
        .append(Component.literal(")").withStyle(ChatFormatting.YELLOW))

    private fun pad(n: Int): String {
        val nString = n.toString()
        return if (HytilsRebornConfig.padPlayerCount) {
            val paddingSize = maxPlayerCount.toString().length - nString.length
            "0".repeat(paddingSize.coerceAtLeast(0)) + nString
        } else {
            nString
        }
    }
}
