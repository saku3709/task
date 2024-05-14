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
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JComboBox;

public class TaskDetailsFrame extends JFrame {

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

	private TaskDaoImpl dao = TaskDaoImpl.getInstance();
	public static final String[] CATEGORY_NAMES = {};

	private Component parent = null;
	private String userId;
	private TaskMain app = null;
	private int taskId = 0;
	private int tabIndex = 0;
	private int state = 0;

	private Color white = Color.white;
	private Color gray = Color.gray;

	private LocalDate startDate = null;
	private LocalDate limitDate = null;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textTitleUpdate;
	private JLabel lblStartDateUpdate;
	private JLabel lblLimitDateUpdate;
	private JPanel btnPanel;
	private JButton btnUpdate;
	private JButton btnCancel;
	private JPanel startPanelUpdate;
	private JLabel lblStartYUpdate;
	private JComboBox<String> combStartYUpdate;
	private JComboBox<String> combStartMUpdate;
	private JComboBox<String> combStartDUpdate;
	private JLabel lblStartMUpdate;
	private JLabel lblStartDUpdate;
	private JTextArea textContentUpdate;
	private JPanel updatePanel;
	private JPanel limitPanelUpdate;
	private JComboBox<String> combLimitYUpdate;
	private JComboBox<String> combLimitMUpdate;
	private JComboBox<String> combLimitDUpdate;
	private JLabel lblLimitYUpdate;
	private JLabel lblLimitMUpdate;
	private JLabel lblLimitDUpdate;
	private JLabel lblCategory;
	private JPanel categoryPanel;
	private JComboBox<String> combCategory;
	private JTextField textCategory;
	private JButton btnCategory;
	private DefaultComboBoxModel combModelCate;
	private JScrollPane scrollPaneUpdate;

