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

public class SignUp extends JFrame {

    private JTextField idField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private UserInfo_dao dao = new UserInfo_dao();

    public SignUp() {
        setTitle("회원가입");
        setSize(300, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input fields panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("사용자 ID:"));
        idField = new JTextField(10);
        inputPanel.add(idField);

        inputPanel.add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField(10);
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("이름:"));
        nameField = new JTextField(10);
        inputPanel.add(nameField);

        add(inputPanel, BorderLayout.CENTER);

        // Centered button panel
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("가입");
        buttonPanel.add(registerButton); // Center-align button
        add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> handleSignUp());

        setVisible(true);
    }

    private void handleSignUp() {
        String userId = idField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();

        if (!userId.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
            UserInfo newUser = new UserInfo(0, userId, password, name, 0);
            boolean success = dao.addUser(newUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공!");
                dispose(); // Close the sign-up window
            } else {
                JOptionPane.showMessageDialog(this, "회원가입 실패. 다시 시도해 주세요.");
            }
        } else if(userId.isEmpty()){
            JOptionPane.showMessageDialog(this, "사용자 ID를 입력하세요");
        }
        else if(password.isEmpty()){
            JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
        } else if(name.isEmpty()){
            JOptionPane.showMessageDialog(this, "사용자명을 입력하세요");
        }
    }
}
