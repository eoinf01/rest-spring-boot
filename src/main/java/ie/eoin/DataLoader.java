package ie.eoin;

import ie.eoin.dao.DepartmentRepo;
import ie.eoin.dao.OfficeRepo;
import ie.eoin.dao.UserRepo;
import ie.eoin.entities.Department;
import ie.eoin.entities.Office;
import ie.eoin.entities.PersistentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    OfficeRepo officeRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Department cs = departmentRepo.save(new Department("Computer Science","fehilyeoin@gmail.com"));
        departmentRepo.save(new Department("Nutrition","nutrition@gmail.com"));
        departmentRepo.save(new Department("Art","art@gmail.com"));
        Office newOffice = new Office(2000,1999,cs);
        officeRepo.save(newOffice);
        officeRepo.save(new Office(2000,0,cs));
        officeRepo.changeOccupancy(1212,1);

        PersistentUser persistentUser1 = new PersistentUser("hos@gmail.com",passwordEncoder.encode("secret"),"HOS",false,false);
        userRepo.save(persistentUser1);

        PersistentUser persistentUser2 = new PersistentUser("hod@gmail.com",passwordEncoder.encode("secret"),"HOD",false,false);
        userRepo.save(persistentUser2);


    }
}
