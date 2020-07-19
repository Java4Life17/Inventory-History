package invhistory.invhistory;

import invhistory.invhistory.Tools.CategoryInventory;
import invhistory.invhistory.Tools.LocalManager;
import invhistory.invhistory.Tools.Tools;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor
{
    private InvHistory plugin;
    public Commands(InvHistory plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("invhistory")){

            LocalManager manager = new LocalManager();

            if(!player.hasPermission("invhistory.generalDeaths.admin")){
                Tools.sendColorMessage(player, manager.getLocalMessage("No_Perm"));
                return false;
            }
            try {

                if(args[0].equalsIgnoreCase("all")){
                    player.openInventory(plugin.generalDeaths.get(1));
                    Tools.playerPlaySound(player, Sound.ENTITY_WITCH_CELEBRATE, 1.8F);
                }else if(args[0].equalsIgnoreCase("player")){
                    String name = args[1];

                    if(!plugin.Deaths.getConfigurationSection("Inventories").contains(name)){
                        String message = manager.getLocalMessage("NoPlayerExists");
                        Tools.sendColorMessage(player, message);
                        return false;
                    }else{
                        CategoryInventory categoryInventory = new CategoryInventory(plugin);
                        String titleSent =  manager.getLocalMessage("Player_Menu_Name");
                        titleSent = titleSent.replace("%player%", name);
                        player.openInventory(categoryInventory.getPlayerInventory(name, Tools.getColorText(titleSent)));
                        Tools.playerPlaySound(player, Sound.ENTITY_ARROW_HIT, 0.1F);

                    }
                }
                else if(args[0].equalsIgnoreCase("reload")){
                    String reloaded = manager.getLocalMessage("ReloadComplete");
                    plugin.reloadEverything();
                    Tools.playerPlaySound(player, Sound.ENTITY_ARROW_HIT_PLAYER, 1.4F);
                    Tools.sendColorMessage(player, reloaded);
                }

            }catch (Exception e){
                Tools.sendColorMessage(player, "&c=====================================================");
                Tools.sendColorMessage(player, "&c/invhistory player <playerName>");
                Tools.sendColorMessage(player, "&c/invhistory all");
                Tools.sendColorMessage(player, "&c=====================================================");
            }

        }

        return false;
    }
}
