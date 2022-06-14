package de.jpx3.bedrock

import de.jpx3.intave.IntaveAccessor
import de.jpx3.intave.access.IntaveColdException
import de.jpx3.intave.access.player.trust.TrustFactor
import de.jpx3.intave.access.player.trust.TrustFactorResolver
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi

class BedrockSupportPlugin : JavaPlugin() {
    override fun onEnable() {
        enableBedrockSupport()
    }

    /**
     * Enables [BedrockSupportPlugin] by creating a new [TrustFactorResolver] which assigns
     * the [TrustFactor.BYPASS] to bedrock players registered in the [FloodgateApi]
     */
    private fun enableBedrockSupport() {
        val access = IntaveAccessor.weakAccess().get() ?: run {
            throw IntaveColdException("Intave offline")
        }
        access.setTrustFactorResolver { player, callback ->
            if (FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)) {
                callback.accept(TrustFactor.BYPASS)
                return@setTrustFactorResolver
            }
        }
    }
}