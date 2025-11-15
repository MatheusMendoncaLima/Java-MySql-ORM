package me.texels.mySqlOrm.column;

public enum ColumnTypes {
    // Numerics
    INTEGER("INTEGER"),
    SMALLINT("SMALLINT"),
    BIGINT("BIGINT"),
    REAL("REAL"),
    DOUBLE("DOUBLE PRECISION"),
    FLOAT("FLOAT"),
    DECIMAL(10, 2, "DECIMAL(%{precision}%, %{scale}%)"),
    NUMERIC(10, 2, "NUMERIC(%{precision}%, %{scale}%)"),

    // Text
    CHAR(1, "CHAR(%{size}%)"),
    STRING(255, "VARCHAR(%{size}%)"),
    TEXT("TEXT"),
    CLOB("CLOB"),

    // Binaries
    BLOB("BLOB"),
    BINARY(1, "BINARY(%{size}%)"),
    VARBINARY(255, "VARBINARY(%{size}%)"),

    // Date Time
    DATE("DATE"),
    TIME("TIME"),
    TIMESTAMP("TIMESTAMP"),
    DATETIME("DATETIME"),

    // bool
    BOOLEAN("BOOLEAN"),

    // others
    JSON("JSON"),
    UUID("UUID"),
    ENUM("'A','B','C'", "ENUM(%{values}%)");


    private final int defaultSize;
    private final String statement;
    private final int defaultPrecision;
    private final int defaultScale;
    private final String defaultValues;
    ColumnTypes(int size, String statement){
        this.statement=statement;
        this.defaultSize = size;
        this.defaultPrecision = -1;
        this.defaultScale = -1;
        this.defaultValues = "";
    }
    ColumnTypes(String statement){
        this.statement=statement;
        this.defaultSize=-1;
        this.defaultPrecision = -1;
        this.defaultScale = -1;
        this.defaultValues = "";
    }
    ColumnTypes(int precision, int scale, String statement){
        this.statement=statement;
        this.defaultSize=-1;
        this.defaultPrecision = scale;
        this.defaultScale = precision;
        this.defaultValues = "";
    }
    ColumnTypes(String values, String statement){
        this.statement=statement;
        this.defaultSize=-1;
        this.defaultPrecision = -1;
        this.defaultScale = -1;
        this.defaultValues = values;
    }
    public ColumnType get(int size, int precision, int scale,String values){
        size = (size==-1)? defaultSize : size;
        precision = (precision==-1)? defaultPrecision : precision;
        scale = (scale==-1)? defaultScale : scale;
        values = (values.isEmpty())? defaultValues : values;
        return new ColumnType(this, size, scale, precision, values, statement);
    }

}
