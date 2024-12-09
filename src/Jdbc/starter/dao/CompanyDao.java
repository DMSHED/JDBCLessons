package Jdbc.starter.dao;

import Jdbc.starter.entity.Company;
import Jdbc.starter.exception.DaoException;
import Jdbc.starter.util.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CompanyDao implements Dao<Integer, Company>{
    private static final CompanyDao INSTANCE= new CompanyDao();

    //language=PostgreSQL
    private static final String FIND_BY_ID_SQL = """
            select id, name, date, image
            from company_storage.company
            where id = ?
            """;
    private CompanyDao(){}

    public static CompanyDao getINSTANCE() {
        return INSTANCE;
    }


    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public void update(Company employeeEntity) {

    }

    @Override
    public Company save(Company employeeEntity) {
        return null;
    }

    @Override
    public Optional<Company> findById(Integer id) {
        try (var connection = ConnectionPool.get()){
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    };


    public Optional<Company> findById(Integer id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            Company companyEntity = null;

            if (resultSet.next()) {
                companyEntity = buildCompanyEntity(resultSet);
            }
            return Optional.ofNullable(companyEntity);


        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Company buildCompanyEntity(ResultSet resultSet) throws SQLException {
        return new Company(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getBytes("image")
        );
    }

    @Override
    public List<Company> findAll() {
        return null;
    }
}
