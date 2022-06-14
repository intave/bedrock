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
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi
import java.lang.ref.WeakReference

class BedrockSupportPlugin : JavaPlugin(), Listener {
    private lateinit var accessReference: WeakReference<IntaveAccess>

    override fun onEnable() {
        enableBedrockSupport()
    }

    /**
     * Enables the [BedrockSupportPlugin] by creating a join listener to automatically assign the [TrustFactor.BYPASS]
     * to bedrock players
     */
    private fun enableBedrockSupport() {
        accessReference = IntaveAccessor.weakAccess()
        Bukkit.getPluginManager().registerEvents(this, this)
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, { check(tick + 1, player) }, 10)
        }
    }
}
