package Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokeEncyclopedia_dao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String userid = "scott";
	private String passwd = "tiger";

	public PokeEncyclopedia_dao() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userid, passwd);
	}

	//포켓몬 정보 검색
	public PokeInfo getPokemonByPokenum(int usernum, int pokenum) {
		PokeInfo pokeInfo = null;
		String sql = "SELECT b.pokenum, b.name, b.type1, b.type2, b.rank "
				+ "FROM userinfo a, pokeinfo b, encyclopedia c "
				+ "WHERE c.usernum = ? AND b.pokenum = c.pokenum AND b.pokenum = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, usernum);
			pstmt.setInt(2, pokenum);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					pokeInfo = new PokeInfo(rs.getInt("pokenum"), rs.getString("name"), rs.getString("type1"),
							rs.getString("type2"), rs.getString("rank"), "");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pokeInfo;
	}

	//도감 페이지 검색
	public List<PokeInfo> getPokemons(int usernum, int page) {
		List<PokeInfo> pokemons = new ArrayList<>();
		int offset = (page - 1) * 16;

		String sql = "SELECT * FROM ( " + "SELECT b.pokenum, b.name, b.type1, b.type2, " + "ROWNUM AS rn "
				+ "FROM userinfo a, pokeinfo b, encyclopedia c " + "WHERE c.usernum = ? AND a.usernum = c.usernum "
				+ "AND b.pokenum = c.pokenum " + "ORDER BY b.pokenum) " + "WHERE rn > ? AND rn <= ?";

		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, usernum);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, offset + 16);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int pokenum = rs.getInt("pokenum");
				String name = rs.getString("name");
				String type1 = rs.getString("type1");
				String type2 = rs.getString("type2");
				pokemons.add(new PokeInfo(pokenum, name, type1, type2, null, null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pokemons;
	}

	//
	public int getTotalPages(int usernum) {
		int totalPokemons = 0;

		String sql = "SELECT COUNT(*) FROM userinfo a, encyclopedia c "
				+ "WHERE c.usernum = ? AND a.usernum = c.usernum";

		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, usernum);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				totalPokemons = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (int) Math.ceil(totalPokemons / 16.0);
	}
}
