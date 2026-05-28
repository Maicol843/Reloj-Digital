import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RelojDigitalModerno extends JFrame {

    private JLabel etiquetaHora;
    private JLabel etiquetaFecha;
    private SimpleDateFormat formatoHora;
    private SimpleDateFormat formatoFecha;
    private Point clickMouse; // Para registrar las coordenadas del arrastre

    public RelojDigitalModerno() {
        // 1. Configuración de ventana moderna (sin bordes del sistema)
        setUndecorated(true);
        setSize(420, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hacer que el fondo por defecto del JFrame sea transparente para que se vean los bordes redondeados
        setBackground(new Color(0, 0, 0, 0));

        // 2. Formatos de tiempo y fecha (Localizados al español)
        formatoHora = new SimpleDateFormat("HH:mm:ss");
        formatoFecha = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        // 3. Panel Principal con diseño personalizado (Esquinas redondeadas y gradiente)
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Suavizar bordes de dibujo (Anti-aliasing)
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar fondo con un gradiente oscuro y elegante
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 28, 36), 0, getHeight(), new Color(15, 17, 22));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Dibujar un borde delgado brillante de neón
                g2d.setColor(new Color(0, 229, 255, 60)); // Color cian con transparencia
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
            }
        };
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setOpaque(false); // Para que se aplique nuestro dibujo personalizado

        // 4. Implementar arrastre de la ventana (Funcionalidad obligatoria al no tener barra de título)
        panelPrincipal.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickMouse = e.getPoint();
            }
        });
        panelPrincipal.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point puntoActual = e.getLocationOnScreen();
                setLocation(puntoActual.x - clickMouse.x, puntoActual.y - clickMouse.y);
            }
        });

        // 5. Botones de Control superiores (Cerrar y Minimizar)
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelControles.setOpaque(false);

        JButton btnMinimizar = crearBotonControl("—", new Color(120, 130, 140));
        btnMinimizar.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton btnCerrar = crearBotonControl("×", new Color(244, 67, 54));
        btnCerrar.addActionListener(e -> System.exit(0));

        panelControles.add(btnMinimizar);
        panelControles.add(btnCerrar);
        panelPrincipal.add(panelControles, BorderLayout.NORTH);

        // 6. Contenedor del Reloj (Hora y Fecha)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        // Estilo de la etiqueta de la hora
        etiquetaHora = new JLabel();
        etiquetaHora.setFont(new Font("Consolas", Font.BOLD, 55));
        etiquetaHora.setForeground(new Color(0, 229, 255)); // Cian eléctrico / Neón
        etiquetaHora.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Estilo de la etiqueta de la fecha
        etiquetaFecha = new JLabel();
        etiquetaFecha.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        etiquetaFecha.setForeground(new Color(138, 150, 163)); // Gris azulado suave
        etiquetaFecha.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInfo.add(etiquetaHora);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10))); // Separación de 10px
        panelInfo.add(etiquetaFecha);

        panelPrincipal.add(panelInfo, BorderLayout.CENTER);
        add(panelPrincipal);

        // 7. Iniciar el temporizador
        iniciarReloj();
    }

    private JButton crearBotonControl(String texto, Color colorHover) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(new Color(180, 190, 200));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Animación simple de hover (cambio de color al pasar el cursor)
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setForeground(colorHover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setForeground(new Color(180, 190, 200));
            }
        });

        return boton;
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> actualizarTiempo());
        timer.start();
        actualizarTiempo(); // Carga inicial
    }

    private void actualizarTiempo() {
        Calendar cal = Calendar.getInstance();
        String hora = formatoHora.format(cal.getTime());
        String fecha = formatoFecha.format(cal.getTime());

        // Asegurar que la primera letra del día de la semana esté en mayúscula
        if (fecha.length() > 0) {
            fecha = fecha.substring(0, 1).toUpperCase() + fecha.substring(1);
        }

        etiquetaHora.setText(hora);
        etiquetaFecha.setText(fecha);
    }

    public static void main(String[] args) {
        // Ejecución en el Event Dispatch Thread para evitar fallos de interfaz
        SwingUtilities.invokeLater(() -> {
            new RelojDigitalModerno().setVisible(true);
        });
    }
}