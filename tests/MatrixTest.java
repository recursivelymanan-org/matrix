import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class MatrixTest {

    @Test
    public void testBasicTransformREF() {
        MatrixReader reader = new MatrixReader();
        Matrix matrix = reader.readFromTerm();

        MatrixTransformer trans = new MatrixTransformer();
        trans.transformREF(matrix);

    }

}
