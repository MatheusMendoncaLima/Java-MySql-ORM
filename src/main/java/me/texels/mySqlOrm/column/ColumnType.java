package me.texels.mySqlOrm.column;

public class ColumnType{
    private final ColumnTypes type;
    private final int size;
    private final String statement;
    private final int precision;
    private final int scale;
    private final String values;
    protected ColumnType(ColumnTypes type, int size, int scale, int precision, String values, String statement){
        this.type = type;
        this.size = size;
        this.statement=statement;
        this.precision = precision;
        this.scale = scale;
        this.values = values;
    }

    public String getStatement() {
        return statement.replace("%{size}%", ""+size).
                replace("%{precision}%", ""+precision)
                .replace("%{scale}%", ""+scale)
                .replace("%{values}%", values);
    }
}