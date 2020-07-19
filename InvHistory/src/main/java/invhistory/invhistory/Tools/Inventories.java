package invhistory.invhistory.Tools;

import invhistory.invhistory.InvHistory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inventories implements Listener {

    private InvHistory plugin;

    public Inventories(InvHistory plugin) {
        this.plugin = plugin;
    }

    private List<ItemStack> items = new ArrayList<>();


    public void getMainMenu() {
        for (String category : plugin.Deaths.getConfigurationSection("Inventories").getKeys(false)) {
            ItemStack item = new ItemStack(Material.TURTLE_EGG);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(Tools.getColorText("&e&l[&c " + category + " &e&l]"));
            item.setItemMeta(itemMeta);
            items.add(item);
        }


        LocalManager manager = new LocalManager();

        //List<Integer> glassPanes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 47, 49, 50, 51, 52);//24
        //List<Integer> availableSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);//28

        String name = manager.getLocalMessage("Main_Menu_Name");

        int totalInventories = 1;
        boolean done = false;


        while (!done){
            if ((items.size()) > (totalInventories * 28)){
                totalInventories++;
            }else {
                done = true;
            }
        }
        int pageNumer = 1;

        while(items.size() > 28){
            Inventory inv = getInventory(pageNumer, name);
            for(int i = 0; i < 28; i++){
                inv.addItem(items.get(0));
                items.remove(0);
            }
            plugin.generalDeaths.put(pageNumer, inv);
            pageNumer++;
        }

        if(items.size() > 0){
            Inventory inv = getInventory(pageNumer, name);

            for(ItemStack itemStack: items){
                inv.addItem(itemStack);
            }
            plugin.generalDeaths.put(pageNumer, inv);
        }



    }

    private Inventory getInventory(int i, String name) {
        List<Integer> glassPanes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 48, 49, 50, 51, 52);//24

        Inventory inv = Bukkit.createInventory(null, 54, Tools.getColorText(name + " " + i));

        ItemStack glassPane = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        assert glassMeta != null;
        glassMeta.setDisplayName(" ");
        glassPane.setItemMeta(glassMeta);
        glassPane.addUnsafeEnchantment(InvHistory.glowEnch, 1);


        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName(Tools.getColorText("&6&lNEXT"));
        next.setItemMeta(nextMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = next.getItemMeta();
        assert backMeta != null;
        backMeta.setDisplayName(Tools.getColorText("&6&lPREVIOUS"));
        back.setItemMeta(backMeta);

        Bukkit.createInventory(null, 54, name + i);
        for(int f: glassPanes){
            inv.setItem(f, glassPane);
        }
        inv.setItem(45, back);
        inv.setItem(53, next);

        return inv;
    }





}
