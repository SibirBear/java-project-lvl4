package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public final class Url extends Model {

    @Id
    private final long id;

    private final String name;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private final Date createAt;

    public Url(String db, long pId, String pName, Date pCreateAt) {
        super(db);
        this.id = pId;
        this.name = pName;
        this.createAt = pCreateAt;
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
