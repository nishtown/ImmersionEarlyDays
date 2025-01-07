package net.nishtown.immersionearlydays.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.ModBlocks;

public class ModCreativeModeTabs {
    public  static  final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ImmersionEarlyDays.MOD_ID);

    public  static final RegistryObject<CreativeModeTab> IMMERSIONEARLYDAYS_TAB = CREATIVE_MODE_TABS.register("immersionearlydays",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.STONE_WASHER_BLOCK.get()))
                    .title(Component.translatable("creativetab:immersionearlydays"))
                    .displayItems((pParameters,pOutput) -> {

                        for(int i = 0; i < ModBlocks.ALL_BLOCKS.stream().count(); i++)
                        {
                            pOutput.accept((ModBlocks.ALL_BLOCKS.get(i).get()));
                        }

                        for(int i = 0; i < ModItems.ALL_ITEMS.stream().count(); i++)
                        {
                            pOutput.accept((ModItems.ALL_ITEMS.get(i).get()));
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
