package Jdbc.starter;

import Jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        long companyId = 3;

//        var deleteCompanySql = "delete from company_storage.company where id = ? ";
//        var deleteEmployeeSql = "delete from company_storage.employee where company_id = ? ";
        var deleteCompanySql = "delete from company_storage.company where id = " + companyId;
        var deleteEmployeeSql = "delete from company_storage.employee where company_id = " + companyId;


        Connection connection = null;
//        PreparedStatement deleteCompanyStatement = null;
//        PreparedStatement deleteEmployeeStatement = null;
        Statement statement = null;

        try{
            connection = ConnectionManager.open();
            connection.setAutoCommit(false);
//            deleteCompanyStatement = connection.prepareStatement(deleteCompanySql);
//            deleteEmployeeStatement = connection.prepareStatement(deleteEmployeeSql);

            statement = connection.createStatement();
            statement.addBatch(deleteEmployeeSql);
            statement.addBatch(deleteCompanySql);

            var ints = statement.executeBatch();

//            для того, чтобы реализовать транзакцию, нужно отключить
//            авто коммит, который выполняется после каждого execute
//            главное, потом обратно не забыть его переключить в true
//            когда перейдем в пулл connection

//            deleteEmployeeStatement.setLong(1, companyId);
//            deleteCompanyStatement.setLong(1, companyId);

//            var deletedEmployeeResult = deleteEmployeeStatement.executeUpdate();
//            if(true) {
//                throw new RuntimeException("Ooops...");
//            }
//            var deletedCompanyResult = deleteCompanyStatement.executeUpdate();
            connection.commit();

        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
//            если не проверять на null, то можно словить исключение, если он
//             не был открыт и был null
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
         }
    }
}
