package com.cubixedu.hr.sample.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cubixedu.hr.sample.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Page<Employee> findBySalaryGreaterThan(int minSalary, Pageable pageable);

	List<Employee> findByPositionName(String jobTitle);

	List<Employee> findByNameStartingWithIgnoreCase(String namePrefix);
	
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime start, LocalDateTime end);

	
	@Modifying
	@Transactional
	@Query("UPDATE Employee e2 "
			+ "SET e2.salary = :minSalary "
			+ "WHERE e2.id IN "
			+ "(SELECT e.id "
			+ "FROM Employee e "
			+ "WHERE e.salary < :minSalary "
			+ "AND e.company.id = :companyId "
			+ "AND e.position.name = :positionName)")
	void updateSalaries(long companyId, String positionName, int minSalary);
	
}
