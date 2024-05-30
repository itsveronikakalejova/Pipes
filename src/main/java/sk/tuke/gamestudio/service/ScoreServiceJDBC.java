//package sk.tuke.gamestudio.service;
//
//import sk.tuke.gamestudio.entity.Score;
//
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ScoreServiceJDBC implements ScoreService {
//    private static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "postgres";
//
//    public static final String INSERT_COMMAND = "INSERT INTO score (player, game, points, playedOn) VALUES (?, ?, ?, ?)";
//
//    public static final String DELETE_COMMAND = "DELETE FROM score";
//
//    public static final String SELECT_COMMAND = "SELECT player, game, points, playedOn FROM score WHERE game = ? ORDER BY points DESC LIMIT 5";
//
//    @Override
//    public void addScore(Score score) {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            // Check if the player already exists in the database
//            try (var checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM score WHERE player = ?")) {
//                checkStatement.setString(1, score.getPlayer());
//                try (var resultSet = checkStatement.executeQuery()) {
//                    resultSet.next();
//                    int playerCount = resultSet.getInt(1);
//
//                    // If the player exists, update their score
//                    if (playerCount > 0) {
//                        try (var updateStatement = connection.prepareStatement(
//                                "UPDATE score SET points = points + ?, playedOn = ? WHERE player = ?")) {
//                            updateStatement.setInt(1, score.getPoints());
//                            updateStatement.setTimestamp(2, new Timestamp(score.getPlayedOn().getTime()));
//                            updateStatement.setString(3, score.getPlayer());
//                            updateStatement.executeUpdate();
//                        }
//                    } else {
//                        // If the player doesn't exist, insert a new score
//                        try (var insertStatement = connection.prepareStatement(INSERT_COMMAND)) {
//                            insertStatement.setString(1, score.getPlayer());
//                            insertStatement.setString(2, score.getGame());
//                            insertStatement.setInt(3, score.getPoints());
//                            insertStatement.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
//                            insertStatement.executeUpdate();
//                        }
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            throw new GameStudioException(e);
//        }
//    }
//
//
//    @Override
//    public List<Score> getTopScores(String game) {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.prepareStatement(SELECT_COMMAND)) {
//            statement.setString(1, game);
//            try (var rs = statement.executeQuery()) {
//                var scores = new ArrayList<Score>();
//
//                while (rs.next()) {
//                    scores.add(new Score(
//                            rs.getString(1),
//                            rs.getString(2),
//                            rs.getInt(3),
//                            rs.getTimestamp(4)));
//                }
//
//                return scores;
//            }
//        } catch (SQLException e) {
//            throw new GameStudioException(e);
//        }
//    }
//
//    @Override
//    public Score getPlayerScore(String playerName) {
//        Score playerScore = null;
//
//        String SELECT_PLAYER_SCORE_COMMAND = "SELECT player, game, points, playedOn FROM score WHERE player = ?";
//
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.prepareStatement(SELECT_PLAYER_SCORE_COMMAND)) {
//            statement.setString(1, playerName);
//
//            try (var rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    playerScore = new Score(
//                            rs.getString("player"),
//                            rs.getString("game"),
//                            rs.getInt("points"),
//                            rs.getTimestamp("playedOn")
//                    );
//                }
//            }
//        } catch (SQLException e) {
//            throw new GameStudioException(e);
//        }
//
//        return playerScore;
//    }
//
//    @Override
//    public void reset() {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.createStatement()) {
//            statement.executeUpdate(DELETE_COMMAND);
//        } catch (SQLException e) {
//            throw new GameStudioException(e);
//        }
//    }
//}
//
//
