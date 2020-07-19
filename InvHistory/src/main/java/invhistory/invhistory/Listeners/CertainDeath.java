package invhistory.invhistory.Listeners;

import invhistory.invhistory.InvHistory;
import invhistory.invhistory.Options.GiveCertainItems;
import invhistory.invhistory.Tools.LocalManager;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import javax.swing.plaf.SplitPaneUI;
import javax.tools.Tool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CertainDeath implements Listener {

    private InvHistory plugin;

    public CertainDeath(InvHistory plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onCertainDeathMenu(InventoryClickEvent event) throws IOException, InvalidConfigurationException {
        String title = event.getView().getTitle();
        title = title.replace(Tools.getColorText("&5"), "").replace(Tools.getColorText("&0"), "").replace(Tools.getColorText("&1"), "");
        String[] titleArgs = title.split(" ");
        if (titleArgs.length < 3) {
            return;
        }
        if (titleArgs[1].equals("-")) {

            LocalManager manager = new LocalManager();
            Player p = (Player) event.getWhoClicked();
            event.setCancelled(true);
            Tools.playerPlaySound(p, Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.6F);

            String playerName = titleArgs[0].replace("&0", "");
            String key = titleArgs[2];

            if (event.getSlot() == 45) {

                if (!plugin.Deaths.getConfigurationSection("Inventories." + playerName).getKeys(false).contains(key)) {
                    p.closeInventory();
                    String message = manager.getLocalMessage("FailedToDelete");
                    Tools.sendColorMessage(p, message);
                    return;
                }

                plugin.Deaths.load();
                List<String> sizeList = new ArrayList<>(plugin.Deaths.getConfigurationSection("Inventories." + playerName).getKeys(false));
                int size = sizeList.size();
                if (size > 1) {
                    plugin.Deaths.set("Inventories." + playerName + "." + key, null);
                    plugin.Deaths.save();
                    plugin.Deaths.load();
                    p.closeInventory();
                    String message = manager.getLocalMessage("DeletedSingleInv");
                    Tools.sendColorMessage(p, message);
                } else {
                    plugin.Deaths.set("Inventories." + playerName, null);
                    plugin.Deaths.save();
                    plugin.reloadEverything();
                    p.closeInventory();
                    String message = manager.getLocalMessage("DeletedSingleInv");
                    Tools.sendColorMessage(p, message);
                }

            } else if (event.getSlot() == 46) {
                p.closeInventory();
                p.openInventory(plugin.generalDeaths.get(1));
            } else if (event.getSlot() == 52) {
                p.closeInventory();
                String location = plugin.Deaths.getString("Inventories." + playerName + "." + key + ".Location");
                // -190 68 -21 world
                String[] coords = location.split(" ");
                World world = Bukkit.getServer().getWorld(coords[3]);
                Location loc = new Location(world, Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));

                if (plugin.inTeleportDelay.contains(p)) {
                    String message = manager.getLocalMessage("AlreadyTeleporting");
                    Tools.sendColorMessage(p, message);
                    return;
                }
                plugin.inTeleportDelay.add(p);

                String subtitle = "";
                if (plugin.Configuration.getString("Local").equals("ES_MX")) {
                    subtitle = Tools.getColorText("&e...&7escribe &ccancel&7 para cancelar&e...");
                } else {
                    subtitle = Tools.getColorText("&e...&7type &ccancel &7to cancel&e...");
                }

                String finalSubtitle = subtitle;
                new BukkitRunnable() {
                    int delay = plugin.Configuration.getInt("Delay");

                    @Override
                    public void run() {
                        if (plugin.inTeleportDelay.contains(p)) {
                            if (delay <= 0) {
                                cancel();
                                String message = manager.getLocalMessage("Teleported");
                                Tools.playerPlaySound(p, Sound.ENTITY_GENERIC_EXPLODE, 1.0F);
                                Tools.sendColorMessage(p, message);
                                plugin.inTeleportDelay.remove(p);
                                p.teleport(loc);
                            } else {
                                p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&d&l" + delay), finalSubtitle, 0, 20, 20);
                                Tools.playerPlaySound(p, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 0.1F);
                                delay--;
                            }
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 25);
            } else if (event.getSlot() == 47) {
                Player player = plugin.getServer().getPlayer(playerName);
                if (player == null) {
                    String message = manager.getLocalMessage("PlayerOffline");
                    Tools.sendColorMessage(p, message);
                    p.closeInventory();
                } else {
                    GiveCertainItems giveCertainItems = new GiveCertainItems(plugin);
                    Tools.playerPlaySound(p, Sound.BLOCK_STONE_BREAK, 0.5F);
                    String inventoryKey = plugin.Deaths.getString("Inventories." + playerName + "." + key + ".Inventory");
                    p.openInventory(giveCertainItems.certainItemsInv(playerName, inventoryKey, key));
                }
            } else if (event.getSlot() == 49) {
                Player player = plugin.getServer().getPlayer(playerName);
                if (player == null) {
                    String message = manager.getLocalMessage("PlayerOffline");
                    Tools.sendColorMessage(p, message);
                    p.closeInventory();
                } else {
                    GiveCertainItems giveCertainItems = new GiveCertainItems(plugin);
                    Tools.playerPlaySound(player, Sound.BLOCK_STONE_BREAK, 0.5F);
                    String inventoryKey = plugin.Deaths.getString("Inventories." + playerName + "." + key + ".Inventory");
                    player.openInventory(giveCertainItems.certainItemsInv(playerName, inventoryKey, key));
                }
            } else if (event.getSlot() == 51) {

                Player player = plugin.getServer().getPlayer(playerName);
                if (player == null) {
                    String message = manager.getLocalMessage("PlayerOffline");
                    Tools.sendColorMessage(p, message);
                    p.closeInventory();
                } else {
                    p.closeInventory();
                    String message = manager.getLocalMessage("DisplayDone");
                    message = message.replace("%player%", playerName);
                    Tools.sendColorMessage(p, message);
                    Tools.playerPlaySound(p, Sound.ITEM_BOOK_PAGE_TURN, 0.8F);
                    Inventory inventory = Bukkit.createInventory(null, 54, Tools.getColorText("                &0&lDISPLAY       "));
                    String inventoryKey = plugin.Deaths.getString("Inventories." + playerName + "." + key + ".Inventory");
                    Inventory copy = Tools.fromBase64(inventoryKey, playerName, 1);
                    for (ItemStack stack : copy) {
                        if (stack != null) {
                            inventory.addItem(stack);
                        }
                    }

                    new BukkitRunnable() {
                        int delay = 5;

                        @Override
                        public void run() {
                            String warning = manager.getLocalMessage("DisplayWarning");
                            warning = warning.replace("%seconds%", Integer.toString(delay));

                            if (delay < 1) {
                                player.openInventory(inventory);
                                cancel();
                            } else {
                                Tools.playerPlaySound(player, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1.3F);
                                Tools.sendColorMessage(player, warning);
                            }
                            delay--;

                        }
                    }.runTaskTimer(plugin, 0, 20);


                }
            } else if (event.getSlot() == 53) {
                Player receiver = plugin.getServer().getPlayer(playerName);
                if (receiver == null) {
                    String message = manager.getLocalMessage("PlayerOffline");
                    Tools.sendColorMessage(p, message);
                    p.closeInventory();
                    return;
                }
                int slots = 36;
                int containsSomething = 0;

                for (ItemStack stack : receiver.getInventory()) {
                    if (stack != null) {
                        containsSomething++;
                    }
                }

                if (containsSomething != 0) {
                    String message = manager.getLocalMessage("NotEmpty");
                    Tools.sendColorMessage(p, message);
                    Tools.playerPlaySound(p, Sound.ENTITY_VILLAGER_NO, 1.2F);
                    p.closeInventory();
                    return;
                }

                if(receiver.getInventory().getHelmet() != null){
                    String message = manager.getLocalMessage("NotEmpty");
                    Tools.sendColorMessage(p, message);
                    Tools.playerPlaySound(p, Sound.ENTITY_VILLAGER_NO, 1.2F);
                    p.closeInventory();
                    return;
                }
                if(receiver.getInventory().getChestplate() != null){
                    String message = manager.getLocalMessage("NotEmpty");
                    Tools.sendColorMessage(p, message);
                    Tools.playerPlaySound(p, Sound.ENTITY_VILLAGER_NO, 1.2F);
                    p.closeInventory();
                    return;
                }
                if(receiver.getInventory().getLeggings() != null){
                    String message = manager.getLocalMessage("NotEmpty");
                    Tools.sendColorMessage(p, message);
                    Tools.playerPlaySound(p, Sound.ENTITY_VILLAGER_NO, 1.2F);
                    p.closeInventory();
                    return;
                }
                if(receiver.getInventory().getBoots() != null){
                    String message = manager.getLocalMessage("NotEmpty");
                    Tools.sendColorMessage(p, message);
                    Tools.playerPlaySound(p, Sound.ENTITY_VILLAGER_NO, 1.2F);
                    p.closeInventory();
                    return;
                }

                boolean helmet = false;
                ItemStack helmetItem = null;
                boolean chestPlate = false;
                ItemStack chestPlateItem = null;
                boolean leggings = false;
                ItemStack leggingsItem = null;
                boolean boots = false;
                ItemStack bootsItem = null;

                String inventoryKey = plugin.Deaths.getString("Inventories." + playerName + "." + key + ".Inventory");
                Inventory receiverCopy = Tools.fromBase64(inventoryKey, playerName, 1);

                for(int stack = 0; stack < receiverCopy.getSize(); stack++){
                    if(receiverCopy.getItem(stack) != null){
                        if(!helmet){
                            if(receiverCopy.getItem(stack) != null) {
                                if (Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.DIAMOND_HELMET) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.GOLDEN_HELMET) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.IRON_HELMET) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.LEATHER_HELMET) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.CHAINMAIL_HELMET)) {
                                    helmetItem = receiverCopy.getItem(stack);
                                    receiverCopy.setItem(stack, null);
                                    helmet = true;
                                }
                            }
                        }
                        if(!chestPlate){
                            if(receiverCopy.getItem(stack) != null) {
                                if (Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.DIAMOND_CHESTPLATE) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.GOLDEN_CHESTPLATE) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.IRON_CHESTPLATE) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.LEATHER_CHESTPLATE) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
                                    chestPlateItem = receiverCopy.getItem(stack);
                                    receiverCopy.setItem(stack, null);
                                    chestPlate = true;
                                }
                            }
                        }
                        if(!leggings){
                            if(receiverCopy.getItem(stack) != null) {
                                if (Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.DIAMOND_LEGGINGS) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.GOLDEN_LEGGINGS) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.IRON_LEGGINGS) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.LEATHER_LEGGINGS) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.CHAINMAIL_LEGGINGS)) {
                                    leggingsItem = receiverCopy.getItem(stack);
                                    receiverCopy.setItem(stack, null);
                                    leggings = true;
                                }
                            }
                        }
                        if(!boots){
                            if(receiverCopy.getItem(stack) != null) {
                                if (Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.DIAMOND_BOOTS) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.GOLDEN_BOOTS) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.IRON_BOOTS) || Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.LEATHER_BOOTS) ||
                                        Objects.requireNonNull(receiverCopy.getItem(stack)).getType().equals(Material.CHAINMAIL_BOOTS)) {
                                    bootsItem = receiverCopy.getItem(stack);
                                    receiverCopy.setItem(stack, null);
                                    boots = true;
                                }
                            }
                        }
                    }
                }

                receiver.getInventory().setBoots(bootsItem);
                receiver.getInventory().setChestplate(chestPlateItem);
                receiver.getInventory().setLeggings(leggingsItem);
                receiver.getInventory().setHelmet(helmetItem);

                for(ItemStack stack: receiverCopy){
                    if(stack != null){
                        receiver.getInventory().addItem(stack);
                    }
                }

                if(plugin.Deaths.getConfigurationSection("Inventories." + playerName).getKeys(false).size() > 1){
                    plugin.Deaths.set("Inventories." + playerName + "." + key, null);
                    plugin.Deaths.save();
                }else{
                    plugin.Deaths.set("Inventories." + playerName, null);
                    plugin.Deaths.save();
                    plugin.reloadEverything();
                }

                String message = manager.getLocalMessage("ReturnedItems").replace("%key%", key).replace("%receiver%", playerName);;
                Tools.playerPlaySound(p, Sound.BLOCK_NOTE_BLOCK_BELL, 1.5F);
                Tools.sendColorMessage(p, message);
                p.closeInventory();


            }
        }
    }

    @EventHandler
    public void cancelTeleport(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().equalsIgnoreCase("cancel")) {
            if (plugin.inTeleportDelay.contains(player)) {
                plugin.inTeleportDelay.remove(player);
                LocalManager manager = new LocalManager();
                String message = manager.getLocalMessage("TeleportCanceled");
                Tools.sendColorMessage(player, message);
                Tools.playerPlaySound(player, Sound.BLOCK_ANVIL_FALL, 1.3F);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisplayCopy(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Tools.getColorText("                &0&lDISPLAY       "))) {
            event.setCancelled(true);
        }
    }

}
