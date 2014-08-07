package org.samson.bukkit.plugins.regionboard.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.samson.bukkit.plugins.regionboard.region.Region;

public class ScoreboardController {
	
	private static final String TIMER_ENTRY = "&6Time Left";
	private Map<String, Scoreboard> regionalScoreboardMap = new HashMap<String, Scoreboard>();
	
	public void displayObjectiveForPlayer(Player player, String regionId, String displayName) {
		
		if (player.isOnline()) {
			
			Scoreboard scoreboard = getOrCreateRegionalScoreboard(regionId);
			Objective objective = getOrCreateObjective(scoreboard, player, displayName);
			
			player.setScoreboard(scoreboard);
			
	        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			
		}
		
	}

	private static Objective getOrCreateObjective(Scoreboard scoreboard, Player player, String displayName) {
		
		Objective objective = scoreboard.getObjective("regional");
		
		if (objective == null) {
		
			objective = scoreboard.registerNewObjective("regional", "dummy");
			objective.setDisplayName(getFormattedScoreboardDisplayName(displayName));
			
		}

		return objective;
		
	}

	private Scoreboard getOrCreateRegionalScoreboard(String regionId) {
		
		Scoreboard scoreboard;
		
		if (! regionalScoreboardMap.containsKey(regionId)) {
			
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			regionalScoreboardMap.put(regionId, scoreboard);
			
		} else {
			
			scoreboard = regionalScoreboardMap.get(regionId);
			
		}
		
		return scoreboard;
		
	}

	public void displayMainScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		player.setScoreboard(scoreboard);
	}

	public void updateScoreForPlayer(Player player, Region firstRegion, int incr) {
		
		Scoreboard scoreboard = getOrCreateRegionalScoreboard(firstRegion.getRegionId());
		
		Objective objective = getOrCreateObjective(scoreboard, player, firstRegion.getScoreboardDisplayName());
		
		Score score = objective.getScore(player.getName());
		score.setScore(score.getScore() + incr);
		
		
	}

	public void resetScoreboard(String regionId) {
		
		if (regionalScoreboardMap.containsKey(regionId)) {
			
			Scoreboard scoreboard = regionalScoreboardMap.get(regionId);
			
			Set<String> entries = scoreboard.getEntries();
			for (String entry : entries) {
				scoreboard.resetScores(entry);
			}
			
		}
		
	}

	public void removeScoreboardForRegion(String regionId) {
		
		if (regionalScoreboardMap.containsKey(regionId)) {
			Scoreboard scoreboard = regionalScoreboardMap.get(regionId);
			scoreboard.clearSlot(DisplaySlot.SIDEBAR);
			regionalScoreboardMap.remove(regionId);
		}
		
	}

	public List<String> getTopEntries(String regionId) {
		
		List<String> scores = new ArrayList<String>();
		
		if (regionalScoreboardMap.containsKey(regionId)) {
			
			Scoreboard scoreboard = regionalScoreboardMap.get(regionId);
			
			Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
			
			Set<String> entries = scoreboard.getEntries();
			
			int maxScore = 0;
			
			for (String entry : entries) {
				
				int score = objective.getScore(entry).getScore();
				
				if (score == maxScore) {
					scores.add(entry);
				}
				else if (score > maxScore) {
					maxScore = score;
					scores.clear();
					scores.add(entry);
				}
				
			}
			
		}
		
		return scores;
		
	}
	
	public static String getFormattedScoreboardDisplayName(String displayName) {
		String formattedDisplayName = StringUtils.replaceChars(displayName, '_', ' ');
		return ChatColor.translateAlternateColorCodes('&', formattedDisplayName);
	}

	public void updateScoreboardTimer(String regionId, int remainingTicks) {
		Scoreboard scoreboard = getOrCreateRegionalScoreboard(regionId);
		Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		if (objective != null) {
			objective.getScore(getTimerEntry()).setScore(-1 * remainingTicks);
		}
	}
	
	private static String getTimerEntry() {
		return ChatColor.translateAlternateColorCodes('&', TIMER_ENTRY);
	}
	

}
