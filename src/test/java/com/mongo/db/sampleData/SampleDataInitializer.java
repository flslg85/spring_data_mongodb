package com.mongo.db.sampleData;

import com.mongo.db.user.User;
import com.mongo.db.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@ActiveProfiles("test")
public class SampleDataInitializer {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository.saveAll(Arrays.asList(
                new User().setName("alice").setAge(10),
                new User().setName("bob").setAge(10),
                new User().setName("cyndi").setAge(10),
                new User().setName("diana").setAge(10)
        ));
    }
}
