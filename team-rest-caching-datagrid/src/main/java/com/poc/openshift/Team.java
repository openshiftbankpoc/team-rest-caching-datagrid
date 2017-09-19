package com.poc.openshift;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.poc.datagrid.rest.FootballManager;

import com.google.gson.Gson;

@Path("/team")
public class Team {

	@Inject
	FootballManager footballManager;

	@GET
	@Path("/get")
	@Produces({ "application/json" })
	public String getTeam() {
		List<String> teams = footballManager.getTeams();
		Gson gson = new Gson();
		String json = gson.toJson(teams);
		return json;
	}

	@POST
	@Path("/add")
	@Produces({ "application/json" })
	public String addTeam() {
		return "<xml><result>Hello</result></xml>";
	}
}
