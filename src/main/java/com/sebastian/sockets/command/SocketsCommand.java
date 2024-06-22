package com.sebastian.sockets.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sebastian.sockets.blockentities.SocketBlockEntity;
import com.sebastian.sockets.blockentities.SocketPluggableEntity;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeType;
import com.sebastian.sockets.recipe.ToasterRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.List;

@Mod.EventBusSubscriber
public class SocketsCommand {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        if (event.getCommandSelection() == Commands.CommandSelection.INTEGRATED)
            event.getDispatcher()
                    .register(Commands.literal("sockets").requires(s -> s.hasPermission(4)).then(Commands.literal("debug").then(Commands.literal("recipe").then(Commands.argument("recipeid", StringArgumentType.word()).executes(arguments -> {

                        String rId = StringArgumentType.getString(arguments, "recipeid");
                        Level world = arguments.getSource().getUnsidedLevel();
                        Entity entity = arguments.getSource().getEntity();
                        if (entity == null && world instanceof ServerLevel _servLevel)
                            entity = FakePlayerFactory.getMinecraft(_servLevel);

                        entity.sendSystemMessage(Component.literal("Recipes for " + rId + ":"));
                        if(rId.equals("toast")) {
                            System.out.println("toast r");
                            List<ToasterRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ToasterRecipe.Type.INSTANCE);
                            for (ToasterRecipe recipe : recipes) {
                                String rid = recipe.getId().toString();
                                String in = ForgeRegistries.ITEMS.getKey(recipe.getInputItem().getItems()[0].getItem()).toString();
                                String out = ForgeRegistries.ITEMS.getKey(recipe.getOutput().getItem()).toString();
                                entity.sendSystemMessage(Component.literal("Recipe " + rid + " takes " + in + " and returns " + out));
                            }
                        }

                        if(rId.equals("smelt")) {
                            System.out.println("smelt r");
                            List<SmeltingRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
                            for (SmeltingRecipe recipe : recipes) {
                                String rid = recipe.getId().toString();
                                String in = ForgeRegistries.ITEMS.getKey(recipe.getIngredients().get(0).getItems()[0].getItem()).toString();
                                String out = ForgeRegistries.ITEMS.getKey(recipe.getResultItem(RegistryAccess.EMPTY).getItem()).toString();
                                entity.sendSystemMessage(Component.literal("Recipe " + rid + " takes " + in + " and returns " + out));
                            }
                        }

                        return 0;

                    }))).then(Commands.literal("energy").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("energy_val", DoubleArgumentType.doubleArg(0)).executes(arguments -> {

                        BlockPos pos = BlockPosArgument.getBlockPos(arguments, "pos");
                        int energy = (int) DoubleArgumentType.getDouble(arguments, "energy_val");
                        Level world = arguments.getSource().getUnsidedLevel();
                        Entity entity = arguments.getSource().getEntity();
                        if (entity == null && world instanceof ServerLevel _servLevel)
                            entity = FakePlayerFactory.getMinecraft(_servLevel);

                        BlockEntity blockentity = world.getBlockEntity(pos);

                        if(blockentity == null) {
                            entity.sendSystemMessage(Component.literal("BlockEntity not found!"));
                            return 0;
                        }

                        if(blockentity instanceof SocketBlockEntity socketBlockEntity) {
                            socketBlockEntity.getEnergy().setEnergy(energy);
                            entity.sendSystemMessage(Component.literal("Set Sockets energy to " + energy));
                            return 0;
                        }

                        if(blockentity instanceof SocketPluggableEntity socketPlugable) {
                            socketPlugable.getEnergy().setEnergy(energy);
                            entity.sendSystemMessage(Component.literal("Set Devices energy to " + energy));
                            return 0;
                        }

                        entity.sendSystemMessage(Component.literal("Cant set energy here!"));

                        return 0;
                    }))))).then(Commands.literal("pub").executes(arguments -> {
                        Level world = arguments.getSource().getUnsidedLevel();
                        Entity entity = arguments.getSource().getEntity();
                        if (entity == null && world instanceof ServerLevel _servLevel)
                            entity = FakePlayerFactory.getMinecraft(_servLevel);
                        entity.sendSystemMessage(Component.literal("Cmd not done/found!"));
                        return 0;
                    })));
    }
}
