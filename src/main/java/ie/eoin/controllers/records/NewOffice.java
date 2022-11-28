package ie.eoin.controllers.records;

import javax.validation.constraints.Min;

public record NewOffice(@Min(0) int maxOccupancy, @Min(0) int currentOccupancy, int departmentId) {
}
