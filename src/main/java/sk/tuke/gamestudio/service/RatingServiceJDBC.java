//package sk.tuke.gamestudio.service;
//
//import sk.tuke.gamestudio.entity.Rating;
//
//import java.sql.*;
//import java.util.Date;
//
//public class RatingServiceJDBC implements RatingService {
//    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
//    public static final String USER = "postgres";
//    public static final String PASSWORD = "postgres";
//    public static final String SELECT_AVERAGE = "SELECT AVG(rating) FROM rating WHERE game = ?";
//    public static final String SELECT = "SELECT rating FROM rating WHERE game = ? AND player = ?";
//    public static final String DELETE = "DELETE FROM rating";
//    public static final String INSERT = "INSERT INTO rating (player, game, rating, ratedOn) VALUES (?, ?, ?, ?)";
//    public static final String UPDATE = "UPDATE rating SET rating = ? WHERE game = ? AND player = ?";
//
//    @Override
//    public void setRating(Rating rating) {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.prepareStatement(INSERT)
//        ) {
//            statement.setString(1, rating.getPlayer());
//            statement.setString(2, rating.getGame());
//            statement.setInt(3, rating.getRating());
//            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RatingException("Problem setting rating", e);
//        }
//    }
//
//    @Override
//    public int getAverageRating(String game) {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.prepareStatement(SELECT_AVERAGE)
//        ) {
//            statement.setString(1, game);
//            try (var rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt(1);
//                } else {
//                    return 0;
//                }
//            }
//        } catch (SQLException e) {
//            throw new RatingException("Problem getting average rating", e);
//        }
//    }
//
//    @Override
//    public int getRating(String game, String player) {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.prepareStatement(SELECT)
//        ) {
//            statement.setString(1, game);
//            statement.setString(2, player);
//            try (var rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt(1);
//                } else {
//                    return 0;
//                }
//            }
//        } catch (SQLException e) {
//            throw new RatingException("Problem getting rating", e);
//        }
//    }
//
//    @Override
//    public void reset() {
//        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             var statement = connection.createStatement();
//        ) {
//            statement.executeUpdate(DELETE);
//        } catch (SQLException e) {
//            throw new RatingException("Problem resetting ratings", e);
//        }
//    }
//}
