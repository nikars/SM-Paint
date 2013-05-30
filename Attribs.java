/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Attribs.java
 * 
 * Created on 15-ene-2012, 23:28:53
 */
package smpaint_3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Vector;
import javax.swing.JColorChooser;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * Panel de atributos de las figuras / texto
 * @author Nikolai
 */
public class Attribs extends javax.swing.JPanel {
    //Variables
    private Color colorActual, colorSec;
    private MainWindow father;
    private int grosor, trazo;
    private float transp;
    private boolean relleno, degradado, dir, edit;
    private double zoom;
    private Fuente fuente;    
    private Lienzo imagenActual;
    
    //Constructores
    /**
     * Inicializa el panel
     * @param padre Ventana principal
     */
    public Attribs(javax.swing.JFrame padre) {
        initComponents();
        
        father = (MainWindow)padre;
        colorActual = Color.black;
        colorSec = Color.white;
        trazo = 0;
        grosor = 2;
        relleno = false;
        degradado = false;
        dir = true; //Vertical
        transp = 1.0f; //Opaco 
        zoom = 1.0;
        edit = false;
        
        horRadioButton.setVisible(false);
        vertRadioButton.setVisible(false);
        fillComboBox.setEnabled(false);
      
        
        mainColorPanel.setBackground(colorActual);
        secColorPanel.setBackground(colorSec);
        fuente = new Fuente ("Monospaced");       
    }
   
    
    //Métodos
    private void ChColor() {        
            Figura f;
            imagenActual = ((Lienzo)father.GetSelectedWindow().GetContentReference());
            f = imagenActual.GetEditTarget();
            if(f != null) {
                f.SetColor(colorActual);                
                imagenActual.ApplyEdit(f);
            }
    }
    
    private void ChStroke() {
        Figura f;
        imagenActual = ((Lienzo)father.GetSelectedWindow().GetContentReference());
        f = imagenActual.GetEditTarget();
         
        if(f != null) {
            BasicStroke strk;        
            if(trazo == 0) {
                //Continuo
                strk = new BasicStroke(grosor);
            }
            else {
                //Punteado
                float dash1[] = {grosor};
                strk =  new BasicStroke(grosor,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            1.0f, dash1, 0.0f);
            }
            f.SetStroke(strk);
            imagenActual.ApplyEdit(f);
        }
    }
    /**
     * Devuelve la cantidad del zoom
     * @return Zoom (factor)
     */
    public double GetZoom(){
        return zoom;
    }
    
    /**
     * Devuelve la fuente seleccionada
     * @return Fuente
     */
    public Fuente GetFuente(){
        return fuente;
    }
    
    /**
     * Devuelve el color seleccionado
     * @return Color
     */
    public Color GetColor(){
        return colorActual;
    }
    
    /**
     * Devuelve el color secundario seleccionado 
     * @return Color secundario
     */
    public Color GetColorSec(){
        return colorSec;
    }
    
    /**
     * Devuelve el grosor del trazo seleccionado 
     * @return Grosor
     */
    public int GetGrosor(){
        return grosor;
    }
    
    /**
     *  Devuelve el tipo de trazo seleccionado
     * @return Tipo de trazo
     */
    public int GetTrazo(){
        return trazo;
    }
    
    /**
     * Devuelve la opacidad seleccionada
     * @return Alfa
     */
    public float GetTransp(){
        return transp;
    }
    
    /**
     * Relleno de la figura
     * @return true si hay que rellenar, false si no
     */
    public boolean GetRelleno(){
        return relleno;
    }
    
    /**
     * Degradado del relleno
     * @return true si se rellena con el degradado, false con un color liso
     */
    public boolean GetDegradado(){
        return degradado;
    }
    
        /**
     * Dirección del degradado
     * @return true para la vertical, false para la horizontal
     */
    public boolean GetDir(){
        return dir;
    } 
    
    /**
     * Oculta el combBox relleno
     */
    public void HideFill()
    {
        fillComboBox.setSelectedIndex(0);
        fillComboBox.setEnabled(false);
        vertRadioButton.setVisible(false);
        horRadioButton.setVisible(false);
    }
    
    /**
     * Muestra el comboBox relleno
     */
    public void ShowFill()
    {
        fillComboBox.setEnabled(true);
    }
    
    /**
     * Oculta el comboBox tipo de trazo
     */
    public void HideStroke()
    {
        typeLabel.setEnabled(false);
        strokeComboBox.setEnabled(false);
    }
    
