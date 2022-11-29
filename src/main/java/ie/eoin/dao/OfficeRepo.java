package ie.eoin.dao;

import ie.eoin.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OfficeRepo extends JpaRepository<Office,Integer> {

    List<Office> findAllByDepartment_DepartmentId(int id);


    @Query(value = "select o from Office o where o.currentOccupancy = 0")
    List<Office> findEmptyOffices();

    @Query(value = "select o from Office o where o.currentOccupancy < o.maxOccupancy")
     List<Office> findOfficesWithSpace();

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.department_department_id = :newDepartment where o.office_number = :officeNumber",nativeQuery = true)
    void changeDepartment(@Param("newDepartment") int newDepartmentId, @Param("officeNumber") int officeNumber);

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.currentOccupancy = :newOccupancy where o.officeNumber = :officeNumber")
    void changeOccupancy(@Param("newOccupancy") int newOccupancy, @Param("officeNumber") int officeNumber);

}
