package it.polito.policytoollib.test.generator;

import it.polito.policytoollib.generator.policy.FilteringPolicyGenerator;
import it.polito.policytoollib.generator.policy.SquidPolicyGenerator;
import it.polito.policytoollib.generator.util.PolicyStatistics;
import it.polito.policytoollib.policy.impl.Policy;

import org.junit.Test;

public class PolicyGeneratorCalibation {
	@Test
	public void run_test() throws Exception {
		
		//test_0_0();
		
		//test_1_5();
		
		//test_2_10();
		
		test_4_20();
		
		//test_8_40();
	}
	
	public static void test_0_0() throws Exception {
		int max=0;
		double inter=0;
		int i;
		FilteringPolicyGenerator gen = new FilteringPolicyGenerator();

		for (i = 0; i < 10; i++) {
			Policy policy = gen.getPolicy_0_0(1000, 0, "test");
			PolicyStatistics stat = new PolicyStatistics(policy);
			max += stat.getMAX();
			inter += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
		}
		
	}
	
	public static void test_1_5() throws Exception {
		int max=0;
		double inter=0;
		int i;
		FilteringPolicyGenerator gen = new FilteringPolicyGenerator();

		for (i = 0; i < 10; i++) {
			Policy policy = gen.getPolicy_1_5(1000, 0, "test");
			PolicyStatistics stat = new PolicyStatistics(policy);
			max += stat.getMAX();
			inter += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
		}
		
	}
	
	public static void test_2_10() throws Exception {
		int max=0;
		double inter=0;
		int i;
		FilteringPolicyGenerator gen = new FilteringPolicyGenerator();

		for (i = 0; i < 10; i++) {
			Policy policy = gen.getPolicy_2_10(1000, 0, "test");
			PolicyStatistics stat = new PolicyStatistics(policy);
			max += stat.getMAX();
			inter += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
		}
		
	}
	

