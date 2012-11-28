import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import com.sun.j3d.utils.image.*;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.image.TextureLoader;


public class MyKeyboardBehavior extends Behavior 
{
	  private Load3DExample objComModelo;
	  private boolean alternaTextura = false;
	  private int counterTexture = 0;
	  private ArrayList<String> lTextures = new ArrayList<String>();
	  
	  
		
	  public MyKeyboardBehavior(Load3DExample o)
	  {
		  this.objComModelo = o;
		  
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\fur1.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\fur2.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\fur3.jpg");
		  
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Grass1.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Grass2.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Grass3.jpg");
		  
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Wood1.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Wood2.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Wood3.jpg");
		  
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Metal1.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Metal2.jpg");
		  lTextures.add("C:\\VIAGEM_2012\\Java3d\\Modelos\\Textures\\Metal3.jpg");
		  
		  
	  }
	
	
	   @Override
       public void initialize() 
	   {
           this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
           
       }

       @Override
       public void processStimulus(Enumeration criteria) 
       {
    	   
    	   while (criteria.hasMoreElements()) 
           {
               Object element = criteria.nextElement();
               
               if (element instanceof WakeupOnAWTEvent) 
               {
                   WakeupOnAWTEvent event = (WakeupOnAWTEvent) element;
                   
                   for (AWTEvent awtEvent : event.getAWTEvent()) 
                   {
                       if (awtEvent instanceof KeyEvent) 
                       {
                           KeyEvent keyEvent = (KeyEvent) awtEvent;
                       
                          // 79 -> CTRL+O  (ABRIR MODELO - TODO: ACERTAR ARRAYS)
                          // 83 -> CTRL+S  (SALVAR O MODELO)
                          // 67 -> LETRA C (COR AZUL ON-OFF)
                          // 84 -> LETRA T (TEXTURAS)
                          // 82 -> LETRA R (REINICAR WIREFRAME DO MODELO)
                          // 49 -> TECLA 1 (AFUNDAR OU LEVANTAR O PONTO COM O FINGER) 
                          // 50 -> TECLA 2 (AFUNDAR OU LEVANTAR O PONTO COM O FINGER)
                          // 51 -> TECLA 3 (AFUNDAR OU LEVANTAR O PONTO COM O FINGER)
                          // 52 -> TECLA 4 (AFUNDAR OU LEVANTAR O PONTO COM O FINGER)
                          // 53 -> TECLA 5 (AFUNDAR OU LEVANTAR O PONTO COM O FINGER)
                          // = ou (+) -> TECLA 61 ou 107   AUMENTA SENSITIVIDADE
                          // -        -> TECLA 45 OU 109   DIMINUI SENSITIVIDAE 
                           
                          // System.out.println("Tecla pressionada:" + String.valueOf(keyEvent.getKeyCode()));
                           
                           // CTRL+O
                           if (keyEvent.getKeyCode() == 79) 
                           {
                        	   // Aqui preciso abrir o modelo
                        	   String filename = "";
                        	   JFileChooser fc = new JFileChooser(new File(filename));
                        	   
                        	   Action saveAction = new OpenSaveFileAction(this.objComModelo, fc, "Open...");
                        	   
                        	   saveAction.actionPerformed(null);
                           }
                           
                           // CTRL+S
                           if (keyEvent.getKeyCode() == 83) 
                           {
                        	   // Aqui preciso salvar o modelo
                        	   
                        	   String filename = "";
                        	   JFileChooser fc = new JFileChooser(new File(filename));
                        	   
                        	   Action saveAction = new OpenSaveFileAction(this.objComModelo, fc, "Save As...");
                        	   
                        	   saveAction.actionPerformed(null);
                        	   
                           }
                           
                           // TECLA C -> COLOCAR UMA TEXTURA AZUL
                           if (keyEvent.getKeyCode() == 67) 
                           {
                        	    
                        	   if(alternaTextura)
                        	   {
                        		   this.objComModelo.setModelWireFrame(this.objComModelo.partOfTheObject);
                        		   
                        		   alternaTextura = false;
                        	   }
                        		   
                        	   else
                        	   {
                        		   
	                            	Appearance lightBlueApp = new Appearance();
	                           	    
	                           	    lightBlueApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
	                           	   
	                           	    
	                           	    Color3f c = new Color3f(0.0f,0.1f,0.3f);
	                           	    
	                           	    this.objComModelo.setToMyDefaultAppearance(lightBlueApp,c);
	                           	    this.objComModelo.partOfTheObject.setAppearance(lightBlueApp);
	                           	   
	                           	    
	                           	    /*
                        		    Appearance fBoxApp1 = new Appearance();
                        		   
                        		    Color3f ambientColourFBox = new Color3f(0.3f,0.0f,0.0f);
                        		    Color3f emissiveColourFBox = new Color3f(0.0f,0.0f,0.0f);
                        		    Color3f diffuseColourFBox = new Color3f(0.6f,0.0f,0.0f);
                        		    Color3f specularColourFBox = new Color3f(0.8f,0.0f,0.0f);
                        		    float shininessFBox = 128.0f;
                        		    
                        		    fBoxApp1.setMaterial(new Material(ambientColourFBox,emissiveColourFBox,
                                            diffuseColourFBox,specularColourFBox,shininessFBox));

                        		    //Generate interpolated transparency.
                        		    TransparencyAttributes ta1 = new TransparencyAttributes();
                        		    ta1.setTransparencyMode(TransparencyAttributes.BLENDED);
                        		    ta1.setTransparency(0.2f);

                        		    fBoxApp1.setTransparencyAttributes(ta1);

                        		    this.objComModelo.partOfTheObject.setAppearance(fBoxApp1);
                        		    */
                        		   
                        		   
	                           	    
	                           	    alternaTextura = true;
                        	   }
                     
                        	   
                           }
                           
                           // TECLA T -> COLOCAR UMA TEXTURA AZUL
                           if (keyEvent.getKeyCode() == 84) 
                           {
                        	   // TextureLoader loader = new TextureLoader("C:\\Meus Documentos\\pos\\Tese\\Artigos2012\\UIST_2012\\Java3d\\Modelos\\Textures\\red-fur1.jpg", this.objComModelo);
                        	   // TextureLoader loader = new TextureLoader("C:\\Meus Documentos\\pos\\Tese\\Artigos2012\\UIST_2012\\Java3d\\Modelos\\Textures\\Grass2.jpg", this.objComModelo);
                        	  //  TextureLoader loader = new TextureLoader("C:\\Meus Documentos\\pos\\Tese\\Artigos2012\\UIST_2012\\Java3d\\Modelos\\Textures\\Grass3.jpg", this.objComModelo);
                        	   
                        	   
                        	 /*  
                        	   Texture brick = loader.getTexture();
                       	 	
                       		// Create Appearance Object
                        	 Appearance appearance = new Appearance();

                       		// Create Appearance Attributes and give to Appearance.
                       		// TextureAttributes can be used for transforming texture (e.g. scaling)
                       		TextureAttributes ta = new TextureAttributes();	    
                       		
                       		ta.setTextureMode(TextureAttributes.COMBINE);
                       		appearance.setTextureAttributes(ta);

                       		// Attach Texture object to Appearance object
                       		appearance.setTexture(brick);
                       	    this.objComModelo.partOfTheObject.setAppearance(appearance);
 
                       		*/
                        	   

                        	    //Generate a (scaled) image of the texture. Height and width must be
                        	    //powers of 2.
                        	    // ImageComponent2D textureIm = loader.getScaledImage(64,128);
                        	   // ImageComponent2D textureIm = loader.getScaledImage(900,674);
                        	   
                        	   
                        	   TextureLoader loader = new TextureLoader(this.lTextures.get(this.counterTexture) , this.objComModelo);
                        	   
                        	   ImageComponent2D textureIm = loader.getImage();

                        	    //Generate the texture.
                        	    Texture2D aTexture = new Texture2D(Texture2D.BASE_LEVEL,Texture2D.RGB,
                        	                                            textureIm.getWidth(),
                        	                                            textureIm.getHeight());
                        	    aTexture.setImage(0,textureIm);

                        	    //An Appearance which will use the texture.
                        	    Appearance textureApp = new Appearance();
                        	    textureApp.setTexture(aTexture);
                        	    TextureAttributes textureAttr = new TextureAttributes();
                        	    textureAttr.setTextureMode(TextureAttributes.REPLACE);
                        	    textureApp.setTextureAttributes(textureAttr);
                        	    
                        	    Material mat = new Material();
                        	    mat.setShininess(120.0f);
                        	    textureApp.setMaterial(mat);
                        	    TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
                        	                                                    TexCoordGeneration.TEXTURE_COORDINATE_2);

                        	    textureApp.setTexCoordGeneration(tcg);

                       		   this.objComModelo.partOfTheObject.setAppearance(textureApp);
                       		   
                       		this.counterTexture++;
                       		
                       		if(this.counterTexture==this.lTextures.size())
                       			this.counterTexture = 0;

                        	   
                           }

                           
                           // = ou (+) -> TECLA 61 ou 107   AUMENTA SENSITIVIDADE
                           if (  (keyEvent.getKeyCode() == 61) || (keyEvent.getKeyCode() == 107) )   
                           {
                        	   for(FingerSphere f: this.objComModelo.listaFingers)
                        	   {
                        		   f.sensitividade = f.sensitividade + 0.001f; 
                        	   }
                        	   
                           }
                           
                        // -        -> TECLA 45 OU 109   DIMINUI SENSITIVIDAE
                           if (  (keyEvent.getKeyCode() == 45) || (keyEvent.getKeyCode() == 109) )   
                           {
                        	   for(FingerSphere f: this.objComModelo.listaFingers)
                        	   {
                        		   f.sensitividade = f.sensitividade - 0.001f; 
                        	   }
                        	   
                           }
                           
                           
                           
                           
                        // TECLA R -> REINICIALIZAR A GEOMETRIA DO MODELO
                           if (keyEvent.getKeyCode() == 82) 
                           {

                        	   this.objComModelo.RestoraBackup();
                           }
                           
                           // TECLA 1 -> AFUNDAR/LEVANTAR OS PONTOS
                           if (keyEvent.getKeyCode() == 49) 
                           {
                        	   	this.objComModelo.setFingerPressDirection(0);
                           }
                           // TECLA 2 -> AFUNDAR/LEVANTAR OS PONTOS
                           if (keyEvent.getKeyCode() == 50) 
                           {
                        	   	this.objComModelo.setFingerPressDirection(1);
                           }
                           
                           // TECLA 3 -> AFUNDAR/LEVANTAR OS PONTOS
                           if (keyEvent.getKeyCode() == 51) 
                           {
                        	   	this.objComModelo.setFingerPressDirection(2);
                           }
                           
                           // TECLA 4 -> AFUNDAR/LEVANTAR OS PONTOS
                           if (keyEvent.getKeyCode() == 52) 
                           {
                        	   	this.objComModelo.setFingerPressDirection(3);
                           }
                           
                           // TECLA 5 -> AFUNDAR/LEVANTAR OS PONTOS
                           if (keyEvent.getKeyCode() == 53) 
                           {
                        	   	this.objComModelo.setFingerPressDirection(4);
                           }
                           
                           
                           
                           
                           /*
                           if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                               moveLeft();
                           } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                               moveRight();
                           } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                               moveForward();
                           } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                               moveBackwards();
                           }*/
                           
                       }
                   }
               }
           }
           
