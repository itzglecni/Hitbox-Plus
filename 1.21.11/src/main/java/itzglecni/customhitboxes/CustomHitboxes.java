package itzglecni.customhitboxes;

import itzglecni.customhitboxes.command.TeamCommands;
import itzglecni.customhitboxes.config.ConfigManager;
import itzglecni.customhitboxes.gui.CustomConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CustomHitboxes implements ClientModInitializer {
    // Read from fabric.mod.json, which Gradle fills from the VERSION file. Never hard-code a version here.
    public static final String VERSION = FabricLoader.getInstance().getModContainer("hitboxesplus")
        .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
        .orElse("dev");

    private static final Category KEY_CATEGORY = Category.create(Identifier.of("hitboxesplus", "main"));
    private static final String OPEN_GUI_KEY = "key.hitboxesplus.open_config";

    private static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            OPEN_GUI_KEY,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            KEY_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> TeamCommands.register(dispatcher));
        System.out.println("[Hitbox Plus] Initialized ItzGlecni's Client-Side Hitbox Rendering Engine v" + VERSION + ".");
    }

    private void onClientTick(MinecraftClient client) {
        while (openConfigKey.wasPressed()) {
            if (!ConfigManager.getConfig().openConfigHotkeyEnabled) {
                continue;
            }

            client.setScreen(new CustomConfigScreen(client.currentScreen));
        }
    }
}