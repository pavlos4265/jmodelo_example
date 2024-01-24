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
import jmodelo_example.UsersBL;
import jmodelo_example.models.UserVO;

public class UserController extends Controller{
	
	public ActionResult register() throws IOException, ScriptException {
		if (isLoggedIn())
			return redirect("/");
		
		return view("register.html");
	}
	
	public ActionResult login() throws IOException, ScriptException {
		if (isLoggedIn())
			return redirect("/");
		
		return view("login.html");
	}
	
	@HttpPost
	public ActionResult loginuser(PostData form) throws SQLException {
		String username = form.fields().get("username");
		if (username == null || username.trim().length() == 0)
			return html("Fill in your username");
			
		String password = form.fields().get("password");
		if (password == null || password.trim().length() == 0)
			return html("Fill in your password");
		
		UsersBL usersBL = new UsersBL(getDatabaseConnection());
		if (!usersBL.checkUserPassword(username, password))
			return html("Wrong username or password");
		
		UserVO user = usersBL.getUserByUsername(username);
		getSession().addValue("userid", user.id()+"");
		getSession().addValue("username", user.username());
		
		getExchange().getResponseHeaders().add("hx-redirect", "/");
		return empty();
	}
	
	@HttpPost
	public ActionResult logoutuser(){
		getSession().removeValue("userid");
		getSession().removeValue("username");
		
		getExchange().getResponseHeaders().add("hx-redirect", "/");
		return empty();
	}
	
	@HttpPost
	public ActionResult createuser(PostData form) throws SQLException {
		String username = form.fields().get("username");
		if (username == null || username.trim().length() == 0)
			return html("Fill in your username");
			
		String password = form.fields().get("password");
		if (password == null || password.trim().length() == 0)
			return html("Fill in your password");
		
		UsersBL usersBL = new UsersBL(getDatabaseConnection());
		if (usersBL.getUserByUsername(username) != null)
			return html("This username already exists");
		
		UserVO user = new UserVO(0, username, password);
		usersBL.insert(user);
		
		getExchange().getResponseHeaders().add("hx-retarget", "#registerform");
		return html("Your account has been created. You can now <a href=\"login\">login</a>"); 
	}
	
	private boolean isLoggedIn() {
		return getSession().getValue("userid") != null;
	}
}
