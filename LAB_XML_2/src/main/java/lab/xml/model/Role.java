package lab.xml.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    LIBRARIAN,
    READER;

    @Override
    public String getAuthority() {
        return name();
    }
}