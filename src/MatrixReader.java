
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixReader {

    private final static String PROMPT_USER_ENTRY = "Before performing operations on a Matrix, " +
            "you need to create one. " +
            "Please enter the desired matrix row by row, separating each element with a space" +
            " and" +
            " hitting the 'return' key after each " +
            "row.\nOnce you have finished typing out the rows, type 'done'.";
    private final static String INVALID_ENTRY = "Matrix entered was invalid, please try again. " +
            "Matrix must have at least 2 rows, and each row must be of the same length.";
    private final static String MATRIX_SAVED = "Matrix has been saved.";


    public Matrix readFromTerm() {
        Matrix matrix = new Matrix();
        ArrayList<ArrayList<Double>> entries = new ArrayList<>();
        MatrixTransformer transformer = new MatrixTransformer();
        Scanner scanner = new Scanner(System.in);

        System.out.println(PROMPT_USER_ENTRY);

        String entry = "";

        while (true) {
            while (!entry.equals("done")) {
                entry = scanner.nextLine();
                if (!entry.equals("done")) {
                    String[] strRow = entry.split(" ");
                    Double[] intRow = new Double[strRow.length];
                    for (int i = 0; i < strRow.length; i++) {
                        intRow[i] = Double.parseDouble(strRow[i]);
                    }
                    ArrayList<Double> row = new ArrayList<>(Arrays.asList(intRow));
                    entries.add(row);
                }
            }
            matrix.entries = entries;
            if (!matrix.checkValid()) {
                entries.clear();
                System.out.println(INVALID_ENTRY);
                entry = "";
            }
            else {
                break;
            }
        }

        //System.out.println(this.matrix);
        //scanner.close();
        matrix.height = matrix.entries.size();
        matrix.length = matrix.entries.get(0).size();
        System.out.println(MATRIX_SAVED);

        return matrix;
    }
}
