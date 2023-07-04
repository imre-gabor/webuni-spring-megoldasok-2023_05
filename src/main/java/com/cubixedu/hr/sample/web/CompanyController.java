package com.cubixedu.hr.sample.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cubixedu.hr.sample.dto.CompanyDto;
import com.cubixedu.hr.sample.dto.EmployeeDto;
import com.cubixedu.hr.sample.mapper.CompanyMapper;
import com.cubixedu.hr.sample.mapper.EmployeeMapper;
import com.cubixedu.hr.sample.model.AverageSalaryByPosition;
import com.cubixedu.hr.sample.model.Company;
import com.cubixedu.hr.sample.repository.CompanyRepository;
import com.cubixedu.hr.sample.service.CompanyService;
import com.cubixedu.hr.sample.service.SalaryService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	SalaryService salaryService;
	

	// 1. megoldás
	@GetMapping
	public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full) {
		List<Company> companies = companyService.findAll();
		return mapCompanies(companies, full);
	}

	// 2. megoldás
//	@GetMapping(params="full=true")
//	public List<CompanyDto> findAll() {
//		return new ArrayList<>(companies.values());
//	}
//	
//	@GetMapping
//	@JsonView(Views.BaseData.class)
//	public List<CompanyDto> findAllNoEmployees() {
//		return new ArrayList<>(companies.values());
//	}

//	private CompanyDto createCompanyWithoutEmployees(CompanyDto company) {
//		return new CompanyDto(company.getId(), company.getRegistrationNumber(), company.getName(), company.getAddress(), null);
//	}

	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
		Optional<Company> optionalCompany = companyService.findById(id);
		if(optionalCompany.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		Company company = optionalCompany.get();
		return full.orElse(false) 
				? companyMapper.companyToDto(company)
				: companyMapper.companySummaryToDto(company);
	}

	@PostMapping
	public CompanyDto create(@RequestBody CompanyDto companyDto) {
		return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}

	@PutMapping("/{id}")
	public CompanyDto update(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		companyDto.setId(id);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
		if (updatedCompany == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return companyMapper.companyToDto(updatedCompany);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		companyService.delete(id);
	}

	@PostMapping("/{id}/employees")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		Company company = companyService.addEmployee(id, companyMapper.dtoToEmployee(employeeDto));
		return companyMapper.companyToDto(company);
	}

//	private CompanyDto getCompanyOrThrow(long id) {
//		return null;
//		CompanyDto company = companies.get(id);
//		if (company == null) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
//		return company;
//	}

	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployeeFromCompany(@PathVariable long id, @PathVariable long employeeId) {
		Company company = companyService.deleteEmployee(id, employeeId);
		return companyMapper.companyToDto(company);
	}

	@PutMapping("/{id}/employees")
	public CompanyDto replaceAllEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> newEmployees) {
		Company company = companyService.replaceEmployees(id, employeeMapper.dtosToEmployees(newEmployees));
		return companyMapper.companyToDto(company);
	}

	@GetMapping(params = "aboveSalary")
	public List<CompanyDto> getCompaniesAboveSalary(@RequestParam int aboveSalary,
			@RequestParam Optional<Boolean> full) {
		List<Company> filteredCompanies = companyRepository.findByEmployeeWithSalaryHigherThan(aboveSalary);
		return mapCompanies(filteredCompanies, full);
	}

	@GetMapping(params = "aboveEmployeeCount")
	public List<CompanyDto> getCompaniesAboveEmployeeCount(@RequestParam int aboveEmployeeCount,
			@RequestParam Optional<Boolean> full) {
		List<Company> filteredCompanies = companyRepository.findByEmployeeCountHigherThan(aboveEmployeeCount);
		return mapCompanies(filteredCompanies, full);
	}

	@GetMapping("/{id}/salaryStats")
	public List<AverageSalaryByPosition> getSalaryStatsById(@PathVariable long id) {
		return companyRepository.findAverageSalariesByPosition(id);
	}
	

	@PutMapping("/{id}/raiseMinSalary/{position}/{minSalary}")
	public void updateMinSalary(@PathVariable long id, @PathVariable String position, @PathVariable int minSalary) {
		salaryService.raiseMinSalary(id, position, minSalary);
	}
	
	private List<CompanyDto> mapCompanies(List<Company> companies, Optional<Boolean> full) {
		return full.orElse(false) 
				? companyMapper.companiesToDtos(companies)
				: companyMapper.companySummariesToDtos(companies);
	}

}
