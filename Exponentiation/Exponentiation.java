package Exponentiation;

import java.text.MessageFormat;
import java.util.*;
import Contracts.IExecutable;
import Conversion.Conversion;
import TimeCalculation.*;

public class Exponentiation implements IExecutable {

    private final int[] bases = { 1, 2, 7, 10, 12, 10 };
    private final int[] expoentes = { 1, 3, 5, 10, 10, 12 };

    private List<CalculationResult> resultsDefault = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDivideConquer = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDynamicProg = new ArrayList<CalculationResult>();

    private TimeCalculation timeCalculation = new TimeCalculation();

    @Override
    public void ExecuteAllMethods() {
        ExecuteBruteForce();
        ExecuteDivideConquer();
        ExecuteDynamicProgramming();
    }

    @Override
    public void ShowResults() {
        System.out.println("\n\nRESULTADOS DEFAULT\n");
        PrintResults(resultsDefault);

        System.out.println("\n\nRESULTADOS DIVISÃO E CONQUISTA\n");
        PrintResults(resultsDivideConquer);

        System.out.println("\n\nRESULTADOS PROGRAMAÇÃO DINÂMICA\n");
        PrintResults(resultsDivideConquer);
    }

    private void PrintResults(List<CalculationResult> results) {
        int i = 0;
        for(CalculationResult result : resultsDefault) {
            long resultadoOperacao = Conversion.toLong(result.getResult());
            double duracaoEmMs = result.getTimeDuration();

            System.out.println("Duração: " + duracaoEmMs);
            System.out.println(MessageFormat.format("{0} ^ {1} = {2}", bases[i], expoentes[i], resultadoOperacao));

            i++;
        }
    }

    private void ExecuteBruteForce() {
        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DefaultExecution(bases[index], expoentes[index]);
            }, params);

            resultsDefault.add(resultado);
        }
    }

    private long DefaultExecution(int base, int expoente) {
        long resultado = 1;
        for (int i = 0; i < expoente; i++) {
            resultado *= base;
        }
        return resultado;
    }

    private void ExecuteDivideConquer() {
        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DivideConquer(bases[index], expoentes[index]);
            }, params);

            resultsDivideConquer.add(resultado);
        }
    }

    private long DivideConquer(int base, int expoente) {
        if (expoente == 0) {
            return 1;
        } else if (expoente % 2 == 0) {
            long resultado = DivideConquer(base, expoente / 2);
            return resultado * resultado;
        } else {
            long resultado = DivideConquer(base, (expoente - 1) / 2);
            return base * resultado * resultado;
        }
    }

    private void ExecuteDynamicProgramming() {
        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DynamicProgramming(bases[index], expoentes[index]);
            }, params);

            resultsDynamicProg.add(resultado);
        }
    }

    private long DynamicProgramming(int base, int expoente) {
        long[] resultados = new long[expoente + 1];
        resultados[0] = 1;

        for (int i = 1; i <= expoente; i++) {
            resultados[i] = resultados[i / 2] * resultados[i / 2];
            if (i % 2 == 1) {
                resultados[i] *= base;
            }
        }

        return resultados[expoente];
    }
}