	/**
	 * Launch the application.
	 */
	public static void showTaskDetailsFrame(Component parent, TaskMain app, int id, int tabIndex, String userId) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskDetailsFrame frame = new TaskDetailsFrame(parent, app, id, tabIndex, userId);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TaskDetailsFrame(Component parent, TaskMain app, int id, int tabIndex, String userId) {
		this.parent = parent;
		this.app = app;
		this.taskId = id;
		this.tabIndex = tabIndex;
		this.userId = userId;
		initialize();
		initializeTask();
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

		updatePanel = new JPanel();
		tabbedPane.addTab("Details", null, updatePanel, null);
		updatePanel.setLayout(null);

		textTitleUpdate = new JTextField();
		textTitleUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		textTitleUpdate.setBounds(61, 15, 434, 70);
		updatePanel.add(textTitleUpdate);
		textTitleUpdate.setColumns(10);

		scrollPaneUpdate = new JScrollPane();
		scrollPaneUpdate.setBounds(61, 100, 434, 108);
		updatePanel.add(scrollPaneUpdate);

		textContentUpdate = new JTextArea();
		textContentUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		scrollPaneUpdate.setViewportView(textContentUpdate);

		lblStartDateUpdate = new JLabel("Start Date");
		lblStartDateUpdate.setForeground(gray);
		lblStartDateUpdate.setFont(new Font("D2Coding", Font.BOLD, 18));
		lblStartDateUpdate.setBounds(70, 320, 115, 29);
		updatePanel.add(lblStartDateUpdate);

		lblLimitDateUpdate = new JLabel("Limit Date");
		lblLimitDateUpdate.setForeground(gray);
		lblLimitDateUpdate.setFont(new Font("D2Coding", Font.BOLD, 18));
		lblLimitDateUpdate.setBounds(70, 446, 115, 37);
		updatePanel.add(lblLimitDateUpdate);

		startPanelUpdate = new JPanel();
		startPanelUpdate.setBounds(80, 361, 389, 75);
		updatePanel.add(startPanelUpdate);
		startPanelUpdate.setLayout(null);

		lblStartYUpdate = new JLabel("Y");
		lblStartYUpdate.setBounds(12, 25, 25, 24);
		lblStartYUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		startPanelUpdate.add(lblStartYUpdate);

		combStartYUpdate = new JComboBox<>();

		DefaultComboBoxModel<String> combModelYear = new DefaultComboBoxModel<>(CombDate.YEAR_STR);
		combStartYUpdate.setModel(combModelYear); // TODO: YEAR_INT 를 넣으면 안된다.. setModel은 int 배열은 못 넣나?? 공부.

		combStartYUpdate.setBounds(32, 20, 80, 32);
		combStartYUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		startPanelUpdate.add(combStartYUpdate);

		lblStartMUpdate = new JLabel("M");
		lblStartMUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblStartMUpdate.setBounds(145, 25, 25, 24);
		startPanelUpdate.add(lblStartMUpdate);

		combStartMUpdate = new JComboBox<String>();

		DefaultComboBoxModel<String> combModelMonth = new DefaultComboBoxModel<>(CombDate.MONTH_STR);
		combStartMUpdate.setModel(combModelMonth);

		combStartMUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combStartMUpdate.setBounds(165, 20, 80, 32);
		startPanelUpdate.add(combStartMUpdate);

		lblStartDUpdate = new JLabel("D");
		lblStartDUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblStartDUpdate.setBounds(277, 25, 25, 24);
		startPanelUpdate.add(lblStartDUpdate);

		combStartDUpdate = new JComboBox<String>();

		DefaultComboBoxModel<String> combModelDay = new DefaultComboBoxModel<String>(CombDate.DAY_STR);
		combStartDUpdate.setModel(combModelDay);

		combStartDUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combStartDUpdate.setBounds(297, 20, 80, 32);
		startPanelUpdate.add(combStartDUpdate);

		limitPanelUpdate = new JPanel();
		limitPanelUpdate.setBounds(80, 487, 389, 75);
		updatePanel.add(limitPanelUpdate);
		limitPanelUpdate.setLayout(null);

		lblLimitYUpdate = new JLabel("Y");
		lblLimitYUpdate.setBounds(12, 25, 25, 24);
		lblLimitYUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		limitPanelUpdate.add(lblLimitYUpdate);

		combLimitYUpdate = new JComboBox<String>();
		combModelYear = new DefaultComboBoxModel<>(CombDate.YEAR_STR);
		combLimitYUpdate.setModel(combModelYear);

		combLimitYUpdate.setBounds(32, 20, 80, 32);
		combLimitYUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		limitPanelUpdate.add(combLimitYUpdate);

		lblLimitMUpdate = new JLabel("M");
		lblLimitMUpdate.setBounds(145, 25, 25, 24);
		lblLimitMUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		limitPanelUpdate.add(lblLimitMUpdate);

		combLimitMUpdate = new JComboBox<String>();
		combModelMonth = new DefaultComboBoxModel<>(CombDate.MONTH_STR);
		combLimitMUpdate.setModel(combModelMonth);
		combLimitMUpdate.setBounds(165, 20, 80, 32);
		combLimitMUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		limitPanelUpdate.add(combLimitMUpdate);

		lblLimitDUpdate = new JLabel("D");
		lblLimitDUpdate.setBounds(277, 25, 25, 24);
		lblLimitDUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		limitPanelUpdate.add(lblLimitDUpdate);

		combLimitDUpdate = new JComboBox<String>();
		combModelDay = new DefaultComboBoxModel<String>(CombDate.DAY_STR);
		combLimitDUpdate.setModel(combModelDay);
		combLimitDUpdate.setBounds(297, 20, 80, 32);
		combLimitDUpdate.setFont(new Font("D2Coding", Font.PLAIN, 18));
		limitPanelUpdate.add(combLimitDUpdate);

		lblCategory = new JLabel("Category");
		lblCategory.setForeground(gray);
		lblCategory.setFont(new Font("D2Coding", Font.BOLD, 18));
		lblCategory.setBounds(70, 215, 115, 37);
		updatePanel.add(lblCategory);

		categoryPanel = new JPanel();
		categoryPanel.setLayout(null);
		categoryPanel.setBounds(80, 254, 389, 49);
		updatePanel.add(categoryPanel);

		combCategory = new JComboBox<String>();
		combCategory.setFont(new Font("D2Coding", Font.PLAIN, 18));
		combCategory.setBounds(17, 13, 155, 32);
		categoryPanel.add(combCategory);

		textCategory = new JTextField();
		textCategory.setFont(new Font("D2Coding", Font.PLAIN, 18));
		textCategory.setColumns(10);
		textCategory.setBounds(182, 13, 155, 32);
		categoryPanel.add(textCategory);

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

		btnUpdate = new JButton("Update");
		btnUpdate.setBorderPainted(false);
		btnUpdate.setFocusable(false);
		btnUpdate.setBackground(white);
		btnUpdate.addActionListener(e -> updateTask());
		btnUpdate.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnPanel.add(btnUpdate);

		btnCancel = new JButton("Cancel");
		btnCancel.setBorderPainted(false);
		btnCancel.setFocusable(false);
		btnCancel.setBackground(white);
		btnCancel.addActionListener(e -> calcelUpdateTask());
		btnCancel.setFont(new Font("D2Coding", Font.PLAIN, 20));
		btnPanel.add(btnCancel);
	}

	// TODO: 카테고리에 있는 것을 텍스트 필드에서 똑같이 쓰는 경우 방지.
	private void setCate() {
		List<String> cateList = dao.setCateToCombobox(userId);
		// List를 String배열로 변환.
		// 중복제거 distinct
		String[] cateArr = cateList.stream().distinct().toArray(String[]::new);
		combModelCate = new DefaultComboBoxModel<>(cateArr);
		combCategory.setModel(combModelCate);

	}

