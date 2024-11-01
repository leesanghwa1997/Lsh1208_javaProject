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

	public PokeInfo oneGach(UserInfo userInfo) {
		List<PokeInfo> pokeList = new ArrayList<>();
		String selectsql = "SELECT * FROM pokeinfo WHERE emergen = 'Y'";
		String updatesql = "UPDATE userinfo SET MONEY = MONEY-200 WHERE USERNUM = ?";
		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(selectsql);
				PreparedStatement updatePstmt = conn.prepareStatement(updatesql);) {

			updatePstmt.setInt(1, userInfo.getUserNum());
			updatePstmt.executeUpdate();

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int pokenum = rs.getInt("pokenum");
				String name = rs.getString("name");
				String type1 = rs.getString("type1");
				String type2 = rs.getString("type2");
				String rank = rs.getString("rank");

				pokeList.add(new PokeInfo(pokenum, name, type1, type2, rank, "y"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return selectPokeByRank(pokeList);
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
}
