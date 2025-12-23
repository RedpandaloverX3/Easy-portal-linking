package redpandalover.easyportallinking

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.client.MinecraftClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.world.World
import org.apache.logging.log4j.Logger
import org.slf4j.LoggerFactory
import jdk.internal.net.http.common.TimeSource.source
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.round

object EasyPortalLinkingClient : ClientModInitializer {
    private val logger = LoggerFactory.getLogger("Easy portal linking")
	override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("portal")
                    .executes { context ->
                        val client = MinecraftClient.getInstance()
                        val player = client.player
                        val world = client.world

                        val x: Double = player?.x ?: return@executes 0
                        val z: Double = player.z
                        val xStr = "%.0f".format(x)
                        val zStr = "%.0f".format(z)
                        val dimension = world?.registryKey
                        if (dimension == World.OVERWORLD) {
                            val xOw = round(x)
                            val zOw = round(z)
                            val xnether = xOw.div(8)
                            val znether = zOw.div(8)
                            val xnetherStr = "%.0f".format(xnether)
                            val znetherStr = "%.0f".format(znether)
                            logger.info("$xnetherStr $znetherStr in the Nether")
                            context.getSource().sendFeedback(Text.literal("$xnetherStr $znetherStr in the Nether"));
                        }
                        if (dimension == World.NETHER) {
                            val xnether = round(x)
                            val znether = round(z)
                            val xOw = xnether.times(8)
                            val zOw = znether.times(8)
                            val xOwStr = "%.0f".format(xOw)
                            val zOwStr = "%.0f".format(zOw)


                            logger.info("$xOwStr $zOwStr in the Overworld")
                            context.getSource().sendFeedback(Text.literal("$xOwStr $zOwStr in the Overworld"));
                        }
                        if (dimension == World.END) {
                            context.getSource().sendFeedback(Text.literal("You cant make a portal in this dimension").formatted(Formatting.RED));
                        }
                        1
                    })
        }
	}
}