           this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
       }
}



//This action creates and shows a modal save-file dialog.
class OpenSaveFileAction extends AbstractAction {
 JFileChooser chooser;
 JFrame frame;
 String msg;

 public OpenSaveFileAction(JFrame frame, JFileChooser chooser, String m) 
 {
     super(m);
     this.chooser = chooser;
     this.frame = frame;
     
     this.msg = m;
     
 }

 public void actionPerformed(ActionEvent evt) 
 {

	 Load3DExample l = (Load3DExample) frame;
	 
     try {
		if(this.msg=="Save As...")
		 {
		     // Show dialog; this method does not return until dialog is closed
		     chooser.showSaveDialog(frame);

		     // Get the selected file
		     File file = chooser.getSelectedFile();
		     
		     // System.out.println("Caminho:" + String.valueOf(file.getAbsolutePath() ));

			 
			 // Gravando o modelo no arquivo escolhido
		     
		     l.GravaModelo(file.getAbsolutePath());
		 }
		 
		 if(this.msg=="Open...")
		 {
		     // Show dialog; this method does not return until dialog is closed
		     chooser.showOpenDialog(frame);

		     // Get the selected file
		     File file = chooser.getSelectedFile();
			 
			 // Gravando o modelo no arquivo escolhido
		     // System.out.println("Abrindo arquivo:" + String.valueOf(file.getAbsolutePath() ));
		    
		     l.loadAnotherModel(file.getAbsolutePath());
		     
		 }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Problem openning or salving file: " + e.getMessage() );
	}
     
 }
};
