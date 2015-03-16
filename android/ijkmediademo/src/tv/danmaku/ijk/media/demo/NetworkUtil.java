package tv.danmaku.ijk.media.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;


public class NetworkUtil {
    public static final String kTag = "NetworkUtil";
    public static final int kDefaultConnectionTimeout = 2000;
    public static final int kDefaultReadingTimeout = 5000;

    /**
     * This method will be executed in a separated thread, and the calling thread will be blocked until this method
     * finished or timeout.
     */
    public static String httpGet(final String url) throws IOException, InterruptedException,
            ExecutionException {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final Callable<String> task = new Callable<String>() {

            @Override
            public String call() throws Exception {
                final HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, kDefaultConnectionTimeout);
                HttpConnectionParams.setSoTimeout(httpParameters, kDefaultReadingTimeout);
                final HttpClient client = new DefaultHttpClient(httpParameters);
                final HttpGet get = new HttpGet(url);
                final HttpResponse response = client.execute(get);
                String result = null;
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    final InputStream is = response.getEntity().getContent();
                    result = streamToString(is);
                }
                return result;
            }
        };
        final Future<String> result = exec.submit(task);
        exec.shutdown();
        return result.get();
    }

    public static String httpsGet(final String url) throws IOException, InterruptedException,
            ExecutionException {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final Callable<String> task = new Callable<String>() {

            @Override
            public String call() throws Exception {
                final URL uurl = new URL(url);
                final HttpsURLConnection urlConnection = (HttpsURLConnection) uurl.openConnection();
                final InputStream is = urlConnection.getInputStream();
                String result = streamToString(is);
                return result;
            }
        };
        final Future<String> result = exec.submit(task);
        exec.shutdown();
        return result.get();
    }

    public static String httpPost(final String url, final List<NameValuePair> nameValuePair) throws ExecutionException, InterruptedException {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                final HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, kDefaultConnectionTimeout);
                HttpConnectionParams.setSoTimeout(httpParameters, kDefaultReadingTimeout);
                final HttpClient client = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(url);
                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.e("error","http post fail:Unsupported Encoding Exception");
                }
                final HttpResponse response = client.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        };
        final Future<String> result = exec.submit(task);
        exec.shutdown();
        return result.get();

    }

    public static String streamToString(final InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return new String(outputStream.toByteArray());
    }

    public final static Bitmap getImageFromUrl(final String paramURL) throws InterruptedException, ExecutionException {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final Callable<Bitmap> task = new Callable<Bitmap>() {

            @Override
            public Bitmap call() throws Exception {
                {
                    final URL uurl = new URL(paramURL);
                    final HttpURLConnection urlConnection = (HttpURLConnection) uurl.openConnection();

                    final InputStream is = urlConnection.getInputStream();
                    final BufferedInputStream mBufferedInputStream = new BufferedInputStream(is);
                    final Bitmap mBitmap = BitmapFactory.decodeStream(mBufferedInputStream);
                    mBufferedInputStream.close();
                    return mBitmap;
                }
            }
        };
        final Future<Bitmap> result = exec.submit(task);
        exec.shutdown();
        return result.get();
    }
}
