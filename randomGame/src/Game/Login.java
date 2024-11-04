package Game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {

    private JTextField userid;
    private JPasswordField userpassword;
    private Main mainFrame;
    UserInfo_dao dao = new UserInfo_dao();

    public Login(Main mainFrame) {
        this.mainFrame = mainFrame;
        setTitle("로그인");
        setSize(300, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputJPanel = new JPanel();
        inputJPanel.setLayout(new GridLayout(4, 2, 10, 10));

        inputJPanel.add(new JLabel("사용자 ID:"));
        userid = new JTextField(10);
        inputJPanel.add(userid);

        inputJPanel.add(new JLabel("비밀번호:"));
        userpassword = new JPasswordField(10);
        inputJPanel.add(userpassword);

        JButton loginbtn = new JButton("로그인");
        inputJPanel.add(loginbtn);

        JButton signin = new JButton("가입");
        inputJPanel.add(signin);

        add(inputJPanel, BorderLayout.CENTER);

        loginbtn.addActionListener(e -> handleLogin());
        signin.addActionListener(e -> handleSignup());

        setVisible(true);
    }

    private void handleLogin() {
        String userIdText = userid.getText();
        String passwordText = new String(userpassword.getPassword());
        if (!userIdText.isEmpty() && !passwordText.isEmpty()) {
            UserInfo userInfo = dao.getUserInfo(userIdText, passwordText);
            if (userInfo != null) {
                JOptionPane.showMessageDialog(this, "로그인 성공");
                mainFrame.setUserInfo(userInfo); // 사용자 정보를 설정
                mainFrame.setLoggedIn(true); // 로그인 상태를 true로 변경
                dispose(); // 로그인 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "없는 아이디거나 잘못된 정보입니다.");
            }
        } else {
            if (userIdText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "아이디를 입력하세요");
            } else if (passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요");
            }
        }
    }

    

    private void handleSignup() {
        System.out.println("가입 버튼 클릭");
        // 가입 로직을 추가합니다.
    }
}
