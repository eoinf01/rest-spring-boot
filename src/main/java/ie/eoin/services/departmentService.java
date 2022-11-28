package ie.eoin.services;

import ie.eoin.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class departmentService {

    @Autowired
    ie.eoin.dao.departmentRepo departmentRepo;

    public Optional<Department> checkExists(String name){
        return Optional.of(departmentRepo.findByDepartmentName(name));
    }

    public Optional<Department> saveDepartment(Department newDepartment){
        if(checkExists(newDepartment.getDepartmentName()).isEmpty()){
            return Optional.of(departmentRepo.save(newDepartment));

        }
        return Optional.empty();
    }
}
