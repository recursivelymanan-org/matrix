import java.util.ArrayList;

public class Matrix {

    ArrayList<ArrayList<Double>> entries;
    boolean isREF, isRREF;
    int height, length;

    public Matrix() {
        this.entries = new ArrayList<>();
    }

    public Matrix(ArrayList<ArrayList<Double>> entries) throws IllegalArgumentException {
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
        for (ArrayList<Double> row : entries) {
            if (row.size() != rowLength) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        ArrayList<String> print = new ArrayList<>();
        for (ArrayList<Double> row : entries) {
            print.add(row.toString() + "\n");
        }
        StringBuilder result = new StringBuilder();
        for (String s : print) {
            result.append(s);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        MatrixReader reader = new MatrixReader();
        Matrix matrix = reader.readFromTerm();

        MatrixTransformer trans = new MatrixTransformer();
        trans.transformREF(matrix);
    }
}
