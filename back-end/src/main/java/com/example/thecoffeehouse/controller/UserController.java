package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.UserDto;
import com.example.thecoffeehouse.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto,
                                              @RequestParam("password") String password) {
        return new ResponseEntity<>(userService.createUser(userDto, password), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam("name") String name,
                                                  @RequestParam("phoneNumber") String phoneNumber,
                                                  @RequestParam("pageNo") int pageNo,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(userService.getUsers(name, phoneNumber, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("success");
    }
}
