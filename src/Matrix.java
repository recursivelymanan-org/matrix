/*
Author: Manan Chopra (m1chopra@ucsd.edu)
Date: April 2, 2021
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import static java.lang.Integer.parseInt;


/*
In this file, some linear algebra terminology is used. A quick list of some definitions:
    REF and RREF : Row Echelon Form and Reduced Row Echelon Form, respectively
    Leading entry : The first nonzero entry in a row
    Nonzero row : A row that contains at least one nonzero entry
    Pivot entry : Leading entry in a row

Matrix class contains all methods for transforming the matrix, as well as the main method that is
currently being used mostly for testing.
 */
public class Matrix {

    ArrayList<ArrayList<Integer>> matrix;
    boolean isREF, isRREF;

    private final static String PROMPT_USER_ENTRY = "Before performing operations on a Matrix, " +
            "you need to create one. " +
            "Please enter the desired matrix row by row, separating each element with a comma" +
            " and" +
            " hitting the 'return' key after each " +
            "row.\nOnce you have finished typing out the rows, type 'done'.";
    private final static String MATRIX_SAVED = "Matrix has been saved.";
    private final static String TRANSFORMING_MATRIX = "Transforming matrix...";
    private final static String TRANSFORMATION_DONE = "Done! Here is the result:";

    /*
    When a Matrix object is created using this constructor, the user is prompted through the
    terminal to enter the values of the matrix row by row.
     */
    public Matrix() {
        this.matrix = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println(PROMPT_USER_ENTRY);

        String entry = "";

        while (true) {
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
            if (!checkValid(this)) {
                this.matrix.clear();
            }
            else {
                break;
            }
        }

        //System.out.println(this.matrix);
        scanner.close();
        System.out.println(MATRIX_SAVED);
    }

    // Constructor for testing purposes
    public Matrix (ArrayList<ArrayList<Integer>> matrix) {
        this.matrix = matrix;
    }

    /*
    Transforms the given matrix into REF using the recursive helper method.
     */
    public static void transformREF(Matrix matrix) {
        System.out.println(TRANSFORMING_MATRIX);
        transformREF(matrix, 0);
        System.out.println(TRANSFORMATION_DONE);
        System.out.println(matrix);
    }

    /*
    Helper method for the public transformREF method, uses recursion to move through and
    transform the matrix.

    TODO: finish implementation, currently only performs steps once, needs to work repeatedly
     */
    private static void transformREF(Matrix matrix, int rowMin) {
        if (matrix.isREF) { return; }
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>(matrix.matrix);
        int col = findLeftMostNonzeroCol(temp, rowMin);
        int pivot = findAndSwapPivot(temp, col, rowMin);
        createZerosBelowPivot(temp, col, pivot);

        matrix.matrix = temp;

        if (!checkForm(matrix)[0]) {
            transformREF(matrix, rowMin + 1);
        }

        matrix.isREF = true;
    }

    /*
    Transforms a method (THAT IS ALREADY IN REF) to RREF.

    TODO: currently does nothing except check that it is REF
     */
    public void transformRREF(Matrix matrix) {
        if (matrix.isRREF) { return; }
        if (!matrix.isREF) {
            transformREF(matrix);
            matrix.isREF = true;
        }

        matrix.isRREF = true;
    }

    /*
    Used in the transform methods to find leftmost nonzero column in a matrix. rowMin refers to
    the top border of the portion of the matrix that we are looking at.
     */
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

    /*
    Used in the transform methods, finds the pivot row and moves it to the 'top' of the matrix.
     */
    private static int findAndSwapPivot(ArrayList<ArrayList<Integer>> temp, int col, int rowMin) {
        int row = -1;
        int pivot = 0;

        for (int i = rowMin; i < temp.get(0).size(); i++) {
            if (temp.get(i).get(col) != 0) {
                row = i;
                pivot = temp.get(i).get(col);
                break;
            }
        }
        if (row != rowMin) {
            ArrayList<Integer> topRow = temp.get(rowMin);
            ArrayList<Integer> swapRow = temp.get(row);
            temp.remove(rowMin);
            temp.add(rowMin, swapRow);
            temp.remove(row);
            temp.add(row, topRow);
        }
        return pivot;
    }

    // Returns the column containing the leading entry for a given row
    private static int findLeadingEntry(ArrayList<Integer> row) {
        int col = 0;
        for (Integer i : row) {
            if (i != 0) {
                break;
            }
            col++;
        }
        return col;
    }

