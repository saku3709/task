package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.TaskDaoImpl;
import controller.UserDao;
import model.Task;
import model.User;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.font.NumericShaper.Range;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskAddFrame extends JFrame {

	class CombDate {

		// 콤보박스에 설정할 년/월/일
		private static final int SRART_YEAR = 2024;
		private static final int LAST_YEAR = 2050;
		private static final int START_MONTH = 1;
		private static final int LAST_MONTH = 12;
		private static final int START_DAY = 1;
		private static final int LAST_DAY = 31;
		// 년 문자열 배열로 생성
		// 숫자 범위 IntStream을 array로 바꾼다
		private static final int[] YEAR_INT = IntStream.rangeClosed(SRART_YEAR, LAST_YEAR).toArray();
		private static final String[] YEAR_STR = Arrays.toString(YEAR_INT).replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,.]", "")
				.split(",");
		// --> int[]의 원소를 위에 써진 문자만 제외하고 삭제한 후(대괄호 삭제를 위해서) 콤마,를 기준으로 잘라서 String[]에 넣는다.
		// 월 문자열 배열로 생성
		private static final int[] MONTH_INT = IntStream.rangeClosed(START_MONTH, LAST_MONTH).toArray();
		private static final String[] MONTH_STR = Arrays.toString(MONTH_INT).replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,.]", "")
				.split(",");
		// 일 문자열 배열로 생성
		private static final int[] DAY_INT = IntStream.rangeClosed(START_DAY, LAST_DAY).toArray();
		private static final String[] DAY_STR = Arrays.toString(DAY_INT).replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,.]", "")
				.split(",");
	}

	public static final List<String> cateList = new ArrayList<>();

	private TaskDaoImpl dao = TaskDaoImpl.getInstance();

	private TaskMain app = null;

	private Component parent = null;

	private String userId;

	private LocalDate startDate = null;
	private LocalDate limitDate = null;

	private Color white = Color.white;
	private Color gray = Color.gray;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textTitleTodo;
	private JLabel lblStartDateTodo;
	private JLabel lblLimitDateTodo;
	private JPanel btnPanel;
	private JButton btnOk;
	private JButton btnNo;
	private JPanel startPanelTodo;
	private JPanel limitPanelTodo;
	private JLabel lblStartYTodo;
	private JComboBox<String> combStartYTodo;
	private JLabel lblLimitYTodo;
	private JComboBox<String> combLimitYTodo;
	private JLabel lblStartMTodo;
	private JComboBox<String> combStartMTodo;
	private JLabel lblStartDTodo;
	private JComboBox<String> combStartDTodo;
	private JLabel lblLimitMTodo;
	private JComboBox<String> combLImitMTodo;
	private JLabel lblLimitDTodo;
	private JComboBox<String> combLimitDTodo;
	private JPanel categoryPanel;
	private JTextField textCategory;
	private JComboBox<String> combCategory;
	private JButton btnCategory;
	private JLabel lblCategory;

	private DefaultComboBoxModel combModelCate;
	private JTextArea textContent;
	private JScrollPane scrollPaneTodo;
	private JPanel todoPanel;

	/**
	 * Launch the application.
	 */
	public static void showTaskAddFrame(Component parent, TaskMain app, String userId) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskAddFrame frame = new TaskAddFrame(parent, app, userId);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TaskAddFrame(Component parent, TaskMain app, String userId) {
		this.parent = parent;
		this.app = app;
		this.userId = userId;
		initialize();
		setCate();
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
		setBounds(x, y, 600, 770);
		if (parent == null) {
			setLocationRelativeTo(null);
		}
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(white);
		tabbedPane.setFont(new Font("D2Coding", Font.PLAIN, 16));
		tabbedPane.setBounds(12, 30, 560, 620);
		contentPane.add(tabbedPane);

		todoPanel = new JPanel();
		tabbedPane.addTab("ADD", null, todoPanel, null);
		todoPanel.setLayout(null);

		textTitleTodo = new JTextField();
		textTitleTodo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textTitleTodo.setText("");
			}
		});
		textTitleTodo.setHorizontalAlignment(SwingConstants.LEFT);
		textTitleTodo.setText("Title..");
		textTitleTodo.setForeground(gray);
		textTitleTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		textTitleTodo.setBounds(61, 15, 434, 70);
		todoPanel.add(textTitleTodo);
		textTitleTodo.setColumns(10);

		scrollPaneTodo = new JScrollPane();
		scrollPaneTodo.setBounds(61, 100, 434, 108);
		todoPanel.add(scrollPaneTodo);

		textContent = new JTextArea();
		textContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textContent.setText("");
			}
		});
		textContent.setText("Content..");
		textContent.setForeground(gray);
		textContent.setFont(new Font("D2Coding", Font.PLAIN, 18));
		scrollPaneTodo.setViewportView(textContent);

		lblStartDateTodo = new JLabel("Start Date");
		lblStartDateTodo.setForeground(gray);
		lblStartDateTodo.setFont(new Font("D2Coding", Font.ITALIC, 18));
		lblStartDateTodo.setBounds(70, 320, 115, 29);
		todoPanel.add(lblStartDateTodo);

		lblLimitDateTodo = new JLabel("Limit Date");
		lblLimitDateTodo.setForeground(gray);
		lblLimitDateTodo.setFont(new Font("D2Coding", Font.ITALIC, 18));
		lblLimitDateTodo.setBounds(70, 446, 115, 37);
		todoPanel.add(lblLimitDateTodo);

		startPanelTodo = new JPanel();
		startPanelTodo.setBounds(80, 361, 389, 75);
		todoPanel.add(startPanelTodo);
		startPanelTodo.setLayout(null);

		lblStartYTodo = new JLabel("Y");
		lblStartYTodo.setBounds(12, 25, 25, 24);
		lblStartYTodo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		startPanelTodo.add(lblStartYTodo);

		combStartYTodo = new JComboBox<>();

		DefaultComboBoxModel<String> combModel = new DefaultComboBoxModel<>(CombDate.YEAR_STR);
		combStartYTodo.setModel(combModel); // TODO: YEAR_INT 를 넣으면 안된다.. setModel은 int 배열은 못 넣나?? 공부.

		combStartYTodo.setBounds(32, 20, 80, 32);
		combStartYTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		startPanelTodo.add(combStartYTodo);

		lblStartMTodo = new JLabel("M");
		lblStartMTodo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblStartMTodo.setBounds(145, 25, 25, 24);
		startPanelTodo.add(lblStartMTodo);

		combStartMTodo = new JComboBox<String>();

		combModel = new DefaultComboBoxModel<>(CombDate.MONTH_STR);
		combStartMTodo.setModel(combModel);

		combStartMTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combStartMTodo.setBounds(165, 20, 80, 32);
		startPanelTodo.add(combStartMTodo);

		lblStartDTodo = new JLabel("D");
		lblStartDTodo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblStartDTodo.setBounds(277, 25, 25, 24);
		startPanelTodo.add(lblStartDTodo);

		combStartDTodo = new JComboBox<String>();

		combModel = new DefaultComboBoxModel<String>(CombDate.DAY_STR);
		combStartDTodo.setModel(combModel);

		combStartDTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combStartDTodo.setBounds(297, 20, 80, 32);
		startPanelTodo.add(combStartDTodo);

		limitPanelTodo = new JPanel();
		limitPanelTodo.setBounds(80, 487, 389, 75);
		todoPanel.add(limitPanelTodo);
		limitPanelTodo.setLayout(null);

		lblLimitYTodo = new JLabel("Y");
		lblLimitYTodo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblLimitYTodo.setBounds(12, 25, 25, 24);
		limitPanelTodo.add(lblLimitYTodo);

		combLimitYTodo = new JComboBox<>();

		combModel = new DefaultComboBoxModel<>(CombDate.YEAR_STR);
		combLimitYTodo.setModel(combModel);

		combLimitYTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combLimitYTodo.setBounds(32, 20, 80, 32);
		limitPanelTodo.add(combLimitYTodo);

		lblLimitMTodo = new JLabel("M");
		lblLimitMTodo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblLimitMTodo.setBounds(145, 25, 25, 24);
		limitPanelTodo.add(lblLimitMTodo);

		combLImitMTodo = new JComboBox<String>();

		combModel = new DefaultComboBoxModel<String>(CombDate.MONTH_STR);
		combLImitMTodo.setModel(combModel);

		combLImitMTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combLImitMTodo.setBounds(165, 20, 80, 32);
		limitPanelTodo.add(combLImitMTodo);

		lblLimitDTodo = new JLabel("D");
		lblLimitDTodo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblLimitDTodo.setBounds(277, 25, 25, 24);
		limitPanelTodo.add(lblLimitDTodo);

		combLimitDTodo = new JComboBox<String>();

		combModel = new DefaultComboBoxModel<String>(CombDate.DAY_STR);
		combLimitDTodo.setModel(combModel);

		combLimitDTodo.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combLimitDTodo.setBounds(297, 20, 80, 32);
		limitPanelTodo.add(combLimitDTodo);

		lblCategory = new JLabel("Category");
		lblCategory.setForeground(gray);
		lblCategory.setFont(new Font("D2Coding", Font.ITALIC, 18));
		lblCategory.setBounds(70, 215, 115, 37);
		todoPanel.add(lblCategory);

		categoryPanel = new JPanel();
		categoryPanel.setBounds(80, 254, 389, 49);
		todoPanel.add(categoryPanel);
		categoryPanel.setLayout(null);

		combCategory = new JComboBox<String>();
		combCategory.setBounds(17, 13, 155, 32);
		categoryPanel.add(combCategory);
		combCategory.setFont(new Font("D2Coding", Font.PLAIN, 18));

		textCategory = new JTextField();
		textCategory.setFont(new Font("D2Coding", Font.PLAIN, 18));
		textCategory.setBounds(182, 13, 155, 32);
		categoryPanel.add(textCategory);
		textCategory.setColumns(10);

		btnCategory = new JButton("v");
		btnCategory.setFocusable(false);
		btnCategory.setBorder(new LineBorder(Color.black));
		btnCategory.setBackground(white);
		btnCategory.addActionListener(e -> addCategory());
		btnCategory.setFont(new Font("D2Coding", Font.PLAIN, 15));
		btnCategory.setBounds(347, 13, 42, 32);
		categoryPanel.add(btnCategory);

		btnPanel = new JPanel();
		btnPanel.setBounds(0, 660, 584, 37);
		contentPane.add(btnPanel);
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnOk = new JButton("OK");
		btnOk.setBorderPainted(false);
		btnOk.setFocusable(false);
		btnOk.setBackground(white);
		btnOk.addActionListener(e -> addTask());
		btnOk.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnPanel.add(btnOk);

		btnNo = new JButton("NO");
		btnNo.setBorderPainted(false);
		btnNo.setFocusable(false);
		btnNo.setBackground(white);
		btnNo.addActionListener(e -> calcelAddTask());
		btnNo.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnPanel.add(btnNo);
	}

	private void setCate() {
		List<String> cateList = dao.setCateToCombobox(userId);
		// List를 String배열로 변환.to array
		// 중복제거 distinct
		String[] cateArr = cateList.stream().distinct().toArray(String[]::new);
		combModelCate = new DefaultComboBoxModel<>(cateArr);
		combCategory.setModel(combModelCate);

	}

	private void addCategory() {
		String cateText = textCategory.getText();
		if (cateText != null) {
			combCategory.addItem(textCategory.getText());
		}
	}

	private void calcelAddTask() {

		int option = JOptionPane.showConfirmDialog(TaskAddFrame.this, "일정 추가를 취소합니다", "cancel",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
		if (option == 0) {
			dispose();
		}
	}

	private void getDate() {
		// 시작일~
		String startYear = combStartYTodo.getSelectedItem().toString();
		// 월&일이 반드시 2자리수를 가진다(%2s). 빈 자리는 0으로 대체한다(replace). -> 1월은 01월로
		String startMonth = String.format("%2s", combStartMTodo.getSelectedItem().toString()).replace(" ", "0");
		String startDay = String.format("%2s", combStartDTodo.getSelectedItem().toString()).replace(" ", "0");

		// 만료일~
		String limitYear = combLimitYTodo.getSelectedItem().toString();
		String limitMonth = String.format("%2s", combLImitMTodo.getSelectedItem().toString()).replace(" ", "0");
		String limitDay = String.format("%2s", combLimitDTodo.getSelectedItem().toString()).replace(" ", "0");
		// 얻은 문자열을 하나의 start 날짜와 하나의 limit 날짜로 합쳐준다.
		String startStr = String.format("%s-%s-%s", startYear, startMonth, startDay);
		String limitStr = String.format("%s-%s-%s", limitYear, limitMonth, limitDay);
		// 합친 문자열을 LocalDate 객체로 변환.
		startDate = LocalDate.parse(startStr, DateTimeFormatter.ISO_DATE);
		limitDate = LocalDate.parse(limitStr, DateTimeFormatter.ISO_DATE);
	}

	private void addTask() {
		String title = textTitleTodo.getText();
		String content = textContent.getText();
		// 카테고리를 고르지 않을 경우 방지.
		String category;
		if (combCategory.getSelectedItem() != null) {
			category = combCategory.getSelectedItem().toString();
		} else {
			JOptionPane.showMessageDialog(TaskAddFrame.this, "카테고리를 적어주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		getDate();
		if (title.equals("") || content.equals("") || category.equals("") || startDate == null || limitDate == null) {
			JOptionPane.showMessageDialog(TaskAddFrame.this, "제목, 내용, 시작일, 완료일을 모두 적어주세요.", "경고",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// TODO 날짜 미입력시 시작일과 완료일 현재 날짜로 들어가게 하기.
		Task task = new Task(0, title, content, 0, startDate, limitDate, category, userId);
		int result = dao.create(userId, task);
		if (result == 1) {
			JOptionPane.showMessageDialog(TaskAddFrame.this, "일정 추가 성공");
			app.initializeTable();
			dispose();
		} else {
			JOptionPane.showMessageDialog(TaskAddFrame.this, "일정 추가 실패");

		}

	}
}
