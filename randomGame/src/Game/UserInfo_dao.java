package Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserInfo_dao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String userid = "scott";
	private String passwd = "tiger";

	public UserInfo_dao() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public UserInfo getUserInfo(String userId, String password) {
		UserInfo userInfo = null;
		String sql = "SELECT * FROM userinfo WHERE id = ? AND password = ?";

		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				// 쿼리 결과를 바탕으로 UserInfo 객체 생성
				int userNum = rs.getInt("usernum"); // 데이터베이스에서 사용자 번호 가져오기
				String name = rs.getString("name"); // 이름 가져오기
				int money = rs.getInt("money"); // 잔액 가져오기

				userInfo = new UserInfo(userNum, userId, password, name, money);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInfo; // 로그인 성공 시 UserInfo 객체 반환
	}

	public boolean addUser(UserInfo user) {
		String sql = "INSERT INTO userinfo (usernum, id, password, name) VALUES (userinfo_seq.NEXTVAL, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
