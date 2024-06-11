import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.text.*;
import java.math.*;
class EmployeeManagerUpdateTestCase
{
public static void main(String gg[])
{
try
{
String employeeId="A10000001";
String name="Chhavi Chouhan";
DesignationInterface designation=new Designation();
designation.setCode(2);
Date dateOfBirth=new Date();
boolean isIndian=true ;
BigDecimal basicSalary=new BigDecimal("440000");
String panNumber="PANCC1234";
String aadharCardNumber="ACNCC1234";
EmployeeInterface employee;
employee=new Employee();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDesignation(designation);
employee.setDateOfBirth(dateOfBirth);
employee.setGender(GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
EmployeeManagerInterface employeeManager;
employeeManager=EmployeeManager.getEmployeeManager();
employeeManager.updateEmployee(employee);
System.out.println("Employee updated");
}catch(BLException blException)
{
if(blException.hasGenericException())System.out.println(blException.getGenericException());
List<String> properties=blException.getProperties();
for(String property:properties)
{
System.out.println(blException.getException(property));
}
}
}
}
