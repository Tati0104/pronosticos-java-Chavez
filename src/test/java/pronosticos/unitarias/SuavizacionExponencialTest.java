package pronosticos.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pronosticos.CalculadoraPronosticos;

/**
 * Pruebas unitarias de suavizacionExponencial.
 *
 * <p>Los valores esperados salen de aplicar la fórmula a mano:
 * F2 = 120, F3 = 123.6, F4 = 124.92, F5 = 129.744, F6 = 132.2208, F7 = 137.5546.
 */
class SuavizacionExponencialTest {

    private static final List<Double> SERIE = List.of(120.0, 132.0, 128.0, 141.0, 138.0, 150.0);
    private static final double TOLERANCIA = 0.01;

    private CalculadoraPronosticos calculadora;

    @BeforeEach
    void prepararCalculadora() {
        calculadora = new CalculadoraPronosticos();
    }

    @Test
    @DisplayName("Pronostica el siguiente periodo con alfa 0.3")
    void pronosticaElSiguientePeriodoConAlfa03() {
        double resultado = calculadora.suavizacionExponencial(SERIE, 0.3);

        assertEquals(137.5546, resultado, TOLERANCIA);
    }

    @Test
    @DisplayName("Con alfa 0 el pronóstico nunca cambia")
    void conAlfaCeroElPronosticoNuncaCambia() {
        double resultado = calculadora.suavizacionExponencial(SERIE, 0);

        assertEquals(120.00, resultado, TOLERANCIA);
    }

    @Test
    @DisplayName("Con alfa 1 el pronóstico es la última demanda")
    void conAlfaUnoElPronosticoEsLaUltimaDemanda() {
        double resultado = calculadora.suavizacionExponencial(SERIE, 1);

        assertEquals(150.00, resultado, TOLERANCIA);
    }

    @ParameterizedTest(name = "alfa = {0}")
    @ValueSource(doubles = {-0.1, 1.5})
    @DisplayName("Rechaza un alfa fuera del rango de 0 a 1")
    void rechazaUnAlfaFueraDeRango(double alfa) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculadora.suavizacionExponencial(SERIE, alfa));

        assertEquals("Alfa debe estar entre 0 y 1", error.getMessage());
    }
}
