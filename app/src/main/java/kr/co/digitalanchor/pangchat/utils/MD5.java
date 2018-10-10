package kr.co.digitalanchor.pangchat.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Thomas on 2015-07-29.
 */
public class MD5 {

    private static final String EMPTY_MD5_RESULT = "00000000000000000000000000000000";

    private static final String ALGORITHM_MD5 = "MD5";

    public static final String getHash(String planedText) {

        String encodedText = "";

        try {

            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);

            md.update(planedText.getBytes());

            byte[] data = md.digest();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < data.length; i++) {

                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));

            }

            encodedText = sb.toString();

        } catch (NoSuchAlgorithmException e) {

            encodedText = EMPTY_MD5_RESULT;
        }

        return encodedText;
    }

    public static final String getHash(Drawable d) {

        String encodedText = "";

        Bitmap bitmap = null;

        try {

            bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] plainedData = stream.toByteArray();

            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);

            md.update(plainedData);

            byte[] data = md.digest();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < data.length; i++) {

                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));

            }

            encodedText = sb.toString();

        } catch (NoSuchAlgorithmException e) {

            encodedText = EMPTY_MD5_RESULT;

        } finally {

            if (bitmap != null && !bitmap.isRecycled()) {

                bitmap.recycle();

                bitmap = null;
            }
        }

        return encodedText;
    }
}
