/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;

public class StrokeState extends javax.swing.JPanel {

    private float strokeThickness = 1f;//kích thước nét vẽ - mặc định là 1
    private BasicStroke stroke = new BasicStroke(strokeThickness);//kiểu + kích thước nét vẽ

    private float[] dash;

    private static final float[] DASH_1 = null;
    private static final float[] DASH_2 = {10f};
    private static final float[] DASH_3 = {2f};
    private static final float[] DASH_4 = {1f, 2f, 5f};
    private static final float[] DASH_5 = {1f, 5f, 5f, 5f};
    
    public StrokeState() {
        initComponents();
        this.setStrokeComboBox();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stroke 1", "Stroke 2", "Stroke 3", "Stroke 4", "Stroke 5" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", " " }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 96, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        switch (jComboBox1.getSelectedIndex() + 1) {
            case 1:
                this.setDash(DASH_1);
                this.setStroke(strokeThickness, dash);
                break;
            case 2:
                this.setDash(DASH_2);
                this.setStroke(strokeThickness, dash);
                break;
            case 3:
                this.setDash(DASH_3);
                this.setStroke(strokeThickness, dash);
                break;
            case 4:
                this.setDash(DASH_4);
                this.setStroke(strokeThickness, dash);
                break;
            case 5:
                this.setDash(DASH_5);
                this.setStroke(strokeThickness, dash);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        this.setStrokeThickness((float) (jComboBox2.getSelectedIndex() + 1));
        this.setStroke(strokeThickness, dash);
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    public float getStrokeThickness() {
        return strokeThickness;
    }

    public void setStrokeThickness(float strokeThickness) {
        this.strokeThickness = strokeThickness;
    }

    public BasicStroke getStroke() {
        return stroke;
    }

    public void setStroke(float strokeThickness, float[] dash) {
        BasicStroke basicStroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 1.0f, dash, 2f);
        this.stroke = basicStroke;
    }

    public float[] getDash() {
        return dash;
    }

    public void setDash(float[] dash) {
        this.dash = dash;
    }
    
    private void setStrokeComboBox() {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (int i = 0; i < jComboBox1.getItemCount(); i++) {
            m.addElement(i);
        }
        jComboBox1.setModel(m);
        StrokeComboboxRenderer r = new StrokeComboboxRenderer();
        r.setPreferredSize(new Dimension(50, 15));
        jComboBox1.setRenderer(r);
    }

    public Image getImageIcon(String path) {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
        return image;
    }
    
    
    class StrokeComboboxRenderer extends JLabel implements ListCellRenderer {

        private String fileStrokeIcon[] = new String[]{"Stroke 1", "Stroke 2", "Stroke 3", "Stroke 4", "Stroke 5"};
        private ImageIcon strokeIcon[] = new ImageIcon[fileStrokeIcon.length];

        public StrokeComboboxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            for (int i = 0; i < fileStrokeIcon.length; i++) {
//                strokeIcon[i] = new ImageIcon(getImageIcon("/icon/stroke/" + fileStrokeIcon[i] + ".png"));
            }
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer) value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            ImageIcon icon = strokeIcon[selectedIndex];
            setIcon(icon);
            setText(null);
            return this;
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    // End of variables declaration//GEN-END:variables
}
