import java.util.ArrayList;
import java.util.List;

interface Workable {
    String getWorkStatus();
    default String getTools() {
        return "Laptop, Coffee mug";
    }
}

abstract class Employee {
    protected String name;
    protected int id;
    protected double baseSalary;
    private static int idCounter = 1;

    public Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
        this.id = idCounter++;
    }

    public String getName() {
        return name;
    }

    public abstract double calculateSalary();
}

class Manager extends Employee implements Workable {
    private double projectBonus;

    public Manager(String name, double baseSalary, double projectBonus) {
        super(name, baseSalary);
        this.projectBonus = projectBonus;
    }

    @Override
    public double calculateSalary() {
        return this.baseSalary + this.projectBonus;
    }

    @Override
    public String getWorkStatus() {
        return "Manager " + name + " is holding meetings.";
    }
}

class Developer extends Employee implements Workable {
    private String programmingLanguage;

    public Developer(String name, double baseSalary, String programmingLanguage) {
        super(name, baseSalary);
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public double calculateSalary() {
        return this.baseSalary * 1.20;
    }

    @Override
    public String getWorkStatus() {
        return "Developer " + name + " is writing code in " + this.programmingLanguage + ".";
    }
}

class Designer extends Employee implements Workable {
    private String designTool;

    public Designer(String name, double baseSalary, String designTool) {
        super(name, baseSalary);
        this.designTool = designTool;
    }

    @Override
    public double calculateSalary() {
        return this.baseSalary * 1.10;
    }


    @Override
    public String getWorkStatus() {
        return "Designer " + name + " is creating mockups in " + this.designTool + ".";
    }

    @Override
    public String getTools() {
        return Workable.super.getTools() + ", " + this.designTool + " Tablet";
    }
}

class EmployeeFactory {
    public static Employee createEmployee(String type, String name, double baseSalary, String specialization) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Employee type cannot be null or empty");
        }
        
        switch (type.toLowerCase()) {
            case "manager":
                try {
                    double bonus = Double.parseDouble(specialization);
                    return new Manager(name, baseSalary, bonus);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Specialization for manager must be a number (bonus amount)");
                }
            case "developer":
                return new Developer(name, baseSalary, specialization);
            case "designer":
                return new Designer(name, baseSalary, specialization);
            default:
                throw new IllegalArgumentException("Unknown employee type: " + type);
        }
    }
}

public class CompanyDemo {
    public static void main(String[] args) {
        
        Employee manager = EmployeeFactory.createEmployee("manager", "Olena", 5000, "1500");
        Employee developer = EmployeeFactory.createEmployee("developer", "Andriy", 4000, "Java");
        Employee designer = EmployeeFactory.createEmployee("designer", "Maria", 3500, "Figma");

        List<Employee> employees = new ArrayList<>();
        employees.add(manager);
        employees.add(developer);
        employees.add(designer);
        
        for (Employee emp : employees) {
            String empInfo = emp.getName() + ": Salary " + emp.calculateSalary();
            
            if (emp instanceof Workable) {
                Workable worker = (Workable) emp;
                empInfo += " | Status: " + worker.getWorkStatus();
                empInfo += " | Tools: " + worker.getTools();
            }
            System.out.println(empInfo);
            System.out.println(); 
        }
    }
}