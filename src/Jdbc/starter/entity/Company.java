package Jdbc.starter.entity;

import java.time.LocalDate;

public record Company(Integer id,
                      String name,
                      LocalDate date,
                      byte[] image) {
}
