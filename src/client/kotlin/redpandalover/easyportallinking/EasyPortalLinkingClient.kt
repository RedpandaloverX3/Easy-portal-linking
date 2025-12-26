package redpandalover.easyportallinking

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.client.MinecraftClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.world.World
import org.slf4j.LoggerFactory
import net.minecraft.text.Text
import kotlin.math.round
import redpandalover.easyportallinking.EasyPortalLinkingClient.marker
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.particle.ParticleTypes




object EasyPortalLinkingClient : ClientModInitializer {
    private val logger = LoggerFactory.getLogger("Easy portal linking")

    fun copyToClipboard(text: String) {
        MinecraftClient.getInstance().keyboard.clipboard = text
    }
    class marker {
        companion object {
            var beamx: Int = 0
            var beamz = 0
            var beamdim = 0
            var recentcoords =""
        }
    }


	override fun onInitializeClient() {





        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("portalcoords")
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
                            marker.recentcoords = "$xnetherStr $znetherStr in  the Nether"

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
                            marker.recentcoords = "$xOwStr $zOwStr in the Overworld"
                            context.getSource().sendFeedback(Text.literal("$xOwStr $zOwStr in the Overworld"));
                        }

                        1
                    }
                    .then(
                        ClientCommandManager.argument("dimension", StringArgumentType.string())
                            .suggests { context, builder ->
                                builder.suggest("overworld")
                                builder.suggest("nether")
                                builder.suggest("")
                                builder.buildFuture()
                            }
                            .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                                .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                        .executes { context ->


                            val dimension = StringArgumentType.getString(context, "dimension")
                            val x = IntegerArgumentType.getInteger(context, "x")
                            val z = IntegerArgumentType.getInteger(context, "z")
                            logger.info(x.toString())
                            logger.info(z.toString())
                            logger.info(dimension)



                            if (dimension == "overworld") {
                                val xnether = x.div(8)
                                val znether = z.div(8)

                                marker.recentcoords = "$xnether $znether"
                                context.getSource().sendFeedback(Text.literal("$xnether $znether in the Nether"));
                            }
                            if (dimension == "nether") {
                                val xOw = x.times(8)
                                val zOw = z.times(8)


                                marker.recentcoords = "$xOw $zOw"
                                context.getSource().sendFeedback(Text.literal("$xOw $zOw in the Overworld"));
                            }


                            1
                        }

            )
                    )
            )
            )

        }
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("copyrecentcoords")
                    .executes { context ->
                        context.getSource().sendFeedback(Text.literal("copied ${marker.recentcoords} to the clipboard"));
                        copyToClipboard(marker.recentcoords)

                        1

                    }
            )

        }
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("copycoords")
                    .executes { context ->
                        val client = MinecraftClient.getInstance()
                        val player = client.player


                        val x: Double = player?.x ?: return@executes 0
                        val z: Double = player.z
                        val xStr = "%.0f".format(x)
                        val zStr = "%.0f".format(z)


                        marker.recentcoords="$xStr $zStr"
                        copyToClipboard("$xStr $zStr")
                        context.getSource().sendFeedback(Text.literal("copied $xStr $zStr to the clipboard"));
                        1

                    }
                    .then(
                        ClientCommandManager.argument("", StringArgumentType.string())
                            .suggests { context, builder ->
                                builder.suggest("include_dimension")
                                builder.buildFuture()
                            }
                            .executes { context ->
                                val client = MinecraftClient.getInstance()
                                val player = client.player
                                val world = client.world

                                val x: Double = player?.x ?: return@executes 0
                                val z: Double = player.z
                                val xStr = "%.0f".format(x)
                                val zStr = "%.0f".format(z)
                                val dimension = world?.registryKey
                                if (dimension == World.NETHER){
                                    context.getSource().sendFeedback(Text.literal("copied $xStr $zStr in the Nether to the clipboard"));
                                    marker.recentcoords="$xStr $zStr in the Nether"
                                    copyToClipboard("$xStr $zStr in the Nether")

                                }
                                if (dimension == World.OVERWORLD) {
                                    context.getSource().sendFeedback(Text.literal("copied $xStr $zStr in the Overworld to the clipboard"));
                                    marker.recentcoords="$xStr $zStr in the Overworld"
                                    copyToClipboard("$xStr $zStr in the Overworld")

                                }
                                if (dimension == World.END){
                                    context.getSource().sendFeedback(Text.literal("copied $xStr $zStr in the End to the clipboard"));
                                    marker.recentcoords="$xStr $zStr in the End"
                                    copyToClipboard("$xStr $zStr in the End")

                                }

                                1
                            }
                    )
            )
        }


        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("markrecentcoords")
                    .executes { context ->
                        if ("End" in marker.recentcoords){
                            marker.beamdim =1
                        }
                        else if ("Nether" in marker.recentcoords){
                            marker.beamdim =-1
                        }
                        else if ("Overworld" in marker.recentcoords){
                            marker.beamdim =0
                        }
                        else{
                            marker.beamdim =2
                        }
                        val numbers = "-?\\d+".toRegex().findAll(marker.recentcoords).map { it.value.toInt() }.toList()
                        marker.beamx = numbers[0]
                         marker.beamz = numbers[1]

                        1

                    }
            )
        }
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            renderBeam()
        }
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("setmarker")
                    .executes { context ->
                        val client = MinecraftClient.getInstance()
                        val player = client.player
                        val world =client.world


                        val x: Double = player?.x ?: return@executes 0
                        val z: Double = player.z
                        val xStr = "%.0f".format(x)
                        val zStr = "%.0f".format(z)
                        val dimension = world?.registryKey
                        if (dimension == World.NETHER){
                            marker.beamdim = -1
                            val dimension ="Nether"
                        }
                        if (dimension == World.OVERWORLD) {
                            marker.beamdim = 0
                            val dimension ="Overworld"
                        }
                        if (dimension == World.END){
                            marker.beamdim = 1
                            val dimension ="End"
                        }


                        marker.beamx =xStr.toInt()
                        marker.beamz =zStr.toInt()
                        context.getSource().sendFeedback(Text.literal("set marker to ${xStr.toInt()} ${zStr.toInt()} in the $dimension"));
                        1
                    }
                        )
                    }

        }
    }

fun renderBeam() {
    val client = MinecraftClient.getInstance()
    val world = client.world ?: return
    val markerdimension = marker.beamdim
    val markerz= marker.beamz
    val markerx= marker.beamx

    val dimId = when (world.registryKey.value.path) {
        "overworld" -> 0
        "the_nether" -> -1
        "the_end" -> 1
        else -> return
    }
    if (markerdimension != 2) {
        if (markerdimension != dimId) return
    }
    val x = markerx + 0.5
    val z = markerz + 0.5

    // Spawn vertical beam
    for (y in 0..320) {
        MinecraftClient.getInstance().particleManager.addParticle(
            ParticleTypes.END_ROD,
            markerx + 0.5,
            y.toDouble(),
            markerz + 0.5,
            0.0,
            0.05,
            0.0
        )
    }
}


