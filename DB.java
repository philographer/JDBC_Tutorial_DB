import java.sql.*;

public class DB {

	private Connection con;
	private Statement stmt;

	public DB()
	{
		//JDBC설정
		try {
			String driverName ="org.gjt.mm.mysql.Driver";		// JDBC 드라이버에 따라 다르게 설정
			Class.forName(driverName);							// 드라이버이름 설정
			String url = "jdbc:mysql://localhost:3306/university"; //university
			con = DriverManager.getConnection(url, "root", "");	 //  connection 설정
			stmt= con.createStatement();			//쿼리문을 실행할 statement 설정

		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");		//쿼리의 실행 결과를 출력합니다.
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQLException");		//쿼리의 실행 결과를 출력합니다.
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("All Exception");
		}
	}// constructor


	public void selectQueryOne()
	{
		String query="SELECT * FROM(\n" +
				"SELECT id,name, sum(credits) AS nowCredit,sum(score * credits) / sum(credits) AS average\n" +
				"FROM \n" +
				"(SELECT id,name,tot_cred,course_id,grade,\n" +
				"CASE grade\n" +
				"\t\tWHEN \"A+\" THEN 4.3\n" +
				"\t\tWHEN \"A\"  THEN 4.0\n" +
				"\t\tWHEN \"A-\" THEN 3.7\n" +
				"\t\tWHEN \"B+\" THEN 3.3\n" +
				"\t\tWHEN \"B\"  THEN 3.0\n" +
				"\t\tWHEN \"B-\" THEN 2.7\n" +
				"\t\tWHEN \"C+\" THEN 2.3\n" +
				"\t\tWHEN \"C\"  THEN 2.0\n" +
				"\t\tWHEN \"C-\" THEN 1.7\n" +
				"\t\tWHEN \"D+\" THEN 1.3\n" +
				"\t\tWHEN \"D\"  THEN 1.0\n" +
				"\t\tWHEN \"D-\" THEN 0.7\n" +
				"\t\tWHEN \"F\"  THEN 0\n" +
				"\tEND AS score\n" +
				"FROM student NATURAL JOIN takes) AS T1 \n" +
				"NATURAL JOIN course GROUP BY id) AS T2\n" +
				"WHERE average > 3.5 ORDER BY average";		// attribute와 table이름에 따라 다르게 설정하세요.
		ResultSet rs;

		try {

			rs = stmt.executeQuery(query);		// 쿼리를 실행 시킵니다.


			System.out.format("%-20s%-20s%-20s%-20s\n", "ID", "이름", "이수학점", "평점");
			while(rs.next())					//쿼리 실행 결과 다수의 라인을 포함하는 경우 반복문을 통하여 결과를 가져옵니다.
			{
				System.out.format("%-20s%-20s%-20s%-20s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));

			}//while

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");		//쿼리의 실행 결과를 출력합니다.
			e.printStackTrace();
		}	//catch
	}//selectQueryone

	public void selectQueryTwo(String paramDept)
	{
		ResultSet rs;
		String query="";

		query="SELECT * FROM(\n" +
				"SELECT id,name, sum(credits) AS nowCredit,sum(score * credits) / sum(credits) AS average\n" +
				"FROM \n" +
				"(SELECT id,name,tot_cred,course_id,grade,\n" +
				"CASE grade\n" +
				"\t\tWHEN \"A+\" THEN 4.3\n" +
				"\t\tWHEN \"A\"  THEN 4.0\n" +
				"\t\tWHEN \"A-\" THEN 3.7\n" +
				"\t\tWHEN \"B+\" THEN 3.3\n" +
				"\t\tWHEN \"B\"  THEN 3.0\n" +
				"\t\tWHEN \"B-\" THEN 2.7\n" +
				"\t\tWHEN \"C+\" THEN 2.3\n" +
				"\t\tWHEN \"C\"  THEN 2.0\n" +
				"\t\tWHEN \"C-\" THEN 1.7\n" +
				"\t\tWHEN \"D+\" THEN 1.3\n" +
				"\t\tWHEN \"D\"  THEN 1.0\n" +
				"\t\tWHEN \"D-\" THEN 0.7\n" +
				"\t\tWHEN \"F\"  THEN 0\n" +
				"\tEND AS score\n" +
				"FROM student NATURAL JOIN takes) AS T1 \n" +
				"NATURAL JOIN course GROUP BY id) AS T2\n" +
				"WHERE average > '"+paramDept+"' order by average";
		// 쿼리의 조건문을 파라미터로 받아 쿼리를 설정합니다

		try {

			rs = stmt.executeQuery(query);

			System.out.format("%-20s%-20s%-20s%-20s\n", "ID", "이름", "이수학점", "평점");
			while(rs.next())
			{
				System.out.format("%-20s%-20s%-20s%-20s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}//while
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("파라미터를 잘못 입력하셨습니다");
		}//catch

	}//selectQueryTwo


	public void selectQueryThree(String param)
	{
		//String query = "SELECT year, semester, course_id, title, credits, grade FROM( select id,name,course_id,semester,year,grade FROM student natural join takes) as T natural join course where name = " + "xue";

		ResultSet rs;

		try {
			PreparedStatement psmt = null;

			psmt = con.prepareStatement("select id, name from student where id = ?");
			psmt.setInt(1, Integer.parseInt(param));
			rs = psmt.executeQuery();
			while(rs.next())
			{
				System.out.format("학번: %s 이름: %s \n",rs.getString(1), rs.getString(2));
			}

			psmt = con.prepareStatement("SELECT year, semester, course_id, title, credits, grade FROM( select id,name,course_id,semester,year,grade FROM student natural join takes) as T natural join course where ID = ? order by year");
			psmt.setInt(1, Integer.parseInt(param));
			rs = psmt.executeQuery();

			System.out.format("%-18s%-19s%-19s%-38s%-19s%-19s\n", "해당년도","학기","과목번호","과목명","학점","성적");
			while(rs.next())
			{
				System.out.format("%-20s%-20s%-20s%-40s%-20s%-20s\n", rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
				//System.out.println(rs.getString(1));
			}

			psmt = con.prepareStatement("SELECT sum(credits)\n" +
					" FROM( SELECT id,name,course_id,semester,year,grade FROM student NATURAL JOIN takes) AS T NATURAL JOIN course WHERE ID = ? ORDER BY year");
			psmt.setInt(1, Integer.parseInt(param));
			rs = psmt.executeQuery();

			while(rs.next())
			{
				System.out.format("총 이수학점: %s\n", rs.getString(1));
				//System.out.println(rs.getString(1));
			}

			psmt = con.prepareStatement("SELECT id,name,sum(score*credits) / sum(credits) AS average FROM\n" +
					"(SELECT id,name,course_id,grade,\n" +
					"CASE grade\n" +
					"\t\tWHEN \"A+\" THEN 4.3\n" +
					"\t\tWHEN \"A\"  THEN 4.0\n" +
					"\t\tWHEN \"A-\" THEN 3.7\n" +
					"\t\tWHEN \"B+\" THEN 3.3\n" +
					"\t\tWHEN \"B\"  THEN 3.0\n" +
					"\t\tWHEN \"B-\" THEN 2.7\n" +
					"\t\tWHEN \"C+\" THEN 2.3\n" +
					"\t\tWHEN \"C\"  THEN 2.0\n" +
					"\t\tWHEN \"C-\" THEN 1.7\n" +
					"\t\tWHEN \"D+\" THEN 1.3\n" +
					"\t\tWHEN \"D\"  THEN 1.0\n" +
					"\t\tWHEN \"D-\" THEN 0.7\n" +
					"\t\tWHEN \"F\"  THEN 0\n" +
					"END AS score\n" +
					"FROM student NATURAL JOIN takes) AS T2\n" +
					"NATURAL JOIN course\n" +
					"WHERE id = ? order by average");
			psmt.setInt(1, Integer.parseInt(param));
			rs = psmt.executeQuery();

			while(rs.next())
			{
				System.out.format("평점 평균: %s\n", rs.getString(3));
				//System.out.println(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("쿼리를 잘못 입력하셨습니다" + e.toString());
		}//catch
	}//method

}//class