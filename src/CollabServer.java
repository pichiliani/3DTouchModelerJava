import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*; 

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class CollabServer extends Thread
{
	
	public Shape3D objeto;                     // O objeto completo
	public TriangleArray geometriaModelo;      // A geometria do objeto
	public float[] vertices;                   // os vertices em um array de triângulos
	public ArrayList<Point3f> listaVertices = new ArrayList<Point3f>();  // O arraylist de pontos únicos
	public ArrayList<FingerSphere> listaFingers = new ArrayList<FingerSphere>();  // O arraylist de pontos únicos
	public Load3DExample classeCompleta;
	
	int port = 0;
	
	public CollabServer(Load3DExample classe)  
	{
		this.classeCompleta = classe;
	}
	
	
	public void run() 
	{
		try
		{ 
			int port = 123;
			
			this.IniciaServico(port);
		}
		catch (IOException ex)
		{
			   System.out.println("Servidor finalizado!" );
		}

	}
	
	public void IniciaServico(int port) throws IOException {
		// Abre o servidor na porta escolhida
        ServerSocket ss = new  ServerSocket(port);
		System.out.println("Collaboration server listening on port " + port +".CTRL+C to finish.") ;


		// Configurando os arquivos de logs
		try
		{ 
			
			CollabServerT c;
			// A cada nova requisão uma nova Thread é criada
			while (true)
			{
				c = new CollabServerT (ss.accept(),this);
				c.start();
				
				// clientVector.addElement(c);
			}
			
		}
		catch (Exception ex)
		{
        		ex.printStackTrace();
		}
		
	}

	
	public static void main (Load3DExample classe) 
	{

			CollabServer s = new CollabServer(classe);
			
			s.start();
		
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

class CollabServerT extends Thread 
{
	Socket client;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    
    private InputStreamReader isEYE;
    private OutputStreamWriter osEYE;
    
    private String CON = "OBJ";
    private CollabServer acesso;
    
	
	// Construtor da classe
	CollabServerT (Socket client,CollabServer s) throws SocketException 
	{
		// Preenche os campos e seta a priopridade desta Thread
		this.client = client;
		setPriority( NORM_PRIORITY - 1);
		this.acesso = s;
	
	}

	public void run() 
	{
        try {
            this.connect();
            boolean clientTalking = true;
            
            Object clientObject = null;
            
            //a loop that reads from and writes to the socket
            while (clientTalking) 
            {
            		BufferedReader inBR = new BufferedReader(isEYE); 
                    
                    String text = inBR.readLine(); 
                    
                    if ( (inBR == null) || (text==null)) 
                        clientTalking = false;
                    else
                    {
                      // Order de recebimento dos dados do ForcePad
                      // SEQUENCIA DE DADOS: Finger %d, XRaw %d, YRaw %d, ZRaw %d, Force %d, evento
                      
                      	  // evento = 1 -> TOUCH_DOWN
                          // evento = 2 -> TOUCH_MOVE
                          // evento = 3 -> TOUCH_UP
                      
                      // Dedo (0-4), X, Y, Z, Força, evento
                    	
                    	// System.out.println("Texto recebido:" + text);
                    	
                    	Finger f = new Finger(text);
                    	
                    	
                    	// if(f.evento==1)
                    	//	this.TOUCH_DOWN(f.id, f.x, f.y, f.z, f.force);

                    	if(f.evento==2)
                    		this.TOUCH_MOVE(f.id, f.x, f.y, f.z, f.force);
                    	
                    	// if(f.evento==3)
                    	//	this.TOUCH_UP(f.id);

                    		
                    	
                    }
            }
            
        } catch (Exception e) {
        	// System.out.println("Conexão fechada pelo cliente.");
        	e.printStackTrace();
        }
        this.disconnect();
	}

    
	// when connection starts - opens streams and calls server to notify
    // all other currently connected clients about the joining of this user.
    private void connect () throws java.io.IOException	{

    	// Aqui verifica qual é o tipo conexão
    	try 
    	{
    		is = new ObjectInputStream(client.getInputStream());
            os = new ObjectOutputStream(client.getOutputStream());
	
            this.CON = "OBJ";
            os.flush();
    		
    	} catch (IOException e) 
    	{
            isEYE = new InputStreamReader(client.getInputStream());
            osEYE = new OutputStreamWriter(client.getOutputStream());

            this.CON = "TXT";
            osEYE.flush();
    	}
    	
    }   
    
    //  when connection ends - closes streams, stops this thread and notifies
    // server about the disconnection of this client.
    private void disconnect () {
        try {
            
        	if(CON == "OBJ") // Caso conexão ARGO ou GEF
        	{
        		is.close();
        		os.close();
        	} 
        	else
        	{
        		isEYE.close();
        		osEYE.close();
        	}
            
            client.close();


            this.interrupt();
        } catch (IOException e) {
            // e.printStackTrace();
        } 
    }           // disconnect

    
    public void TOUCH_DOWN(int id, int x, int y,  int z, int force)
    {
    	// REMOVENDO TEMPORARIAMENTE PARA NÃO MOSTRAR OS FINGERS DEVIDO A PROBLEMA DE PRECISÃO!
    	// FingerSphere f = (FingerSphere) this.acesso.listaFingers.get(id);
    	// f.show();
    }
    
    public void TOUCH_MOVE(int id, int x, int y,  int z, int force)
    {
    	
    	FingerSphere f = (FingerSphere) this.acesso.classeCompleta.listaFingers.get(id);

    	
    	// Faz o cálculo para saber o quando de incremento deve zer colocado no eixo X e Y
    	/*  LIMITES DO DISPOSITIVO (DADOS EMPÍRICOS)
    	 *   X-> DE 1024 A 5900. CONVERTENDO EM VALORES UTILIZÁVEIS: (5900-1024) = 4876
    	 *   Y-> DE 1024 A 4700. CONVERTENDO EM VALORES UTILIZÁVEIS: (4700-1024) = 3676
    	 *   Z-> DE 0 A 220. 
    	 *
    	 *   X, Y E Z SÃO CONVERTIDOS EM PORCENTUAL INTERNAMENTE!
    	 */
    	
    	float inc_x = this.AdequaCoordenadas(x,1024,4876,0.93f,f.centro.x);
    	float inc_y = this.AdequaCoordenadas(y,1024,3676,0.90f,f.centro.y);
    	
    	// float inc_z = this.AdequaCoordenadasZ(z,0,220,0.001f);
    	
    	// O valor 40 para o inicio é para balancear a sensitividade do aparelho
    	// float inc_z = this.AdequaCoordenadasZ(z,65,220,0.001f);

    	 // O valor 12 para o inicio é para balancear a sensitividade do aparelho
    	// float inc_z = this.AdequaCoordenadasZ(force,15,1700,0.001f);
    	// float inc_z = this.AdequaCoordenadasZ(force,10,1700,f.sensitividade);
    	float inc_z = this.AdequaCoordenadasZ(force,5,700,f.sensitividade);
    	
    	
    	// Aqui vejo se é para fundar (1.0f) ou levantar (-1.0f)
    	inc_z = inc_z  * f.afundarLevantar; 
    	
    	// Atualizando o finger
    	// f.MoveTo(inc_x, inc_y,0.0f);
    	f.MoveToFast(inc_x, inc_y,0.0f);

    	// TRABALHANDO... Movendo o ponto para o vértice mais próximo
    	// f.MoveToClosestVertice(inc_x, inc_y,0.0f, this.acesso.listaVertices);
    	
    	// IDÉIA:  AO INVÉS DE MOVER LIVREMENTE O FINGER EU MOVO PARA O PONTO MAIS
    	// PRÓXIMO DO CENTRO DO FINGER!
    	
        // Aqui vou tentar detectar quais pontos estão debaixo do Finger
    	
    	
    	// Nova maneira de mudar os pontos dentro do circulo! Mais rápida!
    	// f.mudaPontosDentro(this.acesso.classeCompleta.listaVertices, this.acesso.classeCompleta.vertices,inc_z);
    	
		
    	
    	// O código abaixo funciona sem problemas apesar o código acima parecer que é mais rápido
    	
    	ArrayList<Point3f> dentro = f.getPontosDentro(this.acesso.classeCompleta.listaVertices);
    	// ArrayList<Point3f> dentro = f.MoveToClosestVertice(inc_x, inc_y,0.0f,this.acesso.classeCompleta.listaVertices);
    	
    	// System.out.println("Qtd pontos dentro:" + String.valueOf(dentro.size()) );
		
		// Maneira nova de modificar os pontos!
		for( Point3f p : dentro )
		{
			// Primeiro alterando o ponto no arraylist
			Point3f novo = new Point3f(p.x,p.y, p.z+inc_z);
			
						
			int pos = this.acesso.classeCompleta.listaVertices.indexOf(p);
			  
			  if(pos==-1)
			  {
				  continue;
			  }
			  
			  // Primeiro achar o ponto antigo no arrayList e modificá-lo
			  this.acesso.classeCompleta.listaVertices.set(pos, novo);
			  
			  
			  // Agora vou alterar apenas os dados do array que eu precisar alterar
			  
			  	// Ao invës de percorrer todo o vetor, pego os índices direto do array de backup
			    // Os elementos de listaVertices e bkp_listaVertices estão na mesma posição!  
			  	
			  	MyPoint3f my = 	this.acesso.classeCompleta.bkp_listaVertices.get(pos) ;
			    
			  	for(int indicePonto: my.aPontos)
			  	{
			  		// O indice dá a coordenada x e preciso alterar apenas a coordenada z
			  		this.acesso.classeCompleta.vertices[indicePonto+2] = this.acesso.classeCompleta.vertices[indicePonto+2] +inc_z;
			  	}
			  		
			  	
			  	/*
				for(int i = 0 ; i<this.acesso.classeCompleta.vertices.length ;i=i+3)
				{
					Point3f doArrayVertices  = new Point3f(this.acesso.classeCompleta.vertices[i],this.acesso.classeCompleta.vertices[i+1], this.acesso.classeCompleta.vertices[i+2]);
					
					// Se achou o ponto
					if( (p.x==doArrayVertices.x) && (p.y==doArrayVertices.y) && (p.z==doArrayVertices.z) )
					{
					// Mudando apenas o componente Z!
						this.acesso.classeCompleta.vertices[i+2] = this.acesso.classeCompleta.vertices[i+2] + +inc_z;
					}
					
				}
				*/
			  
				
				
		}
		
		
		/************** INICIO DO BACKUP DO CÓDIGO BOM */
		
		/*
		 * ArrayList<Point3f> dentro = f.getPontosDentro(this.acesso.classeCompleta.listaVertices);
    	// ArrayList<Point3f> dentro = f.MoveToClosestVertice(inc_x, inc_y,0.0f,this.acesso.classeCompleta.listaVertices);
    	
    	
		// Maneira nova de modificar os pontos!
		for( Point3f p : dentro )
		{
			// Primeiro alterando o ponto no arraylist
			Point3f novo = new Point3f(p.x,p.y, p.z+inc_z);
			
						
			int pos = this.acesso.classeCompleta.listaVertices.indexOf(p);
			  
			  if(pos==-1)
			  {
				  continue;
			  }
			  
			  // Primeiro achar o ponto antigo no arrayList e modificá-lo
			  this.acesso.classeCompleta.listaVertices.set(pos, novo);
			  
			  // Agora vou alterar apenas os dados do array que eu precisar alterar
			  
			  
				for(int i = 0 ; i<this.acesso.classeCompleta.vertices.length ;i=i+3)
				{
					Point3f doArrayVertices  = new Point3f(this.acesso.classeCompleta.vertices[i],this.acesso.classeCompleta.vertices[i+1], this.acesso.classeCompleta.vertices[i+2]);
					
					// Se achou o ponto
					if( (p.x==doArrayVertices.x) && (p.y==doArrayVertices.y) && (p.z==doArrayVertices.z) )
					{
					// Mudando apenas o componente Z!
						this.acesso.classeCompleta.vertices[i+2] = this.acesso.classeCompleta.vertices[i+2] + +inc_z;
					}
					
				}
			  
				
		}
		 */
		
		/************** FIM DO BACKUP DO CÓDIGO BOM */
		
		/*
		 // Aqui temos a maneira tradicional de modificar os pontos (lenta e muda todo o array)
		for( Point3f p : dentro )
		{
			if(p.z > 0.0f)
			{
				// System.out.println("Novo ponto dentro!");
				
				 
				Point3f novo = new Point3f(p.x,p.y, p.z+inc_z);
				
				// Atualizando os pontos do modelo
				
				// TODO: ARRRUMAR ESTE CARA PARA PEGAR APENAS OS PONTOS Z E Y COM O Z MAIS PRÓXIMO DO FINGER!				
				float[] modificar = this.MudaPontoNoVetor(p, novo, this.acesso.classeCompleta.listaVertices, this.acesso.classeCompleta.vertices);
				
				if(modificar!=null)
				{
					this.acesso.classeCompleta.vertices = modificar;
					
				}
				
				
			}
		}
		*/
    	
    	
    	
    }
    public void TOUCH_UP(int id)
    {
    	// REMOVENDO TEMPORARIAMENTE PARA NÃO MOSTRAR OS FINGERS DEVIDO A PROBLEMA DE PRECISÃO!
    	// FingerSphere f = (FingerSphere) this.acesso.listaFingers.get(id);
    	// f.hide();
    }

    private float AdequaCoordenadasZ(int z, int limite_ini, int limite_fim, float incremento)
    {
    	float retornoIncremento;
    	
    	int z_escala = z-limite_ini; 
    	float percentual = (float) (z_escala*100)/limite_fim;
    	
    	retornoIncremento = incremento*percentual;
    			
    	return retornoIncremento;
    }

    
    private float AdequaCoordenadas(int x, int limite_ini, int limite_fim, float max_tela, float ponto_central)
    {
    	float retornoIncremento;

    	
    	// COM ESTA CONTA A ESCALA DE VALORES DO EIXO X DO DISPOSITOV É DE 0 -> 4864
    	int x_escala = x-limite_ini;  
    	float x_percentual = (float) (x_escala*100)/limite_fim;

    	
    	if(x_percentual >= 50.f)
    	{
    		float diff = x_percentual - 50.0f;  // SO DE 0% A 50% 
    		diff = diff*2;
    		float relativo_centro = (max_tela*diff)/100.0f;
    		
    		float p1 = ponto_central;       // atual
    		float p2 = relativo_centro; // novo
    		
    		
    		if( p2 >= p1)
    			retornoIncremento = (p2 - p1);
    		else
    			retornoIncremento =  - (p1 - p2);
    	
    			
    	}
    	else
    	{
    		float diff = x_percentual; // SO DE 0% A 50%
    		diff = diff*2;

    		float relativo_centro = ((max_tela)*diff)/100.0f;
    		relativo_centro  = (-max_tela) + relativo_centro;
    		
    		
    		float p1 = ponto_central*(-1);       // atual
    		float p2 = relativo_centro*(-1); // novo
    		
    		
    		if( p2 >= p1)
    			retornoIncremento = (p1 - p2);
    		else
    			retornoIncremento =  - (p2 - p1);
    	}

    	return retornoIncremento;
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

    

}

class Finger
{

  public int id,x,y,z,force,evento;
  
  public Finger(String s)
  {
		 String[] result = s.split(",");
     
     this.id = Integer.valueOf(result[0]);
     this.x = Integer.valueOf(result[1]);
     this.y = Integer.valueOf(result[2]);
     this.z = Integer.valueOf(result[3]);
     this.force = Integer.valueOf(result[4]);
     this.evento = Integer.valueOf(result[5]);
  
  }

}


