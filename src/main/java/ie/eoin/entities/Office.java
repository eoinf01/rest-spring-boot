package ie.eoin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int officeNumber;

    public Office(int maxOccupancy, int currentOccupancy, Department department) {
        this.maxOccupancy = maxOccupancy;
        this.currentOccupancy = currentOccupancy;
        this.department = department;
    }

    @Column(unique = false,nullable = false)
    private int maxOccupancy;
    @Column(nullable = false,unique = false)
    private int currentOccupancy;

    public Office(int maxOccupancy, int currentOccupancy) {
        this.maxOccupancy = maxOccupancy;
        this.currentOccupancy = currentOccupancy;
    }

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Department department;

}
