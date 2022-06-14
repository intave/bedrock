package de.intave.bedrock

import de.jpx3.intave.IntaveAccessor
import de.jpx3.intave.access.IntaveAccess
import de.jpx3.intave.access.IntaveColdException
import de.jpx3.intave.access.player.trust.TrustFactor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi

class BedrockSupportPlugin : JavaPlugin(), Listener {
    private lateinit var access: IntaveAccess

    override fun onEnable() {
        enableBedrockSupport()
    }

    /**
     * Enables the [BedrockSupportPlugin] by creating a join listener to automatically assign the [TrustFactor.BYPASS]
     * to bedrock players
     */
    private fun enableBedrockSupport() {
        access = IntaveAccessor.weakAccess().get() ?: throw IntaveColdException("Intave offline")
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    private fun receiveJoin(event: PlayerJoinEvent) {
        val player = event.player
        val intavePlayer = access.player(player)
        if (FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)) {
            intavePlayer.setTrustFactor(TrustFactor.BYPASS)
        }
    }
}
