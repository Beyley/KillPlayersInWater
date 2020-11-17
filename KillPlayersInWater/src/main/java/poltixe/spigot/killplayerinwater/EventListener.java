package poltixe.spigot.killplayerinwater;

import java.util.Random;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.Material;
import java.util.ArrayList;

//The event listener
public class EventListener implements Listener {
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		for (PlayerState state : App.getPlugin(App.class).playerStates) {
			state.checkPlayerConditions();
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		PlayerState.getPlayerStateFromGlobal(e.getPlayer()).checkPlayerConditions();
	}
}
