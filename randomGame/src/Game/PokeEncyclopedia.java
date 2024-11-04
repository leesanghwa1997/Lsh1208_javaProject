package Game;

import javax.swing.*;
import java.awt.*;

public class PokeEncyclopedia extends JFrame {
    private JPanel gridPanel;
    private JLabel pageLabel;
    private JButton prevButton;
    private JButton nextButton;
    private int currentPage = 1;
    private int totalPages;
    private UserInfo userInfo;
    private PokeEncyclopedia_dao dao;

    public PokeEncyclopedia(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.dao = new PokeEncyclopedia_dao();

        setTitle("도감");
        setSize(800, 800); 
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 4)); // 4x4 grid
        add(gridPanel, BorderLayout.CENTER);

        // Navigation Panel
        JPanel navigationPanel = new JPanel();
        prevButton = new JButton("<");
        nextButton = new JButton(">");
        pageLabel = new JLabel();

        navigationPanel.add(prevButton);
        navigationPanel.add(pageLabel);
        navigationPanel.add(nextButton);
        add(navigationPanel, BorderLayout.SOUTH);

        // 페이지 전환 버튼 리스너 추가
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateGrid();
            }
        });
        nextButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateGrid();
            }
        });

        updateGrid(); // 초기 그리드 업데이트
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateGrid() {
        gridPanel.removeAll(); // 이전 데이터를 지움

        // 버튼 비활성화
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);

        // 총 페이지 수 업데이트
        totalPages = (int) Math.ceil(151 / 16.0); // 151 마리의 포켓몬을 16마리씩 표시할 경우 총 페이지 수 계산
        pageLabel.setText(currentPage + "/" + totalPages); // 현재 페이지 표시

        // 현재 페이지의 시작 및 끝 포켓몬 번호 계산
        int startPokenum = (currentPage - 1) * 16 + 1;
        int endPokenum = Math.min(startPokenum + 15, 151); // 151 초과 방지

        // 현재 페이지에 맞는 포켓몬 정보 표시
        for (int currentPokenum = startPokenum; currentPokenum <= endPokenum; currentPokenum++) {
            PokeInfo poke = dao.getPokemonByPokenum(userInfo.getUserNum(), currentPokenum); // 포켓몬 정보 가져오기

            JPanel pokePanel = new JPanel();
            pokePanel.setLayout(new GridBagLayout());
            pokePanel.setPreferredSize(new Dimension(150, 150)); // 각 패널의 크기

            JLabel pokeImageLabel;
            String nameLabel;

            if (poke != null) {
                String imagePath = "/Game/imges/" + poke.getPokenum() + ".png"; // 이미지 경로
                pokeImageLabel = new JLabel(new ImageIcon(resizeImage(imagePath, 128, 128))); // 이미지 리사이즈
                nameLabel = poke.getName();
            } else {
                // 포켓몬 데이터가 없을 경우
                pokeImageLabel = new JLabel(new ImageIcon(resizeImage("/Game/imges/question-mark.png", 128, 128))); // 플래이스홀더 이미지
                nameLabel = "???"; // 이름
            }

            // 이미지와 이름 중앙 정렬
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; // 열
            gbc.gridy = 0; // 행
            gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬
            pokePanel.add(pokeImageLabel, gbc);

            gbc.gridy = 1; // 이름 행으로 이동
            pokePanel.add(new JLabel(nameLabel, SwingConstants.CENTER), gbc); // 이름 중앙 정렬

            // 패널 클릭 시 포켓몬 정보 팝업 띄우기 (poke가 null이 아닐 때만)
            if (poke != null) {
                pokePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (evt.getClickCount() == 2) {
                            new PokeInfoPopup(poke); // 팝업 띄우기
                        }
                    }
                });
            }

            gridPanel.add(pokePanel);
        }

        // 마지막 페이지에서 남은 패널을 비워서 균형 맞추기
        int totalDisplayed = endPokenum - startPokenum + 1; // 현재 페이지에 표시된 총 포켓몬 수
        int remainingPanels = 8 - totalDisplayed; // 2x4 그리드이므로 총 8개 패널

        for (int i = 0; i < remainingPanels; i++) {
            gridPanel.add(new JPanel()); // 빈 패널 추가
        }

        gridPanel.revalidate(); // 레이아웃 업데이트
        gridPanel.repaint(); // 화면 새로 고침

        // 버튼 다시 활성화
        prevButton.setEnabled(true);
        nextButton.setEnabled(true);
    }


    // 이미지 리사이즈 메서드
    private Image resizeImage(String path, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
        Image img = imageIcon.getImage(); // 이미지 변환
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 부드럽게 리사이즈
        return newImg; // 변환된 이미지 반환
    }
}
