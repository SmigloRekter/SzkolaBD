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

public class GetAllClasses extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * DAO - typ klasy odpowiadaj¹cy za po³¹czenie
	 * aplikacji ze Ÿród³em danych, np. baz¹ danych.
	 * To tutaj bêdzie znajdowaæ siê implementacja
	 * JDBC.
	 */
	private ClassesDAO classesDAO;

	//inicjalizacja komponentów
	public GetAllClasses() {
		classesDAO = new ClassesDAO();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print(classesDAO.getAll());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}