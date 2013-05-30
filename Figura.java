/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smpaint_3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Shape;

/**
 * Clase que almacena la figura (Shape) con todos sus atributos
 * @author Nikolai
 */
public class Figura {
    //Variables
    private Shape shape;
    private Text text;
    private Color color;
    private GradientPaint pattern;
    private BasicStroke stroke;
    private boolean fill, grad, gradDir; //false para el degradado horizontal, true para el vertical
    private float alfa;
    
    //Constructores
    /**
     * Constructor para formas simples (no rellenables)
     * @param s Forma
     * @param c Color
     * @param st Trazo
     * @param a Alfa (Opacidad)
     */
    public Figura(Shape s, Color c, BasicStroke st, float a){
        text = null;
        fill = false;
        grad = false;
        gradDir = false;
        pattern = null;
        shape = s;
        color = c;
        alfa = a;
        stroke = st;
    }
    /**
     * Constructor para formas complejas (rellenables)
     * @param s Forma
     * @param c Color
     * @param st Trazo
     * @param a Alfa (Opacidad)
     * @param f Relleno
     * @param g Degradado
     * @param d 
     * @param p Patron de degradado
     */
    public Figura(Shape s, Color c, BasicStroke st, float a, boolean f, boolean g, boolean d, GradientPaint p){
        text = null;
        shape = s;
        color = c;
        alfa = a;
        fill = f;
        grad = g;
        gradDir = d;
        pattern = p;
        stroke = st;
    }
    
    /**
     * Constructor para texto
     * @param t Texto
     * @param c Color
     * @param a Alfa (Opacidad)
     */
    public Figura (Text t, Color c, float a) {
        shape = null;
        fill = false;
        grad = false;
        pattern = null;
        stroke = null;
        text = t;
        color = c;
        alfa = a;
    }
    
    //Métodos
    /**
     * Devuelve la figura
     * @return Figura
     */
    public Shape GetShape() {
        return shape;
    }
    
    /**
     * Devuelve el color
     * @return Color
     */
    public Color GetColor() {
        return color;
    }
    
     /**
     * Establece el color
     * @param c Color
     */
    public void SetColor(Color c) {
        color = c;
    }
    
     /**
     * Establece el color secundario
     * @param c Color secundario
     */
    public void SetColorSec(Color c) {
        if (pattern != null) {
            pattern = new GradientPaint(pattern.getPoint1(), color, pattern.getPoint2(), c);
        }       
    }
    
     /**
     * Establece el relleno
     * @param b true - se rellena, false - no se rellena
     */
    public void SetFill(boolean b) {
        fill = b;
    }
    
    /**
     * Establece el degradado
     * @param b true para el degradado, false para color liso
     */
    public void SetGrad(boolean b) {
        grad = b;
    }
    
     /**
     * Establece el trazo
     * @param b Trazo
     */
    public void SetStroke(BasicStroke b) {
        stroke = b;
    }
    
     /**
     * Establece la opacidad
     * @param a Opacidad (Alfa)
     */
    public void SetAlfa(float a) {
        alfa = a;
    }
    
    /**
     * Devuelve el relleno
     * @return true si hay que rellenar
     */
    public boolean GetFill() {
        return fill;
    }
    
     /**
     * Devuelve la dirección del degradado
     * @return true para el vertical, false para el horizontal
     */
    public boolean GetGradDir() {
        return gradDir;
    }
    
    /**
     * Devuelve el degradado
     * @return true si hay que rellenar con el degradado
     */
    public boolean GetGradient() {
        return grad;
    }
    
    /**
     * Devuelve el patron de degradado
     * @return Patron de degradado
     */
    public GradientPaint GetGradientPattern() {
        return pattern;
    }
    
    /**
     * Devuelve el trazo
     * @return Trazo
     */
    public BasicStroke GetStroke() {
        return stroke;
    }
    
     /**
     * Asigna el patron de degradado
     * @param g 
     */
    public void SetGradientPattern(GradientPaint g) {
        pattern = g;
    }
    
    /**
     * Devuelve la opacidad
     * @return Alfa
     */
    public float GetAlfa() {
        return alfa;
    }

    /**
     * Devuelve el texto
     * @return Texto
     */
    public Text GetText() {
        return text;
    }
}
