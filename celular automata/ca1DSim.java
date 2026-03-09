


import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import java.util.*;

public class ca1DSim extends JFrame {

    private JComboBox<String> comboCondicionesFrontera;
    private JComboBox<String> comboConfiguracionInicial;
    private JTextField txtk;
    private JTextField txtRegla; 
    private JTextField txtGeneraciones;
    private JTextField txtAncho;
    private AutomataCelularGrafico panelAutomata;

    // Nombres de los generadores para usarlos en varios sitios
    private final String[] nombresCondicionesFrontera = {"NULA", "CILINDRICA"};
    private final String[] nombresConfiguracionInicial = {"ALEATORIA", "CENTRAL"};

    public ca1DSim() {
        setTitle("Simulador de Autómatas Celulares 1D");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout());

        // --- 1. PANEL CENTRAL (GRÁFICO) ---
        panelAutomata = new AutomataCelularGrafico();
        JScrollPane scrollPanel = new JScrollPane(panelAutomata);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder()); // Opcional: quita bordes extra
        add(scrollPanel, BorderLayout.CENTER);

        // --- 2. PANEL DERECHO (CONTROLES Y LEYENDA) ---
        JPanel panelDerechoContenedor = new JPanel(new BorderLayout());
        panelDerechoContenedor.setBackground(Color.GRAY);
        panelDerechoContenedor.setPreferredSize(new Dimension(250, 0));

        // 2.1. Panel de Controles (Arriba)
        JPanel panelControles = new JPanel(new GridLayout(0, 1, 0, 10));
        panelControles.setOpaque(false);
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelControles.add(crearEtiqueta("Condición de Frontera:"));
        comboCondicionesFrontera = new JComboBox<>(nombresCondicionesFrontera);
        comboCondicionesFrontera.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        panelControles.add(comboCondicionesFrontera);

        panelControles.add(crearEtiqueta("Configuración Inicial:"));
        comboConfiguracionInicial = new JComboBox<>(nombresConfiguracionInicial);
        comboConfiguracionInicial.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        panelControles.add(comboConfiguracionInicial);
        

        panelControles.add(crearEtiqueta("REGLA:"));
        txtRegla = new JTextField("792"); 
        txtRegla.setHorizontalAlignment(JTextField.CENTER);
        txtRegla.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        panelControles.add(txtRegla);

        panelControles.add(crearEtiqueta("Nº ESTADOS (K):"));
        txtk = new JTextField("3"); 
        txtk.setHorizontalAlignment(JTextField.CENTER);
        txtk.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        panelControles.add(txtk);

        panelControles.add(crearEtiqueta("Nº CELULAS:"));
        txtAncho = new JTextField("500"); 
        txtAncho.setHorizontalAlignment(JTextField.CENTER);
        txtAncho.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        panelControles.add(txtAncho);
        
        panelControles.add(crearEtiqueta("Nº GENERACIONES:"));
        txtGeneraciones = new JTextField("500"); 
        txtGeneraciones.setHorizontalAlignment(JTextField.CENTER);
        txtGeneraciones.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        panelControles.add(txtGeneraciones);



        panelControles.add(crearBoton("Start"));
        panelControles.add(crearBoton("Limpiar"));

        panelDerechoContenedor.add(panelControles, BorderLayout.NORTH);
        add(panelDerechoContenedor, BorderLayout.EAST);
    }

    private void computarSimulacion() {
        try {
            int k = Integer.parseInt(txtk.getText());
            int regla = Integer.parseInt(txtRegla.getText());
            int gen = Integer.parseInt(txtGeneraciones.getText());
            int ancho = Integer.parseInt(txtAncho.getText()); // Leemos el ancho deseado

            String frontera = (String) comboCondicionesFrontera.getSelectedItem();
            String inicial = (String) comboConfiguracionInicial.getSelectedItem();

            panelAutomata.configurar(ancho, regla, k, frontera, inicial);
            panelAutomata.computarSimulacion(gen);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en los datos.");
        }
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        return l;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18)); 
        int r = (int)(Math.random()*256), g = (int)(Math.random()*256), b = (int)(Math.random()*256);
        boton.setBackground(new Color(r, g, b)); 
        boton.setForeground((0.299*r + 0.587*g + 0.114*b) < 128 ? Color.WHITE : Color.BLACK);
        boton.setFocusPainted(false);
        
        boton.addActionListener(e -> {
            if (texto.equals("Start")) computarSimulacion();
            else if (texto.equals("Limpiar")) { panelAutomata.reset();}
        });
        return boton;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ca1DSim().setVisible(true));
    }
}














class AutomataCelularGrafico extends JPanel {

    private BufferedImage imagen;
    private Color [] paleta;

    private String inicial;
    private String frontera;
    private int[] regla; //regla del autómata celular, en decimal, que se convertirá a base k para la función de transición
    private int k; //número de estados del autómata celular
    private int [] config; //configuración del autómata celular

    /**
     * @param index El índice de la célula cuyo estado se desea obtener.
     * @return El estado de la célula en el índice especificado.
     */
    public int getCell(int index) {return this.config[index];}

    /**
     * @return El número de estados k del autómata celular.
     */
    public int getK() {return this.k;}


    public AutomataCelularGrafico() {
        setBackground(Color.BLACK); 
    }

