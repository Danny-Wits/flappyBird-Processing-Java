import java.lang.reflect.Array;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Matrix Class representing a two-dimensional array with additional matrix
 * operations.
 * It is designed to perform various operations on matrices commonly used in
 * linear algebra.
 * 
 * @param <N> The type of Number (e.g., Integer, Float, Double) this matrix will
 *            store.
 * 
 * @author Danny-Wits
 */
@SuppressWarnings("unchecked")
public class Matrix<N extends Number> implements java.io.Serializable, Cloneable {
    // !FIELDS
    // Wrapped 2D array representing the matrix.
    private N[][] array = null;

    @java.io.Serial
    private static final long serialVersionUID = -1212121212121L;

    // !GETTERS AND SETTERS
    /**
     * Gets the internal 2D array representing the matrix.
     * 
     * @return 2D array of type N[][]
     */
    public N[][] getArray() {
        return array;
    }

    /**
     * Sets the internal 2D array representing the matrix.
     * 
     * @param 2D_array of type N[][]
     */
    public void setArray(N[][] array) {
        this.array = array;
    }

    /**
     * Returns the number of rows in the matrix.
     * 
     * @return The number of rows in the matrix.
     */
    public int rowCount() {
        return array.length;
    }

    /**
     * Returns the number of columns in the matrix.
     * 
     * @return The number of columns in the matrix.
     */
    public int columnCount() {
        return array[0].length;
    }

    /**
     * Retrieves the specified row from the matrix.
     * 
     * @param index The index of the row to retrieve.
     * @return The row as an array of type N[].
     * @throws MatrixException If the row index is out of bounds.
     */
    public N[] getRow(int index) {
        if (index < 0 || index >= rowCount()) {
            throw OutOfBoundOfMatrix;
        }

        var row = (N[]) Array.newInstance(getValueAt(0, 0).getClass(), columnCount());
        for (int i = 0; i < row.length; i++) {
            row[i] = array[index][i];
        }
        return row;
    }

    /**
     * Retrieves the specified column from the matrix.
     * 
     * @param index The index of the column to retrieve.
     * @return The column as an array of type N[].
     * @throws MatrixException If the column index is out of bounds.
     */
    public N[] getColumn(int index) {
        if (index < 0 || index >= columnCount()) {
            throw OutOfBoundOfMatrix;
        }

        var column = (N[]) Array.newInstance(getValueAt(0, 0).getClass(), rowCount());
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    /**
     * Sets the value at the specified row and column in the matrix.
     * 
     * @param row    The row index where the value is to be set.
     * @param column The column index where the value is to be set.
     * @param value  The value to set at the specified position.
     * @return The matrix itself (for method chaining).
     * @throws MatrixException If the specified row or column is out of bounds.
     */
    public Matrix<N> setValueAt(int row, int column, N value) {
        if (!inBound(row, column))
            throw OutOfBoundOfMatrix;
        this.array[row][column] = value;
        return this;
    }

    /**
     * Retrieves the value at the specified row and column in the matrix.
     * 
     * @param row    The row index of the value to retrieve.
     * @param column The column index of the value to retrieve.
     * @return The value at the specified position in the matrix.
     * @throws MatrixException If the specified row or column is out of bounds.
     */
    public N getValueAt(int row, int column) {
        if (!inBound(row, column))
            throw OutOfBoundOfMatrix;
        return this.array[row][column];
    }

    /**
     * Retrieves the value at the specified row and column in the matrix.
     * If the specified indices are out of bounds, the provided default value is
     * returned.
     * 
     * @param row     The row index of the value to retrieve.
     * @param column  The column index of the value to retrieve.
     * @param Default The default value to return if the specified indices are out
     *                of bounds.
     * @return The value at the specified position in the matrix, or the default
     *         value if out of bounds.
     */
    public N getValueAt(int row, int column, N Default) {
        if (!inBound(row, column))
            return Default;
        return this.array[row][column];
    }

    // !CONSTRUCTORS
    /**
     * Default constructor that creates an empty matrix.
     */
    public Matrix() {

    }

    /**
     * Constructs a matrix from a provided 2D array.
     * 
     * @param array A 2D array of type N[][].
     * @throws MatrixException If the provided array has invalid dimensions.
     */
    public Matrix(N[][] array) {
        if (!isValidLength(array)) {
            throw ZeroLengthException;
        }
        this.array = array;
    }

    /**
     * Constructs a matrix from a provided 1D array, interpreting it as a single row
     * matrix.
     * 
     * @param array A 1D array of type N[].
     * @throws MatrixException If the provided array has invalid dimensions.
     */
    public Matrix(N[] array) {
        if (array.length <= 0) {
            throw ZeroLengthException;
        }
        N[][] array2d = (N[][]) Array.newInstance(array[0].getClass(), 1, array.length);
        for (int i = 0; i < array.length; i++) {
            array2d[0][i] = array[i];
        }
        this.array = array2d;
    }

    /**
     * Constructs a new matrix with the specified number of rows and columns,
     * initializing all elements with a default value.
     *
     * @param <N>     The type of numbers stored in the matrix. This must extend
     *                {@link Number}.
     * @param rows    The number of rows in the new matrix.
     * @param columns The number of columns in the new matrix.
     * @param Default The default value to initialize all elements in the matrix.
     * @throws IllegalArgumentException if the specified rows or columns are zero or
     *                                  negative.
     */
    public Matrix(int rows, int columns, N Default) {
        var array = (N[][]) Array.newInstance(Default.getClass(), rows, columns);
        if (!isValidLength(array)) {
            throw ZeroLengthException;
        }

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = Default;
            }
        }

