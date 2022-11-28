package ie.eoin.controllers.records;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public record NewDepartment(@NotEmpty @NotBlank String departmentName,@NotEmpty @NotBlank String email) {
}
