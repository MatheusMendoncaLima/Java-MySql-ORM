package me.texels.mySqlOrm;

import me.texels.mySqlOrm.annotations.Column;
import me.texels.mySqlOrm.annotations.TableConfig;
import me.texels.mySqlOrm.column.ColumnHandler;
import me.texels.mySqlOrm.utils.Pair;

import java.lang.reflect.Field;
import java.util.*;

public class TableMetaData {
    protected Database _db;
    protected Class<?> _clazz;
    protected String _tableName;
    protected LinkedHashMap<String, Column> _columns;

    public TableMetaData(Database _db, Class<?> _clazz) {
        this._db = _db;
        this._clazz = _clazz;
    }
    public <T extends Table> void init(){
        if (_clazz.isAnnotationPresent(TableConfig.class) && !_clazz.getAnnotation(TableConfig.class).name().isEmpty()){
            TableConfig tableConfig = _clazz.getAnnotation(TableConfig.class);
            _tableName=tableConfig.name();
        }else{
            String[] splitClassName = _clazz.getName().split("\\.");

            _tableName= splitClassName[splitClassName.length-1];

        }
        LinkedHashMap<String,Column> columns = new LinkedHashMap<>();
        for (int i = 0; i < _clazz.getDeclaredFields().length; i++) {
            Field field = _clazz.getDeclaredFields()[i];
            if(field.isAnnotationPresent(Column.class)){
                Column column = field.getAnnotation(Column.class);
                columns.put(column.columnName().isEmpty()? field.getName() : column.columnName(), column);
            }
        }
        this._columns = columns;
        _columns.forEach(((name,column) -> {
            //System.out.println(_tableName+" - coluna - " + name);
        }));

    }

    public  boolean sync(boolean alter, boolean force){
        List<Map<String, Pair<Class<?>, Object>>> findTable = _db.execute("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = \""+_db.getDbName() + "\" AND TABLE_NAME = \"" +_tableName + "\"");
        if(force && !findTable.isEmpty()){
            _db.execute("DROP TABLE `"+ _tableName +"`;");
            findTable.clear();
        }

        if (findTable.isEmpty()){
            String statement = "CREATE TABLE `"+_tableName+"`(\n";

            List<String> statements = new ArrayList<>();
            for (int i = 0; i < _columns.size(); i++) {
                String columnName = (String) Arrays.asList(_columns.keySet().toArray()).get(i);
                Column column = _columns.get(columnName);

                statements.add(ColumnHandler.toStatement(Pair.of(columnName, column)));
            }
            statement += String.join(", \n", statements);
            statement+="\n);";
            _db.execute(statement);
            return true;
        }else{
            if(alter) {
                List<Map<String, Pair<Class<?>, Object>>> columns = _db.execute("SELECT COLUMN_NAME, COLUMN_KEY FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_SCHEMA = \"" + _db.getDbName() + "\" AND TABLE_NAME = \"" + _tableName + "\";");
                List<String> columnNames = new ArrayList<>();
                String primaryKeyName = "";
                for (Map<String, Pair<Class<?>, Object>> row : columns) {
                    if (row.get("COLUMN_KEY").second.toString().equals("PRI")) {
                        primaryKeyName= row.get("COLUMN_NAME").second.toString();
                        continue;
                    }
                    columnNames.add(row.get("COLUMN_NAME").second.toString());
                }
                for (int i = 0; i < _columns.size(); i++) {
                    String columnName = (String) Arrays.asList(_columns.keySet().toArray()).get(i);
                    Column column = _columns.get(columnName);
                    if (columnName.equals(primaryKeyName)) continue;
                    if (columnNames.contains(columnName)) {
                        _db.execute("ALTER TABLE `" + _tableName + "` MODIFY COLUMN " + ColumnHandler.toStatement(Pair.of(columnName, column)) + ";");
                    } else {
                        _db.execute("ALTER TABLE `" + _tableName + "` ADD COLUMN " + ColumnHandler.toStatement(Pair.of(columnName, column)) + ";");
                    }
                }
                return true;
            }
        }
        return false;
    }
}
