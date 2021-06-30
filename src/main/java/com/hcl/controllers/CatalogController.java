package com.hcl.controllers;

import com.hcl.service.ItemService;
import com.hcl.service.OrderItemService;
import com.hcl.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CatalogController {

    @Autowired
    ItemService itemService;

    @Autowired
    OrderItemService orderItemService;
    
    @Autowired
    UserService userService;

    @GetMapping("/catalog")
    public String displayCatalog(Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); // get logged in username
        model.addAttribute("username", name);
        return "catalog";
    }
    
    @GetMapping("catalog/{id}")
    public String displayItem(@PathVariable long id, Model model) {

        model.addAttribute("item", itemService.getItemById(id));
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); // get logged in username
        model.addAttribute("username", name);
        
    	return "item";
    }
    
   
    
    

}
