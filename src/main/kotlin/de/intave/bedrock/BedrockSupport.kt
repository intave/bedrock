package de.intave.bedrock

import de.jpx3.intave.IntaveAccessor
import de.jpx3.intave.access.IntaveAccess
import de.jpx3.intave.access.IntaveColdException
import de.jpx3.intave.access.player.trust.TrustFactor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.geysermc.floodgate.api.FloodgateApi
import java.lang.ref.WeakReference

class BedrockSupport(private val plugin: BedrockSupportPlugin) : Listener {
    private val accessReference: WeakReference<IntaveAccess> = IntaveAccessor.weakAccess()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    private fun receiveJoin(event: PlayerJoinEvent) {
        check(0, event.player)
    }

    /**
     * Checks if the player's floodgate state changes in the next 30 ticks (3 x 10 ticks) to re-assign the
     * trust-factor if anything went wrong
     *
     * Once the player is marked as bedrock player no re-check will be scheduled
     *
     * @param tick The current ticks
     * @param player The player being checked
     */
    private fun check(tick: Int, player: Player) {
        val access = accessReference.get() ?: throw IntaveColdException("Intave offline")
        val intavePlayer = access.player(player)
        if (FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)) {
            intavePlayer.setTrustFactor(TrustFactor.BYPASS)
        } else if (tick < 3) {
            // Schedule a new check in 10 ticks
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, { check(tick + 1, player) }, 10)
        }
    }

}
