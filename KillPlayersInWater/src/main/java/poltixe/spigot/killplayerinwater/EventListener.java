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
	//Get a list of all people that need to be killed
	ArrayList<String> peopleToKill = (ArrayList<String>) app.config.getStringList("peopleToKill");

	//Gets the block the player is standing on
        Material m = e.getPlayer().getLocation().getBlock().getType();

	//Checks if the block the player is standing on is water
        if (m == Material.WATER) {
	    //Checks if the list of people to kill contains the person that moved
            if (peopleToKill.contains(e.getPlayer().getName())) {
		//Creates a temporary variable to tell if the player is in the known state array already
                boolean playerInStateArray = false;

		//Loops through all player states
                for(PlayerState state : app.playerStates) {
		    //Checks if the current player state we are looping has the same name as the player that moved
                    if(state.playerName.equals(e.getPlayer().getName())) {
			//Sets a variable sayint that the player is in the state array
                        playerInStateArray = true;
			//Sets the global working playerState to the current state we are iterating on
                        playerState = state;
                    }
                }

		//Checks if the player is not in the player state array
                if(!playerInStateArray) {
		    //Creates a new PlayerState with the username and adds it to the PlayerState array
                    app.playerStates.add(new PlayerState(e.getPlayer().getName(), -1));

		    //Loops through all the known PlayerStates
                    for(PlayerState state : app.playerStates) {
			//Checks if the player state we are iterating on has the same name as the player that moved
                        if(state.playerName.equals(e.getPlayer().getName())) {
			    //Sets the global working PlayerState to the state we are currently iterating on
                            playerState = state;
                        }
                    }
                }

		//Checks if the players looping ID is -1, indicating that there is no loop currently happening
                if(playerState.checkId == -1) {
		    //Creates a new repeating task and sets the task ID of the playerState to the new repeating tasks ID
                    playerState.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {
                        public void run() {
			    //Gets the material they are standing on
                            Material m = e.getPlayer().getLocation().getBlock().getType();

			    //Checks if that material is water
                            if (m == Material.WATER) {
				//Damages the player the amount specified in the config
                                e.getPlayer().damage(app.config.getInt("waterDamageAmount"));
                            } else {
				//Cancells the task
                                app.getServer().getScheduler().cancelTask(playerState.checkId);
				//Resets the task ID
                            	playerState.checkId = -1;
			    }
                        }
                    }, 0, 10);
                }
            }
        }

	//Checks if it is currently raining or snowing
	if (e.getPlayer().getWorld().hasStorm()) {
	    //Loops through all online players
	    for(Player player : Bukkit.getOnlinePlayers()) {
		//Checks if the player that moved is in the array of people that should be damaged
		if(peopleToKill.contains(player.getName())) {
		    //Gets the highest Y position corrosponding to a block above them
		    int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());

		    //Checks if the block location is lower then the players location
		    if(blockLocation <= player.getLocation().getY()){
			//Sets a temporary variable to check if a player is in the global PlayerState array
			boolean playerInStateArray = false;

			//Loops through all known PlayerStates
			for(PlayerState state : app.playerStates) {
			    //Checks if the player name is equivaelent to the player state we are currently looping over
			    if(state.playerName.equals(player.getName())) {
				//Sets the variable saying that the player is in the player state array to true
				playerInStateArray = true;
				//Sets the current working PlayerState to the current playerstate we are iterating over
				playerState = state;
			    }
			}

			//Checks if the player is not in the PlayerState array
			if(!playerInStateArray) {
			    //Creates a new PlayerState with the users name and adds it to the array
			    app.playerStates.add(new PlayerState(player.getName(), -1));

			    //Loops through all known PlayerStates
			    for(PlayerState state : app.playerStates) {
				//Checks if the player name is equal to the name in the PlayerState we are currently iterating on
				if(state.playerName.equals(player.getName())) {
				    //Sets the global working PlayerState to the PlayerState we are currently working on
				    playerState = state;
				}
			    }
			}

			//Checks if the PlayerStates check ID is -1, indicating that the player currently has no loops running on them
			if(playerState.checkId == -1) {
			    //Creates a new repeating task, and sets the PlayerStates check ID to the ID passed by the scheduleSyncRepeatingTask
			    playerState.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {
				    public void run() {
					//Gets the y position of the highest block above the player
					int blockLocation = player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());

					//Checks if the players locaiton is above the blocks location
					if(blockLocation <= player.getLocation().getY()){
					    //Damage the player the amount specified in the config
					    player.damage(app.config.getInt("rainDamageAmount"));
					} else {
					    //Cancels the task
					    app.getServer().getScheduler().cancelTask(playerState.checkId);
					    //Sets the ID to -1, the ID for no current task running
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
