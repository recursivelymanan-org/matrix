/*
Author: Manan Chopra (m1chopra@ucsd.edu)
Date: April 2, 2021
 */
import java.util.ArrayList;

/*
In this file, some linear algebra terminology is used. A quick list of some definitions:
    REF and RREF : Row Echelon Form and Reduced Row Echelon Form, respectively
    Leading entry : The first nonzero entry in a row
    Nonzero row : A row that contains at least one nonzero entry
    Pivot entry : Leading entry in a row

Matrix class contains all methods for transforming the matrix, as well as the main method that is
currently being used mostly for testing.
 */
public class MatrixTransformer {

    /*
    Transforms the given matrix into REF using the recursive helper method.
     */
    public void transformREF(Matrix matrix) {
        transformREF(matrix, 0);
    }

    /*
    Helper method for the public transformREF method, uses recursion to move through and
    transform the matrix.
     */
    private void transformREF(Matrix matrix, int rowMin) {
        if (matrix.isREF) { return; }
        ArrayList<ArrayList<Double>> temp = new ArrayList<>(matrix.entries);
        int col = findLeftMostNonzeroCol(temp, rowMin);
        double pivot = findAndSwapPivot(temp, col, rowMin);
        createZerosBelowPivot(temp, col, pivot);

        matrix.entries = temp;

        if (!checkForm(matrix)[0]) {
            transformREF(matrix, rowMin + 1);
        }

        matrix.isREF = true;
    }

    /*
    Transforms a method (THAT IS ALREADY IN REF) to RREF.
     */
    public void transformReducedREF(Matrix matrix) {
        if (matrix.isRREF) { return; }
        if (!matrix.isREF) {
            transformREF(matrix);
            matrix.isREF = true;
        }

        transformReducedREF(matrix, matrix.height - 1);
    }

    public void transformReducedREF(Matrix matrix, int rowMax) {
        if (!checkAllZeros(matrix.entries.get(rowMax))) {
            int col = findLeadingEntry(matrix.entries.get(rowMax));
            createZerosAbovePivot(matrix, col);
            scaleRow(matrix, rowMax);

        }
        if (!checkForm(matrix)[1]) {
            transformReducedREF(matrix, rowMax - 1);
        }

        matrix.isRREF = true;
    }

    private void scaleRow(Matrix matrix, int rowMax) {
        ArrayList<Double> newRow = matrix.entries.get(rowMax);
        int col = findLeadingEntry(newRow);
        double lead = newRow.get(col);
        for (int i = col; i < matrix.length; i++) {
            double newValue = newRow.get(i) / lead;
            newRow.remove(i);
            newRow.add(i, newValue);
        }

        matrix.entries.remove(rowMax);
        matrix.entries.add(rowMax, newRow);
    }

    private void createZerosAbovePivot(Matrix matrix, int col) {
        //First iterate from bottom to top of column and find the pivot
        int pivotRow = 0;
        for (int i = matrix.height - 1; i >= 0; i --) {
            if (matrix.entries.get(i).get(col) != 0) {
                pivotRow = i;
                break;
            }
        }
        
        if (pivotRow == 0) { return; }

        for (int i = pivotRow - 1; i >= 0; i--) {
            ArrayList<Double> newRow = new ArrayList<>();
            if (matrix.entries.get(i).get(col) != 0) {
                double mult = getMultiplier(matrix.entries.get(pivotRow).get(col),
                        matrix.entries.get(i).get(col));

                for (int j = 0; j < matrix.entries.get(0).size(); j++) {
                    double newEntry = (matrix.entries.get(i).get(j)) +
                            (matrix.entries.get(pivotRow).get(j) * mult);

                    newRow.add(newEntry);
                }
                matrix.entries.remove(i);
                matrix.entries.add(i, newRow);
            }
        }
        
    }

