package edu.sctu.giftbook.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * Created by zhengsenwen on 2018/4/4.
 */

public class QRHelper {

    public static String getResult(Bitmap bitmap) {
        String string = null;
        if (bitmap != null) {
            string = parseBitmap(bitmap);
        }
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        return null;
    }

    private static String parseBitmap(Bitmap bitmap) {
        Result result = parse(bitmap);
        return recode(result.toString());
    }

    private static String recode(String str) {
        String format = "";
        boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                .canEncode(str);
        try {

            if (ISO) {
                format = new String(str.getBytes("ISO-8859-1"), "GB2312");
            } else {
                format = str;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return format;
    }

    private static Result parse(Bitmap bitmap) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        Bitmap parseBitmap = Bitmap.createBitmap(bitmap);
        int px[] = new int[parseBitmap.getHeight() * parseBitmap.getWidth()];
        parseBitmap.getPixels(px, 0, parseBitmap.getWidth(), 0, 0, parseBitmap.getWidth(), bitmap.getHeight());

        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource
                (parseBitmap.getWidth(), parseBitmap.getHeight(), px);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        QRCodeReader qrCodeReader = new QRCodeReader();

        try {
            return qrCodeReader.decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
