package ie.eoin.dao;

import ie.eoin.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface departmentRepo extends JpaRepository<Department,Integer> {
    Department findByDepartmentName(String name);


}
