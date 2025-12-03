package me.texels.mySqlOrm;

import me.texels.mySqlOrm.annotations.Column;
import me.texels.mySqlOrm.column.ColumnHandler;
import me.texels.mySqlOrm.column.ColumnTypes;
import me.texels.mySqlOrm.annotations.TableConfig;
import me.texels.mySqlOrm.utils.Pair;

import java.lang.reflect.Field;
import java.util.*;

public  abstract class Table {
    private final TableMetaData meta;
    protected Class<?> _clazz;
    protected Database _db;
    protected String _tableName;
    protected LinkedHashMap<String, Column> _columns;

    public Table(){
        this.meta = TableRegistry.get(this.getClass());
        this._clazz = this.meta._clazz;
        this._db = this.meta._db;
        this._tableName = this.meta._tableName;
        this._columns = this.meta._columns;

        if (this.getClass() == _clazz){
            _columns.forEach((columnName, column) ->{
                try {
                    if (column.type() == ColumnTypes.INTEGER && column.primaryKey() && column.autoIncrement()){
                        _clazz.getField(columnName).set(this, count()+1);
                    }
                }catch (NoSuchFieldException | IllegalAccessException e){
                    System.out.println("something went wrong");

                }
            });
        }
    }

    public  void update(){

    }
    public void destroy(Where where){
        _db.execute("DELETE FROM `"+_tableName+"` " +where.getStatement() + ";");
    }
    public  <T extends Table> T create(){
        //_db.execute("INSERT INTO `"+_tableName+"` ("+_columns+") VALUES ()");
        return null;
    }
    public  int count(){
        List<Map<String, Pair<Class<?>, Object>>> result =_db.execute("SELECT COUNT(*) FROM `"+_tableName+"`");
        if (result.isEmpty()) return 0;
        try {
            return Math.toIntExact((long)(result.get(0).get("COUNT(*)").first.cast(result.get(0).get("COUNT(*)").second)));
        }catch (Exception e){
            System.err.println(e);
            return 0;
        }
    }
    public boolean save(){
        HashMap<String, Object> clazzAttributes = new HashMap<>();
        Pair<String, Object> primaryKey = null;

        for (Field attribute : _clazz.getDeclaredFields()){
            if (attribute.isAnnotationPresent(Column.class)){
                try {
                    if (attribute.getAnnotation(Column.class).primaryKey()){
                        primaryKey = Pair.of(attribute.getName(), attribute.get(this));
                        continue;
                    }
                    clazzAttributes.put("`"+attribute.getName()+"`", attribute.get(this));
                }
                catch (IllegalAccessException e){
                    throw new RuntimeException("COLUMN NOT SET AS PUBLIC");
                }
            }
        }
        clazzAttributes.forEach((nome, val)->{
        });
        if (primaryKey == null) return false;

        final List<String> valuesList = new ArrayList<>();
        clazzAttributes.forEach((s,v) ->{
            if (v == null) v = "NULL";
            valuesList.add("\""+v.toString()+"\"");
        });
        if (_db.execute("SELECT * FROM `" + _tableName + "` WHERE `" + primaryKey.first + "` = " + primaryKey.second.toString()).isEmpty()){
            _db.execute("INSERT INTO `"+_tableName+"` ("+String.join(",", clazzAttributes.keySet())+") VALUES ("+String.join(",", valuesList)+")");
            for (Field field : _clazz.getDeclaredFields()){
                if (field.getName().equals(primaryKey.first)){
                    List<Map<String, Pair<Class<?>, Object>>> allResults = _db.execute("SELECT * FROM `" + _tableName + "`");
                    Object lastPKValue = allResults.get(allResults.size()-1).get(primaryKey.first).second;
                    try {
                        field.set(this, field.getType().cast(lastPKValue));
                    } catch (IllegalAccessException e) {
                        //
                    }catch (ClassCastException e) {
                        try {
                            field.set(this, Math.toIntExact((long) lastPKValue));
                        }catch (Exception err){
                            System.err.println(e);
                            return false;
                        }
                    }
                }
            }

        }else{
            String statement = "";
            for (int i = 0; i < valuesList.size(); i++) {
                String key = (String) clazzAttributes.keySet().toArray()[i];
                String value = valuesList.get(i);
                statement += key + " = " + value;
                if (i != valuesList.size()-1) statement+=",";
            }

            _db.execute("UPDATE `"+_tableName+"` SET " + statement + " WHERE `" + primaryKey.first + "` = "+primaryKey.second);
        }
        return true;
    }
    public <T extends Table> T rowToObject(Map<String, Pair<Class<?>, Object>> row){
        try {
            T instance =  (T) _clazz.getDeclaredConstructor().newInstance();
            for (String columnNames : row.keySet()) {
                if (_clazz.getField(columnNames).getType() == int.class && row.get(columnNames).first == Long.class) {
                    _clazz.getField(columnNames).set(instance, Math.toIntExact ((long)row.get(columnNames).first.cast(row.get(columnNames).second)));

                } else {
                    _clazz.getField(columnNames).set(instance, row.get(columnNames).first.cast(row.get(columnNames).second));
                }
            }
            return instance;
        }catch (Exception e){
            System.err.println(e);
            return null;
        }

    }
    public  <T extends Table> List<T> find(Where where){
        List<Map<String, Pair<Class<?>, Object>>> allColumns = _db.execute("SELECT * FROM `"+_tableName+"` " + where.getStatement()+";");
        List<T> instances = new ArrayList<>();
        for (Map<String, Pair<Class<?>, Object>> row : allColumns){
            T instance = rowToObject(row);
            if (instance != null) instances.add(instance);
        }
        return instances;
    }
    public  <T extends Table> T findByPk(){
        Pair<String, Column> primaryKey = null;
        for (String columnName : _columns.keySet()){
            Column column = _columns.get(columnName);
            if (column.primaryKey()) primaryKey = Pair.of(columnName, column);
        }
        if (primaryKey == null) return null;
        Map<String, Pair<Class<?>, Object>> result = _db.execute("SELECT * FROM WHERE "+primaryKey.first+" = " + primaryKey.second).get(0);
        return rowToObject(result);
    }



}
