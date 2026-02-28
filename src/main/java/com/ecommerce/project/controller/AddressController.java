package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressServiceImpl;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private AddressServiceImpl addressService;

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Create address", description = "API to create a new address")
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.OK);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Get addresses", description = "API to get all addresses")
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addressList = addressService.getAddress();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Get address", description = "API to get a address by addressId")
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressesById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Get address", description = "API to get all addresses of logged user")
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressList = addressService.getAddressesByUser(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Update address", description = "API to update a address by addressId")
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,@Valid @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Delete address", description = "API to delete a address by passing addressId")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
