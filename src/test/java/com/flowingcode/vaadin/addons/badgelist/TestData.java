/*-
 * #%L
 * Badge List Add-on
 * %%
 * Copyright (C) 2023 - 2024 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.badgelist;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestData {

  private static final Faker faker = new Faker();

  private static synchronized Person newPerson() {
    return Person.builder()
        .active(faker.random().nextBoolean())
        .firstName(faker.name().firstName())
        .lastName(faker.name().lastName())
        .phoneNumber(faker.phoneNumber().cellPhone())
        .emailAddress(faker.internet().emailAddress())
        .title(faker.name().title())
        .roles(generateRoles())
        .build();
  }

  public static List<Person> initializeData() {
    return Stream.generate(TestData::newPerson).limit(8).collect(Collectors.toList());
  }
  
  public static Person singlePerson() {
    return newPerson();
  }

  private static List<String> generateRoles() {
    List<String> roles = new ArrayList<>();
    int limit = faker.random().nextInt(6, 12);
    for (int i = 0; i < limit; i++) {
      roles.add("ROLE" + (i + 1));
    }
    return roles;
  }
}
