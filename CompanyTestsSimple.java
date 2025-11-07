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

// --- Клас для тестів ---

public class CompanyTestsSimple {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;
        int totalTests = 6;

        if (runTest("testFactoryCreatesDeveloper", CompanyTestsSimple::testFactoryCreatesDeveloper)) passed++; else failed++;
        if (runTest("testFactoryCreatesManager", CompanyTestsSimple::testFactoryCreatesManager)) passed++; else failed++;
        if (runTest("testFactoryThrowsExceptionOnUnknownType", CompanyTestsSimple::testFactoryThrowsExceptionOnUnknownType)) passed++; else failed++;
        if (runTest("testFactoryThrowsExceptionOnBadManagerBonus", CompanyTestsSimple::testFactoryThrowsExceptionOnBadManagerBonus)) passed++; else failed++;
        if (runTest("testPolymorphicSalaryCalculation", CompanyTestsSimple::testPolymorphicSalaryCalculation)) passed++; else failed++;
        if (runTest("testDefaultMethodOverride", CompanyTestsSimple::testDefaultMethodOverride)) passed++; else failed++;

        System.out.println("---");
        System.out.println("Result: " + passed + "/" + totalTests + " passed.");
        if (failed > 0) {
            System.out.println(failed + " test(s) FAILED.");
        }
    }

    private static boolean runTest(String testName, Runnable test) {
        try {
            test.run();
            System.out.println(testName + ": OK");
            return true;
        } catch (Exception e) {
            System.out.println(testName + ": FAILED (" + e.getMessage() + ")");
            return false;
        }
    }

    private static void checkEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new RuntimeException(message + " | Expected: [" + expected + "], Got: [" + actual + "]");
        }
    }
    
    private static void checkEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.001) {
            throw new RuntimeException(message + " | Expected: [" + expected + "], Got: [" + actual + "]");
        }
    }
    
    private static void checkTrue(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    private static void testFactoryCreatesDeveloper() {
        Employee emp = EmployeeFactory.createEmployee("developer", "TestDev", 1000, "Python");
        checkTrue(emp instanceof Developer, "Should be an instance of Developer");
        checkEquals("TestDev", emp.getName(), "Name mismatch");
    }

    private static void testFactoryCreatesManager() {
        Employee emp = EmployeeFactory.createEmployee("manager", "TestMgr", 2000, "500");
        checkTrue(emp instanceof Manager, "Should be an instance of Manager");
        checkEquals(2500.0, emp.calculateSalary(), "Salary calculation incorrect");
    }

    private static void testFactoryThrowsExceptionOnUnknownType() {
        try {
            EmployeeFactory.createEmployee("intern", "TestIntern", 500, "None");
            throw new RuntimeException("Test failed: Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // OK
        } catch (Exception e) {
            throw new RuntimeException("Test failed: Expected IllegalArgumentException, but got " + e.getClass().getName());
        }
    }
    
    private static void testFactoryThrowsExceptionOnBadManagerBonus() {
        try {
            EmployeeFactory.createEmployee("manager", "BadMgr", 3000, "abc");
            throw new RuntimeException("Test failed: Expected IllegalArgumentException for bad bonus");
        } catch (IllegalArgumentException e) {
            // OK
        } catch (Exception e) {
            throw new RuntimeException("Test failed: Expected IllegalArgumentException, but got " + e.getClass().getName());
        }
    }

    private static void testPolymorphicSalaryCalculation() {
        Employee dev = EmployeeFactory.createEmployee("developer", "Dev", 1000, "Java");
        Employee mgr = EmployeeFactory.createEmployee("manager", "Mgr", 2000, "500");
        
        checkEquals(1200.0, dev.calculateSalary(), "Developer salary incorrect");
        checkEquals(2500.0, mgr.calculateSalary(), "Manager salary incorrect");
    }

    private static void testDefaultMethodOverride() {
        Workable dev = (Workable) EmployeeFactory.createEmployee("developer", "Dev", 1000, "Java");
        Workable dsn = (Workable) EmployeeFactory.createEmployee("designer", "Mary", 1000, "Figma");

        checkEquals("Laptop, Coffee mug", dev.getTools(), "Developer tools incorrect");
        checkEquals("Laptop, Coffee mug, Figma Tablet", dsn.getTools(), "Designer tools incorrect");
    }
}