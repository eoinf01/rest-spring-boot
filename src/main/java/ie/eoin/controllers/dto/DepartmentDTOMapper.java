package ie.eoin.controllers.dto;

import ie.eoin.controllers.WebService;
import ie.eoin.entities.Department;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DepartmentDTOMapper extends RepresentationModelAssemblerSupport<Department,DepartmentDTO> {
    public DepartmentDTOMapper() {
        super(WebService.class, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO toModel(Department entity) {
        DepartmentDTO departmentDTO = new DepartmentDTO(entity.getDepartmentId(),entity.getDepartmentName(),entity.getEmail());
        departmentDTO.add(linkTo(methodOn(WebService.class).getDepartmentById(entity.getDepartmentId())).withSelfRel());
        departmentDTO.add(linkTo(methodOn(WebService.class).getAllOfficesInDepartment(entity.getDepartmentId())).withRel("offices"));
        return departmentDTO;
    }

    @Override
    public CollectionModel<DepartmentDTO> toCollectionModel(Iterable<? extends Department> entities){
        return super.toCollectionModel(entities).add(linkTo(methodOn(WebService.class).getDepartments()).withSelfRel());
    }
}
