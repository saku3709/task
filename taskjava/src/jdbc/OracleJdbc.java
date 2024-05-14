package jdbc;

public interface OracleJdbc {

	String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	// 다른 컴퓨터에서 시연할 때는 내 컴퓨터의(데이터베이스가 있는)  ip 주소 써야 한다.
	String USER = "scott";
	String PASSWORD = "tiger";
	//String USER="c##saku";
	//String PASSWORD="0414";
}
