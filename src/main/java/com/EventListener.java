package com;

import org.bukkit.event.Listener;

public class EventListener implements Listener {

    private final KarmaPoints plugin;

    public EventListener(KarmaPoints plugin) {
        this.plugin = plugin;
    }

//    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
//    public void onChat(PlayerChatEvent e) {
//        TextComponent textComponent = new TextComponent("[Test]");
//        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Description").create()));
//        for (Player p : Bukkit.getOnlinePlayers())
//            p.sendMessage(textComponent.getText());
//        e.setCancelled(true);
//    }
}