package net.nishtown.immersionearlydays;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nishtown.immersionearlydays.block.ModBlocks;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.handlers.*;
import net.nishtown.immersionearlydays.item.ModCreativeModeTabs;
import net.nishtown.immersionearlydays.item.ModItems;
import net.nishtown.immersionearlydays.recipe.ModRecipes;
import net.nishtown.immersionearlydays.screen.ModMenuTypes;
import net.nishtown.immersionearlydays.screen.StoneWasherScreen;
import net.nishtown.immersionearlydays.util.CoordinateDisplayHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ImmersionEarlyDays.MOD_ID)
public class ImmersionEarlyDays
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "immersionearlydays";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ImmersionEarlyDays()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        KeybindHandler.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(KeyInputHandler.class);
        // Register creative tabs, items, blocks, etc.
        ModCreativeModeTabs.register(modEventBus);
        ModItems.regiser(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);


        // Add listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::doClientSetup);

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new SaplingGrowthHandler());
        MinecraftForge.EVENT_BUS.register(new PebbleDropHandler());
        MinecraftForge.EVENT_BUS.register(new CobblestoneGeneratorHandler());
        MinecraftForge.EVENT_BUS.register(CoordinateDisplayHandler.class);

        MinecraftForge.EVENT_BUS.register(this);

        // Register configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)    {


    }

    private void doClientSetup(final FMLClientSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.STONE_WASHER_MENU.get(), StoneWasherScreen::new);


        }

    }


    public class ClientState {
        public static boolean showBlockPos = false; // Initially off
    }
}
