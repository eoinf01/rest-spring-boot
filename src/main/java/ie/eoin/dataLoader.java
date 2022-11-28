package ie.eoin;

import ie.eoin.dao.departmentRepo;
import ie.eoin.dao.officeRepo;
import ie.eoin.entities.Department;
import ie.eoin.entities.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class dataLoader implements CommandLineRunner {

    @Autowired
    officeRepo officeRepo;

    @Autowired
    departmentRepo departmentRepo;

    @Autowired
    ie.eoin.services.departmentService departmentService;

    @Override
    public void run(String... args) throws Exception {
        Department cs = departmentRepo.save(new Department("Computer Science","fehilyeoin@gmail.com"));
        departmentRepo.save(new Department("Nutrition","nutrition@gmail.com"));
        departmentRepo.save(new Department("Art","art@gmail.com"));
        departmentRepo.findAll().forEach(System.out::println);
        System.out.println(departmentRepo.findByDepartmentName("BIS"));
        System.out.println(departmentRepo.findById(1));
        departmentService.saveDepartment(new Department("Computer Science","fehilyeoin@gmail.com")).ifPresentOrElse(System.out::println,()->System.out.println("Department already exists"));
        Office newOffice = new Office(2000,1999,cs);
        officeRepo.save(newOffice);
        officeRepo.save(new Office(2000,0,cs));
        officeRepo.findAll().forEach(System.out::println);
//        System.out.println(officeRepo.findById(1));
//        System.out.println(officeRepo.findAllByDepartment_DepartmentId(1));
        System.out.println(officeRepo.findEmptyOffices());
        System.out.println(officeRepo.findOfficesWithSpace());
        officeRepo.changeOccupancy(1212,1);
        System.out.println(officeRepo.findById(1));
        System.out.println(officeRepo.findOfficesWithSpace());
//        officeRepo.changeDepartment(bis,1);
        System.out.println(officeRepo.findById(1));

        officeRepo.delete(newOffice);

    }
}
