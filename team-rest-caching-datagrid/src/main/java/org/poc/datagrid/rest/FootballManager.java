/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.poc.datagrid.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class FootballManager {

    private static final String JDG_HOST = "dg65-openshift-ccbt-poc.apps.192.168.3.84.nip.io";
    // REST specific properties
    public static final String HTTP_PORT = "80";
    public static final String REST_CONTEXT_PATH = "rest";

   
    private static final String teamsKey = "bankcache";

 
    private RESTCache<String, Object> cache;

    
    public FootballManager() {
       
        String contextPath = "rest";//jdgProperty(REST_CONTEXT_PATH);
        if (contextPath.length() > 0 && !contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        cache = new RESTCache<String, Object>("bankcache", "http://" + JDG_HOST + ":" + HTTP_PORT
                + contextPath + "/");
        List<String> teams = (List<String>) cache.get(teamsKey);
        if (teams == null) {
            teams = new ArrayList<String>();
            Team t = new Team("Barcelona");
            t.addPlayer("Messi");
            t.addPlayer("Pedro");
            t.addPlayer("Puyol");
            cache.put(t.getName(), t);
            teams.add(t.getName());
        }
        cache.put(teamsKey, teams);
    }

    public void addTeam(String teamName) {
       
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(encode(teamsKey));
        if (teams == null) {
            teams = new ArrayList<String>();
        }
        Team t = new Team(teamName);
        cache.put(encode(teamName), t);
        teams.add(teamName);
        // maintain a list of teams under common key
        cache.put(teamsKey, teams);
    }

    public void addPlayers(String teamName,List<String> players) {
       
        
        Team t = (Team) cache.get(encode(teamName));
        if (t != null) {            
            	for(String playerName:players)
                t.addPlayer(playerName);            
            cache.put(encode(teamName), t);
        } else {            
            System.out.println("Team not found...!");
        }
    }

    public void removePlayer(String playerName,String teamName) {        
        
        Team t = (Team) cache.get(encode(teamName));
        if (t != null) {
            t.removePlayer(playerName);
            cache.put(encode(teamName), t);
        } else {            
            System.out.println("Team not found...!");
        }
    }

    public void removeTeam(String teamName) {
      
        Team t = (Team) cache.get(encode(teamName));
        if (t != null) {
            cache.remove(encode(teamName));
            @SuppressWarnings("unchecked")
            List<String> teams = (List<String>) cache.get(teamsKey);
            if (teams != null) {
                teams.remove(teamName);
            }
            cache.put(teamsKey, teams);
        } else {
        		System.out.println("Team not found!");
        }
    }

    public List<String> getTeams() {
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(teamsKey);
        /*if (teams != null) {
            for (String teamName : teams) {
                con.printf(cache.get(encode(teamName)).toString());
            }
        }*/
        
        return teams;
    }

    public static void main(String[] args) {
        
        //FootballManager manager = new FootballManager();      

    }

    public static String jdgProperty(String name) {
        Properties props = new Properties();
    /*    try {
           // props.load(FootballManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }*/
        return props.getProperty(name);
    }

    public static String encode(String key) {
        try {
            return URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
