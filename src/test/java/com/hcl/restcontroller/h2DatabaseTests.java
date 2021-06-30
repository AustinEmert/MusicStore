package com.hcl.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.controllers.rest.OrderRestController;
import com.hcl.controllers.rest.UserRestController;
import com.hcl.model.Authority;
import com.hcl.model.Item;
import com.hcl.model.Order;
import com.hcl.model.User;
import com.hcl.service.AuthService;
import com.hcl.service.ItemService;
import com.hcl.service.OrderService;
import com.hcl.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class h2DatabaseTests {
	ObjectMapper mapper;

    @Autowired
    OrderRestController orderRestController;
    
    @Autowired
    UserRestController userRestController;

    @Autowired
    UserService userService;
    
    @Autowired
    AuthService authService;

    @Autowired
    ItemService itemService;
    
    @Autowired
    OrderService orderService;

    private MockMvc mockMvc;
    private MockMvc mockMvc2;

    @BeforeEach
    public void init(){
    	mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController).build();
        mockMvc2 = MockMvcBuilders.standaloneSetup(userRestController).build();
    }

    @Test
    @WithMockUser(username = "test", password = "pass", roles = "USER")
    public void postOrderTest() throws  Exception{
        userService.insertUser(new User("test", "pass", true, "", "", "", "", "", "","", "",""));
        itemService.insertItem(new Item(1,9.99,"", "", "", "", 100L));
        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .param("quantity", "1")
                .param("itemId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "test", password = "pass", roles = "USER")
    public void postOrderAntiTest() throws  Exception{
        userService.insertUser(new User("test", "pass", true, "", "", "", "", "", "","", "",""));
        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .param("quantity", "1")
                .param("itemId", "1"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    @WithMockUser(username = "test", password = "pass", roles = "USER")
    public void putOrderTest() throws Exception {
    	Item item = new Item(1,9.99,"", "", "", "", 100L);
		User user = new User("test", "pass", true, "", "", "", "", "", "","", "","");
		itemService.insertItem(item);
		userService.insertUser(user);
    	orderService.insertOrders(new Order(1, 1, "Test", item, user));
        
        mockMvc.perform(post("/updateOrder") .contentType(MediaType.APPLICATION_JSON)
                .param("orderId", "1")
                .param("quantity", "1"))
                .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithMockUser(username = "test", password = "pass", roles = "USER")
    @Transactional
    public void setRoleTest() throws Exception {
    	User user = new User(1, "test", "firstNameTest", "lastNameTest");
    	Authority authority = new Authority("USER");
    	userService.insertUser(user);
    	authService.insertAuth(authority);
    	String body = new ObjectMapper().valueToTree(authority).toString();
    	
    	mockMvc2.perform(post("/user/1/authority").contentType(MediaType.APPLICATION_JSON).content(body))
		.andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "test", password = "pass", roles = "USER")
    public void deleteUserTest() throws Exception {
    	userService.insertUser(new User(1, "test", "firstNameTest", "lastNameTest"));
    	mockMvc2.perform(delete("/user/1")).andExpect(status().isOk());
    }
}