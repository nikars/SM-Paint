/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smpaint_3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;

/**
 * Agrupa e inicializa los paneles que aparecerán dentro de los InternalFrames
 * @author nikolai
 */
public class MyInternalFrame extends JInternalFrame {
    //Variables
    static int newImageCount = 0;
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30; //Para que las nuevas ventanas no aparezcan encima de otras
    private javax.swing.JScrollPane scrollPane;
    private Lienzo lienzo;
    private ToolBox toolbox;
    private AudioPlayer aplayer;
    private VideoPlayer vplayer;
    private Attribs attribs;
    private ImageOpers opers;
    private Object reference;
    private boolean type; //true para ventanas de imagen, false para otras
 
    //Constructores
    /**
     * Imagen nueva
     * @param padre MainWindow
     * @param d Dimensión del lienzo
     */
    public MyInternalFrame(javax.swing.JFrame padre, Dimension d) { 
        super("Nueva #" + (++newImageCount),
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    --openFrameCount;
                }
            });

        lienzo = new Lienzo(padre, d);
        init(lienzo);
        type = true;
   }
    
     /**
     * Abrir una imagen del disco
     * @param padre MainWindow
     * @param img Imágen abierta
     * @param s Nombre del archivo de la imagen
     */
    public MyInternalFrame(javax.swing.JFrame padre,
                            BufferedImage img, String s) {
        super(s,
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable
        
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    --openFrameCount;
                }
            });        
        
        lienzo = new Lienzo(padre, img);
        init(lienzo);
        type = true;
    }
    
    /**
     * Abrir un Sonido o video
     * @param padre MainWindow
     * @param f Fichero abierto
     * @param s true si es un sonido, false si es un vídeo
     */
    public MyInternalFrame(javax.swing.JFrame padre, File f, boolean s) {
        super(f.getName(),
              false, //resizable
              true, //closable
              false, //maximizable
              true);//iconifiable
        
        if(s) { //Sonido
            this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    aplayer.Kill();
                    --openFrameCount;
            }
            });
            
            
            setLayout(new java.awt.BorderLayout());  
            aplayer = new AudioPlayer(padre, f);
            setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
            reference = (Object)aplayer;
            setSize(250,120);
            getContentPane().add(aplayer, BorderLayout.CENTER);
            ++openFrameCount;            
        }
        else { //Video
            this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    vplayer.Kill();
                    --openFrameCount;
                }
            });
            
            setLayout(new java.awt.BorderLayout());  
            vplayer = new VideoPlayer(padre, f);
            setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
            reference = (Object)vplayer;
            setSize(640,480);
            getContentPane().add(vplayer, BorderLayout.CENTER);
            ++openFrameCount;            
        }
        type = false;
    }
    
    /**
     * Paneles de herramientas
     * @param padre MainWindow
     * @param tipo 0:Herramientas, 1:Atributos, 2:Operaciónes
     */
    public MyInternalFrame(javax.swing.JFrame padre, int tipo) { //
        super("", false, //resizable
              false, //closable
              false, //maximizable
              true);//iconifiable

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());
    
        switch(tipo){
            //Herramientas de dibujo
            case 0: {
                toolbox = new ToolBox(padre);
                reference = (Object)toolbox;
                setSize(153,275);
                setTitle("Herramientas de Dibujo");
                setLocation(padre.getSize().width-168, 0);     
                getContentPane().add(toolbox, BorderLayout.CENTER);
            }
                break;
            //Atributos    
            case 1:{
                attribs = new Attribs(padre);
                reference = (Object)attribs;
                setSize(153,328);
                setTitle("Atributos de Dibujo");
                setLocation(padre.getSize().width-168, padre.getSize().height-435);     
                getContentPane().add(attribs, BorderLayout.CENTER); 
            }
                break;
            //Operaciónes    
            case 2:{
                opers = new ImageOpers(padre);
                reference = (Object)opers;
                setSize(300,180);
                setTitle("Operaciones sobre Imagenes");
                setLocation(padre.getSize().width-475, padre.getSize().height-298);     
                getContentPane().add(opers, BorderLayout.CENTER); 
            }
                break;
        }
        type = false;
    }
    
    //Metodos
    private void init(Lienzo lienzo){ 
        setLayout(new java.awt.BorderLayout());
        reference = (Object)lienzo;
        setSize(600,400); //Modificar para cambiar el tamaño por defecto
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);  
        scrollPane = new javax.swing.JScrollPane();
        scrollPane.getViewport().add(lienzo);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        ++openFrameCount;
    }
        
    /**
     * Devuelve la referencia al objeto contenido
     * @return
     */
    public Object GetContentReference(){
        return reference;   
    }
    /**
     * Devuelve el tipo de la ventana (Imagen o no)
     * @return true si es de imagen, false si es otra cosa
     */
    public boolean GetType() {
        return type;
    }
}