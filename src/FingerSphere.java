import java.util.ArrayList;
import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;


public class FingerSphere {

	private int id; // O id de cada Finger
	private boolean fVisible = true;
	private float visibleTransparency = 0.8f;
	private Sphere tSphere;
    private float[] vertices;
	private ArrayList<Point3f> listaVertices = new ArrayList<Point3f>();
	public Point3f centro = new Point3f(0.0f,0.0f,0.0f);
	public float raio;
	public float afundarLevantar = 1.0f;
	
	public float sensitividade = 0.003f;
	
	
	public FingerSphere(int i)
	{
		this.id = i;
		
	}
	
	public TransformGroup getFingerSphere()
	{
		 Appearance blackApp = new Appearance();
    	this.setToMyDefaultAppearance(blackApp,new Color3f(0.0f,0.0f,0.0f));
		  
	    TransparencyAttributes ta = new TransparencyAttributes();
	    ta.setTransparencyMode(TransparencyAttributes.BLENDED);
	    
	    ta.setTransparency(1.0f);
	    fVisible = false;
	    
	    // ta.setTransparency(1.0f);
	    blackApp.setTransparencyAttributes(ta);
	    
	    //A sphere to which Appearance, i.e. the texture is assigned.
	    tSphere = new Sphere(0.05f,Sphere.GEOMETRY_NOT_SHARED ,10,blackApp);
	    
	    
	    this.raio = tSphere.getRadius();
	    
	    Shape3D s = tSphere.getShape();

	    convert(s,false,false,true,false,false,false);
	    
	    s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
	    s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	    s.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
	    s.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
	    
	    TriangleStripArray g = (TriangleStripArray) tSphere.getShape().getGeometry();
	    
	    g.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
	    g.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
	    g.setCapability(GeometryArray.BY_REFERENCE);
	    
	    // Obtendo a geometria de pontos do Modelo
	    this.vertices =  g.getCoordRefFloat();
		
	    // Convertendo os pontos para um formato melhor...
	    this.listaVertices = this.ConvertArrayFloatParaArrayPoint3f(this.vertices);
	    
	    // tSphere = new Sphere(0.05f,Sphere.GEOMETRY_NOT_SHARED | Sphere.GENERATE_NORMALS | Sha ,50,blackApp);
	    
	    //The transformation group for the sphere.
	    TransformGroup tgSphere = new TransformGroup();
	    tgSphere.addChild(tSphere);
	    
	    return tgSphere;
		
	}
	
