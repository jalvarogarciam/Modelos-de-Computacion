/* Va todo sin acentos... por si acaso...

Modelo básico de interfaz grafica para computacion de automatas celulares unidimensionales binarios incluyendo:

a) Dos modos de carga de configuracion inicial (por linea de comandos): aleatoria, o célula central activa, resto no;
b) Núcleo de computación basico para una función de transicion con ejemplo de sintesis "total" de reglas para k=3, r=1 y regla codigo 792
c) Condicion de frontera nula, por comodidad... para mi :-); esto no afecta de forma significativa al patron espacio-temporal global
d) Elementos basicos para pintar la evoluación espacio-temporal del modelo con un output gráfico.

Este código puede utilizarse como base para desarrollar los productos pedidos en la práctica número 3.

Ejodo (modo=true pecucion: java automataCelularChorra2 mara carga aleatoria, false para celula central activa, resto no)
NOTA: COMPARESE EL OUTPUT GRAFICO QUE SE OBTIENE CON LA ILUSTRACION DE LAS FIG. 5 Y 6, PAGINA 421 DEL TEXTO DE WOLFRAM EN NATURE (), disponible en la carpeta de la 
practica "Cellular Automata as Model of Complexity". */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.*;

public class automataCelularChorra2 extends JPanel {  

  boolean modo;  //elegir configuracipn inicial aleatoria (true) o celula central a 1, resto a 2 (false)...

  public automataCelularChorra2(boolean modo){
      this.modo = modo;
  }          

   public void paint(Graphics g) {
      Image img = createImageWithText();
      g.drawImage(img, 20,20,this);
   }

   private Image createImageWithText() {
      Random r       = new Random();   //sustituir esto por RANDU o similar, normalizando si hace falta...
      int ancho      = 940;
      int alto       = 500;	
      int [] data    = new int[ancho]; //configuración actual...
      int [] data2   = new int[ancho]; //configuración sucesora...
      int [] temp;                     //para intercambios...

      //estableciendo configuracion inicial, aleatoria, o todas a estado 2 y la central a 1...
      if(modo==true)
        for(int a=0; a<data.length; a++) data[a]=r.nextInt(3);  
        else {for(int b=0; b<data.length; b++) data[b]=2;       
              data[399]=1;
             }
      
  
      //mandangas graficas...
      BufferedImage bufferedImage = new BufferedImage(ancho,alto, BufferedImage.TYPE_INT_RGB);
      Graphics g = bufferedImage.getGraphics();
	  
	  
      
    //ahora, la regla k=3, r=1 con codigo 792 (Fig. 5, pag. 421, texto de Wolfram en Nature) en base 3 es 1002100 
    //ATENCION: ¡¡¡ANALIZAR LA LEYENDA DE LAS FIGURAS ANTES DE SEGUIR!!!!
    //aqui, la sinstesis se hace "a pelo" dentro de un switch; en la GUI, leer un decimal, pasar a la base que corresponda, utilizar un array auxiliar para almacenar los digitos
    //de la regla, y reescribir los casos del switch con el array en la derecha de las asignaciones...
    //si con esto no queda claro...
  
    
    //núcleo de computación del autómata celular...

    int nGeneraciones = alto; //tiempo de evolucion...	
	  
	  //se pinta la actual configuracion inicial con una paleta elemental...
      for(int k=0; k<data.length; k++)
        if(data[k]==0){g.setColor(Color.BLACK);  g.drawOval(k, 0, 1, 1);}
          else if(data[k]==1){g.setColor(Color.GREEN); g.drawOval(k, 0, 1, 1);}
                  else if(data[k]==2){g.setColor(Color.RED);g.drawOval(k, 0, 1, 1);}

      for(int i = 0; i<nGeneraciones; i++){
        for(int j=0; j<data.length; j++){

          //ahora, la funcion de transicion (regla 792) del automata celular, utilizando frontera nula por comodidad (no afecta significativamente al patron global)...
          //sumamos celula j-esima y vecinas para r=1... con lo que 0<=suma<=6
          //OJO!!! aqui no necesito tomar residuos con %... la regla ya esta en base 3 y por tanto asigna el siguiente estado tomandolo en {0, 1, 2}...

          int suma = 0; 
          if(j==0)suma=(data[j]+data[j+1]);
             else if(j==data.length-1)suma=(data[j-1]+data[j]);
                   else suma=(data[j-1]+data[j]+data[j+1]);
				   

          //este selector multiple es la "clave" de todo el asunto...
          switch(suma){
          //filtramos la suma por la regla 792 escrita en base 3 para decidir el estado siguiente... 
           case  6: data2[j] = 1;break;
           case  5: data2[j] = 0;break;
           case  4: data2[j] = 0;break;
           case  3: data2[j] = 2;break;
           case  2: data2[j] = 1;break;
           case  1: data2[j] = 0;break;
           case  0: data2[j] = 0;break;
         }//switch
        }//for j

        //se pinta la actual configuracion con una paleta elemental...
        for(int k=0; k<data.length; k++)
          if(data2[k]==0){g.setColor(Color.BLACK);  g.drawOval(k, i, 1, 1);}
            else if(data2[k]==1){g.setColor(Color.GREEN); g.drawOval(k, i, 1, 1);}
                   else if(data2[k]==2){g.setColor(Color.RED);g.drawOval(k, i, 1, 1);}      

        //se intercambian los arrays...
        temp=data;
        data=data2;
        data2=temp;	
      }//for i
      return bufferedImage;
   }
   
   public static void main(String[] args) {
      boolean  mode  = true;    
      int anchoFrame = 1000;
      int altoFrame  =  600;
      JFrame frame   = new JFrame();
      frame.setSize(anchoFrame, altoFrame);
      frame.getContentPane().add(new automataCelularChorra2(mode));
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(anchoFrame, altoFrame);
      frame.setVisible(true);
   }
}