    /*
    Used in the transform methods to find leftmost nonzero column in a matrix. rowMin refers to
    the top border of the portion of the matrix that we are looking at.
     */
    private int findLeftMostNonzeroCol(ArrayList<ArrayList<Double>> temp, int rowMin) {
        for (int i = 0; i < temp.get(0).size(); i++) {
            int tracker = 0;
            for (int j = rowMin; j < temp.size(); j++) {
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
    private double findAndSwapPivot(ArrayList<ArrayList<Double>> temp, int col, int rowMin) {
        int row = -1;
        double pivot = 0;

        for (int i = rowMin; i < temp.size(); i++) {
            if (temp.get(i).get(col) != 0) {
                row = i;
                pivot = temp.get(i).get(col);
                break;
            }
        }
        if (row != rowMin) {
            ArrayList<Double> topRow = temp.get(rowMin);
            ArrayList<Double> swapRow = temp.get(row);
            temp.remove(rowMin);
            temp.add(rowMin, swapRow);
            temp.remove(row);
            temp.add(row, topRow);
        }
        return pivot;
    }

    // Returns the column containing the leading entry for a given row
    private int findLeadingEntry(ArrayList<Double> row) {
        int col = 0;
        for (Double i : row) {
            if (i != 0) {
                break;
            }
            col++;
        }
        return col;
    }

    // Will return true if all values of the List are 0
    private boolean checkAllZeros(ArrayList<Double> row) {
        int tracker = 0;
        for (Double i : row) {
            tracker += i;
            if (tracker != 0) {
                return false;
            }
        }
        return true;
    }

    // Used in the
    private double getMultiplier(double i, double j) {
        j = j * -1;
        return (j / i);
    }

    /*
    Used to fulfill RULE 3 for REF and transforms matrix so that entries below the given pivot
    are all zero.
     */
    private void createZerosBelowPivot(ArrayList<ArrayList<Double>> temp, int col,
                                              double pivot) {
        // First find the row containing the pivot
        int i = 0;
        int tracker = -1;
        for (ArrayList<Double> row : temp) {
            if (row.get(col) == pivot) {
                tracker = i;
                break;
            }
            i++;
        }
        
        // Then transform matrix such that all entries below pivot are zero
        for (int index = tracker + 1; index < temp.size(); index++) {
            ArrayList<Double> newRow = new ArrayList<>();
            if (temp.get(index).get(col) != 0) {
                double mult = getMultiplier(temp.get(tracker).get(col), temp.get(index).get(col));
                for (int j = 0; j < temp.get(0).size(); j++) {
                    //int mult = getMultiplier(temp.get(tracker).get(j), temp.get(index).get(j));
                    double newEntry = (temp.get(index).get(j)) + (temp.get(tracker).get(j) * mult);
                    newRow.add(newEntry);
                }
                temp.remove(index);
                temp.add(index, newRow);
            }
        }

    }



    /*
    Returns an array of length 2 that describes if the given matrix is in REF or RREF. Index 0 of
    the returned array corresponds to REF, index 1 corresponds to RREF. Makes use of five helper
    methods that all check their own rule.
     */
    public boolean[] checkForm(Matrix matrix) {
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
    private boolean checkRuleOneREF(Matrix matrix) {
        boolean zeroRowFound = false;
        for (ArrayList<Double> row : matrix.entries) {
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
    private boolean checkRuleTwoREF(Matrix matrix) {
        int row = 0;
        int col = 0;
        while (true) {
            if (checkAllZeros(matrix.entries.get(0))) {
                break;
            }
            else {
                for (int i = 0; i < col; i++) {
                    if (matrix.entries.get(row).get(i) != 0) {
                        return false;
                    }
                }
                col++;
                row++;
            }

            if (row >= matrix.height || col >= matrix.length) {
                break;
            }
        }
        return true;
    }

    // RULE 3 for REF: all entries below a leading entry in the same column should be 0.
    private boolean checkRuleThreeREF(Matrix matrix) {
        for (int i = 0; i < matrix.height; i++) {
            if (!checkAllZeros(matrix.entries.get(i))) {
                int col = findLeadingEntry(matrix.entries.get(i));
                for (int j = i + 1; j < matrix.height; j++) {
                    if (matrix.entries.get(j).get(col) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // RULE 1 for RREF: leading entry for a nonzero row must be 1
    private boolean checkRuleOneRREF(Matrix matrix) {
        for (ArrayList<Double> row : matrix.entries) {
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
    private boolean checkRuleTwoRREF(Matrix matrix) {
        for (int i = 0; i < matrix.height; i++) {
            if (!checkAllZeros(matrix.entries.get(i))) {
                int col = findLeadingEntry(matrix.entries.get(i));
                for (int j = 0; j < matrix.height; j++) {
                    if (j != i) {
                        if (matrix.entries.get(j).get(col) != 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


}
