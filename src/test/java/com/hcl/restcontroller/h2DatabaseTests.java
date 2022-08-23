package com.hcl.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.controllers.AdminController;
import com.hcl.controllers.rest.OrderItemRestController;
import com.hcl.controllers.rest.UserRestController;
import com.hcl.model.Authority;
import com.hcl.model.Item;
import com.hcl.model.Order;
import com.hcl.model.OrderItem;
import com.hcl.model.User;
import com.hcl.repository.ItemRepository;
import com.hcl.service.AuthService;
import com.hcl.service.ItemService;
import com.hcl.service.OrderItemService;
import com.hcl.service.OrderService;
import com.hcl.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class h2DatabaseTests {
	ObjectMapper mapper;

    @Autowired
    OrderItemRestController orderItemRestController;
    
    @Autowired
    UserRestController userRestController;

    @Autowired
    AdminController adminController;
    
    @Autowired
    UserService userService;
    
    @Autowired
    AuthService authService;

    @Autowired
    ItemService itemService;
    
    @Autowired
    OrderItemService orderItemService;
    
    @Autowired
	OrderService orderService;
    
    @Autowired
    ItemRepository itemRepository;

    private MockMvc mockMvc;
    private MockMvc mockMvc2;
    private MockMvc mockMvc3;

    @BeforeEach
    public void init(){
    	mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemRestController).build();
        mockMvc2 = MockMvcBuilders.standaloneSetup(userRestController).build();
        mockMvc3 = MockMvcBuilders.standaloneSetup(adminController).build();
    }
//Need to add cascade stuff to annotations in model... Then something wrong with sql injection
//    @Test
//    @WithMockUser(username = "test", password = "pass", roles = "USER")
//    public void createOrderItemTest() throws  Exception{
//        userService.insertUser(new User("test", "pass", true, "", "", "", "", "", "","", "",""));
//        itemService.insertItem(new Item(1,9.99,"", "", "", "", 100L));
//        orderService.insertOrder(new Order("Test", (new User("test", "pass", true, "", "", "", "", "", "","", "",""))));
//        mockMvc.perform(post("/orderItem")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("quantity", "1")
//                .param("itemId", "1"))
//                .andExpect(status().is3xxRedirection());
//    }

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
    public void updateOrderItemTest() throws Exception {
    	Item item = new Item(1,9.99,"", "", "", "", 100L);
		User user = new User("test", "pass", true, "", "", "", "", "", "","", "","");
		itemService.insertItem(item);
		userService.insertUser(user);
    	orderItemService.insertOrderItem(new OrderItem());

        mockMvc.perform(post("/updateOrderItem") .contentType(MediaType.APPLICATION_JSON)
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
    
//    @Test
//    @WithMockUser(username = "test", password = "pass", roles = "USER")
//    public void editItemsPostTest() throws Exception {
//    	Item item1 = new Item(9.99, "test1", "", "", "", 100L);
//    	item1.setId(1);
//        itemRepository.save(item1);
//        mockMvc3.perform(post("/editItem/1")
//                .param("price", "9.99")
//                .param("name", "new name")
//                .param("thumbnail", "")
//                .param("category", "")
//                .param("description", ""))
//                .andExpect(status().isOk());
//        assertTrue(itemRepository.findAll().stream().anyMatch(item -> item.getName() == "new name"));
//        assertFalse(itemRepository.findAll().stream().anyMatch(item -> item.getName() == "test1"));
//    }
    
}
