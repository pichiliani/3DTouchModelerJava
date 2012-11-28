import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.Bounds;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;


public class ThreadManipulaModelo extends Thread 
{
	
	private Shape3D objeto;                     // O objeto completo
	private TriangleArray geometriaModelo;      // A geometria do objeto
	private float[] vertices;                   // os vertices em um array de triângulos
	private ArrayList<Point3f> listaVertices = new ArrayList<Point3f>();  // O arraylist de pontos únicos
	private ArrayList<FingerSphere> listaFingers = new ArrayList<FingerSphere>();  // O arraylist de pontos únicos
	
	
	public ThreadManipulaModelo(Shape3D o, TriangleArray geo, float[] v, ArrayList<Point3f> l, ArrayList<FingerSphere> f )
	{
	
		this.objeto = o;
		this.geometriaModelo = geo;
		this.vertices = v;
		this.listaVertices = l;
		this.listaFingers = f;
		
	}
	
	public void run() 
	{
		
		// while(true)
		// {
			
			try {
				Thread.sleep(1000);	
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		    
			// As linhas abaixo colocam o modelo em cor verde
			/* 
			Appearance lightBlueApp = new Appearance();
			lightBlueApp.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		    setToMyDefaultAppearance(lightBlueApp,new Color3f(0.2f,0.5f,0.2f));
		    this.objeto.setAppearance(lightBlueApp);
			*/
			
			// AS linhas abaixo mostram um exemplo de mdificação de pontos da geometria da figura
			/*
			for( Point3f p : this.listaVertices  )
			{
				Point3f novo = new Point3f(p.x+0.20f,p.y, p.z);
				
				// Atualizando os pontos do modelo
				this.vertices = this.MudaPontoNoVetor(p, novo, listaVertices, this.vertices);
			}
			
			this.geometriaModelo.setCoordRefFloat(this.vertices);
			*/
			
			//  FingerSphere f = (FingerSphere) this.listaFingers.get(0);
			//  f.MoveTo(0.2f,0.2f);
			 
			 // f.hide();
			
			for( Point3f p : this.listaVertices  )
			{
				System.out.println("Ponto do modelo - x: " + String.valueOf(p.x) + " y: " + String.valueOf(p.y) + " z :" + String.valueOf(p.z) );
			} 
			
			FingerSphere f = (FingerSphere) this.listaFingers.get(0);
			
			System.out.println("Centro - x: " + String.valueOf(f.centro.x) + " y: " + String.valueOf(f.centro.y) + " z: " + String.valueOf(f.centro.z) );
			
			System.out.println("Raio: " + String.valueOf(f.raio) );
			
			Bounds limites  = this.objeto.getBounds();
			
			// System.out.println("Bounds:" + limites.toString());
			
			
			 
			ArrayList<Point3f> dentro = f.getPontosDentro(this.listaVertices);
			
			for( Point3f p : dentro )
			{
				System.out.println("Ponto dentro:" + String.valueOf(p.x));
			}
			
			
			/*
			while(true)
			{
				try {
					Thread.sleep(1000);
					
					Point3f p  = (Point3f) this.listaVertices.get(0);
					
					System.out.println("Primeiro ponto:" + String.valueOf(p.x));
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			*/
			
		
		// }
	 
		
	}
	
	public static void setToMyDefaultAppearance(Appearance app, Color3f col)
	  {
	    app.setMaterial(new Material(col,col,col,col,120.0f));
	  }

	 
	  // Este método muda o ponto escolhido do antigo para o novo tanto no ArrayList como no vetor
	  // retona o vetor de floats modificado para poder atualziar o modelo
	  public float[] MudaPontoNoVetor(Point3f antigo, Point3f novo, ArrayList<Point3f> aLista, float[] vetor)
	  {
		  float[] retorno = vetor;
		  
		  // Primeiro achar o ponto antigo no arrayList e modificá-lo
		  aLista.set(aLista.indexOf(antigo), novo);
		  
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

}
