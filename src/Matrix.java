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
        boolean[] result = new boolean[2];

        // RULE 1 for REF: all nonzero rows must be above any rows consisting of all zeros
        boolean zeroRowFound = false;
        for (ArrayList<Integer> row : matrix.matrix) {
            if (zeroRowFound && !checkAllZeros(row)) {
                return result;
            }
            if (checkAllZeros(row)) {
                zeroRowFound = true;
            }
        }

        // RULE 2 for REF: each leading entry must be one column to the right of the previous
        //                 leading entry.
        int row = 0;
        int col = 0;
        while (true) {
            if (checkAllZeros(matrix.matrix.get(0))) {
                break;
            }
            else {
                for (int i = 0; i < col; i++) {
                    if (matrix.matrix.get(row).get(i) != 0) {
                        return result;
                    }
                }
                col++;
                row++;
            }

            if (row >= matrix.matrix.size() || col >= matrix.matrix.get(0).size()) {
                break;
            }
        }

        // RULE 3 for REF: all entries below a leading entry in the same column should be 0.


        return result;
    }

    // Will return true if all values of the List are 0
    private static boolean checkAllZeros(ArrayList<Integer> row) {
        int tracker = 0;
        for (Integer i : row) {
            tracker += i;
        }
        return tracker == 0;
    }

    public static void transformREF(Matrix matrix) {
        transformREF(matrix, 0);
    }

    private static void transformREF(Matrix matrix, int rowMin) {
        if (matrix.isREF) { return; }
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>(matrix.matrix);
        int col = findLeftMostNonzeroCol(temp, rowMin);
        int pivot = findAndSwapPivot(temp, col, rowMin);
        createZerosBelowPivot(temp, col, pivot);


        // TODO: IMPLEMENT THE ABOVE WITH RECURSION?

        matrix.matrix = temp;

        if (!checkForm(matrix)[0]) {
            transformREF(matrix, rowMin + 1);
        }

        matrix.isREF = true;
    }

    private static int getMultiplier(int i, int j) {
        j = j * -1;
        return (j / i);
    }


    private static void createZerosBelowPivot(ArrayList<ArrayList<Integer>> temp, int col,
                                              int pivot) {
        int i = 0;
        int tracker = -1;
        for (ArrayList<Integer> row : temp) {
            if (row.get(col) == pivot) {
                tracker = i;
                break;
            }
            i++;
        }

        for (int index = tracker + 1; index < temp.get(0).size(); index++) {
            ArrayList<Integer> newRow = new ArrayList<>();
            if (temp.get(index).get(col) != 0) {
                int mult = getMultiplier(temp.get(0).get(col), temp.get(index).get(col));
                for (int j = 0; j < temp.get(0).size(); j++) {
                    int newEntry = (temp.get(index).get(j)) + (temp.get(0).get(j) * mult);
                    newRow.add(newEntry);
                }
                temp.remove(index);
                temp.add(index, newRow);
            }
        }

    }

    private static int findLeftMostNonzeroCol(ArrayList<ArrayList<Integer>> temp, int rowMin) {
        for (int i = 0; i < temp.get(0).size(); i++) {
            int tracker = 0;
            for (int j = rowMin; j < temp.get(0).size(); j++) {
                tracker += temp.get(j).get(i);
                if (tracker != 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int findAndSwapPivot(ArrayList<ArrayList<Integer>> temp, int col, int rowMin) {
        int row = -1;
        int pivot = 0;
        boolean breakNow = false;
        for (int i = 0; i < temp.get(0).size(); i++) {
            if (temp.get(i).get(col) != 0) {
                row = i;
                pivot = temp.get(i).get(col);
                break;
            }
        }
        ArrayList<Integer> topRow = temp.get(rowMin);
        ArrayList<Integer> swapRow = temp.get(row);
        temp.remove(rowMin);
        temp.add(rowMin, swapRow);
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

        System.out.println(Arrays.toString(checkForm(matrix)));

    }
}
