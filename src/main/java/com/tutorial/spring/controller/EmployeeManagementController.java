package com.tutorial.spring.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tutorial.spring.dao.EmployeeManagementDAO;
import com.tutorial.spring.model.Employee;
import com.tutorial.spring.utils.PaginationUtils;

@Controller
public class EmployeeManagementController {

	@Autowired
	EmployeeManagementDAO employeeManagementDAO;
	
	@RequestMapping(value="/list")
	public ModelAndView listEmployee(HttpServletRequest request, ModelAndView model) throws IOException{
		String requestedPage = request.getParameter("page");
		int totalRecords = employeeManagementDAO.getCount();
		HashMap<String, Object> paramMap = new HashMap<>();
		// add pagination
		PaginationUtils.addPaginationLimit(paramMap, totalRecords, String.valueOf(PaginationUtils.RECORDS_PER_PAGE), requestedPage);
		// list
		List<Employee> list = employeeManagementDAO.findAll(paramMap);
		model.addObject("totalRecords", totalRecords);
		model.addObject("employeeList", list);
		model.setViewName("list-employee");
		return model;
	}
	
	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ModelAndView get(ModelAndView model, HttpServletRequest request) {
		Employee employee = new Employee();
		final String id = request.getParameter("id");
		if(StringUtils.isNumeric(id)) {
			employee = employeeManagementDAO.findById(Integer.parseInt(id));
		}
		model.addObject("employee", employee);
		model.setViewName("employee-form");
		return model;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute Employee employee) {
		employeeManagementDAO.save(employee);		
		return new ModelAndView("redirect:/list");
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest request) {
		final String id = request.getParameter("id");
		if(StringUtils.isNumeric(id)) {
			employeeManagementDAO.delete(Integer.parseInt(id));
		}
		return new ModelAndView("redirect:/list");
	}
}