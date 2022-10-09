package net.ingoh.minecraft.plugins.ingobotmc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import net.ingoh.util.calculator.Calculator;

public class CalculatorTest {

    // Manual testing
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String line = "";
        while (!line.equals("end")) {
            if (s.hasNextLine()) {
                line = s.nextLine();
                String[] r = Calculator.calculate(line);
                for (int i = 0; i < r.length; i++) {
                    String out = r[i];
                    System.out.println(out);
                }
            }
        }
        s.close();
    }

    @Test
    public void evaluateSimpleAddition() {
        assertEquals("69", Calculator.calculate("41+28")[0]);
    }

    @Test
    public void evaluateSimpleParentheses() {
        assertEquals("28", Calculator.calculate("2*(2+3*(2/(1/2")[0]);
    }

    @Test
    public void evaluateSimpleParentheses2() {
        assertEquals("466", Calculator.calculate("2*(5+3*(2-4)*(12-5*(10)))")[0]);
    }

    @Test
    public void evaluateSimpleMod() {
        assertEquals("4", Calculator.calculate("(2*5)%(12/2)")[0]);
    }

    @Test
    public void evaluateSimpleFactorial() {
        assertEquals("3628800", Calculator.calculate("10!!")[0]);
    }

    @Test
    public void evaluateUnary() {
        assertEquals("2", Calculator.calculate("1++++--")[0]);
    }

    @Test
    public void evaluateVariables() {
        assertEquals("5", Calculator.calculate("a=1+1;three=2*2-1;2a+three/3")[0]);
    }
}
