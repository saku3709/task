package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import model.User;
import oracle.jdbc.driver.OracleDriver;

import static jdbc.OracleJdbc.*;
import static model.User.Entity.*;

public class UserDao {
	// singleton
	private static UserDao instance = null;

	private UserDao() {
		try {
			DriverManager.registerDriver(new OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	// -- singleton
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

	private static final String SQL_CKECK_ID = String.format("select count(%s) from %s where %s=?", COL_ID, TBL_USER,
			COL_ID);

	/**
	 * id 중복검사
	 */
	public int checkId(String id) {
		int result = 1;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_CKECK_ID);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			rs.next(); // 행
			if (rs.getInt(1) == 1) { // 열
				result = -1;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return result;
	}

	private static final String SQL_SIGN_UP = String.format("insert into %s(%s,%s,%s) values(?,?,?)", TBL_USER, COL_ID,
			COL_NAME, COL_PASSWORD);

	/**
	 * 회원가입
	 */
	public int signUp(User user) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SIGN_UP);
			stmt.setString(1, user.getId());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getPassword());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResourcec(conn, stmt);
		}
		return result;
	}

	private static final String SQL_SIGN_IN = String.format("select * from %s where %s=? and %s=?", TBL_USER, COL_ID,
			COL_PASSWORD);

	/**
	 * 로그인
	 */
	public int signIn(String id, String pw) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SIGN_IN);
			stmt.setString(1, id);
			stmt.setString(2, pw);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}

		return -1;
	}

	public int signOut() {
		return 1;
	}

}
