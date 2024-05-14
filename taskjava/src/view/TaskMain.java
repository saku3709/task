package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.w3c.dom.events.Event;

import controller.TaskDao;
import controller.TaskDaoImpl;
import controller.UserDao;
import model.Task;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

import static model.Task.Entity.*;
import static view.TaskDetailsFrame.showTaskDetailsFrame;
import static view.TaskLogin.resetText;

import javax.swing.JComboBox;
import javax.swing.BoxLayout;

public class TaskMain {

	private static final String[] COLUMN_NAMES = { "id", "category", "title", "content", "start_date", "dead_line" };
	private static final String[] COMBO_ARRANGE_NAMES = { "id","category", "title", "start_date", "dead_line" };
	private static final String SEARCH_STRING = "search..";
	private static TaskDao dao = TaskDaoImpl.getInstance();
	private static final int COL_INDEX_ID = 0;
	private Color white = Color.white;
	// private static final int COL_INDEX_STATE = 3;
	private static int arrIndex = 0;
	private Component parent = null;
	private String userId;

	private JFrame frame;
	private static JTabbedPane tabbedPane;
	private JPanel orderPanel;
	private JScrollPane scrollTodo;
	private JScrollPane scrollDone;
	private static JTable tableTodo;
	private static JTable tableDone;
	private static JTable tableInprogress;

	private static DefaultTableModel tableModelTodo;
	private static DefaultTableModel tableModelInprogress;
	private static DefaultTableModel tableModelDone;

	private JScrollPane scrollInprogress;
	private JPanel btnPanel;
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnToLeft;
	private JButton btnDelete;
	private JButton btnToRight;
	private JTextField textSearch;

	// tabbedPane index
	int indexFromtabbedPane = 0;
	// row index
	int rowIndexFromTodo = 0;
	int rowIndexFromInprogress = 0;
	int rowIndexFromDone = 0;

	// col_id_index
	int idIndexFromTodo = 0;
	int idIndexFromInprogress = 0;
	int idIndexFromDone = 0;

	// col_state_index
	int stIndexFromTodo = 0;
	int stIndexFromInprogress = 0;
	int stIndexFromDone = 0;
	private JComboBox comboArrange;
	private JButton btnReadAll;
	private JButton btnLogout;

