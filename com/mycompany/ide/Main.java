
package com.mycompany.ide;
import vista.EditorFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Error al establecer el look and feel: " + e.getMessage());
            }
            
            EditorFrame frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}