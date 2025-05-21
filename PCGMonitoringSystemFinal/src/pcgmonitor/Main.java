package pcgmonitor;

import pcgmonitor.controller.PCGController;
import pcgmonitor.view.PCGView;

import javax.swing.SwingUtilities;
import pcgmonitor.view.PCGView;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PCGView view = new PCGView();
            new PCGController(view);
            view.setVisible(true);
        });
    }
}
