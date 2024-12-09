package Jdbc.starter;

import Jdbc.starter.dao.EmployeeDao;
import Jdbc.starter.dto.EmployeeFilter;
import Jdbc.starter.entity.Company;
import Jdbc.starter.entity.EmployeeEntity;

import java.util.List;

public class DaoRunner {
    public static void main(String[] args) {
//        saveTest();
//        deleteTest();
//        updateSalary(10, 1000);
//        testFilter();

        EmployeeDao employeeDao = EmployeeDao.getInstance();

        EmployeeFilter employeeFilter = new EmployeeFilter(3, null, null, 1);

        List<EmployeeEntity> employeeEntities = EmployeeDao.getInstance().findAll(employeeFilter);
        System.out.println();

    }

    private static void testFilter() {
        EmployeeDao employeeDao = EmployeeDao.getInstance();

        EmployeeFilter employeeFilter = new EmployeeFilter(3, null, null, 1);

        List<EmployeeEntity> employeeEntities = EmployeeDao.getInstance().findAll(employeeFilter);
        System.out.println();
    }

    private static void updateSalary(Integer id, Integer newSalary) {
        EmployeeDao employeeDao = EmployeeDao.getInstance();

        var employeeEntity = employeeDao.findById(id);
        employeeEntity.ifPresent(employee -> {
            employee.setSalary(newSalary);
            employeeDao.update(employee);
        });
    }

    private static void deleteTest() {
        EmployeeDao employeeDao = EmployeeDao.getInstance();
        var deleteResult = employeeDao.delete(9);
        System.out.println(deleteResult);
    }

    private static void saveTest(String first_name, String last_name, Company company, Integer salary) {
        EmployeeDao employeeDao = EmployeeDao.getInstance();
        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setFirst_name(first_name);
        employeeEntity.setLast_name(last_name);
        employeeEntity.setCompany_id(company);
        employeeEntity.setSalary(salary);

        EmployeeEntity savedEmployee = employeeDao.save(employeeEntity);
        System.out.println(savedEmployee);
    }
}
