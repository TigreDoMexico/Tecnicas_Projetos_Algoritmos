package BruteForce;

import java.text.MessageFormat;
import java.util.*;
import Contracts.IExecutavel;
import Conversion.Conversion;
import TimeCalculation.*;

public class Exponenciacao implements IExecutavel {

    private final int[] bases = { 1, 2, 7, 10, 12, 10 };
    private final int[] expoentes = { 1, 3, 5, 10, 10, 12 };

    private List<CalculationResult> results = new ArrayList<CalculationResult>();
    private TimeCalculation timeCalculation = new TimeCalculation();

    @Override
    public void Executar() {
        System.out.println("EXECUTANDO EXPONENCIACAO (FORCA BRUTA)");

        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            CalculationResult resultado = timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return CalcularExpoente(bases[index], expoentes[index]);
            }, params);

            results.add(resultado);
        }

        System.out.println("EXECUÇÃO EXPONENCIAÇÃO FINALIZADA (FORCA BRUTA)");
    }

    @Override
    public void MostrarResultados() {
        int i = 0;
        for(CalculationResult result : results) {
            long resultadoOperacao = Conversion.toLong(result.getResult());
            double duracaoEmMs = result.getTimeDuration();

            System.out.println("Duração: " + duracaoEmMs);
            System.out.println(MessageFormat.format("{0} ^ {1} = {2}", bases[i], expoentes[i], resultadoOperacao));

            i++;
        }
    }

    private long CalcularExpoente(int base, int expoente) {
        long resultado = 1;
        for (int i = 0; i < expoente; i++) {
            resultado *= base;
        }
        return resultado;
    }
}
