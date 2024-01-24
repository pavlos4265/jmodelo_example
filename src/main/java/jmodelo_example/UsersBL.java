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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jmodelo_example.models.UserVO;

public class UsersBL {
	private Connection connection;

	public UsersBL(Connection connection) {
		this.connection = connection;
	}
	
	public UserVO getUserByUsername(String username) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT id, username FROM users WHERE username=?");
		statement.setString(1, username);
		
		ResultSet results = statement.executeQuery();

		return results.next() ? new UserVO(results.getInt("id"), results.getString("username"), null) : null;
	}
	
	public boolean checkUserPassword(String username, String password) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) FROM users WHERE username=? AND password=?");
		statement.setString(1, username);
		statement.setString(2, password);
		
		ResultSet results = statement.executeQuery();
		
		return results.next() && results.getInt(1) > 0; 
	}
	
	public UserVO insert(UserVO user) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");

		statement.setString(1, user.username());
		statement.setString(2, user.password());
		
		statement.execute();
		
		statement = connection.prepareStatement("SELECT last_insert_rowid()");
		ResultSet results = statement.executeQuery();
		
		return new UserVO(results.next() ? results.getInt(1) : 0, user.username(), user.password());
	}
}