	/**
	 * Launch the application.
	 */
	public static void showTaskMain(Component parent, String id) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskMain window = new TaskMain(parent, id);
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
	public TaskMain(Component parent, String id) {
		this.parent = parent;
		this.userId = id;
		initialize();
		initializeTable();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int x = 0;
		int y = 0;
		frame = new JFrame();
		if (parent != null) {
			x = parent.getX();
			y = parent.getY();
		}
		frame.setBounds(x, y, 600, 770);
		if (parent == null) {
			frame.setLocationRelativeTo(null);
		}
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("TASK");
		frame.getContentPane().setLayout(null);

		orderPanel = new JPanel();
		orderPanel.setBounds(381, 70, 190, 25);
		frame.getContentPane().add(orderPanel);

		// 정렬
		comboArrange = new JComboBox<>();
		comboArrange.addActionListener(e -> arrangeTable());
		DefaultComboBoxModel<String> combModelArr = new DefaultComboBoxModel<>(COMBO_ARRANGE_NAMES);
		orderPanel.setLayout(new BorderLayout(0, 0));
		comboArrange.setModel(combModelArr);
		comboArrange.setFont(new Font("D2Coding", Font.PLAIN, 17));
		orderPanel.add(comboArrange);

		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(381, 42, 190, 25);
		frame.getContentPane().add(searchPanel);
		searchPanel.setLayout(new BorderLayout(0, 0));

		textSearch = new JTextField();
		textSearch.setForeground(Color.gray);
		textSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textSearch.setText("");
				resetTabbedPane();
			};
		});
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					search();
				}
			}
		});
		textSearch.setHorizontalAlignment(SwingConstants.TRAILING);
		textSearch.setFont(new Font("D2Coding", Font.PLAIN, 17));
		searchPanel.add(textSearch);
		textSearch.setColumns(10);
		textSearch.setText(SEARCH_STRING);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.setBounds(12, 70, 560, 645);
		frame.getContentPane().add(tabbedPane);
		scrollTodo = new JScrollPane();

		JLabel tolabel = new JLabel();

		tabbedPane.setFont(new Font("D2Coding", Font.PLAIN, 16));
		// TODO tabbedpane의 이름변경기능 - Change tab name for JTabbedPane
		tabbedPane.addTab("To-Do", scrollTodo);

		tableTodo = new JTable();
		tableTodo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					getIndexFromTable();
					showTaskDetailsFrame(frame, TaskMain.this, idIndexFromTodo, indexFromtabbedPane, userId);

				}
			}
		});
		tableTodo.setFont(new Font("D2Coding", Font.PLAIN, 16));
		tableModelTodo = new DefaultTableModel(null, COLUMN_NAMES); // model
		tableTodo.setModel(tableModelTodo);
		scrollTodo.setViewportView(tableTodo);

		tableInprogress = new JTable();
		tableInprogress.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					getIndexFromTable();
					showTaskDetailsFrame(frame, TaskMain.this, idIndexFromInprogress, indexFromtabbedPane, userId);

				}
			}
		});
		tableInprogress.setFont(new Font("D2Coding", Font.PLAIN, 16));
		tableModelInprogress = new DefaultTableModel(null, COLUMN_NAMES);
		tableInprogress.setModel(tableModelInprogress);

		scrollInprogress = new JScrollPane();
		tabbedPane.addTab("In Progress", scrollInprogress);

		scrollInprogress.setViewportView(tableInprogress);

		scrollDone = new JScrollPane();
		tabbedPane.addTab("Done", scrollDone);

		tableDone = new JTable();
		tableDone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					getIndexFromTable();
					showTaskDetailsFrame(frame, TaskMain.this, idIndexFromDone, indexFromtabbedPane, userId);

				}
			}
		});

		tableDone.setFont(new Font("D2Coding", Font.PLAIN, 16));
		tableModelDone = new DefaultTableModel(null, COLUMN_NAMES);
		tableDone.setModel(tableModelDone);

		// 테이블 열 font 및 크기
		tableTodo.getTableHeader().setFont(new Font("D2Coding", Font.PLAIN, 16));
		tableInprogress.getTableHeader().setFont(new Font("D2Coding", Font.PLAIN, 16));
		tableDone.getTableHeader().setFont(new Font("D2Coding", Font.PLAIN, 16));

		// 테이블 행 높이
		tableTodo.setRowHeight(30);
		tableInprogress.setRowHeight(30);
		tableDone.setRowHeight(30);

		scrollDone.setViewportView(tableDone);

		btnPanel = new JPanel();
		btnPanel.setBounds(0, 0, 584, 37);
		frame.getContentPane().add(btnPanel);

		// color
		frame.setBackground(white);
		scrollTodo.setBackground(white);
		btnPanel.setBackground(white);
		btnPanel.setLayout(null);

		btnLogout = new JButton("SignOut");
		btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
		btnLogout.setBounds(0, 2, 104, 31);
		btnPanel.add(btnLogout);
		// 버튼 border 없애기 설정.
		btnLogout.setBorderPainted(false);
		btnLogout.setFocusable(false);
		btnLogout.setBackground(Color.white);
		btnLogout.setFont(new Font("D2Coding", Font.PLAIN, 17));
		btnLogout.addActionListener(e -> logOut());

		btnAdd = new JButton("Add");
		btnAdd.setHorizontalAlignment(SwingConstants.LEFT);
		btnAdd.setBounds(105, 2, 69, 31);
		btnAdd.setFocusable(false);
		btnAdd.setBorderPainted(false);
		btnAdd.setBackground(white);
		btnPanel.add(btnAdd);
		btnAdd.addActionListener(e -> TaskAddFrame.showTaskAddFrame(frame, TaskMain.this, userId));
		btnAdd.setFont(new Font("D2Coding", Font.PLAIN, 17));

		btnUpdate = new JButton("Details");
		btnUpdate.setBounds(174, 2, 104, 31);
		btnUpdate.setBorderPainted(false);
		btnUpdate.setFocusable(false);
		btnUpdate.setBackground(white);
		btnPanel.add(btnUpdate);
		btnUpdate.addActionListener(e -> detailsTask());

		btnUpdate.setHorizontalAlignment(SwingConstants.LEFT);
		btnUpdate.setFont(new Font("D2Coding", Font.PLAIN, 17));

		btnDelete = new JButton("Delete");
		btnDelete.setHorizontalAlignment(SwingConstants.LEFT);
		btnDelete.setBounds(279, 2, 95, 31);
		btnDelete.setBorderPainted(false);
		btnDelete.setFocusable(false);
		btnDelete.setBackground(white);
		btnPanel.add(btnDelete);
		btnDelete.addActionListener(e -> deleteTask());
		btnDelete.setFont(new Font("D2Coding", Font.PLAIN, 17));

		btnToLeft = new JButton("◀-");
		btnToLeft.setHorizontalAlignment(SwingConstants.LEFT);
		btnToLeft.setBounds(376, 2, 61, 31);
		btnToLeft.setBackground(white);
		btnToLeft.setFocusable(false);
		btnToLeft.setBorderPainted(false);
		btnPanel.add(btnToLeft);
		btnToLeft.addActionListener(e -> taskMoveToLeft());
		btnToLeft.setFont(new Font("D2Coding", Font.PLAIN, 17));

		btnToRight = new JButton("-▶");
		btnToRight.setHorizontalAlignment(SwingConstants.LEFT);
		btnToRight.setBounds(437, 2, 61, 31);
		btnToRight.setBackground(white);
		btnToRight.setBorderPainted(false);
		btnToRight.setFocusable(false);
		btnPanel.add(btnToRight);
		btnToRight.setVerticalAlignment(SwingConstants.BOTTOM);
		btnToRight.addActionListener(e -> taskMoveToRight());
		btnToRight.setFont(new Font("D2Coding", Font.PLAIN, 17));

		btnReadAll = new JButton("List");
		btnReadAll.setHorizontalAlignment(SwingConstants.LEFT);
		btnReadAll.setBounds(500, 2, 84, 31);
		btnReadAll.setBorderPainted(false);
		btnReadAll.setBackground(white);
		btnReadAll.setFocusable(false);
		btnPanel.add(btnReadAll);
		// TODO search 시험 끝나면 리스트 읽기로 바꾸기.
		btnReadAll.addActionListener(e -> initializeTable());

		btnReadAll.setFont(new Font("D2Coding", Font.PLAIN, 17));

		JPanel userPanel = new JPanel();
		userPanel.setBounds(22, 45, 209, 33);
		frame.getContentPane().add(userPanel);
		userPanel.setLayout(new BorderLayout(0, 0));
		// TODO:
		JLabel lblUser = new JLabel("User:" + userId);
		userPanel.add(lblUser, BorderLayout.CENTER);
		lblUser.setFont(new Font("D2Coding", Font.ITALIC, 15));

		// tabbedPane.setForeground(white);
		tabbedPane.setBackground(white);
		//tabbedPane.setBackgroundAt(1, white);
		//tabbedPane.setBackgroundAt(x, white);
	}

	private void logOut() {
		// taskMain 뷰에서 로그아웃 버튼 누를 시
		frame.dispose();
		resetText();
	}

	private void search() {
		String keyword = textSearch.getText();
		int arr = comboArrange.getSelectedIndex();
		if (keyword.equals("") || keyword.equals(SEARCH_STRING)) {
			JOptionPane.showMessageDialog(frame, "검색어를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			textSearch.requestFocus(); // 검색어 입력 JTextField에 포커스를 줌(커서 깜박깜박).
			return;
		}
		List<Task> tasks = dao.search(userId, keyword, arr);
		resetTable(tasks);
		// tabbedPane 색상 변하게
		Color color = Color.green;
		if (tableTodo.getRowCount() > 0) {
			tabbedPane.setBackgroundAt(0, color);
		}
		if (tableInprogress.getRowCount() > 0) {
			tabbedPane.setBackgroundAt(1, color);
		}
		if (tableDone.getRowCount() > 0) {
			tabbedPane.setBackgroundAt(2, color);

		}

	}

	private void arrangeTable() {
		arrIndex = comboArrange.getSelectedIndex();
		if (textSearch.getText() != null && !textSearch.getText().equals(SEARCH_STRING)) {
			search();
			return;
		}
		initializeTable();
	}

	public void getIndexFromTable() {
		// tabbedPane index
		indexFromtabbedPane = tabbedPane.getSelectedIndex(); // 현재 tab

		// row index
		rowIndexFromTodo = tableTodo.getSelectedRow();
		rowIndexFromInprogress = tableInprogress.getSelectedRow();
		rowIndexFromDone = tableDone.getSelectedRow();
		// column index
		if (rowIndexFromTodo != -1 && rowIndexFromInprogress == -1 && rowIndexFromDone == -1) { // todo 테이블의 행을 선택했다면
			// todo 테이블에서 선택한 행의(index) 0번째 열의 값 가져옴 -> id
			idIndexFromTodo = (int) tableModelTodo.getValueAt(rowIndexFromTodo, COL_INDEX_ID);
			// todo 테이블에서 선택한 행의 3번째 열의 값 가져옴 -> state
			stIndexFromTodo = dao.read(userId, idIndexFromTodo).getState();
			// stIndexFromTodo = (int) tableModelTodo.getValueAt(rowIndexFromTodo,
			// COL_INDEX_STATE);

		}
		if (rowIndexFromInprogress != -1 && rowIndexFromTodo == -1 && rowIndexFromDone == -1) { // inprogress 테이블의 행을
																								// 선택했다면
			idIndexFromInprogress = (int) tableModelInprogress.getValueAt(rowIndexFromInprogress, COL_INDEX_ID);
			stIndexFromInprogress = dao.read(userId, idIndexFromInprogress).getState();
			// stIndexFromInprogress = (int)
			// tableModelInprogress.getValueAt(rowIndexFromInprogress, COL_INDEX_STATE);
		}

		if (rowIndexFromDone != -1 && rowIndexFromTodo == -1 && rowIndexFromInprogress == -1) { // done 테이블의 행을 선택했다면
			idIndexFromDone = (int) tableModelDone.getValueAt(rowIndexFromDone, COL_INDEX_ID);
			stIndexFromDone = dao.read(userId, idIndexFromDone).getState();
			// stIndexFromDone = (int) tableModelDone.getValueAt(rowIndexFromDone,
			// COL_INDEX_STATE);
		}

	}

	private void taskMoveToLeft() {
		getIndexFromTable();

		// 테이블의 행 미선택시 -1리턴됨-> 경고 메세지
		if (rowIndexFromTodo != -1 && rowIndexFromInprogress == -1 && rowIndexFromDone == -1) {
			JOptionPane.showMessageDialog(frame, "좌측으로 더이상 옮길 수 없어요.", "to the left", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (rowIndexFromInprogress == -1 && rowIndexFromDone == -1) {
			JOptionPane.showMessageDialog(frame, "좌측으로 옮길 행을 선택하세요", "to the left", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (rowIndexFromInprogress != -1 && rowIndexFromDone == -1) { // inprogress 테이블의 행을 선택했다면
			dao.moveToLeft(stIndexFromInprogress, idIndexFromInprogress);
		} else if (rowIndexFromDone != -1 && rowIndexFromInprogress == -1) { // done 테이블의 행을 선택했다면
			dao.moveToLeft(stIndexFromDone, idIndexFromDone);
		}
		if (comboArrange != null && !textSearch.getText().equals(SEARCH_STRING)) {
			search();
			return;
		}
		initializeTable();

	}

	private void taskMoveToRight() {
		getIndexFromTable();
		if (rowIndexFromDone != -1 && rowIndexFromTodo == -1 && rowIndexFromInprogress == -1) {
			JOptionPane.showMessageDialog(frame, "우측으로 더 이상 옮길 수 없어요.", "to the right", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 테이블의 행 미선택시 -1리턴됨-> 경고 메세지
		if (rowIndexFromTodo == -1 && rowIndexFromInprogress == -1) {
			JOptionPane.showMessageDialog(frame, "우측으로 옮길 행을 선택하세요", "to the right", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (rowIndexFromTodo != -1 && rowIndexFromInprogress == -1) { // todo 테이블의 행을 선택했다면~
			dao.moveToRight(stIndexFromTodo, idIndexFromTodo);
		} else if (rowIndexFromInprogress != -1 && rowIndexFromTodo == -1) { // inprogress 테이블의 행을 선택했다면~
			dao.moveToRight(stIndexFromInprogress, idIndexFromInprogress);
		}
		if (comboArrange != null && !textSearch.getText().equals(SEARCH_STRING)) {
			search();
			return;
		}
		// 테이블 로딩
		initializeTable();

	}

	private void detailsTask() {
		getIndexFromTable();
		if (rowIndexFromTodo == -1 && rowIndexFromInprogress == -1 && rowIndexFromDone == -1) {
			JOptionPane.showMessageDialog(frame, "행을 선택하세요", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		switch (indexFromtabbedPane) {
		case 0:
			showTaskDetailsFrame(frame, TaskMain.this, idIndexFromTodo, indexFromtabbedPane, userId);
			break;
		case 1:
			showTaskDetailsFrame(frame, TaskMain.this, idIndexFromInprogress, indexFromtabbedPane, userId);
			break;
		case 2:
			showTaskDetailsFrame(frame, TaskMain.this, idIndexFromDone, indexFromtabbedPane, userId);
			break;
		}
	}

	private void deleteTask() {
		getIndexFromTable();
		int id = 0;
		if (rowIndexFromTodo == -1 && rowIndexFromInprogress == -1 && rowIndexFromDone == -1) {
			JOptionPane.showMessageDialog(frame, "삭제할 행을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (indexFromtabbedPane == 0) {
			id = idIndexFromTodo;
		} else if (indexFromtabbedPane == 1) {
			id = idIndexFromInprogress;
		} else {
			id = idIndexFromDone;
		}
		int result = dao.delete(id);
		if (result == 1) {
			initializeTable();
			JOptionPane.showMessageDialog(frame, "삭제 성공");
		} else {
			JOptionPane.showMessageDialog(frame, "삭제 실패");
		}

	}

	private void resetTable(List<Task> tasks) {
		tableModelTodo = new DefaultTableModel(null, COLUMN_NAMES);
		tableModelInprogress = new DefaultTableModel(null, COLUMN_NAMES);
		tableModelDone = new DefaultTableModel(null, COLUMN_NAMES);

		for (Task t : tasks) {

			// TODO: TABBED PANE의 이름에 개수 들어가게 -예시 to-do(3), inprogress(8)

			Object[] row = { t.getId(), t.getCategory(), t.getTitle(), t.getContent(), t.getStartDate(),
					t.getLimitDate() };
			switch (t.getState()) {
			case 0:
				tableModelTodo.addRow(row);
				break;
			case 1:
				tableModelInprogress.addRow(row);
				break;
			case 2:
				tableModelDone.addRow(row);
				break;
			}
		}
		tableTodo.setModel(tableModelTodo);
		tableInprogress.setModel(tableModelInprogress);
		tableDone.setModel(tableModelDone);
	}

	private void resetTabbedPane() {
		// 기본 색상으로 돌리기.
		tabbedPane.setBackgroundAt(0, null);
		tabbedPane.setBackgroundAt(1, null);
		tabbedPane.setBackgroundAt(2, null);
	}

	private void resetSearchBar() {
		// 검색바 기본 문장 돌리기.
		textSearch.setText(SEARCH_STRING);
	}

	void initializeTable() {
		List<Task> tasks = dao.readAll(userId, arrIndex);
		resetTable(tasks);
		resetTabbedPane();
		resetSearchBar();
		// hideColumnState();
	}
}
