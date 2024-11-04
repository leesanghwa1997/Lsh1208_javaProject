package Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class Gacha_dao {
	private static final Map<String, Double> RANK_PROBABILITIES = new HashMap<>();

	static {
		RANK_PROBABILITIES.put("SS", 0.002); // 0.2%
		RANK_PROBABILITIES.put("S", 0.018); // 1.8%
		RANK_PROBABILITIES.put("A", 0.08); // 8%
		RANK_PROBABILITIES.put("B", 0.2); // 20%
		RANK_PROBABILITIES.put("C", 0.7); // 70%
	}
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "scott";
	String passwd = "tiger";

	public Gacha_dao() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<PokeInfo> gacha(UserInfo userInfo, int numberOfDraws, int cost) {
		List<PokeInfo> pokeList = new ArrayList<>();

		// Check if user has enough money
		if (userInfo.getMoney() < cost) {
			JOptionPane.showMessageDialog(null, "소지금이 부족합니다.");
			return pokeList;
		}

		// Deduct the cost and update user money in the database
		userInfo.setMoney(userInfo.getMoney() - cost);
		updateUserMoney(userInfo.getUserNum(), userInfo.getMoney());

		// SQL to select Pokémon eligible for gacha
		String selectsql = "SELECT * FROM pokeinfo WHERE emergen = 'Y'";

		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(selectsql)) {

			ResultSet rs = pstmt.executeQuery();

			// Populate the list of available Pokémon for selection
			List<PokeInfo> availablePokemon = new ArrayList<>();
			while (rs.next()) {
				int pokenum = rs.getInt("pokenum");
				String name = rs.getString("name");
				String type1 = rs.getString("type1");
				String type2 = rs.getString("type2");
				String rank = rs.getString("rank");
				availablePokemon.add(new PokeInfo(pokenum, name, type1, type2, rank, "y"));
			}

			// Select the specified number of Pokémon based on rank
			for (int i = 0; i < numberOfDraws; i++) {
				pokeList.add(selectPokeByRank(availablePokemon));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pokeList;
	}

	// Method to update the user's money in the database
	private void updateUserMoney(int userNum, int newMoney) {
		String updatesql = "UPDATE userinfo SET MONEY = ? WHERE USERNUM = ?";
		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(updatesql)) {
			pstmt.setInt(1, newMoney);
			pstmt.setInt(2, userNum);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PokeInfo selectPokeByRank(List<PokeInfo> pokeList) {
		Random random = new Random();
		double randNum = random.nextDouble(); // 0.0 이상 1.0 미만의 랜덤 수 생성

		double cumulativeProbability = 0.0;

		for (String rank : RANK_PROBABILITIES.keySet()) {
			cumulativeProbability += RANK_PROBABILITIES.get(rank);

			if (randNum < cumulativeProbability) {
				// 해당 등급의 포켓몬 중 랜덤으로 선택
				List<PokeInfo> filteredPokes = pokeList.stream().filter(p -> p.getRank().equals(rank)) // 현재 등급의 포켓몬 필터링
						.collect(Collectors.toList());

				if (!filteredPokes.isEmpty()) {
					return filteredPokes.get(random.nextInt(filteredPokes.size())); // 랜덤으로 포켓몬 선택
				}
			}
		}
		return null; // 아무 포켓몬도 선택되지 않았을 경우 null 반환
	}
	
	public void saveToEncyclopedia(int usernum, int pokenum) {
	    // 먼저 ENCYCLOPEDIA 테이블에서 존재하는지 확인
	    String checkSql = "SELECT pokecount FROM encyclopedia WHERE usernum = ? AND pokenum = ?";
	    
	    try (Connection conn = DriverManager.getConnection(url, userid, passwd);
	         PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
	         
	        checkPstmt.setInt(1, usernum);
	        checkPstmt.setInt(2, pokenum);
	        
	        ResultSet rs = checkPstmt.executeQuery();

	        if (rs.next()) {
	            // 데이터가 이미 존재하는 경우
	            int currentCount = rs.getInt("pokecount");
	            updatePokeCount(usernum, pokenum, currentCount + 1); // pokecount 증가
	        } else {
	            // 데이터가 존재하지 않는 경우
	            insertPoke(usernum, pokenum); // 새로운 데이터 삽입
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// 포켓몬 수량을 업데이트하는 메서드
	private void updatePokeCount(int usernum, int pokenum, int newCount) {
	    String updateSql = "UPDATE encyclopedia SET pokecount = ? WHERE usernum = ? AND pokenum = ?";
	    try (Connection conn = DriverManager.getConnection(url, userid, passwd);
	         PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
	        pstmt.setInt(1, newCount);
	        pstmt.setInt(2, usernum);
	        pstmt.setInt(3, pokenum);
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// 새로운 포켓몬 데이터를 삽입하는 메서드
	private void insertPoke(int usernum, int pokenum) {
	    String insertSql = "INSERT INTO encyclopedia (usernum, pokenum, pokecount) VALUES (?, ?, ?)";
	    try (Connection conn = DriverManager.getConnection(url, userid, passwd);
	         PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
	        pstmt.setInt(1, usernum);
	        pstmt.setInt(2, pokenum);
	        pstmt.setInt(3, 1); // 새로 삽입할 때 pokecount는 1
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
