import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Matrix {

    ArrayList<ArrayList<Integer>> matrix;
    boolean isREF, isRREF;

    public Matrix() {
        this.matrix = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Before performing operations on a Matrix, you need to create one. " +
                "Please enter the desired matrix row by row, separating each element with a comma" +
                " and" +
                " hitting the 'return' key after each " +
                "row.\nOnce you have finished typing out the rows, type 'done'.");

        String entry = "";
        while (!entry.equals("done")) {
            entry = scanner.nextLine();
            if (!entry.equals("done")) {
                String[] strRow = entry.split(",");
                Integer[] intRow = new Integer[strRow.length];
                for (int i = 0; i < strRow.length; i++) {
                    intRow[i] = parseInt(strRow[i]);
                }
                ArrayList<Integer> row = new ArrayList<>(Arrays.asList(intRow));
                this.matrix.add(row);
            }
        }

        System.out.println(this.matrix);
        scanner.close();

        System.out.println("Matrix has been saved.");
    }

    public Matrix (ArrayList<ArrayList<Integer>> matrix) {
        this.matrix = matrix;
    }

    /*
    Valid matrix should have more than 1 row, and each row should have the same number of entries
     */
    public boolean checkValid(Matrix matrix) {
        if (matrix.matrix.size() < 2) { return false; }

        int rowLength = matrix.matrix.get(0).size();
        for (ArrayList<Integer> row : matrix.matrix) {
            if (row.size() != rowLength) {
                return false;
            }
        }
        return true;
    }

    /*
    Returns an array of length 2 that describes if the given matrix is in REF or RREF.
     */
    public static boolean[] checkForm(Matrix matrix) {
        // TODO: check for REF/RREF
        return null;
    }

    public static void transformREF(Matrix matrix) {
        if (matrix.isREF) { return; }
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>(matrix.matrix);
        int col = findLeftMostNonzeroCol(temp);
        int pivot = findAndSwapPivot(temp, col);
        createZerosBelowPivot(temp, col);

        // TODO: IMPLEMENT THE ABOVE WITH RECURSION?

        matrix.matrix = temp;
        matrix.isREF = true;
    }

    private static int getMultiplier(int i, int j) {
        j = j - (2 * j);
        return (j / i);
    }


    private static void createZerosBelowPivot(ArrayList<ArrayList<Integer>> temp, int col) {
        for (int i = 1; i < temp.get(0).size(); i++) {
            ArrayList<Integer> newRow = new ArrayList<>();
            if (temp.get(i).get(col) != 0) {
                int mult = getMultiplier(temp.get(0).get(col), temp.get(i).get(col));
                for (int j = 0; j < temp.get(0).size(); j++) {
                    int newEntry = (temp.get(i).get(j)) + (temp.get(0).get(j) * mult);
                    newRow.add(newEntry);
                }
                temp.remove(i);
                temp.add(i, newRow);
            }
        }

    }

    private static int findLeftMostNonzeroCol(ArrayList<ArrayList<Integer>> temp) {
        for (int i = 0; i < temp.get(0).size(); i++) {
            int tracker = 0;
            for (ArrayList<Integer> row : temp) {
                tracker += row.get(i);
                if (tracker != 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int findAndSwapPivot(ArrayList<ArrayList<Integer>> temp, int col) {
        int row = -1;
        int pivot = 0;
        for (int i = 0; i < temp.get(0).size(); i++) {
            if (temp.get(i).get(col) != 0) {
                row = i;
                pivot = temp.get(i).get(col);
                break;
            }
        }
        ArrayList<Integer> topRow = temp.get(0);
        ArrayList<Integer> swapRow = temp.get(row);
        temp.remove(0);
        temp.add(0, swapRow);
        temp.remove(row);
        temp.add(row, topRow);
        return pivot;
    }

    public void transformRREF(Matrix matrix) {
        if (matrix.isRREF) { return; }
        if (!matrix.isREF) {
            transformREF(matrix);
            matrix.isREF = true;
        }

        // TODO: transform from REF to RREF
        matrix.isRREF = true;
    }

    @Override
    public String toString() {
        return this.matrix.toString();
    }

    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        System.out.println(matrix);

        transformREF(matrix);
        System.out.println(matrix);

    }
}
