package Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class RoundedBorder extends javax.swing.border.AbstractBorder {
	private final Color color;
	private final int radius;

	public RoundedBorder(Color color, int radius) {
		this.color = color;
		this.radius = radius;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(color);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
		g2.dispose();
	}
}

public class PokeInfoPopup extends JFrame {
	private PokeInfo pokeInfo;
	private static final Map<String, Color> typeColors = new HashMap<>();
	private static final Map<String, Color> rankColors = new HashMap<>();

	static {
		typeColors.put("불", new Color(0xe56c3e));
		typeColors.put("독", new Color(0x735198));
		typeColors.put("비행", new Color(0xa2c3e7));
		typeColors.put("풀", new Color(0x14ae5c));
		typeColors.put("드래곤", new Color(0x535ca8));
		typeColors.put("물", new Color(0x5185c5));
		typeColors.put("노말", new Color(0x8e8e8f));
		typeColors.put("악", new Color(0x4c4948));
		typeColors.put("에스퍼", new Color(0xdd6b7b));
		typeColors.put("얼음", new Color(0x6dc7ea));
		typeColors.put("땅", new Color(0x9c7743));
		typeColors.put("페어리", new Color(0xdab4d4));
		typeColors.put("전기", new Color(0xfbb917));
		typeColors.put("강철", new Color(0x69a9c7));
		typeColors.put("벌레", new Color(0x9fa244));
		typeColors.put("격투", new Color(0xe09c40));
		typeColors.put("고스트", new Color(0x684870));

		rankColors.put("SS", new Color(0xff6c00));
		rankColors.put("S", new Color(0xe4b432));
		rankColors.put("A", new Color(0x9410be));
		rankColors.put("B", new Color(0x4b97ff));
		rankColors.put("C", new Color(0x00c73c));
	}

	public PokeInfoPopup(PokeInfo pokeInfo) {
		this.pokeInfo = pokeInfo;
		setTitle(pokeInfo.getName());
		setSize(300, 400);
		setResizable(false);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Font font = new Font("맑은 고딕", Font.PLAIN, 16);
		Font rankFont = font.deriveFont(Font.BOLD, 45);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		String imagePath = "/Game/imges/" + pokeInfo.getPokenum() + ".png";
		ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
		JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
		imageLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		mainPanel.add(imageLabel);

		JLabel nameLabel = new JLabel(pokeInfo.getName(), JLabel.CENTER);
		nameLabel.setFont(font.deriveFont(Font.BOLD, 20));
		nameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		mainPanel.add(nameLabel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JPanel typePanel = new JPanel();
		typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		typePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		addTypeLabel(typePanel, pokeInfo.getType1(), font);
		if (pokeInfo.getType2() != null && !pokeInfo.getType2().isEmpty()) {
			addTypeLabel(typePanel, pokeInfo.getType2(), font);
		}

		mainPanel.add(typePanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel rankLabel = new JLabel(pokeInfo.getRank());
		rankLabel.setOpaque(false);
		rankLabel.setForeground(rankColors.getOrDefault(pokeInfo.getRank(), Color.BLACK));
		rankLabel.setFont(rankFont);
		rankLabel.setHorizontalAlignment(JLabel.CENTER);
		rankLabel.setPreferredSize(new Dimension(100, 40));
		rankLabel.setBorder(null);

		JPanel rankPanel = new JPanel();
		rankPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		rankPanel.add(rankLabel);

		mainPanel.add(rankPanel);

		add(mainPanel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addTypeLabel(JPanel parent, String type, Font font) {
		Color color = typeColors.getOrDefault(type, Color.BLACK);
		JLabel typeLabel = new JLabel(type);
		typeLabel.setOpaque(true);
		typeLabel.setBackground(color);
		typeLabel.setForeground(Color.WHITE);
		typeLabel.setFont(font.deriveFont(Font.BOLD, 16));
		typeLabel.setHorizontalAlignment(JLabel.CENTER);
		typeLabel.setPreferredSize(new Dimension(100, 40));
		typeLabel.setBorder(new RoundedBorder(color, 20));
		parent.add(typeLabel);
	}
}
