import javax.vecmath.*;

import com.sun.j3d.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.vp.*;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import java.util.Hashtable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.*;
import java.io.*;
import java.util.*;


import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;

import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import java.util.Hashtable;
import java.util.Enumeration;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;


/**
* An example for loading a geometric object and showing it in the scene.
* The names of the parts of the object are printed.
* The colour blue is assigned to one of the part.
*
* @author Frank Klawonn
* Last change 07.07.2005
*/
public class Load3DExample extends JFrame 
{

 //
   public float[] vertices;       // O array com os vertices
   public float[] bkp_vertices;   // O array com o backp dos vertices
   
   public  ArrayList<Point3f> listaVertices = new ArrayList<Point3f>(); // Os pontos da geometria
   public ArrayList<MyPoint3f> bkp_listaVertices = new ArrayList<MyPoint3f>(); // Os pontos da geometria (backup)
   
   
   
   public ArrayList<FingerSphere> listaFingers = new ArrayList<FingerSphere>();
   
   // O Shape que representa o modelo
   public Shape3D partOfTheObject;
	  
  //The canvas to be drawn upon.
  public Canvas3D myCanvas3D;
   // public My3DCanvas myCanvas3D;
   
  
  public Load3DExample()
  {

	  
    //Mechanism for closing the window and ending the program.
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    //Default settings for the viewer parameters.
    myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
   //  myCanvas3D = new My3DCanvas(SimpleUniverse.getPreferredConfiguration());
    


    //Construct the SimpleUniverse:
    //First generate it using the Canvas.
    SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);


    //Default position of the viewer.
    simpUniv.getViewingPlatform().setNominalViewingTransform();


    //The scene is generated in this method.
    createSceneGraph(simpUniv);


    //Add some light to the scene.
    addLight(simpUniv);


    //The following three lines enable navigation through the scene using the mouse.
   
    OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
    ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
    simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);


    //Show the canvas/window.
    setTitle("The 3D Touch Modeler");
    setSize(700,700);
    getContentPane().add("Center", myCanvas3D);
    setVisible(true);


  }

  public static void main(String[] args)
  {

	  Load3DExample le = new Load3DExample();
  }

  
  
  //In this method, the objects for the scene are generated and added to 
  //the SimpleUniverse.
  public void createSceneGraph(SimpleUniverse su)
  {


	  // Criando a cena a partir do modelo
	 // Scene s = this.getSceneFromModel("");
	  // Scene s = this.getSceneFromModel("C:\\MODELS\\cube.obj");
	// Scene s = this.getSceneFromModel("C:\\MODELS\\tetrahedron.obj");
	  // Scene s = this.getSceneFromModel("C:\\MODELS\\Coelho.obj");
	  // Scene s = this.getSceneFromModel("C:\\MODELS\\Navio.obj");
	  
	  
	  // Melhores modelos para demonstração no notebook
	  
	 
	  Scene s = this.getSceneFromModel("C:\\MODELS\\Coelho.obj");
	  
	  
	  // Scene s = this.getSceneFromModel("C:\\MODELS\\head.obj");
	  //Scene s = this.getSceneFromModel("C:\\MODELS\\goblet.obj");
	   // Scene s = this.getSceneFromModel("C:\\MODELS\\space_shuttle.obj");
	 
	 
	 // Scene s = this.getSceneFromModel("C:\\MODELS\\Coelho.obj");
	//  Scene s = this.getSceneFromModel("C:\\MODELS\\X_WING.obj");
	  
	 // Scene s = this.getSceneFromModel("C:\\MODELS\\Coelho.obj");
	  
	  
	// Scene s = this.getSceneFromModel("C:\\MODELS\\tie.obj");
	  
	  
	  
	  
	  
	 // Scene s = this.getSceneFromModel("C:\\MODELS\\sphere20.obj");
	 // Scene s = this.getSceneFromModel("");
	  
	  // Scene s = this.getSceneFromModel("C:\\MODELS\\horse.obj");

	 
     // Carregando uma parte do modelo
    partOfTheObject  = this.getShape3dFromScene("main",s);

		  
    //Generate a transformation group for the loaded object.
    /* 
    Transform3D tfObject = new Transform3D();
    tfObject.rotZ(0.4*Math.PI);
    
    Transform3D xRotation = new Transform3D();
    xRotation.rotY(0.4*Math.PI);
    
    tfObject.mul(xRotation);
    */
    // TransformGroup tgObject = new TransformGroup(tfObject);
    
    
    TransformGroup tgObject = new TransformGroup();
    tgObject.addChild(s.getSceneGroup());
    
 
    // Coloando o modelo como um WireFrame
    this.setModelWireFrame(partOfTheObject);

    // Convertendo para poder obter os vertices
    convert(partOfTheObject,false,false,true,false,false,false);
  
    // printInfo(partOfTheObject);
    partOfTheObject.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    partOfTheObject.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
    // printInfo(partOfTheObject);
     
    TriangleArray g = (TriangleArray) partOfTheObject.getGeometry();
    
    g.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
    g.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
    g.setCapability(GeometryArray.BY_REFERENCE);

    // Obtendo a geometria de pontos do Modelo
    this.vertices =  g.getCoordRefFloat();
    
	
    // Convertendo os pontos para um formato melhor...
    // this.listaVertices = this.ConvertArrayFloatParaArrayPoint3f(this.vertices);
    
    this.listaVertices = this.ConvertArrayFloatParaArrayPoint3f_FAST(this.vertices);
    
    // Fazendo o backup da geometria da figura
    this.FazBackup();
    
    

    /***************** INICIO ESFERAS ****************/
    
    FingerSphere s1 = new FingerSphere(1);
    TransformGroup tgSphere1 = s1.getFingerSphere();
    // s1.hide();
    s1.show();
    // s1.MoveTo(0.50f,0.50f,0.0f);
    // s1.MoveTo(0.0f,0.90f);
    listaFingers.add(s1);
    s1.apagaMasMovimenta();
    
    
    FingerSphere s2 = new FingerSphere(2);
    TransformGroup tgSphere2 = s2.getFingerSphere();
    // s2.hide();
    s2.show();
    // s2.MoveToPoint(new Point3f(0.50f,0.50f,0.0f));
    // s2.MoveTo(.0f,0.0f);
    listaFingers.add(s2);
    s2.apagaMasMovimenta();
    
    
    FingerSphere s3 = new FingerSphere(3);
    TransformGroup tgSphere3 = s3.getFingerSphere();
    // s3.hide();
    s3.show();
    // s3.MoveTo(0.0f,-1.0f);
    listaFingers.add(s3);
    s3.apagaMasMovimenta();
    
    FingerSphere s4 = new FingerSphere(4);
    TransformGroup tgSphere4 = s4.getFingerSphere();
    // s4.hide();
    s4.show();
    // s4.MoveTo(-1.0f,0.0f);
    listaFingers.add(s4);
    s4.apagaMasMovimenta();
    
    FingerSphere s5 = new FingerSphere(5);
    TransformGroup tgSphere5 = s5.getFingerSphere();
    s5.show();
    // s5.MoveTo(0.0f,1.0f);
    // s5.hide();
    listaFingers.add(s5);
    s5.apagaMasMovimenta();
    
    /***************** FIM ESFERAS ****************/
    
    
    
    
    
 // Variável estática para a janela de captura do BCI
    // ThreadManipulaModelo t  = new ThreadManipulaModelo(partOfTheObject,g,this.vertices,this.listaVertices, listaFingers);
    // t.start();
    
    
	// Iniciando o servidor que recebe os dados  
	CollabServer.main(this);  

    
    BranchGroup theScene = new BranchGroup();
    theScene.addChild(tgObject); // Adicionando o modelo
    
    /* AQUI ADICIONO OS FINGERS */
   
    theScene.addChild(tgSphere1); // Adicionando a esfera
    theScene.addChild(tgSphere2); // Adicionando a esfera
    theScene.addChild(tgSphere3); // Adicionando a esfera
    theScene.addChild(tgSphere4); // Adicionando a esfera
    theScene.addChild(tgSphere5); // Adicionando a esfera
    
    
    // Testes: colocando uma esfera para cada ponto da geometria
    /*
    for(Point3f p: this.listaVertices)
    {
    	FingerSphere sTeste = new FingerSphere(99);
        TransformGroup tgTeste = sTeste.getFingerSphere();
        sTeste.show();
        sTeste.MoveTo(p.x,p.y,p.z);
        // sTeste.MoveToPoint(new Point3f(p.x,p.y,p.z));
    	
        theScene.addChild(tgTeste);
        
    }
*/
    

    /* AQUI ADICIONO O KEYBOARD BEHAVIOR PARA PEGAR O QUE O USUÁRIO DIGITO*/
    TransformGroup tgTeclado = new TransformGroup();
    
    MyKeyboardBehavior b = new MyKeyboardBehavior(this);
    b.setEnable(true);
    b.setSchedulingBounds(new BoundingSphere());

    theScene.addChild(b);
    
    
    //The following four lines generate a white background.
    Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
    bg.setApplicationBounds(bounds);
    theScene.addChild(bg);

  
    theScene.compile();
    
    //Add the scene to the universe.
    su.addBranchGraph(theScene);
    
    
  }
  
  // Fazendo o backup da figura para posteriormente poder voltar ao formato original
  public void FazBackup()
  {
	  this.bkp_vertices = this.vertices.clone();
	  
	  /*
	  // ESTE CÓDIGO COMENTADO FOI SUBSTITUIDO PELO CLONE() ACIMA
	  this.bkp_vertices = new float[this.vertices.length];
	  
	  for(int i = 0; i < this.vertices.length ;i++)
	  {
		  this.bkp_vertices[i] = this.vertices[i];
	  }
	  */
	  
	  
	  this.bkp_listaVertices.clear();
	  
	  for(Point3f p: this.listaVertices )
	  {
		  // Point3f novo = new Point3f(p.x,p.y,p.z);
		  
		  // Aqui vou colocar no backup todos os pontos e seus respectivos índices 
		  // no array auxiliar de bkp_vertices
		  MyPoint3f myNovo = new MyPoint3f(p.x,p.y,p.z);
		  
		  // Aqui vou colocar o novo ponto
		  this.bkp_listaVertices.add(myNovo);
		  
		  // Agora vou descobrir em quais locais este novo ponto está!
		  this.criaIndicePonto(myNovo);
		  
	  }
  }
  
  // Restaurando a figura no formato original
  public void RestoraBackup()
  {
	  
	  this.vertices = new float[this.bkp_vertices.length];
	  
	  for(int i = 0; i < this.bkp_vertices.length  ;i++)
	  {
		  this.vertices[i] = this.bkp_vertices[i];
	  }
	  
	  this.listaVertices.clear();
	  
	  for(MyPoint3f p: this.bkp_listaVertices )
	  {
		  Point3f novo = new Point3f(p.x,p.y,p.z);
		  
		  this.listaVertices.add(novo);
	  }
	  
		
	  TriangleArray g = (TriangleArray) this.partOfTheObject.getGeometry();
	    
	   g.setCoordRefFloat(this.vertices);
	  
  }
  
  // Aqui eu vou descobrir todos os índices do vetor de vértices que contém este ponto!
  public void criaIndicePonto(MyPoint3f myNovo)
  {
		for(int i = 0 ; i<this.vertices.length ;i=i+3)
		{
			Point3f doArrayVertices  = new Point3f(this.vertices[i],this.vertices[i+1], this.vertices[i+2]);
			
			// Se achou o ponto
			if( (myNovo.x==doArrayVertices.x) && (myNovo.y==doArrayVertices.y) && (myNovo.z==doArrayVertices.z) )
			{
				// Adicionando o indice (onde o ponto aparece primeiro, ou seja, na coordenada x deste ponto!)
				myNovo.aPontos.add(i);
			}
		}
	  
  }
  
  
  
  // Muda a operação com o finger (afundar ou levantar)
  public void setFingerPressDirection(int finger)
  {
	  FingerSphere theFinger = this.listaFingers.get(finger);
	  
	  if(theFinger.afundarLevantar == 1.0f)
		  theFinger.afundarLevantar = -1.0f;
	  else
		  theFinger.afundarLevantar = 1.0f;
  }
 
  
  
  // Coloca tudo que está no array de float para a collection
  public ArrayList<Point3f> ConvertArrayFloatParaArrayPoint3f_FAST(float[] v)
  {

	  ArrayList<Point3f> pp = new ArrayList<Point3f>();
		 
		 Point3f temp = new Point3f();
		 
		 for(int i = 0 ; i<this.vertices.length ;i=i+3)
		{
			temp = new Point3f(this.vertices[i],this.vertices[i+1], this.vertices[i+2]);
			
			if(!pp.contains(temp))
				 pp.add(temp);
		}
		 
		 return pp;
  }
  
  public ArrayList<Point3f> ConvertArrayFloatParaArrayPoint3f_FAST_BACKUP(float[] v)
  {

	  // Faz a conversão para o arraylist e também ja monta o backup
	  this.bkp_vertices = this.vertices.clone();
	  this.bkp_listaVertices.clear();

	  ArrayList<Point3f> pp = new ArrayList<Point3f>();
		 
		 Point3f temp = new Point3f();
		 
		 for(int i = 0 ; i<this.vertices.length ;i=i+3)
		{
			temp = new Point3f(this.vertices[i],this.vertices[i+1], this.vertices[i+2]);
			
			if(!pp.contains(temp))
			{
				 pp.add(temp);
				 MyPoint3f myNovo = new MyPoint3f(temp.x,temp.y,temp.z);

				 // Aqui vou colocar o novo ponto
				 this.bkp_listaVertices.add(myNovo);
				 
				  // Agora vou descobrir em quais locais este novo ponto está!
				  this.criaIndicePonto(myNovo);

			}
		}
		
		 
		 
		 return pp;
  }
  

  // Coloca tudo que está no array de float para a collection
  public ArrayList<Point3f> ConvertArrayFloatParaArrayPoint3f(float[] v)
  {
  
	 ArrayList<Point3f> pp = new ArrayList<Point3f>();
	 
	 int j = 0;
	 
	 Point3f temp = new Point3f();
	 
	 for(int i = 0 ; i<v.length ;i++)
	 {
		 // Terminou o ponto (coordenada x)
		 if(i%3==0)
		 {
			 temp.x = v[i];
		 }


		// Coordenada y
		 if(i%3==1)
		 {
			 temp.y = v[i];
		 }

		 // Coordenada z
		 if(i%3==2)
		 {
			 temp.z = v[i];
			
			 // Aqui checo se o ponto já existe
			 if(!pp.contains(temp))
				 pp.add(temp);
			 
			 temp = new Point3f();
			 
		 }
		 
	 }

	 return pp;
  }
  
 
  public void GravaModelo(String file)
  {
	   
	    // As linhas de código abaixo fazem a gravação do modelo modificado em um arquivo .obj!
	    
	    try {
			OBJWriter writer = new OBJWriter(file);
			writer.writeNode(this.partOfTheObject);
			writer.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
  }
  
  
  // Carrega o modelo e retorna um Shape3D
  public Scene getSceneFromModel(String file)
  {
	  // Aqui pergunto qual modelo que vai ser aberto! 
	  String filename = "";


	  if(file=="")
	  {
		   JFileChooser fc = new JFileChooser(new File(filename));
		   fc.showOpenDialog(this);
		   filename =  fc.getSelectedFile().getAbsolutePath();
	  }
	  else
	  {
		  filename = file;
	  }
	     
	  // Load an obj-file.
       ObjectFile f = new ObjectFile(ObjectFile.RESIZE);

	  Scene s = null;
	  
	  try
	    {
	    	s = f.load(filename);
	    }
	    catch (Exception e)
	    {
	      System.out.println("File loading failed:" + e);
	      return null;
	    }  
	  
	  return s;
  }
  
  // Carrega o modelo e retorna um Shape3D
   public Shape3D getShape3dFromScene(String partName,Scene s)
  {
	  
    Hashtable namedObjects = s.getNamedObjects();
    Shape3D shapeObject = (Shape3D) namedObjects.get(partName);
    
    
    // Permite a alteração da aparência
    shapeObject.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    shapeObject.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    
    shapeObject.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    shapeObject.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
    shapeObject.setCapability(Shape3D.ALLOW_BOUNDS_READ);
    shapeObject.setCapability(Shape3D.ALLOW_BOUNDS_WRITE);
    
    shapeObject.setCapability(Shape3D.ALLOW_LOCALE_READ);
    


    return shapeObject;

  }
  
    // Colocando o modelo em um Wireframe
    public void setModelWireFrame(Shape3D m)
    {
     	   // m.setGeometry(arg0);
        
        // Aqui vou criar uma aparence para colocar o modelo como wireframe
        //A black Appearance to display all objects as wire frame models.
        Appearance blackApp = new Appearance();
        setToMyDefaultAppearance(blackApp,new Color3f(0.0f,0.0f,0.0f));

        //The following lines change the display style of the Appearance blackApp
        //to wire frame instead of solid.
        PolygonAttributes polygAttr = new PolygonAttributes();
        polygAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        blackApp.setPolygonAttributes(polygAttr);

        m.setAppearance(blackApp);
    	
    }
    
    

  /**
  * Generates a default surface (Appearance) in a specified colour.
  *
  * @param app      The Appearance for the surface.
  * @param col      The colour.
  */
  public static void setToMyDefaultAppearance(Appearance app, Color3f col)
  {
    app.setMaterial(new Material(col,col,col,col,150.0f));
  }



  //Some light is added to the scene here.
  public void addLight(SimpleUniverse su)
  {

    BranchGroup bgLight = new BranchGroup();

    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
    Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
    Vector3f lightDir1  = new Vector3f(-1.0f,0.0f,-0.5f);
    DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
    light1.setInfluencingBounds(bounds);

    bgLight.addChild(light1);
    su.addBranchGraph(bgLight);
  }

  
  
  public void loadAnotherModel(String file)
  {
	  // Primeiro removendo a geometria do modelo
	  Enumeration<GeometryArray> e  = this.partOfTheObject.getAllGeometries();
	   
	    while (e.hasMoreElements())
	    {
	      GeometryArray geo = (GeometryArray)e.nextElement();
	      
	      this.partOfTheObject.removeGeometry(geo);       
	    }
	  
	  // Agora carregando o arquivo
      ObjectFile f = new ObjectFile(ObjectFile.RESIZE);

	  Scene cenaTemp = this.getSceneFromModel(file);
	  
	  Shape3D novoModelo = this.getShape3dFromScene("main",cenaTemp);
	  
	  
	  // Setando as opções da geometria do modelo
	  novoModelo.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
	  novoModelo.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
	 
	  // Agora colocando a geometria nova no modelo original 
	  GeometryArray g = (GeometryArray) novoModelo.getGeometry();
	  
	  g.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
	  g.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
	  g.setCapability(GeometryArray.BY_REFERENCE);
	
	  GeometryInfo gi = new GeometryInfo(g);
	  
      NormalGenerator ng = new NormalGenerator();
      ng.generateNormals(gi);      
      GeometryArray ga; 

      ga = gi.getGeometryArray(true,false,false); 

      this.partOfTheObject.addGeometry(ga);
       
      // Obtendo a geometria de pontos do Modelo
      this.vertices =  ga.getCoordRefFloat();
  	
      // Convertendo os pontos para um formato melhor...
      this.listaVertices.clear();
      this.listaVertices = this.ConvertArrayFloatParaArrayPoint3f(this.vertices);
      
      this.FazBackup();
	  
  }
  
  
  /**
   * convert Shape3D's geometryArrays  using GeometryInfo. 
   * @param shp - the Shape3D target
   * @param indexed - creates a indexed GeometryArray
   * @param compact - compact, if indexed.
   * @param byRef - create a BY_REFERENCE geometry
   * @param interleaved -  create a INTERLEAVED geometry
   * @param useCoordIndexOnly - force USE_COORD_INDEX_ONLY in indexed Geometries
   * @param nio - Use NIO buffers
   */
   public  void convert(Shape3D shp,boolean indexed,
                                    boolean compact,
                                    boolean byRef,
                                    boolean interleaved,
                                    boolean useCoordIndexOnly,
                                    boolean nio)
   {     
	
	Enumeration<GeometryArray> e  = shp.getAllGeometries();
	   
    while (e.hasMoreElements())
    {
      GeometryArray geo = (GeometryArray)e.nextElement();
      
      shp.removeGeometry(geo);       
      
      GeometryInfo gi = new GeometryInfo(geo);
      NormalGenerator ng = new NormalGenerator();
      ng.generateNormals(gi);      
      GeometryArray ga; 
      if (indexed)
      {
        ga = gi.getIndexedGeometryArray(compact,
                                         byRef,
                                         interleaved,
                                         useCoordIndexOnly,
                                         nio);
      }
      else
      {
        ga = gi.getGeometryArray(byRef,interleaved,nio); 
      }
      shp.addGeometry(ga);
      
     }
   }
   
  
   // Imprime informações sobre o shape
  public void printInfo(Shape3D shp)
  {
	  Enumeration<GeometryArray> e  = shp.getAllGeometries();
	  
	  
   while (e.hasMoreElements())
   {
     Object obj = e.nextElement();
     if (obj instanceof GeometryArray)
     {
      GeometryArray geo = (GeometryArray) obj; //enum.nextElement(); 
      System.out.println("Geometry " + geo.getClass().getName());
      
      int vertexFormat = geo.getVertexFormat();    
      
      System.out.println("VertexCount: " + geo.getVertexCount() );
      System.out.println("ValidVertexCount: " + geo.getValidVertexCount() );
      
      boolean isReference = bInA(GeometryArray.BY_REFERENCE,vertexFormat) ;
      boolean isTC2 = bInA(GeometryArray.TEXTURE_COORDINATE_2,vertexFormat);
      boolean isTC3 = bInA(GeometryArray.TEXTURE_COORDINATE_3,vertexFormat);
      boolean isTC4 = bInA(GeometryArray.TEXTURE_COORDINATE_4,vertexFormat);
      boolean isInterleaved = bInA(GeometryArray.INTERLEAVED,vertexFormat);
      boolean isUSE_NIO_BUFFER = bInA(GeometryArray.USE_NIO_BUFFER,vertexFormat);
      boolean isUSE_COORD_INDEX_ONLY = bInA(GeometryArray.USE_COORD_INDEX_ONLY,vertexFormat);
      boolean isNORMALS = bInA(GeometryArray.NORMALS,vertexFormat);
      
       System.out.println("Is BY_REFERENCE: " + isReference );
       System.out.println("Is TEXCOORD2: " + isTC2 );
       System.out.println("Is TEXCOORD3: " + isTC3 );
       System.out.println("Is TEXCOORD4: " + isTC4 );
       System.out.println("Is INTERLEAVED: " + isInterleaved );
       System.out.println("Is USE_NIO_BUFFER: " + isUSE_NIO_BUFFER );
       System.out.println("Is USE_COORD_INDEX_ONLY: " + isUSE_COORD_INDEX_ONLY );
       System.out.println("Has isNORMALS: " + isNORMALS );       
    }
   }
  }
  
  // Método auxiliar
  private boolean bInA(int b, int a)
  {
    return (a & b) == b;
  }
}


class MyPoint3f extends Point3f
{
	public ArrayList<Integer> aPontos = new ArrayList<Integer>();
	
	public MyPoint3f(float x, float y, float z)
	{
		super(x,y,z);
	}

}

