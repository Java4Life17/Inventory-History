package invhistory.invhistory.Listeners;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;
import java.util.Objects;

public class exitInventorySave implements Listener {
    private InvHistory plugin;
    public exitInventorySave(InvHistory plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) throws IOException, InvalidConfigurationException {
        String title = event.getView().getTitle();
        title = title.replace(Tools.getColorText("&0"), "");
        String[] titleSize = title.split(" ");

        if(titleSize.length != 3){
            return;
        }
        if(titleSize[2].equalsIgnoreCase("GCI")){
            String playerName = titleSize[0];
            String key = titleSize[1];
            Player player = (Player) event.getPlayer();
            saveLeftItems(event.getInventory(), playerName, key, player);
            return;
        }
    }

    private void saveLeftItems(Inventory inventory, String playerName, String key, Player player) throws IOException, InvalidConfigurationException {

        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) != null) {
                if (Objects.requireNonNull(inventory.getItem(i)).getType().equals(Material.BARRIER)) {
                    inventory.remove(Objects.requireNonNull(inventory.getItem(i)));
                }
            }
        }
        String newData = Tools.toBase64(inventory);
        plugin.Deaths.set("Inventories." + playerName + "." + key + ".Inventory", newData);
        plugin.Deaths.save();
        plugin.reloadEverything();
    }
}
