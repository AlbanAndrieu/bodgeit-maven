<%@ page import="java.sql.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%!
	private Connection conn = null;

	public void jspInit() {
		try {
			// Get hold of the JDBC driver
			Class.forName("org.hsqldb.jdbcDriver" );
			// Establish a connection to an in memory db
			conn = DriverManager.getConnection("jdbc:hsqldb:mem:SQL", "sa", "");
		} catch (SQLException e) {
			getServletContext().log("Db error: " + e);
		} catch (Exception e) {
			getServletContext().log("System error: " + e);
		}
	}
	
	public void jspDestroy() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			getServletContext().log("Db error: " + e);
		} catch (Exception e) {
			getServletContext().log("System error: " + e);
		}
	}
%>
<jsp:include page="/header.jsp"/>

<h3>Admin page</h3>
<%
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
		if (session == null || ! "admin@thebodgeitstore.com".equals(session.getAttribute("username"))) {
			conn.createStatement().execute("UPDATE Score SET status = 1 WHERE task = 'HIDDEN_ADMIN'");
		} 
		
		stmt = conn.prepareStatement("SELECT * FROM Users");
		rs = stmt.executeQuery();
		out.println("<br/><center><table class=\"border\" width=\"80%\">");
		out.println("<tr><th>UserId</th><th>User</th><th>Role</th><th>BasketId</th></tr>");
		while (rs.next()) {
			out.println("<tr>");
			out.println("<td>" + rs.getInt("userid") + "</td><td>" + rs.getString("name") + 
					"</td><td>" + rs.getString("type") + "</td><td>" + rs.getInt("currentbasketid") + "</td>");
			out.println("</tr>");
		}
		out.println("</table></center><br/>");
		
		stmt = conn.prepareStatement("SELECT * FROM Baskets");
		rs = stmt.executeQuery();
		out.println("<br/><center><table class=\"border\" width=\"80%\">");
		out.println("<tr><th>BasketId</th><th>UserId</th><th>Date</th></tr>");
		while (rs.next()) {
			out.println("<tr>");
			out.println("<td>" + rs.getInt("basketid") + "</td><td>" + rs.getInt("userid") + 
					"</td><td>" + rs.getTimestamp("created") + "</td>");
			out.println("</tr>");
		}
		out.println("</table></center><br/>");
		
		stmt = conn.prepareStatement("SELECT * FROM BasketContents");
		rs = stmt.executeQuery();
		out.println("<br/><center><table class=\"border\" width=\"80%\">");
		out.println("<tr><th>BasketId</th><th>ProductId</th><th>Quantity</th></tr>");
		while (rs.next()) {
			out.println("<tr>");
			out.println("<td>" + rs.getInt("basketid") + "</td><td>" + rs.getInt("productid") + 
					"</td><td>" + rs.getInt("quantity") + "</td>");
			out.println("</tr>");
		}
		out.println("</table></center><br/>");
		
		
	} catch (SQLException e) {
		out.println("System error.<br/>" + e);
	} finally {
		if (stmt != null) {
			stmt.close();
		}
		if (rs != null) {
			rs.close();
		}
	}
%>

<jsp:include page="/footer.jsp"/>

