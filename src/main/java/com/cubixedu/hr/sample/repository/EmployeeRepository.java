package com.cubixedu.hr.sample.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cubixedu.hr.sample.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findBySalaryGreaterThan(int minSalary);

	List<Employee> findByJobTitle(String jobTitle);

	List<Employee> findByNameStartingWithIgnoreCase(String namePrefix);
	
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);
	
}
