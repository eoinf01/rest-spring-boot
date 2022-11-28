package ie.eoin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.eoin.entities.Department;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest("ie.eoin")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class JpaApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	@SneakyThrows
	@Order(1)
	void getOfficesOK(){
		mockMvc.perform(
				MockMvcRequestBuilders.get("/offices/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.offices", Matchers.hasSize(1)));
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles = {"HOD","HOS"})
	@Order(2)
	void deleteOfficeOk(){
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/offices/{id}",2))
				.andExpect(status().isOk()
				);
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles = {"HOD","HOS"})
	@Order(3)
	void deleteOfficeNotExist(){
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/offices/{id}",1))
				.andExpect(status().isNotFound());
	}

	@Test
	@SneakyThrows
	@Order(4)
	void deleteOfficeUnauthorized(){
		mockMvc.perform(MockMvcRequestBuilders.delete("/offices/{id}",2)).andExpect(status().isUnauthorized());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles = "HOS")
	@Order(5)
	void createDepartmentOk(){
		String departmentJson = new ObjectMapper().writeValueAsString(new Department("Eoin's Second Office","secondoffice@gmail.com"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments")
				.content(departmentJson)
						.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isCreated())
				.andExpect(jsonPath("_links.self").exists());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles = "HOD")
	@Order(6)
	void createDepartmentNoAuthUser(){
		String departmentJson = new ObjectMapper().writeValueAsString(new Department("Eoin's Third Office","thirdoffice@gmail.com"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments")
						.content(departmentJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isForbidden());
	}

	@Test
	@SneakyThrows
	@Order(7)
	void createDepartmentNoAuth(){
		String departmentJson = new ObjectMapper().writeValueAsString(new Department("Eoin's Third Office","thirdoffice@gmail.com"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments")
				.content(departmentJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		).andExpect(status().isUnauthorized());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles = "HOS")
	@Order(8)
	void createDepartmentConflict(){
		String departmentJson = new ObjectMapper().writeValueAsString(new Department("Eoin's Second Office","secondoffice@gmail.com"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
						.content(departmentJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
				).andExpect(status().isConflict());
	}

	@Test
	@SneakyThrows
	@WithMockUser( roles = "HOS")
	@Order(9)
	void createDepartmentBadJSON(){
		String departmentJson = new Department("","").toString();
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
				.content(departmentJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		).andExpect(status().isBadRequest());
	}



}
