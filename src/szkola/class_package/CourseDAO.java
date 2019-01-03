package szkola.class_package;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.google.gson.Gson;

import szkola.Convertor;

public class CourseDAO {
	
	/**
	 * Tutaj nale¿y zdefiniowaæ u¿ytkownika, has³o, adres oraz
	 * sterownik do bazy danych z któr¹ zamierzamy siê po³¹czyæ.
	 */
	private final static String DBURL = "jdbc:mysql://104.248.143.154:3306/bd_projekt";
	private final static String DBUSER = "bd_projekt";
	private final static String DBPASS = "haslo2018";
	private final static String DBDRIVER = "com.mysql.jdbc.Driver";

	//obiekt tworz¹cy po³¹czenie z baz¹ danych.
	private Connection connection;
	//obiekt pozwalaj¹cy tworzyæ nowe wyra¿enia SQL
	private PreparedStatement statement;
	//zapytanie SQL
	private String query;
	//zamiana odpowiedzi bazy danych do obiektu typu JSON
	private Convertor convertor;

	public CourseDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	//Wylistowanie wszystkich zajêæ
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT Course.ID, Course.TeacherID, Course.Duration, Course.Name, Course.ClassroomID, Course.Date, Classroom.Name as 'ClassroomName',Teacher.FirstName,Teacher.LastName FROM Course JOIN Classroom on Course.ClassroomID = Classroom.ID JOIN Teacher on Course.TeacherID = Teacher.ID;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	//Edycja zajêæ
	public int editSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String teacher = request.getParameter("teacher");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("UPDATE Course SET Name = ?, TeacherID = ? WHERE ID= ?;");
		statement.setString(1, name);
		statement.setString(2, teacher);
		statement.setString(3, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Usuniêcie zajêæ
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Course WHERE ID= ?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie zajêæ
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
        String s;
        //wyci¹gniêcie z ¿¹dania http jsona
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		//zamiana jsona na obiekt
		Course myBean = gson.fromJson(sb.toString(), Course.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO Course (TeacherID, Duration, Name, ClassroomID, Date) VALUES (?,?,?,?,?);");
		statement.setInt(1, myBean.getTeacherID());
		statement.setInt(2, myBean.getDuration());
		statement.setString(3, myBean.getName());
		statement.setInt(4, myBean.getClassroomID());
		statement.setString(5, myBean.getDate());
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//listowanie zajêæ wybranego studenta
	public JSONArray getStudentsCourses(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Course JOIN Course_Student CS on Course.ID = CS.CourseID WHERE StudentID=?;");
		statement.setString(1, id);
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}

}