    /**
     * Constructor del autómata celular.
     * @param size El tamaño del autómata (número de células)
     * @param regla El número de la regla en decimal (ej. 792)
     * @param k El número de estados del autómata (ej. 3)
     * @param condicion_frontera La condición de frontera (NULA o CILINDRICA)
     * @param modoInicial Si es true, la configuración inicial es aleatoria; si es false, la célula central está a 1 y el resto a 0.
     */
    public AutomataCelularGrafico(int size, int regla, int k, String condicion_frontera, String modoInicial){
        configurar(size, regla, k, condicion_frontera, modoInicial);
        setBackground(Color.BLACK); 
    }

    /**
     * Configura el autómata celular con los parámetros especificados. 
     * Este método se puede llamar para reiniciar el autómata con una nueva configuración sin 
     * necesidad de crear una nueva instancia.
     * @param size El tamaño del autómata (número de células)
     * @param regla El número de la regla en decimal (ej. 792)
     * @param k El número de estados del autómata (ej. 3)
     * @param condicion_frontera La condición de frontera (NULA o CILINDRICA)
     * @param modoInicial Si es "ALEATORIA", la configuración inicial es aleatoria; 
     *                   Si es "CENTRAL", la célula central está a 1 y el resto a 0.
     */
    public void configurar(int size, int regla, int k, String condicion_frontera, String modoInicial) {
        this.regla = decodificarRegla(regla, k);
        this.k = k;
        this.frontera = condicion_frontera;
        this.inicial = modoInicial;
        this.config = new int[size];

        // CONFIGURACION INICIAL
        if (inicial.equals("ALEATORIA")) { // Configuración aleatoria
        
            for (int a = 0; a < config.length; a++) {
                config[a] = (int)(Math.random() * k); 
            }

        } else {  // Configuración con la célula central a 1 y el resto a 0

            for (int b = 0; b < config.length; b++) {

                config[b] = 0;
            }
            config[config.length / 2] = 1; // Centro activo
            
        }

        paleta = generarPaleta(k);
    }

    /**
     * Evoluciona el autómata celular una generación utilizando la regla definida y 
     * la condición de frontera especificada.
     */
    public void evolucionar() {

        int[] next_config = new int[config.length];

        for (int i = 0; i < config.length; i++) {

            int suma = config[i];
            if (frontera.equals("CILINDRICA")) { // Condición de frontera cilíndrica

                suma += config[(i - 1 + config.length) % config.length]; // izquierda
                suma += config[(i + 1) % config.length];                 // derecha

            } else { // Condición de frontera nula 

                if (i > 0) suma += config[i - 1];                         // izquierda
                if (i < config.length - 1) suma += config[i + 1];         // derecha

            }

            next_config[i] = regla[suma];
        }

        // Intercambiamos las configuraciones
        this.config = next_config;
    }
    

    /**
     * Inicia la simulación del autómata celular con los parámetros especificados.
     * @param generaciones El número de generaciones a simular.
     * @return Una matriz 2D donde cada fila representa una generación del autómata celular y 
     * cada columna representa el estado de una célula en esa generación.
     */
    public void computarSimulacion(int generaciones) {
    
        int ancho = config.length; 
        int alto = generaciones;
        
        // Definimos el tamaño preferido para que el ScrollPane lo detecte
        this.setPreferredSize(new Dimension(ancho, alto));
        this.revalidate();  // Avisamos al contenedor que el tamaño cambió

        imagen = new BufferedImage(ancho,alto, BufferedImage.TYPE_INT_RGB);
        Graphics g = imagen.getGraphics();

        // Bucle de generaciones
        for (int i = 1; i <= generaciones; i++) {
            
            // Pintamos la configuración actual
            for (int j = 0; j < config.length; j++) {
                g.setColor(paleta[config[j]]);
                g.drawOval(j, i, 1, 1);
            }        
            //Pasamos a la siguiente
            evolucionar();

            
        }
       repaint();
    }


    public void reset() { imagen = null; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    /**
     * Convierte un número de regla decimal a un array en base k.
     * @param regla El número de la regla (ej. 792)
     * @param k El número de estados (ej. 3)
     * @return Array donde el índice es la suma de la vecindad y el valor es el nuevo estado.
     */
    private int[] decodificarRegla(int regla, int k) {
        /* Como miramos 3 células (izquierda, centro, derecha) y cada una puede estar en uno de k estados, 
        la suma máxima que podemos obtener es 3*(k-1) (si todas las células están en el estado más alto). 
        Por lo tanto, necesitamos un array que pueda indexar desde 0 hasta esa suma máxima, de ahí el + 1.
        */
        int numDigitos = 3 * (k - 1) + 1;
        int[] arrayRegla = new int[numDigitos];
        
        // Pasar el número a base 'k'
        for (int i = 0; i < numDigitos; i++) {
            arrayRegla[i] = regla % k; // El resto es el dígito en base k
            regla /= k;                // Dividimos para la siguiente iteración
        }
        
        return arrayRegla;
    }

    /**
     * Genera un vector de numEstados colores lo más diferentes posible entre sí
     * @param numEstados número de estados posibles
     * @return Vector de numEstados colores 
     */
    private Color[] generarPaleta(int numEstados) {
        // https://www.learnui.design/blog/the-hsb-color-system-practicioners-primer.html
        Color[] paleta = new Color[numEstados];
        paleta[0] = Color.BLACK; 
        for (int i = 1; i < numEstados; i++) {
            float hue = (float) (i - 1) / (numEstados - 1);
            paleta[i] = Color.getHSBColor(hue, 0.8f, 1f);
        }
        return paleta;
    }
}




