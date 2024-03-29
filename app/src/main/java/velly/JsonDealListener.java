package velly;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import velly.Interface.IDataListener;
import velly.Interface.IHttpListener;
import velly.util.FileUtil;

public class JsonDealListener<M> implements IHttpListener {
    private static final String TAG = JsonDealListener.class.getName();
    private Class<M> response;
    private IDataListener<M> dataListener;

    private Handler handler = new Handler(Looper.getMainLooper());

    public JsonDealListener(Class<M> response, IDataListener<M> dataListener) {
        this.response = response;
        this.dataListener = dataListener;
    }

    @Override
    public void onSuccess(HttpEntity httpEntity) {
        InputStream is = null;
        try {
            is = httpEntity.getContent();
            String content = getContent(is);
            final M m = JSON.parseObject(content, response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dataListener.onSuccess(m);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeQuietly(is);
        }
    }

    private String getContent(InputStream inputStream) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                dataListener.onFail();
                System.out.println("Error=" + e.toString());
            } finally {
                FileUtil.closeQuietly(inputStream);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            dataListener.onFail();
        }
        return content;
    }

    @Override
    public void onFail() {
        dataListener.onFail();
    }

    @Override
    public void addHttpHeader(Map<String, String> headerMap) {

    }
}
