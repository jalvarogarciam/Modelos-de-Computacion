package bt1;
/*
Páctca de Generadores de Números Pseudoaleatorios de la asignatura de Modelos de Computación.
Implementación de algunos de los generadores descritos en https://www.cse.wustl.edu/~jain/iucee/ftp/k_26rng.pdf .


 */

public class randomGenerator {
    
    /**
     * Generador lineal congruencial, con parametros a, m y semilla 
     * donde t(n) = (a * t(n-1)) mod m. El resultado no estará normalizado.
     * @param a multiplicador
     * @param m modulo
     * @param semilla semilla inicial
     * @param n último término a generar, el vector de numeros pseudoaleatorios 
     * generado tendrá tamaño n+1, incluyendo la semilla.
     * @return vector de numeros pseudoaleatorios generados por el método lineal congruencial, normalizados a [0,1).
     */
    private double [] generator(long a, long m,long semilla, int n) {
        double [] numeros = new double[n];

        for (int i = 0; i < n; i++) {

            long mult = i==0 ? a * semilla : a * (long) numeros[i-1]; // para evitar overflow, 
            numeros[i] = (double) (mult % m);

        }

        return numeros;
    }

    /**
     * Normaliza el vector de numeros pseudoaleatorios dividiendo cada término por m, 
     * para obtener valores en el rango [0,1).
     * @param numeros vector de numeros pseudoaleatorios a normalizar, sin normalizar.
     * @param m modulo utilizado en el generador lineal congruencial, para dividir cada término y normalizar a [0,1).
     * @return vector de numeros pseudoaleatorios normalizados a [0,1).
     */
    private double [] normalize(double[] numeros, long m) {
        double [] normalized = new double[numeros.length];
        for (int i = 0; i < numeros.length; i++) {
            normalized[i] = numeros[i] / m;
        }
        return normalized;
    }


    public double[] generador26_1a(long semilla,  int n) {
        // t(n) = 5*t(n-1) mod 2^5
        long a = 5, m = 32;
        return normalize(generator(a, m, semilla, n), m);
    }

    public double[] generador26_1b(long semilla,  int n) {
        // t(n) = 7*t(n-1) mod 2^5
        long a = 7, m = 32;
        return normalize(generator(a, m, semilla, n), m);
    }

    public double[] generador26_2(long semilla,  int n) {
        // t(n) = 3*t(n-1) mod 31
        long a = 3, m = 31;
        return normalize(generator(a, m, semilla, n), m);
    }

    public double[] generador26_3(long semilla,  int n) {
        // t(n) = (7^5)*t(n-1) mod (2^31 - 1)
        long a = 16807L, m = 2147483647L;
        return normalize(generator( a,m, semilla, n), m);
    }



    /**
     * Generador combinado de la diapositiva 26-42;
     * @param semillas vector de 3 semillas, cada una para un generador distinto.
     * @param n tamaño del vector de numeros pseudoaleatorios a generar.
     * @return vector de numeros pseudoaleatorios generados por el metodo combinado, utilizando los 3 generadores anteriores.
     */
    public double[] generadorCombinado(long[] semillas,  int n) {

        // vector de numeros pseudoaleatorios a generar.
        double [] numeros = new double[n+1];
        // vectores para cada uno de los 3 subgeneradores.
        double [] x = generator(146L,31727L, semillas[0], n);
        double [] y = generator(142L,31657L, semillas[1], n);
        double [] w = generator(157L,32363L, semillas[2], n);

        long m = 32362;

        // combinacion de los 3 subgeneradores.
        for (int i = 1; i <= n; i++) {

            numeros[i] = (w[i]-x[i]+y[i]) % m ;

            // para asegurar que el resultado sea positivo.
            if (numeros[i] <= 0) numeros[i] += m; 
            
        }

        return normalize(numeros, m);
    }

    public double[] generadorCombinado(long semilla,  int n) {

        // vector de numeros pseudoaleatorios a generar.
        double [] numeros = new double[n+1];
        // vectores para cada uno de los 3 subgeneradores.
        double [] x = generator(146L,31727L, semilla, n);
        double [] y = generator(142L,31657L, semilla, n);
        double [] w = generator(157L,32363L, semilla, n);

        long m = 32362;

        // combinacion de los 3 subgeneradores.
        for (int i = 0; i <= n; i++) {

            numeros[i] = (w[i]-x[i]+y[i]) % m ;

            // para asegurar que el resultado sea positivo.
            if (numeros[i] <= 0) numeros[i] += m; 
            
        }

        return normalize(numeros, m);
    }

    public double[] generadorFishmanMoore(long semilla, int n) {
        // t(n) = 48271*t(n-1) mod (2^31 - 1)
        long a = 48271L, m = 2147483647L;
        return normalize(generator(a, m, semilla, n), m);
    }

    public double[] generadorRANDU(long semilla, int n) {
        // t(n) = 65539*t(n-1) mod (2^31)
        long a = 65539L, m = 2147483648L;
        return normalize(generator(a, m, semilla, n), m);
    }


    
    public static void main(String[] args) {
        
        randomGenerator generador = new randomGenerator();

        double [] valores = generador.generadorFishmanMoore(5, 4);
        System.out.println("Valores generados: ");
        for (double val : valores) {
            System.out.println(val);
        }
    }
        
}