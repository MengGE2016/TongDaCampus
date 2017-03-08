package cn.mengge.tongdacampus.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MengGE on 2016/9/22.
 */
public class FileServer {

    private String fileName;
    private Context context;

    public FileServer(Context context) {
        this.context = context;
    }


    public FileServer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    /**
     * 创建文件夹
     * <p/>
     * //     * @param path     路径
     *
     * @param dir_name 文件夹名字
     * @return 是否创建成功
     */
    public boolean makeDir(String dir_name) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + dir_name);
            if (!dir.exists()) {
                dir.mkdirs();
                return true;
            }
            return true;
        }
        return false;
    }

    public void saveStringFile2SD(String fileName, String content) {

    }

//    public byte[] Bitmap2ByteArr(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//
//        return byteArrayOutputStream.toByteArray();
//    }

    /**
     * 保存图片到SD卡
     *
     * @param path     路径
     * @param fileName 图片名称.jpg
     * @param bitmap   图片(位图)
     */
    public void saveImageFile2SD(String path, String fileName, Bitmap bitmap) {
        if (!(Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))) {

            Toast.makeText(context, "请检查SD卡是否插好", Toast.LENGTH_SHORT).show();
        } else {
            File image = new File(Environment.getExternalStorageDirectory() + "/" + path + "/" + fileName);
//            Log.d("Image_Path", Environment.getExternalStorageDirectory() + "/" + path + "/" + fileName);
            FileOutputStream fileOutputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(image);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);

                fileOutputStream.flush();
                bufferedOutputStream.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) fileOutputStream.close();
                    if (bufferedOutputStream != null) bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap getBitmapFromSDcard(String path, String imageName) {
        Bitmap bitmap = null;
        if (!(Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))) {
            Toast.makeText(context, "请检查SD卡是否插好", Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + path + "/" + imageName);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + path + "/" + imageName);
            }
        }
        return bitmap;
    }


    /**
     * 从网络流下载文件到SD卡
     *
     * @param inputStream 网络流
     * @param path        文件保存路径
     * @param fileName    文件名
     */
    public void saveFileFromInternet2SD(InputStream inputStream, String path, String fileName) {
        if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            Toast.makeText(context, "请检查SD卡是否插好", Toast.LENGTH_SHORT).show();
        } else {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + path + "/" + fileName);
                int len = 0;
                byte[] data = new byte[10];
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 通过字节流复制文件到其他文件夹
     *
     * @param sorcePath  原路径包括文件名
     * @param targetPath 目的路径
     * @param fileName   复制后的文件名称
     */
    public void copyFile2OtherDirbyByte(String sorcePath, String targetPath, String fileName) {
        if (!(Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))) {

            Toast.makeText(context, "请检查SD卡是否插好", Toast.LENGTH_SHORT).show();
        } else {
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + sorcePath);
                fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + targetPath + "/" + fileName);
                int len = 0;
                byte[] data = new byte[10];
                while ((len = fileInputStream.read(data)) != -1)
                    fileOutputStream.write(data, 0, len);

            } catch (FileNotFoundException E) {
                E.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
