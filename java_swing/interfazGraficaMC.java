package java_swing;


import javax.swing.*;
import java.awt.*;


public class interfazGraficaMC extends JFrame {

    public interfazGraficaMC() {

        // --- 1.  Configuramos el marco redimensionable

        //Título de la ventana
        setTitle("MC - Ejemplo de GUI básica"); 
        
        //Tamaño de la ventana por defecto
        setSize(800, 500);
        setLocationRelativeTo(null); //Aparece en el medio

        // Configurado para terminar el programa cuando se cierre la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 



        // --- 2. Añadimos la barra de menu
        setJMenuBar(crearMenuBar());



        // --- 3. Añadimos el contenido principal de la ventana

        /*Usamos Border layout para poder poner en el centro/izquierda la parte 
        de simulación y en la derecha la parte del panel de control*/
        setLayout(new BorderLayout());

        // *** 3.1. REGIÓN A LA IZQUIERDA (SIMULACIONES GRÁFICAS) ---

        // Usamos un JPanel.
        JPanel panelIzquierda = new JPanel();
        panelIzquierda.setBackground(Color.BLACK); // Como el agujero negro de tu foto
        panelIzquierda.setLayout(new GridBagLayout()); // Solo para centrar el texto provisional
        
        panelIzquierda.setLayout(new BorderLayout()); 
        
        panelIzquierda.setBackground(Color.BLACK); 


        // Etiqueta con imagen
        JLabel etiquetaConImagen = new JLabel(new ImageIcon("mc/java_swing/start/fondo.jpg"));
        panelIzquierda.add(etiquetaConImagen, BorderLayout.CENTER);



        // *** 3.2. REGIÓN A LA DERECHA (BOTONERA, AYUDA, ACERCA DE) ---
        JPanel panelDerecha = new JPanel();
        /* GridLayout(0, 1) hace una sola columna con tantas filas como botones metamos.
        Los números (10, 10) son el espacio entre botones.*/
        panelDerecha.setLayout(new GridLayout(0, 1, 0, 20)); 
        panelDerecha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Un poco de margen
        panelDerecha.setBackground(Color.GRAY); 
        
        // Creamos los botones según el ejemplo
        panelDerecha.add(crearBoton("Parámetros"));
        panelDerecha.add(crearBoton("Curva"));
        panelDerecha.add(crearBoton("Computar"));
        panelDerecha.add(crearBoton("Detener"));
        panelDerecha.add(crearBoton("Ayuda"));


        // AÑADIR LOS PANELES A LA VENTANA
        // CENTER ocupa todo el espacio sobrante
        add(panelIzquierda, BorderLayout.CENTER);
        // EAST se pega a la derecha con el ancho que necesiten sus botones
        add(panelDerecha, BorderLayout.EAST);
    }

    // Crea un botón, le pone la acción y lo añade al panel
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);

        // --- ESTÉTICA ---
        // 1. Fuente
        boton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20)); 
        
        // COLOR ALEATORIO
        // Generamos 3 números aleatorios entre 0 y 255
        int r = (int) (Math.random() * 256); // Red
        int g = (int) (Math.random() * 256); // Green
        int b = (int) (Math.random() * 256); // Blue
        
        boton.setBackground(new Color(r, g, b)); 
        
        // Calculamos si el color es oscuro o claro para poner el texto blanco o negro
        double brillo = (0.299 * r + 0.587 * g + 0.114 * b); // Estándar
        if (brillo < 128) {
            boton.setForeground(Color.WHITE); // Fondo oscuro -> Texto blanco
        } else {
            boton.setForeground(Color.BLACK); // Fondo claro -> Texto negro
        }
        
        // Quitar el recuadro de "foco"
        boton.setFocusPainted(false);
        
        // Cursor
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- FUNCIONALIDAD ---
        boton.addActionListener(e -> abrirVentanaMensaje(texto));

        return boton;
    }

    private JMenuBar crearMenuBar(){
        // --- 2. MENÚ DE USUARIO DE AL MENOS DOS NIVELES ---
        JMenuBar menuBar = new JMenuBar();
        
        // Nivel 1: El menú principal
        JMenu menuA = new JMenu("OpcionA");        
        JMenu menuB = new JMenu("OpcionB");
        JMenu menuC = new JMenu("OpcionC");
        JMenu menuAcercaDe = new JMenu("Acerca De");
        
                
        // Nivel 2: Opciones dentro de cada apartado

        //Submenú en la opcion A
        JMenu subMenuA1 = new JMenu("OpcionA1");
        agregarItemMenu(subMenuA1, "OpcionA1.1");
        agregarItemMenu(subMenuA1, "OpcionA1.2");
        menuA.add(subMenuA1); // Añadimos el submenú al menú
        
        // Elementos simples
        agregarItemMenu(menuA, "OpcionA2");
        agregarItemMenu(menuA, "OpcionA3");

        agregarItemMenu(menuB, "OpcionB1");
        agregarItemMenu(menuB, "OpcionB2");

        agregarItemMenu(menuC, "OpcionC1");
        agregarItemMenu(menuC, "OpcionC2");
        
        agregarItemMenu(menuAcercaDe, "Mostrar Info");



        // Añadimos menús a la barra
        menuBar.add(menuA);
        menuBar.add(menuB);
        menuBar.add(menuC);
        menuBar.add(menuAcercaDe);
        
        return menuBar;
    }
    // Crea un item de menú, le pone la acción y lo añade al menú
    private void agregarItemMenu(JMenu menu, String texto) {
        JMenuItem item = new JMenuItem(texto);
        // Lanza evento consistente en abrir ventana con el nombre
        item.addActionListener(e -> abrirVentanaMensaje(texto));
        menu.add(item);
    }

    private void abrirVentanaMensaje(String nombreItem) {
        JOptionPane.showMessageDialog(null, "seleccionaste: "+nombreItem);
    }

    public static void main(String[] args) {
        interfazGraficaMC ventana = new interfazGraficaMC();
        ventana.setVisible(true);
    }
}
