package invhistory.invhistory.Tools;

import invhistory.invhistory.InvHistory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryInventory {

    private InvHistory plugin;

    public CategoryInventory(InvHistory plugin) {
        this.plugin = plugin;
    }

    public Inventory getPlayerInventory(String configSection, String title) {

        Inventory inventory = Bukkit.createInventory(null, 54, Tools.getColorText(title));

        List<Integer> glassPanes = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 47, 48, 49, 50, 51, 52, 53);//24
        //43

        ItemStack fireWorkStar = new ItemStack(Material.HONEY_BOTTLE);
        ItemMeta fireWorkMeta = fireWorkStar.getItemMeta();
        assert fireWorkMeta != null;
        fireWorkMeta.setDisplayName(" ");
        fireWorkStar.setItemMeta(fireWorkMeta);
        fireWorkStar.addUnsafeEnchantment(InvHistory.glowEnch, 1);

        ItemStack delete = new ItemStack(Material.REDSTONE);
        ItemMeta deleteMeta = delete.getItemMeta();
        assert deleteMeta != null;
        deleteMeta.setDisplayName(" ");
        List<String> lore = new ArrayList<>();
        for (String textLine : plugin.Language.getStringList("DeleteAllButton")) {
            lore.add(Tools.getColorText(textLine));
        }
        deleteMeta.setLore(lore);
        delete.setItemMeta(deleteMeta);

        for (int i : glassPanes) {
            inventory.setItem(i, fireWorkStar);
        }
        inventory.setItem(43, delete);

        ItemStack back = new ItemStack(Material.HOPPER);
        ItemMeta backMeta = back.getItemMeta();
        assert backMeta != null;
        backMeta.setDisplayName(" ");
        List<String> backLore = new ArrayList<>();
        for(String line: plugin.Language.getStringList("BackToMainMenu")){
            backLore.add(Tools.getColorText(line));
        }
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);
        inventory.setItem(37, back);

        for (String death : plugin.Deaths.getConfigurationSection("Inventories." + configSection).getKeys(false)) {
            ItemStack stack = new ItemStack(Material.PAPER);
            ItemMeta stackMeta = stack.getItemMeta();
            assert stackMeta != null;
            stackMeta.setDisplayName(Tools.getColorText("&4Key&d - &7" + death));
            List<String> deathInfo = new ArrayList<>();
            deathInfo.add(" ");
            deathInfo.add(" ");
            deathInfo.add(Tools.getColorText("           &9&l        INFO    "));
            deathInfo.add(" ");
            deathInfo.add(Tools.getColorText("&6Time&7: &e" + plugin.Deaths.getString("Inventories." + configSection +
                    "." + death + ".Time")));
            deathInfo.add(" ");
            deathInfo.add(" ");
            deathInfo.add(Tools.getColorText("&6Cause&7: &e" + plugin.Deaths.getString("Inventories." + configSection +
                    "." + death + ".Cause")));
            deathInfo.add(" ");
            deathInfo.add(" ");
            deathInfo.add(Tools.getColorText("&6Location&7: &e" + plugin.Deaths.getString("Inventories." + configSection +
                    "." + death + ".Location")));
            deathInfo.add(" ");
            deathInfo.add(" ");
            deathInfo.add(Tools.getColorText("&6Player&7: &e" + configSection));
            stackMeta.setLore(deathInfo);
            stack.setItemMeta(stackMeta);

            if (inventory.firstEmpty() == -1) {
                break;
            } else {
                inventory.addItem(stack);
            }

        }

        return inventory;
    }

}
