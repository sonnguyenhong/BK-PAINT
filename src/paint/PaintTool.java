
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
/**
 *
 * @author hlong
 */
public class PaintTool extends javax.swing.JPanel implements ActionListener {
   @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static enum DrawMode{LINE,RECTANGLE,OVAL,PENCIL,ERASER,CURVE,TRIANGLE,BUCKET,PICKER,TEXT,SELECT}
    private DrawMode drawMode = DrawMode.PENCIL;
    /**
     * Creates new form PaintTool
     */
    public PaintTool() {
        initComponents();
    }
    public Image getImageIcon(String path){
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
        return image;
    }
    public DrawMode getDrawMode(){
        return drawMode;
    }
    public void setDrawMode(DrawMode newDrawMode){
        DrawMode oldDrawMode = this.drawMode;
        this.drawMode = newDrawMode;
        this.firePropertyChange("tool change", oldDrawMode, newDrawMode);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        bEllipse = new javax.swing.JToggleButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        bPencil = new javax.swing.JToggleButton();
        bBucket = new javax.swing.JToggleButton();
        bText = new javax.swing.JToggleButton();
        bEraser = new javax.swing.JToggleButton();
        bPicker = new javax.swing.JToggleButton();
        bRectangle = new javax.swing.JToggleButton();
        bSelect = new javax.swing.JToggleButton();
        bLine = new javax.swing.JToggleButton();
        bTriangle = new javax.swing.JToggleButton();
        bCurve = new javax.swing.JToggleButton();
        bOval = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        jToggleButton4.setText("jToggleButton4");

        jToggleButton10.setText("jToggleButton10");

        bEllipse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/oval.png"))); // NOI18N
        bEllipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEllipseActionPerformed(evt);
            }
        });

        buttonGroup1.add(bPencil);
        bPencil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/pencil.png"))); // NOI18N
        bPencil.setSelected(true);
        bPencil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPencilActionPerformed(evt);
            }
        });

        buttonGroup1.add(bBucket);
        bBucket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/bucket.png"))); // NOI18N
        bBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBucketActionPerformed(evt);
            }
        });

        buttonGroup1.add(bText);
        bText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/font.png"))); // NOI18N
        bText.setToolTipText("");
        bText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTextActionPerformed(evt);
            }
        });

        buttonGroup1.add(bEraser);
        bEraser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/eraser.png"))); // NOI18N
        bEraser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEraserActionPerformed(evt);
            }
        });

        buttonGroup1.add(bPicker);
        bPicker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/picker.png"))); // NOI18N
        bPicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPickerActionPerformed(evt);
            }
        });

        buttonGroup1.add(bRectangle);
        bRectangle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/rectangle.png"))); // NOI18N
        bRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRectangleActionPerformed(evt);
            }
        });

        buttonGroup1.add(bSelect);
        bSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/select.png"))); // NOI18N
        bSelect.setToolTipText("");
        bSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSelectActionPerformed(evt);
            }
        });

        buttonGroup1.add(bLine);
        bLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/line.png"))); // NOI18N
        bLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLineActionPerformed(evt);
            }
        });

        buttonGroup1.add(bTriangle);
        bTriangle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/triangle.png"))); // NOI18N
        bTriangle.setToolTipText("");
        bTriangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTriangleActionPerformed(evt);
            }
        });

        buttonGroup1.add(bCurve);
        bCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/curve.png"))); // NOI18N
        bCurve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCurveActionPerformed(evt);
            }
        });

        buttonGroup1.add(bOval);
        bOval.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paint/oval.png"))); // NOI18N
        bOval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOvalActionPerformed(evt);
            }
        });

        jLabel1.setText("Select");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bEraser, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPencil, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bBucket, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bRectangle, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bLine, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bOval, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bCurve, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTriangle, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bBucket, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bLine, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bText, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTriangle, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPencil))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bCurve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bOval, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bEraser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRectangle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bPicker, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(bSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bPickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPickerActionPerformed
       setDrawMode(DrawMode.PICKER);
        // TODO add your handling code here:
    }//GEN-LAST:event_bPickerActionPerformed

    private void bEraserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEraserActionPerformed
       setDrawMode(DrawMode.ERASER);
        // TODO add your handling code here:
    }//GEN-LAST:event_bEraserActionPerformed

    private void bRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRectangleActionPerformed
       setDrawMode(DrawMode.RECTANGLE);
        // TODO add your handling code here:
    }//GEN-LAST:event_bRectangleActionPerformed

    private void bEllipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEllipseActionPerformed
    }//GEN-LAST:event_bEllipseActionPerformed

    private void bPencilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPencilActionPerformed
       setDrawMode(DrawMode.PENCIL);
        // TODO add your handling code here:
    }//GEN-LAST:event_bPencilActionPerformed

    private void bOvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOvalActionPerformed
       setDrawMode(DrawMode.OVAL);
        // TODO add your handling code here:
    }//GEN-LAST:event_bOvalActionPerformed

    private void bTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTextActionPerformed
       setDrawMode(DrawMode.TEXT);
        // TODO add your handling code here:
    }//GEN-LAST:event_bTextActionPerformed

    private void bLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLineActionPerformed
       setDrawMode(DrawMode.LINE);
        // TODO add your handling code here:
    }//GEN-LAST:event_bLineActionPerformed

    private void bTriangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTriangleActionPerformed
       setDrawMode(DrawMode.TRIANGLE);
        // TODO add your handling code here:
    }//GEN-LAST:event_bTriangleActionPerformed

    private void bCurveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCurveActionPerformed
       setDrawMode(DrawMode.CURVE);
        // TODO add your handling code here:
    }//GEN-LAST:event_bCurveActionPerformed

    private void bSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSelectActionPerformed
       setDrawMode(DrawMode.SELECT);
        // TODO add your handling code here:
    }//GEN-LAST:event_bSelectActionPerformed

    private void bBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBucketActionPerformed

        setDrawMode(DrawMode.BUCKET);
        // TODO add your handling code here:

    }//GEN-LAST:event_bBucketActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton bBucket;
    private javax.swing.JToggleButton bCurve;
    private javax.swing.JToggleButton bEllipse;
    private javax.swing.JToggleButton bEraser;
    private javax.swing.JToggleButton bLine;
    private javax.swing.JToggleButton bOval;
    private javax.swing.JToggleButton bPencil;
    private javax.swing.JToggleButton bPicker;
    private javax.swing.JToggleButton bRectangle;
    private javax.swing.JToggleButton bSelect;
    private javax.swing.JToggleButton bText;
    private javax.swing.JToggleButton bTriangle;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton4;
    // End of variables declaration//GEN-END:variables
}
