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
/**
 *  A utility class for reading the raw web pages.
 */
public class SiteReader {
    //private static final DateFormat timeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    /**
     * Read a raw web pages.
     *
     * @param host the host name of the server to get the file
     * @param file the file to get the server form.
     * @return the bytes of the file.
     * @throws IOException if a general I/O error happens.
     */
    public static byte[] readFile(String host, String file/*, long time*/) throws IOException {
        byte[] buffer = new byte[Configs.getMaxPageSize()];// Initialize the buffer of largest size
        HttpURLConnection connection = (HttpURLConnection) new URL("http", host, file).openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", Configs.getUserAgent());
        connection.setRequestProperty("Accept", "text/html");
        connection.setRequestProperty("Accept-Language", "en-US");
        connection.setRequestProperty("Upgrade-Insecure_Requests", "1");
        //connection.setRequestProperty("If-Modified_Since", timeFormat.format(new Date(time)));
        InputStream inputStream = connection.getInputStream();

        int read, total = 0;
        while ((read = inputStream.read(buffer, total, buffer.length - total)) != -1) {
            total += read;
            if (total >= buffer.length) throw new RuntimeException();
        }
        return buffer;
    }
}
