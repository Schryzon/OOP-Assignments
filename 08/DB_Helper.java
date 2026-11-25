import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public final class DB_Helper {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String PARAMS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_NAME = "click_rush_practice";
    private static final String USER = "root";
    private static final String PASS  = "";

    public static Connection get_connection() throws SQLException{
        return DriverManager.getConnection(BASE_URL + DB_NAME + PARAMS, USER, PASS);
    }

    public static void bootstrap() throws SQLException {
        try(Connection server = DriverManager.getConnection(BASE_URL + PARAMS, USER, PASS);
            Statement statement = server.createStatement()){
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }
        
        try(Connection conn = get_connection();
            Statement statement = conn.createStatement()){
                statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS players (
                        player_id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        password VARCHAR(100) NOT NULL
                    )    
                """);

                statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS runs (
                        run_id INT AUTO_INCREMENT PRIMARY KEY,
                        player_id INT NOT NULL,
                        clicks INT NOT NULL,
                        played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_runs_player FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
                    )
                """);
        }
    }

    public static int register(String username, String password) throws SQLException {
        String sql = "INSERT INTO players (username, password) VALUES (?, ?)";
        try(Connection conn = get_connection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                try(ResultSet keys = ps.getGeneratedKeys()){
                    if(keys.next()){
                        return keys.getInt(1);
                    }
                }
            }
        throw new SQLException("No generated player_id returned");    
    }

    public static int login(String username, String password) throws SQLException {
        String sql = "SELECT player_id FROM players WHERE username = ? AND password = ?";
        try(Connection conn = get_connection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, username);
                ps.setString(2, password);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return rs.getInt("player_id");
                    }
                }
            }
        return -1;
    }

    public static void save_run(int player_id, int clicks) throws SQLException {
        String sql = "INSERT INTO runs (player_id, clicks) VALUES (?, ?)";
        try(Connection conn = get_connection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, player_id);
                ps.setInt(2, clicks);
                ps.executeUpdate();
            }
    }

    public static List<Object[]> fetch_top_ten() throws SQLException {
        String sql = """
            SELECT p.username, r.clicks, r.played_at
            FROM runs r
            JOIN players p ON r.player_id = p.player_id
            ORDER BY r.clicks DESC
            LIMIT 10
        """;
        List<Object[]> rows = new ArrayList<>();
        try(Connection conn = get_connection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
                int rank = 1;
                while(rs.next()){
                    Timestamp ts = rs.getTimestamp("played_at");
                    rows.add(new Object[]{
                        rank++,
                        rs.getString("username"),
                        rs.getInt("clicks"),
                        ts != null ? ts.toString() : "-"
                    });
                }
            }
        return rows;
    }
}
