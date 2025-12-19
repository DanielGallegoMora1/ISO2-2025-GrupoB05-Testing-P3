package src;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AppTest {

	private static Map<TipoActividad, Actividad> actividades(
			boolean esquiAforo,
			boolean senderismoAforo,
			boolean escaladaAforo,
			boolean catalogoAforo,
			boolean culturalAforo,
			boolean gastronomicoAforo,
			boolean piscinaAforo,
			boolean playaAforo
	) {
		return Map.of(
				TipoActividad.ESQUI, new Actividad(TipoActividad.ESQUI, esquiAforo),
				TipoActividad.SENDERISMO, new Actividad(TipoActividad.SENDERISMO, senderismoAforo),
				TipoActividad.ESCALADA, new Actividad(TipoActividad.ESCALADA, escaladaAforo),
				TipoActividad.CATALOGO_PRIMAVERA_VERANO_OTONO, new Actividad(TipoActividad.CATALOGO_PRIMAVERA_VERANO_OTONO, catalogoAforo),
				TipoActividad.CULTURAL, new Actividad(TipoActividad.CULTURAL, culturalAforo),
				TipoActividad.GASTRONOMICO, new Actividad(TipoActividad.GASTRONOMICO, gastronomicoAforo),
				TipoActividad.PLAYA, new Actividad(TipoActividad.PLAYA, playaAforo),
				TipoActividad.PISCINA, new Actividad(TipoActividad.PISCINA, piscinaAforo),
				TipoActividad.QUEDARSE_EN_CASA, new Actividad(TipoActividad.QUEDARSE_EN_CASA, false)
		);
	}

	private static boolean contiene(Recomendacion r, String tipo) {
		return r.getRecomendaciones().stream().anyMatch(a -> a.getTipo().equals(tipo));
	}

	// caso trivial para comprobar el arnés JUnit/Surefire.
	@Test
	public void testBasico() {
		assertTrue(true);
	}

	// (each-use): partición "persona NO apta" -> salida por excepción NingunaActividadException.
	@Test(expected = NingunaActividadException.class)
	public void siPersonaNoPuedeRealizarActividadFisica_lanzaExcepcion() throws Exception {
		Persona p = new Persona(true, true);
		Tiempo t = new Tiempo(20, 40, false, false, false);
		App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
	}

	// (each-use/pairwise): Regla "<0º, humedad<15, hay precipitación" -> QUEDARSE_EN_CASA.
	@Test
	public void bajoCero_bajaHumedad_yPrecipitacion_recomiendaCasa() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(-3, 10, true, false, false);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "QUEDARSE_EN_CASA"));
	}

	// (each-use/pairwise): Regla "<0º, humedad<15, sin precipitación" -> ESQUI (si aforo OK).
	@Test
	public void bajoCero_bajaHumedad_sinPrecipitacion_recomiendaEsqui_siNoAforo() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(-3, 10, false, false, false);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "ESQUI"));
	}

	// (each-use): misma regla de ESQUI pero con "aforo superado" -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void bajoCero_bajaHumedad_sinPrecipitacion_yEsquiConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(-3, 10, false, false, false);
		App.recomendar(p, t, actividades(true, false, false, false, false, false, false, false));
	}

	// (each-use/pairwise): Regla "0..15º y NO precipitación de agua" -> SENDERISMO + ESCALADA.
	@Test
	public void entre0y15_sinLluvia_recomiendaSenderismoYEscalada() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(10, 40, false, false, true);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "SENDERISMO"));
		assertTrue(contiene(r, "ESCALADA"));
		assertEquals(2, r.getRecomendaciones().size());
	}

	// Teoría (each-use): misma regla 0..15º, par (actividad=senderismo, aforo=1) -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void entre0y15_senderismoConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(10, 40, false, false, false);
		App.recomendar(p, t, actividades(false, true, false, false, false, false, false, false));
	}

	// (each-use): misma regla 0..15º, par (actividad=escalada, aforo=1) -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void entre0y15_escaladaConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(10, 40, false, false, false);
		App.recomendar(p, t, actividades(false, false, true, false, false, false, false, false));
	}

	// (pairwise): Regla "15..25º, sin lluvia, no nublado, humedad<=60" -> CATALOGO_PRIMAVERA_VERANO_OTONO.
	@Test
	public void entre15y25_sinLluvia_noNublado_humedadOK_recomiendaCatalogo() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(20, 60, false, false, false);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "CATALOGO_PRIMAVERA_VERANO_OTONO"));
		assertEquals(1, r.getRecomendaciones().size());
	}

	// (each-use): misma regla 15..25º, par (actividad=catalogo, aforo=1) -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void entre15y25_catalogoConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(20, 60, false, false, false);
		App.recomendar(p, t, actividades(false, false, false, true, false, false, false, false));
	}

	// (each-use/pairwise): Regla "25..35º y sin lluvia" -> GASTRONOMICO + CULTURAL.
	@Test
	public void entre25y35_sinLluvia_recomiendaGastronomicoYCultural() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(30, 40, false, false, true);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "GASTRONOMICO"));
		assertTrue(contiene(r, "CULTURAL"));
		assertEquals(2, r.getRecomendaciones().size());
	}

	// (each-use): misma regla 25..35º, par (actividad=gastronomico, aforo=1) -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void entre25y35_gastronomicoConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(30, 40, false, false, false);
		App.recomendar(p, t, actividades(false, false, false, false, false, true, false, false));
	}

	// (each-use): misma regla 25..35º, par (actividad=cultural, aforo=1) -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void entre25y35_culturalConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(30, 40, false, false, false);
		App.recomendar(p, t, actividades(false, false, false, false, true, false, false, false));
	}

	// (each-use/pairwise): Regla ">30º y sin lluvia" aislada (T>35 para evitar solape) -> PLAYA + PISCINA.
	@Test
	public void mayor30_sinLluvia_recomiendaPlayaYPiscina() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(36, 40, false, false, false);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "PLAYA"));
		assertTrue(contiene(r, "PISCINA"));
		assertEquals(2, r.getRecomendaciones().size());
	}

	// (each-use): misma regla ">30º", par (actividad=piscina, aforo=1) -> AforoSuperadoException.
	@Test(expected = AforoSuperadoException.class)
	public void mayor30_piscinaConAforo_lanzaAforoSuperado() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(36, 40, false, false, false);
		App.recomendar(p, t, actividades(false, false, false, false, false, false, true, false));
	}

	// caso diseñado para mostrar que 32º activa 25..35 y >30 (4 recomendaciones).
	@Test
	public void solape_32grados_sinLluvia_disparaBloques25a35_yMayor30() throws Exception {
		Persona p = new Persona(true, false);
		Tiempo t = new Tiempo(32, 40, false, false, false);

		Recomendacion r = App.recomendar(p, t, actividades(false, false, false, false, false, false, false, false));
		assertTrue(contiene(r, "GASTRONOMICO"));
		assertTrue(contiene(r, "CULTURAL"));
		assertTrue(contiene(r, "PLAYA"));
		assertTrue(contiene(r, "PISCINA"));
		assertEquals(4, r.getRecomendaciones().size());
	}

	// cobertura de todos los valores del enum + getters básicos.
	@Test
	public void actividad_getTipo_cubreTodosLosValoresDelEnum() {
		for (TipoActividad ta : TipoActividad.values()) {
			Actividad a = new Actividad(ta, false);
			assertEquals(ta.name(), a.getTipo());
			assertFalse(a.isAforoSuperado());
		}
	}

	// casos básicos de getters y predicados en Persona/Tiempo (precipitación sí/no, agua/nieve, nublado).
	@Test
	public void tiempo_y_persona_metodosBasicos() {
		Persona pOk = new Persona(true, false);
		Persona pKo = new Persona(true, true);
		assertTrue(pOk.puedeRealizarActividadFisica());
		assertFalse(pKo.puedeRealizarActividadFisica());

		Tiempo t = new Tiempo(1, 2, true, false, true);
		assertEquals(1.0, t.getTemperatura(), 0.0001);
		assertEquals(2.0, t.getHumedad(), 0.0001);
		assertTrue(t.hayPrecipitacion());
		assertTrue(t.hayPrecipitacionAgua());
		assertFalse(t.hayPrecipitacionNieve());
		assertTrue(t.isNublado());
	}
}