    /**
     * Muestra el comboBox tipo de trazo
     */
    public void ShowStroke()
    {
        typeLabel.setEnabled(true);
        strokeComboBox.setEnabled(true);
    }
    
    /**
     * Oculta el spinner grosor
     */
    public void HideGrosor()
    {
        sizeLabel.setEnabled(false);
        sizeSpinner.setEnabled(false);
    }
    
    /**
     * Muestra el spinner grosor
     */
    public void ShowGrosor()
    {
        sizeLabel.setEnabled(true);
        sizeSpinner.setEnabled(true);
    }
    
     /**
     * Establece el modo
     * @param m true para la edición, false para el modo normal
     */
    public void SetMode(boolean m)
    {
        edit = m;
        if (edit) {
            vertRadioButton.setVisible(false);
            horRadioButton.setVisible(false);
            
            //Esto no está del todo bien, deberían de mostrarse los campos en función de la forma seleccionada
            //Por ejemplo, si está seleccionada una línea e intentamos rellenarla, desaparece
            //Habria que almacenar en la clase figura el tipo de cada forma al crearla...       
            fillComboBox.setEnabled(true);
            strokeComboBox.setEnabled(true);
            typeLabel.setEnabled(true);
            sizeLabel.setEnabled(true);
            sizeSpinner.setEnabled(true);
            
        }
        else {
            fillComboBox.setEnabled(false);
            strokeComboBox.setEnabled(true);
            typeLabel.setEnabled(true);
            sizeLabel.setEnabled(true);
            sizeSpinner.setEnabled(true);
        }
            
    }
    
     /**
     * Devuelve el modo
     * @return true si estamos en el modo de edición de figuras
     */
    public boolean EditMode()
    {
        return edit;
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

        fstyleGroup = new javax.swing.ButtonGroup();
        gradDirGroup = new javax.swing.ButtonGroup();
        attribsPane = new javax.swing.JTabbedPane();
        strokePanel = new javax.swing.JPanel();
        sizeLabel = new javax.swing.JLabel();
        SpinnerModel modelpos = new SpinnerNumberModel(2.0, 1.0, 99.0, 1.0);
        sizeSpinner = new javax.swing.JSpinner(modelpos);
        opacityLabel = new javax.swing.JLabel();
        transpSlider = new javax.swing.JSlider();
        strokeComboBox = new javax.swing.JComboBox();
        typeLabel = new javax.swing.JLabel();
        vertRadioButton = new javax.swing.JRadioButton();
        horRadioButton = new javax.swing.JRadioButton();
        fillComboBox = new javax.swing.JComboBox();
        textPanel = new javax.swing.JPanel();
        fontLabel = new javax.swing.JLabel();
        String[] fontNames = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames();

        //... Make vector of all fonts that can display basic chars.
        //    Vector (not newer ArrayList) is used by JComboBox.
        Vector<String> visFonts = new Vector<String>(fontNames.length);
        for (String fontName : fontNames) {
            Font f = new Font(fontName, Font.PLAIN, 12);
            if (f.canDisplay('a')) {
                //... Display only fonts that have the alphabetic characters.
                visFonts.add(fontName);
            } else {
                //    On my machine there are almost 20 fonts (eg, Wingdings)
                //    that don't display text.
                //System.out.println("No alphabetics in " + fontName);
            }
        }
        fontComboBox = new javax.swing.JComboBox(visFonts);
        SpinnerModel modelsize = new SpinnerNumberModel(14, 1, 99, 1);
        fsizeSpinner = new javax.swing.JSpinner(modelsize);
        fsizeLabel = new javax.swing.JLabel();
        plainRadioButton = new javax.swing.JRadioButton();
        boldRadioButton = new javax.swing.JRadioButton();
        italicRadioButton = new javax.swing.JRadioButton();
        bolditalicRadioButton = new javax.swing.JRadioButton();
        colorPanel = new javax.swing.JPanel();
        mainColorPanel = new javax.swing.JPanel();
        secColorPanel = new javax.swing.JPanel();
        moreButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        blackButton = new javax.swing.JButton();
        redButton = new javax.swing.JButton();
        greenButton = new javax.swing.JButton();
        blueButton = new javax.swing.JButton();
        yellowButton = new javax.swing.JButton();
        magentaButton = new javax.swing.JButton();
        cyanButton = new javax.swing.JButton();
        whiteButton = new javax.swing.JButton();
        flipButton = new javax.swing.JButton();
        plusButton = new javax.swing.JButton();
        minusButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(51, 51, 51));

