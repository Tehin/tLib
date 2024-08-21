package dev.tehin.tlib.core;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class LibLogger {

    public static void log(String msg) {
        Bukkit.getLogger().log(Level.INFO, "[tLib] " + msg);
    }

}
