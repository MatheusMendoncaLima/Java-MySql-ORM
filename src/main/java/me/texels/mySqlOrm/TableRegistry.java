package me.texels.mySqlOrm;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class TableRegistry {
    private  static HashMap<Class<?>, TableMetaData> tables = new HashMap<>();
    public static void initTables(Database db, boolean alter, boolean force){
        tables.forEach((clazz, meta)-> {
            meta.init();
            meta.sync(alter, force);
        });
    }
    public static <T extends Table> void addTable(Class<T> tableClass, Database db){
        try {
            tables.put(tableClass, new TableMetaData(db, tableClass));
        } catch (Exception e) {
        }
    }

    protected static <T extends Table> TableMetaData get(Class<T> tableClass){
        return tables.getOrDefault((Class<?>) tableClass, null);
    }


}
