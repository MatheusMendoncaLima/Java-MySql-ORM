package me.texels.mySqlOrm;

public class Where {
    private String statement = "where";

    public String getStatement() {
        return statement.equalsIgnoreCase("where")? "": statement;
    }

    public Where eq(String column ,Object value){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` = " + value ;
        statement+=")";
        return this;
    }
    public Where dif(String column ,Object value){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` != " + value ;
        statement+=")";
        return this;
    }

    public Where gt(String column ,Object value){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` > " + value ;
        statement+=")";
        return this;
    }
    public Where eGt(String column ,Object value){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` >= " + value ;
        statement+=")";
        return this;
    }
    public Where lt(String column ,Object value){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` < " + value ;
        statement+=")";
        return this;
    }
    public Where eLt(String column ,Object value){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` <= " + value ;
        statement+=")";
        return this;
    }

    public Where between(String column ,Object value, Object value2){
        if (!(statement.endsWith("where") || statement.endsWith("AND") || !statement.endsWith("OR"))) statement += " AND";
        statement+=" (";
        statement += "`"+column+"` BETWEEN " + value + " AND " + value2 ;
        statement+=")";
        return this;
    }
    public Where and(){
        statement += " AND";
        return this;
    }
    public Where or(){
        statement += " OR";
        return this;
    }

}
