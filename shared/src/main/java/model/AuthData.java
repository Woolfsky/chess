package model;

public record AuthData(String authToken, String username) {
    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
