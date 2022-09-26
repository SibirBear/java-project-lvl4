package hexlet.code.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import io.ebean.annotation.WhenCreated;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public final class Url extends Model {

    @Id
    private long id;
    private String name;

    @NotNull
    @WhenCreated
    private Instant createdAt;

    public Url(String pName) {
        this.name = pName;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
