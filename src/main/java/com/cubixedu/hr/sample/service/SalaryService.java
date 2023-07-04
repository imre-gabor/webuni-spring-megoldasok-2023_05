package com.cubixedu.hr.sample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cubixedu.hr.sample.model.Employee;
import com.cubixedu.hr.sample.repository.EmployeeRepository;
import com.cubixedu.hr.sample.repository.PositionDetailsByCompanyRepository;
import com.cubixedu.hr.sample.repository.PositionRepository;

@Service
public class SalaryService {

	private EmployeeService employeeService;
	private PositionRepository positionRepository;
	private PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
	private EmployeeRepository employeeRepository;
	
	public SalaryService(EmployeeService employeeService, PositionRepository positionRepository,
			PositionDetailsByCompanyRepository positionDetailsByCompanyRepository,
			EmployeeRepository employeeRepository) {
		this.employeeService = employeeService;
		this.positionRepository = positionRepository;
		this.positionDetailsByCompanyRepository = positionDetailsByCompanyRepository;
		this.employeeRepository = employeeRepository;
	}
	

	public void setNewSalary(Employee employee) {
		int newSalary = employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) / 100;
		employee.setSalary(newSalary);
	}
	
	//1. megoldás az A opcionális feladatra. Probléma: ahány employee, 
	//annyi UPDATE fut majd le --> nem hatékony
//	@Transactional
//	public void raiseMinSalary(String positionName, int minSalary) {
//		positionRepository.findByName(positionName)
//		.forEach(p -> {
//			p.setMinSalary(minSalary);
//			p.getEmployees().forEach(e -> {
//				if(e.getSalary() < minSalary)
//					e.setSalary(minSalary);
//			});
//		});
//	}
	
	
	@Transactional
	public void raiseMinSalary(long companyId, String positionName, int minSalary) {
		positionDetailsByCompanyRepository.findByPositionNameAndCompanyId(positionName, companyId)
		.forEach(pd -> {
			pd.setMinSalary(minSalary);
			employeeRepository.updateSalaries(companyId, positionName, minSalary);
		});
	}

}
