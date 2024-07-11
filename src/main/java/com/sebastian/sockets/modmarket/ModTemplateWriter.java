package com.sebastian.sockets.modmarket;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ModTemplateWriter {

    public static void modTemplate(String id, String name) {
        // Example data to write
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> addonInfo = new HashMap<>();
        addonInfo.put("id", id);
        addonInfo.put("name", name);
        data.put("info", addonInfo);

        List<String> features = new ArrayList<>();
        features.add("/path/to/file.toml");
        features.add("recipes/custom_toaster_recipe.toml");
        features.add("blocks/custom_energy_block.toml");
        data.put("features", features);

        // Directory where the addon.zip will reside
        String minecraftDir = FMLPaths.GAMEDIR.get().toString();
        String addonDir = minecraftDir + "/addons/dev/example_addon";

        try {
            // Ensure the directory structure exists
            Path addonPath = Paths.get(addonDir);
            Files.createDirectories(addonPath);

            // Write to addon.toml within the addon directory
            File addonFile = new File(addonDir + "/addon.toml");
            try (FileConfig config = FileConfig.of(addonFile, TomlFormat.instance())) {
                config.load();

                // Set values individually
                config.set("info.id", id);
                config.set("info.name", name);
                config.set("features", features);

                config.save();
                System.out.println("Config saved successfully to " + addonFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void registerCommand(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("socketsdevicesaddons")
                .then(Commands.literal("template").then(Commands.argument("id", StringArgumentType.word())
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(arguments -> {
                                    Level world = arguments.getSource().getUnsidedLevel();

                                    double x = arguments.getSource().getPosition().x();
                                    double y = arguments.getSource().getPosition().y();
                                    double z = arguments.getSource().getPosition().z();

                                    Entity entity = arguments.getSource().getEntity();
                                    if (entity == null && world instanceof ServerLevel _servLevel)
                                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                                    Direction direction = Direction.DOWN;
                                    if (entity != null)
                                        direction = entity.getDirection();

                                    modTemplate(StringArgumentType.getString(arguments, "id"), StringArgumentType.getString(arguments, "name"));

                                    String minecraftDir = FMLPaths.GAMEDIR.get().toString();
                                    String filePath = minecraftDir + "/addons/dev/example_addon";

                                    MutableComponent textComponent = Component.literal(filePath);
                                    textComponent.setStyle(Style.EMPTY
                                            .withBold(true)
                                            .withUnderlined(true)
                                            .withColor(ChatFormatting.BLUE)
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, filePath))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy to clipboard."))));

                                    assert entity != null;
                                    entity.sendSystemMessage(textComponent);

                                    return 0;
                                })))));
    }
}
