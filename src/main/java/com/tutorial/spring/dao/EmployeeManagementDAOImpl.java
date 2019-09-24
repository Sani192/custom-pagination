package com.tutorial.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.tutorial.spring.model.Employee;

public class EmployeeManagementDAOImpl implements EmployeeManagementDAO {

	private JdbcTemplate jdbcTemplate;
	
	public EmployeeManagementDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void save(Employee employee) {
		if (employee != null && employee.getId() > 0) {
			// update
			final String sql = "UPDATE employee SET name=?, email=?, address=?, "
						+ "telephone=? WHERE id=?";
			jdbcTemplate.update(sql, employee.getName(), employee.getEmail(),
					employee.getAddress(), employee.getTelephone(), employee.getId());
		} else {
			// insert
			final String sql = "INSERT INTO employee (name, email, address, telephone)"
						+ " VALUES (?, ?, ?, ?)";
			jdbcTemplate.update(sql, employee.getName(), employee.getEmail(),
					employee.getAddress(), employee.getTelephone());
		}
		
	}

	@Override
	public void delete(int id) {
		final String sql = "DELETE FROM employee WHERE id=?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Employee> findAll(HashMap<String, Object> paramMap) {
		if(paramMap == null) {
			paramMap = new HashMap<>();
		}
		String limitQuery = "";
		if(paramMap.containsKey("LIMIT_START") && paramMap.containsKey("LIMIT_END") ) {
			limitQuery = " LIMIT " + paramMap.get("LIMIT_START") + " , " + paramMap.get("LIMIT_END");
		}
		String sql = "SELECT * FROM employee WHERE 1=1";
		if(paramMap.get("ID") != null) {
			sql += " AND id = " + paramMap.get("ID");
		}
		sql += " " + limitQuery;
		final List<Employee> list = jdbcTemplate.query(sql, new RowMapper<Employee>() {
			@Override
			public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
				Employee employeeVO = new Employee();
				employeeVO.setId(rs.getInt("id"));
				employeeVO.setName(rs.getString("name"));
				employeeVO.setEmail(rs.getString("email"));
				employeeVO.setAddress(rs.getString("address"));
				employeeVO.setTelephone(rs.getString("telephone"));
				return employeeVO;
			}
		});
		return list;
	}

	@Override
	public Employee findById(int id) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("ID", id);
		List<Employee> list = findAll(paramMap);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public int getCount() {
		String sql = "SELECT count(*) FROM employee";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
}