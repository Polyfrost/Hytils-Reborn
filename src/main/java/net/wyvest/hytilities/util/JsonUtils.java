/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.util;

import net.wyvest.hytilities.Hytilities;
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
