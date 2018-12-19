package test.rc4;

import java.io.ByteArrayOutputStream;

/**
 * Created by xyp on 18/12/19.
 */
public interface ICrypt {
    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);

    void encrypt(byte[] data, ByteArrayOutputStream stream);

    void encrypt(byte[] data, int length, ByteArrayOutputStream stream);

    void decrypt(byte[] data, ByteArrayOutputStream stream);

    void decrypt(byte[] data, int length, ByteArrayOutputStream stream);

    int getIVLength();

    int getKeyLength();
}
