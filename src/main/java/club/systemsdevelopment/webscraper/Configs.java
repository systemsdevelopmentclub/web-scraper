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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configs {

    private static final Properties PROPERTIES = new Properties();

    private static final PageConsumer PAGE_CONSUMER;

    static {
        PageConsumer pageConsumer = null;
        try {
            File propertiesFile = new File(System.getProperty("user.dir") + "\\web-scraper.properties");
            PROPERTIES.load(new FileInputStream(propertiesFile));
            boolean shutdown = check(Configs::getPeerId, "peer-id");
            shutdown |= check(Configs::getUserAgent, "user-agent");
            shutdown |= check(Configs::getPeerAddresses, "peer-addresses");
            shutdown |= check(Configs::getDatabaseUser, "database-user");
            shutdown |= check(Configs::getDatabaseUser, "database-password");
            // todo add checking for class file or jar and load that
            File file = new File(PROPERTIES.getProperty("page-consumer-policy-path", System.getProperty("user.dir") + "\\DefaultPageConsumerPolicy.class"));
            if (file.exists()) {
                try {
                    pageConsumer = (PageConsumer) new URLClassLoader(new URL[]{new URL("file", "localhost", file.getAbsolutePath())}).loadClass(file.getName()).newInstance();
                } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | ClassCastException e) {
                    System.err.println("Unable to load PageConsumer instance");
                    e.printStackTrace();
                    shutdown = true;
                }
            } else {
                System.err.println("No page-consumer-policy-path file");
                shutdown = true;
                System.exit(-1);
            }
            if (shutdown) System.exit(-1);
        } catch (IOException e) {
            System.err.println("IOException loading properties file");
            e.printStackTrace();
        }
        PAGE_CONSUMER = pageConsumer;
    }

    private static boolean check(Supplier<Object> supplier, String name) {
        if (supplier.get() != null) return false;
        System.err.println("No " + name + " provided in properties file.");
        return true;
    }

    /**
     * Gets the unique ID of this peer.
     *
     * @return the unique ID of this peer.
     */
    public static Integer getPeerId() {
        String values = PROPERTIES.getProperty("peer-id");
        if (values == null) return null;
        return Integer.parseInt(values);
    }

    /**
     * Gets the user agent of the bot.
     *
     * @return the user agent of the bot.
     */
    public static String getUserAgent() {
        return PROPERTIES.getProperty("user-agent");
    }

    /**
     * Gets a list of peer addresses from the properties file.
     *
     * @return a list of peer addresses.
     */
    public static List<String> getPeerAddresses() {
        String value = PROPERTIES.getProperty("peer-addresses");
        if (value == null) return null;
        return Stream.of(value.split(Pattern.quote(","))).map(String::trim).collect(Collectors.toList());
    }

    /**
     * Gets the user name for accessing the database.
     *
     * @return the user for the database.
     */
    public static String getDatabaseUser() {
        return PROPERTIES.getProperty("database-user");
    }

    /**
     * Gets the plane text password for accessing the database.
     *
     * @return the plane text password.
     */
    public static String getDatabasePassword() {
        return PROPERTIES.getProperty("database-password");
    }

    /**
     * Gets the max network usage in megabytes per second on average over time.
     *
     * @return the max network usage in megabytes per second.
     */
    public static Integer getMaxNetworkUsage() {// Mbps
        return Integer.parseInt(PROPERTIES.getProperty("max-network-usage", "10"));
    }

    /**
     * Gets the maximum page size for archiving a page.
     * Pages over this will be discarded and marked as oversized.
     *
     * @return the max page size for downloading in bytes per second.
     */
    public static Integer getMaxPageSize() {// Bytes
        return Integer.parseInt(PROPERTIES.getProperty("max-page-size", "20000"));
    }

    /**
     * Gets the path to the {@link PageConsumer} implementation.
     *
     * @return the path to the {@link PageConsumer} class file.
     */
    public static PageConsumer getPageConsumerPolicyPath() {
        return PAGE_CONSUMER;
    }
}
