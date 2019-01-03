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
	 * DAO - typ klasy odpowiadaj�cy za po��czenie
	 * aplikacji ze �r�d�em danych, np. baz� danych.
	 * To tutaj b�dzie znajdowa� si� implementacja
	 * JDBC.
	 */
	private ClassesDAO classesDAO;

	//inicjalizacja komponent�w
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