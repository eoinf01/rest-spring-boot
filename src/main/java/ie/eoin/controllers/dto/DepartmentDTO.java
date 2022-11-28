package ie.eoin.controllers.dto;


import ie.eoin.entities.Office;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "departments",itemRelation = "department")
public class DepartmentDTO extends RepresentationModel<DepartmentDTO> {
    private int departmentId;
    private String departmentName;
    private String email;
}
