package com.cubixedu.hr.sample.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.cubixedu.hr.sample.dto.CompanyDto;
import com.cubixedu.hr.sample.dto.EmployeeDto;
import com.cubixedu.hr.sample.model.Company;
import com.cubixedu.hr.sample.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
	
	CompanyDto companyToDto(Company company);
	
	@Mapping(target = "employees", ignore = true)
	@Named("summary")
	CompanyDto companySummaryToDto(Company company);
	
	List<CompanyDto> companiesToDtos(List<Company> company);
	
	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companySummariesToDtos(List<Company> company);
	
	Company dtoToCompany(CompanyDto companyDto);
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@Mapping(source = "employeeId", target = "id")
	@Mapping(source = "jobTitle", target = "title")
	@Mapping(source = "dateOfStartWork", target = "entryDate")
	EmployeeDto employeeToDto(Employee employee);

	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);

}
