package joboffers.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginAndRegisterConfiguration {

    @Bean
    public LoginAndRegisterFacade loginAndRegisterFacade(UserRepository repository){
        return new LoginAndRegisterFacade(repository,new UserMapper(new IdGenerator()));
    }

}
