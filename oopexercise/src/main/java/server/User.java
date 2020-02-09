package server;

/**
 * This class includes a Fluent Interface Builder implementation.
 */
public class User {

    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static class UserBuilder {

        private User user;

        public UserBuilder() {
            user = new User();
        }

        public UserBuilder hasUsername(String username) {
            user.username = username;
            return this;
        }

        public User build() {
            return user;
        }
    }
}
