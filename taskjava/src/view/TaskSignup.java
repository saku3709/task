package view;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.UserDao;
import model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskSignup extends JFrame {

	private UserDao dao = UserDao.getInstance();
	private Component parent = null;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textName;
	private JTextField textId;
	private JTextField textPassword;
	private JButton btnSignup;
	private JLabel lblTitle;

	private Color white = Color.white;
	private Color gray = Color.gray;

	TaskSignup(Component parent) {
		this.parent = parent;
		initialize();
	}

	/**
	 * Launch the application.
	 */
	public static void showSignupFrame(Component parent) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskSignup frame = new TaskSignup(parent);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x = 0;
		int y = 0;
		if (parent != null) {
			x = parent.getX();
			y = parent.getY();
		}
		setBounds(x, y, 500, 500);
		if (parent == null) {
			setLocale(null);
		}
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelTitle = new JPanel();
		panelTitle.setBackground(Color.WHITE);
		panelTitle.setBounds(0, 0, 483, 110);
		contentPane.add(panelTitle);
		panelTitle.setLayout(new BorderLayout(0, 0));

		lblTitle = new JLabel("Task");
		lblTitle.setForeground(Color.gray);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("D2Coding", Font.BOLD, 40));
		panelTitle.add(lblTitle, BorderLayout.CENTER);

		JPanel panelLogin = new JPanel();
		panelLogin.setLayout(null);
		panelLogin.setBackground(Color.WHITE);
		panelLogin.setBounds(0, 96, 484, 254);
		contentPane.add(panelLogin);

		textName = new JTextField();
		textName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textName.setText("");
			}
		});
		textName.setText("Name..");
		textName.setForeground(gray);
		textName.setFont(new Font("D2Coding", Font.PLAIN, 20));
		textName.setColumns(10);
		textName.setBounds(71, 17, 345, 42);
		panelLogin.add(textName);

		textId = new JTextField();
		textId.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textId.setText("");
			}
		});
		textId.setText("Id..");
		textId.setForeground(gray);
		textId.setFont(new Font("D2Coding", Font.PLAIN, 20));
		textId.setColumns(10);
		textId.setBounds(71, 96, 345, 42);
		panelLogin.add(textId);

		textPassword = new JTextField();
		textPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPassword.setText("");
			}
		});
		textPassword.setText("Password..");
		textPassword.setForeground(gray);
		textPassword.setFont(new Font("D2Coding", Font.PLAIN, 20));
		textPassword.setColumns(10);
		textPassword.setBounds(71, 182, 345, 42);
		panelLogin.add(textPassword);

		JPanel panelSign = new JPanel();
		panelSign.setBackground(white);
		panelSign.setLayout(null);
		panelSign.setBounds(0, 346, 484, 115);
		contentPane.add(panelSign);

		btnSignup = new JButton("Ok");
		btnSignup.setBackground(white);
		btnSignup.setFocusable(false);
		btnSignup.setBorder(new LineBorder(Color.black));
		btnSignup.addActionListener(e -> signup());
		btnSignup.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnSignup.setBounds(107, 25, 110, 39);
		panelSign.add(btnSignup);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBackground(white);
		btnCancel.setFocusable(false);
		btnCancel.setBorder(new LineBorder(Color.black));
		btnCancel.addActionListener(e -> cancelSignup());
		btnCancel.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnCancel.setBounds(262, 25, 110, 39);
		panelSign.add(btnCancel);
	}

	private void cancelSignup() {
		int option = JOptionPane.showConfirmDialog(TaskSignup.this, "일정 추가를 취소합니다", "cancel", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null);
		if (option == 0) {
			dispose();
		}
	}

	private void signup() {

		String id = textId.getText();
		String name = textName.getText();
		String pw = textPassword.getText();
		int idCheck = dao.checkId(id);
		if (id.equals("null") || name.equals("") || pw.equals("")) {
			JOptionPane.showMessageDialog(TaskSignup.this, "아이디, 이름, 비밀번호를 모두 입력해주세요", "경고",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (idCheck == 1) {
			User user = new User(id, name, pw);
			int result = dao.signUp(user);
			if (result == 1) {
				JOptionPane.showMessageDialog(TaskSignup.this, "회원가입에 성공했습니다", "성공", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}

		} else if (idCheck == -1) {

			JOptionPane.showMessageDialog(TaskSignup.this, "이미 존재하는 아이디입니다", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}

	}
}
