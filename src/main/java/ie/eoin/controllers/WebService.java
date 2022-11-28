package ie.eoin.controllers;

import ie.eoin.controllers.dto.DepartmentDTOMapper;
import ie.eoin.entities.Office;
import ie.eoin.controllers.dto.DepartmentDTO;
import ie.eoin.controllers.dto.officeDTO;
import ie.eoin.controllers.dto.officeDTOMapper;
import ie.eoin.controllers.records.MoveOffice;
import ie.eoin.controllers.records.NewDepartment;
import ie.eoin.controllers.records.NewOffice;
import ie.eoin.dao.departmentRepo;
import ie.eoin.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class WebService {

    @Autowired
    ie.eoin.dao.officeRepo officeRepo;

    @Autowired
    departmentRepo departmentRepo;

    @Autowired
    officeDTOMapper officeDTOMapper;

    @Autowired
    DepartmentDTOMapper departmentDTOMapper;

    @GetMapping("/offices")
    public CollectionModel<officeDTO> getAll(){
        return officeDTOMapper.toCollectionModel(officeRepo.findAll());
    };

    @GetMapping("/departments")
    public CollectionModel<DepartmentDTO> getDepartments(){
        return
                departmentDTOMapper.toCollectionModel(departmentRepo.findAll());
    }

    @GetMapping("/department/{departmentId}/offices")
    public CollectionModel<officeDTO> getAllOfficesInDepartment(@PathVariable("departmentId") int id){
        if(departmentRepo.findById(id).isPresent()){
            return officeDTOMapper.toCollectionModel(officeRepo.findOfficesByDepartmentId(id));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Department with that ID does not exist.");
    }

    @GetMapping("/offices/{id}")
    public officeDTO getOfficeById(@PathVariable("id") int id){
        return officeDTOMapper.toModel(officeRepo.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with ID " + id+ " was not found.")
        ));
    }

    @GetMapping("departments/{id}")
    public DepartmentDTO getDepartmentById(@PathVariable("id") int id){
        return departmentDTOMapper.toModel(departmentRepo.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Department with ID"+id+" was not found.")
        ));
    }

    @GetMapping("/offices/empty")
    public CollectionModel<officeDTO> findAllEmptyOffices(){
        return officeDTOMapper.toCollectionModel(officeRepo.findEmptyOffices());
    }

    @GetMapping("/offices/space")
    public CollectionModel<officeDTO> findAllOfficesWithSpace(){
        return officeDTOMapper.toCollectionModel(officeRepo.findOfficesWithSpace());
    }

    @DeleteMapping("departments/{id}")
    public void deleteDepartmentById(@PathVariable("id") int id){
            departmentRepo.deleteById(id);
    }

    @DeleteMapping("offices/{id}")
    public void deleteOfficeById(@PathVariable("id") int id){
            officeRepo.deleteById(id);
    }

    @PatchMapping("offices/{id}/move")
    public officeDTO moveOfficeToDepartment(@PathVariable("id") int id,@RequestBody @Valid MoveOffice payload,BindingResult bindingResult){
       if(bindingResult.hasErrors()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"JSON MALFORMED");
       }
       if(officeRepo.findById(id).isPresent() & departmentRepo.findById(payload.newDepartmentId()).isPresent()){
           officeRepo.changeDepartment(payload.newDepartmentId(),id);
           return officeDTOMapper.toModel(officeRepo.findById(id).get());
       }
       else{
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Department or Office with that ID does not exist.");
       }

    }


    @PostMapping({"/offices","/offices/"})
    @ResponseStatus(HttpStatus.CREATED)
    public officeDTO addOffice(@RequestBody @Valid NewOffice office, BindingResult bindingResult){

            if(bindingResult.hasErrors()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Bad JSON provided");
            }
            Optional<Department> departmentOptional = departmentRepo.findById(office.departmentId());
            if(departmentOptional.isPresent()){
                Office newOffice = new Office(office.maxOccupancy(), office.currentOccupancy(),departmentOptional.get());
                return officeDTOMapper.toModel(officeRepo.save(newOffice));
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Department with ID " + office.departmentId() + " not found.");
            }
    }

    @PostMapping({"/departments/","/departments"})
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO createDepartment(@RequestBody @Valid NewDepartment payload, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Bad JSON provided");
        }
            Department department = new Department(payload.departmentName(), payload.email());
            department.setOffice(new ArrayList<>());
            return departmentDTOMapper.toModel(departmentRepo.save(department));
    }

    @PatchMapping("offices/{id}/occupancy/{newSpace}")
    @ResponseStatus(HttpStatus.CREATED)
    public officeDTO updateOfficeSpace(@PathVariable("id") int id,@PathVariable("newSpace") int space){
        if(officeRepo.findById(id).isPresent()){
            if(space <= officeRepo.findById(id).get().getMaxOccupancy()){
                officeRepo.changeOccupancy(space,id);
                return officeDTOMapper.toModel(officeRepo.findById(id).get());
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Space is greater than max occupancy");
            }

        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Office with ID " + id + " was not found.");
        }

    }

}
