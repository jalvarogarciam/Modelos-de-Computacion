
/*
Páctca de Generadores de Números Pseudoaleatorios de la asignatura de Modelos de Computación.
Implementación de algunos de los generadores descritos en https://www.cse.wustl.edu/~jain/iucee/ftp/k_26rng.pdf .


 */

public class pseudoAleatorios {

    public int[] generador26_1a(int semilla,  int tam) {
        int [] numeros = new int[tam];

        numeros[0] = semilla;

        for (int i = 1; i < tam; i++) {
            numeros[i] = ( 5* numeros[i-1]) % 32;
        }
        return numeros;
    }

    public int[] generador26_1b(int semilla,  int tam) {
        int [] numeros = new int[tam];

        numeros[0] = semilla;

        for (int i = 1; i < tam; i++) {
            numeros[i] = ( 7 * numeros[i-1]) % 32;
        }
        return numeros;
    }

    public int[] generador26_2(int semilla,  int tam) {
        int [] numeros = new int[tam];

        numeros[0] = semilla;

        for (int i = 1; i < tam; i++) {
            numeros[i] = ( 3 * numeros[i-1]) % 31;
        }
        return numeros;
    }

    public int[] generador26_3(int semilla,  int tam) {
        int [] numeros = new int[tam];

        numeros[0] = semilla;

        for (int i = 1; i < tam; i++) {
            numeros[i] = ( (int) Math.pow(7, 5) * numeros[i-1]) % (int)(Math.pow(2, 31)-1);
        }
        return numeros;
    }

    private int w(int semilla, int tam){
        int anterior = semilla;
        int actual = 0;

        for (int i = 1; i < tam; i++) {
            actual = ( 157 * anterior ) % 32363;
            anterior = actual;
        }
        return actual;
    }

    /**
     * Generador combinado de la diapositiva 26-42;
     * @param semilla vector de 3 semillas, cada una para un generador distinto.
     * @param tam tamaño del vector de numeros pseudoaleatorios a generar.
     * @return vector de numeros pseudoaleatorios generados por el metodo combinado, utilizando los 3 generadores anteriores.
     */
    public int[] generadorCombinado(int[] semilla,  int tam) {
        int [] numeros = new int[tam];

        numeros[0] = semilla[0];

        for (int i = 1; i < tam; i++) {
            numeros[i] = ( (int) Math.pow(7, 5) * numeros[i-1]) % (int)(Math.pow(2, 31)-1);
        }
        return numeros;
    }
    
    public static void main(String[] args) {
        
         pseudoAleatorios generador = new pseudoAleatorios();
        int semilla = 1;
        int tam = 10;

        int[] numerosA = generador.generador26_1a(semilla, tam);
        int[] numerosB = generador.generador26_1b(semilla, tam);
        int[] numerosC = generador.generador26_2(semilla, tam);
        int[] numerosD = generador.generador26_3(semilla, 10000);

        System.out.println('\n');

        System.out.println("Generador 26.1a:");
        for (int num : numerosA) {
            System.out.print(num + " ");
        }

        System.out.println('\n');
        System.out.println("\nGenerador 26.1b:");
        for (int num : numerosB) {
            System.out.print(num + " ");
        }
        
        System.out.println('\n');
        System.out.println("\nGenerador 26.2:");
        for (int num : numerosC) {
            System.out.print(num + " ");
        }
        
        System.out.println('\n');
        System.out.println("\nGenerador 26.3:");
        System.out.println("termino 10000 numeros, el ultimo es: " + numerosD[numerosD.length - 1]);
        System.out.println('\n');

    }
}