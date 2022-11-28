package ie.eoin.dao;

import ie.eoin.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface officeRepo extends JpaRepository<Office,Integer> {

    List<Office> findAllByDepartment_DepartmentId(int id);


    @Query(value = "select * from Office o where o.current_occupancy = 0",nativeQuery = true)
    List<Office> findEmptyOffices();

    @Query(value = "select * from Office o where o.current_occupancy < o.max_occupancy",nativeQuery = true)
     List<Office> findOfficesWithSpace();

    @Query(value = "select * from Office o where o.department_department_id = :departmentNumber",nativeQuery = true)
    List<Office> findOfficesByDepartmentId(@Param("departmentNumber") int id);

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.department_department_id = :newDepartment where o.office_number = :officeNumber",nativeQuery = true)
    void changeDepartment(@Param("newDepartment") int newDepartmentId, @Param("officeNumber") int officeNumber);

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.currentOccupancy = :newOccupancy where o.officeNumber = :officeNumber")
    void changeOccupancy(@Param("newOccupancy") int newOccupancy, @Param("officeNumber") int officeNumber);

}
