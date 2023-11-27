package ua.edu.ucu.apps;
import java.sql.*;

public class CachedDocument extends SmartDocument {
    private static final String DB_URL = "jdbc:sqlite:path_to_your_database.db";

    public CachedDocument(String gcsPath) {
        super(gcsPath);
    }

    @Override
    public String parse() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String cachedText = getCachedText(conn);
            if (cachedText != null) {
                return cachedText;
            } else {
                String parsedText = super.parse();
                cacheText(conn, parsedText);
                return parsedText;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return super.parse();
        }
    }

    private String getCachedText(Connection conn) throws SQLException {
        String sql = "SELECT content FROM cache WHERE gcsPath = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.gcsPath);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("content");
            }
        }
        return null;
    }

    private void cacheText(Connection conn, String text) throws SQLException {
        String sql = "INSERT INTO cache (gcsPath, content) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.gcsPath);
            pstmt.setString(2, text);
            pstmt.executeUpdate();
        }
    }
}
