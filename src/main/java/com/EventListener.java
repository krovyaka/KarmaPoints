package com;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

public class EventListener implements Listener {

    private final KarmaPoints plugin;

    public EventListener(KarmaPoints plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(PlayerChatEvent e) {
        TextComponent textComponent = new TextComponent("[Test]");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Description").create()));
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(textComponent.getText());
        e.setCancelled(true);
    }
}