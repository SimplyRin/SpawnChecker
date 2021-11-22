package net.awairo.minecraft.spawnchecker.mc;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;

public enum KeyModifier {
    CONTROL {
        public boolean matches(InputUtil.Key key) {
            int keyCode = key.getCode();
            if (MinecraftClient.IS_SYSTEM_MAC) {
                return (keyCode == 342 || keyCode == 346);
            }
            return (keyCode == 341 || keyCode == 345);
        }

        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.hasControlDown();
        }

        public BaseText getCombinedName(InputUtil.Key key, Supplier<BaseText> defaultLogic) {
            String localizationFormatKey = MinecraftClient.IS_SYSTEM_MAC ? "forge.controlsgui.control.mac" : "forge.controlsgui.control";
            return new TranslatableText(localizationFormatKey, new Object[] { defaultLogic.get() });
        }
    },
    SHIFT {
        public boolean matches(InputUtil.Key key) {
            return (key.getCode() == 340 || key.getCode() == 344);
        }

        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.hasShiftDown();
        }

        public BaseText getCombinedName(InputUtil.Key key, Supplier<BaseText> defaultLogic) {
            return new TranslatableText("forge.controlsgui.shift", new Object[] { defaultLogic.get() });
        }
    },
    ALT {
        public boolean matches(InputUtil.Key key) {
            return (key.getCode() == 342 || key.getCode() == 346);
        }

        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.hasAltDown();
        }

        public BaseText getCombinedName(InputUtil.Key keyCode, Supplier<BaseText> defaultLogic) {
            return new TranslatableText("forge.controlsgui.alt", new Object[] { defaultLogic.get() });
        }
    },
    NONE {
        public boolean matches(InputUtil.Key key) {
            return false;
        }

        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            if (conflictContext != null && !conflictContext.conflicts(KeyConflictContext.IN_GAME))
                for (KeyModifier keyModifier : MODIFIER_VALUES) {
                    if (keyModifier.isActive(conflictContext)) {
                        return false;
                    }
                }
            return true;
        }

        public BaseText getCombinedName(InputUtil.Key key, Supplier<BaseText> defaultLogic) {
            return defaultLogic.get();
        }
    };

    public static final KeyModifier[] MODIFIER_VALUES;

    static {
        MODIFIER_VALUES = new KeyModifier[] { SHIFT, CONTROL, ALT };
    }

    public static KeyModifier getActiveModifier() {
        for (KeyModifier keyModifier : MODIFIER_VALUES) {
            if (keyModifier.isActive(null)) {
                return keyModifier;
            }
        }
        return NONE;
    }

    public static boolean isKeyCodeModifier(InputUtil.Key key) {
        for (KeyModifier keyModifier : MODIFIER_VALUES) {
            if (keyModifier.matches(key))
                return true;
        }
        return false;
    }

    public static KeyModifier valueFromString(String stringValue) {
        try {
            return valueOf(stringValue);
        } catch (NullPointerException|IllegalArgumentException ignored) {
            return NONE;
        }
    }

    public abstract boolean matches(InputUtil.Key paramInput);

    public abstract boolean isActive(@Nullable IKeyConflictContext paramIKeyConflictContext);

    public abstract BaseText getCombinedName(InputUtil.Key paramInput, Supplier<BaseText> paramSupplier);
}
