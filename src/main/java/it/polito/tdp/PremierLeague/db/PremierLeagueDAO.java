package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*public void caricaIdMapPlayer(Map<Integer, Player> idMapAllPlayer) {
		String sql = "SELECT p.PlayerID AS player_id, p.Name AS player_name " + 
					 "FROM players AS p, actions AS a " + 
					 "WHERE p.PlayerID = a.PlayerID " + 
					 "GROUP BY p.PlayerID, p.Name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Player player = new Player(res.getInt("player_id"), res.getString("player_name"));
				idMapAllPlayer.put(res.getInt("player_id"), player);
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			return ;
		}
	}*/
	
	public void getVertici(Double mediaGolPartita, Map<Integer, Player> idMapVertexPlayer) {
		String sql = "SELECT p.PlayerID AS player_id, p.Name AS player_name " + 
					 "FROM players AS p, actions AS a " + 
					 "WHERE p.PlayerID = a.PlayerID " + 
					 "GROUP BY p.PlayerID, p.Name " + 
					 "HAVING SUM(a.Goals)/COUNT(a.MatchID) > ?";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, mediaGolPartita);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Player player = new Player( res.getInt("player_id"), res.getString("player_name") );
				idMapVertexPlayer.put(res.getInt("player_id"), player);
			}
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ;
		}	
	}
	
	public List<Adiacenza> getAdiacenze(Map<Integer, Player> idMapVertexPlayer) {
		String sql = "SELECT a1.PlayerID as player_1, a2.PlayerID as player_2, (SUM(a1.TimePlayed)-SUM(a2.TimePlayed)) AS peso " + 
					 "FROM actions AS a1, actions AS a2 " + 
					 "WHERE a1.TeamID <> a2.TeamID  " + 
					 "AND a1.`Starts` = 1 AND a2.`Starts` = 1  " + 
					 "AND a1.MatchID = a2.MatchID " + 
					 "AND a1.PlayerID > a2.PlayerID " + 
					 "GROUP BY a1.PlayerID, a2.PlayerID";
		List<Adiacenza> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if( idMapVertexPlayer.containsKey(res.getInt("player_1")) && idMapVertexPlayer.containsKey(res.getInt("player_2")) ) {
					Adiacenza a = new Adiacenza( idMapVertexPlayer.get(res.getInt("player_1")),  idMapVertexPlayer.get(res.getInt("player_2")), res.getInt("peso"));
					result.add(a);
				}
				
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
