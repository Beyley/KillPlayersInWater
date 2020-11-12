package poltixe.spigot.killplayerinwater;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.*;
import org.bukkit.Material;
import org.bukkit.Material.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import net.md_5.bungee.api.ChatColor;
import java.util.ArrayList;

//The event listener
public class EventListener implements Listener {
    // Get an instance of the plugin
    private App app = App.getPlugin(App.class);

    public int checkId;

    // Gets the configuration file
    FileConfiguration config = app.getConfig();
    // Creates a new random object
    static Random r = new Random();

    PlayerState playerState = new PlayerState("notrealplayeruwu uwu uwu", -2);

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
	//Checks if it is raining
	if (e.toWeatherState()) {
	    //Loops through all online players
	    for(Player player : Bukkit.getOnlinePlayers()) {
		ArrayList<String> peopleToKill = (ArrayList<String>) app.config.getStringList("peopleToKill");

		if(peopleToKill.contains(player.getName())) {
		    int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());
	    
		    if(blockLocation <= player.getLocation().getY()){
			boolean playerInStateArray = false;
                
			for(PlayerState state : app.playerStates) {
			    if(state.playerName.equals(player.getName())) {
				playerInStateArray = true;
				playerState = state;
			    }
			}
    
			if(!playerInStateArray) {
			    app.playerStates.add(new PlayerState(player.getName(), -1));
			    //playerState = new PlayerState(e.getPlayer().getName(), -1);
    
			    for(PlayerState state : app.playerStates) {
				if(state.playerName.equals(player.getName())) {
				    playerState = state;
				}
			    }
			}
    
			if(playerState.checkId == -1) {
			    playerState.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {
				    public void run() {
					int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());
	    
					if(blockLocation <= player.getLocation().getY()){
					    player.damage(app.config.getInt("rainDamageAmount"));
					} else {
					    app.getServer().getScheduler().cancelTask(playerState.checkId);
					    playerState.checkId = -1;
					}
				    }
				}, 0, 10);
			}
		    }
		}
	    }
	}
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Material m = e.getPlayer().getLocation().getBlock().getType();

        if (m == Material.WATER) {
            ArrayList<String> peopleToKill = (ArrayList<String>) app.config.getStringList("peopleToKill");
    
            if (peopleToKill.contains(e.getPlayer().getName())) {
                //Bukkit.broadcastMessage(e.getPlayer() + " is in water and on *the list*!");
    
                boolean playerInStateArray = false;
                
                for(PlayerState state : app.playerStates) {
                    if(state.playerName.equals(e.getPlayer().getName())) {
                        playerInStateArray = true;
                        playerState = state;
                    }
                }
    
                if(!playerInStateArray) {
                    app.playerStates.add(new PlayerState(e.getPlayer().getName(), -1));
                    //playerState = new PlayerState(e.getPlayer().getName(), -1);
    
                    for(PlayerState state : app.playerStates) {
                        if(state.playerName.equals(e.getPlayer().getName())) {
                            playerState = state;
                        }
                    }
                }
    
                if(playerState.checkId == -1) {
                    playerState.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {
                        public void run() {
                            Material m = e.getPlayer().getLocation().getBlock().getType();

                            if (m == Material.WATER) {
                                e.getPlayer().damage(app.config.getInt("waterDamageAmount"));
                            } else {
                                app.getServer().getScheduler().cancelTask(playerState.checkId);
                            	playerState.checkId = -1;
			    }
                        }
                    }, 0, 10);
                }
            }
        }

	if (e.getPlayer().getWorld().hasStorm()) {
	    //Loops through all online players
	    for(Player player : Bukkit.getOnlinePlayers()) {
		ArrayList<String> peopleToKill = (ArrayList<String>) app.config.getStringList("peopleToKill");

		if(peopleToKill.contains(player.getName())) {
		    int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());
	    
		    if(blockLocation <= player.getLocation().getY()){
			boolean playerInStateArray = false;
                
			for(PlayerState state : app.playerStates) {
			    if(state.playerName.equals(player.getName())) {
				playerInStateArray = true;
				playerState = state;
			    }
			}
    
			if(!playerInStateArray) {
			    app.playerStates.add(new PlayerState(player.getName(), -1));
			    //playerState = new PlayerState(e.getPlayer().getName(), -1);
    
			    for(PlayerState state : app.playerStates) {
				if(state.playerName.equals(player.getName())) {
				    playerState = state;
				}
			    }
			}
    
			if(playerState.checkId == -1) {
			    playerState.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {
				    public void run() {
					int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());
	    
					if(blockLocation <= player.getLocation().getY()){
					    player.damage(app.config.getInt("rainDamageAmount"));
					} else {
					    app.getServer().getScheduler().cancelTask(playerState.checkId);
					    playerState.checkId = -1;
					}
				    }
				}, 0, 10);
			}
		    }
		}
	    }
	}
    }
}
