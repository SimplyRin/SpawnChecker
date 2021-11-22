package net.awairo.minecraft.spawnchecker.mc;

import net.minecraft.client.MinecraftClient;

public enum KeyConflictContext implements IKeyConflictContext {
    UNIVERSAL {
        public boolean isActive() {
            return true;
        }

        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },
    GUI {
        public boolean isActive() {
            return ((MinecraftClient.getInstance()).currentScreen != null);
        }

        public boolean conflicts(IKeyConflictContext other) {
            return (this == other);
        }
    },
    IN_GAME {
        public boolean isActive() {
            return !GUI.isActive();
        }

        public boolean conflicts(IKeyConflictContext other) {
            return (this == other);
        }
    };
}
