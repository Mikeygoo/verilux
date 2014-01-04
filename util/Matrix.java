package util;

import java.util.Arrays;

/**
 *
 * @author michael
 */
public class Matrix {
    private double[][] m;

    public Matrix() {
        m = new double[4][4];
    }

    public double get(int i, int j) {
        return m[i][j];
    }

    public void set(double d, int i, int j) {
        m[i][j] = d;
    }

    public void set(Matrix other) {
        for (int i = 0; i < 4; i++)
            System.arraycopy(other.m[i], 0, m[i], 0, 4);
    }

    public Matrix multiply(Matrix other) {
        Matrix product = new Matrix();

        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++) {
                double sum = 0.0;

                for (int j = 0; j < 4; j++)
                    sum += m[x][j] * other.m[j][y];

                product.m[x][y] = sum;
            }

        return product;
    }

    public Point3D multiply(Point3D p) {
        return new Point3D(m[0][0] * p.x + m[0][1] * p.y + m[0][2] * p.z,
                           m[1][0] * p.x + m[1][1] * p.y + m[1][2] * p.z,
                           m[2][0] * p.x + m[2][1] * p.y + m[2][2] * p.z);
    }

    public Normal multiply(Normal n) {
        return new Normal(m[0][0] * n.x + m[0][1] * n.y + m[0][2] * n.z,
                          m[1][0] * n.x + m[1][1] * n.y + m[1][2] * n.z,
                          m[2][0] * n.x + m[2][1] * n.y + m[2][2] * n.z);
    }

    public Vector3D multiply(Vector3D v) {
        return new Vector3D(m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z,
                            m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z,
                            m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z);
    }

    public void setIdentity() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                m[i][j] = (i == j ? 1 : 0);
    }
}
