import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JTable;

public class rubiksGui extends JFrame implements WindowListener {
    private JPanel root;
    private JTextField nameText;
    private JTextField timeText;
    private JLabel nameLabel;
    private JLabel timeLabel;
    private JTable table;
    private JButton addButton;
    private JButton delButton;
    private JButton quitButton;

    rubiksGui(final RubiksDataModel rubiksDataModel) {

        setContentPane(root);
        pack();
        setTitle("Rubik's Cube solve times");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        table.setGridColor(Color.BLACK);
        table.setModel(rubiksDataModel);

        addButton.addActionListener(e -> {

            //Get Movie title, make sure it's not blank
            String solveName = nameText.getText();

            if (solveName == null || solveName.trim().equals("")) {
                JOptionPane.showMessageDialog(rootPane, "Please enter the name of the solver");
                return;
            }

            float solveTime;

            try {
                solveTime = Float.parseFloat(timeText.getText());
                if (solveTime < 0 || solveTime > 10000) {
                    throw new NumberFormatException("Time must be between 0.00 and 10,000.00 seconds");
                }
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(rootPane,
                        "Time needs to be a number between 0.00 and 10,000.00");
                return;
            }
        });


        quitButton.addActionListener(e -> {
            cubes.shutdown();
            System.exit(0);
        });

        delButton.addActionListener(e -> {
            int currentRow = rubiksDataModel.getColumnCount();

            if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                JOptionPane.showMessageDialog(rootPane, "Please choose a movie to delete");
            }
            boolean deleted = rubiksDataModel.deleteRow(currentRow);
            if (deleted) {
                cubes.loadAllTimes();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error deleting movie");
            }
        });
    }

    //windowListener methods. Only need one of them, but are required to implement the others anyway
    //WindowClosing will call DB shutdown code, which is important, so the DB is in a consistent state however the application is closed.

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        cubes.shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
