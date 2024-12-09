package Jdbc.starter.entity;

public class EmployeeEntity {
    private Integer id;
    private String first_name;
    private String last_name;
    private Company company;

    public EmployeeEntity() {}

    public EmployeeEntity(Integer id, String first_name, String last_name, Company company, Integer salary) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company = company;
        this.salary = salary;
    }


    @Override
    public String toString() {
        return "EmployeeEntity{" +
               "id=" + id +
               ", first_name='" + first_name + '\'' +
               ", last_name='" + last_name + '\'' +
               ", company=" + company +
               ", salary=" + salary +
               '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCompany_id(Company company) {
        this.company = company;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Company getCompany_id() {
        return company;
    }

    public Integer getSalary() {
        return salary;
    }

    private Integer salary;
}
