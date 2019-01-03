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

public class UserDAO {

	/**
	 * Tutaj nale�y zdefiniowa� u�ytkownika, has�o, adres oraz
	 * sterownik do bazy danych z kt�r� zamierzamy si� po��czy�.
	 */
	private final static String DBURL = "jdbc:mysql://104.248.143.154:3306/bd_projekt";
	private final static String DBUSER = "bd_projekt";
	private final static String DBPASS = "haslo2018";
	private final static String DBDRIVER = "com.mysql.jdbc.Driver";

	//obiekt tworz�cy po��czenie z baz� danych.
	private Connection connection;
	//obiekt pozwalaj�cy tworzy� nowe wyra�enia SQL
	private PreparedStatement statement;
	//zapytanie SQL
	private String query;
	//zamiana odpowiedzi bazy danych do obiektu typu JSON
	private Convertor convertor;

	public UserDAO() {
		//inicjalizacja parser�w
		convertor = new Convertor();
	}
	//Wylisotwanie u�ytkownik�w
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM User;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	//Logowanie
	public JSONArray getSingle(HttpServletRequest request) throws Exception {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM User WHERE Login=? AND Password=? LIMIT 1;");
		statement.setString(1, login);
		statement.setString(2, password);
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	//Usuni�cie u�ytkownika
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM User WHERE ID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie u�ytkownika
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		User myBean = gson.fromJson(sb.toString(), User.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO User (Login, Password, AccountType, AccountID) VALUES (?,?,?,?);");
		statement.setString(1, myBean.getLogin());
		statement.setString(2, myBean.getPassword());
		statement.setInt(3, 0);
		statement.setInt(4, 999);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	
}
