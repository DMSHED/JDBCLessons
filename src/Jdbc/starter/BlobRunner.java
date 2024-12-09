package Jdbc.starter;

import Jdbc.starter.util.ConnectionManager;
import Jdbc.starter.util.ConnectionPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;

public class BlobRunner {

    public static void main(String[] args) {
        //blob - bytea   байты
        //clob - TEXT    символы
        try {
            getImage();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getImage() throws SQLException, IOException {
        var sql = """
                select image from company_storage.company
                where id = ?
                """;

        try (var connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1,1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                byte[] image = resultSet.getBytes("image");
                Files.write(Path.of("resources", "G2.jpg"), image, StandardOpenOption.CREATE);
            }
        }


    }

    private static void saveImage() throws IOException, SQLException {
        var sql = """
                update company_storage.company
                set image = ?
                where id = ?
                """;
        int companyId = 1;
        byte[] image = Files.readAllBytes(Path.of("resources", "google.jpg"));
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBytes(1, image);
            preparedStatement.setInt(2, companyId);
            preparedStatement.executeUpdate();

        }

    }

//    private static void saveImage() {
//        var sql = """
//                update company_storage.company
//                set image = ?
//                where id = 1;
//                """;
//
//        try (var connection = ConnectionManager.open();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            connection.setAutoCommit(false);
//            Blob blob = connection.createBlob();
//
//            blob.setBytes(1, Files.readAllBytes(Path.of("resources", "google.jpg")));
//
//            preparedStatement.setBlob(1, blob);
//            preparedStatement.executeUpdate();
//            connection.commit();
//        } catch (SQLException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
