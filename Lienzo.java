/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Lienzo.java
 * 
 * Created on 14-nov-2011, 10:39:50
 */
package smpaint_3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Lienzo para la imagen y las herramientas de dibujo
 * @author Nikolai
 */
public class Lienzo extends javax.swing.JPanel {
    //Variables
    private MainWindow padre;
    private ArrayList<Figura> shapes; //Array de formas
    private boolean dibujando, operando, equilatero, curva, selection;    
    private Point posicionPrevia, posicionActual;
    private BufferedImage bufImg, tempImg, original;    
    private ToolBox toolbox;
    private Attribs attribs;
    Figura editTarget;
    
    //Constructores
    /**
     * Constructor para una imagen nueva
     * 
     * @param padre Ventana principal
     * @param d Dimensión del lienzo
     */
    public Lienzo(javax.swing.JFrame padre, Dimension d){
        initComponents();
        this.padre = (MainWindow)padre;
        init();
        
        this.setPreferredSize(d);
        bufImg = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        clearImage(bufImg, Color.white);
        original = biCopy(bufImg);
    }
    
    /**
     * Constructor para una imagen cargada del disco
     * 
     * @param padre Ventana principal
     * @param img Imagen cargada del disco
     */
    public Lienzo(javax.swing.JFrame padre, BufferedImage img) {
        initComponents();
        this.padre = (MainWindow)padre;
        init();
        
        this.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
        bufImg = img;
        original = biCopy(img);
        tempImg = biCopy(img);
    }
    
    //Metodos
    private void init(){
        setFocusable(true);        
        shapes = new ArrayList<Figura>();        
        dibujando = false;
        curva=false;
        operando = false;
        equilatero = false;
        editTarget = null;
        toolbox = (ToolBox)padre.GetTools().GetContentReference();
        attribs = (Attribs)padre.GetAttribs().GetContentReference();
        selection = false;
        
        //Para capturar el teclado mientras se dibuja
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
    }
    
    private void clearImage(BufferedImage img, Color c) {
        Graphics2D gc = img.createGraphics();
        gc.setColor(c);
        gc.setComposite(makeComposite(1.0f));
        gc.fillRect(0, 0, img.getWidth(), img.getHeight());       
    }
   
    private void Clear(Graphics2D gc){     
        gc.drawImage(original, 0, 0, Color.white, null);
    }
    
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
    
    private BasicStroke Stroke() {
        BasicStroke strk;
        
        switch(attribs.GetTrazo()){            
          //Continuo
          case 0: strk = new BasicStroke(attribs.GetGrosor());
              break;
          //Punteado
          case 1: {
              float dash1[] = {attribs.GetGrosor()};
              strk =  new BasicStroke(attribs.GetGrosor(),
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        1.0f, dash1, 0.0f);
          }
              break;
          default: strk = null;
        }
        return strk;
    }
    
    private void Pintar(Graphics2D g, boolean temp) {        
      //Transparencia
      Composite originalComposite = g.getComposite();
      //Antialiasing
      RenderingHints rh;          
      
      if(temp){ //Se visualiza antes de soltar el ratón
          if(shapes.size() > 0) {
          Figura fig = shapes.get(shapes.size()-1);
          g.setColor(fig.GetColor());          
          g.setComposite(makeComposite(fig.GetAlfa()));
          
          if(fig.GetShape() != null) {
              g.setStroke(fig.GetStroke());
              rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
              g.setRenderingHints(rh);
              
           if(fig.GetFill()){
                if(fig.GetGradient()) {                
                    g.setPaint(fig.GetGradientPattern());
                    g.fill(fig.GetShape());
                }
                else g.fill(fig.GetShape());
            }
            else g.draw(fig.GetShape());
          }
         }
      }
      else { //Al pintar en el bufer
        Clear(g); //Hace falta para mantener la transparencia (si no, se dibuja encima y termina siendo opaco)
        for(Figura fig : shapes) { //Se dibujan todas las formas que están en el array shapes sobre el objeto graphics            
            g.setColor(fig.GetColor());
            g.setComposite(makeComposite(fig.GetAlfa()));

            if(fig.GetShape() != null) {
                g.setStroke(fig.GetStroke());
                rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHints(rh);
                
                if(fig.GetFill()){
                    if(fig.GetGradient()) g.setPaint(fig.GetGradientPattern());
                    g.fill(fig.GetShape());
                }
                else g.draw(fig.GetShape());
            }
            else if (fig.GetText() != null) { //Pintamos el texto                
                rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setRenderingHints(rh);
                g.setFont(fig.GetText().GetFont());
                
                g.drawString(fig.GetText().GetString(), fig.GetText().GetPos().x, fig.GetText().GetPos().y);   
          }
        }
      }                 
      g.setComposite(originalComposite);
    }
    
