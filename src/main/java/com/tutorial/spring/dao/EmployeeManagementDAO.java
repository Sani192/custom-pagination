package com.tutorial.spring.dao;

import java.util.HashMap;
import java.util.List;

import com.tutorial.spring.model.Employee;

public interface EmployeeManagementDAO {

	public void save(Employee employee);
	
	public void delete(int id);
	
	public Employee findById(int id);
	
	public List<Employee> findAll(HashMap<String, Object> paramMap);

	public int getCount();
}