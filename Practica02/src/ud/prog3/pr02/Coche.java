package ud.prog3.pr02;

/** Clase para definir instancias lógicas de coches con posición, dirección y velocidad.
 * @author Andoni Eguíluz
 * Facultad de Ingeniería - Universidad de Deusto (2014)
 */
public class Coche {
	
	private static final double COEF_RZTO_SUELO = 15.5;
	private static final double COEF_RZTO_AIRE = 0.35;
	private static final double MASA = 1;
	
	private static final double FUERZA_BASE_ADELANTE = 100;
	final static double FUERZA_BASE_ATRAS = 0;
	
	protected double miVelocidad;  // Velocidad en pixels/segundo
	protected double miDireccionActual;  // Dirección en la que estoy mirando en grados (de 0 a 360)
	protected double posX;  // Posición en X (horizontal)
	protected double posY;  // Posición en Y (vertical)
	protected String piloto;  // Nombre de piloto
	
	
	
	
	// Constructores
	
	public Coche() {
		miVelocidad = 0;
		miDireccionActual = 0;
		posX = 300;
		posY = 300;
	}
	
	/** Devuelve la velocidad actual del coche en píxeles por segundo
	 * @return	velocidad
	 */
	public double getVelocidad() {
		return miVelocidad;
	}

	/** Cambia la velocidad actual del coche
	 * @param miVelocidad
	 */
	public void setVelocidad( double miVelocidad ) {
		this.miVelocidad = miVelocidad;
	}

	public double getDireccionActual() {
		return miDireccionActual;
	}

	public void setDireccionActual( double dir ) {
		// if (dir < 0) dir = 360 + dir;
		if (dir > 360) dir = dir - 360;
		miDireccionActual = dir;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosicion( double posX, double posY ) {
		setPosX( posX );
		setPosY( posY );
	}
	
	public void setPosX( double posX ) {
		this.posX = posX; 
	}
	
	public void setPosY( double posY ) {
		this.posY = posY; 
	}
	
	public String getPiloto() {
		return piloto;
	}

	public void setPiloto(String piloto) {
		this.piloto = piloto;
	}


	/** Cambia la velocidad actual del coche
	 * @param aceleracion	Incremento/decremento de la velocidad en pixels/segundo
	 * @param tiempo	Tiempo transcurrido en segundos
	 */
	public void acelera( double aceleracion, double tiempo ) {
		miVelocidad = MundoJuego.calcVelocidadConAceleracion( miVelocidad, aceleracion, tiempo );
	}
	
	/** Cambia la dirección actual del coche
	 * @param giro	Angulo de giro a sumar o restar de la dirección actual, en grados (-180 a +180)
	 * 				Considerando positivo giro antihorario, negativo giro horario
	 */
	public void gira( double giro ) {
		setDireccionActual( miDireccionActual + giro );
	}
	
	/** Cambia la posición del coche dependiendo de su velocidad y dirección
	 * @param tiempoDeMovimiento	Tiempo transcurrido, en segundos
	 */
	public void mueve( double tiempoDeMovimiento ) {
		setPosX( posX + MundoJuego.calcMovtoX( miVelocidad, miDireccionActual, tiempoDeMovimiento ) );
		setPosY( posY + MundoJuego.calcMovtoY( miVelocidad, miDireccionActual, tiempoDeMovimiento ) );
	}
	
	@Override
	public String toString() {
		return piloto + " (" + posX + "," + posY + ") - " +
			   "Velocidad: " + miVelocidad + " ## Dirección: " + miDireccionActual; 
	}
	
	
	//paso 4.1 Rozamiento
		public static double calcFuerzaRozamiento (double masa, double coefRozSuelo, double coefRozAire, double vel) {
			
			double fuerzaRozamientoAire = coefRozAire * (-vel); //en contra del movimiento
			
			double fuerzaRozamientoSuelo = masa * coefRozSuelo * ((vel>0)?(-1):1); // en contra del movimiento. si va para adelante >0, y sino <0
			
			return fuerzaRozamientoAire + fuerzaRozamientoSuelo;		
			
		}
		
	//paso 4.2 Aceleración
		public static double calcAceleracionConFuerza( double fuerza, double masa ) {
			// 2ª ley de Newton: F = m*a ---> a = F/m
				
			return fuerza/masa;
			}
		
		
	//paso 4.3 Aplicación de la fuerza al coche
		
		public static void aplicarFuerza( double fuerza, Coche coche ) {
			double fuerzaRozamiento = calcFuerzaRozamiento( Coche.MASA, Coche.COEF_RZTO_SUELO, Coche.COEF_RZTO_AIRE, coche.getVelocidad() );
			double aceleracion = calcAceleracionConFuerza( fuerza+fuerzaRozamiento, Coche.MASA );
			if (fuerza==0) {
			// No hay fuerza, solo se aplica el rozamiento
			double velAntigua = coche.getVelocidad();
			coche.acelera( aceleracion, 0.04 );
			if (velAntigua>=0 && coche.getVelocidad()<0
			|| velAntigua<=0 && coche.getVelocidad()>0) {
			coche.setVelocidad(0); // Si se está frenando, se para (no anda al revés)
			}
			} else {
			coche.acelera( aceleracion, 0.04 );
			}
			
		}
		
	//paso 4.4 Calculo fuerza de aceleracion del coche. Adelante
		
		/** Devuelve la fuerza de aceleración del coche, de acuerdo al motor definido en la práctica 2
		* @return Fuerza de aceleración en Newtixels
		*/
		public double fuerzaAceleracionAdelante() {
			if (miVelocidad<=-150) return FUERZA_BASE_ADELANTE; //100%
			
			else if (miVelocidad<=0)
			return FUERZA_BASE_ADELANTE*(-miVelocidad/150*0.5+0.5); //progresando 50% hacia abajo
			
			else if (miVelocidad<=250)
			return FUERZA_BASE_ADELANTE*(miVelocidad/250*0.5+0.5); //progresando 50% hacia arriba
			/*else if (miVelocidad<=250)
			return FUERZA_BASE_ADELANTE*(miVelocidad/250*0.5+0.5);*/
			
			else if (miVelocidad<=750)
			return FUERZA_BASE_ADELANTE;
			
			else return FUERZA_BASE_ADELANTE*(-(miVelocidad-1000)/250); //progresando 100% hacia abajo
		
		}
			
		
		
		
	//paso 4.4 Calculo fuerza de aceleracion del coche. Atras
		
		public double fuerzaAceleracionAtras() {
			
			if (miVelocidad<=-350) 
			return FUERZA_BASE_ATRAS*(miVelocidad-500/150);
			
			else if (miVelocidad<=-200)
			return FUERZA_BASE_ATRAS;
			
			else if (miVelocidad<=0)
			return FUERZA_BASE_ATRAS*(miVelocidad/200*0.3+0.3);
			
			else if (miVelocidad<=250)
			return FUERZA_BASE_ATRAS*(miVelocidad/250*0.5+0.5);
			
			else if (miVelocidad<=750)
			return FUERZA_BASE_ATRAS;
			
			else return FUERZA_BASE_ATRAS+85; //85%
		
		}
		
	
}
	
