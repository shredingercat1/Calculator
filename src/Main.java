import java.util.Scanner;

class Calculator {

    enum Roman {
        I(1), IV(4), V(5), IX(9), X(10), L(50), C(100);
        final int value;

        Roman(int value) {
            this.value = value;
        }

        int toInt() {
            return value;
        }
    }

    public static int romanToArabic(String roman) {
        int arabic = 0;
        int prevValue = 0;
        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = 0;
            char c = roman.charAt(i);
            switch (c) {
                case 'I':
                    currentValue = 1;
                    break;
                case 'V':
                    currentValue = 5;
                    break;
                case 'X':
                    currentValue = 10;
                    break;
                case 'L':
                    currentValue = 50;
                    break;
                case 'C':
                    currentValue = 100;
                    break;
                case 'D':
                    currentValue = 500;
                    break;
                case 'M':
                    currentValue = 1000;
                    break;
            }
            if (currentValue < prevValue) {
                arabic -= currentValue;
            } else {
                arabic += currentValue;
            }
            prevValue = currentValue;
        }

        return arabic;
    }

    public static String arabicToRoman(int arabic) {
        StringBuilder result = new StringBuilder();
        Roman[] values = Roman.values();
        for (int i = values.length - 1; i >= 0; i--) {
            Roman roman = values[i];
            while (arabic >= roman.toInt()) {
                result.append(roman.name());
                arabic -= roman.toInt();
            }
        }
        return result.toString();
    }


    public static String calc(String input) {
        String[] numbers = input.split("[-+*/]");
        String[] operators = input.split("(?:IIII|VIII|III?|V(?:II?)?|10|I[VX]|[1I]|[2-9X])", 0);
        if (numbers.length == 0 || operators.length == 0) {
            throw new IllegalArgumentException("т.к. строка не является математической операцией");
        }
        int numOperators = operators.length - 1;
        if (numOperators > 1) {
            throw new IllegalArgumentException("т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }


        validateInputConsistency(numbers);

        int result = 0;
        boolean isRoman = false;
        for (int i = 0; i < numbers.length; i++) {
            String numStr = numbers[i];
            String operation = i > 0 ? operators[i] : "";

            isRoman = numStr.matches("[IVXLCDM]+");
            int num = isRoman ? romanToArabic(numStr) : Integer.parseInt(numStr);

            validateNumber(num);

            switch (operation) {
                case "":
                    result = num;
                    break;
                case "+":
                    result += num;
                    break;
                case "-":
                    result -= num;
                    break;
                case "*":
                    result *= num;
                    break;
                case "/":
                    result /= num;
                    break;
                default:
                    throw new IllegalArgumentException("Неверная операция");
            }
        }
        if (isRoman && result < 0) {
            throw new IllegalArgumentException("т.к. в римской системе нет отрицательных чисел");}
        else return isRoman ? arabicToRoman(result) : Integer.toString(result);

    }

    public static void validateNumber(int num) {
        if (num < 1 || num > 10) {
            throw new IllegalArgumentException("т.к введённые числа должны быть в диапазоне от 1 до 10");
        }
    }

    public static void validateInputConsistency(String[] numbers) {
        boolean hasArabic = false;
        boolean hasRoman = false;

        for (String numStr : numbers) {
            if (numStr.matches("\\d+")) {
                hasArabic = true;
            } else if (numStr.matches("[IVXLCDM]+")) {
                hasRoman = true;
            }

            if (hasArabic && hasRoman) {
                throw new IllegalArgumentException("т.к. используются одновременно разные системы счисления");
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите арифметическое выражение: ");
        String input = scanner.nextLine();

        try {
            String result = calc(input);
            System.out.println("Результат: " + result);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());

        }

    }
}