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

public class TeacherDAO {

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

	

	public TeacherDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	//Wylistowanie nauczycieli
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Teacher;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	//Edycja nauczyciela
	public int getSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String phone = request.getParameter("phone");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("UPDATE Teacher SET FirstName = ?, LastName = ?,Phone=? WHERE ID=?;");
		statement.setString(1, firstname);
		statement.setString(2, lastname);
		statement.setString(3, phone);
		statement.setString(4, id);
		int result = statement.executeUpdate();
		//JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return result;
	}
	//Usuniêcie nauczyciela
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Teacher WHERE ID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie nauczyciela
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		int id=0;
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		TeacherWithUser myBean = gson.fromJson(sb.toString(), TeacherWithUser.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO User (Login, Password, AccountType, AccountID) VALUES (?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, myBean.getLogin());
		statement.setString(2, myBean.getPassword());
		statement.setInt(3, 2);
		statement.setInt(4, 999);
		int result = statement.executeUpdate();
		ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()){
            id=rs.getInt(1);
        }
		statement = connection.prepareStatement("INSERT INTO Teacher (FirstName, LastName, Phone, UserID) VALUES  (?,?,?,?)");
		statement.setString(1, myBean.getFirstName());
		statement.setString(2, myBean.getLastName());
		statement.setString(3, myBean.getPhone());
		statement.setInt(4, id);
		result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	
}
