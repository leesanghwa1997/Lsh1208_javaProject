package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gacha extends JFrame {

	private JLabel moneyLabel; // 소지금 라벨
	private JPanel resultPanel; // 뽑기 결과를 보여줄 패널
	private UserInfo userInfo; // UserInfo 객체를 저장할 변수
	private Gacha_dao gachaDao; // Gacha_dao 객체

	public Gacha(UserInfo userInfo) {
		this.userInfo = userInfo; // 전달받은 사용자 정보 저장
		this.gachaDao = new Gacha_dao();
		setTitle("뽑기");
		setSize(500, 300);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 소지금 라벨 상단 우측에 배치
		moneyLabel = new JLabel("소지금: " + userInfo.getMoney()); // 사용자 소지금 표시
		moneyLabel.setHorizontalAlignment(JLabel.RIGHT);
		add(moneyLabel, BorderLayout.NORTH);

		// 중앙 패널에 뽑기 결과 표시
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(1, 5)); // 1행 5열의 그리드 레이아웃
		add(resultPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton oneTimeButton = new JButton("1회 뽑기");
		JButton fiveTimesButton = new JButton("5회 뽑기");
		buttonPanel.add(oneTimeButton);
		buttonPanel.add(fiveTimesButton);
		add(buttonPanel, BorderLayout.SOUTH);

		oneTimeButton.addActionListener(e -> oneGacha(userInfo));
		fiveTimesButton.addActionListener(e -> fiveGacha());

		setVisible(true);
	}

	public void oneGacha(UserInfo userInfo) {
		PokeInfo selectedPoke = gachaDao.oneGach(userInfo);

		// 결과 패널 초기화
		resultPanel.removeAll();
		if (selectedPoke != null) {
			// 포켓몬 이미지와 이름 표시
			int pokenum = selectedPoke.getPokenum();
			String imagePath = "/Game/imges/" + pokenum + ".png"; // 이미지 경로 생성
			ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
			Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 이미지 크기 조절

			// 포켓몬 이름과 이미지를 표시할 패널 생성
			JPanel pokePanel = new JPanel();
			pokePanel.setLayout(new BoxLayout(pokePanel, BoxLayout.Y_AXIS)); // 수직 방향으로 배치

			// 이미지 추가
			JLabel pokeImageLabel = new JLabel(new ImageIcon(image));
			pokeImageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // 중앙 정렬
			pokePanel.add(pokeImageLabel); // 이미지 추가

			// 포켓몬 이름 추가
			JLabel pokeNameLabel = new JLabel(selectedPoke.getName(), JLabel.CENTER);
			pokeNameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // 중앙 정렬
			pokePanel.add(pokeNameLabel); // 이름 추가

			// 패널의 크기를 조정하여 간격 조절
			pokePanel.setPreferredSize(new Dimension(100, 120)); // 적절한 높이로 조정

			resultPanel.add(pokePanel); // 결과 패널에 추가
		} else {
			// 포켓몬이 선택되지 않았을 경우 메시지 표시
			resultPanel.add(new JLabel("포켓몬을 찾을 수 없습니다."));
		}
		resultPanel.revalidate(); // 결과 패널 업데이트
		resultPanel.repaint(); // 패널 재페인팅
	}

	public void fiveGacha() {
		// 5회 뽑기 로직 구현
	}

	public static void main(String[] args) {
		// 예제 사용 시, UserInfo 객체를 생성하여 Gacha 창을 보여줄 수 있습니다.
		// UserInfo userInfo = new UserInfo(...); // 사용자 정보 생성
		// new Gacha(userInfo);
	}
}
