package com.flowingcode.vaadin.addons.badgelist;

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DemoSource
@PageTitle("Read Only Binder Demo")
@SuppressWarnings("serial")
@Route(value = "badge-list/readonly", layout = BadgeListDemoView.class)
public class ReadOnlyBinderDemo extends BaseBadgeListDemo {

  private TextField firstName = new TextField("First Name");
  private TextField lastName = new TextField("Last Name");
  private MultiSelectComboBox<String> rolesComboBox =
      new MultiSelectComboBox<String>("Select Roles");
  private BadgeList rolesBadgeList = new BadgeList();
  private ReadOnlyHasValue<List<Badge>> readonlyBadgeList;
  private Div rolesBadgeListDiv = new Div();
  private Button editModeButton = new Button("Edit Mode");
  private Button readOnlyModeButton = new Button("Read Only Mode");
  private Binder<Person> binder;

  public ReadOnlyBinderDemo() {

    Person person = TestData.singlePerson();

    List<String> roles =
        IntStream.rangeClosed(1, 12).mapToObj(i -> "ROLE" + i).collect(Collectors.toList());
    rolesComboBox.setItems(roles);

    rolesBadgeList = new BadgeList();
    readonlyBadgeList = new ReadOnlyHasValue<List<Badge>>(rolesBadgeList::setBadges);

    binder = new Binder<>();
    binder.bind(firstName, Person::getFirstName, Person::setFirstName);
    binder.bind(lastName, Person::getLastName, Person::setLastName);
    readOnlyMode();
    binder.setBean(person);

    editModeButton.addClickListener(e -> editMode());
    readOnlyModeButton.addClickListener(e -> readOnlyMode());

    HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.add(editModeButton, readOnlyModeButton);
    buttonsLayout.setMargin(true);
    buttonsLayout.setJustifyContentMode(JustifyContentMode.END);

    VerticalLayout layout = new VerticalLayout();
    Span span = new Span("Roles");
    span.addClassName("readonly-badge-list-label");
    rolesBadgeListDiv.add(span);
    rolesBadgeListDiv.setWidth("450px");
    rolesBadgeListDiv.add(rolesBadgeList);
    layout.add(firstName, lastName, rolesComboBox, rolesBadgeListDiv);

    add(layout, buttonsLayout);
  }

  private void editMode() {
    editModeButton.setEnabled(false);
    readOnlyModeButton.setEnabled(true);
    rolesBadgeListDiv.setVisible(false);
    binder.removeBinding(readonlyBadgeList);
    rolesComboBox.setVisible(true);
    binder.bind(rolesComboBox, person -> new HashSet<String>(person.getRoles()),
        (person, roles) -> {
          person.setRoles(new ArrayList<String>(roles));
        });
    binder.setReadOnly(false);
  }

  private void readOnlyMode() {
    readOnlyModeButton.setEnabled(false);
    editModeButton.setEnabled(true);
    rolesComboBox.setVisible(false);
    binder.removeBinding(rolesComboBox);
    rolesBadgeListDiv.setVisible(true);
    binder.forField(readonlyBadgeList).bindReadOnly(person -> {
      return person.getRoles().stream()
          .collect(Collectors.mapping(role -> new Badge(role), Collectors.toList()));
    });
    binder.setReadOnly(true);
  }
}
