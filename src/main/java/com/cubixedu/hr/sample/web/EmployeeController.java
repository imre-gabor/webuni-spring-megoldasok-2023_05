package com.cubixedu.hr.sample.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.cubixedu.hr.sample.dto.EmployeeDto;
import com.cubixedu.hr.sample.mapper.EmployeeMapper;
import com.cubixedu.hr.sample.model.Employee;
import com.cubixedu.hr.sample.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	EmployeeMapper employeeMapper;

	// 1. megoldás
	@GetMapping
	public List<EmployeeDto> findEmployees(@RequestParam Optional<Integer> minSalary) {
		List<Employee> employees = null;
		if (minSalary.isPresent()) {
			// employees = employeeRepository.findBySalaryGreaterThan(minSalary);
		} else {
			employees = employeeService.findAll();
		}
		
		return employeeMapper.employeesToDtos(employees);
	}

	// 2. megoldás
//	@GetMapping
//	public List<EmployeeDto> findAll(){
//		return new ArrayList<>(employees.values());
//	}
//	
//	@GetMapping(params = "minSalary")
//	public List<EmployeeDto> findByMinSalary(@RequestParam int minSalary){
//		return new ArrayList<>(employees.values().stream().filter(e -> e.getSalary() > minSalary).toList());
//	}

	
	 
	 
	@GetMapping("/{id}")
	public EmployeeDto findById(@PathVariable long id) {
		Employee employee = findByIdOrThrow(id);
		return employeeMapper.employeeToDto(employee);
	}

	private Employee findByIdOrThrow(long id) {
		return employeeService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public EmployeeDto create(@RequestBody EmployeeDto employeeDto) {
		return employeeMapper.employeeToDto(employeeService.save(employeeMapper.dtoToEmployee(employeeDto)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> update(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		employeeDto.setId(id);
		Employee updatedEmployee = employeeService.update(employeeMapper.dtoToEmployee(employeeDto));
		if (updatedEmployee == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(employeeMapper.employeeToDto(updatedEmployee));
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		employeeService.delete(id);
	}

	@PostMapping("/payRaise")
	public int getPayRaisePercent(@RequestBody Employee employee) {
		return employeeService.getPayRaisePercent(employee);
	}

}
