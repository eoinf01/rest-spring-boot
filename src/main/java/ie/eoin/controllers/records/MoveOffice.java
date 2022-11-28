package ie.eoin.controllers.records;

import com.sun.istack.NotNull;

import javax.validation.constraints.Min;

public record MoveOffice(@Min(1) @NotNull int newDepartmentId) {
}
