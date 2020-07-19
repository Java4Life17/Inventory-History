package invhistory.invhistory.Options;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Tools.Tools;
import jdk.internal.org.objectweb.asm.commons.SerialVersionUIDAdder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class GiveCertainItems {
    private InvHistory plugin;

    public GiveCertainItems(InvHistory plugin) {
        this.plugin = plugin;
    }

    public Inventory certainItemsInv(String playerName, String inventoryKey, String key) throws IOException {

        Inventory inventory = Bukkit.createInventory(null, 54, Tools.getColorText("&0" + playerName + " " + key + " " + "GCI"));
        Inventory getter = Tools.fromBase64(inventoryKey, playerName, 1);

        for(ItemStack stack: getter) {
            if (stack != null) {
                inventory.addItem(stack);
            }
        }

        ItemStack returned = new ItemStack(Material.BARRIER);
        ItemMeta returnMeta = returned.getItemMeta();
        assert returnMeta != null;
        returnMeta.setDisplayName(Tools.getColorText("&a&l✔"));
        returned.setItemMeta(returnMeta);

        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, returned);
            }
        }


        return inventory;
    }

}
