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
package jmodelo_example.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.script.ScriptException;

import jmodelo.ActionResult;
import jmodelo.Controller;
import jmodelo.PostData;
import jmodelo.annotations.HttpPost;
import jmodelo.annotations.UrlArg;
import jmodelo_example.PostsBL;
import jmodelo_example.models.PostVO;

public class PostController extends Controller{

	@HttpPost
	public ActionResult create(PostData form) throws SQLException, IOException, ScriptException {
		String content = form.fields().get("content");
		if (content == null || content.length() == 0)
			return empty();

		int userId = Integer.parseInt(getSession().getValue("userid"));
		PostVO post = new PostsBL(getDatabaseConnection()).insert(new PostVO(0, userId, content, System.currentTimeMillis()));

		post.setUsername(getSession().getValue("username"));

		getExchange().getResponseHeaders().add("hx-trigger", "postSubmitted");
		
		return partialView("_post.html", post);
	}

	public ActionResult delete(@UrlArg int postId) throws NumberFormatException, SQLException {
		PostsBL postsBL = new PostsBL(getDatabaseConnection());
		PostVO post = postsBL.getPostById(postId);
		if (post == null || post.getUserId() != Integer.parseInt(getSession().getValue("userid")))
			return empty();

		postsBL.delete(postId);

		getExchange().getResponseHeaders().add("hx-reswap", "outerHTML");
		return empty();
	}

	public ActionResult like(@UrlArg int postId) throws SQLException, IOException, ScriptException {
		PostsBL postsBL = new PostsBL(getDatabaseConnection());
		PostVO post = postsBL.getPostById(postId);
		if (post == null) return empty();

		int userId = Integer.parseInt(getSession().getValue("userid"));
		if (!postsBL.isLikedByUser(userId, postId)) {
			postsBL.likePost(userId, postId);
			post.setLikes(post.getLikes()+1);
		}else {
			postsBL.unLikePost(userId, postId);
			post.setLikes(post.getLikes()-1);
		}
		
		return partialView("_postmenu.html", post);
	}
}