    // Will return true if all values of the List are 0
    private static boolean checkAllZeros(ArrayList<Integer> row) {
        int tracker = 0;
        for (Integer i : row) {
            tracker += i;
        }
        return tracker == 0;
    }

    // Used in the
    private static int getMultiplier(int i, int j) {
        j = j * -1;
        return (j / i);
    }

    /*
    Used to fulfill RULE 3 for REF and transforms matrix so that entries below the given pivot
    are all zero.
     */
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
                int mult = getMultiplier(temp.get(tracker).get(col), temp.get(index).get(col));
                for (int j = 0; j < temp.get(0).size(); j++) {
                    //int mult = getMultiplier(temp.get(tracker).get(j), temp.get(index).get(j));
                    int newEntry = (temp.get(index).get(j)) + (temp.get(tracker).get(j) * mult);
                    newRow.add(newEntry);
                }
                temp.remove(index);
                temp.add(index, newRow);
            }
        }

    }

    /*
    Checks if a given matrix is valid for the purposes of this project. A valid matrix should have
    more than 1 row, and each row should have the same number of entries.
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
    Returns an array of length 2 that describes if the given matrix is in REF or RREF. Index 0 of
    the returned array corresponds to REF, index 1 corresponds to RREF. Makes use of five helper
    methods that all check their own rule.
     */
    public static boolean[] checkForm(Matrix matrix) {
        boolean[] result = new boolean[2];

        if (!checkRuleOneREF(matrix)) { return result; }
        if (!checkRuleTwoREF(matrix)) { return result; }
        if (!checkRuleThreeREF(matrix)) { return result; }

        result[0] = true;

        if (!checkRuleOneRREF(matrix)) { return result; }
        if (!checkRuleTwoRREF(matrix)) { return result; }

        result[1] = true;
        return result;
    }

    // RULE 1 for REF: all nonzero rows must be above any rows consisting of all zeros
    private static boolean checkRuleOneREF(Matrix matrix) {
        boolean zeroRowFound = false;
        for (ArrayList<Integer> row : matrix.matrix) {
            if (zeroRowFound && !checkAllZeros(row)) {
                return false;
            }
            if (checkAllZeros(row)) {
                zeroRowFound = true;
            }
        }
        return true;
    }

    /*
    RULE 2 for REF: each leading entry must be one column to the right of the previous leading
    entry.
     */
    private static boolean checkRuleTwoREF(Matrix matrix) {
        int row = 0;
        int col = 0;
        while (true) {
            if (checkAllZeros(matrix.matrix.get(0))) {
                break;
            }
            else {
                for (int i = 0; i < col; i++) {
                    if (matrix.matrix.get(row).get(i) != 0) {
                        return false;
                    }
                }
                col++;
                row++;
            }

            if (row >= matrix.matrix.size() || col >= matrix.matrix.get(0).size()) {
                break;
            }
        }
        return true;
    }

    // RULE 3 for REF: all entries below a leading entry in the same column should be 0.
    private static boolean checkRuleThreeREF(Matrix matrix) {
        for (int i = 0; i < matrix.matrix.size(); i++) {
            if (!checkAllZeros(matrix.matrix.get(i))) {
                int col = findLeadingEntry(matrix.matrix.get(i));
                for (int j = i + 1; j < matrix.matrix.size(); j++) {
                    if (matrix.matrix.get(j).get(col) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // RULE 1 for RREF: leading entry for a nonzero row must be 1
    private static boolean checkRuleOneRREF(Matrix matrix) {
        for (ArrayList<Integer> row : matrix.matrix) {
            if (!checkAllZeros(row)) {
                int lead = findLeadingEntry(row);
                if (row.get(lead) != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // RULE 2 for RREF: each leading 1 is the only non-zero entry in the entire column
    private static boolean checkRuleTwoRREF(Matrix matrix) {
        for (int i = 0; i < matrix.matrix.size(); i++) {
            if (!checkAllZeros(matrix.matrix.get(i))) {
                int col = findLeadingEntry(matrix.matrix.get(i));
                for (int j = 0; j < matrix.matrix.size(); j++) {
                    if (j != i) {
                        if (matrix.matrix.get(j).get(col) != 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        ArrayList<String> print = new ArrayList<>();
        for (ArrayList<Integer> row : this.matrix) {
            print.add(row.toString() + "\n");
        }
        StringBuilder result = new StringBuilder();
        for (String s : print) {
            result.append(s);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        transformREF(matrix);

    }
}
