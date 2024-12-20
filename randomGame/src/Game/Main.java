package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

class RoundedButton extends JButton {
	private Color originalColor;
	private Color hoverColor;

	public RoundedButton(String text) {
		super(text);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorder(new EmptyBorder(10, 10, 10, 10));

		originalColor = Color.LIGHT_GRAY;
		hoverColor = Color.GRAY;
		setBackground(originalColor);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(originalColor);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

		super.paintComponent(g);
		g2.dispose();
	}

	@Override
	public void paintBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(getForeground());
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
		g2.dispose();
	}
}

public class Main extends JFrame {
	private boolean loggedIn = false;
	private UserInfo currentUser;
	private Image backgroundImage;

	public Main() {
		setTitle("랜덤 뽑기");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);

		JLabel imageLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Game/imges/main.png"));
		Image image = imageIcon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
		imageLabel.setIcon(new ImageIcon(image));
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setBorder(new EmptyBorder(40, 0, 10, 0));

		JPanel imagePanel = new JPanel();
		imagePanel.add(imageLabel);

		JPanel inputJPanel = new JPanel();
		inputJPanel.setLayout(new GridLayout(4, 1, 0, 10));
		inputJPanel.setBorder(new EmptyBorder(20, 100, 20, 100));

		JButton loginbtn = new RoundedButton("로그인");
		loginbtn.setBackground(Color.LIGHT_GRAY);
		inputJPanel.add(loginbtn);

		JButton Gachabtn = new RoundedButton("뽑기");
		Gachabtn.setBackground(Color.LIGHT_GRAY);
		inputJPanel.add(Gachabtn);

		JButton Infobtn = new RoundedButton("도감");
		Infobtn.setBackground(Color.LIGHT_GRAY);
		inputJPanel.add(Infobtn);

		add(imageLabel, BorderLayout.NORTH);
		add(inputJPanel, BorderLayout.CENTER);

		loginbtn.addActionListener(e -> login());
		Gachabtn.addActionListener(e -> gacha(currentUser));
		Infobtn.addActionListener(e -> Info());

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void login() {
		if (!loggedIn) {
			new Login(this);
		} else {
			int result = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "로그아웃 확인", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				loggedIn = false;
				currentUser = null;
				((JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(0)).setText("로그인");
			}
		}
	}

	public void setUserInfo(UserInfo userInfo) {
		this.currentUser = userInfo; // 사용자 정보를 설정
		loggedIn = true;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		JButton loginbtn = (JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(0);
		loginbtn.setText(loggedIn ? "로그아웃" : "로그인");
	}

	public void gacha(UserInfo userInfo) {
		if (loggedIn == false) {
			JOptionPane.showMessageDialog(this, "로그인 후 사용가능합니다.");
		} else {
			new Gacha(userInfo);
		}
	}

	public void Info() {
		if (!loggedIn) {
			JOptionPane.showMessageDialog(this, "로그인 후 사용가능합니다.");
		} else {
			new PokeEncyclopedia(currentUser);
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
