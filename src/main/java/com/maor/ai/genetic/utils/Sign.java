package com.maor.ai.genetic.utils;

public enum Sign {
    Plus,
    Minus,
    Multiply,
    Divide;

    public static Sign IntToSign(int value) {
        Sign sign;

        switch (value) {
            case 0:
                sign = Sign.Plus;
                break;
            case 1:
                sign = Sign.Minus;
                break;
            case 2:
                sign = Sign.Multiply;
                break;
            case 3:
                sign = Sign.Divide;
                break;
            default:
                sign = Sign.Divide;
        }

        return sign;
    }

    public static double Calculate(double n1, double n2, Sign sign) {
        double result;

        switch (sign) {
            case Plus:
                result = n1 + n2;
                break;
            case Minus:
                result = n1 - n2;
                break;
            case Multiply:
                result = n1 * n2;
                break;
            case Divide:
                result = n1 / n2;
                break;
            default:
                result = 0;
        }

        return result;
    }

    public static String toString(Sign value) {
        String res;

        switch (value) {
            case Plus:
                res = "+";
                break;
            case Minus:
                res = "-";
                break;
            case Multiply:
                res = "*";
                break;
            case Divide:
                res = "/";
                break;
            default:
                res = "No such sign";
        }

        return res;
    }
}