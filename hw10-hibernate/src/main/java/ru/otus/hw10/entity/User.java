package ru.otus.hw10.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "user", targetEntity = Phone.class, cascade = CascadeType.ALL)
    private List<Phone> phones = new ArrayList<>();

    public User() {
    }

    public User(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
