package TresEnRayaOnline;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTresEnRaya {
	//Creamos los Serversockets y los sockets para poder establecer la conexión
	private static ServerSocket serverAddr = null;
	private static ServerSocket serverAddr2 = null;
	private static Socket sc= null;
	private static Socket sc2= null;
	private static Socket scO= null;
	private static Socket scO2= null;
	//Crea la plantilla que van a ver los jugadores
	private static char[][] plantilla;
	//Establece el turno de los jugadores
	private static int turno=0;
	//Crea los jugadores con la ficha de cada uno
	private static final char J1='X';
	private static final char J2='O';
	private static String plantillaTexto;
	//jugador 1
	    private  static OutputStream ostream    =null;
	    private static ObjectOutputStream out;
	    private static ObjectOutputStream outO;
	    private static  InputStream istream;
		private static ObjectInputStream in ;
	//jugador 2
		 private  static OutputStream ostream2   =null;
		 private static ObjectOutputStream out2;
		 private static ObjectOutputStream outO2;
		 private static  InputStream istream2;
		 private static ObjectInputStream in2 ;
	public static void main ( String [] args) {
		plantilla= plantillaPrincipal(new char[3][3]);
		try {
			//Creamos la conexión
			serverAddr = new ServerSocket(2505);
			serverAddr2 = new ServerSocket(2508);
			sc=serverAddr.accept();
		
			//una vez haceptada la conexion, mandar a los dos jugadores un mensaje de empezar
		
		
		
		    sc2=serverAddr.accept();
		    
			//inicalizar in y out jugador 1
			 out=new ObjectOutputStream(sc.getOutputStream());
			 out.writeInt(1);
			 out.writeObject(plantilla);
			 out.flush();
			 istream = sc.getInputStream();
			 in = new ObjectInputStream(istream);
			 
			//inicializar in y out jugador 2
			 out2=new ObjectOutputStream(sc2.getOutputStream());
			 out2.writeInt(2);
			 out2.writeObject(plantilla);
			 out2.flush();
			 istream2 = sc2.getInputStream();
			 in2 = new ObjectInputStream(istream2);
			 ServidorTresEnRaya.turno=1;
			 
			 scO=serverAddr2.accept();
			 scO2=serverAddr2.accept();
			 
			 outO=new ObjectOutputStream(scO.getOutputStream());
			 outO2=new ObjectOutputStream(scO2.getOutputStream());
		//plantillaPrincipal(plantilla=new char[3][3]);
			 while(true) {
				 plantillaTexto="";
				 if(ServidorTresEnRaya.turno==1) {
					 //leer desde jugador 1 y escribir el dato
					int boton=in.readInt();
					int boton1=in.readInt();
					ControladorTres.jugar(plantilla, boton, boton1, J1);
					ServidorTresEnRaya.mostrarPlantilla(plantilla);
					int ganador=ControladorTres.hayGanador(plantilla);
					boolean casillasLibre=ControladorTres.hayCasillasLibres(plantilla);
					//cambio la plantilla a texto
					plantillaTexto=cambiarPlantillaTexto(plantilla);
					//Escribe el caracter del jugador 1
					outO.writeUTF(plantillaTexto);
					outO2.writeUTF(plantillaTexto);
					 //Escribe si es ganador o no
					out.writeInt(ganador);
					 //scribe el booleano de si la casilla está libre o no
					out.writeBoolean(casillasLibre);
					 //Ejecuta la orden de escritura con el metodo flush
					out.flush();
					outO.flush();
					outO2.flush();
					
					//al metodo le pasas posicion de boton y J1
					//devuelve boolean de ganado / perdido
					
					if(ganador==1 || ganador==2) {
						out.writeInt(ganador);
						ServidorTresEnRaya.turno=1;
					}else {
						ServidorTresEnRaya.turno=2;
					}
					
				 }else {
					 //leer desde jugador 2 y escribir el dato
					int boton=in2.readInt();
					int boton1=in2.readInt();
					ControladorTres.jugar(plantilla, boton, boton1, J2);
					ServidorTresEnRaya.mostrarPlantilla(plantilla);
					int ganador=ControladorTres.hayGanador(plantilla);
					boolean casillasLibre=ControladorTres.hayCasillasLibres(plantilla);
					//cambio la plantilla a texto
					plantillaTexto=cambiarPlantillaTexto(plantilla);
					 //Escribe el caracter del jugador 2
					outO2.writeUTF(plantillaTexto);
					outO.writeUTF(plantillaTexto);
					 //Escribe si es ganador o no
					out2.writeInt(ganador);
					 //Escribe el booleano de si la casilla está libre o no
					out2.writeBoolean(casillasLibre);
					 //Ejecuta la orden de escritura con el metodo flush
					out2.flush();
					outO.flush();
					outO2.flush();
					
					 //al metodo le pasas posicion de boton y J2
					//devuelve boolean de ganado / perdido
					 
					if(ganador==1 || ganador==2) {
						out.writeInt(ganador);
						ServidorTresEnRaya.turno=2;
					}else {
						ServidorTresEnRaya.turno=1;
					}
					
					
				 }
				 
			 }
//			in.readInt();
//			in2.readInt();
		
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	}

	public static String cambiarPlantillaTexto(char a[][]) {
		String texto="";
		for (int contadorFilas=0;contadorFilas<a.length;contadorFilas++)
		{
			for (int contadorColumnas=0;contadorColumnas<a[contadorFilas].length;contadorColumnas++) {
			
				texto=texto+""+a[contadorFilas][contadorColumnas];
			}
		}
		
		return texto;
	}
	/*
	*	Te pone las casillas de ambas pantallas con el caracter '*'
	*/
	public static char[][] plantillaPrincipal(char a[][]) {
		
		for (int contadorFilas=0;contadorFilas<a.length;contadorFilas++)
		{
			for (int contadorColumnas=0;contadorColumnas<a[contadorFilas].length;contadorColumnas++) {
			
				a[contadorFilas][contadorColumnas]='*';
			}
		}
		
	return a;}
	/*
	*	Te muestra por comando los valores de cada posición
	*/
	public static void mostrarPlantilla(char [][]a) {
		for (int contadorFilas=0;contadorFilas<a.length;contadorFilas++)
		{
			for (int contadorColumnas=0;contadorColumnas<a[contadorFilas].length;contadorColumnas++) {
		
				System.out.print(a[contadorFilas][contadorColumnas] + "		");
			}
			System.out.println("");
		}
	
	}
	

}
