package pronosticos.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pronosticos.CalculadoraPronosticos;

/**
 * Pruebas unitarias de mape.
 *
 * <p>El MAPE promedia el error de cada periodo expresado como porcentaje de la demanda real.
 */
class MapeTest {

    private static final double TOLERANCIA = 0.01;

    private CalculadoraPronosticos calculadora;

    @BeforeEach
    void prepararCalculadora() {
        calculadora = new CalculadoraPronosticos();
    }

    @Test
    @DisplayName("Calcula el error porcentual medio de todos los periodos")
    void calculaElErrorPorcentualMedio() {
        List<Double> real = List.of(141.0, 138.0, 150.0);
        List<Double> pronostico = List.of(126.67, 133.67, 135.67);

        // (14.33/141 + 4.33/138 + 14.33/150) / 3 * 100 = (10.16 + 3.14 + 9.55) / 3 = 7.62
        assertEquals(7.62, calculadora.mape(real, pronostico), TOLERANCIA);
    }

    @Test
    @DisplayName("Excluye del promedio los periodos con demanda 0")
    void excluyeLosPeriodosConDemandaCero() {
        List<Double> real = List.of(80.0, 0.0, 95.0);
        List<Double> pronostico = List.of(80.0, 0.0, 100.0);

        // Solo hay 2 periodos válidos: (0 % + 5/95 · 100 %) / 2 = (0 + 5.26) / 2 = 2.63
        assertEquals(2.63, calculadora.mape(real, pronostico), TOLERANCIA);
    }

    @Test
    @DisplayName("Rechaza una serie sin periodos válidos")
    void rechazaUnaSerieSinPeriodosValidos() {
        assertThrows(IllegalArgumentException.class,
                () -> calculadora.mape(List.of(0.0, 0.0), List.of(10.0, 20.0)));
    }
}
