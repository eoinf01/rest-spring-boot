package ie.eoin.controllers.dto;


import ie.eoin.controllers.WebService;
import ie.eoin.entities.Office;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OfficeDTOMapper extends RepresentationModelAssemblerSupport<Office, OfficeDTO> {
    public OfficeDTOMapper() {
        super(WebService.class, OfficeDTO.class);
    }

    @Override
    public OfficeDTO toModel(Office entity) {
        OfficeDTO officeDTO = new OfficeDTO(entity.getOfficeNumber(),entity.getMaxOccupancy(),entity.getCurrentOccupancy(),entity.getDepartment().getDepartmentName());
        officeDTO.add(linkTo(methodOn(WebService.class).getOfficeById(entity.getOfficeNumber())).withSelfRel());
        officeDTO.add(linkTo(methodOn(WebService.class).getDepartmentById(entity.getDepartment().getDepartmentId())).withRel("department"));
        return officeDTO;
    }

    @Override
    public CollectionModel<OfficeDTO> toCollectionModel(Iterable<? extends Office> entities){
        return super.toCollectionModel(entities).add(linkTo(methodOn(WebService.class).getAll()).withSelfRel()).add(linkTo(methodOn(WebService.class).findAllEmptyOffices()).withRel("empty")).add(linkTo(methodOn(WebService.class).findAllOfficesWithSpace()).withRel("spaces"));
    }


}
