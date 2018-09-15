/*
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
package club.systemsdevelopment.webscraper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SiteReader {
    private static final DateFormat timeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public static byte[] readFile(String host, String file, long time) throws IOException {

        // use size limit in config
        byte[] buffer = new byte[Configs.getMaxPageSize()];
        HttpURLConnection connection = (HttpURLConnection) new URL("http", host, file).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", Configs.getUserAgent());
        connection.setRequestProperty("Accept", "text/html");
        connection.setRequestProperty("Accept-Language", "en-US");
        connection.setRequestProperty("Upgrade-Insecure_Requests", "1");
        connection.setRequestProperty("If-Modified_Since", timeFormat.format(new Date(time)));
        InputStream inputStream = connection.getInputStream();
        int read, total = 0, readSize = 0;
        while ((read = inputStream.read(buffer, total, buffer.length - total)) != -1) {
            total += read;
            if (total >= buffer.length) throw new RuntimeException();
        }

        //System.out.println(new String(byteBuffer.array(), 0, total, StandardCharsets.UTF_8));

        return buffer;
    }
}
