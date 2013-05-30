/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smpaint_3;

import java.io.File;
import javax.media.*;

/**
 * Clase base de reproductor multimedie
 * @author Nikolai
 */
public class MPlayer extends javax.swing.JPanel{
    //Variables
    /**
     * Reproductor multimedia
     */
    protected Player p;
    
    //Constructores
    /**
     * Inicializa el reproductor
     * @param padre MainWindow
     * @param fl Fichero multimedia
     * @param r true si es sonido, false si es vídeo
     */
    public MPlayer(javax.swing.JFrame padre, File fl, boolean r) {
        File f = fl;    
        padre = null;
        if(r) { //Sonido
            try {
                MediaLocator ml = new MediaLocator(f.toURI().toURL());
                p = Manager.createRealizedPlayer(ml); 

            } catch(Exception e) {};
        }
        else { //Video
            try {
                MediaLocator ml = new MediaLocator(f.toURI().toURL());
                p = Manager.createPlayer(ml);
            } catch(Exception e) {};
        }
    }    
    
    /**
     * Clase manejador de eventos del reproductor
     */
    public class MyMediaManager extends ControllerAdapter {
        //Variables
        private javax.swing.JPanel panelContr, panelWind;
        
        //Constructores
        /**
         * Inicializa el manejador
         * @param p1 JPanel donde se ubicarán los controles de reproducción
         * @param p2 JPanel donde se ubicará la ventana de vídeo
         */
        public MyMediaManager(javax.swing.JPanel p1, javax.swing.JPanel p2) {
            panelContr = p1;
            panelWind = p2;
        }
        
        /**
         * Se dispara al realizarse el reproductor
         * @param e Información del evento
         */
        @Override
    public void realizeComplete (RealizeCompleteEvent e){
            if(p.getControlPanelComponent() != null) {
                panelContr.add(p.getControlPanelComponent(), java.awt.BorderLayout.SOUTH);
                panelContr.revalidate();
            }                
            if(p.getVisualComponent()!= null) {
                panelWind.add(p.getVisualComponent(),java.awt.BorderLayout.CENTER);
                panelWind.revalidate();
            }                
    }
}
}