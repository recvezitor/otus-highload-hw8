package com.dimas.core.service;

import com.dimas.core.domain.entity.Person;
import com.dimas.core.domain.mapper.PersonMapper;
import com.dimas.core.api.model.ApiUser;
import com.dimas.core.domain.PersonCreate;
import com.dimas.core.persistence.PersonRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.dimas.core.util.SecurityUtil.encrypt;
import static com.dimas.core.util.SecurityUtil.validPassword;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final TokenService tokenService;
    private final AuthService authService;
    private final RabbitService rabbitService;
    private final WebsocketService websocketService;

    public Uni<Person> findById(UUID id) {
        return personRepository.getById(id);
    }

    public Uni<String> login(UUID id, String password) {
        return personRepository.getById(id)
                .flatMap(person -> handleToken(person, password))
                .call(token -> personRepository.findFriends(id)
                        .flatMap(friends -> handleSubscription(id, friends))
                        .replaceWithVoid());
    }

    private Uni<Void> handleSubscription(UUID subscriberId, List<Person> friends) {
        log.debug("handleSubscription {} friends", friends.size());
        return Multi.createFrom().iterable(friends)
                .onItem().transformToUni(friend ->
                        rabbitService.addSubscription(subscriberId, friend.getId(), websocketService)
                )
                .concatenate().collect().asList()
                .replaceWithVoid();
    }


    private Uni<String> handleToken(Person person, String password) {
        log.debug("Person is found={}", person);
        if (!validPassword(person.getPassword(), password)) {
            return Uni.createFrom().failure(new ForbiddenException("Неверный логин или пароль"));
        }
        return tokenService.generate(person.getId(), null)//move tokenService to authservice
                .onItem().call(token -> authService.save(person.getId(), token));
    }

    public Uni<Person> findByName(String name) {
        return personRepository.getByName(name);
    }

    public Uni<Person> create(PersonCreate request) {
        var person = personMapper.map(request)
                .withPassword(encrypt(request.getPassword()));
        return personRepository.create(person);
    }

    public Uni<List<ApiUser>> search(String firstName, String lastName) {
        return personRepository.search(firstName, lastName)
                .map(list -> list.stream().map(personMapper::map).toList());
    }


    public Uni<Integer> findByCity(String city) {
        return personRepository.findByCity(city);
    }

}
