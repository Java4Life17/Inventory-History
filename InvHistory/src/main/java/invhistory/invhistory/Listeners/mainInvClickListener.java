package invhistory.invhistory.Listeners;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Tools.CategoryInventory;
import invhistory.invhistory.Tools.LocalManager;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class mainInvClickListener implements Listener {

    private InvHistory plugin;

    public mainInvClickListener(InvHistory plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void mainMenuListener(InventoryClickEvent event) {
        if (event.getView().getTitle().contains(Tools.getColorText(plugin.title))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            Tools.playerPlaySound(player, Sound.BLOCK_LEVER_CLICK, 1.6F);

            String title = event.getView().getTitle();
            title = title.replace(Tools.getColorText(plugin.title) + " ", "");

            int pageNumer = Integer.parseInt(title);
            int nextPage = pageNumer + 1;
            int previousPage = pageNumer - 1;

            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.PURPLE_STAINED_GLASS_PANE)) {
                return;
            }

            if (Objects.equals(event.getClickedInventory(), player.getInventory())) {
                return;
            }

            if (event.getSlot() == 53) {
                if (plugin.generalDeaths.get(nextPage) == null){
                    Tools.playerPlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 0.6F);
                }else{
                    Tools.playerPlaySound(player, Sound.ITEM_BOOK_PAGE_TURN, 0.6F);
                    player.closeInventory();
                    player.openInventory(plugin.generalDeaths.get(nextPage));
                }
            }else if(event.getSlot() == 45){
                if (plugin.generalDeaths.get(previousPage) == null){
                    Tools.playerPlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 0.6F);
                }else{
                    Tools.playerPlaySound(player, Sound.ITEM_BOOK_PAGE_TURN, 0.6F);
                    player.closeInventory();
                    player.openInventory(plugin.generalDeaths.get(previousPage));
                }
            }else{
                ItemStack item = event.getCurrentItem();
                String name = Objects.requireNonNull(item.getItemMeta()).getDisplayName();
                name = name.replace(Tools.getColorText("&e&l[&c "), "").replace(Tools.getColorText(" &e&l]"), "");
                CategoryInventory categoryInventory = new CategoryInventory(plugin);
                LocalManager localManager = new LocalManager();

                if(!plugin.Deaths.getConfigurationSection("Inventories").contains(name)){
                        player.closeInventory();
                        String message = localManager.getLocalMessage("FailedToDelete");
                        Tools.sendColorMessage(player, message);
                        return;
                }

                String titleSent =  localManager.getLocalMessage("Player_Menu_Name");
                titleSent = titleSent.replace("%player%", name);
                player.closeInventory();
                player.openInventory(categoryInventory.getPlayerInventory(name, Tools.getColorText(titleSent)));
                Tools.playerPlaySound(player, Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1.7F);



            }
        }
    }

}
