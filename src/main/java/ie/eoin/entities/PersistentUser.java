package ie.eoin.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class PersistentUser {

    @Id
    private String userEmail;

    @Column
    private String userPassword;

    @Column
    private String userRole;

    @Column
    private boolean disabled, locked;
}
