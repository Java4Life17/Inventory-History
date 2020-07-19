package invhistory.invhistory.Tools;

import invhistory.invhistory.InvHistory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoComplete implements TabCompleter {
    private InvHistory plugin;
    public AutoComplete(InvHistory plugin){this.plugin = plugin;}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> subCommands = Arrays.asList("all", "player", "reload");
        List<String> finalList = new ArrayList<>();
        List<String> sections = new ArrayList<>(plugin.Deaths.getConfigurationSection("Inventories").getKeys(false));

        if(command.getName().equalsIgnoreCase("invhistory")){

            if(args.length == 1){
                for(String s: subCommands){
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())){finalList.add(s);}
                }
                return finalList;
            }else if(args.length == 2){
                if (args[0].equalsIgnoreCase("player")) {

                    for (String s : sections){
                        if(s.toLowerCase().startsWith(args[1].toLowerCase())){finalList.add(s);}
                    }
                    return finalList;
                }
            }

        }

        return null;
    }
}
