package Generators;

import java.util.Random;
import java.util.stream.IntStream;

public class ArrayGenerator {
    public static int[] GerarArrayRandom(int tamanho) {
        return IntStream.generate(() -> new Random().nextInt(1000)).limit(tamanho).toArray();
    }
}
