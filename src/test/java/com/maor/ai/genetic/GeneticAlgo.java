package com.maor.ai.genetic;

import com.maor.ai.genetic.utils.Sign;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GeneticAlgo {
    @Test
    public void testLengthOfTheUniqueKey() {
        int elementSize = 60;
        int populationSize = 1000;
        double targetVal =2.718281828459045;
        double accuFactor = 0.000000000001;
        double mutationFactor = 0.3;
        int generationAmount = 20000;

        GeneticPopulation population = new GeneticPopulation(elementSize, targetVal, accuFactor, mutationFactor, populationSize, generationAmount);
        GeneticElement result = population.evolve();
        ArrayList<Sign> signsRes = result.getSigns();
        ArrayList<Integer> numRes = result.getNumbers();

        StringBuilder sb = new StringBuilder("Best element output = " + result.getOutputValue() + "\n");
        sb.append("Best element string: ");

        for(int i = 0; i < elementSize; ++i){
            sb.append(Sign.toString(signsRes.get(i)) + " " + numRes.get(i) + " ");
        }

        sb.append("\n");

        System.out.println(sb.toString());

        Assert.assertTrue(Math.abs(targetVal - result.getOutputValue()) < accuFactor);
        Assert.assertEquals(targetVal, result.getOutputValue(), accuFactor);
    }
}