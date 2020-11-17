package poltixe.spigot.killplayerinwater;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.entity.*;

public class PlayerState {
    public Player player;
    private int checkId;

    PlayerState(Player player) {
        this.player = player;
        this.checkId = -1;
    }

    // Get an instance of the plugin
    private static App app = App.getPlugin(App.class);

    public static boolean checkPlayerInGlobalPlayerStateArray(Player player) {
        // Creates a temporary variable to tell if the player is in the known state
        // array already
        boolean playerInStateArray = false;

        // Loops through all player states
        for (PlayerState state : app.playerStates) {
            // Checks if the current player state we are looping has the same name as the
            // player that moved
            if (state.player.getName().equals(player.getName())) {
                // Sets a variable sayint that the player is in the state array
                playerInStateArray = true;
            }
        }

        // Checks if the player is not in the player state array
        if (!playerInStateArray) {
            // Creates a new PlayerState with the username and adds it to the PlayerState
            // array
            app.playerStates.add(new PlayerState(player));
        }

        return playerInStateArray;
    }

    public static PlayerState getPlayerStateFromGlobal(Player player) {
        PlayerState returnState = null;

        checkPlayerInGlobalPlayerStateArray(player);

        // Loops through all player states
        for (PlayerState state : app.playerStates) {
            // Checks if the current player state we are looping has the same name as the
            // player that moved
            if (state.player.getName().equals(player.getName())) {
                // Sets a variable sayint that the player is in the state array
                returnState = state;
            }
        }

        return returnState;
    }

    public void checkPlayerConditions() {
        // Get a list of all people that need to be killed
        ArrayList<String> peopleToKill = (ArrayList<String>) app.config.getStringList("peopleToKill");

        // Checks if the list of people to kill contains the person
        if (peopleToKill.contains(this.player.getName())) {
            // Gets the block the player is standing on
            Material m = this.player.getLocation().getBlock().getType();

            // Checks if the block the player is standing on is water
            if (m == Material.WATER) {
                // Checks if the players looping ID is -1, indicating that there is no loop
                // currently happening
                if (this.checkId == -1) {
                    // Creates a new repeating task and sets the task ID of the playerState to the
                    // new repeating tasks ID
                    this.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {
                        public void run() {
                            // Gets the material they are standing on
                            Material m = player.getLocation().getBlock().getType();

                            // Checks if that material is water
                            if (m == Material.WATER) {
                                // Damages the player the amount specified in the config
                                // Bukkit.broadcastMessage(e.getPlayer().getName() + " is in the water!");
                                player.damage(app.config.getInt("waterDamageAmount"));
                            } else {
                                // Cancells the task
                                app.getServer().getScheduler().cancelTask(checkId);
                                // Resets the task ID
                                checkId = -1;
                            }
                        }
                    }, 0, 10);
                }
            }

            // Checks if it is currently raining or snowing
            if (this.player.getWorld().hasStorm()) {
                // Gets the highest Y position corrosponding to a block above them
                int blockLocation = this.player.getLocation().getWorld().getHighestBlockYAt(player.getLocation());

                // Checks if the block location is lower then the players location
                if (blockLocation <= this.player.getLocation().getY()) {
                    // Checks if the PlayerStates check ID is -1, indicating that the player
                    // currently has no loops running on them
                    if (this.checkId == -1) {
                        // Creates a new repeating task, and sets the PlayerStates check ID to the ID
                        // passed by the scheduleSyncRepeatingTask
                        this.checkId = app.getServer().getScheduler().scheduleSyncRepeatingTask(app, new Runnable() {

                            public void run() {
                                // Gets the y position of the highest block above the player
                                int blockLocation = player.getLocation().getWorld()
                                        .getHighestBlockYAt(player.getLocation());

                                // Checks if the players locaiton is above the blocks location
                                if (blockLocation <= player.getLocation().getY() && player.getWorld().hasStorm()) {
                                    // Damage the player the amount specified in the config
                                    // Bukkit.broadcastMessage(player.getName() + " is in the rain!");
                                    player.damage(app.config.getInt("rainDamageAmount"));
                                } else {
                                    // Cancels the task
                                    app.getServer().getScheduler().cancelTask(checkId);
                                    // Sets the ID to -1, the ID for no current task running
                                    checkId = -1;
                                }
                            }
                        }, 0, 10);
                    }
                }
            }
        }
    }
}
