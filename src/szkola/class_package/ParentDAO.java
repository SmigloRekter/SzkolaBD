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

public class ParentDAO {

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

	public ParentDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	//Wylistowanie wszystkich rodziców
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Parent;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	//wylistowanie rodziców wybranego studenta 
	public JSONArray getSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Parent_Student JOIN Parent P on Parent_Student.ParentID = P.ID JOIN Student S on Parent_Student.StudentID = S.ID WHERE StudentID= ?;");
		statement.setString(1, id);
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	//Usuniêcie rodzica
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Parent_Student WHERE ParentID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement = connection.prepareStatement("DELETE FROM Parent WHERE ID=?;");
		statement.setString(1, id);
		result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie rodzica wraz z kontem i przypisanie do ucznia
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        int id=0;
		Gson gson = new Gson();
		Parent myBean = gson.fromJson(sb.toString(), Parent.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO User (Login, Password, AccountType, AccountID) VALUES (?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, myBean.getLogin());
		statement.setString(2, myBean.getPassword());
		statement.setInt(3, 1);
		statement.setInt(4, 999);
		int result = statement.executeUpdate();
		ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()){
            id=rs.getInt(1);
        }
		statement = connection.prepareStatement("INSERT into Parent (FirstName, LastName, Phone, Address, City, Email, UserID) VALUES  (?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, myBean.getFirstName());
		statement.setString(2, myBean.getLastName());
		statement.setString(3, myBean.getPhone());
		statement.setString(4, myBean.getAddress());
		statement.setString(5, myBean.getCity());
		statement.setString(6, myBean.getEmail());
		statement.setInt(7, id);
		result = statement.executeUpdate();
		rs = statement.getGeneratedKeys();
        if (rs.next()){
            id=rs.getInt(1);
        }
		statement = connection.prepareStatement("INSERT INTO Parent_Student (ParentID, StudentID) VALUES (?,?);");
		statement.setInt(1, id);
		statement.setInt(2, myBean.getStudentID());
		 result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Wylistowanie ocen dzieci danego rodzica
	public JSONArray getChildsMarks(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT MS.StudentID, S.FirstName, S.LastName, S.ClassID, MS.MarkID,  M.Mark, CourseID FROM Parent JOIN Parent_Student PS on Parent.ID = PS.ParentID JOIN Student S on PS.StudentID = S.ID JOIN Mark_Student MS on S.ID = MS.StudentID JOIN Mark M on MS.MarkID = M.ID WHERE PS.ParentID=?;");
		statement.setString(1, id);
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
}

