package wastewise.listing.authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthManagerTests {

    private transient AuthManager authManager;

    @BeforeEach
    public void setup() {
        authManager = new AuthManager();
    }

    @Test
    public void testGetNetId_ReturnsUserName() {
        String expectedUserName = "testUser";

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(expectedUserName);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        String netId = authManager.getNetId();

        assertEquals(expectedUserName, netId);
    }

    @Test
    public void testGetNetId_NoAuthentication_ReturnsNull() {
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(NullPointerException.class, () -> {
            authManager.getNetId(); // Call the method that is expected to throw the exception
        });
    }

    @Test
    public void testGetNetId_UserNameIsEmpty() {
        String expectedUserName = "";

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(expectedUserName);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String netId = authManager.getNetId();

        assertEquals(expectedUserName, netId);
    }
}
