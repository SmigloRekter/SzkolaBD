package szkola.class_package;
import szkola.class_package.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetSingleClass extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * DAO - typ klasy odpowiadaj¹cy za po³¹czenie
	 * aplikacji ze Ÿród³em danych, np. baz¹ danych.
	 * To tutaj bêdzie znajdowaæ siê implementacja
	 * JDBC.
	 */
	private ClassesDAO classesDAO;

	//inicjalizacja komponentów
	public GetSingleClass() {
		classesDAO = new ClassesDAO();
	}

	//pobieranie artyku³u z http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		//Wykorzystaj parser do klas i pobierz dane klasy z requesta
		//Classes cls = sqlClassesParser.parseArticleFromRequest(request);
		//sqlClassesParser.parseClassFromRequest(request);
		//zapisz klase do bazy danych
		//classesDAO.save(cls);
		//PrintWriter out = response.getWriter();
		//out.print("Hello World");
		//String query = "";
		//query = "INSERT INTO class VALUES (NULL, '" + cls.getName() + "', '" + cls.getTeacherid() +"');";
		//return query;
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print(classesDAO.getSingle(request));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print(classesDAO.removeSingle(request));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}