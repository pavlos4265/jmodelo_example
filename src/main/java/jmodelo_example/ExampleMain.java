/*   
 * Copyright 2024 pavlos4265
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jmodelo_example;

import java.io.IOException;

import com.zaxxer.hikari.HikariConfig;

import jmodelo.MVC;

public class ExampleMain {

	public static void main(String[] args) {
		
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:sqlite:social.db");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		MVC mvc = new MVC("jmodelo_example.controllers", true, null, config);
		
		try {
			mvc.startHttp(80);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
