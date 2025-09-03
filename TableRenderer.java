import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            
            // Set default font
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            
            // Center align all content
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Color coding for different columns
            if (column == 4) { // Available Seats column
                try {
                    int seats = Integer.parseInt(value.toString());
                    if (seats == 0) {
                        label.setForeground(Color.RED);
                        label.setFont(new Font("Arial", Font.BOLD, 12));
                    } else if (seats <= 5) {
                        label.setForeground(new Color(255, 140, 0)); // Orange
                        label.setFont(new Font("Arial", Font.BOLD, 12));
                    } else {
                        label.setForeground(new Color(76, 175, 80)); // Green
                    }
                } catch (NumberFormatException e) {
                    // Ignore if not a number
                }
            } else if (column == 6) { // Price column
                label.setForeground(new Color(25, 118, 210)); // Blue
                label.setFont(new Font("Arial", Font.BOLD, 12));
            } else if (column == 7) { // Occupancy column
                try {
                    String occupancyStr = value.toString().replace("%", "");
                    double occupancy = Double.parseDouble(occupancyStr);
                    if (occupancy >= 90) {
                        label.setForeground(Color.RED);
                    } else if (occupancy >= 75) {
                        label.setForeground(new Color(255, 140, 0)); // Orange
                    } else {
                        label.setForeground(new Color(76, 175, 80)); // Green
                    }
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                } catch (NumberFormatException e) {
                    // Ignore if not a number
                }
            } else if (column == 4 && table.getName() != null && table.getName().equals("reservationsTable")) {
                // Status column in reservations table
                String status = value.toString();
                if (status.equals("CONFIRMED")) {
                    label.setForeground(new Color(76, 175, 80)); // Green
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                } else if (status.equals("CANCELLED")) {
                    label.setForeground(Color.RED);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                } else {
                    label.setForeground(new Color(255, 140, 0)); // Orange
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                }
            }
            
            // Set background for selected rows
            if (isSelected) {
                label.setBackground(new Color(25, 118, 210, 100));
            } else {
                label.setBackground(table.getBackground());
            }
        }
        
        return component;
    }
} 