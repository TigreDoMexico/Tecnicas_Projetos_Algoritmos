package SubArrayProblem;

import java.text.MessageFormat;
import java.util.*;
import Contracts.IExecutable;
import Conversion.Conversion;
import TimeCalculation.*;

public class MaximumSubarray implements IExecutable {

    private final int[][] subarrays = {
            { 1, 2, 3, 4, 5, 6 },
            { -2, -5, 6, -2, -3, 1, 5, -6 },
            { -2, -5, 6, -2, -3, 1, 5, -6, 10, -2, -1, 4 },
            { -2, -5, 6, -2, -3, 1, 5, -6, 10, -2, -1, 4, 2, -3, 5, 1, 0, -6, 8, -4, 2, -5, 10, 3, 1, -10, 6, 4, -7, 8 },
    };

    private List<CalculationResult> resultsDefault = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDivideConquer = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDynamicProg = new ArrayList<CalculationResult>();

    private TimeCalculation timeCalculation = new TimeCalculation();

    @Override
    public void ExecuteAllMethods() {
        ExecuteBruteForce();
        ExecuteDivisaoEConquista();
        ExecuteProgramacaoDinamica();
    }

    @Override
    public void ShowResults() {
        System.out.println("\n\nRESULTADOS FORÇA BRUTA\n");
        PrintResults(resultsDefault);

        System.out.println("\n\nRESULTADOS DIVISÃO E CONQUISTA\n");
        PrintResults(resultsDivideConquer);

        System.out.println("\n\nRESULTADOS PROGRAMAÇÃO DINÂMICA\n");
        PrintResults(resultsDynamicProg);
    }

    private void PrintResults(List<CalculationResult> results) {
        int i = 0;
        for (CalculationResult result : resultsDefault) {
            long resultadoOperacao = Conversion.toLong(result.getResult());
            double duracaoEmMs = result.getTimeDuration();

            System.out.println(MessageFormat.format("Duração: {0}", duracaoEmMs));
            System.out.println(
                    MessageFormat.format("SUBARRAY {0} => {1} ({2} ELEMENTOS)", Arrays.toString(subarrays[i]), resultadoOperacao, subarrays[i].length));

            i++;
        }
    }

    private void ExecuteBruteForce() {
        for (int i = 0; i < subarrays.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return BruteForce(subarrays[index]);
            }, params);

            resultsDefault.add(resultado);
        }
    }

    private int BruteForce(int[] numeros) {
        if (numeros.length == 0) {
            return 0;
        }

        int somaMaximo = numeros[0];
        int i, j;

        for (i = 0; i < numeros.length; i++) {
            for (j = 0; j < numeros.length; j++) {
                int somaCorrente = 0;
                int k;

                for (k = i; k <= j; k++) {
                    somaCorrente += numeros[k];
                }

                somaMaximo = retornaMaior(somaCorrente, somaMaximo);
            }
        }

        return somaMaximo;
    }

    private void ExecuteDivisaoEConquista() {
        for (int i = 0; i < subarrays.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DivideConquer(subarrays[index], 0, subarrays[index].length - 1);
            }, params);

            resultsDivideConquer.add(resultado);
        }
    }

    private int DivideConquer(int[] numeros, int idxMinimo, int idxMaximo) {
        if (idxMaximo <= idxMinimo) {
            return numeros[idxMinimo];
        }

        int idx_meio = (idxMaximo + idxMinimo) / 2;

        int maior_soma_esquerda = Integer.MIN_VALUE;
        int soma = 0, i;

        for (i = idx_meio; i >= idxMinimo; i--) {
            soma += numeros[i];
            if (soma > maior_soma_esquerda) {
                maior_soma_esquerda = soma;
            }
        }

        int maior_soma_direita = Integer.MIN_VALUE;
        soma = 0;
        for (i = idx_meio + 1; i <= idxMaximo; i++) {
            soma += numeros[i];
            if (soma > maior_soma_direita) {
                maior_soma_direita = soma;
            }
        }

        int maior_soma_divisao_array = retornaMaior(
                DivideConquer(numeros, idxMinimo, idx_meio),
                DivideConquer(numeros, idx_meio + 1, idxMaximo));

        return retornaMaior(maior_soma_divisao_array,
                maior_soma_esquerda + maior_soma_direita);
    }

    private void ExecuteProgramacaoDinamica() {
        for (int i = 0; i < subarrays.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DynamicProgramming(subarrays[index]);
            }, params);

            resultsDynamicProg.add(resultado);
        }
    }

    private int DynamicProgramming(int[] numeros) {
        int somaMaximo = numeros[0];
        int somaCorrente = numeros[0];

        for (int i = 1; i < numeros.length; i++) {
            somaCorrente = retornaMaior(somaCorrente + numeros[i], numeros[i]);
            somaMaximo = retornaMaior(somaMaximo, somaCorrente);
        }

        return somaMaximo;
    }

    private int retornaMaior(int a, int b) {
        return a > b ? a : b;
    }
}
