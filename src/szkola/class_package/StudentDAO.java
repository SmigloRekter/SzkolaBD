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


public class StudentDAO {

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

	public StudentDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	//Wylistowanie uczniów
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
	
	//Edycja ucznia
	public int editSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String phone = request.getParameter("phone");
		String classid = request.getParameter("class");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("UPDATE Student SET FirstName = ?, LastName = ?,Phone= ?,ClassID = ? WHERE ID=?;");
		statement.setString(1, firstname);
		statement.setString(2, lastname);
		statement.setString(3, phone);
		statement.setString(4, classid);
		statement.setString(5, id);
		int result = statement.executeUpdate();
		//JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return result;
	}
	//Usuniêcie ucznia
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Student WHERE ID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie ucznia wraz z kontem
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		int id=0;
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		StudentWithUser myBean = gson.fromJson(sb.toString(), StudentWithUser.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO User (Login, Password, AccountType, AccountID) VALUES (?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, myBean.getLogin());
		statement.setString(2, myBean.getPassword());
		statement.setInt(3, 3);
		statement.setInt(4, 999);
		int result = statement.executeUpdate();
		ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()){
            id=rs.getInt(1);
        }
		statement = connection.prepareStatement("INSERT into Student (FirstName, LastName, Phone, ClassID, UserID) VALUES  (?,?,?,?,?)");
		statement.setString(1, myBean.getFirstname());
		statement.setString(2, myBean.getLastname());
		statement.setString(3, myBean.getPhone());
		statement.setInt(4, myBean.getClassID());
		statement.setInt(5, id);
		result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	
}