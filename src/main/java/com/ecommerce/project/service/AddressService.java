package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddress();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getAddressesByUser(User user);

    AddressDTO updateAddress(Long addressId, @Valid AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
