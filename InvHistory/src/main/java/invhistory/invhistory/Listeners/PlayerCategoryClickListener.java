package invhistory.invhistory.Listeners;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Tools.CategoryInventory;
import invhistory.invhistory.Tools.Inventories;
import invhistory.invhistory.Tools.LocalManager;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import javax.swing.tree.ExpandVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerCategoryClickListener implements Listener {

    private InvHistory plugin;

    public PlayerCategoryClickListener(InvHistory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCategoryInv(InventoryClickEvent event) throws IOException, InvalidConfigurationException {
        LocalManager localManager = new LocalManager();
        String testTitle = localManager.getLocalMessage("Player_Menu_Name");
        testTitle = testTitle.replace("%player%", "");
        if (event.getView().getTitle().startsWith(Tools.getColorText(testTitle))) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            Tools.playerPlaySound(p, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, 1.8F);

            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getInventory().equals(p.getInventory())) {
                return;
            }
            if(event.getCurrentItem().getType().equals(Material.HONEY_BOTTLE)){
                return;
            }
            ItemStack clicked = event.getCurrentItem();
            String key = Objects.requireNonNull(clicked.getItemMeta()).getDisplayName();
            key = key.replace(Tools.getColorText("&4Key&d - &7"), "");
            String pName = "";
            List<String> lore = Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(10)).getItemMeta()).getLore();
            assert lore != null;
            int lastLore = lore.size() - 1;
            pName = lore.get(lastLore);
            pName = pName.replace(Tools.getColorText("&6Player&7: &e"), "");
            if (event.getSlot() == 43) {

                if(!plugin.Deaths.getConfigurationSection("Inventories").contains(pName)){
                    p.closeInventory();
                    String message = localManager.getLocalMessage("FailedToDelete");
                    Tools.sendColorMessage(p, message);
                    return;
                }

                System.out.println(pName);
                plugin.Deaths.set("Inventories." + pName, null);
                plugin.Deaths.save();
                plugin.reloadEverything();

                p.closeInventory();
                Tools.playerPlaySound(p, Sound.EVENT_RAID_HORN, 1.3F);
                String message = localManager.getLocalMessage("DeletedAllMessage");
                Tools.sendColorMessage(p, message);

            }else if(event.getSlot() == 37){
                p.closeInventory();
                p.openInventory(plugin.generalDeaths.get(1));
            }





            if (event.getCurrentItem().getType().equals(Material.PAPER)) {

                if(!plugin.Deaths.getConfigurationSection("Inventories").contains(pName)){
                    p.closeInventory();
                    String message = localManager.getLocalMessage("FailedToDelete");
                    Tools.sendColorMessage(p, message);
                    return;
                }

                if(!plugin.Deaths.getConfigurationSection("Inventories." + pName).contains(key)){
                    p.closeInventory();
                    String message = localManager.getLocalMessage("FailedToDelete");
                    Tools.sendColorMessage(p, message);
                    return;
                }

                Inventory inventory = getPlayerInventory(pName, key);
                p.closeInventory();
                p.openInventory(inventory);
                Tools.playerPlaySound(p, Sound.ITEM_TRIDENT_HIT_GROUND, 1.0F);

            }


        }

    }

    private Inventory getPlayerInventory(String pName, String key) throws IOException {
        String title = Tools.getColorText("&5" + pName + "&0 - &1" + key);
        //                                       "Nvme_SSD
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        /*
        Delete Items = Slot 45 - Redstone
        Give Certain Items = Slot 47 - Diamond PickAxe
        Let the Owner Choose Items = Slot 49 - Totem
        Display to Owner = Slot 51 Display a Copy of This Inventory to the Owner - Ender Eye
         */

        ItemStack delete = new ItemStack(Material.REDSTONE);
        ItemMeta deleteMeta = delete.getItemMeta();
        assert deleteMeta != null;
        deleteMeta.setDisplayName(" ");
        List<String> deleteLore = new ArrayList<>();
        for (String line : plugin.Language.getStringList("DeleteInventory")) {
            deleteLore.add(Tools.getColorText(line));
        }
        deleteMeta.setLore(deleteLore);
        delete.setItemMeta(deleteMeta);
        delete.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(45, delete);


        ItemStack giveCertain = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta giveCertainMeta = giveCertain.getItemMeta();
        assert giveCertainMeta != null;
        giveCertainMeta.setDisplayName(" ");
        List<String> giveCertainLore = new ArrayList<>();
        for (String line : plugin.Language.getStringList("GiveCertainItems")) {
            giveCertainLore.add(Tools.getColorText(line));
        }
        giveCertainMeta.setLore(giveCertainLore);
        giveCertainMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        giveCertain.setItemMeta(giveCertainMeta);
        giveCertain.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(47, giveCertain);


        ItemStack letOwnerChoose = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta letOwnerMeta = letOwnerChoose.getItemMeta();
        assert letOwnerMeta != null;
        letOwnerMeta.setDisplayName(" ");
        List<String> letOwnerLore = new ArrayList<>();
        for (String line : plugin.Language.getStringList("LetOwnerChoose")) {
            letOwnerLore.add(Tools.getColorText(line));
        }
        letOwnerMeta.setLore(letOwnerLore);
        letOwnerChoose.setItemMeta(letOwnerMeta);
        letOwnerChoose.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(49, letOwnerChoose);


        ItemStack displayCopy = new ItemStack(Material.ENDER_EYE);
        ItemMeta displayMeta = displayCopy.getItemMeta();
        assert displayMeta != null;
        displayMeta.setDisplayName(" ");
        List<String> displayLore = new ArrayList<>();
        for (String line : plugin.Language.getStringList("OpenDemoCopy")) {
            displayLore.add(Tools.getColorText(line));
        }
        displayMeta.setLore(displayLore);
        displayCopy.setItemMeta(displayMeta);
        displayCopy.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(51, displayCopy);


        ItemStack returnItems = new ItemStack(Material.EMERALD);
        ItemMeta returnMeta = returnItems.getItemMeta();
        assert returnMeta != null;
        returnMeta.setDisplayName(" ");
        List<String> returnLore = new ArrayList<>();
        for (String line : plugin.Language.getStringList("GiveItemsBack")) {
            returnLore.add(Tools.getColorText(line));
        }
        returnMeta.setLore(returnLore);
        returnItems.setItemMeta(returnMeta);
        returnItems.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(53, returnItems);

        ItemStack mainMenu = new ItemStack(Material.CLOCK);
        ItemMeta mainMenuMeta = mainMenu.getItemMeta();
        assert mainMenuMeta != null;
        mainMenuMeta.setDisplayName(" ");
        List<String> mainMenuLore = new ArrayList<>();
        for(String line: plugin.Language.getStringList("BackToMainMenu")){
            mainMenuLore.add(Tools.getColorText(line));
        }
        mainMenuMeta.setLore(mainMenuLore);
        mainMenu.setItemMeta(mainMenuMeta);
        mainMenu.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(46, mainMenu);


        ItemStack goToLocation = new ItemStack(Material.COMPASS);
        ItemMeta locationMeta = goToLocation.getItemMeta();
        assert locationMeta != null;
        locationMeta.setDisplayName(" ");
        List<String> locationLore = new ArrayList<>();
        for(String line: plugin.Language.getStringList("GotoLocation")){
            locationLore.add(Tools.getColorText(line));
        }
        locationMeta.setLore(locationLore);
        goToLocation.setItemMeta(locationMeta);
        goToLocation.addUnsafeEnchantment(InvHistory.glowEnch, 1);
        inventory.setItem(52, goToLocation);

        ItemStack greenPane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta greenPaneMeta = greenPane.getItemMeta();
        assert greenPaneMeta != null;
        greenPaneMeta.setDisplayName(" ");
        greenPane.setItemMeta(greenPaneMeta);
        //46 52

        inventory.setItem(48, greenPane);
        inventory.setItem(50, greenPane);


        String invB64 = plugin.Deaths.getString("Inventories." + pName + "." + key + ".Inventory");

        Inventory recovered = Tools.fromBase64(invB64, pName, 1);
        for (ItemStack itemStack : recovered) {
            if (itemStack != null) {
                inventory.addItem(itemStack);
            }
        }


        for (int i = 0; i < 45; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, greenPane);
            }
        }

        return inventory;
    }

}
