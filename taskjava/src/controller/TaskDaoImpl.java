package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Task;
import oracle.jdbc.OracleDriver;

import static jdbc.OracleJdbc.*;
import static model.Task.Entity.*;

public class TaskDaoImpl implements TaskDao {

	private static TaskDaoImpl instance = null;

	TaskDaoImpl() {
		try {
			DriverManager.registerDriver(new OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// singleton
	public static TaskDaoImpl getInstance() {
		if (instance == null) {
			instance = new TaskDaoImpl();
		}
		return instance;
	}

	// -- singleton

	private static final String SQL_READ_CATEGORY_NAMES = String.format("select %s from %s where %s=? order by %s",
			COL_CATEGORY, TBL_TASK, COL_USER_ID, COL_CATEGORY);

	public List<String> setCateToCombobox(String userId) {
		List<String> cateList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_READ_CATEGORY_NAMES);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String cate = rs.getString(COL_CATEGORY);
				cateList.add(cate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return cateList;

	}

	private void closeResourcec(Connection conn, Statement stmt) {
		closeResources(conn, stmt, null);
	}

	private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (conn != null)
				conn.close();
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Task makeTaskFromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(COL_ID);
		String title = rs.getString(COL_TITLE);
		String content = rs.getString(COL_CONTENT);
		int state = rs.getInt(COL_STATE);
		LocalDate startDate = rs.getDate(COL_START_DATE).toLocalDate();
		LocalDate limitDate = rs.getDate(COL_LIMIT_DATE).toLocalDate();
		String category = rs.getString(COL_CATEGORY).toString();
		String userId = rs.getString(COL_USER_ID);
		Task task = new Task(id, title, content, state, startDate, limitDate, category, userId);
		return task;

	}

	private String SQL_INSERT = String.format("insert into %s(%s,%s,%s,%s,%s,%s) values(?,?,?,?,?,?)", TBL_TASK,
			COL_TITLE, COL_CONTENT, COL_START_DATE, COL_LIMIT_DATE, COL_CATEGORY, COL_USER_ID);

	@Override
	public int create(String userId, Task task) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_INSERT);
			stmt.setString(1, task.getTitle());
			stmt.setString(2, task.getContent());
			stmt.setObject(3, task.getStartDate());
			stmt.setObject(4, task.getLimitDate());
			stmt.setString(5, task.getCategory());
			stmt.setString(6, userId);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResourcec(conn, stmt);
		}
		return result;
	}

	private static final String SQL_UPDATE = String.format("update %s set %s=?, %s=?,%s=?,%s=?,%s=? where %s=?",
			TBL_TASK, COL_TITLE, COL_CONTENT, COL_START_DATE, COL_LIMIT_DATE, COL_CATEGORY, COL_ID);

	@Override
	public int update(Task task) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_UPDATE);
			stmt.setString(1, task.getTitle());
			stmt.setString(2, task.getContent());
			stmt.setObject(3, task.getStartDate());
			stmt.setObject(4, task.getLimitDate());
			stmt.setString(5, task.getCategory());
			stmt.setInt(6, task.getId());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResourcec(conn, stmt);
		}
		return result;
	}

	private static final String SQL_DELETE = String.format("delete from %s where %s=?", TBL_TASK, COL_ID);

	@Override
	public int delete(int id) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, id);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResourcec(conn, stmt);
		}

		return result;
	}

	@Override
	public int moveToLeft(int state, int id) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_MOVE_TO_RIGHT);
			// state를 하나 뺌
			stmt.setInt(1, state - 1);
			stmt.setInt(2, id);
			result = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResourcec(conn, stmt);
		}
		return result;
	}

	private static final String SQL_MOVE_TO_RIGHT = String.format("update %s set %s=? where %s=?", TBL_TASK, COL_STATE,
			COL_ID);

	@Override
	public int moveToRight(int state, int id) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_MOVE_TO_RIGHT);
			// state를 하나 더함
			stmt.setInt(1, state + 1);
			stmt.setInt(2, id);
			result = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResourcec(conn, stmt);
		}
		return result;
	}

	// 정렬

	private static final String SQL_READAll_ID = String.format("select * from %s where %s=? order by %s", TBL_TASK,
			COL_USER_ID, COL_ID);
	private static final String SQL_READAll_CATEGORY = String.format("select * from %s where %s=? order by %s",
			TBL_TASK, COL_USER_ID, COL_CATEGORY);
	private static final String SQL_READAll_TITLE = String.format("select * from %s where %s=? order by %s", TBL_TASK,
			COL_USER_ID, COL_TITLE);
	private static final String SQL_READAll_START = String.format("select * from %s where %s=? order by %s", TBL_TASK,
			COL_USER_ID, COL_START_DATE);
	private static final String SQL_READAll_LIMIT = String.format("select * from %s where %s=? order by %s ", TBL_TASK,
			COL_USER_ID, COL_LIMIT_DATE);

	@Override
	public List<Task> readAll(String userId, int arr) {
		List<Task> tasks = new ArrayList<>(); // 처음 읽을때, 표를 비운뒤 데이터를 가져와야 하므로, 여기서 list를 생성한다.
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			switch (arr) {
			case 0:
				stmt = conn.prepareStatement(SQL_READAll_ID);
				break;
			case 1:
				stmt = conn.prepareStatement(SQL_READAll_CATEGORY);
				break;
			case 2:
				stmt = conn.prepareStatement(SQL_READAll_TITLE);
				break;
			case 3:
				stmt = conn.prepareStatement(SQL_READAll_START);
				break;
			case 4:
				stmt = conn.prepareStatement(SQL_READAll_LIMIT);
				break;

			}
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Task task = makeTaskFromResultSet(rs);
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return tasks;
	}

	private static final String SQL_READ = String.format("select * from %s where %s=? and %s=? order by %s", TBL_TASK,
			COL_ID, COL_USER_ID, COL_ID);

	@Override
	public Task read(String userId, int taskId) {
		Task task = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_READ);
			stmt.setInt(1, taskId);
			stmt.setString(2, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				task = makeTaskFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return task;

	}

	private static final String SQL_SEARCH_CATEGORY = String.format(
			"select * from %s where (lower(%s) like ? or lower(%s) like ? or lower(%s) like ?) and %s=? order by %s",
			TBL_TASK, COL_CATEGORY, COL_TITLE, COL_CONTENT, COL_USER_ID, COL_CATEGORY);

	private static final String SQL_SEARCH_TITLE = String.format(
			"select * from %s where (lower(%s) like ? or lower(%s) like ? or lower(%s) like ?) and %s=? order by %s",
			TBL_TASK, COL_CATEGORY, COL_TITLE, COL_CONTENT, COL_USER_ID, COL_TITLE);

	private static final String SQL_SEARCH_START = String.format(
			"select * from %s where (lower(%s) like ? or lower(%s) like ? or lower(%s) like ?) and %s=? order by %s",
			TBL_TASK, COL_CATEGORY, COL_TITLE, COL_CONTENT, COL_USER_ID, COL_START_DATE);

	private static final String SQL_SEARCH_LIMIT = String.format(
			"select * from %s where (lower(%s) like ? or lower(%s) like ? or lower(%s) like ?) and %s=? order by %s",
			TBL_TASK, COL_CATEGORY, COL_TITLE, COL_CONTENT, COL_USER_ID, COL_LIMIT_DATE);

	@Override
	public List<Task> search(String userId, String keyword, int arr) {
		Task task = null;
		List<Task> tasks = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String searchKeyword = "%" + keyword.toLowerCase() + "%";
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			switch (arr) {
			case 0:
				stmt = conn.prepareStatement(SQL_SEARCH_CATEGORY);
				break;
			case 1:
				stmt = conn.prepareStatement(SQL_SEARCH_TITLE);
				break;
			case 2:
				stmt = conn.prepareStatement(SQL_SEARCH_START);
				break;
			case 3:
				stmt = conn.prepareStatement(SQL_SEARCH_LIMIT);
				break;
			}
			stmt.setString(1, searchKeyword);
			stmt.setString(2, searchKeyword);
			stmt.setString(3, searchKeyword);
			stmt.setString(4, userId);
			// System.out.println(userId);
			// stmt.setObject(3, keyword);
			rs = stmt.executeQuery();
			while (rs.next()) {
				task = makeTaskFromResultSet(rs);
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}

		return tasks;
	}

}
