package hexlet.code.app.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @ToString.Include
    private String firstName;

    @ToString.Include
    private String lastName;

    @Column(unique = true)
    @Email
    private String email;

    private String password;

    private LocalDate createdAt;
    private LocalDate updateAt;


}
