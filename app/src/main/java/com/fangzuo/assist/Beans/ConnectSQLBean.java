package com.fangzuo.assist.Beans;

import java.util.ArrayList;

/**
 * Created by NB on 2017/7/24.
 */

public class ConnectSQLBean {


    public String ip;
    public String port;
    public String username;
    public String password;
    public String database;
    public String Version;
    public ArrayList<Integer> choose;

    public ConnectSQLBean(String ip, String port, String username, String password, String database, String version, ArrayList<Integer> choose) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        Version = version;
        this.choose = choose;
    }

    public ConnectSQLBean(String ip, String port, String username, String password, String database) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }
    public ConnectSQLBean(String ip, String port, String username, String password, String database,String Version) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.Version = Version;
    }
}
