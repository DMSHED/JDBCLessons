package Jdbc.starter.dao;

import Jdbc.starter.dto.EmployeeFilter;
import Jdbc.starter.entity.Company;
import Jdbc.starter.entity.EmployeeEntity;
import Jdbc.starter.exception.DaoException;
import Jdbc.starter.util.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeDao implements Dao<Integer, EmployeeEntity>{
    private static final EmployeeDao INSTANCE = new EmployeeDao();
    private static final String DELETE_SQL = """
            DELETE FROM company_storage.employee
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO company_storage.employee (first_name, last_name, company_id, salary) 
            VALUES (?, ?, ?, ?) 
            """;

    private static final String UPDATE_SQL = """
            UPDATE company_storage.employee
            SET first_name = ?,
                last_name = ?,
                company_id = ?,
                salary = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT 
                e.id, 
                e.first_name, 
                e.last_name, 
                e.company_id, 
                e.salary,
                c.name,
                c.date,
                c.image
            FROM company_storage.employee e
            JOIN company_storage.company c 
                ON c.id = e.company_id
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE e.id = ?
            """;

    private static final CompanyDao companyDao = CompanyDao.getINSTANCE();
    private EmployeeDao() {}

    public static EmployeeDao getInstance() {
        return INSTANCE;
    }

    public List<EmployeeEntity> findAll(EmployeeFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.company_id() != null) {
            whereSql.add("company_id = ?");
            parameters.add(filter.company_id());
        }
        if (filter.last_name() != null) {
            whereSql.add("last_name LIKE ?");
            parameters.add(filter.last_name());
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        String where = whereSql.stream().collect(Collectors
                .joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));

        var sql = FIND_ALL_SQL + where;

        try (Connection connection = ConnectionPool.get();
        var statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(statement);

            ResultSet resultSet = statement.executeQuery();
            List<EmployeeEntity> employees = new ArrayList<>();

            while (resultSet.next()) {
                employees.add(buildEmployeeEntity(resultSet));
            }

            return employees;

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public List<EmployeeEntity> findAll() {
        try (var connection = ConnectionPool.get();
        var statement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = statement.executeQuery();
            List<EmployeeEntity> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(buildEmployeeEntity(resultSet));
            }

            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<EmployeeEntity> findById(Integer id) {
        try (var connection = ConnectionPool.get();
        var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            EmployeeEntity employeeEntity = null;

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employeeEntity = buildEmployeeEntity(resultSet);
                return Optional.of(employeeEntity);
            } else {
                return Optional.ofNullable(employeeEntity);
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static EmployeeEntity buildEmployeeEntity(ResultSet resultSet) throws SQLException {
        var company = new Company(
                resultSet.getInt("company_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getBytes("image")

        );
        return new EmployeeEntity(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                companyDao.findById(resultSet.getInt("company_id"),
                        resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getInt("salary")
        );
    }

    @Override
    public void update(EmployeeEntity employeeEntity) {
        try (Connection connection = ConnectionPool.get();
        var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, employeeEntity.getFirst_name());
            statement.setString(2, employeeEntity.getLast_name());
            statement.setInt(3, employeeEntity.getCompany_id().id());
            statement.setInt(4, employeeEntity.getSalary());
            statement.setInt(5, employeeEntity.getId());

            statement.executeUpdate();


        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection connection = ConnectionPool.get();
        var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public EmployeeEntity save(EmployeeEntity employeeEntity) {
        try (Connection connection = ConnectionPool.get();
        var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employeeEntity.getFirst_name());
            statement.setString(2, employeeEntity.getLast_name());
            statement.setInt(3, employeeEntity.getCompany_id().id());
            statement.setInt(4, employeeEntity.getSalary());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                employeeEntity.setId(generatedKeys.getInt("id"));
            }

            return employeeEntity;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }



}
