package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;
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
    private HashMap<String, Color> rankColors; // 랭크 색상 저장 맵

    public Gacha(UserInfo userInfo) {
        this.userInfo = userInfo; // 전달받은 사용자 정보 저장
        this.gachaDao = new Gacha_dao();
        
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(1, 5, 10, 10)); // 1행 5열의 그리드 레이아웃, 가로/세로 간격 10px
        add(resultPanel, BorderLayout.CENTER);

        // 랭크 색상 초기화
        rankColors = new HashMap<>();
        rankColors.put("SS", new Color(0xff6c00)); 
        rankColors.put("S", new Color(0xe4b432)); 
        rankColors.put("A", new Color(0x9410be));
        rankColors.put("B", new Color(0x4b97ff));
        rankColors.put("C", new Color(0x00c73c));

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

            // 패널 생성
            JPanel pokePanel = new JPanel();
            pokePanel.setLayout(new BoxLayout(pokePanel, BoxLayout.Y_AXIS));

            // 이미지 라벨 생성
            JLabel pokeImageLabel = new JLabel(new ImageIcon(image));
            pokeImageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            
            // 포켓몬의 랭크에 따라 테두리 색상 설정
            Color borderColor = rankColors.getOrDefault(selectedPoke.getRank(), Color.BLACK);
            pokeImageLabel.setBorder(new RoundedBorder(borderColor, 40)); // 테두리 두께를 20으로 설정

            pokePanel.add(pokeImageLabel);

            // 이름 라벨 생성
            JLabel pokeNameLabel = new JLabel(selectedPoke.getName(), JLabel.CENTER);
            pokeNameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            pokePanel.add(pokeNameLabel);

            pokePanel.setPreferredSize(new Dimension(120, 150)); // 패널 크기 조정

            // 더블 클릭 이벤트 리스너 추가
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
