package ud.prog3.pr02;

import static org.junit.Assert.*;

import org.junit.Test;

public class CocheTest {

	Coche coche1 = new Coche();
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void testFuerzaAceleracionAtras() {
		double[] tablaVel = { -500, -425, -300, -250, -200, -100, 0, 125, 250, 500, 1100 };
		double[] tablaFuerza = { 0, 0.5, 1, 1, 1, 0.65, 0.3, 0.575, 0.85, 0.85, 0.85 };
			for (int i=0;i<tablaVel.length;i++) {
				coche1.setVelocidad( tablaVel[i] );
				assertEquals( "Velocidad " + tablaVel[i], tablaFuerza[i]*Coche.FUERZA_BASE_ATRAS,
				coche1.fuerzaAceleracionAtras(), 0.0000001 );
			}
	}
	

}