        this.array = array;
    }

    /**
     * Constructs a new matrix by copying the elements of the specified matrix.
     *
     * @param <N>   The type of numbers stored in the matrix. This must extend
     *              {@link Number}.
     * @param other The matrix to be copied.
     * @throws NullPointerException if the specified matrix is null.
     */
    public Matrix(Matrix<N> other) {
        this(other.getArray().clone());
    }

    // !MATRIX ALGEBRA
    /**
     * Adds two matrices element-wise. Both matrices must have the same dimensions.
     * If the dimensions do not match, an exception is thrown.
     *
     * @param <T> The type of numbers stored in the matrix. This must extend
     *            {@link Number}.
     * @param m1  The first matrix to add.
     * @param m2  The second matrix to add.
     * @return A new {@link Matrix} object containing the element-wise sum of m1 and
     *         m2.
     * @throws IllegalArgumentException if the dimensions of the matrices do not
     *                                  match.
     */
    public static <T extends Number> Matrix<T> addition(Matrix<T> m1, Matrix<T> m2) {
        int row = m1.rowCount();
        int column = m1.columnCount();
        if (row != m2.rowCount() || column != m2.columnCount())
            throw DimensionsDoNotMatch;
        var array = (T[][]) Array.newInstance(m1.getValueAt(0, 0).getClass(), row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                array[i][j] = (T) addNumbers(m1.getValueAt(i, j), m2.getValueAt(i, j));
            }
        }
        Matrix<T> m3 = new Matrix<>(array);
        return m3;
    }

    /**
     * Performs matrix multiplication of two matrices m1 and m2.
     * The number of columns in m1 must equal the number of rows in m2.
     *
     * @param <T> The type of numbers stored in the matrix. This must extend
     *            {@link Number}.
     * @param m1  The first matrix to multiply.
     * @param m2  The second matrix to multiply.
     * @return A new {@link Matrix} object that contains the product of m1 and m2.
     * @throws IllegalArgumentException if the number of columns in m1 does not
     *                                  match the number of rows in m2.
     */
    public static <T extends Number> Matrix<T> multiplication(Matrix<T> m1, Matrix<T> m2) {
        if (m1.columnCount() != m2.rowCount()) {
            throw DimensionsDoNotMatchForMultiplication;
        }
        var array = (T[][]) Array.newInstance(m1.getValueAt(0, 0).getClass(), m1.rowCount(), m2.columnCount());
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = productSum(m1.getRow(i), m2.getColumn(j));
            }
        }
        return new Matrix<>(array);
    }

    /**
     * Computes the transpose of a given matrix.
     * The transpose of a matrix is obtained by swapping its rows and columns.
     *
     * @param <T> The type of numbers stored in the matrix. This must extend
     *            {@link Number}.
     * @param m   The matrix to be transposed.
     * @return A new {@link Matrix} object that contains the transpose of the input
     *         matrix.
     */
    public static <T extends Number> Matrix<T> transposition(Matrix<T> m) {
        Matrix<T> TransposeMatrix = new Matrix<>();
        for (int i = 0; i < m.columnCount(); i++) {
            TransposeMatrix.addRow(m.getColumn(i));
        }
        return TransposeMatrix;
    }

    /**
     * Compares two matrices for equality.
     *
     * @param m1 the first matrix to compare
     * @param m2 the second matrix to compare
     * @return true if the matrices have the same dimensions and all corresponding
     *         elements are equal; false otherwise
     * @throws NullPointerException if either matrix is null
     */
    public static <T extends Number> boolean equal(Matrix<T> m1, Matrix<T> m2) {
        int row1 = m1.rowCount();
        int col1 = m1.columnCount();
        int row2 = m2.rowCount();
        int col2 = m2.columnCount();
        if (row1 != row2 || col1 != col2)
            return false;
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                if (!(m1.getValueAt(i, j).equals(m2.getValueAt(i, j)))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Subtracts two matrices element-wise and returns the result as a new matrix.
     *
     * <p>
     * The method performs element-wise subtraction between the elements of the two
     * matrices {@code m1} and {@code m2}. The matrices must have the same
     * dimensions;
     * otherwise, a {@code DimensionsDoNotMatch} exception is thrown.
     *
     * @param <T> the type of numbers contained in the matrices, which must extend
     *            {@link Number}
     * @param m1  the first matrix
     * @param m2  the second matrix
     * @return a new matrix containing the element-wise subtraction of {@code m1}
     *         and {@code m2}
     * @throws DimensionsDoNotMatch if the dimensions of the two matrices do not
     *                              match
     */
    public static <T extends Number> Matrix<T> subtraction(Matrix<T> m1, Matrix<T> m2) {
        int row = m1.rowCount();
        int column = m1.columnCount();
        if (row != m2.rowCount() || column != m2.columnCount())
            throw DimensionsDoNotMatch;
        var array = (T[][]) Array.newInstance(m1.getValueAt(0, 0).getClass(), row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                array[i][j] = (T) subtractNumbers(m1.getValueAt(i, j), m2.getValueAt(i, j));
            }
        }
        Matrix<T> m3 = new Matrix<>(array);
        return m3;
    }

    // ! MATRIX METHODS
    /**
     * Adds the current matrix to another matrix and updates the current matrix with
     * the result.
     *
     * @param <N>   The type of numbers stored in the matrix. This must extend
     *              {@link Number}.
     * @param other The matrix to be added to the current matrix.
     * @return The updated current {@link Matrix} instance containing the sum.
     * @throws IllegalArgumentException if the dimensions of the current matrix and
     *                                  the other matrix do not match.
     */
    public Matrix<N> add(Matrix<N> other) {
        this.array = addition(this, other).getArray();
        return this;
    }

    /**
     * Adds a constant value to each element of the current matrix and updates
     * the current matrix with the result.
     *
     * <p>
     * This method performs an element-wise addition, where the specified constant
     * is added to each element of the matrix. The matrix is updated in place with
     * the sum.
     *
     * @param constant the constant value to be added to each element of the matrix
     * @return this matrix, updated with the result of the element-wise addition
     */
    public Matrix<N> add(N constant) {
        this.array = (N[][]) map(e -> addNumbers(e, constant));
        return this;
    }

    /**
     * Subtracts the given matrix from this matrix and updates the current matrix
     * with the result.
     *
     * <p>
     * This method performs an element-wise subtraction of the matrix {@code other}
     * from the
     * current matrix, where {@code other} is subtracted from the current matrix.
     * The dimensions
     * of the two matrices must be the same; otherwise, an exception is thrown.
     *
     * @param other the matrix to be subtracted from this matrix
     * @return this matrix, updated with the result of the subtraction
     * @throws DimensionsDoNotMatch if the dimensions of the two matrices do not
     *                              match
     */
    public Matrix<N> subtract(Matrix<N> other) {
        this.array = subtraction(this, other).getArray();
        return this;
    }

    /**
     * Subtracts a constant value from each element of the current matrix and
     * updates the current matrix with the result.
     *
     * <p>
     * This method performs an element-wise subtraction, where the specified
     * constant
     * is subtracted from each element of the matrix. The matrix is updated in place
     * with the result of the subtraction.
     *
     * @param constant the constant value to be subtracted from each element of the
     *                 matrix
     * @return this matrix, updated with the result of the element-wise subtraction
     */
    public Matrix<N> subtract(N constant) {
        this.array = (N[][]) map(e -> subtractNumbers(e, constant));
        return this;
    }

    /**
     * Multiplies the current matrix by another matrix and updates the current
     * matrix with the result.
     *
     * @param <N>   The type of numbers stored in the matrix. This must extend
     *              {@link Number}.
     * @param other The matrix to be multiplied with the current matrix.
     * @return The updated current {@link Matrix} instance containing the product.
     * @throws IllegalArgumentException if the number of columns of the current
     *                                  matrix does not match the number of rows of
     *                                  the other matrix.
     */
    public Matrix<N> multiply(Matrix<N> other) {
        this.array = Matrix.multiplication(this, other).getArray();
        return this;
    }

    /**
     * Multiplies each element of the current matrix by a constant value and updates
     * the current matrix with the result.
     *
     * <p>
     * This method performs an element-wise multiplication where each element of
     * the matrix is multiplied by the provided constant value. The matrix is
     * updated
     * in place with the result.
     *
     * @param constant the constant value to multiply each matrix element by
     * @return this matrix, updated with the result of the element-wise
     *         multiplication
     */
    public Matrix<N> multiply(N constant) {
        this.array = (N[][]) map(e -> multiplyNumbers(e, constant));
        return this;
    }

    /**
     * Computes the transpose of the current matrix and updates it with the result.
     *
     * @param <N> The type of numbers stored in the matrix. This must extend
     *            {@link Number}.
     * @return The updated current {@link Matrix} instance containing the transpose.
     */
    public Matrix<N> transpose() {
        this.array = transposition(this).getArray();
        return this;
    }

    /**
     * Compares this matrix to the specified object for equality.
     *
     * <p>
     * This method checks if the specified object is also a matrix and,
     * if so, compares the contents of both matrices using the
     * {@link #equal(Matrix, Matrix)} method.
     *
     * @param other the object to compare with this matrix
     * @return true if the other object is a matrix of the same type
     *         and has the same values as this matrix; false otherwise
     */
    @Override
    public boolean equals(Object other) {

        if (!(other.getClass().equals(this.getClass())))
            return false;

        return equal(this, (Matrix<N>) other);
    }

    // !DISPLAY

    /**
     * Returns a string representation of the matrix, formatted as rows and columns.
     * 
     * @return A string representing the matrix.
     */
    @Override
    public String toString() {
        if (!isValidLength(array)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (N[] row : array) {
            for (N number : row) {
                sb.append(number.toString()).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Prints the matrix to the console.
     * 
     * @return The matrix itself (for method chaining).
     * @throws MatrixException If the matrix has invalid dimensions.
     */
    public Matrix<N> print() {
        if (!isValidLength(array)) {
            throw ZeroLengthException;
        }

        System.out.println("-----------------------");
        System.out.print(this.toString());
        System.out.println("-----------------------");
        return this;
    }

    // ! CUSTOM MATRIX EXCEPTIONS

    /**
     * Custom exception for matrix-related errors.
     */
    static class MatrixException extends RuntimeException {
        MatrixException(String msg) {
            super(msg, new Throwable("Error in Matrix"), false, true);
        }
    }

    private static MatrixException ZeroLengthException = new MatrixException("Empty Matrix");
    private static MatrixException OutOfBoundOfMatrix = new MatrixException("Matrix index out of bounds");
    private static MatrixException RowSizeDoesNotMatch = new MatrixException("Row Size Does Not Match ");
    private static MatrixException DimensionsDoNotMatch = new MatrixException("Dimensions Do Not Match ");
    private static MatrixException DimensionsDoNotMatchForMultiplication = new MatrixException(
            "Dimensions Do Not Match For Multiplication (Column in first should be equal to the rows in the other) ");

    // !FUNCTIONALITY
    /**
     * Applies the provided function to each element in the matrix.
     * 
     * @param F A consumer function that takes an element of type N and processes
     *          it.
     */
    public void forEach(Consumer<N> F) {
        for (N[] row : array) {
            for (N number : row) {
                F.accept(number);
            }
        }
    }

    /**
     * Applies a given function to each element of the current matrix and returns a
     * new
     * array containing the results.
     *
     * <p>
     * This method iterates over each element of the current matrix, applies the
     * provided
     * function {@code F} to each element, and constructs a new array of type
     * {@code X}.
     * The resulting array will have the same dimensions as the original matrix.
     *
     * @param <X> the type of the elements in the resulting array, which can be
     *            different
     *            from the type of elements in the original matrix
     * @param F   a function that takes an element of type {@code N} (the type of
     *            the original
     *            matrix elements) and returns an element of type {@code X}
     * @return a new array containing the results of applying the function {@code F}
     *         to each
     *         element of the current matrix
     */
    public <X> X[][] map(Function<N, X> F) {
        int row = rowCount();
        int column = columnCount();
        X test = F.apply(this.array[0][0]);
        var array = (X[][]) Array.newInstance(test.getClass(), row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                array[i][j] = F.apply(this.array[i][j]);
            }
        }
        return array;
    }

    /**
     * Performs a reduction on the elements of the matrix using a specified binary
     * operation and an initial value.
     *
     * <p>
     * This method iterates through each element of the matrix and applies the
     * provided
     * binary function {@code F} to each element and an accumulator (the current
     * value of {@code collector}). The operation is performed from left to right,
     * and the result is accumulated in {@code collector}. The final value of the
     * accumulator is returned after processing all elements of the matrix.
     *
     * @param F    a binary function that takes two elements of type {@code N} and
     *             returns a single element of type {@code N}, used for the
     *             reduction
     * @param init the initial value for the reduction operation
     * @return the result of the reduction operation, after applying the function
     *         {@code F} to all elements of the matrix
     */
    public N reduce(BiFunction<N, N, N> F, N init) {
        int row = rowCount();
        int column = columnCount();
        var collector = init;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                collector = F.apply(this.array[i][j], collector);
            }
        }
        return collector;
    }

    /**
     * Adds a new row to the matrix with the specified elements.
     * If the matrix is currently empty (null), this method initializes it with the
     * provided row.
     * Otherwise, it appends the new row to the existing matrix, ensuring that the
     * number of elements
     * matches the current column count.
     * 
     * @param elements A variable-length argument (varargs) representing the
     *                 elements of the new row.
     * @return The matrix itself (for method chaining).
     * @throws MatrixException If the number of elements does not match the number
     *                         of columns in the matrix.
     */
    public Matrix<N> addRow(N... elements) {
        if (this.array == null) {
            var array2d = (N[][]) Array.newInstance(elements[0].getClass(), 1, elements.length);
            for (int i = 0; i < elements.length; i++) {
                array2d[0][i] = elements[i];
            }
            this.array = array2d;
            return this;
        }
        if (elements.length != columnCount())
            throw RowSizeDoesNotMatch;
        int row = rowCount();
        var array = (N[][]) Array.newInstance(elements[0].getClass(), row + 1, columnCount());
        for (int i = 0; i < this.array.length; i++) {
            for (int j = 0; j < this.array[0].length; j++) {
                array[i][j] = this.array[i][j];
            }
        }
        for (int i = 0; i < array[0].length; i++) {
            array[row][i] = elements[i];
        }
        this.array = array;
        return this;
    }

    /**
     * Flattens the matrix into a one-dimensional array.
     *
     * @return a one-dimensional array containing all the elements of the matrix
     */
    public N[] flatten() {
        int row = rowCount();
        int column = columnCount();
        int length = row * column;
        var array = (N[]) Array.newInstance(getValueAt(0, 0).getClass(), length);

        for (int i = 0; i < length; i++) {
            array[i] = getValueAt(i / column, i % column);
        }
        return array;
    }

    @Override
    public Matrix<N> clone() {
        return new Matrix<>(this);
    }

    // ! HELPER FUNCTIONS
    /**
     * Checks whether the specified row and column indices are within the bounds of
     * the matrix.
     * 
     * @param row    The row index to check.
     * @param column The column index to check.
     * @return {@code true} if both the row and column are within the valid bounds
     *         of the matrix, {@code false} otherwise.
     */
    private boolean inBound(int row, int column) {
        if (row < 0 || row >= rowCount()) {
            return false;
        }
        if (column < 0 || column >= columnCount()) {
            return false;
        }
        return true;
    }

    /**
     * Validates if the provided array has valid dimensions for a matrix.
     * 
     * @param array A 2D array of type T[][].
     * @param <T>   The type of elements in the array (must extend Number).
     * @return True if the array has valid dimensions, false otherwise.
     */
    private static <T extends Number> boolean isValidLength(T[][] array) {
        if (array == null) {
            return false;
        }
        if (array.length <= 0) {
            return false;
        }
        if (array[0].length <= 0) {
            return false;
        }
        return true;
    }

    private static <T extends Number> T addNumbers(T a, T b) {
        if (a instanceof Double) {
            return (T) Double.valueOf(a.doubleValue() + b.doubleValue());
        } else if (a instanceof Integer) {
            return (T) Integer.valueOf(a.intValue() + b.intValue());
        } else if (a instanceof Long) {
            return (T) Long.valueOf(a.longValue() + b.longValue());
        } else if (a instanceof Float) {
            return (T) Float.valueOf(a.floatValue() + b.floatValue());
        }
        throw new UnsupportedOperationException("Unsupported number type");
    }

    private static <T extends Number> T multiplyNumbers(T a, T b) {
        if (a instanceof Double) {
            return (T) Double.valueOf(a.doubleValue() * b.doubleValue());
        } else if (a instanceof Integer) {
            return (T) Integer.valueOf(a.intValue() * b.intValue());
        } else if (a instanceof Long) {
            return (T) Long.valueOf(a.longValue() * b.longValue());
        } else if (a instanceof Float) {
            return (T) Float.valueOf(a.floatValue() * b.floatValue());
        }
        throw new UnsupportedOperationException("Unsupported number type");
    }

    private static <T extends Number> T subtractNumbers(T a, T b) {
        if (a instanceof Double) {
            return (T) Double.valueOf(a.doubleValue() - b.doubleValue());
        } else if (a instanceof Integer) {
            return (T) Integer.valueOf(a.intValue() - b.intValue());
        } else if (a instanceof Long) {
            return (T) Long.valueOf(a.longValue() - b.longValue());
        } else if (a instanceof Float) {
            return (T) Float.valueOf(a.floatValue() - b.floatValue());
        }
        throw new UnsupportedOperationException("Unsupported number type");
    }

    private static <T extends Number> T productSum(T[] a1, T[] a2) {
        T sum = (T) multiplyNumbers(a1[0], 0);
        for (int i = 0; i < a2.length; i++) {
            sum = addNumbers(sum, multiplyNumbers(a1[i], a2[i]));
        }
        return sum;
    }

}
