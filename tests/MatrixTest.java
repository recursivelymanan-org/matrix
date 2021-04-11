import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MatrixTest {
    @Test
    public void bigDecimalTest() {
        BigDecimal value = new BigDecimal("2");
        BigDecimal value2 = new BigDecimal("6");
        System.out.println(value.divide(value2, new MathContext(2, RoundingMode.CEILING)));

    }

}
