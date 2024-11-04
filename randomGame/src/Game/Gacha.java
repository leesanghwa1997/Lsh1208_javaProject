package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addPokeToResultPanel(PokeInfo selectedPoke) {
		if (selectedPoke != null) {
			int pokenum = selectedPoke.getPokenum();
			String imagePath = "/Game/imges/" + pokenum + ".png";
			ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
			Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

			JPanel pokePanel = new JPanel();
			pokePanel.setLayout(new BoxLayout(pokePanel, BoxLayout.Y_AXIS));

			JLabel pokeImageLabel = new JLabel(new ImageIcon(image));
			pokeImageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			pokePanel.add(pokeImageLabel);

			JLabel pokeNameLabel = new JLabel(selectedPoke.getName(), JLabel.CENTER);
			pokeNameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			pokePanel.add(pokeNameLabel);

			pokePanel.setPreferredSize(new Dimension(100, 120));

			// Add double-click listener to open popup
			pokePanel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					if (evt.getClickCount() == 2) {
						new PokeInfoPopup(selectedPoke);
					}
				}
			});

			resultPanel.add(pokePanel);
		} else {
			resultPanel.add(new JLabel("포켓몬을 찾을 수 없습니다."));
		}
	}

	public void oneGacha(UserInfo userInfo) {
	    resultPanel.removeAll();

	    List<PokeInfo> pokeList = gachaDao.gacha(userInfo, 1, 200);
	    for (PokeInfo poke : pokeList) {
	        addPokeToResultPanel(poke);
	        // PokeEncyclopedia_dto에 데이터 저장
	        gachaDao.saveToEncyclopedia(userInfo.getUserNum(), poke.getPokenum());
	    }

	    moneyLabel.setText("소지금: " + userInfo.getMoney());
	    resultPanel.revalidate();
	    resultPanel.repaint();
	}

	public void fiveGacha() {
	    resultPanel.removeAll();

	    List<PokeInfo> pokeList = gachaDao.gacha(userInfo, 5, 1000);
	    for (PokeInfo poke : pokeList) {
	        addPokeToResultPanel(poke);
	        // PokeEncyclopedia_dto에 데이터 저장
	        gachaDao.saveToEncyclopedia(userInfo.getUserNum(), poke.getPokenum());
	    }

	    moneyLabel.setText("소지금: " + userInfo.getMoney());
	    resultPanel.revalidate();
	    resultPanel.repaint();
	}

	public static void main(String[] args) {

	}
}
