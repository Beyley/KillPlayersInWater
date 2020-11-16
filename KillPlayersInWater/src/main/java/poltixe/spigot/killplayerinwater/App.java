package poltixe.spigot.killplayerinwater;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class App extends JavaPlugin {
	// Get the config file
	public FileConfiguration config = getConfig();

	public List<PlayerState> playerStates = new ArrayList<PlayerState>();

	// Run when the plugin is enabled
	@Override
	public void onEnable() {
		List<String> peoples = new ArrayList<String>();

		// Add default people to array
		peoples.add("Person1");
		peoples.add("Person2");
		peoples.add("Person3");
		peoples.add("Person4");
		peoples.add("Person5");
		peoples.add("Person6");
		peoples.add("Person7");
		peoples.add("Person8");

		// Setup default config
		config.addDefault("peopleToKill", peoples);
		config.addDefault("waterDamageAmount", 2);
		config.addDefault("rainDamageAmount", 1);
		config.options().copyDefaults(true);
		saveConfig();

		// Registers an event listener
		getServer().getPluginManager().registerEvents(new EventListener(), this);
	}
}
