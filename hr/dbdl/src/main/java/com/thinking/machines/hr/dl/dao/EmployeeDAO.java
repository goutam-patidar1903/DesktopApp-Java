package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
import java.sql.*;
import java.io.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private final static String FILE_NAME="employee.data";

public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null");
String employeeId;
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null");
name=name.trim();
if(name.length()==0) throw new DAOException("Name is of zero Length");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid desigantion code : "+designationCode);
Connection connection =null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select code from designation where code =?");
preparedStatement.setInt(1,designationCode);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Designation Code : "+designationCode);
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) 
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Date of birth is null");
}
char gender=employeeDTO.getGender();
if(gender==' ')
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Gender not set to Male/Female");
}
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Basic salary is null");
}
if(basicSalary.signum()==-1)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Basic Salary is negative");
}
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("PAN number is null");
}
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("PAN number is of zero length");
}
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Aadhar card number is null");
}
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Aadhar card number is of zero length");
}
try
{
boolean panNumberExists;
boolean aadharCardNumberExists;
preparedStatement = connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
resultSet = preparedStatement.executeQuery();
panNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();
preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet = preparedStatement.executeQuery();
aadharCardNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();
if(panNumberExists&&aadharCardNumberExists) 
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Pan number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+")exists");
}
if(panNumberExists)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Pan number exists : "+panNumber);
}
if(aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Aadhar card number exists : "+aadharCardNumber);
}
preparedStatement = connection.prepareStatement("insert into employee (name,designation_code,date_of_birth,basic_salary,gender,is_indian,pan_number,aadhar_card_number) values (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDateOfBirth);
preparedStatement.setBigDecimal(4,basicSalary);
preparedStatement.setString(5,String.valueOf(gender));
preparedStatement.setBoolean(6,isIndian);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.executeUpdate();
resultSet = preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedEmployeeId = resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
employeeDTO.setEmployeeId("A"+(1000000+generatedEmployeeId));
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("Invalid Employee Id. : "+employeeId);
if(employeeId.length()==0)throw new DAOException("Length of Employee Id is Zero");
int actualEmployeeId;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception e)
{
throw new DAOException("Invalid Employee Id");
}
Connection connection =null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where employee_id =?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(" Employee Id : "+employeeId+" not exists");
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid desigantion code : "+designationCode);
try
{
preparedStatement = connection.prepareStatement("select code from designation where code =?");
preparedStatement.setInt(1,designationCode);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Designation Code : "+designationCode);
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
String name=employeeDTO.getName();
if(name==null) 
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Name is null");
}
name=name.trim();
if(name.length()==0) 
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Name is of zero Length");
}
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) 
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Date of birth is null");
}
char gender=employeeDTO.getGender();
if(gender==' ')
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Gender not set to Male/Female");
}
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Basic salary is null");
}
if(basicSalary.signum()==-1)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Basic Salary is negative");
}
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("PAN number is null");
}
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("PAN number is of zero length");
}
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Aadhar card number is null");
}
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Aadhar card number is of zero length");
}
try
{
boolean panNumberExists;
boolean aadharCardNumberExists;
preparedStatement = connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?");
preparedStatement.setString(1,panNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
panNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();
preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=? and employee_id<>?");
preparedStatement.setString(1,aadharCardNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
aadharCardNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();
if(panNumberExists&&aadharCardNumberExists) 
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Pan number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+")exists");
}
if(panNumberExists)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Pan number exists : "+panNumber);
}
if(aadharCardNumberExists)
{
try
{
connection.close();
}catch(SQLException sql)
{
throw new DAOException(sql.getMessage());
}
throw new DAOException("Aadhar card number exists : "+aadharCardNumber);
}
preparedStatement = connection.prepareStatement("update employee set name=? ,designation_code=? ,date_of_birth=? ,basic_salary=? ,gender=? ,is_indian=? ,pan_number=? ,aadhar_card_number=? where employee_id=?");
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDateOfBirth);
preparedStatement.setBigDecimal(4,basicSalary);
preparedStatement.setString(5,String.valueOf(gender));
preparedStatement.setBoolean(6,isIndian);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.setInt(9,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Invalid Employee Id. : "+employeeId);
if(employeeId.length()==0)throw new DAOException("Length of Employee Id is Zero");
int actualEmployeeId;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception e)
{
throw new DAOException("Invalid Employee Id");
}
Connection connection =null;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where employee_id =?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(" Employee Id : "+employeeId+" not exists");
}
resultSet.close();
preparedStatement.close();
preparedStatement = connection.prepareStatement("delete from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Invalid Employee Id. : "+employeeId);
if(employeeId.length()==0)throw new DAOException("Length of Employee Id is Zero");
int actualEmployeeId;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception e)
{
throw new DAOException("Invalid Employee Id");
}
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select * from employee where employee_id =?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(" Employee Id : "+employeeId+" not exists");
}
EmployeeDTOInterface employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name"));
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
java.sql.Date sqlDOB = resultSet.getDate("date_of_birth");
java.util.Date dob = new java.util.Date(sqlDOB.getYear(),sqlDOB.getMonth(),sqlDOB.getDate());
employeeDTO.setDateOfBirth(dob);
employeeDTO.setGender((resultSet.getString("gender").equals("M"))?(GENDER.MALE):(GENDER.FEMALE));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number"));
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null) throw new DAOException("Pan Number cannot be null ");
panNumber=panNumber.trim();
if(panNumber.length()==0)throw new DAOException("Invalid PAN number : pan number is of zero length");
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select * from employee where pan_number =?");
preparedStatement.setString(1,panNumber);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(" Pan Number : "+panNumber+" not exists");
}
EmployeeDTOInterface employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name"));
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
java.sql.Date sqlDOB = resultSet.getDate("date_of_birth");
java.util.Date dob = new java.util.Date(sqlDOB.getYear(),sqlDOB.getMonth(),sqlDOB.getDate());
employeeDTO.setDateOfBirth(dob);
employeeDTO.setGender((resultSet.getString("gender").equals("M"))?(GENDER.MALE):(GENDER.FEMALE));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number"));
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) throw new DAOException("Invalid Aadhar card number : "+aadharCardNumber);
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)throw new DAOException("Invalid Aadhar card number : Aadhar card number is of zero length");
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select * from employee where aadhar_card_number =?");
preparedStatement.setString(1,aadharCardNumber);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException(" Aadhar Card Number : "+aadharCardNumber+" not exists");
}
EmployeeDTOInterface employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name"));
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
java.sql.Date sqlDOB = resultSet.getDate("date_of_birth");
java.util.Date dob = new java.util.Date(sqlDOB.getYear(),sqlDOB.getMonth(),sqlDOB.getDate());
employeeDTO.setDateOfBirth(dob);
employeeDTO.setGender((resultSet.getString("gender").equals("M"))?(GENDER.MALE):(GENDER.FEMALE));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number"));
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
if(designationDAO.codeExists(designationCode)==false) throw new DAOException("Invalid designation code : "+designationCode);
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
Connection connection ;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select * from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet = preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO ;
java.sql.Date sqlDOB;
java.util.Date dob ;
while(resultSet.next())
{
employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name"));
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDOB = resultSet.getDate("date_of_birth");
dob = new java.util.Date(sqlDOB.getYear(),sqlDOB.getMonth(),sqlDOB.getDate());
employeeDTO.setDateOfBirth(dob);
employeeDTO.setGender((resultSet.getString("gender").equals("M"))?(GENDER.MALE):(GENDER.FEMALE));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number"));
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
employees.add(employeeDTO);
}
resultSet.close();
preparedStatement.close();
connection.close();
return employees;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
Connection connection ;
Statement statement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
statement=connection.createStatement();
resultSet = statement.executeQuery("select * from employee");
EmployeeDTOInterface employeeDTO ;
java.sql.Date sqlDOB;
java.util.Date dob ;
while(resultSet.next())
{
employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name"));
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDOB = resultSet.getDate("date_of_birth");
dob = new java.util.Date(sqlDOB.getYear(),sqlDOB.getMonth(),sqlDOB.getDate());
employeeDTO.setDateOfBirth(dob);
employeeDTO.setGender((resultSet.getString("gender").equals("M"))?(GENDER.MALE):(GENDER.FEMALE));
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number"));
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
employees.add(employeeDTO);
}
resultSet.close();
statement.close();
connection.close();
return employees;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public boolean isDesignationAlloted(int designationCode)throws DAOException
{
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
if(designationDAO.codeExists(designationCode)==false) throw new DAOException("Invalid designation code : "+designationCode);
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false) return false;
resultSet.close();
preparedStatement.close();
connection.close();
return true;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null) return false;
employeeId=employeeId.trim();
if(employeeId.length()==0)return false;
int actualEmployeeId;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false) return false;
resultSet.close();
preparedStatement.close();
connection.close();
return true;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public boolean panNumberExists(String panNumber)throws DAOException
{
if(panNumber==null) return false;
panNumber=panNumber.trim();
if(panNumber.length()==0)return false;
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false) return false;
resultSet.close();
preparedStatement.close();
connection.close();
return true;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException
{
if(aadharCardNumber==null) return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)return false ;
try
{
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
connection = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false) return false;
resultSet.close();
preparedStatement.close();
connection.close();
return true;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public int getCount()throws DAOException
{
int count =0;
try
{
Connection connection = DAOConnection.getConnection();
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("select count(*) as cnt from employee");
resultSet.next();
count = resultSet.getInt("cnt");
resultSet.close();
statement.close();
connection.close();
return count;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public int getCountByDesignation(int designationCode)throws DAOException
{
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
if(designationDAO.codeExists(designationCode)==false) throw new DAOException("Invalid designation code : "+designationCode);
int count =0;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select count(*) as cnt from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet = preparedStatement.executeQuery();
resultSet.next();
count = resultSet.getInt("cnt");
resultSet.close();
preparedStatement.close();
connection.close();
return count;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

}