	private void addCategory() {
		String cateText = textCategory.getText();
		// 텍스트 필드에서 아무것도 없는 빈 칸을 카테고리에 등록하는 경우 방지.
		if (cateText.equals("")) {
			JOptionPane.showMessageDialog(TaskDetailsFrame.this, "카테고리를 적어주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (cateText != null) {
			combCategory.addItem(textCategory.getText());
		}
	}

	private void calcelUpdateTask() {

		int option = JOptionPane.showConfirmDialog(TaskDetailsFrame.this, "일정 추가를 취소합니다", "cancel",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
		if (option == 0) {
			dispose();
		}
	}

	private void initializeTask() {
		Task task = dao.read(userId, taskId);
		if (task == null) {
			return;
		}
		textTitleUpdate.setText(task.getTitle());
		textContentUpdate.setText(task.getContent());
		state = task.getState();

		startDate = task.getStartDate();
		limitDate = task.getLimitDate();
		initializeDate();
		
		//TODO: 콤보박스에 해당 카테고리가 초기화 되어 있지 않음.
		String cate=task.getCategory().replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9.]", "");
		combCategory.setSelectedItem(cate);
		textCategory.setText(cate);
		

	}

	// 기존 날짜 읽어서 앱에 보이게 하기
	private void initializeDate() {
		// localdate를 String 배열로 만들기
		// 예시_ "2024-04-14" 을 -를 기준으로 잘라서 년,월,일로 잘라진 String 배열로 만든다.
		String[] startDateStr = startDate.toString().split("-");
		String[] limitDateStr = limitDate.toString().split("-");

		String startYear = startDateStr[0];
		// 월,일 앞에 붙인 0을 제거해야 한다. -> comboBox에서 한자리 수 앞에 0을 붙이지 않았기 때문.
		//TODO: 일의 자리0은 제외 
		String startMonth = startDateStr[1].replace("0", "");
		String startDay = startDateStr[2].replace("0", "");

		String limitYear = limitDateStr[0];
		String limitMonth = limitDateStr[1].replace("0", "");
		String limitDay = limitDateStr[2].replace("0", "");

		// 날짜를 comboBox에 초기화하기.
		combStartYUpdate.setSelectedItem(startYear);
		combStartMUpdate.setSelectedItem(startMonth);
		combStartDUpdate.setSelectedItem(startDay);

		combLimitYUpdate.setSelectedItem(limitYear);
		combLimitMUpdate.setSelectedItem(limitMonth);
		combLimitDUpdate.setSelectedItem(limitDay);
	}

	private void getDate() {
		// 시작일~
		String startYear = combStartYUpdate.getSelectedItem().toString();
		// 월&일이 반드시 2자리수를 가진다(%2s). 빈 자리는 0으로 대체한다(replace). -> 1월은 01월로
		String startMonth = String.format("%2s", combStartMUpdate.getSelectedItem().toString()).replace(" ", "0");
		String startDay = String.format("%2s", combStartDUpdate.getSelectedItem().toString()).replace(" ", "0");
		// 만료일~
		String limitYear = combLimitYUpdate.getSelectedItem().toString();
		String limitMonth = String.format("%2s", combLimitMUpdate.getSelectedItem().toString()).replace(" ", "0");
		String limitDay = String.format("%2s", combLimitDUpdate.getSelectedItem().toString()).replace(" ", "0");
		// 얻은 문자열을 하나의 start 날짜와 하나의 limit 날짜로 합쳐준다.
		String startStr = String.format("%s-%s-%s", startYear, startMonth, startDay);
		String limitStr = String.format("%s-%s-%s", limitYear, limitMonth, limitDay);
		// 합친 문자열을 LocalDate 객체로 변환.
		startDate = LocalDate.parse(startStr, DateTimeFormatter.ISO_DATE);
		limitDate = LocalDate.parse(limitStr, DateTimeFormatter.ISO_DATE);
	}

	// TODO 업뎃 기능 다시

	private void updateTask() {
		String title = textTitleUpdate.getText();
		String content = textContentUpdate.getText();
		String category=null;
		if (combCategory.getSelectedItem() != null) {
			category = combCategory.getSelectedItem().toString();
		} else {
			JOptionPane.showMessageDialog(TaskDetailsFrame.this, "카테고리를 적어주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		getDate();
		if (title.equals("") || content.equals("")) {
			JOptionPane.showMessageDialog(this, "제목, 내용을 모두 적어주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Task updatedTask = new Task(taskId, title, content, state, startDate, limitDate, category, userId);
		int result = dao.update(updatedTask);
		if (result == 1) {
			JOptionPane.showMessageDialog(TaskDetailsFrame.this, "업데이트 성공");
			app.initializeTable();
			dispose();
		} else {
			JOptionPane.showMessageDialog(TaskDetailsFrame.this, "업데이트 실패");
			dispose();

		}

	}
}
