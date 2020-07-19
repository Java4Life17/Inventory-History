package invhistory.invhistory;

import invhistory.invhistory.Listeners.*;
import invhistory.invhistory.Tools.AutoComplete;
import invhistory.invhistory.Tools.Inventories;
import invhistory.invhistory.Tools.LocalManager;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class InvHistory extends JavaPlugin {
    public YamlFile Configuration;
    public YamlFile Language;
    public YamlFile Deaths;
    public HashMap<Integer, Inventory> generalDeaths = new HashMap<>();
    public Inventories inventories = new Inventories(this);
    private static InvHistory plugin;
    public static GlowEnch glowEnch;
    public static ArrayList<Enchantment> custom_enchants = new ArrayList<>();
    public String title;
    public List<Player> inTeleportDelay = new ArrayList<>();


    @Override
    public void onEnable() {
        plugin = this;
        glowEnch = new GlowEnch("glowingEffect");
        registerEnchantment(glowEnch);
        custom_enchants.add(glowEnch);

        Configuration = new YamlFile("plugins/InvHistory/Configuration.yml");
        Deaths = new YamlFile("plugins/InvHistory/Deaths.yml");

        loadFiles();

        String language = Configuration.getString("Local");
        if (language.equalsIgnoreCase("EN_US")) {
            Language = new YamlFile("plugins/InvHistory/EN_US.yml");
        } else if (language.equalsIgnoreCase("ES_MX")) {
            Language = new YamlFile("plugins/InvHistory/ES_MX.yml");
        } else {
            Language = new YamlFile("plugins/InvHistory/EN_US.yml");
        }
        try {
            loadLocalFile(language);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        LocalManager manager = new LocalManager();
        title = manager.getLocalMessage("Main_Menu_Name");
        Objects.requireNonNull(getCommand("invhistory")).setExecutor(new Commands(this));

        getServer().getPluginManager().registerEvents(new deathListener(this), this);
        getServer().getPluginManager().registerEvents(new mainInvClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCategoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new CertainDeath(this), this);
        getServer().getPluginManager().registerEvents(new GiveCertainItemsListener(this), this);
        getServer().getPluginManager().registerEvents(new exitInventorySave(this), this);
        Objects.requireNonNull(getCommand("invhistory")).setTabCompleter(new AutoComplete(plugin));
        inventories.getMainMenu();

    }
    public static InvHistory getPlugin() {
        return plugin;
    }


    private void loadFiles() {
        try {
            if (!Configuration.exists()) {
                this.saveResource("Configuration.yml", true);
            }
            Configuration.load();
            if (!Deaths.exists()) {
                this.saveResource("Deaths.yml", false);
            }
            Deaths.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLocalFile(String local) throws InvalidConfigurationException, IOException {
        if (!Language.exists()) {
            if (local.equalsIgnoreCase("EN_US")) {
                this.saveResource("EN_US.yml", true);
            } else if (local.equalsIgnoreCase("ES_MX")) {
                this.saveResource("ES_MX.yml", true);
            } else {
                this.saveResource("EN_US.yml", true);
            }
        }
        Language.load();
    }



    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            doNothing();
        }
    }

    private static void doNothing() {
    }

    @Override
    public void onDisable() {
        // Disable the Power enchantment
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            for (Enchantment enchantment : custom_enchants) {
                if (byKey.containsKey(enchantment.getKey())) {
                    byKey.remove(enchantment.getKey());
                }
            }

            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            for (Enchantment enchantment : custom_enchants) {
                if (byName.containsKey(enchantment.getName())) {
                    byName.remove(enchantment.getName());
                }
            }
        } catch (Exception ignored) {
        }


    }

    public void reloadEverything() throws IOException, InvalidConfigurationException {
        plugin.Deaths.load();

        generalDeaths.clear();
        Inventories inventories = new Inventories(this);


        Configuration.load();

        String language = Configuration.getString("Local");
        if (language.equalsIgnoreCase("EN_US")) {
            Language = new YamlFile("plugins/InvHistory/EN_US.yml");
        } else if (language.equalsIgnoreCase("ES_MX")) {
            Language = new YamlFile("plugins/InvHistory/ES_MX.yml");
        } else {
            Language = new YamlFile("plugins/InvHistory/EN_US.yml");
        }
        try {
            loadLocalFile(language);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        LocalManager manager = new LocalManager();

        title = manager.getLocalMessage("Main_Menu_Name");
        inventories.getMainMenu();

    }


}
