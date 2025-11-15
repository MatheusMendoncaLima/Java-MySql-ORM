package me.texels.mySqlOrm;

import me.texels.mySqlOrm.utils.Pair;

import java.sql.*;
import java.util.*;

public class Database {
    private final String dbName;
    private final Ip host;
    private final String user;
    private final String password;
    private Connection conn;
    public Database(String dbName, Ip host, String user, String password) {
        this.dbName = dbName;
        this.host = host;
        this.user = user;
        this.password = password;
    }
    public List<Map<String, Pair<Class<?>,Object>>> execute(String statement){
        ResultSet result = null;
        try {
            result = conn.prepareStatement(statement).executeQuery();
            if (!result.next()) result = null;
        }catch (SQLException e){
            if(e.getErrorCode() == 0) {
                try {
                    conn.prepareStatement(statement).execute();
                } catch (Exception err) {
                    System.err.println(err);
                }
            }else {
                System.err.println(e);

            }
        }
        if (result == null) return List.of();
        List<Map<String, Pair<Class<?>, Object>>> returnValue = new ArrayList<>();
            while (true) {
                try {
                    Map<String, Pair<Class<?>, Object>> row = new HashMap<>();
                    ResultSetMetaData metaData = result.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i);
                        row.put(columnName, Pair.of(Class.forName(metaData.getColumnClassName(i)), result.getObject(columnName)));
                    }
                    returnValue.add(row);
                    if(!result.next()) break;
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

        return returnValue;

    }

    public boolean connect() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, this.user, this.password);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public boolean disconnect(){
        try {
            this.conn.close();
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    public static class Ip{
        public String host;
        public int port;
        public Ip(String host, int port){
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            return this.host+":"+port;
        }

    }

    public Connection getConn() {
        return conn;
    }

    public String getDbName() {
        return dbName;
    }
}

