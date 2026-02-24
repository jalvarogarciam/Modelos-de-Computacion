package bt1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class randomGeneratorGUI extends JFrame {

    private randomGenerator logicaGeneracion = new randomGenerator();
    private JComboBox<String> comboGeneradores;
    private JTextField txtCantidad;
    private JTextField txtSemilla; 
    private PanelGrafico panelSimulacion;
    private JTextArea areaTextoNumeros;

    // Nombres de los generadores para usarlos en varios sitios
    private final String[] nombresGeneradores = {"26.1a", "26.1b", "26.2", "26.3", "Combinado", "Fishman-Moore", "RANDU"};

    public randomGeneratorGUI() {
        setTitle("MC - Generador de Números Pseudoaleatorios (UCA)"); 
        setSize(1200, 850); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout());

        // --- 1. PANEL CENTRAL (GRÁFICO) ---
        panelSimulacion = new PanelGrafico();
        add(panelSimulacion, BorderLayout.CENTER);

        // --- 2. PANEL DERECHO (CONTROLES Y LEYENDA) ---
        JPanel panelDerechoContenedor = new JPanel(new BorderLayout());
        panelDerechoContenedor.setBackground(Color.GRAY);
        panelDerechoContenedor.setPreferredSize(new Dimension(250, 0));

        // 2.1. Panel de Controles (Arriba)
        JPanel panelControles = new JPanel(new GridLayout(0, 1, 0, 10));
        panelControles.setOpaque(false);
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelControles.add(crearEtiqueta("ALGORITMO:"));
        comboGeneradores = new JComboBox<>(nombresGeneradores);
        comboGeneradores.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        panelControles.add(comboGeneradores);

        panelControles.add(crearEtiqueta("SEMILLA (Seed):"));
        txtSemilla = new JTextField("1"); 
        txtSemilla.setHorizontalAlignment(JTextField.CENTER);
        txtSemilla.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        panelControles.add(txtSemilla);

        panelControles.add(crearEtiqueta("Nº PUNTOS:"));
        txtCantidad = new JTextField("1000"); 
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);
        txtCantidad.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        panelControles.add(txtCantidad);

        panelControles.add(crearBoton("Computar"));
        panelControles.add(crearBoton("Limpiar"));

        // 2.2. Panel de Leyenda (Abajo de los controles)
        JPanel panelLeyenda = new JPanel();
        panelLeyenda.setLayout(new BoxLayout(panelLeyenda, BoxLayout.Y_AXIS));
        panelLeyenda.setOpaque(false);
        panelLeyenda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY), "Leyenda de Colores", 0, 0, 
                new Font(Font.MONOSPACED, Font.BOLD, 12), Color.DARK_GRAY));

        for (String nombre : nombresGeneradores) {
            panelLeyenda.add(crearItemLeyenda(nombre, obtenerColorGenerador(nombre)));
            panelLeyenda.add(Box.createVerticalStrut(5)); // Espacio entre items
        }

        panelDerechoContenedor.add(panelControles, BorderLayout.NORTH);
        panelDerechoContenedor.add(panelLeyenda, BorderLayout.CENTER);
        add(panelDerechoContenedor, BorderLayout.EAST);

        // --- 3. PANEL INFERIOR (OUTPUT) ---
        areaTextoNumeros = new JTextArea(10, 20); 
        areaTextoNumeros.setEditable(false);
        areaTextoNumeros.setLineWrap(true);
        areaTextoNumeros.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaTextoNumeros);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Coordenadas (x, y) acumuladas"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    // Crea un item visual para la leyenda: cuadradito de color + texto
    private JPanel crearItemLeyenda(String nombre, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        
        // El cuadradito de color
        JPanel cuadroColor = new JPanel();
        cuadroColor.setPreferredSize(new Dimension(15, 15));
        cuadroColor.setBackground(color);
        cuadroColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel label = new JLabel(nombre);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        
        p.add(cuadroColor);
        p.add(label);
        return p;
    }

    private Color obtenerColorGenerador(String gen) {
        switch (gen) {
            case "26.1a": return Color.CYAN;
            case "26.1b": return Color.BLUE;
            case "26.2":  return Color.MAGENTA;
            case "26.3":  return Color.ORANGE;
            case "Combinado": return Color.YELLOW;
            case "Fishman-Moore": return Color.GREEN;
            case "RANDU": return Color.RED;
            default: return Color.WHITE;
        }
    }

    private void ejecutarGeneracion() {
        try {
            long semilla = Long.parseLong(txtSemilla.getText());
            int nPuntos = Integer.parseInt(txtCantidad.getText());
            int totalNumeros = nPuntos * 2; 
            String gen = (String) comboGeneradores.getSelectedItem();
            double[] numeros;

            switch (gen) {
                case "26.1a": numeros = logicaGeneracion.generador26_1a(semilla, totalNumeros); break;
                case "26.1b": numeros = logicaGeneracion.generador26_1b(semilla, totalNumeros); break;
                case "26.2":  numeros = logicaGeneracion.generador26_2(semilla, totalNumeros); break;
                case "26.3":  numeros = logicaGeneracion.generador26_3(semilla, totalNumeros); break;
                case "Combinado": numeros = logicaGeneracion.generadorCombinado(semilla, totalNumeros); break;
                case "Fishman-Moore": numeros = logicaGeneracion.generadorFishmanMoore(semilla, totalNumeros); break;
                case "RANDU": numeros = logicaGeneracion.generadorRANDU(semilla, totalNumeros); break;
                default: return;
            }

            panelSimulacion.actualizarGrafico(numeros, obtenerColorGenerador(gen));

            StringBuilder sb = new StringBuilder();
            sb.append("[" + gen + "]: ");
            for (int i = 0; i < numeros.length - 1; i += 2) {
                sb.append(String.format("(%.3f, %.3f) ", numeros[i], numeros[i+1]));
            }
            areaTextoNumeros.append(sb.toString() + "\n\n");

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
            if (texto.equals("Computar")) ejecutarGeneracion();
            else if (texto.equals("Limpiar")) {
                panelSimulacion.reset();
                areaTextoNumeros.setText("");
            }
        });
        return boton;
    }

    private class PanelGrafico extends JPanel {
        private BufferedImage imagenBuffer;
        public PanelGrafico() { setBackground(Color.BLACK); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenBuffer != null) g.drawImage(imagenBuffer, 0, 0, null);
        }
        public void actualizarGrafico(double[] r, Color colorPunto) {
            int ancho = getWidth(), alto = getHeight();
            if (ancho <= 0 || alto <= 0) return;
            if (imagenBuffer == null) {
                imagenBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
                Graphics g = imagenBuffer.getGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, ancho, alto);
                g.dispose();
            }
            Graphics2D g2 = (Graphics2D) imagenBuffer.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorPunto);
            int grosor = 5; 
            for (int i = 0; i < r.length - 1; i += 2) {
                int x = (int) (r[i] * (ancho - grosor));
                int y = (int) (r[i+1] * (alto - grosor));
                g2.fillOval(x, y, grosor, grosor);
            }
            g2.dispose();
            repaint();
        }
        public void reset() { imagenBuffer = null; repaint(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new randomGeneratorGUI().setVisible(true));
    }
}