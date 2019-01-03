package szkola.class_package;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.google.gson.Gson;

import szkola.Convertor;

public class MarkDAO {

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

	public MarkDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	//Wylistowanie wszystkich ocen
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT Student.ID,Student.Phone,Student.FirstName,Student.LastName,Class.Name,Class.ID as 'classID' FROM Student JOIN Class ON Student.ClassID = Class.ID;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	//Wylistowanie ocen danego studenta
	public JSONArray getSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Mark JOIN Course C on Mark.CourseID = C.ID JOIN Mark_Student MS on Mark.ID = MS.MarkID join Student S on MS.StudentID = S.ID WHERE StudentID=?;");
		statement.setString(1, id);
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	//Usuniêcie oceny
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Mark WHERE ID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie oceny
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		int id=0;
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		Mark myBean = gson.fromJson(sb.toString(), Mark.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO Mark (TeacherID, Mark, CourseID) VALUES (?,?,?);",Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, myBean.getTeacherID());
		statement.setInt(2, myBean.getMark());
		statement.setInt(3, myBean.getCourseID());
		int result = statement.executeUpdate();
		ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()){
            id=rs.getInt(1);
        }
		statement = connection.prepareStatement("INSERT INTO Mark_Student (MarkID, StudentID) VALUES (?,?);");
		statement.setInt(1, id);
		statement.setInt(2, myBean.getStudentID());
		result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	
}
