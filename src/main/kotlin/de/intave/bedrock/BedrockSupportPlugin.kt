package de.intave.bedrock

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class BedrockSupportPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        // delay any intialization to make sure that intave api classes are loaded
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
            BedrockSupport(this)
            logger.info("Bedrock support enabled");
        }, 1)
    }
}
