package ie.eoin.controllers.dto;


import ie.eoin.entities.Department;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "offices",itemRelation = "office")
public class officeDTO extends RepresentationModel<officeDTO> {
    private int officeNumber;
    private int maxOccupancy;
    private int currentOccupancy;
    private String departmentName;
}
