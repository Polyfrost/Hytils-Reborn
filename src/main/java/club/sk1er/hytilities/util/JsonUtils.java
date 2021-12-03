package club.sk1er.hytilities.util;

import club.sk1er.hytilities.Hytilities;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class JsonUtils {
    public static final JsonParser PARSER = new JsonParser();
    public static final HttpClientBuilder builder = HttpClients.custom().setUserAgent(Hytilities.MOD_ID + "/" + Hytilities.VERSION)
        .addInterceptorFirst(((HttpRequest request, HttpContext context) -> {
            if (!request.containsHeader("Pragma")) request.addHeader("Pragma", "no-cache");
            if (!request.containsHeader("Cache-Control")) request.addHeader("Cache-Control", "no-cache");
        }));

    public static JsonElement read(String name, File directory) {
        try {
            if (name.endsWith(".json"))
                name = name.substring(0, name.indexOf(".json"));
            if (directory == null)
                directory = new File("./");
            if (!directory.exists())
                directory.mkdirs();
            File file = new File(directory, name + ".json");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            reader.lines().forEach(builder::append);
            return PARSER.parse(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean download(String url, File file) {
        url = url.replace(" ", "%20");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            HttpResponse downloadResponse = builder.build().execute(new HttpGet(url));
            byte[] buffer = new byte[1024];

            int read;
            while ((read = downloadResponse.getEntity().getContent().read(buffer)) > 0) {
                fileOut.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
