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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@DemoSource
@PageTitle("Basic Demo")
@SuppressWarnings("serial")
@Route(value = "badge-list/basic", layout = BadgeListDemoView.class)
public class BadgeListDemo extends BaseBadgeListDemo {

  public BadgeListDemo() {
    Grid<Person> grid = new Grid<>(Person.class, false);
    grid.setItems(TestData.initializeData());

    grid.addColumn(Person::getFirstName).setHeader("First Name").setAutoWidth(true).setFlexGrow(0);
    grid.addColumn(Person::getLastName).setHeader("Last Name").setAutoWidth(true).setFlexGrow(0);
    grid.addComponentColumn(
        person -> {
          List<Badge> badges = new ArrayList<>();
          person.getRoles().forEach(role -> badges.add(new Badge(role)));
          return new BadgeList(badges);
        })
    .setHeader("Roles")
    .setResizable(true);
    grid.addColumn(Person::getEmailAddress).setHeader("Email").setAutoWidth(true).setFlexGrow(0);
    grid.addColumn(Person::getPhoneNumber)
        .setHeader("Phone number")
        .setAutoWidth(true)
        .setFlexGrow(0);
    grid.addColumn(Person::getTitle).setHeader("Title");

    grid.setWidthFull();
    // show-source add(grid);
    // #if vaadin eq 0
    add(
        this.createContainerDiv(
            "BadgeList as part of resizable column 'Roles'", grid)); 
    // #endif
  }
}
