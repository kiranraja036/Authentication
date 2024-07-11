import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/Signup")
public class ServletFilter1 implements Filter {

   

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletContext ctx = request.getServletContext();
        Connection con = (Connection) ctx.getAttribute("dbcon");
        if (con == null) {
            throw new ServletException("Database connection is not initialized.");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        PrintWriter pw = response.getWriter();

        try {
            ps = con.prepareStatement("SELECT * FROM user WHERE email=?");
            ps.setString(1, request.getParameter("email"));
            rs = ps.executeQuery();
            if (rs.next()) {
                response.setContentType("text/html");
                pw.write("<h1 style='position:absolute;top:0'>Email Already Exists</h1>");
                request.getRequestDispatcher("registration.html").include(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database query failed.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
      
    }
}
