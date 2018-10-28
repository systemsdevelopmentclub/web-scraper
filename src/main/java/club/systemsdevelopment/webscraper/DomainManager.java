/*
 * @source: https://github.com/systemsdevelopmentclub/web-scraper
 * @license: https://www.gnu.org/licenses/lgpl.txt
 * @contact: https://www.stuorg.iastate.edu/site/sdc/information
 * Copyright (c) 2018 Systems Development Club
 * This file is part of SDC Search.
 * SDC Search is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * SDC Search is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with SDC Search.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.systemsdevelopment.webscraper;

/**
 * Manages getting pages for a single domain,
 * not including subdomains.
 */
public class DomainManager {
    private final String domain;
    public DomainManager(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }


    // todo add management methods
}
