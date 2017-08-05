package com.maor.ai.genetic;

import com.maor.ai.genetic.utils.Sign;
import com.maor.ai.genetic.utils.SignCOPos;

import java.util.ArrayList;
import java.util.Random;

public class GeneticElement implements Comparable<GeneticElement> {

    private ArrayList<Sign> signs = null;
    private ArrayList<Integer> numbers = null;
    private Double fitnessValue = null;
    private Double outputValue = null;

    public static Double TargetValue = null;
    public static Double AccuracyFactor = null;
    public static Integer Size = null;

    public GeneticElement() {
        generateRandomly();
        calculateOutputValue();
        calculateFitnessValue();
    }

    public GeneticElement(GeneticElement ge){
        this.fitnessValue = ge.getFitnessValue();
        this.outputValue = ge.getOutputValue();
        copySigns(ge.getSigns());
        copyNumbers(ge.getNumbers());
    }

    private void copySigns(ArrayList<Sign> signs){
        this.signs = new ArrayList<Sign>(GeneticElement.Size);

        for(int i = 0; i < GeneticElement.Size; i++){
            this.signs.add(i, signs.get(i));
        }
    }

    private void copyNumbers(ArrayList<Integer> numbers){
        this.numbers = new ArrayList<Integer>(GeneticElement.Size);

        for(int i = 0; i < GeneticElement.Size; i++){
            this.numbers.add(i, numbers.get(i));
        }
    }

    private void generateRandomly() {
        Random rand = new Random();
        signs = new ArrayList<Sign>(GeneticElement.Size);
        numbers = new ArrayList<Integer>(GeneticElement.Size);
        signs.add(0, Sign.IntToSign(rand.nextInt(2)));
        numbers.add(0, rand.nextInt(10));

        Sign signToAdd;
        int numToAdd;

        for (int i = 1; i < GeneticElement.Size; ++i) {
            signToAdd = Sign.IntToSign(rand.nextInt(4));

            do {
                numToAdd = rand.nextInt(10);
            } while (numToAdd == 0);

            while (signToAdd == Sign.Divide && numToAdd == 0) {
                numToAdd = rand.nextInt(10);
            }

            signs.add(i, signToAdd);
            numbers.add(i, numToAdd);
        }
    }

    public boolean isElementValid() {
        boolean isValid = true;
        Sign firstSign = signs.get(0);

        if (firstSign == Sign.Divide || firstSign == Sign.Multiply) {
            isValid = false;
        }

        for (int i = 0; (i < GeneticElement.Size) && isValid; ++i) {
            if ((signs.get(i) == Sign.Divide) && (numbers.get(i) == 0)) {
                isValid = false;
            }
        }

        return isValid;
    }

    private void calculateOutputValue() {
        Sign tempSign = signs.get(0);
        double tempNum = numbers.get(0);
        double res = (tempSign == Sign.Plus) ? tempNum : tempNum * (-1);

        for (int i = 1; i < GeneticElement.Size; ++i) {
            tempSign = signs.get(i);
            tempNum = numbers.get(i);
            res = Sign.Calculate(res, tempNum, tempSign);
        }

        this.outputValue = res;
    }

    private void calculateFitnessValue() {
        double dist = Math.abs(this.outputValue - GeneticElement.TargetValue);
        this.fitnessValue = (dist > GeneticElement.AccuracyFactor) ? (1 / dist) : (1 / GeneticElement.AccuracyFactor);
    }

    public void mutate() {
        Random rand = new Random();
        int index = rand.nextInt(GeneticElement.Size);
        int tempNum;
        Sign tempSign;

        do {
            tempSign = (index != 0) ? Sign.IntToSign(rand.nextInt(4)) : Sign.IntToSign(rand.nextInt(2));

            do {
                tempNum = rand.nextInt(10);
            } while (tempNum == 0);

            numbers.remove(index);
            signs.remove(index);

            numbers.add(index, tempNum);
            signs.add(index, tempSign);

        } while (tempSign == Sign.Divide && tempNum == 0);

        calculateOutputValue();
        calculateFitnessValue();
    }

    public static void CrossOver(GeneticElement e1, GeneticElement e2) {
        Random rand = new Random();
        int coIndex;

        do {
            coIndex = rand.nextInt(GeneticElement.Size - 1);
        } while (coIndex == 0);

        SignCOPos pos = SignCOPos.IntToPos(rand.nextInt(2));

        if (pos == SignCOPos.Beginning) {
            SwitchNumbers(e1.numbers, e2.numbers, 0, coIndex - 1);
            SwitchSigns(e1.signs, e2.signs, 0, coIndex - 1);
        } else {
            SwitchNumbers(e1.numbers, e2.numbers, coIndex, GeneticElement.Size - 1);
            SwitchSigns(e1.signs, e2.signs, coIndex, GeneticElement.Size - 1);
        }

        e1.calculateOutputValue();
        e1.calculateFitnessValue();

        e2.calculateOutputValue();
        e2.calculateFitnessValue();
    }

    private static void SwitchNumbers(ArrayList<Integer> n1, ArrayList<Integer> n2, int startInd, int endInd) {
        Integer tempInt = null;

        for (int i = startInd; i <= endInd; ++i) {
            tempInt = n1.get(i);
            n1.set(i, n2.get(i));
            n2.set(i, tempInt);
        }
    }

    private static void SwitchSigns(ArrayList<Sign> s1, ArrayList<Sign> s2, int startInd, int endInd) {
        Sign tempSign = null;

        for (int i = startInd; i <= endInd; ++i) {
            tempSign = s1.get(i);
            s1.set(i, s2.get(i));
            s2.set(i, tempSign);
        }
    }

    /**
     * Get the value of outputValue
     *
     * @return the value of outputValue
     */
    public Double getOutputValue() {
        return outputValue;
    }

    /**
     * Get the value of fitnessValue
     *
     * @return the value of fitnessValue
     */
    public Double getFitnessValue() {
        return fitnessValue;
    }

    /**
     * Get the value of numbers
     *
     * @return the value of numbers
     */
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    /**
     * Set the value of numbers
     *
     * @param numbers new value of numbers
     */
    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    /**
     * Get the value of signs
     *
     * @return the value of signs
     */
    public ArrayList<Sign> getSigns() {
        return signs;
    }

    /**
     * Set the value of signs
     *
     * @param signs new value of signs
     */
    public void setSigns(ArrayList<Sign> signs) {
        this.signs = signs;
    }

    public int compareTo(GeneticElement o) {
        int res = 0;

        if (o != null && o.fitnessValue != null && this.fitnessValue != null) {
            res = this.fitnessValue.compareTo(o.fitnessValue);
        } else {
            throw new Error("Genetic Element wasn't intialized properly.\n");
        }

        return res;
    }
}