    /**
     * Método Paint sobrecargado
     * 
     * @param g objeto Graphics sobre el que se dibujará
     */
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2d = (Graphics2D)g;
      
        //Control del zoom
        g2d.scale(attribs.GetZoom(), attribs.GetZoom());
        
        //... Mostramos la imagen guardada en pantalla
        g2d.drawImage(bufImg, 0, 0, null);
        
        //Dibujar el shape seleccionado en la pantalla (pero no en el bufer)
        //Se guardara en el bufer cuando soltemos el raton
        if(dibujando) Pintar(g2d, true);
        
        //Aplicamos las transformaciónes
        if(operando) {
            g2d.drawImage(tempImg, 0, 0, null);
            operando=false;
        }  
        
        //Contorno de la seleccion
        if(selection && (editTarget != null)) {
            g2d.setColor(Color.BLACK);
            g2d.draw(editTarget.GetShape().getBounds2D());
        }
    }
    
    /**
     * Deja el lienzo blanco (Sí era una imágen cargada del disco - también)
     */
    public void LimpiarLienzo(){
        shapes.clear();
        clearImage(bufImg, Color.white);
        original = biCopy(bufImg);
        curva = false;
        repaint();
    }
    
     /**
     * Devuelve la imagen dibujada sobre el lienzo
     * @return La imagen
     */
    public BufferedImage GetImage(){
        return tempImg = biCopy(bufImg);
    }
    
     /**
     * Devuelve figura a modificar
     * @return La figura
     */
    public Figura GetEditTarget(){
        return editTarget;
    }
    
    /**
     * Pinta la imagen sobre el lienzo
     * @param img Imagen fuente
     */
    public void SetImg(BufferedImage img){
        operando=true;
        tempImg = img;
        this.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
        padre.requestFocus();
        repaint();
    }
    
    /**
     * Guarda las operaciónes que se han realizado sobre la imagen definitivamente
     */
    public void ApplyOper(){
        bufImg = biCopy(tempImg);
        original = biCopy(tempImg);
        shapes.clear();
        repaint();
    }
    
     /**
     * Aplica la edición
     * @param f Figura editada
     */
    public void ApplyEdit(Figura f){
        for(Figura fig : shapes) {
            if(fig.equals(editTarget)) {
                fig = f;
            }
        }
        attribs.SetMode(false);
        Pintar(bufImg.createGraphics(), false);
        repaint();
        attribs.SetMode(true);
    }
    
    /**
     * Deshace la ultima operación, restablece la imagen original
     */
    public void RestoreOper() {
        tempImg = biCopy(bufImg);
        repaint();
    }
    
    //Métodos estáticos
    /**
     * Convierte el texto en un objeto shape (Experimental - No se utiliza en esta versión)
     * @param font Fuente
     * @param string Texto
     * @return Shape a partir del texto
     */
    public static Shape generateShapeFromText(Font font, String string) {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        try {
        GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), string);
        Shape shape = vect.getOutline(0f, (float) -vect.getVisualBounds().getY());

        return shape;
        } finally {g2.dispose();}
    }
    
    /**
     * Clona un BufferedImage
     * @param bi Imagen fuente
     * @return Copia de la imagen
     */
    static BufferedImage biCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
     /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:      
        posicionActual = evt.getPoint();
        posicionPrevia = posicionActual;
        repaint();
        
        if(attribs.EditMode()) //Estamos en el modo de edición de figuras
        {
            editTarget = null;
            for(Figura fig : shapes) {
                if(fig.GetShape() != null) { //Está a null en los textos
                    if (fig.GetShape().contains(posicionActual))
                        editTarget = fig;
                    else if(fig.GetShape().intersects(posicionActual.x-2, posicionActual.y-2, 4, 4))
                        editTarget = fig;
                }
            }
            if(editTarget != null) {
                selection = true;
                repaint();
            } 
        }
        else {      
            selection = false;
            switch(toolbox.GetActiveTool()){
                //Línea recta
                case 0: {
                    if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    shapes.add(new Figura(new Line2D.Double(posicionActual, posicionActual),
                            attribs.GetColor(), Stroke(), attribs.GetTransp()));

                }
                    break;
                //Trazo libre
                case 1: {
                    if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    shapes.add(new Figura(new GeneralPath(), attribs.GetColor(), Stroke(), attribs.GetTransp(), false, false, false, null));
                    ((GeneralPath)shapes.get(shapes.size()-1).GetShape()).moveTo(posicionActual.x, posicionActual.y);
                }
                    break;
                //Rectángulo
                case 2: {                                    
                    if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    shapes.add(new Figura(new Rectangle2D.Double(posicionActual.x, posicionActual.y, 0, 0),
                            attribs.GetColor(), Stroke(), attribs.GetTransp(), attribs.GetRelleno(), attribs.GetDegradado(), attribs.GetDir(),
                        new GradientPaint(posicionActual.x, posicionActual.y, attribs.GetColor(),posicionActual.x, posicionActual.y,attribs.GetColorSec())));

                }
                    break;
                //Rectángulo redondeado
                case 3: {                                
                    if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    shapes.add(new Figura(new RoundRectangle2D.Double(posicionActual.x, posicionActual.y, 0, 0, 10, 10),
                            attribs.GetColor(), Stroke(), attribs.GetTransp(), attribs.GetRelleno(), attribs.GetDegradado(), attribs.GetDir(),
                        new GradientPaint(0,0, attribs.GetColor(),posicionActual.x, posicionActual.y,attribs.GetColorSec()))); 

                }
                    break;
                //Elipse
                case 4: {
                        if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    shapes.add(new Figura(new Ellipse2D.Double(posicionActual.x, posicionActual.y, 0, 0),
                            attribs.GetColor(), Stroke(), attribs.GetTransp(), attribs.GetRelleno(), attribs.GetDegradado(),attribs.GetDir(),
                        new GradientPaint(0,0, attribs.GetColor(),posicionActual.x, posicionActual.y,attribs.GetColorSec())));

                }
                    break;
                //Curva
                case 5: {
                    if(!curva) {
                        shapes.add(new Figura(new QuadCurve2D.Float(posicionActual.x, posicionActual.y,posicionActual.x,
                            posicionActual.y, posicionActual.x, posicionActual.y), attribs.GetColor(), Stroke(), attribs.GetTransp()));
                        curva = true;                    
                    }
                    else {
                        curva = false;
                        if(shapes.size() > 0) {
                            ((QuadCurve2D.Float)shapes.get(shapes.size()-1).GetShape()).ctrlx = posicionActual.x;
                            ((QuadCurve2D.Float)shapes.get(shapes.size()-1).GetShape()).ctrly = posicionActual.y;  
                        }

                    }
                }
                    break;
                //Texto
                case 6: {
                    if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    shapes.add(new Figura(new Text(toolbox.GetText(), posicionActual,
                    new Font(attribs.GetFuente().GetName(), attribs.GetFuente().GetStyle(),
                        (int)attribs.GetFuente().GetSize())), attribs.GetColor(), attribs.GetTransp()));
                }
                    break;
                //Forma personalizada (Estrella)
                case 7: {
                   if(curva) {
                        shapes.remove(shapes.size()-1);
                        curva = false;
                    }
                    int x2Points[] = {posicionActual.x+50, posicionActual.x+75, posicionActual.x, posicionActual.x+100, posicionActual.x+25, posicionActual.x+50};
                    int y2Points[] = {posicionActual.y, posicionActual.y+100, posicionActual.y+25, posicionActual.y+25, posicionActual.y+100,posicionActual.y};
                    GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x2Points.length);
                    GradientPaint gp;

                    polyline.moveTo (x2Points[0], y2Points[0]);

                    for (int index = 1; index < x2Points.length; index++) {
                            polyline.lineTo(x2Points[index], y2Points[index]);
                    }
                    if(attribs.GetDir()) //Degradado vertical
                        gp =  new GradientPaint(posicionActual.x + (float)polyline.getBounds2D().getWidth()/2, posicionActual.y, attribs.GetColor(), posicionActual.x + (float)polyline.getBounds2D().getWidth()/2, posicionActual.y + (float)polyline.getBounds2D().getHeight(), attribs.GetColorSec());
                    else gp =  new GradientPaint(posicionActual.x, posicionActual.y + (float)polyline.getBounds2D().getHeight()/2, attribs.GetColor(), posicionActual.x + (float)polyline.getBounds2D().getWidth(), posicionActual.y + (float)polyline.getBounds2D().getHeight()/2, attribs.GetColorSec());

                    shapes.add(new Figura(polyline, attribs.GetColor(), Stroke(), attribs.GetTransp(), attribs.GetRelleno(), attribs.GetDegradado(), attribs.GetDir(), gp));

                }
                    break;
            }
            dibujando = true;
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        // TODO add your handling code here:
        if(!attribs.EditMode()) //No estamos en el modo de edición de figuras
        {
            posicionActual = evt.getPoint();

            if(dibujando && !curva){
                dibujando = false;
                //Pintamos las figuras en el bufer
                Pintar(bufImg.createGraphics(), false);
                repaint();
            }   
        }     
    }//GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        if(!attribs.EditMode()) //No estamos en el modo de edición de figuras
        {
            posicionActual = evt.getPoint();
            dibujando = true;

            switch(toolbox.GetActiveTool()){
                //Línea recta
                case 0: ((Line2D.Double)shapes.get(shapes.size()-1).GetShape()).setLine(posicionPrevia, posicionActual);
                    break;
                //Trazo libre 
                case 1: {
                    ((GeneralPath)shapes.get(shapes.size()-1).GetShape()).lineTo(posicionActual.x, posicionActual.y);
                    Pintar(bufImg.createGraphics(), false);
                }   
                    break;
                //Rectángulo
                case 2: {
                    if((posicionActual.x >= posicionPrevia.x) && (posicionActual.y >= posicionPrevia.y)) { //Abajo a la derecha                    
                        if(equilatero){
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.y-posicionPrevia.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }
                        else {
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.x-posicionPrevia.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x+(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y,
                                    attribs.GetColor(),(posicionPrevia.x+(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y +(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height), attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint(posicionPrevia.x, (posicionPrevia.y + (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), (posicionPrevia.x + (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y + (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else if ((posicionActual.x >= posicionPrevia.x) && (posicionActual.y < posicionPrevia.y)) { //Arriba a la derecha                    
                        if(equilatero) {
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.y-posicionActual.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        else {
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.x-posicionPrevia.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x+(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height),
                                    attribs.GetColor(),(posicionPrevia.x+(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y, attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint(posicionPrevia.x, (posicionPrevia.y - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), (posicionPrevia.x + (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else if ((posicionActual.x < posicionPrevia.x) && (posicionActual.y < posicionPrevia.y)) { //Arriba a la izquierda
                        if(equilatero){
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x-(posicionPrevia.y-posicionActual.y);
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.y-posicionActual.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        else {
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionActual.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.x-posicionActual.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x-(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height),
                                    attribs.GetColor(),(posicionPrevia.x-(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y, attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), posicionPrevia.x, (posicionPrevia.y - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else  { //abajo a la izquierda
                        if(equilatero) {
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x-(posicionActual.y-posicionPrevia.y);
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.y-posicionPrevia.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }
                        else {
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionActual.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.x-posicionActual.x;
                            ((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x-(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y,
                                    attribs.GetColor(),(posicionPrevia.x-(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y +(float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height), attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x - (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y + (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), posicionPrevia.x, (posicionPrevia.y + (float)((Rectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                }
                    break;
                //Rectangulo redondeado
                case 3: {
                    if((posicionActual.x >= posicionPrevia.x) && (posicionActual.y >= posicionPrevia.y)) { //Abajo a la derecha
                        if(equilatero){
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.y-posicionPrevia.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }
                        else {
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.x-posicionPrevia.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x+(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y,
                                    attribs.GetColor(),(posicionPrevia.x+(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y +(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height), attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint(posicionPrevia.x, (posicionPrevia.y + (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), (posicionPrevia.x + (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y + (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else if ((posicionActual.x >= posicionPrevia.x) && (posicionActual.y < posicionPrevia.y)) { //Arriba a la derecha
                        if(equilatero) {
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.y-posicionActual.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        else {
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.x-posicionPrevia.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x+(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height),
                                    attribs.GetColor(),(posicionPrevia.x+(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y, attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint(posicionPrevia.x, (posicionPrevia.y - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), (posicionPrevia.x + (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else if ((posicionActual.x < posicionPrevia.x) && (posicionActual.y < posicionPrevia.y)) { //Arriba a la izquierda
                        if(equilatero){
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x-(posicionPrevia.y-posicionActual.y);
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.y-posicionActual.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        else {
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionActual.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.x-posicionActual.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x-(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height),
                                    attribs.GetColor(),(posicionPrevia.x-(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y, attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), posicionPrevia.x, (posicionPrevia.y - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else  { //abajo a la izquierda
                        if(equilatero) {
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x-(posicionActual.y-posicionPrevia.y);
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.y-posicionPrevia.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }
                        else {
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionActual.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.x-posicionActual.x;
                            ((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x-(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y,
                                    attribs.GetColor(),(posicionPrevia.x-(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y +(float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height), attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x - (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y + (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), posicionPrevia.x, (posicionPrevia.y + (float)((RoundRectangle2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                }
                    break;
                //Elipse
                case 4: {
                    if((posicionActual.x >= posicionPrevia.x) && (posicionActual.y >= posicionPrevia.y)) { //Abajo a la derecha
                        if(equilatero){
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.y-posicionPrevia.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }
                        else {
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.x-posicionPrevia.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x+(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y,
                                    attribs.GetColor(),(posicionPrevia.x+(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y +(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height), attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint(posicionPrevia.x, (posicionPrevia.y + (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), (posicionPrevia.x + (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y + (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else if ((posicionActual.x >= posicionPrevia.x) && (posicionActual.y < posicionPrevia.y)) { //Arriba a la derecha
                        if(equilatero) {
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.y-posicionActual.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        else {
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.x-posicionPrevia.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x+(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height),
                                    attribs.GetColor(),(posicionPrevia.x+(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y, attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint(posicionPrevia.x, (posicionPrevia.y - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), (posicionPrevia.x + (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else if ((posicionActual.x < posicionPrevia.x) && (posicionActual.y < posicionPrevia.y)) { //Arriba a la izquierda
                        if(equilatero){
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x-(posicionPrevia.y-posicionActual.y);
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.y-posicionActual.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        else {
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionActual.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionActual.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.x-posicionActual.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionPrevia.y-posicionActual.y;
                        }
                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x-(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height),
                                    attribs.GetColor(),(posicionPrevia.x-(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y, attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), posicionPrevia.x, (posicionPrevia.y - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                    else  { //abajo a la izquierda
                        if(equilatero) {
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionPrevia.x-(posicionActual.y-posicionPrevia.y);
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionActual.y-posicionPrevia.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }
                        else {
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).x = posicionActual.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).y = posicionPrevia.y;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width = posicionPrevia.x-posicionActual.x;
                            ((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height = posicionActual.y-posicionPrevia.y;
                        }

                        //Degradado vertical
                        if (shapes.get(shapes.size()-1).GetGradDir()) 
                            shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x-(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), posicionPrevia.y,
                                    attribs.GetColor(),(posicionPrevia.x-(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width/2), (posicionPrevia.y +(float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height), attribs.GetColorSec()));
                        //Degradado horizontal
                        else shapes.get(shapes.size()-1).SetGradientPattern( new GradientPaint((posicionPrevia.x - (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).width), (posicionPrevia.y + (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2),
                                    attribs.GetColor(), posicionPrevia.x, (posicionPrevia.y + (float)((Ellipse2D.Double)shapes.get(shapes.size()-1).GetShape()).height/2), attribs.GetColorSec()));
                    }
                }
                    break;
                //Curva
                case 5: if(curva) {
                    ((QuadCurve2D.Float)shapes.get(shapes.size()-1).GetShape()).setCurve(posicionPrevia.x, posicionPrevia.y,    //Punto inicial
                            posicionPrevia.x, posicionPrevia.y, //Punto control
                            posicionActual.x, posicionActual.y); //Punto final
                }
                else {
                    if(shapes.size() > 0) {
                    ((QuadCurve2D.Float)shapes.get(shapes.size()-1).GetShape()).ctrlx = posicionActual.x;
                    ((QuadCurve2D.Float)shapes.get(shapes.size()-1).GetShape()).ctrly = posicionActual.y;
                    }
                }
                    break;
            }

            repaint();

            // Actualizamos coordenadas del cursor en la barra de estado
            String s = "Posicion actual  X:" + evt.getPoint().x + " Y:" + evt.getPoint().y;
            padre.setEstado(s); 
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // Actualizamos coordenadas del cursor en la barra de estado
        String s = "Posicion actual  X:" + evt.getPoint().x + " Y:" + evt.getPoint().y;
        padre.setEstado(s);      
    }//GEN-LAST:event_formMouseMoved

    //Para capturar el teclado de todos los componentes
    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.getKeyCode()== KeyEvent.VK_SHIFT)
                equilatero=true;
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                if(e.getKeyCode()== KeyEvent.VK_SHIFT)
                equilatero=false;
            }
            return false;
        }
    }    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
