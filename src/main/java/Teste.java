import com.sun.tools.javac.Main;
import me.texels.mySqlOrm.Database;
import me.texels.mySqlOrm.Table;
import me.texels.mySqlOrm.Where;
import me.texels.mySqlOrm.annotations.Column;
import me.texels.mySqlOrm.column.ColumnTypes;

public class Teste {
    public static void main(String[] args) {
        Database db = new Database("projetoDs", new Database.Ip("localhost", 3306), "root", "");
        db.connect();
        User.init(User.class, db);
        User.sync();
        User user = new User();
        user.name = "bundamole";
        user.save();
        System.out.println(User.find(new Where()));

    }
    public static class User extends Table{
        @Column(type = ColumnTypes.INTEGER, unsigned = true, autoIncrement = true, primaryKey = true)
        public int id;
        @Column(type = ColumnTypes.STRING)
        public String name;
    }
}
