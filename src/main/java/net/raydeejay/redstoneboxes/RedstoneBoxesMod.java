package net.raydeejay.redstoneboxes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.raydeejay.redstoneboxes.commands.TeleportCommand;
import net.raydeejay.redstoneboxes.proxy.CommonProxy;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(modid = RedstoneBoxesMod.MODID, name = RedstoneBoxesMod.NAME, version = RedstoneBoxesMod.VERSION,
     dependencies = "required-after:Forge@[12.18.3.2185,)", useMetadata = true)
public class RedstoneBoxesMod {
    public static final String MODID = "redstoneboxes";
    public static final String NAME = "Redstone Boxes";
    public static final String VERSION = "0.1";

    @SidedProxy(clientSide = "net.raydeejay.redstoneboxes.proxy.ClientProxy",
                serverSide = "net.raydeejay.redstoneboxes.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static RedstoneBoxesMod instance;

    // logger
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        RedstoneBoxesMod.logger.log(Level.INFO, "The Red Box finished loading");
        proxy.postInit(e);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new TeleportCommand());
    }
}
