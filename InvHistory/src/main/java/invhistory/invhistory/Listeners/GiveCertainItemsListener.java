package invhistory.invhistory.Listeners;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Tools.LocalManager;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveCertainItemsListener implements Listener {

    private InvHistory plugin;

    public GiveCertainItemsListener(InvHistory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGiveCertainItemsInv(InventoryClickEvent event){
        String title = event.getView().getTitle();
        title = title.replace(Tools.getColorText("&0"), "");
        String[] titleSize = title.split(" ");

        if(titleSize.length != 3){
            return;
        }
        if(titleSize[2].equalsIgnoreCase("GCI")){
            event.setCancelled(true);
            LocalManager manager = new LocalManager();

            Player player = (Player) event.getWhoClicked();
            Player receiver = plugin.getServer().getPlayer(titleSize[0]);

            if(receiver == null){
                String message = manager.getLocalMessage("PlayerOffline");
                Tools.sendColorMessage(player, message);
                player.closeInventory();
                return;
            }

            if(event.getCurrentItem() == null){
                return;
            }

            if(event.getCurrentItem().getType().equals(Material.BARRIER)){
                return;
            }
            ItemStack returned = new ItemStack(Material.BARRIER);
            ItemMeta returnMeta = returned.getItemMeta();
            assert returnMeta != null;
            returnMeta.setDisplayName(Tools.getColorText("&a&l✔"));
            returned.setItemMeta(returnMeta);

            ItemStack clicked = event.getCurrentItem();
            if(receiver.getInventory().firstEmpty() == -1){
                player.closeInventory();
                String message = manager.getLocalMessage("InvFull");
                Tools.sendColorMessage(player, message);
                return;
            }
            receiver.getInventory().addItem(clicked);
            event.getInventory().setItem(event.getSlot(), returned);
            Tools.playerPlaySound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.5F);

        }
    }

    private void saveLeftItems(Inventory inventory, String playerName, String key) {

    }

}