	public static void test_4_20() throws Exception {
		int max50 = 0, max100 = 0, max250 = 0, max500 = 0, max750 = 0, max1000 = 0, max1500 = 0, max2000 = 0, max2500 = 0, max3000 = 0, max3500 = 0, max4000 = 0, max4500 = 0, max5000 = 0;
		double inter50 = 0, inter100 = 0, inter250 = 0, inter500 = 0, inter750 = 0, inter1000 = 0, inter1500 = 0, inter2000 = 0, inter2500 = 0, inter3000 = 0, inter3500 = 0, inter4000 = 0, inter4500 = 0, inter5000 = 0;

		int i = 0;

		FilteringPolicyGenerator gen = new FilteringPolicyGenerator();

		for (i = 0; i < 10; i++) {
			Policy policy = gen.getPolicy_4_20(50, 0, "test");
			PolicyStatistics stat = new PolicyStatistics(policy);
			max50 += stat.getMAX();
			inter50 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(100, 0, "test");
			stat = new PolicyStatistics(policy);
			max100 += stat.getMAX();
			inter100 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(150, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(200, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(250, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(300, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(350, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(400, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(450, 0, "test");
			stat = new PolicyStatistics(policy);
			max250 += stat.getMAX();
			inter250 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(500, 0, "test");
			stat = new PolicyStatistics(policy);
			max500 += stat.getMAX();
			inter500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(750, 0, "test");
			stat = new PolicyStatistics(policy);
			max750 += stat.getMAX();
			inter750 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(1000, 0, "test");
			stat = new PolicyStatistics(policy);
			max1000 += stat.getMAX();
			inter1000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(1500, 0, "test");
			stat = new PolicyStatistics(policy);
			max1500 += stat.getMAX();
			inter1500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(2000, 0, "test");
			stat = new PolicyStatistics(policy);
			max2000 += stat.getMAX();
			inter2000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(2500, 0, "test");
			stat = new PolicyStatistics(policy);
			max2500 += stat.getMAX();
			inter2500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(3000, 0, "test");
			stat = new PolicyStatistics(policy);
			max3000 += stat.getMAX();
			inter3000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(3500, 0, "test");
			stat = new PolicyStatistics(policy);
			max3500 += stat.getMAX();
			inter3500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(4000, 0, "test");
			stat = new PolicyStatistics(policy);
			max4000 += stat.getMAX();
			inter4000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_4_20(4500, 0, "test");
			stat = new PolicyStatistics(policy);
			max4500 += stat.getMAX();
			inter4500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_4_20(5000, 0, "test");
			stat = new PolicyStatistics(policy);
			max5000 += stat.getMAX();
			inter5000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			System.out.println();
		}

		System.out.println("======================================================================================");
		System.out.print((int) max50 / i + " " + (int) inter50 / i + "   ");
		System.out.print((int) max100 / i + " " + (int) inter100 / i + "   ");
		System.out.print((int) max250 / i + " " + (int) inter250 / i + "   ");
		System.out.print((int) max500 / i + " " + (int) inter500 / i + "   ");
		System.out.print((int) max750 / i + " " + (int) inter750 / i + "   ");
		System.out.print((int) max1000 / i + " " + (int) inter1000 / i + "   ");
		System.out.print((int) max1500 / i + " " + (int) inter1500 / i + "   ");
		System.out.print((int) max2000 / i + " " + (int) inter2000 / i + "   ");
		System.out.print((int) max2500 / i + " " + (int) inter2500 / i + "   ");
		System.out.print((int) max3000 / i + " " + (int) inter3000 / i + "   ");
		System.out.print((int) max3500 / i + " " + (int) inter3500 / i + "   ");
		System.out.print((int) max4000 / i + " " + (int) inter4000 / i + "   ");
		System.out.print((int) max4500 / i + " " + (int) inter4500 / i + "   ");
		System.out.print((int) max5000 / i + " " + (int) inter5000 / i + "   ");

		System.out.println();
		System.out.println("======================================================================================");
		System.out.println(max50 + " " + inter50 + " ");
		System.out.println(max100 + " " + inter100 + " ");
		System.out.println(max250 + " " + inter250 + " ");
		System.out.println(max500 + " " + inter500 + " ");
		System.out.println(max750 + " " + inter750 + " ");
		System.out.println(max1000 + " " + inter1000 + " ");
		System.out.println(max1500 + " " + inter1500 + " ");
		System.out.println(max2000 + " " + inter2000 + " ");
		System.out.println(max2500 + " " + inter2500 + " ");
		System.out.println(max3000 + " " + inter3000 + " ");
		System.out.println(max3500 + " " + inter3500 + " ");
		System.out.println(max4000 + " " + inter4000 + " ");
		System.out.println(max4500 + " " + inter4500 + " ");
		System.out.println(max5000 + " " + inter5000 + " ");
	}

	public static void test_8_40() throws Exception {
		int max500 = 0, max1000 = 0, max1500 = 0, max2000 = 0, max2500 = 0, max3000 = 0, max3500 = 0, max4000 = 0, max4500 = 0, max5000 = 0;
		double inter500 = 0, inter1000 = 0, inter1500 = 0, inter2000 = 0, inter2500 = 0, inter3000 = 0, inter3500 = 0, inter4000 = 0, inter4500 = 0, inter5000 = 0;

		int i = 0;

		FilteringPolicyGenerator gen = new FilteringPolicyGenerator();

		for (i = 0; i < 10; i++) {
			Policy policy = gen.getPolicy_8_40(500, 0, "test");
			PolicyStatistics stat = new PolicyStatistics(policy);
			max500 += stat.getMAX();
			inter500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(1000, 0, "test");
			stat = new PolicyStatistics(policy);
			max1000 += stat.getMAX();
			inter1000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(1500, 0, "test");
			stat = new PolicyStatistics(policy);
			max1500 += stat.getMAX();
			inter1500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(2000, 0, "test");
			stat = new PolicyStatistics(policy);
			max2000 += stat.getMAX();
			inter2000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(2500, 0, "test");
			stat = new PolicyStatistics(policy);
			max2500 += stat.getMAX();
			inter2500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_8_40(3000, 0, "test");
			stat = new PolicyStatistics(policy);
			max3000 += stat.getMAX();
			inter3000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(3500, 0, "test");
			stat = new PolicyStatistics(policy);
			max3500 += stat.getMAX();
			inter3500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(4000, 0, "test");
			stat = new PolicyStatistics(policy);
			max4000 += stat.getMAX();
			inter4000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");

			policy = gen.getPolicy_8_40(4500, 0, "test");
			stat = new PolicyStatistics(policy);
			max4500 += stat.getMAX();
			inter4500 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			
			policy = gen.getPolicy_8_40(5000, 0, "test");
			stat = new PolicyStatistics(policy);
			max5000 += stat.getMAX();
			inter5000 += stat.getIntersectingNoPercent();
			System.out.print(stat.getMAX() + " " + (int) stat.getIntersectingNoPercent() + "   ");
			System.out.println();
		}

		System.out.println("======================================================================================");
		System.out.print((int) max500 / i + " " + (int) inter500 / i + "   ");
		System.out.print((int) max1000 / i + " " + (int) inter1000 / i + "   ");
		System.out.print((int) max1500 / i + " " + (int) inter1500 / i + "   ");
		System.out.print((int) max2000 / i + " " + (int) inter2000 / i + "   ");
		System.out.print((int) max2500 / i + " " + (int) inter2500 / i + "   ");
		System.out.print((int) max3000 / i + " " + (int) inter3000 / i + "   ");
		System.out.print((int) max3500 / i + " " + (int) inter3500 / i + "   ");
		System.out.print((int) max4000 / i + " " + (int) inter4000 / i + "   ");
		System.out.print((int) max4500 / i + " " + (int) inter4500 / i + "   ");
		System.out.print((int) max5000 / i + " " + (int) inter5000 / i + "   ");

		System.out.println();
		System.out.println("======================================================================================");
		System.out.println(max500 + " " + inter500 + " ");
		System.out.println(max1000 + " " + inter1000 + " ");
		System.out.println(max1500 + " " + inter1500 + " ");
		System.out.println(max2000 + " " + inter2000 + " ");
		System.out.println(max2500 + " " + inter2500 + " ");
		System.out.println(max3000 + " " + inter3000 + " ");
		System.out.println(max3500 + " " + inter3500 + " ");
		System.out.println(max4000 + " " + inter4000 + " ");
		System.out.println(max4500 + " " + inter4500 + " ");
		System.out.println(max5000 + " " + inter5000 + " ");
	}
}
