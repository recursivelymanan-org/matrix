import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class Matrix {

    ArrayList<ArrayList<BigDecimal>> entries;
    boolean isREF, isRREF;
    int height, length;

    private final static String CONTINUE = "If you would like to transform another matrix, please" +
            " type 'Y'. If you are done, type 'N'. Thank you!";

    public Matrix() {
        this.entries = new ArrayList<>();
    }

    public Matrix(ArrayList<ArrayList<BigDecimal>> entries) throws IllegalArgumentException {
        if (this.checkValid()) {
            this.entries = entries;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /*
   Checks if a given matrix is valid for the purposes of this project. A valid matrix should have
   more than 1 row, and each row should have the same number of entries.
    */
    public boolean checkValid() {
        if (entries.size() < 2) { return false; }

        int rowLength = entries.get(0).size();
        for (ArrayList<BigDecimal> row : entries) {
            if (row.size() != rowLength) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        ArrayList<String> print = new ArrayList<>();
        for (ArrayList<BigDecimal> row : entries) {
            print.add(row.toString() + "\n");
        }
        StringBuilder result = new StringBuilder();
        for (String s : print) {
            result.append(s);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        while (true) {
            MatrixReader reader = new MatrixReader();
            Matrix matrix = reader.readFromTerm();

            MatrixTransformer trans = new MatrixTransformer();
            trans.transformREF(matrix);
            System.out.println(matrix);

            trans.transformReducedREF(matrix);
            System.out.println(matrix);

            System.out.println(CONTINUE);
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            if (!choice.equals("Y") && !choice.equals("y")) {
                break;
            }
        }
    }
}
