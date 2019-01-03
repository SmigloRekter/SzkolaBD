package szkola.class_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * DAO - typ klasy odpowiadaj�cy za po��czenie
	 * aplikacji ze �r�d�em danych, np. baz� danych.
	 * To tutaj b�dzie znajdowa� si� implementacja
	 * JDBC.
	 */
	private FeeDAO dao;

	//inicjalizacja komponent�w
	public FeeController() {
		dao = new FeeDAO();
	}
	//pobieranie artyku�u z http GET
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			if(request.getParameter("id")==null)
			out.print(dao.getAll());
			else
			out.print(dao.paySingle(request));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//pobieranie artyku�u z http DELETE
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
	//pobieranie artyku�u z http POST
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
	//pobieranie artyku�u z http PUT
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PrintWriter out = response.getWriter();
			out.print(dao.getByStudentID(request));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
