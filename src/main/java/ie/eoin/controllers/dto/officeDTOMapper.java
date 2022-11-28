package ie.eoin.controllers.dto;


import ie.eoin.controllers.WebService;
import ie.eoin.entities.Office;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class officeDTOMapper extends RepresentationModelAssemblerSupport<Office,officeDTO> {
    public officeDTOMapper() {
        super(WebService.class, officeDTO.class);
    }

    @Override
    public officeDTO toModel(Office entity) {
        officeDTO officeDTO = new officeDTO(entity.getOfficeNumber(),entity.getMaxOccupancy(),entity.getCurrentOccupancy(),entity.getDepartment().getDepartmentName());
        officeDTO.add(linkTo(methodOn(WebService.class).getOfficeById(entity.getOfficeNumber())).withSelfRel());
        officeDTO.add(linkTo(methodOn(WebService.class).getDepartmentById(entity.getDepartment().getDepartmentId())).withRel("department"));
        return officeDTO;
    }

    @Override
    public CollectionModel<officeDTO> toCollectionModel(Iterable<? extends Office> entities){
        return super.toCollectionModel(entities).add(linkTo(methodOn(WebService.class).getAll()).withSelfRel()).add(linkTo(methodOn(WebService.class).findAllEmptyOffices()).withRel("empty")).add(linkTo(methodOn(WebService.class).findAllOfficesWithSpace()).withRel("spaces"));
    }


}
