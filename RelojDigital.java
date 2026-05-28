import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RelojDigital extends JFrame {

    private JLabel etiquetaHora;
    private SimpleDateFormat formatoHora;

    public RelojDigital() {
        // 1. Configuración de la ventana (JFrame)
        setTitle("Reloj Digital");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setResizable(false); // Evita que se cambie el tamaño

        // 2. Configuración del diseño y componentes
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(33, 33, 33)); // Fondo oscuro (Gris oscuro)

        // Formato de hora: Horas (24h) : Minutos : Segundos
        formatoHora = new SimpleDateFormat("HH:mm:ss");

        // Inicializar la etiqueta de la hora con un diseño limpio y llamativo
        etiquetaHora = new JLabel();
        etiquetaHora.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaHora.setFont(new Font("Monospaced", Font.BOLD, 45)); // Fuente monoespaciada para que no baile la hora
        etiquetaHora.setForeground(new Color(0, 230, 118)); // Texto verde fosforescente estilo digital

        // Añadir la etiqueta al centro de la ventana
        add(etiquetaHora, BorderLayout.CENTER);

        // 3. Iniciar el temporizador para la actualización en tiempo real
        iniciarReloj();
    }

    private void iniciarReloj() {
        // El Timer ejecuta el código cada 1000 milisegundos (1 segundo)
        Timer timer = new Timer(1000, e -> actualizarHora());
        timer.start();
        
        // Ejecución inicial para que no muestre la pantalla en blanco al abrir
        actualizarHora();
    }

    private void actualizarHora() {
        // Obtiene la hora actual del sistema
        String horaActual = formatoHora.format(Calendar.getInstance().getTime());
        etiquetaHora.setText(horaActual);
    }

    public static void main(String[] args) {
        // Ejecutar la GUI en el hilo de despacho de eventos (buena práctica en Swing)
        SwingUtilities.invokeLater(() -> {
            new RelojDigital().setVisible(true);
        });
    }
}