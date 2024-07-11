

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Signup")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Servlet1() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = getServletContext();
        Connection con = (Connection) ctx.getAttribute("dbcon");
        PrintWriter pw = response.getWriter();
        
        if (con == null) {
            pw.write("Database connection is not available");
            return;
        }

        PreparedStatement ps;
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        response.setContentType("text/html");
        
        try {
            ps = con.prepareStatement("INSERT INTO user (name,email, password) VALUES (?,?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int result = ps.executeUpdate();
            if (result > 0) {
                pw.write("Registered successfully.. Please login to continue");
                request.getRequestDispatcher("registration.html").include(request, response);
       
            } else {
                pw.write("Registration failed");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            pw.write("SQL Exception: " + e.getMessage());
        }
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
