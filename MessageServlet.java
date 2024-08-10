import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/sendMessage")
public class MessageServlet extends HttpServlet {
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String sender = (String) session.getAttribute("username");
        String content = request.getParameter("content");

        try {
            Connection con = JDBCcode.connectDB();
            String query = "INSERT INTO messages (sender, content) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, sender);
            stmt.setString(2, content);
            stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error sending message");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	Connection con = JDBCcode.connectDB();
            String query = "SELECT * FROM messages ORDER BY timestamp ASC";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            response.setContentType("application/json");
            response.getWriter().print("[");
            while (rs.next()) {
                response.getWriter().print("{");
                response.getWriter().print("\"sender\":\"" + rs.getString("sender") + "\",");
                response.getWriter().print("\"content\":\"" + rs.getString("content") + "\",");
                response.getWriter().print("\"timestamp\":\"" + rs.getTimestamp("timestamp") + "\"");
                response.getWriter().print("}");
                if (!rs.isLast()) response.getWriter().print(",");
            }
            response.getWriter().print("]");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error retrieving messages");
        }
    }
}