        strokePanel.setBackground(new java.awt.Color(51, 51, 51));

        sizeLabel.setForeground(new java.awt.Color(255, 255, 255));
        sizeLabel.setText("Grosor");

        sizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sizeSpinnerStateChanged(evt);
            }
        });

        opacityLabel.setForeground(new java.awt.Color(255, 255, 255));
        opacityLabel.setText("Opacidad");

        transpSlider.setValue(100);
        transpSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transpSliderStateChanged(evt);
            }
        });

        strokeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Continuo", "Punteado" }));
        strokeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strokeComboBoxActionPerformed(evt);
            }
        });

        typeLabel.setForeground(new java.awt.Color(255, 255, 255));
        typeLabel.setText("Tipo del trazo");
        typeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        gradDirGroup.add(vertRadioButton);
        vertRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        vertRadioButton.setSelected(true);
        vertRadioButton.setText("Vert.");
        vertRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vertRadioButtonActionPerformed(evt);
            }
        });

        gradDirGroup.add(horRadioButton);
        horRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        horRadioButton.setText("Horiz.");
        horRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                horRadioButtonActionPerformed(evt);
            }
        });

        fillComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sin relleno", "Color liso", "Degradado" }));
        fillComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout strokePanelLayout = new javax.swing.GroupLayout(strokePanel);
        strokePanel.setLayout(strokePanelLayout);
        strokePanelLayout.setHorizontalGroup(
            strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(strokePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, strokePanelLayout.createSequentialGroup()
                        .addComponent(vertRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 50, Short.MAX_VALUE)
                        .addComponent(horRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(strokePanelLayout.createSequentialGroup()
                        .addGroup(strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(typeLabel)
                            .addGroup(strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(strokeComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(transpSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, strokePanelLayout.createSequentialGroup()
                                    .addComponent(sizeLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(opacityLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fillComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 126, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        strokePanelLayout.setVerticalGroup(
            strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, strokePanelLayout.createSequentialGroup()
                .addComponent(typeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(strokeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opacityLabel)
                .addGap(1, 1, 1)
                .addComponent(transpSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sizeLabel)
                    .addComponent(sizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fillComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(strokePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vertRadioButton)
                    .addComponent(horRadioButton))
                .addGap(63, 63, 63))
        );

        attribsPane.addTab("Trazo", strokePanel);

        textPanel.setBackground(new java.awt.Color(51, 51, 51));

        fontLabel.setForeground(new java.awt.Color(255, 255, 255));
        fontLabel.setText("Fuente");

        fontComboBox.setSelectedItem("Monospaced");
        fontComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontComboBoxActionPerformed(evt);
            }
        });

        fsizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fsizeSpinnerStateChanged(evt);
            }
        });

        fsizeLabel.setForeground(new java.awt.Color(255, 255, 255));
        fsizeLabel.setText("Tamaño");

        plainRadioButton.setBackground(new java.awt.Color(51, 51, 51));
        fstyleGroup.add(plainRadioButton);
        plainRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        plainRadioButton.setSelected(true);
        plainRadioButton.setText("Normal");
        plainRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                plainRadioButtonStateChanged(evt);
            }
        });

        boldRadioButton.setBackground(new java.awt.Color(51, 51, 51));
        fstyleGroup.add(boldRadioButton);
        boldRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        boldRadioButton.setText("Negrita");
        boldRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                boldRadioButtonStateChanged(evt);
            }
        });

        italicRadioButton.setBackground(new java.awt.Color(51, 51, 51));
        fstyleGroup.add(italicRadioButton);
        italicRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        italicRadioButton.setText("Cursiva");
        italicRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                italicRadioButtonStateChanged(evt);
            }
        });

        bolditalicRadioButton.setBackground(new java.awt.Color(51, 51, 51));
        fstyleGroup.add(bolditalicRadioButton);
        bolditalicRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        bolditalicRadioButton.setText("N+C");
        bolditalicRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bolditalicRadioButtonStateChanged(evt);
            }
        });

        javax.swing.GroupLayout textPanelLayout = new javax.swing.GroupLayout(textPanel);
        textPanel.setLayout(textPanelLayout);
        textPanelLayout.setHorizontalGroup(
            textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fontComboBox, 0, 163, Short.MAX_VALUE)
                    .addComponent(fontLabel)
                    .addGroup(textPanelLayout.createSequentialGroup()
                        .addComponent(fsizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fsizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(textPanelLayout.createSequentialGroup()
                        .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(plainRadioButton)
                            .addComponent(italicRadioButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bolditalicRadioButton)
                            .addComponent(boldRadioButton))))
                .addContainerGap())
        );
        textPanelLayout.setVerticalGroup(
            textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fontLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fsizeLabel)
                    .addComponent(fsizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plainRadioButton)
                    .addComponent(boldRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(italicRadioButton)
                    .addComponent(bolditalicRadioButton))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        attribsPane.addTab("Texto", textPanel);

        colorPanel.setBackground(new java.awt.Color(51, 51, 51));
        colorPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainColorPanel.setBackground(new java.awt.Color(255, 153, 153));
        mainColorPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        mainColorPanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout mainColorPanelLayout = new javax.swing.GroupLayout(mainColorPanel);
        mainColorPanel.setLayout(mainColorPanelLayout);
        mainColorPanelLayout.setHorizontalGroup(
            mainColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
        mainColorPanelLayout.setVerticalGroup(
            mainColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        colorPanel.add(mainColorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 50, 50));

        secColorPanel.setBackground(new java.awt.Color(153, 153, 255));
        secColorPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        secColorPanel.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout secColorPanelLayout = new javax.swing.GroupLayout(secColorPanel);
        secColorPanel.setLayout(secColorPanelLayout);
        secColorPanelLayout.setHorizontalGroup(
            secColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
        secColorPanelLayout.setVerticalGroup(
            secColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        colorPanel.add(secColorPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 50, 50));

        moreButton.setBackground(new java.awt.Color(102, 102, 102));
        moreButton.setForeground(new java.awt.Color(255, 255, 255));
        moreButton.setText("MásColores...");
        moreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreButtonActionPerformed(evt);
            }
        });
        colorPanel.add(moreButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 120, -1));

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        blackButton.setBackground(new java.awt.Color(0, 0, 0));
        blackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blackButtonActionPerformed(evt);
            }
        });

        redButton.setBackground(new java.awt.Color(255, 0, 0));
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });

        greenButton.setBackground(new java.awt.Color(0, 255, 0));
        greenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenButtonActionPerformed(evt);
            }
        });

        blueButton.setBackground(new java.awt.Color(0, 0, 255));
        blueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButtonActionPerformed(evt);
            }
        });

        yellowButton.setBackground(new java.awt.Color(255, 255, 0));
        yellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yellowButtonActionPerformed(evt);
            }
        });

        magentaButton.setBackground(new java.awt.Color(255, 0, 255));
        magentaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                magentaButtonActionPerformed(evt);
            }
        });

        cyanButton.setBackground(new java.awt.Color(0, 255, 255));
        cyanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cyanButtonActionPerformed(evt);
            }
        });

        whiteButton.setBackground(new java.awt.Color(255, 255, 255));
        whiteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whiteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(blackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(redButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(greenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(yellowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(magentaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(cyanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addGap(72, 72, 72))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(whiteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(108, 108, 108)))
                .addGap(22, 22, 22))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(blackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(whiteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(redButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cyanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(greenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(magentaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(blueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yellowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        colorPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 70));

        flipButton.setBackground(new java.awt.Color(102, 102, 102));
        flipButton.setForeground(new java.awt.Color(255, 255, 255));
        flipButton.setText(">");
        flipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flipButtonActionPerformed(evt);
            }
        });
        colorPanel.add(flipButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 40, 50));

        attribsPane.addTab("Color", colorPanel);

        plusButton.setBackground(new java.awt.Color(153, 153, 153));
        plusButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/plus.png"))); // NOI18N
        plusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusButtonActionPerformed(evt);
            }
        });

        minusButton.setBackground(new java.awt.Color(153, 153, 153));
        minusButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smpaint_3/minus.png"))); // NOI18N
        minusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(plusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(minusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(attribsPane, 0, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(attribsPane, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(plusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minusButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sizeSpinnerStateChanged
        // TODO add your handling code here:
        double val = (Double)sizeSpinner.getValue();
        grosor=(int)val;
        
        if(father.GetSelectedWindow() != null && edit) {
            ChStroke();
        }  
    }//GEN-LAST:event_sizeSpinnerStateChanged

    private void minusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusButtonActionPerformed
        // TODO add your handling code here:
        if(father.GetSelectedWindow() != null) {
            imagenActual = ((Lienzo)father.GetSelectedWindow().GetContentReference());
            zoom-=0.1;
            imagenActual.repaint();
        }
    }//GEN-LAST:event_minusButtonActionPerformed

    private void transpSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_transpSliderStateChanged
        // TODO add your handling code here:
        transp = transpSlider.getValue()*0.01f;
        
        if(father.GetSelectedWindow() != null && edit) {
            Figura f;
            imagenActual = ((Lienzo)father.GetSelectedWindow().GetContentReference());
            f = imagenActual.GetEditTarget();
            if(f != null) {
                f.SetAlfa(transp);
                imagenActual.ApplyEdit(f);
            }
        }
    }//GEN-LAST:event_transpSliderStateChanged

    private void strokeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strokeComboBoxActionPerformed
        // TODO add your handling code here:
        if (strokeComboBox.getSelectedItem() == "Continuo") trazo=0;
        if (strokeComboBox.getSelectedItem() == "Punteado") trazo=1;
        
        if(father.GetSelectedWindow() != null && edit) {
            ChStroke();
            }        
    }//GEN-LAST:event_strokeComboBoxActionPerformed

    private void blackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blackButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.black;
        mainColorPanel.setBackground(colorActual);
        
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_blackButtonActionPerformed

    private void whiteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whiteButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.white;
        mainColorPanel.setBackground(colorActual);
        
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_whiteButtonActionPerformed

    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.red;
        mainColorPanel.setBackground(colorActual);
                
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_redButtonActionPerformed

    private void greenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_greenButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.green;
        mainColorPanel.setBackground(colorActual);
        
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_greenButtonActionPerformed

    private void magentaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_magentaButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.magenta;
        mainColorPanel.setBackground(colorActual);
                
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_magentaButtonActionPerformed

    private void blueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.blue;
        mainColorPanel.setBackground(colorActual);
                
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_blueButtonActionPerformed

    private void yellowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yellowButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.yellow;
        mainColorPanel.setBackground(colorActual);
                
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_yellowButtonActionPerformed

    private void cyanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cyanButtonActionPerformed
        // TODO add your handling code here:
        colorActual = Color.cyan;
        mainColorPanel.setBackground(colorActual);
                
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        }        
    }//GEN-LAST:event_cyanButtonActionPerformed

    private void flipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flipButtonActionPerformed
        // TODO add your handling code here:
        colorSec = colorActual;
        secColorPanel.setBackground(colorSec);
    }//GEN-LAST:event_flipButtonActionPerformed

    private void moreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreButtonActionPerformed
        // TODO add your handling code here:
        colorActual = JColorChooser.showDialog(null, "Paleta de colores", colorActual);
        mainColorPanel.setBackground(colorActual);
                
        if(father.GetSelectedWindow() != null && edit) {
            ChColor();
        } 
    }//GEN-LAST:event_moreButtonActionPerformed

    private void plusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusButtonActionPerformed
        // TODO add your handling code here:
        if((father.GetSelectedWindow() != null)) {
            imagenActual = ((Lienzo)father.GetSelectedWindow().GetContentReference());
            zoom+=0.1;
            imagenActual.repaint();
        }
    }//GEN-LAST:event_plusButtonActionPerformed

    private void fsizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fsizeSpinnerStateChanged
        // TODO add your handling code here:
        fuente.SetSize((Integer)fsizeSpinner.getValue());
    }//GEN-LAST:event_fsizeSpinnerStateChanged

    private void fontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontComboBoxActionPerformed
        // TODO add your handling code here:
        fuente.SetName((String)fontComboBox.getSelectedItem());
    }//GEN-LAST:event_fontComboBoxActionPerformed

    private void plainRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_plainRadioButtonStateChanged
        // TODO add your handling code here:
        StyleSet();
    }//GEN-LAST:event_plainRadioButtonStateChanged

    private void boldRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_boldRadioButtonStateChanged
        // TODO add your handling code here:
        StyleSet();
    }//GEN-LAST:event_boldRadioButtonStateChanged

    private void italicRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_italicRadioButtonStateChanged
        // TODO add your handling code here:
        StyleSet();
    }//GEN-LAST:event_italicRadioButtonStateChanged

    private void bolditalicRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bolditalicRadioButtonStateChanged
        // TODO add your handling code here:
        StyleSet();
    }//GEN-LAST:event_bolditalicRadioButtonStateChanged

    private void fillComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillComboBoxActionPerformed
        // TODO add your handling code here:
        if(fillComboBox.getSelectedItem() == "Degradado") {
            relleno = true;
            degradado = true;
            vertRadioButton.setVisible(true);
            horRadioButton.setVisible(true);
        }
        else if (fillComboBox.getSelectedItem() == "Color liso") {
            relleno = true;
            degradado = false;
            vertRadioButton.setVisible(false);
            horRadioButton.setVisible(false);
        }
        else {
            relleno = false;
            degradado = false;
        }
        
        if(father.GetSelectedWindow() != null && edit) {
            Figura f;
            imagenActual = ((Lienzo)father.GetSelectedWindow().GetContentReference());
            f = imagenActual.GetEditTarget();
            if(f != null) {
                f.SetFill(relleno);                
                f.SetGrad(degradado);
                f.SetColor(colorActual);    
                f.SetColorSec(colorSec);
                imagenActual.ApplyEdit(f);
            }
        }        
    }//GEN-LAST:event_fillComboBoxActionPerformed

    private void vertRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vertRadioButtonActionPerformed
        // TODO add your handling code here:
        if(horRadioButton.isSelected())
            dir = false;
        else if (vertRadioButton.isSelected())
            dir = true;
    }//GEN-LAST:event_vertRadioButtonActionPerformed

    private void horRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_horRadioButtonActionPerformed
        // TODO add your handling code here:
        if(horRadioButton.isSelected())
            dir = false;
        else if (vertRadioButton.isSelected())
            dir = true;
    }//GEN-LAST:event_horRadioButtonActionPerformed

    private void StyleSet(){
        if(plainRadioButton.isSelected())
            fuente.SetStyle(Font.PLAIN);
        else if (boldRadioButton.isSelected())
            fuente.SetStyle(Font.BOLD);
        else if (italicRadioButton.isSelected())
            fuente.SetStyle(Font.ITALIC);
        else fuente.SetStyle(Font.ITALIC+Font.BOLD);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane attribsPane;
    private javax.swing.JButton blackButton;
    private javax.swing.JButton blueButton;
    private javax.swing.JRadioButton boldRadioButton;
    private javax.swing.JRadioButton bolditalicRadioButton;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JButton cyanButton;
    private javax.swing.JComboBox fillComboBox;
    private javax.swing.JButton flipButton;
    private javax.swing.JComboBox fontComboBox;
    private javax.swing.JLabel fontLabel;
    private javax.swing.JLabel fsizeLabel;
    private javax.swing.JSpinner fsizeSpinner;
    private javax.swing.ButtonGroup fstyleGroup;
    private javax.swing.ButtonGroup gradDirGroup;
    private javax.swing.JButton greenButton;
    private javax.swing.JRadioButton horRadioButton;
    private javax.swing.JRadioButton italicRadioButton;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton magentaButton;
    private javax.swing.JPanel mainColorPanel;
    private javax.swing.JButton minusButton;
    private javax.swing.JButton moreButton;
    private javax.swing.JLabel opacityLabel;
    private javax.swing.JRadioButton plainRadioButton;
    private javax.swing.JButton plusButton;
    private javax.swing.JButton redButton;
    private javax.swing.JPanel secColorPanel;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JSpinner sizeSpinner;
    private javax.swing.JComboBox strokeComboBox;
    private javax.swing.JPanel strokePanel;
    private javax.swing.JPanel textPanel;
    private javax.swing.JSlider transpSlider;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JRadioButton vertRadioButton;
    private javax.swing.JButton whiteButton;
    private javax.swing.JButton yellowButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Clase de ayuda que agrupa los atributos básicos de una fuente
     */
    public class Fuente {
        //Variables
        private String name;
        private int size, style;  
        
        //Constructores
        /**
         * Inicializa el objeto
         * @param n Nombre de la fuente
         */
        public Fuente(String n){
            name = n;
            size = 14;
            style = Font.PLAIN;
        }
        
        //Métodos
        /**
         * Asigna el nombre de la fuente
         * @param n Nombre de la fuente
         */
        public void SetName(String n){
            name = n;
        }
        
        /**
         * Asigna el tamaño de la fuente
         * @param s Tamaño de la fuente
         */
        public void SetSize(int s){
            size = s;
        }
        
        /**
         * Asigna el estilo de la fuente
         * @param s Estilo de la fuente
         */
        public void SetStyle(int s){
            style = s;
        }
        
        /**
         * Devuelve el nombre de la fuente
         * @return Nombre de la fuente
         */
        public String GetName(){
            return name;
        }
        
        /**
         * Devuelve el tamaño de la fuente
         * @return Tamaño
         */
        public int GetSize(){
            return size;
        }
        
        /**
         * Devuelve el estilo de la fuente
         * @return Estilo
         */
        public int GetStyle(){
            return style;
        }  
     }
}
