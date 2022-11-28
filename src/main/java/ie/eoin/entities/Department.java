package ie.eoin.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;
    @Column(unique = true,nullable = false)
    private String departmentName;
    @Column(unique = true,nullable = false)
    private String email;

    public Department(String departmentName, String email) {
        this.departmentName = departmentName;
        this.email = email;
    }

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    @JsonIgnore
    private List<Office> office;

}
