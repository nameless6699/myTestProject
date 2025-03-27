package com.dspread.demoui.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileConfig {
    public  File logFileWR = null;

    private String FILE_DS_LOG = "ds_log";
    private String  PATH_DS_LOG= "/DSLogs/";
    private boolean writeFlag = true;
    private static Context mContext;

    private LogFileConfig() {
        LogFileInit(FILE_DS_LOG);
    }

    public void setWriteFlag(boolean writeFlag) {
        this.writeFlag = writeFlag;
    }

    public boolean getWriteFlag() {
        return writeFlag;
    }

    private static class LogFileConfigHolder {
        private static LogFileConfig config = new LogFileConfig();
    }


    public static LogFileConfig getInstance(Context context) {
        mContext = context;
        return LogFileConfigHolder.config;
    }

    private void LogFileInit( String fileName) {
        String model = Build.MODEL;
        String brand = Build.BRAND;

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        String filename = format.format(date);
        if ("".equals(fileName) || null == fileName) {
            filename = filename + "_" + brand + "_" + model + ".txt";
        } else {
            filename = fileName + "_" + filename + "_" + brand + "_" + model + ".txt";
        }

        logFileWR = createMyFile(filename);
    }

    public void writeLog(String str) {
        if (!writeFlag) {
            return;
        }
        if (logFileWR == null) {
            LogFileInit(FILE_DS_LOG);
            return;
        }
        try {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String logDate = format.format(date);

            str += "\r\n";
            str = logDate + "--" + str;
            DataOutputStream d = new DataOutputStream(new FileOutputStream(logFileWR, true));
            d.write(str.getBytes());
            d.flush();
            d.close();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public String readLog(){
        InputStream inputStream = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {

            File file= logFileWR;
            inputStream = new FileInputStream(file);
            reader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(reader);
            StringBuilder result = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                result.append(temp);
            }
            Log.i("POS", "result:" + result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    private File createMyFile(String fileName) {
        File file = null;
        try {
            file = new File(mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (Exception e) {

        }
        Log.d("pos", "文件路径：" + file.getAbsolutePath());

        return file;
    }



    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public  boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
            return dir.delete();
    }

    public boolean deleteAllFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), PATH_DS_LOG);
        if (!dir.exists()) {
            return true;
        }
            String[] children = dir.list();
            if (children != null && children.length > 0) {
                for (int i=0; i<children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
            }
            }
        // 目录此时为空，可以删除
        return dir.delete();

    }


}
