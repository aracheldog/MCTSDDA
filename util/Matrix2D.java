/*
 * Decompiled with CFR 0.150.
 */
package util;

public class Matrix2D {
    private double[][] matrix;

    public Matrix2D(double[] vector) {
        this.matrix = new double[vector.length][1];
        for (int i = 0; i < vector.length; ++i) {
            this.matrix[i][0] = vector[i];
        }
    }

    public Matrix2D(double[][] vector) {
        this.matrix = new double[vector.length][vector[0].length];
        for (int r = 0; r < vector.length; ++r) {
            for (int c = 0; c < vector[r].length; ++c) {
                this.matrix[r][c] = vector[r][c];
            }
        }
    }

    public double[][] getArrays() {
        return this.matrix;
    }

    public int getRow() {
        return this.matrix.length;
    }

    public int getCol() {
        return this.matrix[0].length;
    }

    public Matrix2D T() {
        double[][] t = new double[this.getCol()][this.getRow()];
        for (int r = 0; r < t.length; ++r) {
            for (int c = 0; c < t[r].length; ++c) {
                t[r][c] = this.matrix[c][r];
            }
        }
        return new Matrix2D(t);
    }

    private void changeValue(int row, int col, double a) {
        this.matrix[row][col] = a;
    }

    private double getValue(int row, int col) {
        return this.matrix[row][col];
    }

    public static Matrix2D dot(Matrix2D a, Matrix2D b) {
        double[][] d = new double[a.getRow()][b.getCol()];
        for (int r = 0; r < a.getRow(); ++r) {
            for (int c = 0; c < b.getCol(); ++c) {
                double sum = 0.0;
                for (int k = 0; k < b.getRow(); ++k) {
                    sum += a.getValue(r, k) * b.getValue(k, c);
                }
                d[r][c] = sum;
            }
        }
        return new Matrix2D(d);
    }

    public static Matrix2D add(Matrix2D a, Matrix2D b) {
        double[][] d = new double[a.getRow()][a.getCol()];
        for (int c = 0; c < a.getCol(); ++c) {
            for (int r = 0; r < a.getRow(); ++r) {
                d[r][c] = a.getValue(r, c) + b.getValue(r, c);
            }
        }
        return new Matrix2D(d);
    }

    public static Matrix2D sub(Matrix2D a, Matrix2D b) {
        double[][] d = new double[a.getRow()][a.getCol()];
        for (int c = 0; c < a.getRow(); ++c) {
            for (int r = 0; r < a.getCol(); ++r) {
                d[r][c] = a.getValue(r, c) - b.getValue(r, c);
            }
        }
        return new Matrix2D(d);
    }

    public static Matrix2D sqrt(Matrix2D a) {
        double[][] d = new double[a.getRow()][a.getCol()];
        for (int r = 0; r < a.getRow(); ++r) {
            for (int c = 0; c < a.getCol(); ++c) {
                d[r][c] = Math.sqrt(a.getValue(r, c));
            }
        }
        return new Matrix2D(d);
    }

    public static Matrix2D sqrt(Matrix2D a, double b) {
        double[][] d = new double[a.getRow()][a.getCol()];
        for (int r = 0; r < a.getRow(); ++r) {
            for (int c = 0; c < a.getCol(); ++c) {
                d[r][c] = Math.pow(a.getValue(r, c), b);
            }
        }
        return new Matrix2D(d);
    }

    public static Matrix2D prod(double a, Matrix2D b) {
        double[][] d = new double[b.getRow()][b.getCol()];
        for (int r = 0; r < b.getRow(); ++r) {
            for (int c = 0; c < b.getCol(); ++c) {
                d[r][c] = b.getValue(r, c) * a;
            }
        }
        return new Matrix2D(d);
    }

    public static Matrix2D mult(Matrix2D a, Matrix2D b) {
        if (a.getCol() == b.getRow()) {
            double[][] d = new double[a.getRow()][b.getCol()];
            for (int r = 0; r < a.getRow(); ++r) {
                for (int c = 0; c < b.getCol(); ++c) {
                    double sum = 0.0;
                    for (int i = 0; i < b.getRow(); ++i) {
                        sum += a.getValue(r, i) * b.getValue(i, c);
                    }
                    d[r][c] = sum;
                }
            }
            return new Matrix2D(d);
        }
        return null;
    }