	public Sphere getSphere()
	{
		return this.tSphere;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public boolean isVisible()
	{
		return fVisible;
	}
	
	
	public void show()
	{
	    Appearance blackApp = new Appearance();
    	this.setToMyDefaultAppearance(blackApp,new Color3f(0.0f,0.0f,0.0f));

		TransparencyAttributes ta = new TransparencyAttributes();
	    ta.setTransparencyMode(TransparencyAttributes.BLENDED);
	    ta.setTransparency(visibleTransparency); // Transparente

	    blackApp.setTransparencyAttributes(ta);

	    
	    tSphere.setAppearance(blackApp);
	    
	    fVisible = true;
	}
	
	public void hide()
	{
	    Appearance blackApp = new Appearance();
    	this.setToMyDefaultAppearance(blackApp,new Color3f(0.0f,0.0f,0.0f));

		TransparencyAttributes ta = new TransparencyAttributes();
	    ta.setTransparencyMode(TransparencyAttributes.BLENDED);

	    ta.setTransparency(1.0f); // Invisível
	    blackApp.setTransparencyAttributes(ta);
	    
	    tSphere.setAppearance(blackApp);
	    
	    fVisible = false;
	}
	
	public void apagaMasMovimenta()
	{
	    Appearance blackApp = new Appearance();
    	this.setToMyDefaultAppearance(blackApp,new Color3f(0.0f,0.0f,0.0f));

		TransparencyAttributes ta = new TransparencyAttributes();
	    ta.setTransparencyMode(TransparencyAttributes.BLENDED);

	    ta.setTransparency(1.0f); // Invisível
	    blackApp.setTransparencyAttributes(ta);
	    
	    tSphere.setAppearance(blackApp);
	}

	
	
	// Este método move o finger para determinada posição em relação
	// à posição atual
	public void MoveTo(float variacaox, float variacaoy, float variacaoz)
	{
		if(this.isVisible())
		{
			
			for( Point3f p : this.listaVertices  )
			{
				Point3f novo = new Point3f(p.x+variacaox,p.y + variacaoy, p.z + variacaoz );
				
				float[] modificar = this.MudaPontoNoVetor(p, novo, listaVertices, this.vertices);
				
				if(modificar!=null)
				{
					// Atualizando os pontos do modelo
					this.vertices = modificar;	
				}
				
				
			}
			
			
			TriangleStripArray g = (TriangleStripArray) this.tSphere.getShape().getGeometry();
			
			g.setCoordRefFloat(this.vertices);
			
			this.centro.x = centro.x + variacaox; 
			this.centro.y = centro.y + variacaoy;
			this.centro.z = centro.y + variacaoz;
			
		}
	}

	// Este método move o finger para determinada posição em relação
	// à posição atual de forma rápida 
	public void MoveToFast(float variacaox, float variacaoy, float variacaoz)
	{
		if(this.isVisible())
		{
			
			/*
			// Primeiro atualizando o arraylist this.listaVertices
			for( Point3f p : this.listaVertices  )
			{
					p = new Point3f(p.x+variacaox,p.y + variacaoy, p.z + variacaoz );
			}
			
			// Agora atualizando o array this, vertices
			
			for(int i = 0 ; i<this.vertices.length ;i=i+3)
			{
				this.vertices[i] = this.vertices[i] + variacaox;
				this.vertices[i+1] = this.vertices[i+1] + variacaoy;
				this.vertices[i+2] = this.vertices[i+2] + variacaoz;
			}
	*/
			
			this.centro.x = centro.x + variacaox; 
			this.centro.y = centro.y + variacaoy;
			this.centro.z = centro.y + variacaoz;
			
		}
	}

	
	// Este método move o finger inteiro para um determinado ponto
	public void MoveToPoint(Point3f p)
	{
		if(this.isVisible())
		{
			
			// Primeiro calculando os incrementos de cada componente (x,y e z)
			Point3f p1 = this.centro;       // atual
			Point3f p2 = p; // novo
			
			float mod_x = 0.0f; // Incremento ou decremento a ser passado para o x no novo ponto
			float mod_y = 0.0f; // Incremento ou decremento a ser passado para o y no novo ponto
			float mod_z = 0.0f; // Incremento ou decremento a ser passado para o z no novo ponto
			
    		
			// Primeiro o X!
			if(p1.x >= 0.0f)
			{
	    		if( p2.x >= p1.x)
	    			mod_x = (p2.x - p1.x);
	    		else
	    			mod_x =  - (p1.x - p2.x);
			}
			else
			{
				float aux_p1 = p1.x *(-1.0f);
				float aux_p2 = p2.x *(-1.0f);
				
	    		if( aux_p2 >= aux_p1)
	    			mod_x = (aux_p2 - aux_p1);
	    		else
	    			mod_x =  - (aux_p1 - aux_p2);
			}
			
			// Agora o Y!
			if(p1.y >= 0.0f)
			{
	    		if( p2.y >= p1.y)
	    			mod_y = (p2.y - p1.y);
	    		else
	    			mod_y =  - (p1.y - p2.y);
			}
			else
			{
				float aux_p1 = p1.y *(-1);
				float aux_p2 = p2.y *(-1);
				
	    		if( aux_p2 >= aux_p1)
	    			mod_y = (aux_p2 - aux_p1);
	    		else
	    			mod_y =  - (aux_p1 - aux_p2);
			}

			// Agora o Z!
			if(p1.z >= 0.0f)
			{
	    		if( p2.z >= p1.z)
	    			mod_z = (p2.z - p1.z);
	    		else
	    			mod_z =  - (p1.z - p2.z);
			}
			else
			{
				float aux_p1 = p1.z *(-1.0f);
				float aux_p2 = p2.z *(-1.0f);
				
	    		if( aux_p2 >= aux_p1)
	    			mod_z = (aux_p2 - aux_p1);
	    		else
	    			mod_z =  - (aux_p1 - aux_p2);
			}
		
			this.MoveTo(mod_x, mod_y, mod_z);
			
		}
	}
	
	
	
	// Este método vai mover o finger para o vértice mais próximo
	public ArrayList<Point3f> MoveToClosestVertice(float x_pos,float y_pos,float z_pos,ArrayList<Point3f> aListaPontosModelo)
	{
		
		ArrayList<Point3f> ret = new ArrayList<Point3f>();
		
		
		// Primeiro efetivamente movo o finger para a posição dele!
			// this.MoveTo(x_pos, y_pos, z_pos);
			
			// Agora vou calcular qual é o vértice mais próximo
			
			// Crio um ponto para armazenar a posição atual do finger
			Point3f novo_ponto = this.centro;
			
			Point3f ponto_mais_perto = null;
			float menor_distancia = 99.9f;
			
			
			// No loop abaixo obtenho o ponto mais perto da posição atual do finger
			
			for( Point3f p : aListaPontosModelo  )
			{
				if(p.z > 0f)
				{
					if( p.distance(novo_ponto) <= menor_distancia)
					{
						menor_distancia = p.distance(novo_ponto);
						ponto_mais_perto = p;
					}
				}
			}
			
			// this.MoveToPoint(ponto_mais_perto);
			
			ret.add(ponto_mais_perto);
			
		return ret;
		
	}
	
	
	// Aqui vou retornar os pontos dentro desta esfera para ver quais eu afundo ou puxo
	public void mudaPontosDentro(ArrayList<Point3f> aListaPontos, float[] vetor_pontos, float inc_z)
	{
				
		for(Point3f p : aListaPontos)
		{
			if(p.z > 0.0f)
			{
				if( this.ponto_no_criculo(p))
				{
					Point3f encontrado = new Point3f(p.x,p.y, p.z);
					
					// Ok, achou o ponto P. Agora preciso modificá-lo!
					p = new Point3f(p.x,p.y, p.z+inc_z);
					
					 // Agora vou alterar apenas os dados do array que eu precisar alterar
					  
					for(int i = 0 ; i<vetor_pontos.length ;i=i+3)
					{
						Point3f doArrayVertices  = new Point3f(vetor_pontos[i],vetor_pontos[i+1], vetor_pontos[i+2]);
						
						// Se achou o ponto
						if( (encontrado.x==doArrayVertices.x) && (encontrado.y==doArrayVertices.y) && (encontrado.z==doArrayVertices.z) )
						{
						// Mudando apenas o componente Z!
							vetor_pontos[i+2] = vetor_pontos[i+2] + +inc_z;
						}
					}
				}
			}
		}
	}
	
	// Aqui vou retornar os pontos dentro desta esfera para ver quais eu afundo ou puxo
	public ArrayList<Point3f> getPontosDentro(ArrayList<Point3f> checarPontos)
	{
		
		ArrayList<Point3f> ret = new ArrayList<Point3f>();
		
		for(Point3f p : checarPontos)
		{
			if(p.z > 0.0f)
			{
				if( this.ponto_no_criculo(p))
				{
					ret.add(p);
				}
			}
		}
		
		
		return ret;
	}
	
	private ArrayList<Point3f> removeRepedidosXY(ArrayList<Point3f> ret, Point3f p)
	{
		int primeira_pos = -1;
		ArrayList<Point3f> remover = new ArrayList<Point3f>(); 
		
		for( Point3f procurado: ret)
		{
			// Achou os pontos nas coordenadas x e y
			if(  (procurado.x == p.x) && (procurado.y == p.y)  )
			{ 
				// Se for o primeiro ponto, OK!
				if(primeira_pos==-1 )
				{
					primeira_pos = 1;
				}
				else
				{
					// Achou uma repedição. Preciso guardar para remover depois!
					remover.add(procurado);
				}
				
			}
			
		}
	
		return remover;
	}
	
	
	public boolean ponto_no_criculo(Point3f p)
	{
		float square_dist = ( (centro.x - p.x)*(centro.x - p.x) ) + ( (centro.y - p.y)*(centro.y - p.y) );
		
		return square_dist <= (this.raio*this.raio); 
	}
	
	
	
	// Aqui vou acertar o tamanho desta esfera de acordo com a variação do zoom 
	// feito pelo usuário. Objetivo: manter a esfera sempre do mesmo tamanho
	public void acertaTamanho(float variacao)
	{
	
	}
	
		
	// Método auxiliar
	private void setToMyDefaultAppearance(Appearance app, Color3f col)
    {
	    app.setMaterial(new Material(col,col,col,col,150.0f));
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

	  // Este método muda o ponto escolhido do antigo para o novo tanto no ArrayList como no vetor
	  // retona o vetor de floats modificado para poder atualziar o modelo
	  public float[] MudaPontoNoVetor(Point3f antigo, Point3f novo, ArrayList<Point3f> aLista, float[] vetor)
	  {
		  float[] retorno = vetor;
		  
		  int pos = aLista.indexOf(antigo);
		  
		  if(pos==-1)
		  {
			  return null;
		  }
		  
		  // Primeiro achar o ponto antigo no arrayList e modificá-lo
		  aLista.set(pos, novo);
		  
		  // Agora modifico todos os locais que este ponto aparece
		  
	     Point3f temp = new Point3f();
	     
		 for(int i = 0 ; i<retorno.length ;i++)
		 {
			 // Terminou o ponto (coordenada x)
			 if(i%3==0)
			 {
				 temp.x = retorno[i];
			 }


			// Coordenada y
			 if(i%3==1)
			 {
				 temp.y = retorno[i];
			 }

			 // Coordenada z
			 if(i%3==2)
			 {
				 temp.z = retorno[i];
				
				 // Aqui checo se o ponto existe
				 // Caso seja ele, atualizo para o novo valor!
				 if(temp.equals(antigo))
				 {
					 retorno[i-2] = novo.x;
				     retorno[i-1] = novo.y;
				     retorno[i] = novo.z;
					 
				 }
				 temp = new Point3f();
				 
			 }
			 
		 }
		  
		  return retorno;
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
	   
	  
	  
}
