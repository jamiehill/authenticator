package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class User extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String username;

    @Constraints.Required
    @Formats.NonEmpty
    public String password;

    public String displayName;

    /**
     * Retrieve a user by username.
     */
    public static User findByUsername(String username)
    {
        return find.where().eq("username", username).findUnique();
    }

    /**
     * Queries a user
     */
    public static Model.Finder<Long, User> find =
            new Model.Finder<Long, User>(Long.class, User.class);

}
