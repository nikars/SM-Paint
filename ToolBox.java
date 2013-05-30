/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ToolBox.java
 *
 * Created on 15-ene-2012, 21:01:04
 */
package smpaint_3;

/**
 * Panel de herramientas de dibujo
 * @author Nikolai
 */
public class ToolBox extends javax.swing.JPanel {
    //Variables
    private int activeTool;
    private MainWindow padre;
    private Attribs attribs;
    private String text;
    
    //Constructores
    /** Inicializa el panel
     * @param p (padre) MainWindow
     */
    public ToolBox(javax.swing.JFrame p) {
        initComponents();
        
        activeTool=0;
        this.padre = (MainWindow)p;
        attribs = (Attribs)padre.GetAttribs().GetContentReference();
    }
    
    //Métodos
    /**
     * Devuelve la herramienta seleccionada
     * @return Herramienta seleccionada
     */
    public int GetActiveTool(){
        return activeTool;
    }
    
     /**
     * Devuelve el texto a escribir
     * @return Texto
     */
    public String GetText(){
        return text;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     * @return 
     */
    @SuppressWarnings("unchecked")   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lineButton = new javax.swing.JToggleButton();
        freeButton = new javax.swing.JToggleButton();
        rectButton = new javax.swing.JToggleButton();
        roundButton = new javax.swing.JToggleButton();
        ellipseButton = new javax.swing.JToggleButton();
        curveButton = new javax.swing.JToggleButton();
        textButton = new javax.swing.JToggleButton();
        costomButton = new javax.swing.JToggleButton();
        editModeButton = new javax.swing.JToggleButton();

        setToolTipText("");
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup1.add(lineButton);
        lineButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/line.png"))); // NOI18N
        lineButton.setSelected(true);
        lineButton.setToolTipText("Línea");
        lineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineButtonActionPerformed(evt);
            }
        });
        add(lineButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 50, 50));

        buttonGroup1.add(freeButton);
        freeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/brush.png"))); // NOI18N
        freeButton.setToolTipText("Trazo Libre");
        freeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freeButtonActionPerformed(evt);
            }
        });
        add(freeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 50, 50));

        buttonGroup1.add(rectButton);
        rectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/rect.png"))); // NOI18N
        rectButton.setToolTipText("Rectángulo (Cuadrado)");
        rectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectButtonActionPerformed(evt);
            }
        });
        add(rectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, 50));

        buttonGroup1.add(roundButton);
        roundButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/rounded.png"))); // NOI18N
        roundButton.setToolTipText("Rectángulo (Cuadrado) Redondeado");
        roundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundButtonActionPerformed(evt);
            }
        });
        add(roundButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 50, 50));

        buttonGroup1.add(ellipseButton);
        ellipseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/ellipse.png"))); // NOI18N
        ellipseButton.setToolTipText("Elipse (Círculo)");
        ellipseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ellipseButtonActionPerformed(evt);
            }
        });
        add(ellipseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 50, 50));

        buttonGroup1.add(curveButton);
        curveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/free.png"))); // NOI18N
        curveButton.setToolTipText("Curva");
        curveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curveButtonActionPerformed(evt);
            }
        });
        add(curveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 50, 50));

        buttonGroup1.add(textButton);
        textButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/text.png"))); // NOI18N
        textButton.setToolTipText("Texto");
        textButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textButtonActionPerformed(evt);
            }
        });
        add(textButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 50, 50));

        buttonGroup1.add(costomButton);
        costomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/custom.png"))); // NOI18N
        costomButton.setToolTipText("Forma Personalizada (Estrella)");
        costomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                costomButtonActionPerformed(evt);
            }
        });
        add(costomButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 50, 50));

        editModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/edit.png"))); // NOI18N
        editModeButton.setText("Edición");
        editModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editModeButtonActionPerformed(evt);
            }
        });
        add(editModeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 120, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void lineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 0;        
        if(attribs!=null) {
            attribs.HideFill();
            attribs.ShowGrosor();
            attribs.ShowStroke();
        }
    }//GEN-LAST:event_lineButtonActionPerformed

    private void freeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freeButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 1;
        if(attribs!=null) {
            attribs.HideFill();
            attribs.ShowGrosor();
            attribs.ShowStroke();
        }
    }//GEN-LAST:event_freeButtonActionPerformed

    private void rectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 2;
        if(attribs!=null) {
            attribs.ShowFill();
            attribs.ShowGrosor();
            attribs.ShowStroke();
        }
    }//GEN-LAST:event_rectButtonActionPerformed

    private void roundButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 3;
        if(attribs!=null) {
            attribs.ShowFill();
            attribs.ShowGrosor();
            attribs.ShowStroke();
        }
    }//GEN-LAST:event_roundButtonActionPerformed

    private void ellipseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ellipseButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 4;
        if(attribs!=null) {
            attribs.ShowFill();
            attribs.ShowGrosor();
            attribs.ShowStroke();
        }
    }//GEN-LAST:event_ellipseButtonActionPerformed

    private void curveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curveButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 5;
        if(attribs!=null)attribs.HideFill();
    }//GEN-LAST:event_curveButtonActionPerformed

    private void textButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textButtonActionPerformed
        // TODO add your handling code here:
        TextInputDialog tid = new TextInputDialog(padre, true);
        text = tid.GetInsertText();
        activeTool = 6;
        if(attribs!=null) {
            attribs.HideFill();
            attribs.HideGrosor();
            attribs.HideStroke();
        }
    }//GEN-LAST:event_textButtonActionPerformed

    private void costomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_costomButtonActionPerformed
        // TODO add your handling code here:
        activeTool = 7;
        if(attribs!=null) {
            attribs.ShowFill();
            attribs.ShowGrosor();
            attribs.ShowStroke();
        }
    }//GEN-LAST:event_costomButtonActionPerformed

    private void editModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editModeButtonActionPerformed
        // TODO add your handling code here:
        if(editModeButton.isSelected()){
            attribs.SetMode(true);
            
            lineButton.setEnabled(false);
            freeButton.setEnabled(false);
            rectButton.setEnabled(false);
            roundButton.setEnabled(false);
            ellipseButton.setEnabled(false);
            curveButton.setEnabled(false);
            textButton.setEnabled(false);
            costomButton.setEnabled(false);
        }
        else {
            attribs.SetMode(false);
                                  
            lineButton.setEnabled(true);
            lineButton.setSelected(true);
            freeButton.setEnabled(true);
            rectButton.setEnabled(true);
            roundButton.setEnabled(true);
            ellipseButton.setEnabled(true);
            curveButton.setEnabled(true);
            textButton.setEnabled(true);
            costomButton.setEnabled(true);
        }
    }//GEN-LAST:event_editModeButtonActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton costomButton;
    private javax.swing.JToggleButton curveButton;
    private javax.swing.JToggleButton editModeButton;
    private javax.swing.JToggleButton ellipseButton;
    private javax.swing.JToggleButton freeButton;
    private javax.swing.JToggleButton lineButton;
    private javax.swing.JToggleButton rectButton;
    private javax.swing.JToggleButton roundButton;
    private javax.swing.JToggleButton textButton;
    // End of variables declaration//GEN-END:variables
}