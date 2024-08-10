import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String location = request.getParameter("location");
        
        try {
            Connection con = JDBCcode.connectDB();
            PreparedStatement ps = null;
            String sql = "INSERT INTO users (username, password, gender, dob, location) VALUES (?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, gender);
            ps.setDate(4, java.sql.Date.valueOf(dob));
            ps.setString(5, location);
            ps.executeUpdate();
            response.sendRedirect("login.html");
            con.close();
        } catch (Exception e) {
        	response.sendRedirect("registererror.html");
        }
    }
}
