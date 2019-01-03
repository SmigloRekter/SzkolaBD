package szkola.class_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClassroomController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	/**
	 * DAO - typ klasy odpowiadaj¹cy za po³¹czenie
	 * aplikacji ze Ÿród³em danych, np. baz¹ danych.
	 * To tutaj bêdzie znajdowaæ siê implementacja
	 * JDBC.
	 */
	private ClassroomDAO dao;

	//inicjalizacja komponentów
	public ClassroomController() {
		dao = new ClassroomDAO();
	}
	//pobieranie artyku³u z http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			if(request.getParameter("id")==null)
			out.print(dao.getAll());
			else
			out.print(dao.getSingle(request));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//pobieranie artyku³u z http DELETE
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print(dao.removeSingle(request));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//pobieranie artyku³u z http POST
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print(dao.addSingle(request));
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
