package invhistory.invhistory.Tools;

import invhistory.invhistory.InvHistory;
import org.bukkit.event.Listener;

public class LocalManager implements Listener {

    private InvHistory plugin;

    public String getLocalMessage(String section){
        plugin = InvHistory.getPlugin();
        return plugin.Language.getString(section);
    }

}
