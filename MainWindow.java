/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainWindow.java
 *
 * Created on Dec 12, 2011, 3:11:12 PM
 */
package smpaint_3;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ventana principal del programa
 * @author nikolai
 */
public class MainWindow extends javax.swing.JFrame {
    //Variables
    private JFileChooser fc;
    private Dimension newSize;
    private Help ayuda;
    //Vale, estas deberían de ser privadas, con sus métodos Set y Get... pero son muchas y me queda poco tiempo :(
    private MyInternalFrame tools, attribs, opers, selectedWindow;
    
    //Constructores
    /**
     * Inicializa la ventana
     */
    public MainWindow() {
        initComponents();
        setSize(1024, 720);
        setLocationByPlatform(true);
        setLocationRelativeTo(null);
        saveMenuItem.setEnabled(false);
        saveTButton.setEnabled(false);
        
        initPanels();
        
        //Para capturar los eventos del mouse en todos los componentes descendentes
        long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
        {
            @Override
            public void eventDispatched(AWTEvent e)
            {
                FrameControl();
   
            }
        }, eventMask);
    }
    
    //Metodos
    private void New() {
        newImageDialog nid = new newImageDialog(this, true);
        newSize = nid.GetSize();
        if(newSize!=null){
            try {
                tools.setIcon(false);
            } catch (java.beans.PropertyVetoException e) {} 
            try {
                attribs.setIcon(false);
            } catch (java.beans.PropertyVetoException e) {} 
            selectedWindow = createFrame();
        }        
        newSize=null;
        FrameControl();
    }
    
    private void FrameControl() {
        if(selectedWindow != null) { //Hay ventanas de imagen abiertas
                    if(((MyInternalFrame)jDesktopPane1.getSelectedFrame()).GetType()) {
                        //Seleccinamos la ventana activa en cada momento
                        selectedWindow = (MyInternalFrame)jDesktopPane1.getSelectedFrame();
                        saveMenuItem.setEnabled(true);
                        saveTButton.setEnabled(true);
                    }
                    else {
                        saveMenuItem.setEnabled(false);
                        saveTButton.setEnabled(false);
                    }
                }
    }
    
    private void Open() {
        fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Medios soportados (*.jpg, *.png, *.gif, *.avi, *.mvr, *.mpg, *.mov, "
                + "*.au, *.wav, *.mp2, *.mp3, *.mid, *.gsm, *.aiff)", "jpg", "png", "gif", "avi", "mvr", "mpg", "mov", "au", 
                "wav", "mp2", "mp3", "mid", "gsm", "aiff"));
        int resp = fc.showOpenDialog(this);
        if( resp == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length());   
            
            
            if(("jpg".equals(ext))||("png".equals(ext))||("gif".equals(ext))) { //Imágenes
                try{                    
                    selectedWindow = createFrame(ImageIO.read(f), f.getName());
                    try {
                        tools.setIcon(false);
                    } catch (java.beans.PropertyVetoException e) {} 
                    try {
                        attribs.setIcon(false);
                    } catch (java.beans.PropertyVetoException e) {} 
                    try {
                        opers.setIcon(false);
                    } catch (java.beans.PropertyVetoException e) {} 

                }                
                catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Error al leer la imagen!",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (("avi".equals(ext))||("mvr".equals(ext))||("mpg".equals(ext))||("mov".equals(ext))) { //Videos
                try {
                    createFrame(f, false);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Error al leer el fichero de Video",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                } 
            }
            else if (("au".equals(ext))||("wav".equals(ext))||("mp2".equals(ext))||("mp3".equals(ext))||
                    ("mid".equals(ext))||("gsm".equals(ext))||("aiff".equals(ext))){ //Sonidos
                try {
                    createFrame(f, true);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Error al leer el fichero de Audio",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
           
        }
        FrameControl();
    }
    
    private void Save() {
        if(selectedWindow!=null){
            FileFilter filter = new FileNameExtensionFilter("Archivo PNG", "png");

            fc = new JFileChooser();
            fc.addChoosableFileFilter(filter);
            int resp = fc.showSaveDialog(this);
            if( resp == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage bi = ((Lienzo)selectedWindow.GetContentReference()).GetImage();
                    File outputfile = fc.getSelectedFile();
                    ImageIO.write(bi, "png", outputfile);
                    selectedWindow.setTitle(fc.getSelectedFile().getName());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al guardar la imagen!",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else JOptionPane.showMessageDialog(this, "Nada que guardar!",
                                    "Hmm", JOptionPane.INFORMATION_MESSAGE);
    }
   
    private void initPanels() { //Crear frames internos
        if(attribs==null){
            attribs = new MyInternalFrame(this, 1);
            //Por encima de otras ventanas
            jDesktopPane1.add(attribs, new Integer( 10 ));
        }
        try {
            attribs.setIcon(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        attribs.setVisible(true);
        
        if(tools==null){
            tools = new MyInternalFrame(this, 0);
            jDesktopPane1.add(tools, new Integer( 10 ));
        }        
        tools.setVisible(true);
        try {
            tools.setIcon(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        if(opers==null){
            opers = new MyInternalFrame(this, 2);
            jDesktopPane1.add(opers, new Integer( 10 ));
        }
        try {
            opers.setIcon(true);
        } catch (java.beans.PropertyVetoException e) {}
        
        opers.setVisible(true);
    }
    
    private MyInternalFrame createFrame() { //Nueva imagen
        MyInternalFrame frame = new MyInternalFrame(this, newSize);
        frame.setVisible(true);
        jDesktopPane1.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        return frame;
    }

    private MyInternalFrame createFrame(BufferedImage img, String s) { //Imagen cargada del disco
        MyInternalFrame frame = new MyInternalFrame(this, img, s);
        frame.setVisible(true);
        jDesktopPane1.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        return frame;
    }
 
    private MyInternalFrame createFrame(File f, boolean s) { //s=true:Sonido s=falses:video
        MyInternalFrame frame = new MyInternalFrame(this, f, s);
        frame.setVisible(true);
        jDesktopPane1.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
        return frame;
    }
    
    /**
     * Devuelve la ventana de imagen activa
     * @return Ventana activa
     */
    public MyInternalFrame GetSelectedWindow() {
        return selectedWindow;
    }
    
     /**
     * Devuelve la ventana de atributos
     * @return Ventana de atributos
     */
    public MyInternalFrame GetAttribs() {
        return attribs;
    }
    
     /**
     * Devuelve la ventana de herramientas
     * @return Ventana de herramientas
     */
    public MyInternalFrame GetTools() {
        return tools;
    }
    
    //Acerca de..
    private void About() {
        JOptionPane.showMessageDialog(null, "Sistemas Multimedia. Práctica SMPaint v3.0\n"
                + "Nikolai Arsentiev. I.T.I. Sistemas\nUniversidad de Granada, 2012", "Acerca de SMPaint",1);
    }
    
    //Ayuda
    private void Help() {
        ayuda = new Help(this, true);
        ayuda.setVisible(true);
    }
    
    /**
    * Actualiza la barra de estado
    * @param s Texto a mostrar en la barra de estado
    */
    public void setEstado(String s){
        barraEstado.setText(s);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraEstado = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        newTButton = new javax.swing.JButton();
        openTButton = new javax.swing.JButton();
        saveTButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        helpTButton = new javax.swing.JButton();
        aboutTButton = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        eraseMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SMPaint");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        barraEstado.setText("Listo");
        getContentPane().add(barraEstado, java.awt.BorderLayout.PAGE_END);

        jPanel1.setBackground(javax.swing.UIManager.getDefaults().getColor("tab_highlight_header"));
        jPanel1.setForeground(new java.awt.Color(224, 56, 56));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 30));

        jToolBar1.setFloatable(false);
        jToolBar1.setBorderPainted(false);
        jToolBar1.setOpaque(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 35));

        newTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/new.png"))); // NOI18N
        newTButton.setToolTipText("Nueva Imagen");
        newTButton.setMaximumSize(new java.awt.Dimension(30, 30));
        newTButton.setMinimumSize(new java.awt.Dimension(30, 30));
        newTButton.setPreferredSize(new java.awt.Dimension(30, 30));
        newTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(newTButton);

        openTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/open.png"))); // NOI18N
        openTButton.setMaximumSize(new java.awt.Dimension(30, 30));
        openTButton.setMinimumSize(new java.awt.Dimension(30, 30));
        openTButton.setPreferredSize(new java.awt.Dimension(30, 30));
        openTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(openTButton);

        saveTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/save.png"))); // NOI18N
        saveTButton.setMaximumSize(new java.awt.Dimension(30, 30));
        saveTButton.setMinimumSize(new java.awt.Dimension(30, 30));
        saveTButton.setPreferredSize(new java.awt.Dimension(30, 30));
        saveTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(saveTButton);
        jToolBar1.add(jSeparator2);

        helpTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/help.png"))); // NOI18N
        helpTButton.setFocusable(false);
        helpTButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        helpTButton.setMaximumSize(new java.awt.Dimension(30, 30));
        helpTButton.setMinimumSize(new java.awt.Dimension(30, 30));
        helpTButton.setPreferredSize(new java.awt.Dimension(30, 30));
        helpTButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        helpTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpTButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(helpTButton);

        aboutTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/about.png"))); // NOI18N
        aboutTButton.setFocusable(false);
        aboutTButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        aboutTButton.setMaximumSize(new java.awt.Dimension(30, 30));
        aboutTButton.setMinimumSize(new java.awt.Dimension(30, 30));
        aboutTButton.setPreferredSize(new java.awt.Dimension(30, 30));
        aboutTButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        aboutTButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutTButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(aboutTButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 507, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jDesktopPane1.setFocusCycleRoot(false);
        jDesktopPane1.setPreferredSize(new java.awt.Dimension(800, 500));
        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        fileMenu.setText("Archivo");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("Nueva imagen");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText("Abrir...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Guardar imagen como...");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);
        fileMenu.add(jSeparator3);
        fileMenu.add(jSeparator4);

        exitMenuItem.setText("Salir");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        toolsMenu.setText("Herramientas");

        eraseMenuItem.setText("Limpiar Lienzo");
        eraseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eraseMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(eraseMenuItem);

        jMenuBar1.add(toolsMenu);

        helpMenu.setText("Ayuda");

        helpMenuItem.setText("Ayuda");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        aboutMenuItem.setText("Acerca de...");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        // TODO add your handling code here:
        New();               
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        // TODO add your handling code here:
        Open();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void eraseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eraseMenuItemActionPerformed
        // TODO add your handling code here:
        if(selectedWindow != null)
            ((Lienzo)selectedWindow.GetContentReference()).LimpiarLienzo();
    }//GEN-LAST:event_eraseMenuItemActionPerformed

    private void openTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTButtonActionPerformed
        // TODO add your handling code here:
        Open();
    }//GEN-LAST:event_openTButtonActionPerformed

    private void newTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTButtonActionPerformed
        // TODO add your handling code here:
        New();
    }//GEN-LAST:event_newTButtonActionPerformed

    private void saveTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTButtonActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_saveTButtonActionPerformed

    private void aboutTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutTButtonActionPerformed
        // TODO add your handling code here:
        About();
    }//GEN-LAST:event_aboutTButtonActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
        About();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_formMouseMoved

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        // TODO add your handling code here:
        Help();
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void helpTButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpTButtonActionPerformed
        // TODO add your handling code here
        Help();
    }//GEN-LAST:event_helpTButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton aboutTButton;
    private javax.swing.JLabel barraEstado;
    private javax.swing.JMenuItem eraseMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JButton helpTButton;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JButton newTButton;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JButton openTButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JButton saveTButton;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables
}
