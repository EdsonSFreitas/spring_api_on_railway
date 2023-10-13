package freitas.io.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import freitas.io.enums.RolesEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {User.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserTest {
    @Autowired
    private User user;

    /**
     * Method under test: {@link User#getRole()}
     */
    @Test
    void testGetRole() {
        User user2 = new User();
        user2.setRole(RolesEnum.ROLE_USER);
        assertEquals(RolesEnum.ROLE_USER, user2.getRole());
    }

    /**
     * Method under test: {@link User#getRole()}
     */
    @Test
    void testGetRole2() {
        User user2 = new User();
        user2.setRole(RolesEnum.ROLE_ADMIN);
        assertEquals(RolesEnum.ROLE_ADMIN, user2.getRole());
    }

    /**
     * Method under test: {@link User#getRole()}
     */
    @Test
    void testGetRole3() {
        User user2 = new User();
        user2.setRole(RolesEnum.ROLE_SUPER);
        assertEquals(RolesEnum.ROLE_SUPER, user2.getRole());
    }

    /**
     * Method under test: {@link User#getRole()}
     */
    @Test
    void testGetRole4() {
        Account account = mock(Account.class);
        doNothing().when(account).setAgency(Mockito.<String>any());
        doNothing().when(account).setBalance(Mockito.<BigDecimal>any());
        doNothing().when(account).setId(Mockito.<Long>any());
        doNothing().when(account).setLimit(Mockito.<BigDecimal>any());
        doNothing().when(account).setNumber(Mockito.<String>any());
        account.setAgency("Agency");
        account.setBalance(new BigDecimal("2.3"));
        account.setId(1L);
        account.setLimit(new BigDecimal("2.3"));
        account.setNumber("42");
        User.UserBuilder accountResult = User.builder().account(account);

        Card card = new Card();
        card.setId(1L);
        card.setLimit(new BigDecimal("2.3"));
        card.setNumber("42");
        User.UserBuilder cardResult = accountResult.card(card);
        User.UserBuilder featuresResult = cardResult.features(new ArrayList<>());
        User.UserBuilder nameResult = featuresResult.id(UUID.randomUUID()).login("Login").name("Name");
        assertEquals(RolesEnum.ROLE_USER,
                nameResult.news(new ArrayList<>()).password("senhaQualquer").role(1).build().getRole());
        verify(account).setAgency(Mockito.<String>any());
        verify(account).setBalance(Mockito.<BigDecimal>any());
        verify(account).setId(Mockito.<Long>any());
        verify(account).setLimit(Mockito.<BigDecimal>any());
        verify(account).setNumber(Mockito.<String>any());
    }

    /**
     * Method under test: {@link User#setRole(RolesEnum)}
     */
    @Test
    void testSetRole() {
        User user2 = new User();
        user2.setRole(null);
        assertNull(user2.getAccount());
        assertNull(user2.getUsername());
        assertNull(user2.getPassword());
        assertNull(user2.getNews());
        assertNull(user2.getName());
        assertNull(user2.getId());
        assertNull(user2.getFeatures());
        assertNull(user2.getCard());
    }

    /**
     * Method under test: {@link User#setRole(RolesEnum)}
     */
    @Test
    void testSetRole2() {
        Account account = mock(Account.class);
        doNothing().when(account).setAgency(Mockito.<String>any());
        doNothing().when(account).setBalance(Mockito.<BigDecimal>any());
        doNothing().when(account).setId(Mockito.<Long>any());
        doNothing().when(account).setLimit(Mockito.<BigDecimal>any());
        doNothing().when(account).setNumber(Mockito.<String>any());
        account.setAgency("Agency");
        account.setBalance(new BigDecimal("2.3"));
        account.setId(1L);
        account.setLimit(new BigDecimal("2.3"));
        account.setNumber("42");
        User.UserBuilder accountResult = User.builder().account(account);

        Card card = new Card();
        card.setId(1L);
        card.setLimit(new BigDecimal("2.3"));
        card.setNumber("42");
        User.UserBuilder cardResult = accountResult.card(card);
        User.UserBuilder featuresResult = cardResult.features(new ArrayList<>());
        User.UserBuilder nameResult = featuresResult.id(UUID.randomUUID()).login("Login").name("Name");
        User buildResult = nameResult.news(new ArrayList<>()).password("senhaQualquer").role(1).build();
        buildResult.setRole(RolesEnum.ROLE_USER);
        verify(account).setAgency(Mockito.<String>any());
        verify(account).setBalance(Mockito.<BigDecimal>any());
        verify(account).setId(Mockito.<Long>any());
        verify(account).setLimit(Mockito.<BigDecimal>any());
        verify(account).setNumber(Mockito.<String>any());
        assertEquals(RolesEnum.ROLE_USER, buildResult.getRole());
    }

    /**
     * Method under test: {@link User#getAuthorities()}
     */
    @Test
    void testGetAuthorities() {
        User user2 = new User();
        user2.setRole(RolesEnum.ROLE_USER);
        Collection<? extends GrantedAuthority> actualAuthorities = user2.getAuthorities();
        assertEquals(1, actualAuthorities.size());
        assertEquals("ROLE_USER", ((List<? extends GrantedAuthority>) actualAuthorities).get(0).getAuthority());
    }

    /**
     * Method under test: {@link User#getAuthorities()}
     */
    @Test
    void testGetAuthorities2() {
        User user2 = new User();
        user2.setRole(RolesEnum.ROLE_ADMIN);
        Collection<? extends GrantedAuthority> actualAuthorities = user2.getAuthorities();
        assertEquals(1, actualAuthorities.size());
        assertEquals("ROLE_ADMIN", ((List<? extends GrantedAuthority>) actualAuthorities).get(0).getAuthority());
    }

    /**
     * Method under test: {@link User#getAuthorities()}
     */
    @Test
    void testGetAuthorities3() {
        Account account = mock(Account.class);
        doNothing().when(account).setAgency(Mockito.<String>any());
        doNothing().when(account).setBalance(Mockito.<BigDecimal>any());
        doNothing().when(account).setId(Mockito.<Long>any());
        doNothing().when(account).setLimit(Mockito.<BigDecimal>any());
        doNothing().when(account).setNumber(Mockito.<String>any());
        account.setAgency("Agency");
        account.setBalance(new BigDecimal("2.3"));
        account.setId(1L);
        account.setLimit(new BigDecimal("2.3"));
        account.setNumber("42");
        User.UserBuilder accountResult = User.builder().account(account);

        Card card = new Card();
        card.setId(1L);
        card.setLimit(new BigDecimal("2.3"));
        card.setNumber("42");
        User.UserBuilder cardResult = accountResult.card(card);
        User.UserBuilder featuresResult = cardResult.features(new ArrayList<>());
        User.UserBuilder nameResult = featuresResult.id(UUID.randomUUID()).login("Login").name("Name");
        Collection<? extends GrantedAuthority> actualAuthorities = nameResult.news(new ArrayList<>())
                .password("senhaQualquer")
                .role(1)
                .build()
                .getAuthorities();
        assertEquals(1, actualAuthorities.size());
        assertEquals("ROLE_USER", ((List<? extends GrantedAuthority>) actualAuthorities).get(0).getAuthority());
        verify(account).setAgency(Mockito.<String>any());
        verify(account).setBalance(Mockito.<BigDecimal>any());
        verify(account).setId(Mockito.<Long>any());
        verify(account).setLimit(Mockito.<BigDecimal>any());
        verify(account).setNumber(Mockito.<String>any());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link User#getPassword()}
     *   <li>{@link User#getUsername()}
     *   <li>{@link User#isAccountNonExpired()}
     *   <li>{@link User#isAccountNonLocked()}
     *   <li>{@link User#isCredentialsNonExpired()}
     *   <li>{@link User#isEnabled()}
     * </ul>
     */
    @Test
    void testGetPassword() {
        Account account = new Account();
        account.setAgency("Agency");
        account.setBalance(new BigDecimal("2.3"));
        account.setId(1L);
        account.setLimit(new BigDecimal("2.3"));
        account.setNumber("42");

        Card card = new Card();
        card.setId(1L);
        card.setLimit(new BigDecimal("2.3"));
        card.setNumber("42");

        User user = new User();
        user.setAccount(account);
        user.setCard(card);
        user.setFeatures(new ArrayList<>());
        user.setId(UUID.randomUUID());
        user.setLogin("Login");
        user.setName("Name");
        user.setNews(new ArrayList<>());
        user.setPassword("senhaQualquer");
        user.setRole(RolesEnum.ROLE_USER);
        String actualPassword = user.getPassword();
        String actualUsername = user.getUsername();
        boolean actualIsAccountNonExpiredResult = user.isAccountNonExpired();
        boolean actualIsAccountNonLockedResult = user.isAccountNonLocked();
        boolean actualIsCredentialsNonExpiredResult = user.isCredentialsNonExpired();
        assertEquals("senhaQualquer", actualPassword);
        assertEquals("Login", actualUsername);
        assertTrue(actualIsAccountNonExpiredResult);
        assertTrue(actualIsAccountNonLockedResult);
        assertTrue(actualIsCredentialsNonExpiredResult);
        assertTrue(user.isEnabled());
    }
}