package Jdbc.starter.dto;

/*
record - облегченный класс, для хранения
автоматически генерирует геттеры
для всех полей, которые мы перечислили в скобках,
а также toString, equals и hashcode

 */
public record EmployeeFilter(Integer limit,
                             Integer offset,
                             String last_name,
                             Integer company_id) {

}
