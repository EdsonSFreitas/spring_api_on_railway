package freitas.io.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import freitas.io.domain.model.Account;
import freitas.io.domain.model.Card;
import freitas.io.domain.model.User;
import freitas.io.domain.repository.UserRepository;
import freitas.io.enums.RolesEnum;
import freitas.io.service.UserService;
import freitas.io.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#create(User)}
     */
    @Test
    void testCreate() throws Exception {
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
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link UserController#findAll()}
     */
    @Test
    void testFindAll() throws Exception {
        when(userService.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/search");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#findById(UUID)}
     */
    @Test
    void testFindById() {
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
        Optional<User> ofResult = Optional.of(user);
        UserRepository repository = mock(UserRepository.class);
        when(repository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UserController userController = new UserController(new UserServiceImpl(repository, new BCryptPasswordEncoder()));
        ResponseEntity<User> actualFindByIdResult = userController.findById(UUID.randomUUID());
        assertTrue(actualFindByIdResult.hasBody());
        assertTrue(actualFindByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFindByIdResult.getStatusCodeValue());
        verify(repository).findById(Mockito.<UUID>any());
    }

    /**
     * Method under test: {@link UserController#findByIdParam(UUID)}
     */
    @Test
    void testFindByIdParam() {
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
        Optional<User> ofResult = Optional.of(user);
        UserRepository repository = mock(UserRepository.class);
        when(repository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UserController userController = new UserController(new UserServiceImpl(repository, new BCryptPasswordEncoder()));
        ResponseEntity<User> actualFindByIdParamResult = userController.findByIdParam(UUID.randomUUID());
        assertTrue(actualFindByIdParamResult.hasBody());
        assertTrue(actualFindByIdParamResult.getHeaders().isEmpty());
        assertEquals(200, actualFindByIdParamResult.getStatusCodeValue());
        verify(repository).findById(Mockito.<UUID>any());
    }
}