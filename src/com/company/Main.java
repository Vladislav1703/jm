package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStreamReader streamInput = new InputStreamReader(System.in);
        BufferedReader readerinput = new BufferedReader(streamInput); // читаем данные с инпут стрима
        String inputstring;

        do {
            inputstring = readerinput.readLine(); // конвертируем в строку
            InputValidation.validateInput(inputstring);
            inputstring = inputstring.replaceAll("\\s+",""); // "Строка после валидации и удаления всех пробелов"
            String[] operands = inputstring.split("[+\\-/*]");
            String operator = Calculator.getOperator(inputstring);
            if (InputValidation.checkOnlyRomanNumber(inputstring)) {
                int operatorA = ConvertOperators.romanToInt(operands[0]);
                int operatorB = ConvertOperators.romanToInt(operands[1]);
                int result = Calculator.calculate(operatorA, operatorB, operator);
                result = (int) Math.floor(result);
                RomanValidation.validateInt(result);
                String romanResult = ConvertOperators.intToRoman(result);
                System.out.println("roman Result: " + romanResult);
            } else {
                Number result = Calculator.calculate(Integer.parseInt(operands[0]), Integer.parseInt(operands[1]), operator);
                System.out.println("result: " + result);
            }

        } while (!inputstring.equals(""));
    }
}

class RomanValidation {
    public static void validateInt (int ArabicInt) throws IOException {
        if (ArabicInt < 0) {
            throw new IOException("В римской системе счиления нет отрицательных чисел");
        }
        if (ArabicInt == 0) {
            throw new IOException("В римской системе счиления нет нуля");
        }
    }
}

class InputValidation {
    public static void validateInputSymbols (String input) throws IOException {
        Pattern patternSymbols = Pattern.compile("^[0-9ivxIVX+*/\\- ]+$");
        Matcher symbols = patternSymbols.matcher(input);
        if (!symbols.matches()) throw new IOException("Ошибка, введены некорректрные символы");
    }
    public static void validateOnlyOneSystem (String input) throws IOException {
        if (!checkOnlyArabicNumber(input) && !checkOnlyRomanNumber(input)) throw new IOException("Ошибка, можно использовать " +
                "одновременно только" +
                " арабские или только римские цифры");
    }
    public static boolean checkOnlyArabicNumber (String input) {
        Pattern patternArabic = Pattern.compile("^[0-9+*/\\- ]+$");
        Matcher symbolsArabic = patternArabic.matcher(input);
        return symbolsArabic.matches();
    }
    public static boolean checkOnlyRomanNumber (String input) {
        Pattern patternRoman = Pattern.compile("^[ivxIVX+*/\\- ]+$");
        Matcher symbolsArabic = patternRoman.matcher(input);
        return symbolsArabic.matches();
    }
    public static void validateQuantityOperands (String input) throws IOException {
        String stringWithoutSpace = input.trim().replaceAll("[+\\-/*]", " ").replaceAll(" +", " ");
        String[] operandsWithoutSpace = stringWithoutSpace.split(" ");
        if (Objects.equals(operandsWithoutSpace[0], "")) throw new IOException("Ошибка, неверный формат примера. Не математическая операция");
        if (operandsWithoutSpace.length != 2) throw new IOException("Ошибка, неверный формат примера. " +
                "Введенное количество операндов - " + operandsWithoutSpace.length + ". Правильное количество операндов - 2");
    }
    public static void validateQuantityOperators (String input, Pattern patternOperators) throws IOException {
        Matcher countOperandsMather = patternOperators.matcher(input);
        int countOperators = 0;
        while (countOperandsMather.find()) {
            countOperators++;
        }
        if (countOperators != 1) throw new IOException("Ошибка, неверный формат примера. " +
                "Введенное количество операторов - " + countOperators + ". Правильное количество операторов - 1");
    }
    public static void validateOperatorsAndOperands (String input) throws IOException {
        Pattern patternOperators = Pattern.compile("[+\\-*/]");
        validateQuantityOperators(input, patternOperators);
        validateQuantityOperands(input);
    }
    public static void validateInput (String input) throws IOException {
        validateOnlyOneSystem(input);
        validateInputSymbols(input);
        validateOperatorsAndOperands(input);
    }
}

class Calculator {
    public static int calculate (int a, int b, String operators) {
        int result = 0;
        switch (operators) {
            case "+" -> result = a + b;
            case "-" -> result = a - b;
            case "/" -> result = a / b;
            case "*" -> result = a * b;
        }
        return result;
    }

    public static String getOperator (String input) throws IOException {
        Pattern patternOperator = Pattern.compile("[+\\-*/]");
        Matcher matcherOperator = patternOperator.matcher(input);
        if (matcherOperator.find()) {
            return matcherOperator.group();
        }
        throw new IOException("Ошибка, неверный формат примера. Операторы не найдены");
    }
}

class ConvertOperators {
    public static int romanToInt(String romanOperator) {
        int ans = 0, num = 0;
        for (int i = romanOperator.length()-1; i >= 0; i--) {
            switch (romanOperator.charAt(i)) {
                case 'I', 'i' -> num = 1;
                case 'V', 'v' -> num = 5;
                case 'X', 'x' -> num = 10;
                case 'L', 'l' -> num = 50;
                case 'C', 'c' -> num = 100;
                case 'D', 'd' -> num = 500;
                case 'M', 'm' -> num = 1000;
            }
            if (4 * num < ans) ans -= num;
            else ans += num;
        }
        return ans;
    }

    public static String intToRoman(int num) {
        String[] romans = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] value = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        int seqSize = romans.length;
        int idx = seqSize - 1;
        String ans = "";
        while(num>0){
            while(value[idx]<=num){
                ans += romans[idx];
                num -= value[idx];
            }
            idx--;
        }
        return ans;
    }
}