package szkola.class_package;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.mysql.jdbc.ResultSetMetaData;

import szkola.Convertor;

public class ClassesDAO {

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

	public ClassesDAO() {
		//inicjalizacja parserów
		convertor = new Convertor();
	}
	
	public JSONArray getAll() throws Exception {
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("SELECT Class.ID, Class.TeacherID, Class.Name,Teacher.FirstName,Teacher.LastName,Teacher.Phone From Class JOIN Teacher ON Class.TeacherID=Teacher.ID;");
		ResultSet result = statement.executeQuery();
		JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return tmp;
	}
	
	
	public int getSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String teacher = request.getParameter("teacher");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("UPDATE Class SET Name = ?, TeacherID = ? WHERE ID= ?;");
		statement.setString(1, name);
		statement.setString(2, teacher);
		statement.setString(3, id);
		int result = statement.executeUpdate();
		//JSONArray tmp = convertor.convertResultSetIntoJSON(result);
		statement.close();
        connection.close();
        return result;
	}
	
	public int removeSingle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("DELETE FROM Class WHERE ID="+id+";");
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	
	public int addSingle(HttpServletRequest request) throws Exception {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
		Gson gson = new Gson();
		Classes myBean = gson.fromJson(sb.toString(), Classes.class);
		Class.forName(DBDRIVER).newInstance();
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		statement = connection.prepareStatement("INSERT INTO Class (name, teacherid) VALUES ('"+myBean.getName()+"',"+myBean.getTeacherid()+");");
		int result = statement.executeUpdate();
		statement.close();
        connection.close();
        return result;
	}
	
}