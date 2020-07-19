package invhistory.invhistory.Listeners;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class deathListener implements Listener {

    private InvHistory plugin;

    public deathListener(InvHistory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void listenForDeath(PlayerDeathEvent event) throws IOException, InvalidConfigurationException {

        int minItems = plugin.Configuration.getInt("MinItems");

        if (event.getDrops().size() < minItems) {
            return;
        }

        Player player = event.getEntity();

        String deathCause = event.getDeathMessage();

        String location = player.getLocation().getBlockX() + " ";
        location += player.getLocation().getBlockY() + " ";
        location += player.getLocation().getBlockZ() + " ";
        location += Objects.requireNonNull(player.getLocation().getWorld()).getName();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);

        Inventory inventory = Bukkit.createInventory(null, 54, Tools.getColorText("&dMuerte de " + player.getName()));
        for (ItemStack drop : event.getDrops()) {
            inventory.addItem(drop);
        }
        String inventoryBase64 = Tools.toBase64(inventory);

        String section = getRandomString(6);

        if(plugin.Deaths.getConfigurationSection("Inventories") == null){
            plugin.Deaths.createSection("Inventories");
            plugin.Deaths.save();
        }


        if(!plugin.Deaths.getConfigurationSection("Inventories").contains(player.getName())){
            plugin.Deaths.getConfigurationSection("Inventories").createSection(player.getName());
        }

        String random = "";
        boolean contains = true;

        while(contains){
            random = "Inv_" + getRandomString(7);
            if(!plugin.Deaths.getConfigurationSection("Inventories." + player.getName()).contains(random)){
                contains = false;
            }
        }

        plugin.Deaths.getConfigurationSection("Inventories." + player.getName()).createSection(random);
        plugin.Deaths.set("Inventories." + player.getName() + "." + random + ".Inventory", inventoryBase64);
        plugin.Deaths.set("Inventories." + player.getName() + "." + random + ".Time", time);
        plugin.Deaths.set("Inventories." + player.getName() + "." + random + ".Cause", deathCause);
        plugin.Deaths.set("Inventories." + player.getName() + "." + random + ".Location", location);

        plugin.Deaths.save();
        plugin.Deaths.load();

        plugin.reloadEverything();

    }

    public  String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}