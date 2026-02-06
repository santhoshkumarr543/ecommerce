package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepository;
import com.ecommerce.project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address = modelMapper.map(addressDTO, Address.class);

        // User and Address has ManyToMany Relationship. so we need to set in both sides
        // User side
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        // Address Side
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddress() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOList = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressesByUser(User user) {
        List<Address> addresses = user.getAddresses();
        List<AddressDTO> addressDTOList = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
        return addressDTOList;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setPincode(addressDTO.getPincode());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress = addressRepository.save(existingAddress);

        // Map this address to the respective user aswell
        User user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address existingAddress = addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("Address" , "addressId", addressId));

        // Map this address to the respective user aswell
        User user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(existingAddress);
        return "Address deleted successfully with addressId : " + addressId;
    }
}
