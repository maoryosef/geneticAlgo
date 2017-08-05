package com.maor.ai.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticPopulation {

    private ArrayList<GeneticElement> population = null;
    private Double mutationFactor = null;
    private Double accuracyFactor = null;
    private Integer populationSum = null;
    private Integer generationsAmount = null;
    private Integer populationSize = null;

    public GeneticPopulation(int elementSize, double targetValue, double accuracyFactor,
                             double mutationFactor, int populationSize, int generationsAmount) {
        GeneticElement.Size = elementSize;
        GeneticElement.TargetValue = targetValue;
        GeneticElement.AccuracyFactor = accuracyFactor;
        this.accuracyFactor = accuracyFactor;
        this.mutationFactor = mutationFactor;
        this.generationsAmount = generationsAmount;
        this.populationSize = populationSize;
        this.population = new ArrayList<GeneticElement>(populationSize);
        generateInitialPopulation();
        calcPopulationSum();
    }

    private void generateInitialPopulation() {
        for (int i = 0; i < this.populationSize; ++i) {
            population.add(i, new GeneticElement());
        }
    }

    private void calcPopulationSum() {
        int size = this.populationSize;
        int sum = 0;

        for (int i = 0; i < size; i++) {
            sum += (i + 1);
        }

        this.populationSum = sum;
    }

    private GeneticElement rankSelection() {
        GeneticElement selected = null;
        Random rand = new Random();
        int selectionNum = rand.nextInt(this.populationSum);
        int tempSum = 0;

        for (int i = 0; i < this.populationSize; ++i) {
            tempSum += (i + 1);

            if (tempSum > selectionNum) {
                selected = population.get(i);
                break;
            }
        }

        return selected;
    }

    private void evolveSingleGeneration() {
        int size = this.populationSize;
        ArrayList<GeneticElement> nextGeneration = new ArrayList<GeneticElement>(size);

        //Elitism Principle
        if (size % 2 != 0) {
            nextGeneration.add(new GeneticElement(this.population.get(size - 1)));
        } else {
            nextGeneration.add(new GeneticElement(this.population.get(size - 1)));
            nextGeneration.add(new GeneticElement(this.population.get(size - 2)));
        }

        //Cross Over
        int coAmount = (size % 2 == 0) ? ((size - 2) / 2) : size / 2; //size / 2;
        for (int i = 0; i < coAmount; ++i) {
            GeneticElement e1 = new GeneticElement(rankSelection());
            GeneticElement e2 = new GeneticElement(rankSelection());
            GeneticElement.CrossOver(e1, e2);
            nextGeneration.add(e1);
            nextGeneration.add(e2);
        }

        Random rand = new Random();

        //Mutation and Validation
        for (GeneticElement e : nextGeneration) {
            double mutationChance = rand.nextDouble();

            if (mutationChance < this.mutationFactor) {
                e.mutate();
            }

            if (!e.isElementValid()) {
                throw new Error("Something went wrong, value of element:\n" + e.getNumbers().toString() + "\n" + e.getSigns().toString() + "\n");
            }
        }

        this.population = nextGeneration;
    }

    public GeneticElement evolve() {
        Collections.sort(this.population);
        GeneticElement bestElement = this.population.get(this.populationSize - 1);
        int generationsCounter = this.generationsAmount;

        while ((generationsCounter > 0) && (bestElement.getFitnessValue() < (1 / this.accuracyFactor))) {
            evolveSingleGeneration();
            Collections.sort(this.population);
            bestElement = this.population.get(this.populationSize - 1);
            generationsCounter--;
        }
        return bestElement;
    }

    /**
     * Get the value of population
     *
     * @return the value of population
     */
    public ArrayList<GeneticElement> getPopulation() {
        return population;
    }

    /**
     * Set the value of population
     *
     * @param population new value of population
     */
    public void setPopulation(ArrayList<GeneticElement> population) {
        this.population = population;
    }

    /**
     * Get the value of mutationFactor
     *
     * @return the value of mutationFactor
     */
    public Double getMutationFactor() {
        return mutationFactor;
    }

    /**
     * Set the value of mutationFactor
     *
     * @param mutationFactor new value of mutationFactor
     */
    public void setMutationFactor(Double mutationFactor) {
        this.mutationFactor = mutationFactor;
    }
}
