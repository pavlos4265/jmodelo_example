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
import jmodelo_example.PostsBL;

public class DefaultController extends Controller{
	
	public ActionResult index() throws IOException, ScriptException, SQLException {
		if (getSession().getValue("userid") == null)
			return redirect("/user/login");
		
		PostsBL postsBL = new PostsBL(getDatabaseConnection());
		
		return view("index.html", postsBL.getPosts());
	}
}
