package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import controller.UserDao;

import javax.swing.JButton;

import static model.User.Entity;
import static view.TaskMain.showTaskMain;
import static view.TaskSignup.showSignupFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TaskLogin extends JFrame {

	private UserDao dao = UserDao.getInstance();
	private JFrame frame;
	private JLabel lblTitle;
	private static JTextField textId;

	private Color white = Color.white;

	private static JTextField textPassword;
	private JButton btnSignup;
	private JButton btnSignin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskLogin window = new TaskLogin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TaskLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(600, 200, 500, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("LOGIN_TASK");
		frame.getContentPane().setLayout(null);

		JPanel panelTitle = new JPanel();
		panelTitle.setBounds(0, 0, 483, 110);
		frame.getContentPane().add(panelTitle);
		panelTitle.setLayout(new BorderLayout(0, 0));

		lblTitle = new JLabel("Task");
		lblTitle.setForeground(Color.gray);
		lblTitle.setFont(new Font("D2Coding", Font.BOLD, 40));
		lblTitle.setHorizontalAlignment(lblTitle.CENTER);
		panelTitle.add(lblTitle);

		JPanel panelLogin = new JPanel();
		panelLogin.setBounds(0, 95, 484, 214);
		frame.getContentPane().add(panelLogin);
		panelLogin.setLayout(null);

		textId = new JTextField();
		textId.setText("Id..");
		textId.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textId.setText("");
			}
		});

		textId.setForeground(Color.gray);
		textId.setBounds(71, 47, 345, 42);
		textId.setFont(new Font("D2Coding", Font.PLAIN, 20));
		// textId.setHorizontalAlignment(textId.CENTER);
		panelLogin.add(textId);
		textId.setColumns(10);

		textPassword = new JTextField();
		textPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPassword.setText("");
			}
		});
		textPassword.setText("Password..");
		textPassword.setForeground(Color.gray);
		textPassword.setFont(new Font("D2Coding", Font.PLAIN, 20));
		textPassword.setColumns(10);
		textPassword.setBounds(71, 127, 345, 42);
		panelLogin.add(textPassword);

		JPanel panelSign = new JPanel();
		panelSign.setBounds(0, 308, 484, 153);
		frame.getContentPane().add(panelSign);
		panelSign.setLayout(null);

		btnSignin = new JButton("Sign-In");
		// btnSignin.setBorderPainted(false);
		btnSignin.setFocusable(false);
		btnSignin.setBorder(new LineBorder(Color.black));
		btnSignin.addActionListener(e -> signin());
		btnSignin.setBounds(107, 30, 110, 39);
		btnSignin.setFont(new Font("D2Coding", Font.PLAIN, 20));
		panelSign.add(btnSignin);

		btnSignup = new JButton("Sign-Up");
		btnSignup.setFocusable(false);
		btnSignup.setBorder(new LineBorder(Color.black));

		btnSignup.addActionListener(e -> showSignupFrame(frame));
		btnSignup.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnSignup.setBounds(262, 30, 110, 39);
		panelSign.add(btnSignup);

		frame.setBackground(white);
		panelLogin.setBackground(white);
		panelTitle.setBackground(white);
		panelSign.setBackground(white);
		btnSignin.setBackground(white);
		btnSignin.setBackground(white);
		btnSignup.setBackground(white);
	}

	private void signin() {
		String id = textId.getText();
		String pw = textPassword.getText();
		int result = dao.signIn(id, pw);

		if (id.equals("") || pw.equals("")) {
			JOptionPane.showMessageDialog(TaskLogin.this, "아이디, 비밀번호를 모두 입력해주세요", "경고", JOptionPane.WARNING_MESSAGE);
			// textId.requestFocus(); // 검색어 입력 JTextField에 포커스를 줌(커서 깜박깜박).
			textPassword.requestFocus();

			return;
		}
		if (result == 1) {
			showTaskMain(frame, id);
			// dispose(); 메인이라 안 사라지나??
		} else {
			int idCheck = dao.checkId(id);
			if (idCheck == 1) {
				JOptionPane.showMessageDialog(frame, "존재하지 않는 아이디입니다", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "아이디와 비밀번호를 올바르게 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public static void resetText() {
		textId.setText(null);
		textPassword.setText(null);
	}

}
