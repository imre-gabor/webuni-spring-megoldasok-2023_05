package com.cubixedu.hr.sample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cubixedu.hr.sample.model.AverageSalaryByPosition;
import com.cubixedu.hr.sample.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	public List<Company> findByEmployeeWithSalaryHigherThan(int minSalary);
	
	public List<Company> findByEmployeeCountHigherThan(int minEmployeeCount);

	public List<AverageSalaryByPosition> findAverageSalariesByPosition(long companyId);
	
}
