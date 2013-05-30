/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smpaint_3;

import java.awt.Font;
import java.awt.Point;

/**
 * Clase de aydua para almacenar los atributos asociados al texo.
 * @author Nikolai
 */
public class Text {
    //Variables
    private String text;
    private Point pos;
    private Font font;
    
    //Constructores
    /**
     * Inicializa el objeto
     * @param s Texto
     * @param p Posción
     * @param f Fuente
     */
    public Text(String s, Point p, Font f){
        text = s;
        pos = p;
        font = f;
    }
    
    //Métodos
    /**
     * Devuelve el texto
     * @return Texto
     */
    public String GetString(){
        return text;
    }
    /**
     * Devuelve la posición
     * @return Posición
     */
    public Point GetPos(){
        return pos;
    }
    /**
     * Devuelve la fuente
     * @return Fuente
     */
    public Font GetFont(){
        return font;
    }    
}
