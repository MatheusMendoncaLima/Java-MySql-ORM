package me.texels.mySqlOrm.column;

import me.texels.mySqlOrm.annotations.Column;
import me.texels.mySqlOrm.utils.Pair;

public class ColumnHandler {

    public static String toStatement(Pair<String, Column> column){
        ColumnType type = column.second.type().get(column.second.length(), column.second.precision(), column.second.scale(), column.second.values());

        String statement = "`"+column.first+"`" + " " + type.getStatement();
        if (column.second.unsigned()) statement += " UNSIGNED";
        if(column.second.primaryKey()) statement+= " PRIMARY KEY";
        if (!column.second.allowNull()) statement += " NOT NULL";
        if (!column.second.defaultValue().isEmpty()) statement += " DEFAULT "+column.second.defaultValue();
        if (!column.second.onUpdate().isEmpty()) statement += " ON UPDATE "+column.second.onUpdate();
        if (column.second.autoIncrement()) statement += " AUTO_INCREMENT";
        return statement;
    }

}
