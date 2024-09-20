package com.example.manageeducation.userservice.customer.service;

import com.example.manageeducation.userservice.Utils.CustomerServiceUtils;
import com.example.manageeducation.userservice.dto.CustomerImportRequest;
import com.example.manageeducation.userservice.dto.CustomerResponse;
import com.example.manageeducation.userservice.dto.CustomerUpdateRequest;
import com.example.manageeducation.userservice.dto.RequestForListOfCustomer;
import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.jdbc.CustomerJdbc;
import com.example.manageeducation.userservice.model.*;
import com.example.manageeducation.userservice.repository.AuthorityRepository;
import com.example.manageeducation.userservice.repository.CustomerRepository;
import com.example.manageeducation.userservice.repository.RoleRepository;
import com.example.manageeducation.userservice.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private AuthorityRepository authorityRepository;

    @MockBean
    private CustomerServiceUtils customerServiceUtils;

    @MockBean
    private CustomerJdbc customerJdbc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomerService customerService;

    private final UUID customerId1 = UUID.randomUUID();
    private final UUID customerId2 = UUID.randomUUID();
    private final UUID customerId3 = UUID.randomUUID();
    private final UUID customerId4 = UUID.randomUUID();
    private final UUID customerId5 = UUID.randomUUID();
    private final UUID customerId6 = UUID.randomUUID();
    private final UUID customerId7 = UUID.randomUUID();
    private final UUID customerId8 = UUID.randomUUID();
    private final UUID customerId9 = UUID.randomUUID();
    private final UUID customerId10 = UUID.randomUUID();

    private final List<Customer> customers = new ArrayList<>(
            List.of(
                    Customer.builder()
                            .id(customerId1)
                            .email("customer1@example.com")
                            .avatar("https://example.com/avatar1.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(3600))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer One")
                            .birthday(new Date(90, 1, 15))
                            .gender(Gender.MALE)
                            .level("Gold")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId2)
                            .email("customer2@example.com")
                            .avatar("https://example.com/avatar2.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(7200))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Two")
                            .birthday(new Date(85, 5, 25))
                            .gender(Gender.FEMALE)
                            .level("Silver")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId3)
                            .email("customer3@example.com")
                            .avatar("https://example.com/avatar3.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(10800))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Three")
                            .birthday(new Date(92, 10, 30))
                            .gender(Gender.FEMALE)
                            .level("Bronze")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId4)
                            .email("customer4@example.com")
                            .avatar("https://example.com/avatar4.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(14400))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Four")
                            .birthday(new Date(88, 7, 8))
                            .gender(Gender.MALE)
                            .level("Gold")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId5)
                            .email("customer5@example.com")
                            .avatar("https://example.com/avatar5.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(18000))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Five")
                            .birthday(new Date(80, 3, 21))
                            .gender(Gender.FEMALE)
                            .level("Silver")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId6)
                            .email("customer6@example.com")
                            .avatar("https://example.com/avatar6.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(21600))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Six")
                            .birthday(new Date(95, 12, 11))
                            .gender(Gender.MALE)
                            .level("Gold")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId7)
                            .email("customer7@example.com")
                            .avatar("https://example.com/avatar7.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(25200))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Seven")
                            .birthday(new Date(89, 9, 4))
                            .gender(Gender.FEMALE)
                            .level("Silver")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.DEACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId8)
                            .email("customer8@example.com")
                            .avatar("https://example.com/avatar8.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(28800))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Eight")
                            .birthday(new Date(84, 6, 15))
                            .gender(Gender.FEMALE)
                            .level("Bronze")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.DEACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId9)
                            .email("customer9@example.com")
                            .avatar("https://example.com/avatar9.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(32400))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Nine")
                            .birthday(new Date(77, 11, 22))
                            .gender(Gender.MALE)
                            .level("Gold")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build(),

                    Customer.builder()
                            .id(customerId10)
                            .email("customer10@example.com")
                            .avatar("https://example.com/avatar10.png")
                            .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                            .expiredDate(Instant.now().plusSeconds(36000))
                            .createdDate(Instant.now())
                            .updatedDate(Instant.now())
                            .fullName("Customer Ten")
                            .birthday(new Date(90, 1, 5))
                            .gender(Gender.FEMALE)
                            .level("Silver")
                            .fsu(UUID.randomUUID())
                            .status(CustomerStatus.ACTIVE)
                            .otp(new OTP())
                            .build()
            )
    );

    @BeforeEach
    public void setUp() {
        List<Authority> authorities = new ArrayList<>(
                List.of(
                        Authority.builder()
                                .id(UUID.fromString("1bfd2c0c-5042-11ed-bdc3-0242ac120002"))
                                .permission("NO_ACCESS")
                                .resource("SYLLABUS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("2815056e-5042-11ed-bdc3-0242ac120002"))
                                .permission("NO_ACCESS")
                                .resource("LEARNING_MATERIAL")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("2a9b6440-5042-11ed-bdc3-0242ac120002"))
                                .permission("NO_ACCESS")
                                .resource("CLASS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("30051fac-5042-11ed-bdc3-0242ac120002"))
                                .permission("NO_ACCESS")
                                .resource("USER")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("4f0b4f08-4dee-11ed-bdc3-0242ac120002"))
                                .permission("FULL_ACCESS")
                                .resource("SYLLABUS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("59c30ff8-4dee-11ed-bdc3-0242ac120002"))
                                .permission("MODIFY")
                                .resource("SYLLABUS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("61c642c4-4dee-11ed-bdc3-0242ac120002"))
                                .permission("CREATE")
                                .resource("SYLLABUS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("67f0f02c-4dee-11ed-bdc3-0242ac120002"))
                                .permission("VIEW")
                                .resource("SYLLABUS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("6d81e5e6-4dee-11ed-bdc3-0242ac120002"))
                                .permission("FULL_ACCESS")
                                .resource("TRAINING_PROGRAM")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("72213048-4dee-11ed-bdc3-0242ac120002"))
                                .permission("MODIFY")
                                .resource("TRAINING_PROGRAM")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("7948057c-4dee-11ed-bdc3-0242ac120002"))
                                .permission("CREATE")
                                .resource("TRAINING_PROGRAM")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("7e6b83e8-5042-11ed-bdc3-0242ac120002"))
                                .permission("NO_ACCESS")
                                .resource("TRAINING_PROGRAM")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("7e7076a6-4dee-11ed-bdc3-0242ac120002"))
                                .permission("VIEW")
                                .resource("TRAINING_PROGRAM")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("82feadd2-4dee-11ed-bdc3-0242ac120002"))
                                .permission("FULL_ACCESS")
                                .resource("LEARNING_MATERIAL")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("8948ed60-4dee-11ed-bdc3-0242ac120002"))
                                .permission("MODIFY")
                                .resource("LEARNING_MATERIAL")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("9aba981e-4dee-11ed-bdc3-0242ac120002"))
                                .permission("CREATE")
                                .resource("LEARNING_MATERIAL")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("a055e742-4dee-11ed-bdc3-0242ac120002"))
                                .permission("VIEW")
                                .resource("LEARNING_MATERIAL")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("a447f106-4dee-11ed-bdc3-0242ac120002"))
                                .permission("FULL_ACCESS")
                                .resource("CLASS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("a7ed6d72-4dee-11ed-bdc3-0242ac120002"))
                                .permission("MODIFY")
                                .resource("CLASS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("abd89c5e-4dee-11ed-bdc3-0242ac120002"))
                                .permission("CREATE")
                                .resource("CLASS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("b04d483e-4dee-11ed-bdc3-0242ac120002"))
                                .permission("VIEW")
                                .resource("CLASS")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("b3ebfa26-4dee-11ed-bdc3-0242ac120002"))
                                .permission("FULL_ACCESS")
                                .resource("USER")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("b75c7bd6-4dee-11ed-bdc3-0242ac120002"))
                                .permission("MODIFY")
                                .resource("USER")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("ba8758a8-4dee-11ed-bdc3-0242ac120002"))
                                .permission("CREATE")
                                .resource("USER")
                                .build(),
                        Authority.builder()
                                .id(UUID.fromString("bf6cbb74-4dee-11ed-bdc3-0242ac120002"))
                                .permission("VIEW")
                                .resource("USER")
                                .build()
                )
        );

        Optional<Authority> authority1 = Optional.ofNullable(authorities.get(0));
        Optional<Authority> authority2 = Optional.ofNullable(authorities.get(1));
        Optional<Authority> authority3 = Optional.ofNullable(authorities.get(2));
        Optional<Authority> authority4 = Optional.ofNullable(authorities.get(3));
        Optional<Authority> authority5 = Optional.ofNullable(authorities.get(4));
        Optional<Authority> authority6 = Optional.ofNullable(authorities.get(5));
        Optional<Authority> authority7 = Optional.ofNullable(authorities.get(6));
        Optional<Authority> authority8 = Optional.ofNullable(authorities.get(7));
        Optional<Authority> authority9 = Optional.ofNullable(authorities.get(8));
        Optional<Authority> authority10 = Optional.ofNullable(authorities.get(9));
        Optional<Authority> authority11 = Optional.ofNullable(authorities.get(10));
        Optional<Authority> authority12 = Optional.ofNullable(authorities.get(11));
        Optional<Authority> authority13 = Optional.ofNullable(authorities.get(12));
        Optional<Authority> authority14 = Optional.ofNullable(authorities.get(13));
        Optional<Authority> authority15 = Optional.ofNullable(authorities.get(14));
        Optional<Authority> authority16 = Optional.ofNullable(authorities.get(15));
        Optional<Authority> authority17 = Optional.ofNullable(authorities.get(16));
        Optional<Authority> authority18 = Optional.ofNullable(authorities.get(17));
        Optional<Authority> authority19 = Optional.ofNullable(authorities.get(18));
        Optional<Authority> authority20 = Optional.ofNullable(authorities.get(19));
        Optional<Authority> authority21 = Optional.ofNullable(authorities.get(20));
        Optional<Authority> authority22 = Optional.ofNullable(authorities.get(21));
        Optional<Authority> authority23 = Optional.ofNullable(authorities.get(22));
        Optional<Authority> authority24 = Optional.ofNullable(authorities.get(23));
        Optional<Authority> authority25 = Optional.ofNullable(authorities.get(24));


        Mockito.when(authorityRepository.findById(UUID.fromString("1bfd2c0c-5042-11ed-bdc3-0242ac120002"))).thenReturn(authority1);
        Mockito.when(authorityRepository.findById(UUID.fromString("2815056e-5042-11ed-bdc3-0242ac120002"))).thenReturn(authority2);
        Mockito.when(authorityRepository.findById(UUID.fromString("2a9b6440-5042-11ed-bdc3-0242ac120002"))).thenReturn(authority3);
        Mockito.when(authorityRepository.findById(UUID.fromString("30051fac-5042-11ed-bdc3-0242ac120002"))).thenReturn(authority4);
        Mockito.when(authorityRepository.findById(UUID.fromString("4f0b4f08-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority5);
        Mockito.when(authorityRepository.findById(UUID.fromString("59c30ff8-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority6);
        Mockito.when(authorityRepository.findById(UUID.fromString("61c642c4-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority7);
        Mockito.when(authorityRepository.findById(UUID.fromString("67f0f02c-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority8);
        Mockito.when(authorityRepository.findById(UUID.fromString("6d81e5e6-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority9);
        Mockito.when(authorityRepository.findById(UUID.fromString("72213048-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority10);
        Mockito.when(authorityRepository.findById(UUID.fromString("7948057c-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority11);
        Mockito.when(authorityRepository.findById(UUID.fromString("7e6b83e8-5042-11ed-bdc3-0242ac120002"))).thenReturn(authority12);
        Mockito.when(authorityRepository.findById(UUID.fromString("7e7076a6-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority13);
        Mockito.when(authorityRepository.findById(UUID.fromString("82feadd2-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority14);
        Mockito.when(authorityRepository.findById(UUID.fromString("8948ed60-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority15);
        Mockito.when(authorityRepository.findById(UUID.fromString("9aba981e-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority16);
        Mockito.when(authorityRepository.findById(UUID.fromString("a055e742-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority17);
        Mockito.when(authorityRepository.findById(UUID.fromString("a447f106-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority18);
        Mockito.when(authorityRepository.findById(UUID.fromString("a7ed6d72-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority19);
        Mockito.when(authorityRepository.findById(UUID.fromString("abd89c5e-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority20);
        Mockito.when(authorityRepository.findById(UUID.fromString("b04d483e-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority21);
        Mockito.when(authorityRepository.findById(UUID.fromString("b3ebfa26-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority22);
        Mockito.when(authorityRepository.findById(UUID.fromString("b75c7bd6-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority23);
        Mockito.when(authorityRepository.findById(UUID.fromString("ba8758a8-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority24);
        Mockito.when(authorityRepository.findById(UUID.fromString("bf6cbb74-4dee-11ed-bdc3-0242ac120002"))).thenReturn(authority25);


        // Building Role data
        List<Role> roles = new ArrayList<>(
                List.of(
                        Role.builder()
                                .id(UUID.fromString("4436720a-4def-11ed-bdc3-0242ac120002"))
                                .name(RoleType.SUPER_ADMIN)
                                .authorities(Set.of(
                                        authority5.get(), authority6.get(), authority7.get(), authority8.get(), // SYLLABUS
                                        authority9.get(), authority10.get(), authority11.get(), authority13.get(), // TRAINING_PROGRAM
                                        authority14.get(), authority15.get(), authority16.get(), authority17.get(), // LEARNING_MATERIAL
                                        authority18.get(), authority19.get(), authority20.get(), authority21.get(), // CLASS
                                        authority22.get(), authority23.get(), authority24.get(), authority25.get() // USER
                                ))
                                .build(),

                        Role.builder()
                                .id(UUID.fromString("9821f52e-4def-11ed-bdc3-0242ac120002"))
                                .name(RoleType.TRAINER)
                                .authorities(Set.of(
                                        authority6.get(), authority7.get(), authority8.get(), // SYLLABUS
                                        authority10.get(), authority11.get(), authority13.get(), // TRAINING_PROGRAM
                                        authority14.get(), authority15.get(), authority16.get(), authority17.get(), // LEARNING_MATERIAL
                                        authority18.get(), authority19.get(), authority20.get(), authority21.get() // CLASS
                                ))
                                .build(),

                        Role.builder()
                                .id(UUID.fromString("9f64df36-4def-11ed-bdc3-0242ac120002"))
                                .name(RoleType.CLASS_ADMIN)
                                .authorities(Set.of(
                                        authority8.get(), // SYLLABUS
                                        authority11.get(), authority13.get(), // TRAINING_PROGRAM
                                        authority14.get(), authority15.get(), authority16.get(), authority17.get(), // LEARNING_MATERIAL
                                        authority18.get(), authority19.get(), authority20.get(), authority21.get() // CLASS
                                ))
                                .build(),

                        Role.builder()
                                .id(UUID.fromString("a358a9e2-48a7-48ea-8f1b-93591130da1d"))
                                .name(RoleType.GUEST)
                                .authorities(Set.of(
                                        authority1.get(), authority2.get(), authority3.get(), authority4.get() // All NO_ACCESS authorities
                                ))
                                .build(),

                        Role.builder()
                                .id(UUID.fromString("b424387c-4def-11ed-bdc3-0242ac120002"))
                                .name(RoleType.STUDENT)
                                .authorities(Set.of(
                                        authority4.get(), // USER NO_ACCESS
                                        authority11.get(), authority13.get(), // TRAINING_PROGRAM
                                        authority16.get(), authority17.get(), // LEARNING_MATERIAL
                                        authority20.get(), authority21.get() // CLASS
                                ))
                                .build()
                )
        );

        Optional<Role> role1 = Optional.ofNullable(roles.get(0));
        Optional<Role> role2 = Optional.ofNullable(roles.get(1));
        Optional<Role> role3 = Optional.ofNullable(roles.get(2));
        Optional<Role> role4 = Optional.ofNullable(roles.get(3));
        Optional<Role> role5 = Optional.ofNullable(roles.get(4));

        Mockito.when(roleRepository.findById(UUID.fromString("4436720a-4def-11ed-bdc3-0242ac120002"))).thenReturn(role1);
        Mockito.when(roleRepository.findById(UUID.fromString("9821f52e-4def-11ed-bdc3-0242ac120002"))).thenReturn(role2);
        Mockito.when(roleRepository.findById(UUID.fromString("9f64df36-4def-11ed-bdc3-0242ac120002"))).thenReturn(role3);
        Mockito.when(roleRepository.findById(UUID.fromString("a358a9e2-48a7-48ea-8f1b-93591130da1d"))).thenReturn(role4);
        Mockito.when(roleRepository.findById(UUID.fromString("b424387c-4def-11ed-bdc3-0242ac120002"))).thenReturn(role5);


        // Building Role data
        List<Customer> customers = new ArrayList<>(
                List.of(
                        Customer.builder()
                                .id(customerId1)
                                .email("customer1@example.com")
                                .avatar("https://example.com/avatar1.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(3600))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer One")
                                .birthday(new Date(90, 1, 15))
                                .gender(Gender.MALE)
                                .level("Gold")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role1.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId2)
                                .email("customer2@example.com")
                                .avatar("https://example.com/avatar2.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(7200))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Two")
                                .birthday(new Date(85, 5, 25))
                                .gender(Gender.FEMALE)
                                .level("Silver")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role2.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId3)
                                .email("customer3@example.com")
                                .avatar("https://example.com/avatar3.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(10800))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Three")
                                .birthday(new Date(92, 10, 30))
                                .gender(Gender.FEMALE)
                                .level("Bronze")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role3.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId4)
                                .email("customer4@example.com")
                                .avatar("https://example.com/avatar4.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(14400))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Four")
                                .birthday(new Date(88, 7, 8))
                                .gender(Gender.MALE)
                                .level("Gold")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role4.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId5)
                                .email("customer5@example.com")
                                .avatar("https://example.com/avatar5.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(18000))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Five")
                                .birthday(new Date(80, 3, 21))
                                .gender(Gender.FEMALE)
                                .level("Silver")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role5.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId6)
                                .email("customer6@example.com")
                                .avatar("https://example.com/avatar6.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(21600))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Six")
                                .birthday(new Date(95, 12, 11))
                                .gender(Gender.MALE)
                                .level("Gold")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role5.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId7)
                                .email("customer7@example.com")
                                .avatar("https://example.com/avatar7.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(25200))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Seven")
                                .birthday(new Date(89, 9, 4))
                                .gender(Gender.FEMALE)
                                .level("Silver")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.DEACTIVE)
                                .role(role5.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId8)
                                .email("customer8@example.com")
                                .avatar("https://example.com/avatar8.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(28800))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Eight")
                                .birthday(new Date(84, 6, 15))
                                .gender(Gender.FEMALE)
                                .level("Bronze")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.DEACTIVE)
                                .role(role5.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId9)
                                .email("customer9@example.com")
                                .avatar("https://example.com/avatar9.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(32400))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Nine")
                                .birthday(new Date(77, 11, 22))
                                .gender(Gender.MALE)
                                .level("Gold")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role5.get())
                                .otp(new OTP())
                                .build(),

                        Customer.builder()
                                .id(customerId10)
                                .email("customer10@example.com")
                                .avatar("https://example.com/avatar10.png")
                                .password("$2a$10$1JPmKGP2iwftlMdeqg5NDeHCj88lPncIT7lTGWR9jh0fZUIL7qvrK")
                                .expiredDate(Instant.now().plusSeconds(36000))
                                .createdDate(Instant.now())
                                .updatedDate(Instant.now())
                                .fullName("Customer Ten")
                                .birthday(new Date(90, 1, 5))
                                .gender(Gender.FEMALE)
                                .level("Silver")
                                .fsu(UUID.randomUUID())
                                .status(CustomerStatus.ACTIVE)
                                .role(role5.get())
                                .otp(new OTP())
                                .build()
                )
        );

        Optional<Customer> customer1 = Optional.ofNullable(customers.get(0));
        Optional<Customer> customer2 = Optional.ofNullable(customers.get(1));
        Optional<Customer> customer3 = Optional.ofNullable(customers.get(2));
        Optional<Customer> customer4 = Optional.ofNullable(customers.get(3));
        Optional<Customer> customer5 = Optional.ofNullable(customers.get(4));
        Optional<Customer> customer6 = Optional.ofNullable(customers.get(5));
        Optional<Customer> customer7 = Optional.ofNullable(customers.get(6));
        Optional<Customer> customer8 = Optional.ofNullable(customers.get(7));
        Optional<Customer> customer9 = Optional.ofNullable(customers.get(8));
        Optional<Customer> customer10 = Optional.ofNullable(customers.get(9));

        Mockito.when(customerRepository.findById(customerId2)).thenReturn(customer1);
        Mockito.when(customerRepository.findById(customerId2)).thenReturn(customer2);
        Mockito.when(customerRepository.findById(customerId3)).thenReturn(customer3);
        Mockito.when(customerRepository.findById(customerId4)).thenReturn(customer4);
        Mockito.when(customerRepository.findById(customerId5)).thenReturn(customer5);
        Mockito.when(customerRepository.findById(customerId6)).thenReturn(customer6);
        Mockito.when(customerRepository.findById(customerId7)).thenReturn(customer7);
        Mockito.when(customerRepository.findById(customerId8)).thenReturn(customer8);
        Mockito.when(customerRepository.findById(customerId9)).thenReturn(customer9);
        Mockito.when(customerRepository.findById(customerId1)).thenReturn(customer10);
        Mockito.when(customerRepository.findAll()).thenReturn(customers);

    }

    @Test
    @DisplayName("Get customer by keyword empty not sort")
    public void getCustomerByKeywordEmptyNotSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("", 1, 10, null, null);
        Page<Customer> results = new PageImpl<>(customers);
        Mockito.when(customerRepository.findAll(any(Pageable.class))).thenReturn(results);

        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("Get customer by keyword empty sort")
    public void getCustomerByKeywordEmptySort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSortingAllCustomers(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword name not sort name")
    public void getCustomerByKeywordNameNotSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("two", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestions(request.getPage(), request.getSize(),
                request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword name sort name")
    public void getCustomerByKeywordNameSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("one", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType(), request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword email not sort name")
    public void getCustomerByKeywordEmailNotSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("customer8@example.c", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestions(request.getPage(), request.getSize(),
                request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword email sort name")
    public void getCustomerByKeywordEmailSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("customer8@example.c", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType(), request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword gender not sort name")
    public void getCustomerByKeywordGenderNotSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("FEMALE", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestions(request.getPage(), request.getSize(),
                request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword gender sort name")
    public void getCustomerByKeywordGenderSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("FEMALE", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType(), request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword level not sort name")
    public void getCustomerByKeywordLevelNotSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("Gold", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestions(request.getPage(), request.getSize(),
                request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword level sort name")
    public void getCustomerByKeywordLevelSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("Gold", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType(), request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword status not sort name")
    public void getCustomerByKeywordStatusNotSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("DEACTIVE", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestions(request.getPage(), request.getSize(),
                request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("Get customer by keyword status sort name")
    public void getCustomerByKeywordStatusSort() {
        RequestForListOfCustomer request = new RequestForListOfCustomer("ACTIVE", 1, 10, "NAME", "DESC");
        String sql = customerServiceUtils.getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage(), request.getSize(),
                request.getSortBy(), request.getSortType(), request.getKeyword());
        Mockito.when(customerJdbc.getCustomers(sql)).thenReturn(customers);

        List<CustomerResponse> expectedList = customers.stream()
                .map(syllabus -> modelMapper.map(syllabus, CustomerResponse.class))
                .collect(Collectors.toList());
        ResponseEntity<ResponseObject> response = customerService.getAllCustomers(request);
        List<CustomerResponse> actualList = (List<CustomerResponse>) response.getBody().getData();
        Assertions.assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    @DisplayName("De-active customer information")
    public void deactiveCustomer(){
        try{
            String update = customerService.deActiveCustomer(customerId10);
            Assertions.assertThat(update).isEqualTo("De-active customer successful.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete customer information")
    public void deleteCustomer(){
        try{
            String update = customerService.deleteCustomer(customerId10);
            Assertions.assertThat(update).isEqualTo("Delete customer successful.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Update customer information")
    public void updateCustomerInformation(){
        try{
            CustomerUpdateRequest customerUpdate = CustomerUpdateRequest.builder()
                    .email("customer100@example.com")
                    .fullName("Anh Bang")
                    .birthday(new Date(90, 1, 5))
                    .gender(Gender.MALE)
                    .level("Gold")
                    .build();
            String update = customerService.updateCustomer(customerId10,customerUpdate);
            Assertions.assertThat(update).isEqualTo("Update customer successful.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create customer with condition password is large than or equal 6")
    public void createCustomerWithPasswordSmallThanSix(){
        try{
            CustomerImportRequest customer = CustomerImportRequest.builder()
                    .email("customer109@example.com")
                    .password("1234p")
                    .fullName("Thanh Thang")
                    .birthday(new Date(90, 1, 5))
                    .gender(Gender.FEMALE)
                    .role(RoleType.STUDENT)
                    .level("Silver")
                    .build();
            String update = customerService.createUser(customer);
            Assertions.assertThat(update).isEqualTo("Password is large than or equal 6.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create customer with condition email is existed.")
    public void createCustomerWithConditionEmailExisted(){
        try{
            CustomerImportRequest customer = CustomerImportRequest.builder()
                    .email("customer10@example.com")
                    .password("1234p")
                    .fullName("Thanh Thang")
                    .birthday(new Date(90, 1, 5))
                    .gender(Gender.FEMALE)
                    .role(RoleType.STUDENT)
                    .level("Silver")
                    .build();
            String update = customerService.createUser(customer);
            Assertions.assertThat(update).isEqualTo("Email is existed.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create customer successfully")
    public void createCustomerInformation(){
        try{
            CustomerImportRequest customer = CustomerImportRequest.builder()
                    .email("customer180@example.com")
                    .password("1234p90")
                    .fullName("Tan Khang")
                    .birthday(new Date(90, 1, 5))
                    .gender(Gender.FEMALE)
                    .role(RoleType.STUDENT)
                    .level("Silver")
                    .build();
            String update = customerService.createUser(customer);
            Assertions.assertThat(update).isEqualTo("Create successful.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create customer successfully with file excel")
    public void createCustomerWithFileExcel(){
        try{
            Path filePath = Paths.get("user-service/src/main/resources/ImportTemplate.xlsx");
            File file = filePath.toFile();
            String update = customerService.createCustomerByExcel((MultipartFile) file);
            Assertions.assertThat(update).isEqualTo("Create customer successful.");
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

}
