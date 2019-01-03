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

public class FeeDAO {

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


	public FeeDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	//Wylistowanie wszystkich op³at
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Fee;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	//Ustawienie statusu op³aty na op³acon¹
	public int paySingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("UPDATE Fee SET Status='1'WHERE ID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Usuniêcie op³aty
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Fee WHERE ID=?;");
		statement.setString(1, id);
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Dodanie op³aty
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		Fee myBean = gson.fromJson(sb.toString(), Fee.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT into Fee (Status, Amount, Title, StudentID) VALUES (?,?,?,?)");
		statement.setBoolean(1, myBean.isStatus());
		statement.setInt(2, myBean.getAmount());
		statement.setString(3, myBean.getTitle());
		statement.setInt(4, myBean.getStudentID());
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	//Wylistowanie op³at danego studenta
	public JSONArray getByStudentID(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT * FROM Fee WHERE StudentID = ?;");
		statement.setString(1, id);
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	
}
