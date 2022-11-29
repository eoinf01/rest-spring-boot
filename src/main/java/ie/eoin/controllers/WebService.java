package ie.eoin.controllers;

import ie.eoin.controllers.dto.DepartmentDTOMapper;
import ie.eoin.dao.OfficeRepo;
import ie.eoin.entities.Office;
import ie.eoin.controllers.dto.DepartmentDTO;
import ie.eoin.controllers.dto.OfficeDTO;
import ie.eoin.controllers.dto.OfficeDTOMapper;
import ie.eoin.controllers.records.MoveOffice;
import ie.eoin.controllers.records.NewDepartment;
import ie.eoin.controllers.records.NewOffice;
import ie.eoin.dao.DepartmentRepo;
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
    OfficeRepo officeRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    OfficeDTOMapper officeDTOMapper;

    @Autowired
    DepartmentDTOMapper departmentDTOMapper;


    /*
    Get all offices
    Sample url: http://localhost:8080/offices
     */
    @GetMapping("/offices")
    public CollectionModel<OfficeDTO> getAll(){
        return officeDTOMapper.toCollectionModel(officeRepo.findAll());
    }


    /*
    Get all departments
    Sample url: http://localhost:8080/departments
     */
    @GetMapping("/departments")
    public CollectionModel<DepartmentDTO> getDepartments(){
        return
                departmentDTOMapper.toCollectionModel(departmentRepo.findAll());
    }

    /*
    Get all offices in department by ID
    Sample url: http://localhost:8080/department/2/offices
     */
    @GetMapping("/department/{departmentId}/offices")
    public CollectionModel<OfficeDTO> getAllOfficesInDepartment(@PathVariable("departmentId") int id){
        if(departmentRepo.findById(id).isPresent()){
            return officeDTOMapper.toCollectionModel(officeRepo.findAllByDepartment_DepartmentId(id));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Department with that ID does not exist.");
    }

    /*
    Get office by ID
    Sample url: http://localhost:8080/offices/2
     */
    @GetMapping("/offices/{id}")
    public OfficeDTO getOfficeById(@PathVariable("id") int id){
        return officeDTOMapper.toModel(officeRepo.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with ID " + id + " was not found.")
        ));
    }


    /*
    Get department by ID
    Sample url: http://localhost:8080/departments/1
     */
    @GetMapping("departments/{id}")
    public DepartmentDTO getDepartmentById(@PathVariable("id") int id){
        return departmentDTOMapper.toModel(departmentRepo.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Department with ID"+id+" was not found.")
        ));
    }

    /*
    Get offices that are empty
    Sample url: http://localhost:8080/offices/empty
     */
    @GetMapping("/offices/empty")
    public CollectionModel<OfficeDTO> findAllEmptyOffices(){
        return officeDTOMapper.toCollectionModel(officeRepo.findEmptyOffices());
    }


    /*
    Get offices with space
    Sample url: http://localhost:8080/offices/space
     */
    @GetMapping("/offices/space")
    public CollectionModel<OfficeDTO> findAllOfficesWithSpace(){
        return officeDTOMapper.toCollectionModel(officeRepo.findOfficesWithSpace());
    }



    /*
    Create new office
    URL: http://localhost:8080/offices
    {
        "maxOccupancy":3000,
        "currentOccupancy":2000,
        "departmentId": 1
    }
     */
    @PostMapping({"/offices","/offices/"})
    @ResponseStatus(HttpStatus.CREATED)
    public OfficeDTO addOffice(@RequestBody @Valid NewOffice office, BindingResult bindingResult){

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


    /*
    Create new department
    URL: http://localhost:8080/departments/
    {
        "departmentName": "Eoin's Test Department",
        "email": "newDepartment@gmail.com"
    }
     */
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


    /*
    Update current occupancy of office
    Sample URL: http://localhost:8080/offices/2/occupancy/1232
     */
    @PatchMapping("offices/{id}/occupancy/{newSpace}")
    @ResponseStatus(HttpStatus.CREATED)
    public OfficeDTO updateOfficeSpace(@PathVariable("id") int id, @PathVariable("newSpace") int space){
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

    /*
    Move office to new department
    Sample URL: http://localhost:8080/offices/2/move
    {
        "newDepartmentId": "2"
    }
     */
    @PatchMapping("offices/{id}/move")
    public OfficeDTO moveOfficeToDepartment(@PathVariable("id") int id, @RequestBody @Valid MoveOffice payload, BindingResult bindingResult){
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

    /*
    Deletes office by id
    Sample url: http://localhost:8080/offices/2
     */
    @DeleteMapping("offices/{id}")
    public void deleteOfficeById(@PathVariable("id") int id){
        officeRepo.deleteById(id);
    }


}
