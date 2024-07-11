

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@WebServlet("/login")
public class Servlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public Servlet2() {
        super();
     
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			PrintWriter pw = response.getWriter();
	        response.setContentType("text/html");
	        Cookie[] cookies = request.getCookies();
	        String sessionId = null;
	        response.setContentType("text/html");
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("session_id")) {
	                    sessionId = cookie.getValue();
	                    break;
	                }
	            }
	        }
	
	        if (sessionId != null) {
	            ServletContext ctx = getServletContext();
	            String name = (String) ctx.getAttribute(sessionId);
	            if (name != null) {
	            	 pw.write("<h1>Welcome "+name+"<br>");
		             pw.write("<a href='login.html'><input type='submit' value='logout'></a>");
	            } else {
	                pw.write("<h1>Session invalid. Please log in again.</h1>");
	                response.sendRedirect("login.html");
	            }
	        } else {
	        	ServletContext ctx = getServletContext();
		        Connection con = (Connection) ctx.getAttribute("dbcon");
		         pw = response.getWriter();
		        ResultSet rs = null;
		        
		        if (con == null) {
		            pw.write("Database connection is not available");
		            return;
		        }
		
		        PreparedStatement ps;
		        String email = request.getParameter("email");
		        String password = request.getParameter("pass");
		        response.setContentType("text/html");
		        
		        try {
		            ps = con.prepareStatement("select * from user where email=? and password=?");
		            ps.setString(1, email);
		            ps.setString(2, password);
		            rs = ps.executeQuery();
		            if (rs.next()) {
		            	 sessionId = UUID.randomUUID().toString();
		                 
		                 Cookie sessionCookie = new Cookie("session_id", sessionId);
		                 sessionCookie.setMaxAge(60 * 30); 
		                 response.addCookie(sessionCookie);
		                 ctx.setAttribute(sessionId, rs.getString("name"));
		                pw.write("<h1>Welcome "+rs.getString("name")+"<br>");
		                pw.write("<a href='login.html'><input type='submit' value='logout'></a>");
		       
		            } else {
		                pw.write("\"<h1 style='position:absolute;top:0'>wrong credential....!!!</h1>");
		                request.getRequestDispatcher("login.html").include(request, response);
		            }
		            ps.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		            pw.write("SQL Exception: " + e.getMessage());
		        }
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
