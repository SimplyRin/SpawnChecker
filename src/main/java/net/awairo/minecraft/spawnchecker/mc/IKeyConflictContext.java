package net.awairo.minecraft.spawnchecker.mc;

public interface IKeyConflictContext {
    boolean isActive();
    boolean conflicts(IKeyConflictContext paramIKeyConflictContext);
}
