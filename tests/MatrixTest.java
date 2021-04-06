import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class MatrixTest {

    @Test
    public void testCheckValid() {
        ArrayList<ArrayList<Double>> test = new ArrayList<>();
        Matrix matrix = new Matrix(test);
        assertFalse(Matrix.checkValid(matrix));
    }

    // Used for testing with user input
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        Matrix.transformREF(matrix);
    }


}
