package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class Url extends Model {

    @Id
    private final long id;

    private final String name;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private final Date createAt;

    public Url(String db, long id, String name, Date createAt) {
        super(db);
        this.id = id;
        this.name = name;
        this.createAt = createAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreateAt() {
        return createAt;
    }

}
