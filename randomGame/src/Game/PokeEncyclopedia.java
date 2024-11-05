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
		gridPanel.setLayout(new GridLayout(4, 4));// 4x4 grid
		add(gridPanel, BorderLayout.CENTER);

		JPanel navigationPanel = new JPanel();
		prevButton = new JButton("<");
		nextButton = new JButton(">");
		pageLabel = new JLabel();

		navigationPanel.add(prevButton);
		navigationPanel.add(pageLabel);
		navigationPanel.add(nextButton);
		add(navigationPanel, BorderLayout.SOUTH);

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

		updateGrid();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void updateGrid() {
		gridPanel.removeAll();

		prevButton.setEnabled(false);
		nextButton.setEnabled(false);

		totalPages = (int) Math.ceil(151 / 16.0);
		pageLabel.setText(currentPage + "/" + totalPages);

		int startPokenum = (currentPage - 1) * 16 + 1;
		int endPokenum = Math.min(startPokenum + 15, 151);

		for (int currentPokenum = startPokenum; currentPokenum <= endPokenum; currentPokenum++) {
			PokeInfo poke = dao.getPokemonByPokenum(userInfo.getUserNum(), currentPokenum);

			JPanel pokePanel = new JPanel();
			pokePanel.setLayout(new GridBagLayout());
			pokePanel.setPreferredSize(new Dimension(150, 150));

			JLabel pokeImageLabel;
			String nameLabel;

			if (poke != null) {
				String imagePath = "/Game/imges/" + poke.getPokenum() + ".png";
				pokeImageLabel = new JLabel(new ImageIcon(resizeImage(imagePath, 128, 128)));
				nameLabel = poke.getName();
			} else {
				pokeImageLabel = new JLabel(new ImageIcon(resizeImage("/Game/imges/question-mark.png", 128, 128)));
				nameLabel = "???";
			}

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			pokePanel.add(pokeImageLabel, gbc);

			gbc.gridy = 1;
			pokePanel.add(new JLabel(nameLabel, SwingConstants.CENTER), gbc);

			if (poke != null) {
				pokePanel.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						if (evt.getClickCount() == 2) {
							new PokeInfoPopup(poke);
						}
					}
				});
			}

			gridPanel.add(pokePanel);
		}

		int totalDisplayed = endPokenum - startPokenum + 1;
		int remainingPanels = 8 - totalDisplayed;

		for (int i = 0; i < remainingPanels; i++) {
			gridPanel.add(new JPanel());
		}

		gridPanel.revalidate();
		gridPanel.repaint();

		prevButton.setEnabled(true);
		nextButton.setEnabled(true);
	}

	private Image resizeImage(String path, int width, int height) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
		Image img = imageIcon.getImage();
		Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return newImg;
	}
}