    public static Matrix2D catHorizon(Matrix2D a, Matrix2D b) {
        if (a.getRow() == b.getRow()) {
            double[][] d = new double[a.getRow()][a.getCol() + b.getCol()];
            for (int r = 0; r < a.getRow(); ++r) {
                int c;
                for (c = 0; c < a.getCol(); ++c) {
                    d[r][c] = a.getValue(r, c);
                }
                for (c = 0; c < b.getCol(); ++c) {
                    d[r][a.getCol() + c] = b.getValue(r, c);
                }
            }
            return new Matrix2D(d);
        }
        return null;
    }

    public static Matrix2D catVertical(Matrix2D a, Matrix2D b) {
        if (a.getCol() == b.getCol()) {
            double[][] d = new double[a.getRow() + b.getRow()][a.getCol()];
            for (int c = 0; c < a.getCol(); ++c) {
                int r;
                for (r = 0; r < a.getRow(); ++r) {
                    d[r][c] = a.getValue(r, c);
                }
                for (r = 0; r < b.getRow(); ++r) {
                    d[a.getRow() + r][c] = b.getValue(r, c);
                }
            }
            return new Matrix2D(d);
        }
        return null;
    }

    private static Matrix2D pivodRow(int i, int j, Matrix2D a) {
        double[][] b = new double[a.getRow()][a.getCol()];
        for (int r = 0; r < a.getRow(); ++r) {
            for (int c = 0; c < a.getCol(); ++c) {
                b[r][c] = r == i ? a.getValue(j, c) : (r == j ? a.getValue(i, c) : a.getValue(r, c));
            }
        }
        return new Matrix2D(b);
    }

    public static Matrix2D inv(Matrix2D a) {
        int r;
        Matrix2D d = Matrix2D.catHorizon(a, Matrix2D.IdentityMatrix(a.getRow()));
        block0: for (r = 0; r < a.getRow(); ++r) {
            if (a.getValue(r, r) >= 0.0) continue;
            for (int i = r + 1; i < a.getRow(); ++i) {
                if (!(a.getValue(i, r) >= 0.0)) continue;
                d = Matrix2D.pivodRow(r, i, d);
                continue block0;
            }
        }
        for (r = 0; r < a.getCol(); ++r) {
            double A = 1.0 / d.getValue(r, r);
            for (int c = 0; c < d.getCol(); ++c) {
                d.changeValue(r, c, A * d.getValue(r, c));
            }
            for (int i = r + 1; i < a.getRow(); ++i) {
                double B = d.getValue(i, r);
                for (int c = 0; c < d.getCol(); ++c) {
                    d.changeValue(i, c, d.getValue(i, c) - B * d.getValue(r, c));
                }
            }
        }
        for (r = 0; r < a.getRow(); ++r) {
            for (int i = r + 1; i < a.getCol(); ++i) {
                double B = d.getValue(r, i);
                for (int j = i; j < d.getCol(); ++j) {
                    d.changeValue(r, j, d.getValue(r, j) - B * d.getValue(i, j));
                }
            }
        }
        double[][] e = new double[a.getRow()][a.getCol()];
        for (int r2 = 0; r2 < a.getRow(); ++r2) {
            for (int c = 0; c < a.getCol(); ++c) {
                e[r2][c] = d.getValue(r2, c + a.getCol());
            }
        }
        return new Matrix2D(e);
    }

    public static Matrix2D IdentityMatrix(int N) {
        double[][] a = new double[N][N];
        for (int r = 0; r < N; ++r) {
            for (int c = 0; c < N; ++c) {
                if (r != c) continue;
                a[r][c] = 1.0;
            }
        }
        return new Matrix2D(a);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < this.getRow(); ++r) {
            sb.append("|");
            for (int c = 0; c < this.getCol(); ++c) {
                if (this.matrix[r][c] < 0.0) {
                    sb.append(String.format("%.5f ", this.matrix[r][c]));
                    continue;
                }
                sb.append(String.format(" %.5f ", this.matrix[r][c]));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}

