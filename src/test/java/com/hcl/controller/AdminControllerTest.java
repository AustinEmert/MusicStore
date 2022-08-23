package com.hcl.controller;

import com.hcl.model.Item;
import com.hcl.model.User;
import com.hcl.repository.ItemRepository;
import com.hcl.repository.OrderItemRepository;
import com.hcl.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void adminCredentialsTest() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void adminCredentialsAntiTest() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void allUsersTest() throws Exception {
        mockMvc.perform(get("/admin/allUsers")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void allOrdersTest() throws Exception {
        mockMvc.perform(get("/admin/allOrders")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void allItemsTest() throws Exception {
        mockMvc.perform(get("/admin/allItems")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void editItemsGetTest() throws Exception {
        mockMvc.perform(get("/admin/editItem/1")).andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void editItemsPostTest() throws Exception {
        Item item1 = new Item(9.99, "test1", "", "", "", 100L);
        itemRepository.save(item1);
        mockMvc.perform(post("/admin/editItem/" + item1.getId())
                .param("price", "9.99")
                .param("name", "new name")
                .param("thumbnail", "")
                .param("category", "")
                .param("description", ""))
                .andExpect(status().isOk());
        assertTrue(itemRepository.findAll().stream().anyMatch(item -> item.getName() == "new name"));
        assertFalse(itemRepository.findAll().stream().anyMatch(item -> item.getName() == "test1"));

    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void addItemGetTest() throws Exception {
        mockMvc.perform(get("/admin/addItem")).andExpect(status().isOk());
    }

//    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
//    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
//    public void addItemPostTest() throws Exception {
//        Item item1 = new Item(9.99, "test1", "", "", "", 100L);
//        mockMvc.perform(post("/admin/addItem")
//                .param("price", "9.99")
//                .param("name", "test1")
//                .param("thumbnail", "")
//                .param("category", "")
//                .param("description", ""))
//                .andExpect(status().isOk());
//        assertTrue(itemRepository.findAll().stream().anyMatch(item -> item.getName() == "test1"));
//    }

//    @Test
//    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
//    public void editUserGetTest() throws Exception {
//        mockMvc.perform(get("/admin/editUserById/1")).andExpect(status().isOk());
//    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void editUserPostTest() throws Exception {
        User user = new User("test", "pass", true,
                "", "", "", "", "",
                "","", "","");
        userRepository.save(user);
        mockMvc.perform(post("/admin/editUserById/1")
                .param("username", "new username")
                .param("pwd", "")
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "")
                .param("phoneNumber", "")
                .param("address", "")
                .param("apartmentNumber", "")
                .param("city", "")
                .param("state", "")
                .param("zip", ""))
                .andExpect(status().isOk());
        assertTrue(userRepository.findAll().stream().anyMatch(u -> u.getUsername() == "new username"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
    public void postAddAuthTest() throws Exception {
        userRepository.save(new User("test", "pass", true,
                "", "", "", "", "",
                "","", "",""));
        mockMvc.perform(post("/admin/addAuth/1")).andExpect(status().isOk());
    }
//    @Test
//    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
//    public void editOrderGetTest() throws Exception {
//        userRepository.save(new User(1L, "test", "pass", true,
//                "", "", "", "", "",
//                "","", "","", new HashSet<>(), new HashSet<>()));
//        itemRepository.save(new Item(1L, 9.99, "", "", "", ""));
//        orderItemRepository.save(new OrderItem(1L, 1, "",
//                itemRepository.getById(1L) ,userRepository.getById(1L)));
//        mockMvc.perform(get("/admin/editOrderById/1")).andExpect(status().isOk());
//    }
//
//    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
//    @WithMockUser(username = "user", password = "password", roles = "ADMIN")
//    public void editOrderPostTest() throws Exception {
//        userRepository.save(new User(1L, "test", "pass", true,
//                "", "", "", "", "",
//                "","", "","", new HashSet<>(), new HashSet<>()));
//        itemRepository.save(new Item(1L, 9.99, "", "", "", ""));
//        orderItemRepository.save(new OrderItem(1L, 1, "",
//                itemRepository.getById(1L) ,userRepository.getById(1L)));
//        mockMvc.perform(post("/admin/editOrderById/1")
//                .param("quantity", "5")
//                .param("status", ""))
//                .andExpect(status().isOk());
//        assertTrue(orderItemRepository.findAll().stream().anyMatch(order -> order.getQuantity() == 5));
//    }
}
