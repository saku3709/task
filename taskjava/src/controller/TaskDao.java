package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Task;

public interface TaskDao {

	/**
	 * ResultSet으로부터 데이터를 읽어와서 Task 객체를 만든다.
	 * 
	 * @param ResultSet 객체
	 * @return Task 객체
	 * @throws SQLException
	 */
	Task makeTaskFromResultSet(ResultSet rs) throws SQLException;

	/**
	 * Task 객체를 TASK 테이블의 행에 삽입한다.
	 * 
	 * @param userId 로그인한 사용자 아이디
	 * @param task 객체
	 * @return 삽입된 행의 개수
	 */
	int create(String userId, Task task);

	/**
	 * Task 객체를 TASK 테이블의 index번째 행에 삽입한다.
	 * 
	 * @param index. Task 객체를 삽입할 테이블의 행 번호.
	 * @param task.  Task 객체
	 * @return 업데이트한 행의 개수
	 */
	int update(Task task);

	/**
	 * TASK 테이블의 index번째 행의 데이터를 삭제한다.
	 * 
	 * @param index. 삭제할 행의 번호
	 * @return 삭제한 행의 개수
	 */
	int delete(int index);

	/**
	 * TASK 테이블의 index 번째 행의 state를 왼쪽 state로 바꾼다.
	 * 
	 * @param index. TASK 테이블에서 수정할 행 번호
	 * @return 상태를 바꾼 행의 개수
	 */
	int moveToLeft(int state, int id);

	/**
	 * TASK 테이블의 index 번째 행의 state를 오른쪽 state로 바꾼다.
	 * 
	 * @param index. TASK 테이블에서 수정할 행 번호
	 * @return 상태를 바꾼 행의 개수
	 */
	int moveToRight(int state, int id);

	/**
	 * TASK 테이블의 행 전체를 읽는다.
	 * 
	 * @param userId 로그인한 사용자 아이디
	 * @param int.정렬을 하기 위한 기준. 0->id, 1->title, 2->start_date, 3-> limit_date
	 * @return Task 객체 타입의 List
	 */
	List<Task> readAll(String userid, int arr);

	/**
	 * TASK 테이블에서 행 하나의 데이터를 읽어온다.
	 * 
	 * @param userId 로그인한 사용자 아이디
	 * @param index. TASK 테이블에서 읽어올 행의 번호.
	 * @return Task 객체
	 */
	Task read(String userId, int index);

	/**
	 * keyword를 포함하는 Task들을 리스트로 읽어온다
	 * 
	 * @param userId 로그인 사용자 아이디.
	 * @param keyword 포함하는 keyword
	 * @param int.정렬을 하기 위한 기준. 0->id, 1->title, 2->start_date, 3-> limit_date
	 * @return Task의 List
	 */
	List<Task> search(String userId, String keyword, int arr);

}
