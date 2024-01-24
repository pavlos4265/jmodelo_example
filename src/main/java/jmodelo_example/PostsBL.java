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
import java.util.ArrayList;
import java.util.List;

import jmodelo_example.models.PostVO;

public class PostsBL {

	private Connection connection;

	public PostsBL(Connection connection) {
		this.connection = connection;
	}

	public List<PostVO> getPosts() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT posts.id AS post_id, username, posts.user_id AS post_user_id, "
				+ "content, time_posted, COUNT(likes.post_id) AS likes_count "
				+ "FROM posts JOIN users ON users.id=posts.user_id LEFT JOIN likes ON likes.post_id=posts.id "
				+ "GROUP BY posts.id ORDER BY time_posted DESC");

		ResultSet results = statement.executeQuery();

		List<PostVO> posts = new ArrayList<>();
		while (results.next()) {
			PostVO post = new PostVO(results.getInt("post_id"), results.getInt("post_user_id"), results.getString("content"), 
					results.getLong("time_posted"));
			post.setUsername(results.getString("username"));
			post.setLikes(results.getInt("likes_count"));
			posts.add(post);
		}
		
		return posts;
	}
	
	public PostVO getPostById(int postId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT posts.id AS post_id, posts.user_id AS post_user_id, content, "
				+ "time_posted, COUNT(likes.post_id) AS likes_count "
				+ "FROM posts LEFT JOIN likes ON likes.post_id=posts.id "
				+ "WHERE posts.id=? GROUP BY posts.id");
		
		statement.setInt(1, postId);

		ResultSet results = statement.executeQuery();
		
		if (!results.next()) return null;
		
		PostVO post = new PostVO(results.getInt("post_id"), results.getInt("post_user_id"), results.getString("content"),
				results.getLong("time_posted"));
		post.setLikes(results.getInt("likes_count"));
		
		return post;
	}
	
	public boolean isLikedByUser(int userId, int postId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM likes WHERE user_id=? AND post_id=?");
		
		statement.setInt(1, userId);
		statement.setInt(2, postId);

		ResultSet results = statement.executeQuery();
		
		return results.next();
	}
	
	public void likePost(int userId, int postId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO likes(user_id, post_id) VALUES (?, ?)");
		
		statement.setInt(1, userId);
		statement.setInt(2, postId);

		statement.execute();		
	}
	
	public void unLikePost(int userId, int postId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM likes WHERE user_id=? AND post_id=?");
		
		statement.setInt(1, userId);
		statement.setInt(2, postId);

		statement.execute();		
	}
	
	public PostVO insert(PostVO post) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (user_id, content, time_posted) VALUES "
				+ "(?, ?, ?)");

		statement.setInt(1, post.getUserId());
		statement.setString(2, post.getContent());
		statement.setLong(3, post.getTimePosted());
		
		statement.execute();
		
		statement = connection.prepareStatement("SELECT last_insert_rowid()");
		ResultSet results = statement.executeQuery();
		post.setId(results.next() ? results.getInt(1) : 0);
		
		return post;
	}
	
	public void delete(int postId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE id=?");

		statement.setInt(1, postId);
		
		statement.execute();
	}

}
