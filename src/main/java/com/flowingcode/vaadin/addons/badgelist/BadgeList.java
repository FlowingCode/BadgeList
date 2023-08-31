/*-
 * #%L
 * Badge List Add-on
 * %%
 * Copyright (C) 2023 Flowing Code
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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import java.util.ArrayList;
import java.util.List;

/**
 * Component that represents a responsive list of badges.
 *
 * @author Paola De Bartolo / Flowing Code
 */
@SuppressWarnings("serial")
@JsModule("@vaadin/vaadin-lumo-styles/badge-global.js")
@JsModule("./src/fc-badge-list.ts")
@Tag("fc-badge-list")
public class BadgeList extends Component implements HasTheme, HasSize {

  private List<Badge> badges = new ArrayList<>();

  /**
   * Creates a new instance of BadgeList with the supplied list of {@link Badge badges}.
   *
   * @param badges list of badges
   */
  public BadgeList(List<Badge> badges) {
    this.badges = badges;
    this.addBadges();
  }

  private void addBadges() {
    this.badges.forEach(
        badge -> {
          badge.getElement().setAttribute("slot", "badges");
          this.getElement().appendChild(badge.getElement());
        });
  }
}
