package cn.acyou.leo.tool.test.testcase;

import java.util.Scanner;

/**
 * @author youfang
 * @version [1.0.0, 2025/9/5]
 **/
public class IncomeTaxCalculator2025 {
    // 个税税率表（2025版）
    private static final double[] TAX_RATES = {0.03, 0.10, 0.20, 0.25, 0.30, 0.35, 0.45};
    private static final double[] TAX_THRESHOLDS = {0, 36000, 144000, 300000, 420000, 660000, 960000};
    private static final double[] QUICK_DEDUCTIONS = {0, 2520, 16920, 31920, 52920, 85920, 181920};

    // 个税起征点（每月）
    private static final double TAX_FREE_INCOME = 5000;

    // 累计收入和扣除项
    private double cumulativeIncome = 0;
    private double cumulativeDeduction = 0;
    private double cumulativeTaxPaid = 0;
    private int month = 0;

    public static void main(String[] args) {
        IncomeTaxCalculator2025 calculator = new IncomeTaxCalculator2025();
        double salary = 50000;
        double insurance = 2000;
        for (int i = 1; i <= 12; i++) {
            MonthlyResult result = calculator.calculateMonthlyTax(salary, insurance);
            System.out.printf("第["+i+"]月:\t");
            System.out.printf("本月应交个税: %.2f 元\t", result.tax);
            System.out.printf("本月税后工资: %.2f 元%n", result.afterTaxSalary);
        }
    }
    public static void main2(String[] args) {
        IncomeTaxCalculator2025 calculator = new IncomeTaxCalculator2025();
        Scanner scanner = new Scanner(System.in);
        System.out.println("个人所得税计算器（2025版）");
        System.out.println("请输入每月工资和五险一金金额（输入-1结束）：");
        System.out.print("工资收入（元）: ");
        double salary = scanner.nextDouble();
        System.out.print("五险一金（元）: ");
        double insurance = scanner.nextDouble();
        for (int i = 1; i <= 12; i++) {
            MonthlyResult result = calculator.calculateMonthlyTax(salary, insurance);
            System.out.print("第["+i+"]月:");
            System.out.printf("本月应交个税: %.2f 元%n", result.tax);
            System.out.printf("本月税后工资: %.2f 元%n", result.afterTaxSalary);
            System.out.println("------------------------");
        }
        scanner.close();
    }

    /**
     * 计算每月个人所得税和税后工资
     * @param monthlySalary 月工资
     * @param monthlyInsurance 五险一金金额
     * @return 包含个税和税后工资的结果对象
     */
    public MonthlyResult calculateMonthlyTax(double monthlySalary, double monthlyInsurance) {
        month++;
        cumulativeIncome += monthlySalary;
        cumulativeDeduction += monthlyInsurance;

        // 计算累计应纳税所得额
        double cumulativeTaxableIncome = cumulativeIncome - cumulativeDeduction - (TAX_FREE_INCOME * month);

        // 如果累计应纳税所得额小于等于0，则不需要交税
        if (cumulativeTaxableIncome <= 0) {
            double afterTaxSalary = monthlySalary - monthlyInsurance;
            return new MonthlyResult(0, afterTaxSalary);
        }

        // 根据累计应纳税所得额确定适用税率和速算扣除数
        int bracket = findTaxBracket(cumulativeTaxableIncome);
        double taxRate = TAX_RATES[bracket];
        double quickDeduction = QUICK_DEDUCTIONS[bracket];

        // 计算累计应纳个税
        double cumulativeTax = cumulativeTaxableIncome * taxRate - quickDeduction;

        // 计算本月应纳个税（累计应纳个税 - 已缴纳个税）
        double monthlyTax = cumulativeTax - cumulativeTaxPaid;
        cumulativeTaxPaid = cumulativeTax;

        // 计算税后工资
        double afterTaxSalary = monthlySalary - monthlyInsurance - monthlyTax;

        return new MonthlyResult(monthlyTax, afterTaxSalary);
    }

    /**
     * 根据应纳税所得额查找适用的税率区间
     * @param taxableIncome 应纳税所得额
     * @return 税率区间索引
     */
    private int findTaxBracket(double taxableIncome) {
        for (int i = TAX_THRESHOLDS.length - 1; i >= 0; i--) {
            if (taxableIncome > TAX_THRESHOLDS[i]) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 用于存储每月计算结果的数据类
     */
    public static class MonthlyResult {
        public double tax;
        public double afterTaxSalary;

        public MonthlyResult(double tax, double afterTaxSalary) {
            this.tax = tax;
            this.afterTaxSalary = afterTaxSalary;
        }
    }
}
